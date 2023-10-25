package org.syr.cis687.controller_impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.syr.cis687.controller.DriverController;
import org.syr.cis687.models.DriverDetails;
import org.syr.cis687.service_impl.DriverServiceImpl;

import java.sql.Driver;
import java.util.List;
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
    public ResponseEntity<DriverDetails> addDriver(@RequestBody DriverDetails details) {
        try {
            DriverDetails insertedObj = driverService.addDriver(details);
            if (insertedObj == null) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            return new ResponseEntity<>(insertedObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @GetMapping(path = "/getAllDriverDetails")
    public ResponseEntity<List<DriverDetails>> getAllUsers() {
        List<DriverDetails> insertedObj = driverService.getAllDrivers();
        if (insertedObj != null && !insertedObj.isEmpty()) {
            return new ResponseEntity<>(insertedObj, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    @GetMapping(path = "/getDriverById/{id}")
    public ResponseEntity<DriverDetails> getDriverById(@PathVariable("id") Long id) {
        Optional<DriverDetails> insertedObj = driverService.getDriverById(id);
        return insertedObj.map(
                driverDetails ->
                new ResponseEntity<>(driverDetails, HttpStatus.OK)).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @Override
    @PutMapping(path = "/updateDriverDetails/{id}")
    public ResponseEntity<DriverDetails> updateDriverDetails(@PathVariable("id") Long id, @RequestBody DriverDetails details) {
        DriverDetails updated = driverService.updateDriver(id, details);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Override
    @DeleteMapping(path = "/deleteDriverDetails/{id}")
    public ResponseEntity<Boolean> deleteDriverDetails(@PathVariable("id") Long id) {
        boolean status = driverService.deleteDriver(id);
        if (status) {
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        }

        return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_MODIFIED);
    }
}