package org.syr.cis687.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.Location;

public interface LocationController {

    ResponseEntity<ApiResponse> addLocation(@RequestBody Location location);

    ResponseEntity<ApiResponse> getAllLocations();

    ResponseEntity<ApiResponse> getLocationById(@RequestParam Long id);

    ResponseEntity<ApiResponse> updateLocationById(@RequestParam Long id, @RequestBody Location location);

    ResponseEntity<ApiResponse> deleteLocationById(@RequestParam Long id);
}
