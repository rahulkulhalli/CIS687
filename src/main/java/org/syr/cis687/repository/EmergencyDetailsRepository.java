package org.syr.cis687.repository;

import org.springframework.data.repository.CrudRepository;
import org.syr.cis687.models.EmergencyContacts;

public interface EmergencyDetailsRepository extends CrudRepository<EmergencyContacts, Long> {
}
