package org.syr.cis687.service_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.syr.cis687.models.Location;
import org.syr.cis687.models.Student;
import org.syr.cis687.repository.LocationRepository;
import org.syr.cis687.repository.StudentRepository;
import org.syr.cis687.service.StudentService;
import org.syr.cis687.utils.CommonUtils;

import java.util.List;
import java.util.Optional;


@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepository repository;

    private LocationRepository locationRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository repo, LocationRepository lRepo) {
        this.repository = repo;
        this.locationRepository = lRepo;
    }

    @Override
    public Optional<Student> getStudentById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Student addStudent(Student student) {
        Location loc = student.getAddress();
        this.locationRepository.save(loc);
        return repository.save(student);
    }

    @Override
    public boolean deleteStudent(Long id) {
        try {
            Student student = this.repository.findById(id).orElse(null);
            if (student == null) {
                return false;
            }

            Location location = student.getAddress();

            // delete the location too.
            this.locationRepository.deleteById(location.getId());
            return this.repository.deleteByIdAndReturnCount(id) > 0;
        } catch (Exception e) {
            return false;
        }
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
        dbStudent.setAddress(student.getAddress());

        Location newLocation = student.getAddress();

        // persist in db.
        addStudent(dbStudent);
        this.locationRepository.save(newLocation);

        return dbStudent;
    }

    @Override
    public List<Student> getAllStudents() {
        return CommonUtils.convertIterableToList(repository.findAll());
    }
}
