package org.syr.cis687.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.syr.cis687.models.Student;
import org.syr.cis687.repository.LocationRepository;
import org.syr.cis687.repository.StudentRepository;
import org.syr.cis687.service_impl.StudentServiceImpl;


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
}
