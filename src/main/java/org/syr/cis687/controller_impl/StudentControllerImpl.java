package org.syr.cis687.controller_impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.syr.cis687.controller.StudentController;
import org.syr.cis687.enums.OpType;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.Student;
import org.syr.cis687.service_impl.StudentServiceImpl;
import org.syr.cis687.utils.CommonUtils;

import java.util.Optional;

@Controller
@RequestMapping(path = "/student")
public class StudentControllerImpl implements StudentController {

    private final StudentServiceImpl service;

    public StudentControllerImpl(StudentServiceImpl impl) {
        this.service = impl;
    }

    @Override
    @PostMapping(path = "/addStudent")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> addStudent(@RequestBody Student student) {
        return CommonUtils.validateAndReturn(this.service.addStudent(student), OpType.INSERT);
    }

    @Override
    @GetMapping(path = "/getAllStudents")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getAllStudents() {
        return CommonUtils.validateAndReturn(this.service.getAllStudents(), OpType.FIND_ALL);
    }

    @Override
    @GetMapping(path = "/getStudentById")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getStudentById(@RequestParam Long id) {
        Optional<Student> student = this.service.getStudentById(id);
        if (student.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return CommonUtils.validateAndReturn(student.get(), OpType.FIND_ONE);
    }

    @Override
    @PutMapping(path = "/updateStudent")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STUDENT')")
    public ResponseEntity<ApiResponse> updateStudent(@RequestParam Long id, @RequestBody Student student) {
        return CommonUtils.validateAndReturn(this.service.updateStudent(id, student), OpType.UPDATE);
    }

    @Override
    @DeleteMapping(path = "/deleteStudent")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteStudent(@RequestParam Long id) {
        return CommonUtils.validateAndReturn(this.service.deleteStudent(id), OpType.DELETE);
    }
}
