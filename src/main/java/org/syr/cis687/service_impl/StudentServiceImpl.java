package org.syr.cis687.service_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.syr.cis687.models.Student;
import org.syr.cis687.repository.StudentRepository;
import org.syr.cis687.service.StudentService;
import org.syr.cis687.utils.CommonUtils;

import java.util.List;
import java.util.Optional;


@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository repository;

    @Override
    public Optional<Student> getStudentById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Student addStudent(Student student) {
        return repository.save(student);
    }

    @Override
    public boolean deleteStudent(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        try {
            repository.deleteById(id);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        if (!repository.existsById(id)) {
            return null;
        }

        Optional<Student> optStudent = getStudentById(id);

        if (optStudent.isEmpty()) {
            return null;
        }

        Student dbStudent = optStudent.get();

        dbStudent.setFirstName(student.getFirstName());
        dbStudent.setLastName(student.getLastName());
        dbStudent.setContactNumber(student.getContactNumber());
        dbStudent.setOrgId(student.getOrgId());
        dbStudent.setEmailId(student.getEmailId());

        // remove original from db.
        deleteStudent(dbStudent.getId());

        // persist in db.
        addStudent(dbStudent);

        return dbStudent;
    }

    @Override
    public List<Student> getAllStudents() {
        return CommonUtils.convertIterableToList(repository.findAll());
    }
}
