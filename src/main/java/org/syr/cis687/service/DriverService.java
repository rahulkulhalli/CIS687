package org.syr.cis687.service;

import org.syr.cis687.models.DriverDetails;

import java.util.List;
import java.util.Optional;

public interface DriverService {
    public List<DriverDetails> getAllDrivers();

    public Optional<DriverDetails> getDriverById(Long id);

    public DriverDetails addDriver(DriverDetails driver);

    public DriverDetails updateDriver(Long id, DriverDetails driver);

    public boolean deleteDriver(Long id);
}
