package org.syr.cis687.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.syr.cis687.models.Passenger;

public interface PassengerRepository extends CrudRepository <Passenger, Long> {
    @Modifying
    @Query("DELETE FROM Passenger p WHERE p.id = ?1")
    @Transactional
    int deleteByIdAndReturnCount(Long id);
}
