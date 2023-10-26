package org.syr.cis687.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.Student;

public interface StudentController {

    ResponseEntity<ApiResponse> addStudent(@RequestBody Student student);

    ResponseEntity<ApiResponse> getAllStudents();

    ResponseEntity<ApiResponse> getStudentById(@RequestParam Long id);

    ResponseEntity<ApiResponse> updateStudent(@RequestParam Long id, @RequestBody Student student);

    ResponseEntity<ApiResponse> deleteStudent(@RequestParam Long id);
}
