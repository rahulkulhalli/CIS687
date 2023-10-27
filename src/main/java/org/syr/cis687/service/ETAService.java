package org.syr.cis687.service;

import org.springframework.http.ResponseEntity;
import org.syr.cis687.models.ApiResponse;

public interface ETAService {
    ResponseEntity<ApiResponse> calculateETA(String orgId);
}
