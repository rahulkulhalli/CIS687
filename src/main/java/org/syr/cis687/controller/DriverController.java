package org.syr.cis687.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.DriverDetails;

public interface DriverController {

    ResponseEntity<ApiResponse> addDriver(@RequestBody DriverDetails details);

    ResponseEntity<ApiResponse> getAllUsers();

    ResponseEntity<ApiResponse> getDriverById(@RequestParam Long id);

    ResponseEntity<ApiResponse> updateDriverDetails(@RequestParam Long id, @RequestBody DriverDetails details);

    ResponseEntity<ApiResponse> deleteDriverDetails(@RequestParam("id") Long id);
}