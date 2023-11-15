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

        ETA eta = LocationUtils.ETA_BUILDER
                .withShuttle(shuttle)
                .forStudent(student)
                .withShuttleStopLocation(Shuttle.getDEFAULT_LOCATION())
                .calculate();

        if (eta == null) {
            return CommonUtils.getBadResponse(orgId, String.format("Could not compute ETA for SUID %s!", orgId));
        }

        return CommonUtils.getOkResponse(eta, String.format("ETA for SUID %s", orgId));
    }
}
