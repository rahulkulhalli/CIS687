package org.syr.cis687.repository;

import org.springframework.data.repository.CrudRepository;
import org.syr.cis687.models.Location;

public interface LocationRepository extends CrudRepository<Location, Long> {
}
