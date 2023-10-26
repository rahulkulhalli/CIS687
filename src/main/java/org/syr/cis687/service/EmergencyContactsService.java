package org.syr.cis687.service;

import org.syr.cis687.models.EmergencyContacts;

import java.util.List;
import java.util.Optional;

public interface EmergencyContactsService {
    public List<EmergencyContacts> getAllContacts();

    public Optional<EmergencyContacts> getContactById(Long id);

    public EmergencyContacts addContact(EmergencyContacts driver);

    public EmergencyContacts updateContact(Long id, EmergencyContacts driver);

    public boolean deleteContact(Long id);
}
