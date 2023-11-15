package org.syr.cis687.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.syr.cis687.auth_payload.request.LoginRequest;
import org.syr.cis687.auth_payload.request.SignupRequest;

public interface AuthController {
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest);
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest);
}
