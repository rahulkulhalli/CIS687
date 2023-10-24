package org.syr.cis687.controller_impl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.syr.cis687.controller.StudentController;
import org.syr.cis687.models.Student;
import org.syr.cis687.service_impl.StudentServiceImpl;

import java.util.List;

@Controller
@RequestMapping(path = "/student")
public class StudentControllerImpl implements StudentController {

    private final StudentServiceImpl service;

    public StudentControllerImpl(StudentServiceImpl impl) {
        this.service = impl;
    }

    @Override
    public Student addStudent(Student student) {
        return this.service.addStudent(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return this.service.getAllStudents();
    }

    @Override
    public Student getStudentById(Long id) {
        return this.service.getStudentById(id).orElse(null);
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        return this.service.updateStudent(id, student);
    }

    @Override
    public boolean deleteStudent(Long id) {
        return this.service.deleteStudent(id);
    }
}
