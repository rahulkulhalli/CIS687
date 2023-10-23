package org.syr.cis687.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.syr.cis687.models.Student;
import org.syr.cis687.repository.StudentRepository;

@Controller
@RequestMapping(path = "/student")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping(path = "/addStudentDetails")
    public @ResponseBody String addStudent (
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String suid,
            @RequestParam String email,
            @RequestParam String contactNumber
    ) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setOrgId(suid);
        student.setContactNumber(contactNumber);
        student.setEmailId(email);

        studentRepository.save(student);

        return "added student!";
    }

    @GetMapping(path = "/getAllStudents")
    public @ResponseBody Iterable<Student> getStudentInfo() {
        return studentRepository.findAll();
    }
}
