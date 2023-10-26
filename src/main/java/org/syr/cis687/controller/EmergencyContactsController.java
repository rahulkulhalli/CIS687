package org.syr.cis687.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.EmergencyContacts;

public interface EmergencyContactsController {
    public ResponseEntity<ApiResponse> addContact(@RequestBody EmergencyContacts details);

    public ResponseEntity<ApiResponse> getAllContacts();

    public ResponseEntity<ApiResponse> getContactById(@RequestParam Long id);

    public ResponseEntity<ApiResponse> updateContactDetails(@RequestParam Long id, @RequestBody EmergencyContacts details);

    public ResponseEntity<ApiResponse> deleteContactDetails(@RequestParam Long id);
}
