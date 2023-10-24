package org.syr.cis687.controller;

import org.springframework.web.bind.annotation.*;
import org.syr.cis687.models.DriverDetails;
import java.util.Optional;

public interface DriverController {

    @PostMapping(path = "/addDriverDetails")
    public DriverDetails addDriver(@RequestBody DriverDetails details);

    @GetMapping(path = "/getAllDriverDetails")
    public Iterable<DriverDetails> getAllUsers();

    @GetMapping(path = "/getDriverById/{id}")
    public Optional<DriverDetails> getDriverById(@PathVariable("id") Long id);

    @PutMapping(path = "/updateDriverDetails/{id}")
    public DriverDetails updateDriverDetails(@PathVariable("id") Long id, @RequestBody DriverDetails details);

    @DeleteMapping(path = "/deleteDriverDetails/{id}")
    public boolean deleteDriverDetails(@PathVariable("id") Long id);
}