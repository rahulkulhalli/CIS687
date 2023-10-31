package org.syr.cis687.service_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.syr.cis687.models.DriverDetails;
import org.syr.cis687.repository.DriverRepository;
import org.syr.cis687.service.DriverService;
import org.syr.cis687.utils.CommonUtils;

import java.util.List;
import java.util.Optional;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository repository;

    @Override
    public List<DriverDetails> getAllDrivers() {
        return CommonUtils.convertIterableToList(repository.findAll());
    }

    @Override
    public Optional<DriverDetails> getDriverById(Long id) {
        return repository.findById(id);
    }

    @Override
    public DriverDetails addDriver(DriverDetails driver) {
        return repository.save(driver);
    }

    @Override
    public DriverDetails updateDriver(Long id, DriverDetails driver) {
        // first, find the existing id.
        Optional<DriverDetails> objToModify = getDriverById(id);

        if (objToModify.isEmpty()) {
            return null;
        }

        // Make the changes.
        DriverDetails details = objToModify.get();

        details.setFirstName(driver.getFirstName());
        details.setLastName(driver.getLastName());
        details.setOrgId(driver.getOrgId());
        details.setEmailId(details.getEmailId());
        details.setContactNumber(details.getContactNumber());

        // Persist in db.
        addDriver(details);

        // return the updated object.
        return details;
    }

    @Override
    public boolean deleteDriver(Long id) {
        try {
            return repository.deleteByIdAndReturnCount(id) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
