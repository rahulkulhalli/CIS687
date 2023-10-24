package org.syr.cis687.controller;

import org.springframework.web.bind.annotation.*;
import org.syr.cis687.models.Location;

import java.util.List;

public interface LocationController {

    @PostMapping(name = "/addLocation")
    public Location addLocation(@RequestBody Location location);

    @GetMapping(path = "/getAllLocations")
    public List<Location> getAllLocations();

    @GetMapping(path = "/getLocationById/{id}")
    public Location getLocationById(@PathVariable("id") Long id);

    @PutMapping(path = "/updateLocation/{id}")
    public Location updateLocationById(@PathVariable("id") Long id, @RequestBody Location location);

    @DeleteMapping(path = "/deleteLocation/{id}")
    public boolean deleteLocationById(@PathVariable("id") Long id);
}
