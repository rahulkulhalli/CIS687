package org.syr.cis687.controller_impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.syr.cis687.controller.ETAController;
import org.syr.cis687.models.ApiResponse;

@Controller
@RequestMapping(path = "/ETA")
public class ETAControllerImpl implements ETAController {
    @GetMapping(path = "/getETA")
    @Override
    public ResponseEntity<ApiResponse> calculateETA(@RequestParam String SUID) {
        // For now, implementing here itself. TODO: Gotta port this to a Service layer.

        // Steps:
        // 1. Get the shuttle's current location.
        // 2. Check if the student is on-board the shuttle.
        // 3. If YES, calculate ETA from current shuttle location to destination.
        // 4. If NO, calculate ETA from current shuttle location to pick-up stop.

        return null;
    }
}
