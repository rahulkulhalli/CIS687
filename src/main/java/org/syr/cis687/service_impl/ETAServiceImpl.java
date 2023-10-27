package org.syr.cis687.service_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.syr.cis687.models.*;
import org.syr.cis687.repository.ShuttleRepository;
import org.syr.cis687.repository.ShuttleStopRepository;
import org.syr.cis687.repository.StudentRepository;
import org.syr.cis687.service.ETAService;
import org.syr.cis687.utils.CommonUtils;
import org.syr.cis687.utils.LocationUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ETAServiceImpl implements ETAService {

    @Autowired
    private ShuttleRepository shuttleRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ShuttleStopRepository shuttleStopRepository;

    @Override
    public ResponseEntity<ApiResponse> calculateETA(String orgId) {

        // Ideally should only be one value.
        List<Student> found = this.studentRepository.findByOrgId(orgId);

        ShuttleStop shuttleStop = this.shuttleStopRepository.findById(1L).orElse(null);

        if (shuttleStop == null) {
            return CommonUtils.getBadResponse(null, "No ShuttleStop object found for ETA computation!");
        }

        // First, let's see if the id exists in the DB.
        if (found == null || found.isEmpty()) {
            // no such student in the db. Return error.
            return CommonUtils.getBadResponse(orgId, String.format("Student with ID %s not found in db!", orgId));
        }

        // Now, let's see if the id exists in the passenger list.
        Optional<Shuttle> optShuttle = shuttleRepository.findById(1L);

        if (optShuttle.isEmpty()) {
            return CommonUtils.getBadResponse(orgId, String.format("Shuttle ID %d does not exist!", 1L));
        }

        // Key-in on the student.
        Student student = found.get(0);

        // Key-in on the shuttle.
        Shuttle shuttle = optShuttle.get();

        /*
        If the student is IN the shuttle, display the ETA to their destination address.
        If the student is NOT in the shuttle, display the ETA of the shuttle to the stop.
         */

        ETA eta;

        if (CommonUtils.isStudentOnShuttle(shuttle, orgId)) {
            // This means that the student is IN the shuttle.
            eta = LocationUtils.ETA
                    .from(shuttle.getCurrentLocation())
                    .to(student.getAddress())
                    .withDepartedTime(shuttle.getDepartureTime())
                    .calculate();
        } else {
            // This means that the student is OUTSIDE the shuttle.
            eta = LocationUtils.ETA
                    .from(shuttle.getCurrentLocation())
                    .to(shuttleStop.getShuttleLocation())
                    .withDepartedTime(shuttle.getDepartureTime())
                    .calculate();
        }

        if (eta == null) {
            return CommonUtils.getBadResponse(orgId, String.format("Could not compute ETA for SUID %s!", orgId));
        }

        return CommonUtils.getOkResponse(eta, String.format("ETA for SUID %s", orgId));
    }
}
