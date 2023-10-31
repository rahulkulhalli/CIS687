package org.syr.cis687.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.syr.cis687.models.ApiResponse;

/**
 * Provides contract methods to implement the ETA functionality.
 */
public interface ETAController {
    ResponseEntity<ApiResponse> calculateETA(@RequestParam String SUID);
}
