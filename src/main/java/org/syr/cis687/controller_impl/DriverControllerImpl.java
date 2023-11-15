package org.syr.cis687.controller_impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.syr.cis687.controller.DriverController;
import org.syr.cis687.enums.OpType;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.DriverDetails;
import org.syr.cis687.service_impl.DriverServiceImpl;
import org.syr.cis687.utils.CommonUtils;

import java.util.Optional;

@Controller
@RequestMapping(path = "/driver")
public class DriverControllerImpl implements DriverController {

    private final DriverServiceImpl driverService;

    public DriverControllerImpl(DriverServiceImpl service) {
        this.driverService = service;
    }

    @Override
    @PostMapping(path = "/addDriverDetails")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> addDriver(@RequestBody DriverDetails details) {
        return CommonUtils.validateAndReturn(this.driverService.addDriver(details), OpType.INSERT);
    }

    @Override
    @GetMapping(path = "/getAllDriverDetails")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return CommonUtils.validateAndReturn(this.driverService.getAllDrivers(), OpType.FIND_ALL);
    }

    @Override
    @GetMapping(path = "/getDriverDetailsById")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getDriverById(@RequestParam Long id) {
        Optional<DriverDetails> insertedObj = driverService.getDriverById(id);
        if (insertedObj.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return CommonUtils.validateAndReturn(insertedObj.get(), OpType.FIND_ONE);
    }

    @Override
    @PutMapping(path = "/updateDriverDetails")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STUDENT')")
    public ResponseEntity<ApiResponse> updateDriverDetails(@RequestParam Long id, @RequestBody DriverDetails details) {
        return CommonUtils.validateAndReturn(this.driverService.updateDriver(id, details), OpType.UPDATE);
    }

    @Override
    @DeleteMapping(path = "/deleteDriverDetails")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteDriverDetails(@RequestParam Long id) {
        return CommonUtils.validateAndReturn(this.driverService.deleteDriver(id), OpType.DELETE);
    }
}
