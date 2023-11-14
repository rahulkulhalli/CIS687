package org.syr.cis687.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.syr.cis687.models.Location;
import org.syr.cis687.models.Student;
import org.syr.cis687.repository.StudentRepository;

public class TestStudentService {

    private Student student;
    private StudentRepository repository;

    @Before
    public void setup() {
        // Just so that we don't have to keep creating students.
        this.student = new Student();
        student.setId(1L);
        student.setContactNumber("Test1");
        student.setEmailId("test1@syr.edu");
        student.setFirstName("Test");
        student.setLastName("noLastName");
        student.setOrgId("0001");

        Location location = new Location();
        location.setLatitude(1.00);
        location.setLongitude(2.00);

        student.setAddress(location);

        // Mock the repo. We don't want to actually insert/retrieve data from here.
        this.repository = Mockito.mock(StudentRepository.class);
    }

    @Test
    public void testStudentAddition() {
        // set positive outcome.
        Mockito.when(this.repository.save(this.student)).thenReturn(this.student);

        // Simulate the addition.
        Student result = this.repository.save(this.student);

        // Assert if both are equal.
        Assert.assertEquals(result, this.student);
    }
}
