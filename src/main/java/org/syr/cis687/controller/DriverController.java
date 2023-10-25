package org.syr.cis687.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.syr.cis687.models.DriverDetails;

import java.util.List;
import java.util.Optional;

public interface DriverController {

    public ResponseEntity<DriverDetails> addDriver(@RequestBody DriverDetails details);

    public ResponseEntity<List<DriverDetails>> getAllUsers();

    public ResponseEntity<DriverDetails> getDriverById(@PathVariable("id") Long id);

    public ResponseEntity<DriverDetails> updateDriverDetails(@PathVariable("id") Long id, @RequestBody DriverDetails details);

    public ResponseEntity<Boolean> deleteDriverDetails(@PathVariable("id") Long id);
}