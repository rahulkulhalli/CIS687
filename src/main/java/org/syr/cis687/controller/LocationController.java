package org.syr.cis687.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.syr.cis687.models.Location;
import org.syr.cis687.repository.LocationRepository;

import java.util.Date;

@Controller
@RequestMapping(path = "/location")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @PostMapping(path = "/addLocation")
    public @ResponseBody String addLocation(
            @RequestParam String latitude,
            @RequestParam String longitude
    ) {

        // set lastUpdatedDate to now.
        Date now = new Date();

        Location location = new Location();
        location.setLatitude(Double.parseDouble(latitude));
        location.setLongitude(Double.parseDouble(longitude));
        location.setLastUpdated(now);

        locationRepository.save(location);

        return "Added location!";
    }

    @GetMapping(path = "/getAllLocations")
    public @ResponseBody Iterable<Location> getLocations() {
        return locationRepository.findAll();
    }
}
