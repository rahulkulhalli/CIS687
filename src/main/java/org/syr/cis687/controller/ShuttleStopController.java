package org.syr.cis687.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.ShuttleStop;

public interface ShuttleStopController {
    ResponseEntity<ApiResponse> addShuttleStop(@RequestBody ShuttleStop stop);

    ResponseEntity<ApiResponse> getShuttleStop();

    ResponseEntity<ApiResponse> deleteShuttleStop(@RequestParam Long id);

    ResponseEntity<ApiResponse> updateShuttleStop(@RequestParam Long id, @RequestBody ShuttleStop stop);
}
