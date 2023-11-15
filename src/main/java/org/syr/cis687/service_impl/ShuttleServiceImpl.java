package org.syr.cis687.service_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.syr.cis687.enums.CurrentState;
import org.syr.cis687.enums.OperatingState;
import org.syr.cis687.models.*;
import org.syr.cis687.repository.*;
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

    private static final Long TRIGGER_EVERY = 10L;

    @Autowired
    private ShuttleRepository repository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ShuttleScheduleRepository scheduleRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ShuttleStopRepository stopRepository;

    private double distanceAB;
    private double estimatedDuration;
    private int totalIntervals;
    private double intervalFactor;

    private Shuttle getShuttle() {
        List<Shuttle> shuttles = getAllShuttles();
        return !shuttles.isEmpty() ? shuttles.get(0) : null;
    }

    private void initializeTrackingVariables(Location start, Location end) {

        double speed = 18;
        this.distanceAB = LocationUtils.calculateHaversineDistance(start, end);

        // calculate duration using s=d/t. d=m, s=m/h, t=h * 3600 = seconds.
        this.estimatedDuration = this.distanceAB / speed * 3600.0;

        // total intervals.
        this.totalIntervals = (int) (this.estimatedDuration / TRIGGER_EVERY);
        this.intervalFactor = 1.0 / this.totalIntervals;
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

        // get the 0th location.
        Location location = this.locationRepository.findById(1L).orElse(null);
        shuttle.setCurrentLocation(location);

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

        if (shuttle.getHasDepartedFromStop()) {
            // Cannot add more students once the shuttle departs!
            // TODO: Compute the ETA.
            return null;
        }

        // Does this student exist in the shuttle already?
        if (CommonUtils.isStudentOnShuttle(shuttle, studentId)) {
            // No need to do anything, just return the shuttle as-is.
            return shuttle;
        }

        if (shuttle.getPassengerList().size() == shuttle.getMaxCapacity()) {
            // this means that we can't have additional students.
            return null;
        }

        // Update the current capacity.
        shuttle.setCurrentCapacity(shuttle.getCurrentCapacity() + 1);

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

        // Retrieve the first shuttle.
        Shuttle shuttle = shuttleList.get(0);

        if  (shuttle.getHasDepartedFromStop()) {
            // Cannot remove student.
            return null;
        }

        List<Student> students = this.studentRepository.findByOrgId(studentId);
        if (students == null || students.isEmpty()) {
            return null;
        }

        // Retrieve the student.
        Student student = students.get(0);

        if (CommonUtils.isStudentOnShuttle(shuttle, studentId)) {
            // Remember, this is a bidirectional relation. So, dereference the shuttle too.
            shuttle.getPassengerList().remove(student);
            student.setShuttle(null);

            // Update the current capacity.
            shuttle.setCurrentCapacity(shuttle.getCurrentCapacity() - 1);
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
    @Scheduled(fixedRate = 12000)
    public void pollStartAndEnd() {

        // 600000

        // System.out.println("[pollStartAndEnd] Polling startAndEnd...\n");

        Shuttle shuttle = getShuttle();
        if (shuttle == null) {
            return;
        }

        if (shuttle.getOperatingState() == OperatingState.MAINTENANCE) {
            return;
        }

        if (shuttle.getHasArrivedAtStop()) {
            return;
        }

        if (shuttle.getHasDepartedFromStop()) {
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
        // currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(startBuffer) <= 0
        if (currentTime.compareTo(startTime) >= 0) {

            // System.out.println("[pollStartAndEnd] Shuttle has arrived!\n");

            if (this.stopRepository == null) {
                return;
            }

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
    @Scheduled(fixedRate = 15000)
    public void checkForStart() {

        // 600000

        // System.out.println("[checkForStart] polling to see if shuttle can start...\n");

        Shuttle shuttle = getShuttle();
        if (shuttle == null) {
            return;
        }

        // Shuttle can't start until there is at least someone in the shuttle.
        if (shuttle.getPassengerList().isEmpty()) {
            return;
        }

        // if the shuttle has already departed, no need to check for Start.
        if (shuttle.getHasDepartedFromStop()) {
            return;
        }

        // We only enter this method if the shuttle has arrived on the stop AND the shuttle is operational.
        // Also, reaching this point of code means that there is at least one student in the shuttle.
        if (shuttle.getHasArrivedAtStop()
                && shuttle.getOperatingState() == OperatingState.OPERATIONAL) {

            Time currentTime = Time.valueOf(LocalTime.now());

            // check if shuttle is full or 15 minutes have elapsed..
            // 900000L
            if (shuttle.getPassengerList().size() == shuttle.getMaxCapacity()
                    || (currentTime.getTime() - shuttle.getArrivalTime().getTime() >= 10000L)) {

                // System.out.println("[checkForStart] Shuttle has departed!\n");

                // mark for departure.
                shuttle.setHasArrivedAtStop(false);
                shuttle.setHasDepartedFromStop(true);
                shuttle.setDepartureTime(currentTime);
                shuttle.setTimeSinceLastStop(0L);

                // calculate the distance from shuttle to 1st student here.
                initializeTrackingVariables(
                        shuttle.getCurrentLocation(),
                        shuttle.getPassengerList().get(0).getAddress()
                );

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

        // System.out.println("[tripSimulation] Shuttle is on the move...\n");

        Location destination;

        if (shuttle.getPassengerList().isEmpty()) {
            if (shuttle.getOperatingState() == OperatingState.NON_OPERATIONAL) {
                // Why do we check for non-operational state?
                // Having no students, being in the departed state, and having a non_operational
                // flag means that it is past the shuttle's operating hours and the last student has been
                // dropped. So we don't need to go back to the stop.
                return;
            } else {
                destination = Shuttle.getDEFAULT_LOCATION();
                System.out.println("Shuttle returning to College Place!");
            }
        } else {
            destination = shuttle.getPassengerList().get(0).getAddress();
            System.out.println("Destination: " + destination.toString());
        }

        Location currentLocation = shuttle.getCurrentLocation();

        if (LocationUtils.isLocationClose(currentLocation, destination)) {

            if (destination.equals(Shuttle.getDEFAULT_LOCATION())) {

                System.out.println("[tripSimulation] Shuttle has returned to the stop!\n");

                shuttle.setHasArrivedAtStop(true);
                shuttle.setHasDepartedFromStop(false);
                shuttle.setTimeSinceLastStop(0L);
                currentLocation.setLongitude(destination.getLongitude());
                currentLocation.setLatitude(destination.getLatitude());
                shuttle.setCurrentLocation(currentLocation);
                return;
            }
            // This means that the shuttle has reached a student's destination.

            System.out.println("[tripSimulation] Student dropped off! Moving on...\n");

            // Pop the student off the queue.
            Student poppedStudent = shuttle.getPassengerList().remove(0);

            // Disassociate the student from the shuttle.
            poppedStudent.setShuttle(null);

            Location newStart = poppedStudent.getAddress();
            Location newEnd;

            // Set timeSinceLastStop to 0.
            shuttle.setTimeSinceLastStop(0L);

            // calculate the new total distance from student[i] - student[i+1]
            if (!shuttle.getPassengerList().isEmpty()) {
                newEnd = shuttle.getPassengerList().get(0).getAddress();
                initializeTrackingVariables(newStart, newEnd);
            } else {
                newEnd = Shuttle.getDEFAULT_LOCATION();
                initializeTrackingVariables(newStart, newEnd);
            }
            // Persist to db.
            this.repository.save(shuttle);

            // Don't do anything else in this time cycle.
            return;
        }
        // Update time since the last stop. (Add 10 seconds.)
        shuttle.setTimeSinceLastStop(shuttle.getTimeSinceLastStop() + 10L);

        // calculate the number of  elapsed intervals.
        int intervalNumber = (int) (shuttle.getTimeSinceLastStop() / 10.0);

        // get fraction of distance covered.
        double distanceCovered = (double) intervalNumber / this.totalIntervals;

        Location newLocation = LocationUtils.interpolate(currentLocation, destination, distanceCovered);

        currentLocation.setLatitude(newLocation.getLatitude());
        currentLocation.setLongitude(newLocation.getLongitude());

        shuttle.setCurrentLocation(currentLocation);

        System.out.println(
                "Current location: (" + newLocation +
                        "), distance to end: " + LocationUtils.calculateHaversineDistance(currentLocation, destination)
        );

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
