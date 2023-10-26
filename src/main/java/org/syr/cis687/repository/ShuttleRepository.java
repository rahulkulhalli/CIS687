package org.syr.cis687.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.syr.cis687.models.Shuttle;

public interface ShuttleRepository extends CrudRepository<Shuttle, Long> {
    @Modifying
    @Query("DELETE FROM Shuttle s WHERE s.shuttleId = ?1")
    @Transactional
    int deleteByIdAndReturnCount(Long id);
}
