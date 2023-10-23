package org.syr.cis687.repository;

import org.springframework.data.repository.CrudRepository;
import org.syr.cis687.models.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {
}
