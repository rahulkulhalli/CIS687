package org.syr.cis687.service_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.syr.cis687.enums.CurrentState;
import org.syr.cis687.enums.OperatingState;
import org.syr.cis687.models.*;
import org.syr.cis687.repository.ShuttleRepository;
import org.syr.cis687.repository.ShuttleScheduleRepository;
import org.syr.cis687.repository.ShuttleStopRepository;
import org.syr.cis687.repository.StudentRepository;
import org.syr.cis687.service.ShuttleService;
import org.syr.cis687.utils.CommonUtils;
import org.syr.cis687.utils.LocationUtils;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ShuttleServiceImpl implements ShuttleService {

    @Autowired
    private ShuttleRepository repository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ShuttleScheduleRepository scheduleRepository;

    @Autowired
    private ShuttleStopRepository stopRepository;

    private Shuttle getShuttle() {
        List<Shuttle> shuttles = getAllShuttles();
        return !shuttles.isEmpty() ? shuttles.get(0) : null;
    }

    @Override
    public List<Shuttle> getAllShuttles() {
        return CommonUtils.convertIterableToList(repository.findAll());
    }

    @Override
    public Optional<Shuttle> getShuttleById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Shuttle addShuttle(Shuttle shuttle) {
        // first, check if a shuttle already exists.
        List<Shuttle> shuttleList = CommonUtils.convertIterableToList(this.repository.findAll());

        if (!shuttleList.isEmpty()) {
            // This won't do, because we only allow a single shuttle to be added..
            return null;
        }

        return this.repository.save(shuttle);
    }

    @Override
    public Shuttle addStudentToShuttle(String studentId) {

        // First, get the shuttle.
        List<Shuttle> shuttleList = CommonUtils.convertIterableToList(this.repository.findAll());

        if (shuttleList.isEmpty()) {
            return null;
        }

        List<Student> students = this.studentRepository.findByOrgId(studentId);
        if (students == null || students.isEmpty()) {
            return null;
        }

        // Retrieve the first shuttle.
        Shuttle shuttle = shuttleList.get(0);

        // Does this student exist in the shuttle already?
        if (CommonUtils.isStudentOnShuttle(shuttle, studentId)) {
            // No need to do anything, just return the shuttle as-is.
            return shuttle;
        }

        // Retrieve the student.
        Student student = students.get(0);

        shuttle.getPassengerList().add(student);

        student.setShuttle(shuttle);

        // persist in db
        this.repository.save(shuttle);

        return shuttle;
    }

    @Override
    public Shuttle removeStudentFromShuttle(String studentId) {
        // First, get the shuttle.
        List<Shuttle> shuttleList = CommonUtils.convertIterableToList(this.repository.findAll());

        if (shuttleList.isEmpty()) {
            return null;
        }

        List<Student> students = this.studentRepository.findByOrgId(studentId);
        if (students == null || students.isEmpty()) {
            return null;
        }

        // Retrieve the first shuttle.
        Shuttle shuttle = shuttleList.get(0);

        // Retrieve the student.
        Student student = students.get(0);

        if (CommonUtils.isStudentOnShuttle(shuttle, studentId)) {
            // Remember, this is a bidirectional relation. So, dereference the shuttle too.
            shuttle.getPassengerList().remove(student);
            student.setShuttle(null);
        }

        // persist in db
        this.repository.save(shuttle);

        return shuttle;
    }

    @Override
    public Shuttle startTrip() {
        return null;
    }

    /**
     * Poll every 10 minutes and check if the time is between (start + 10 minutes) or (end + 10 minutes)
     * We care about the buffer because in the worst case, the polling can begin at x:59:59. If that is the
     * case, the next polling will happen at (x+1):09:59.
     */
    @Scheduled(fixedRate = 600000)
    public void pollStartAndEnd() {
        Shuttle shuttle = getShuttle();
        if (shuttle == null) {
            return;
        }

        if (shuttle.getOperatingState() == OperatingState.MAINTENANCE) {
            return;
        }

        long dayIndex = Integer.valueOf(LocalDate.now().getDayOfWeek().ordinal() + 1).longValue();
        ShuttleSchedule schedule = this.scheduleRepository.findById(dayIndex).orElse(null);

        if (schedule == null) {
            return;
        }

        Time startTime = schedule.getStartTime();
        Time startBuffer = Time.valueOf(startTime.toLocalTime().plusMinutes(10L));

        Time endTime = schedule.getEndTime();
        Time endBuffer = Time.valueOf(endTime.toLocalTime().plusMinutes(10L));

        // Get the current time.
        Time currentTime = Time.valueOf(LocalTime.now());

        // start time.
        if (currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(startBuffer) <= 0) {

            // get shuttle location.
            ShuttleStop stop = CommonUtils.convertIterableToList(this.stopRepository.findAll()).get(0);

            // switch the boolean flag, set the arrival time and location.
            shuttle.setHasArrivedAtStop(true);
            shuttle.setOperatingState(OperatingState.OPERATIONAL);
            shuttle.setArrivalTime(Time.valueOf(LocalTime.now()));
            shuttle.setCurrentLocation(stop.getShuttleStopLocation());

            // Calculate HOW late. If > 5 minutes, set as LATE. Otherwise, set as ON_TIME.
            if (Math.abs(currentTime.getTime() - startTime.getTime()) > 300000L) {
                shuttle.setCurrentState(CurrentState.LATE);
            } else {
                shuttle.setCurrentState(CurrentState.ON_TIME);
            }

            // persist changes.
            this.repository.save(shuttle);
            return;
        }

        // end time.
        if (currentTime.compareTo(endTime) >= 0 && currentTime.compareTo(endBuffer) <= 0) {
            // switch the boolean flag.
            shuttle.setOperatingState(OperatingState.NON_OPERATIONAL);

            // persist changes.
            this.repository.save(shuttle);
            return;
        }
    }

    /**
     * Poll every 10 minutes. if the shuttle has been marked to have arrived at the stop,
     */
    @Scheduled(fixedRate = 600000)
    public void checkForStart() {
        Shuttle shuttle = getShuttle();
        if (shuttle == null) {
            return;
        }

        if (shuttle.getPassengerList().isEmpty()) {
            return;
        }

        if (shuttle.getHasArrivedAtStop()
                && shuttle.getOperatingState() == OperatingState.OPERATIONAL) {

            Time currentTime = Time.valueOf(LocalTime.now());

            // check if shuttle is full or 15 minutes have elapsed..
            if (shuttle.getPassengerList().size() == shuttle.getMaxCapacity()
                    || (currentTime.getTime() - shuttle.getArrivalTime().getTime() >= 900000L)) {

                // mark for departure.
                shuttle.setArrivalTime(null);
                shuttle.setHasArrivedAtStop(false);
                shuttle.setHasDepartedFromStop(true);
                shuttle.setDepartureTime(currentTime);

                // persist changes.
                this.repository.save(shuttle);
            }
        }
    }

    @Scheduled(fixedRate = 10000)
    public void tripSimulation() {
        Shuttle shuttle = getShuttle();

        if (shuttle == null) {
            return;
        }

        if (!shuttle.getHasDepartedFromStop()) {
            return;
        }

        Location destination;

        if (shuttle.getPassengerList().isEmpty()) {
            if (shuttle.getOperatingState() == OperatingState.NON_OPERATIONAL) {
                // Why do we check for non-operational state?
                // Having no students, being in the departed state, and having a non_operational
                // flag means that it is past the shuttle's operating hours and the last student has been
                // dropped. So we don't need to go back to the stop.
                return;
            } else {
                destination = CommonUtils.convertIterableToList(
                        this.stopRepository.findAll()
                ).get(0).getShuttleStopLocation();
            }
        } else {
            destination = shuttle.getPassengerList().get(0).getAddress();
        }

        Location currentLocation = shuttle.getCurrentLocation();

        if (currentLocation.getLatitude().equals(destination.getLatitude())
                && currentLocation.getLongitude().equals(destination.getLongitude())) {
            // This means that the shuttle has reached a student's destination.

            // Pop the student off the queue.
            shuttle.getPassengerList().remove(0);

            // Set timeSinceLastStop to 0.
            shuttle.setTimeSinceLastStop(0L);

            // Persist to db.
            this.repository.save(shuttle);

            // Don't do anything else in this time cycle.
            return;
        }

        // Update time since last stop. (Add 10 seconds.)
        shuttle.setTimeSinceLastStop(shuttle.getTimeSinceLastStop() + 10L);

        // How far have you traveled since your last stop?
        // Speed is m/h. So first, convert timeSinceLastStop to hours.
        double timeOffsetHours = shuttle.getTimeSinceLastStop().doubleValue() / 3600.0;

        // s = d/t (this value will be in miles).
        double distanceTraveled = shuttle.getCurrentSpeed() / timeOffsetHours;

        double travelRatio = distanceTraveled / LocationUtils.calculateHaversineDistance(currentLocation, destination);

        Location interpolated = LocationUtils.interpolate(currentLocation, destination, travelRatio);

        // Set the updated location.
        shuttle.setCurrentLocation(interpolated);

        // persist in db.
        this.repository.save(shuttle);
    }

    @Override
    public Shuttle updateShuttle(Long id, Shuttle shuttle) {
        if (!repository.existsById(id)) {
            return null;
        }

        Optional<Shuttle> optShuttle = getShuttleById(id);
        if (optShuttle.isEmpty()) {
            return null;
        }

        Shuttle dbShuttleObj = optShuttle.get();

        dbShuttleObj.setAlias(shuttle.getAlias());
        dbShuttleObj.setMaxCapacity(shuttle.getMaxCapacity());
        dbShuttleObj.setCurrentCapacity(shuttle.getCurrentCapacity());
        dbShuttleObj.setCurrentLocation(shuttle.getCurrentLocation());
        dbShuttleObj.setOperatingState(shuttle.getOperatingState());
        dbShuttleObj.setCurrentState(shuttle.getCurrentState());
        dbShuttleObj.setPassengerList(shuttle.getPassengerList());

        // persist in db.
        addShuttle(dbShuttleObj);

        return dbShuttleObj;
    }

    @Override
    public boolean deleteShuttle(Long id) {
        try {
            return this.repository.deleteByIdAndReturnCount(id) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Location getCurrentShuttleLocation() {
        // First, get the shuttle.
        List<Shuttle> shuttles = CommonUtils.convertIterableToList(this.repository.findAll());

        if (shuttles.isEmpty()) {
            return null;
        }

        // Get the first shuttle and return its location.
        return shuttles.get(0).getCurrentLocation();
    }

    @Override
    public Shuttle markShuttleArrival() {
        Shuttle shuttle = getShuttle();
        if (shuttle == null) {
            return null;
        }

        if (shuttle.getHasArrivedAtStop()) {
            System.out.println("Shuttle has already arrived!");
            return null;
        }

        shuttle.setHasArrivedAtStop(true);
        shuttle.setHasDepartedFromStop(false);

        // persist changes/
        return this.repository.save(shuttle);
    }

    @Override
    public Shuttle markShuttleDeparture() {
        Shuttle shuttle = getShuttle();
        if (shuttle == null) {
            return null;
        }

        if (shuttle.getHasDepartedFromStop()) {
            System.out.println("Shuttle has already departed!");
            return null;
        }

        shuttle.setHasDepartedFromStop(true);
        shuttle.setHasArrivedAtStop(false);

        // persist changes/
        return this.repository.save(shuttle);
    }
}
