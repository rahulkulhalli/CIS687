package org.syr.cis687.repository;

import org.springframework.data.repository.CrudRepository;
import org.syr.cis687.models.ShuttleSchedule;

import java.util.List;

public interface ShuttleScheduleRepository extends CrudRepository<ShuttleSchedule, Long> {

    // Auto-implemented by Spring.
    List<ShuttleSchedule> findByDayOfWeek(String dayOfWeek);
}
