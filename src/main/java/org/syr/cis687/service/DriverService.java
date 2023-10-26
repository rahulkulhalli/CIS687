package org.syr.cis687.service;

import org.syr.cis687.models.DriverDetails;

import java.util.List;
import java.util.Optional;

public interface DriverService {
    List<DriverDetails> getAllDrivers();

    Optional<DriverDetails> getDriverById(Long id);

    DriverDetails addDriver(DriverDetails driver);

    DriverDetails updateDriver(Long id, DriverDetails driver);

    boolean deleteDriver(Long id);
}
