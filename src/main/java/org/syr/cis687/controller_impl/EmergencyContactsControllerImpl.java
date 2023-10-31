package org.syr.cis687.controller_impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.syr.cis687.controller.EmergencyContactsController;
import org.syr.cis687.enums.OpType;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.EmergencyContacts;
import org.syr.cis687.service_impl.EmergencyContactsServiceImpl;
import org.syr.cis687.utils.CommonUtils;

import java.util.Optional;

@Controller
@RequestMapping(path = "/emergencyContacts")
public class EmergencyContactsControllerImpl implements EmergencyContactsController {

    private final EmergencyContactsServiceImpl service;

    public EmergencyContactsControllerImpl(EmergencyContactsServiceImpl impl) {
        this.service = impl;
    }

    @Override
    @PostMapping(path = "/addContactDetails")
    public ResponseEntity<ApiResponse> addContact(EmergencyContacts details) {
        return CommonUtils.validateAndReturn(this.service.addContact(details), OpType.INSERT);
    }

    @Override
    @GetMapping(path = "/getAllContactDetails")
    public ResponseEntity<ApiResponse> getAllContacts() {
        return CommonUtils.validateAndReturn(this.service.getAllContacts(), OpType.FIND_ALL);
    }

    @Override
    @GetMapping(path = "/getContactDetailsById")
    public ResponseEntity<ApiResponse> getContactById(@RequestParam Long id) {
        Optional<EmergencyContacts> insertedObj = this.service.getContactById(id);
        if (insertedObj.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return CommonUtils.validateAndReturn(insertedObj.get(), OpType.FIND_ONE);
    }

    @Override
    @PutMapping(path = "/updateContactDetails")
    public ResponseEntity<ApiResponse> updateContactDetails(@RequestParam Long id, @RequestBody EmergencyContacts details) {
        return CommonUtils.validateAndReturn(this.service.updateContact(id, details), OpType.UPDATE);
    }

    @Override
    @DeleteMapping(path = "/deleteContactDetails")
    public ResponseEntity<ApiResponse> deleteContactDetails(@RequestParam Long id) {
        return CommonUtils.validateAndReturn(this.service.deleteContact(id), OpType.DELETE);
    }
}
