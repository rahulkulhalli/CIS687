package org.syr.cis687.service_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.syr.cis687.models.EmergencyContacts;
import org.syr.cis687.repository.EmergencyDetailsRepository;
import org.syr.cis687.service.EmergencyContactsService;
import org.syr.cis687.utils.CommonUtils;

import java.util.List;
import java.util.Optional;

@Service
public class EmergencyContactsServiceImpl implements EmergencyContactsService {

    @Autowired
    private EmergencyDetailsRepository contactsRepo;

    @Override
    public List<EmergencyContacts> getAllContacts() {
        return CommonUtils.convertIterableToList(contactsRepo.findAll());
    }

    @Override
    public Optional<EmergencyContacts> getContactById(Long id) {
        return contactsRepo.findById(id);
    }

    @Override
    public EmergencyContacts addContact(EmergencyContacts driver) {
        return contactsRepo.save(driver);
    }

    @Override
    public EmergencyContacts updateContact(Long id, EmergencyContacts driver) {
        // first, find the existing id.
        Optional<EmergencyContacts> objToModify = getContactById(id);

        if (objToModify.isEmpty()) {
            return null;
        }

        // Make the changes.
        EmergencyContacts details = objToModify.get();

        details.setFirstName(driver.getFirstName());
        details.setLastName(driver.getLastName());
        details.setOrgId(driver.getOrgId());
        details.setEmailId(details.getEmailId());
        details.setContactNumber(details.getContactNumber());

        // Persist in db.
        addContact(details);

        // return the updated object.
        return details;
    }

    @Override
    public boolean deleteContact(Long id) {
        try {
            return this.contactsRepo.deleteByIdAndReturnCount(id) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
