package org.syr.cis687.controller;

import org.springframework.web.bind.annotation.*;
import org.syr.cis687.models.Student;

import java.util.List;

public interface StudentController {
    @PostMapping(path = "/addStudent")
    public Student addStudent(@RequestBody Student student);

    @GetMapping(path = "/getAllStudents")
    public List<Student> getAllStudents();

    @GetMapping(path = "/getStudent/{id}")
    public Student getStudentById(@PathVariable("id") Long id);

    @PutMapping(path = "/updateStudent/{id}")
    public Student updateStudent(@PathVariable("id") Long id, @RequestBody Student student);

    @DeleteMapping(path = "/deleteStudent/{id}")
    public boolean deleteStudent(@PathVariable("id") Long id);
}
