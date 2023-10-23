package org.syr.cis687.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.syr.cis687.enums.CurrentState;
import org.syr.cis687.enums.OperatingState;
import org.syr.cis687.models.Location;
import org.syr.cis687.models.Shuttle;
import org.syr.cis687.repository.LocationRepository;
import org.syr.cis687.repository.ShuttleRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(path = "/shuttle")
public class ShuttleController {

    @Autowired
    private ShuttleRepository shuttleRepository;
    @Autowired
    private LocationRepository locationRepository;

    @PostMapping(path = "/createShuttle")
    public @ResponseBody String addShuttle(
            @RequestParam String alias,
            @RequestParam String maxCapacity,
            @RequestParam String timeOfArrival
    ) {

        // Check if a shuttle already exists.
        Iterable<Shuttle> existingShuttles = shuttleRepository.findAll();
        if (existingShuttles.iterator().hasNext()) {
            // we already have a shuttle.
            return "One shuttle already in system.";
        }

        //Creates a new shuttle with default states.
        Shuttle shuttle = new Shuttle();
        shuttle.setAlias(alias);
        shuttle.setMaxCapacity(Integer.parseInt(maxCapacity));

        // Start state values.
        shuttle.setCurrentCapacity(0);
        shuttle.setOperatingState(OperatingState.OPERATIONAL);
        shuttle.setCurrentState(CurrentState.ON_TIME);

        // Shuttle start location - location at id 1.
        Optional<Location> startLoc = locationRepository.findById(1L);

        // Functional-style check
        startLoc.ifPresent(shuttle::setCurrentLocation);

        shuttleRepository.save(shuttle);

        return "Shuttle added!";
    }
}
