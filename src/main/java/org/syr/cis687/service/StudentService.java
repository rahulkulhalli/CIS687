package org.syr.cis687.service;

import org.syr.cis687.models.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    public Optional<Student> getStudentById(Long id);
    public Student addStudent(Student student);
    public boolean deleteStudent(Long id);
    public Student updateStudent(Long id, Student student);
    public List<Student> getAllStudents();
}
