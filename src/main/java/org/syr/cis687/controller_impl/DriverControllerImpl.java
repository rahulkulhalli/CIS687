package org.syr.cis687.controller_impl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.syr.cis687.controller.DriverController;
import org.syr.cis687.models.DriverDetails;
import org.syr.cis687.service_impl.DriverServiceImpl;

import java.util.Optional;

@Controller
@RequestMapping(path = "/driver")
public class DriverControllerImpl implements DriverController {

    private final DriverServiceImpl driverService;

    public DriverControllerImpl(DriverServiceImpl service) {
        this.driverService = service;
    }

    @Override
    public DriverDetails addDriver(DriverDetails details) {
        return driverService.addDriver(details);
    }

    @Override
    public Iterable<DriverDetails> getAllUsers() {
        return driverService.getAllDrivers();
    }

    @Override
    public Optional<DriverDetails> getDriverById(Long id) {
        return driverService.getDriverById(id);
    }

    @Override
    public DriverDetails updateDriverDetails(Long id, DriverDetails details) {
        return driverService.updateDriver(id, details);
    }

    @Override
    public boolean deleteDriverDetails(Long id) {
        return driverService.deleteDriver(id);
    }
}