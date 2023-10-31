package org.syr.cis687.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.syr.cis687.models.Location;

public interface LocationRepository extends CrudRepository<Location, Long> {
    @Modifying
    @Query("DELETE FROM Location l WHERE l.id = ?1")
    @Transactional
    int deleteByIdAndReturnCount(Long id);
}
