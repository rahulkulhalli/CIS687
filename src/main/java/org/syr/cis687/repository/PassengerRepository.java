package org.syr.cis687.repository;

import org.springframework.data.repository.CrudRepository;
import org.syr.cis687.models.Passenger;

public interface PassengerRepository extends CrudRepository <Passenger, Long> {
}
