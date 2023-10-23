package org.syr.cis687.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.syr.cis687.models.DriverDetails;
import org.syr.cis687.repository.DriverRepository;

@Controller
@RequestMapping(path="/driver")
public class DriverController {
    @Autowired
    private DriverRepository driverRepository;

    @PostMapping(path="/addDriverDetails")
    public @ResponseBody String addDriver (
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String employeeId,
            @RequestParam String contactNumber,
            @RequestParam String email) {

        // Create the driver object.
        DriverDetails d = new DriverDetails();

        // Populate fields.
        d.setFirstName(firstName);
        d.setLastName(lastName);
        d.setOrgId(employeeId);
        d.setContactNumber(contactNumber);
        d.setEmailId(email);

        driverRepository.save(d);
        return "Saved";
    }

    @GetMapping(path="/getDriverDetails")
    public @ResponseBody Iterable<DriverDetails> getAllUsers() {
        // This returns a JSON or XML with the users
        return driverRepository.findAll();
    }
}