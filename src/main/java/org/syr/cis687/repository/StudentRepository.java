package org.syr.cis687.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.syr.cis687.models.Student;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student, Long> {
    @Modifying
    @Query("DELETE FROM Student s WHERE s.id = ?1")
    @Transactional
    int deleteByIdAndReturnCount(Long id);

    // Spring should automatically detect and parse this SQL statement.
    List<Student> findByOrgId(String orgId);
}
