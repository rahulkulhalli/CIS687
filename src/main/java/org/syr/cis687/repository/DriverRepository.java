package org.syr.cis687.repository;

import org.springframework.data.repository.CrudRepository;
import org.syr.cis687.models.DriverDetails;

public interface DriverRepository extends CrudRepository<DriverDetails, Long> {
    // pass.
}
