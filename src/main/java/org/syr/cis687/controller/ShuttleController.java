package org.syr.cis687.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.Shuttle;

public interface ShuttleController {

    ResponseEntity<ApiResponse> getAllShuttles();

    ResponseEntity<ApiResponse> getShuttleById(@RequestParam Long id);

    ResponseEntity<ApiResponse> addShuttle(@RequestBody Shuttle shuttle);

    ResponseEntity<ApiResponse> updateShuttleById(@RequestParam Long id, @RequestBody Shuttle shuttle);

    ResponseEntity<ApiResponse> deleteShuttleById(@RequestParam Long id);

    ResponseEntity<ApiResponse> getCurrentShuttleLocation();

    // Shuttle operations.

    ResponseEntity<ApiResponse> markShuttleDeparture();

    ResponseEntity<ApiResponse> markShuttleArrival();

    ResponseEntity<ApiResponse> addStudentToShuttle(@RequestParam String studentId);

    ResponseEntity<ApiResponse> removeStudentFromShuttle(@RequestParam String studentId);

    ResponseEntity<ApiResponse> startTrip();
}
