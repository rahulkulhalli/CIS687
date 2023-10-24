package org.syr.cis687.controller_impl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.syr.cis687.controller.LocationController;
import org.syr.cis687.models.Location;
import org.syr.cis687.service_impl.LocationServiceImpl;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/location")
public class LocationControllerImpl implements LocationController {

    private final LocationServiceImpl locationService;

    public LocationControllerImpl(LocationServiceImpl service) {
        this.locationService = service;
    }

    @Override
    public Location addLocation(Location location) {
        return this.locationService.addLocation(location);
    }

    @Override
    public List<Location> getAllLocations() {
        return this.locationService.getAllLocations();
    }

    @Override
    public Location getLocationById(Long id) {
        Optional<Location> optLocation = this.locationService.getLocationById(id);
        return optLocation.orElse(null);

    }

    @Override
    public Location updateLocationById(Long id, Location location) {
        return this.locationService.updateLocationById(id, location);
    }

    @Override
    public boolean deleteLocationById(Long id) {
        return this.locationService.deleteLocationById(id);
    }
}
