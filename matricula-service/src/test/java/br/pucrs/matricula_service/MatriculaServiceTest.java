package br.pucrs.matricula_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.pucrs.matricula_service.clients.DisciplineClient;
import br.pucrs.matricula_service.clients.StudentClient;
import br.pucrs.matricula_service.errors.AlreadyRegisteredException;
import br.pucrs.matricula_service.errors.NotFoundException;
import br.pucrs.matricula_service.models.Discipline;
import br.pucrs.matricula_service.models.Student;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(MockitoExtension.class)
public class MatriculaServiceTest {

    @InjectMocks
    private MatriculaService matriculaService;

    @Mock
    private StudentClient studentClient;

    @Mock
    private DisciplineClient disciplineClient;

    @Mock
    private MatriculaRepository repository;

    @Test
    public void testFindStudentsInClass() {
        String classCode = "ClassCode1";
        String turma = "Turma1";
        Matricula matricula1 = Matricula.builder()
                                        .studentRegistrationNumber("101040")
                                        .classCode(classCode)
                                        .turma(turma)
                                        .build();
        Matricula matricula2 = Matricula.builder()
                                        .studentRegistrationNumber("101050")
                                        .classCode(classCode)
                                        .turma(turma)
                                        .build();
        List<Matricula> matriculas = Arrays.asList(matricula1, matricula2);
        
        Student student1 = new Student(10L, "Giovanni", "giovanni@email.com"); // Assuming Student has these parameters
        Student student2 = new Student(20L, "Gabriel", "gabriel@email.com");
        List<Student> students = Arrays.asList(student1, student2);

        when(repository.findByClassCodeAndTurma(classCode, turma)).thenReturn(matriculas);
        when(matriculaService.getStudentsById(anyList())).thenReturn(students);

        List<Student> result = matriculaService.findStudentsInClass(classCode, turma);

        assertEquals(2, result.size());
        assertEquals(student1.getRegistrationNumber(), result.get(0).getRegistrationNumber());
        assertEquals(student2.getRegistrationNumber(), result.get(1).getRegistrationNumber());
    }

    @Test
    public void testFindStudentClasses() {
        // Define test data
        String registrationNumber = "101040";
        Matricula matricula1 = Matricula.builder()
                                        .studentRegistrationNumber(registrationNumber)
                                        .classCode("ClassCode1")
                                        .turma("Turma1")
                                        .build();
        Matricula matricula2 = Matricula.builder()
                                        .studentRegistrationNumber(registrationNumber)
                                        .classCode("ClassCode2")
                                        .turma("Turma2")
                                        .build();
        List<Matricula> matriculas = Arrays.asList(matricula1, matricula2);
        
        Discipline discipline1 = Discipline.builder().classCode("ClassCode1").turma("Turma1").build();
        Discipline discipline2 = Discipline.builder().classCode("ClassCode2").turma("Turma2").build();
        List<Discipline> disciplines = Arrays.asList(discipline1, discipline2);

        // Mock the behavior of repository and getDisciplinesByClassCodeAndTurma methods
        when(repository.findByStudentRegistrationNumber(registrationNumber)).thenReturn(matriculas);
        when(matriculaService.getDisciplinesByClassCodeAndTurma(anyList())).thenReturn(disciplines);

        List<Discipline> result = matriculaService.findStudentClasses(registrationNumber);

        assertEquals(2, result.size());
        assertEquals(discipline1, result.get(0));
        assertEquals(discipline2, result.get(1));
    }


    @Test
    public void testFindById_Success() {
        Matricula expectedMatricula = new Matricula(1L, "12345", "classCode1", "turma1");

        when(repository.findById(1L)).thenReturn(Optional.of(expectedMatricula));

        Matricula actualMatricula = matriculaService.findById(1L);

        assertEquals(expectedMatricula, actualMatricula);
    }

    @Test
    public void testFindById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(
            NotFoundException.class, () -> matriculaService.findById(1L)
        );

        assertEquals("Matricula not found", thrown.getMessage());
    }

    @Test
    public void testRegisterMatricula_Success() {
        Matricula matricula = new Matricula(1L, "101040", "classCode1", "turma1");

        when(repository.existsByStudentRegistrationNumberAndClassCode(matricula.getStudentRegistrationNumber(), matricula.getClassCode())).thenReturn(false);
        when(studentClient.getStudentByRegistrationNumber(matricula.getStudentRegistrationNumber())).thenReturn(new Student());
        when(disciplineClient.getDisciplineByClassCodeAndTurma(matricula.getClassCode(), matricula.getTurma())).thenReturn(new Discipline());

        matriculaService.registerMatricula(matricula);
        verify(repository, times(1)).save(matricula);
    }

    @Test
    public void testRegisterMatricula_AlreadyRegistered() {
        Matricula matricula = new Matricula(1L, "101040", "classCode1", "turma1");

        when(repository.existsByStudentRegistrationNumberAndClassCode(
            matricula.getStudentRegistrationNumber(), matricula.getClassCode()
            )).thenReturn(true);

        AlreadyRegisteredException thrown = assertThrows(
            AlreadyRegisteredException.class,
            () -> matriculaService.registerMatricula(matricula)
        );

        assertEquals("Student already registered in this discipline", thrown.getMessage());
    }

    @Test
    public void testRegisterMatricula_SameClassCodeDifferentTurma_AlreadyRegistered() {
        Matricula matricula1 = new Matricula(1L, "101030", "classCode1", "turma1");
        Matricula matricula2 = new Matricula(2L, "101030", "classCode1", "turma2");

        Discipline discipline1 = Discipline.builder().classCode("classCode1").turma("turma1").build();

        Student student = Student.builder()
                                 .name("Giovnani")
                                 .documentNumber("101030")
                                 .build();

        when(repository.existsByStudentRegistrationNumberAndClassCode(
            matricula1.getStudentRegistrationNumber(), matricula1.getClassCode()
            )).thenReturn(false);
        when(studentClient.getStudentByRegistrationNumber(
            matricula1.getStudentRegistrationNumber()
            )).thenReturn(student);
        when(disciplineClient.getDisciplineByClassCodeAndTurma(
            matricula1.getClassCode(), matricula1.getTurma()
            )).thenReturn(discipline1);

        matriculaService.registerMatricula(matricula1);

        when(repository.existsByStudentRegistrationNumberAndClassCode(
            matricula2.getStudentRegistrationNumber(), matricula2.getClassCode()
            )).thenReturn(true);

        AlreadyRegisteredException thrown = assertThrows(
            AlreadyRegisteredException.class,
            () -> matriculaService.registerMatricula(matricula2)
        );

        assertEquals("Student already registered in this discipline", thrown.getMessage());
    }


}
