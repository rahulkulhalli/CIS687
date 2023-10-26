package org.syr.cis687.service;

import org.syr.cis687.models.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Optional<Student> getStudentById(Long id);
    Student addStudent(Student student);
    boolean deleteStudent(Long id);
    Student updateStudent(Long id, Student student);
    List<Student> getAllStudents();
}
