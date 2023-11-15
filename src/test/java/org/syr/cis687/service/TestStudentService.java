package org.syr.cis687.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.syr.cis687.models.Student;
import org.syr.cis687.repository.LocationRepository;
import org.syr.cis687.repository.StudentRepository;
import org.syr.cis687.service_impl.StudentServiceImpl;

import java.util.Optional;


public class TestStudentService {

    @InjectMocks
    private StudentServiceImpl service;

    @Spy
    private StudentRepository repository;

    @Mock
    private LocationRepository locationRepository;

    @Before
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSomething() {
        Student s = new Student();
        Mockito.when(repository.save(s)).thenReturn(s);
        Student returned = service.addStudent(s);
        Assert.assertEquals(returned, s);
    }
    // Test to ensure `addStudent` method properly handles a RuntimeException thrown during student saving.
    // Note: This test checks exception throwing but not the subsequent handling logic.
    @Test
    public void testGetStudentById_ExistingStudent() {
        Long studentId = 1L;
        Student expectedStudent = new Student();
        expectedStudent.setId(studentId);
        Mockito.when(repository.findById(studentId)).thenReturn(Optional.of(expectedStudent));

        Optional<Student> actualStudent = service.getStudentById(studentId);

        Assert.assertTrue(actualStudent.isPresent());
        Assert.assertEquals(expectedStudent, actualStudent.get());
    }
    // Test to verify `addStudent` throws RuntimeException when repository save operation fails.
    @Test(expected = RuntimeException.class)
    public void testAddStudent_Exception() {
        Student s = new Student();
        Mockito.when(repository.save(Mockito.any(Student.class))).thenThrow(new RuntimeException());

        service.addStudent(s);
    }
    // Test to verify `getAllStudents` method throws RuntimeException on repository `findAll` failure.
    @Test(expected = RuntimeException.class)
    public void testGetAllStudents_Exception() {
        Mockito.when(repository.findAll()).thenThrow(new RuntimeException());

        service.getAllStudents();
    }
    // Test to ensure `deleteStudent` returns false and avoids deletion for a non-existing student ID.
    @Test
    public void testDeleteStudent_NonExistingStudent() {
        Long studentId = 1L;
        Mockito.when(repository.findById(studentId)).thenReturn(Optional.empty());

        boolean isDeleted = service.deleteStudent(studentId);

        Assert.assertFalse(isDeleted);
        Mockito.verify(locationRepository, Mockito.never()).deleteById(Mockito.anyLong());
        Mockito.verify(repository, Mockito.never()).deleteByIdAndReturnCount(studentId);
    }
}
