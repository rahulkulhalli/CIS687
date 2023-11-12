package org.syr.cis687.controller_impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.syr.cis687.controller.ShuttleController;
import org.syr.cis687.enums.OpType;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.Shuttle;
import org.syr.cis687.service_impl.ShuttleServiceImpl;
import org.syr.cis687.utils.CommonUtils;

import java.util.Optional;

@Controller
@RequestMapping(path = "/shuttle")
public class ShuttleControllerImpl implements ShuttleController {

    private final ShuttleServiceImpl shuttleService;

    public ShuttleControllerImpl(ShuttleServiceImpl impl) {
        this.shuttleService = impl;
    }

    @Override
    @GetMapping(path = "/getAllShuttles")
    public ResponseEntity<ApiResponse> getAllShuttles() {
        return CommonUtils.validateAndReturn(this.shuttleService.getAllShuttles(), OpType.FIND_ALL);
    }

    @Override
    @GetMapping(path = "/getShuttleById")
    public ResponseEntity<ApiResponse> getShuttleById(@RequestParam Long id) {
        Optional<Shuttle> shuttle = this.shuttleService.getShuttleById(id);
        if (shuttle.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return CommonUtils.validateAndReturn(shuttle.get(), OpType.FIND_ONE);
    }

    @Override
    @PostMapping(path = "/addShuttle")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> addShuttle(@RequestBody Shuttle shuttle) {
        return CommonUtils.validateAndReturn(this.shuttleService.addShuttle(shuttle), OpType.INSERT);
    }

    @Override
    @PostMapping(path = "/addStudentToShuttle")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STUDENT')")
    public ResponseEntity<ApiResponse> addStudentToShuttle(@RequestParam String studentId) {
        return CommonUtils.validateAndReturn(this.shuttleService.addStudentToShuttle(studentId), OpType.INSERT);
    }

    @Override
    @DeleteMapping(path = "/removeStudentFromShuttle")
    public ResponseEntity<ApiResponse> removeStudentFromShuttle(@RequestParam String studentId) {
        return null;
    }

    @Override
    @PostMapping(path = "/startTrip")
    public ResponseEntity<ApiResponse> startTrip() {
        return CommonUtils.validateAndReturn(this.shuttleService.startTrip(), OpType.UPDATE);
    }

    @Override
    @PutMapping(path = "/updateShuttle")
    public ResponseEntity<ApiResponse> updateShuttleById(@RequestParam Long id, @RequestBody Shuttle shuttle) {
        return CommonUtils.validateAndReturn(this.shuttleService.updateShuttle(id, shuttle), OpType.UPDATE);
    }

    @Override
    @DeleteMapping(path = "/deleteShuttle")
    public ResponseEntity<ApiResponse> deleteShuttleById(@RequestParam Long id) {
        return CommonUtils.validateAndReturn(this.shuttleService.deleteShuttle(id), OpType.DELETE);
    }

    @Override
    public ResponseEntity<ApiResponse> getCurrentShuttleLocation() {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> markShuttleDeparture() {
        return CommonUtils.validateAndReturn(this.shuttleService.markShuttleDeparture(), OpType.UPDATE);
    }

    @Override
    public ResponseEntity<ApiResponse> markShuttleArrival() {
        return CommonUtils.validateAndReturn(this.shuttleService.markShuttleArrival(), OpType.UPDATE);
    }
}
