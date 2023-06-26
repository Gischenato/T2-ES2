package br.pucrs.containers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTest {
    
    @Mock
    private StudentRepository repository;

    private StudentService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new StudentService(repository);
    }

    @Test
    public void findByIdTest() {
        Student student = new Student(); // Add relevant data to the student object
        when(repository.findById(1L)).thenReturn(Optional.of(student));

        Student foundStudent = service.findById(1L);

        assertNotNull(foundStudent);
        assertEquals(student, foundStudent);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    public void findAllTest() {
        Student student1 = new Student(); 
        Student student2 = new Student();
        when(repository.findAll()).thenReturn(Arrays.asList(student1, student2));

        List<Student> students = service.findAll();

        assertNotNull(students);
        assertEquals(2, students.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testFindByNameContaining() {
        Student student1 = Student.builder()
            .name("Test student 1")
            .documentNumber("123456789")
            .build();

        Student student2 = Student.builder()
            .name("student Test 2")
            .documentNumber("987654321")
            .build();

        List<Student> students = Arrays.asList(student1, student2);

        when(repository.findByNameContainingIgnoreCase(Mockito.anyString())).thenReturn(students);

        List<Student> result = service.findByNameContaining("test");

        assertEquals(2, result.size(), "Size of the list should be 2");
        assertEquals(student1, result.get(0), "First student does not match");
        assertEquals(student2, result.get(1), "Second student does not match");
    }

}