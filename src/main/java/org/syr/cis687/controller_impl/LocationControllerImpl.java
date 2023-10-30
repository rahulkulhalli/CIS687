package org.syr.cis687.controller_impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.syr.cis687.controller.LocationController;
import org.syr.cis687.enums.OpType;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.Location;
import org.syr.cis687.service_impl.LocationServiceImpl;
import org.syr.cis687.utils.CommonUtils;

import java.util.Optional;

@Controller
@RequestMapping(path = "/location")
public class LocationControllerImpl implements LocationController {

    private final LocationServiceImpl locationService;

    public LocationControllerImpl(LocationServiceImpl service) {
        this.locationService = service;
    }

    @Override
    @PostMapping(path = "/addLocation")
    public ResponseEntity<ApiResponse> addLocation(@RequestBody Location location) {
        return CommonUtils.validateAndReturn(this.locationService.addLocation(location), OpType.INSERT);
    }

    @Override
    @GetMapping(path = "/getAllLocations")
    public ResponseEntity<ApiResponse> getAllLocations() {
        return CommonUtils.validateAndReturn(this.locationService.getAllLocations(), OpType.FIND_ALL);
    }

    @Override
    @GetMapping(path = "/getLocationById")
    public ResponseEntity<ApiResponse> getLocationById(@RequestParam Long id) {
        Optional<Location> loc = this.locationService.getLocationById(id);
        if (loc.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return CommonUtils.validateAndReturn(loc.get(), OpType.UPDATE);
    }

    @Override
    @PutMapping(path = "/updateLocation")
    public ResponseEntity<ApiResponse> updateLocationById(@RequestParam Long id, @RequestBody Location location) {
        return CommonUtils.validateAndReturn(this.locationService.updateLocationById(id, location), OpType.UPDATE);
    }

    @Override
    @DeleteMapping(path = "/deleteLocation")
    public ResponseEntity<ApiResponse> deleteLocationById(@RequestParam Long id) {
        return CommonUtils.validateAndReturn(this.locationService.deleteLocationById(id), OpType.DELETE);
    }
}
