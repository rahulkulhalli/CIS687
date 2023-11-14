package org.syr.cis687.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.syr.cis687.models.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
    @Modifying
    @Query("DELETE FROM Student s WHERE s.id = ?1")
    @Transactional
    int deleteByIdAndReturnCount(Long id);

    // Spring should automatically detect and parse this SQL statement.
    List<Student> findByOrgId(String orgId);

    Boolean existsByEmailId(String emailId);
    Boolean existsByUsername(String username);


    Optional<Object> findByUsername(String username);
}
