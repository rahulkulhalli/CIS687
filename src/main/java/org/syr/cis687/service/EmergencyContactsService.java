package org.syr.cis687.service;

import org.syr.cis687.models.EmergencyContacts;

import java.util.List;
import java.util.Optional;

public interface EmergencyContactsService {
    List<EmergencyContacts> getAllContacts();

    Optional<EmergencyContacts> getContactById(Long id);

    EmergencyContacts addContact(EmergencyContacts driver);

    EmergencyContacts updateContact(Long id, EmergencyContacts driver);

    boolean deleteContact(Long id);
}
