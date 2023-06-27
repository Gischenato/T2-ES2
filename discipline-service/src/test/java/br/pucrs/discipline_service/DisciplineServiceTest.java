package br.pucrs.discipline_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.pucrs.discipline_service.errors.DisciplineAlreadyRegisteredException;
import br.pucrs.discipline_service.errors.DisciplineNotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DisciplineServiceTest {

    private DisciplineService disciplineService;

    @Mock
    private DisciplineRepository disciplineRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        disciplineService = new DisciplineService(disciplineRepository);
    }

    @Test
    public void testFindAllByClassCodeAndTurma() {
        ClassCodeAndTurmaDTO classCodeAndTurmaDTO = new ClassCodeAndTurmaDTO("classCode1", "turma1");
        List<ClassCodeAndTurmaDTO> inputList = Arrays.asList(classCodeAndTurmaDTO);

        Discipline discipline = Discipline.builder()
        .id(1L).name("name1").classCode("classCode1").turma("turma1").build();
        when(disciplineRepository.findByClassCodeAndTurma("classCode1", "turma1")).thenReturn(java.util.Optional.ofNullable(discipline));

        List<Discipline> result = disciplineService.findAllByClassCodeAndTurma(inputList);

        assertEquals(1, result.size());
        assertEquals(discipline, result.get(0));
    }

    @Test
    public void testFindAllByClassCodeAndTurmaReturnsNull() {
        ClassCodeAndTurmaDTO classCodeAndTurmaDTO = new ClassCodeAndTurmaDTO("classCode1", "turma1");
        List<ClassCodeAndTurmaDTO> inputList = Arrays.asList(classCodeAndTurmaDTO);

        when(disciplineRepository.findByClassCodeAndTurma("classCode1", "turma1")).thenReturn(java.util.Optional.ofNullable(null));

        List<Discipline> result = disciplineService.findAllByClassCodeAndTurma(inputList);

        assertEquals(0, result.size());
    }

    @Test
    public void testRegisterDisciplineEmptyList() {
        Discipline discipline = Discipline.builder()
        .id(1L).name("name1").classCode("classCode1").turma("turma1").build();

        when(disciplineRepository.findByClassCode("classCode1")).thenReturn(Collections.emptyList());

        disciplineService.registerDiscipline(discipline);

        verify(disciplineRepository, times(1)).save(discipline);
    }

    @Test
    public void testRegisterDisciplineSameTurma() {
        Discipline discipline = Discipline.builder()
        .id(1L).name("name1").classCode("classCode1").turma("turma1").build();
        Discipline disciplineInDB = Discipline.builder()
        .id(1L).name("name1").classCode("classCode1").turma("turma1").build();;

        when(disciplineRepository.findByClassCode("classCode1")).thenReturn(Arrays.asList(disciplineInDB));

        DisciplineAlreadyRegisteredException thrown = assertThrows(
            DisciplineAlreadyRegisteredException.class,
            () -> disciplineService.registerDiscipline(discipline)
        );

        assertEquals("Discipline already registered", thrown.getMessage());
    }

    @Test
    public void testRegisterDisciplineSameClassCodeDifferentName() {
        Discipline discipline = Discipline.builder()
        .id(1L).name("name1").classCode("classCode1").turma("turma1").build();
        Discipline disciplineInDB = Discipline.builder()
        .id(2L).name("name2").classCode("classCode1").turma("turma2").build();

        when(disciplineRepository.findByClassCode("classCode1")).thenReturn(Arrays.asList(disciplineInDB));

        DisciplineAlreadyRegisteredException thrown = assertThrows(
            DisciplineAlreadyRegisteredException.class,
            () -> disciplineService.registerDiscipline(discipline)
        );

        assertEquals("Classcode classCode1 already registered with name name2", thrown.getMessage());
    }

    @Test
    public void testRegisterDisciplineSameClassCodeNameDifferentTurma() {
        Discipline discipline = Discipline.builder()
        .id(1L).name("name1").classCode("classCode1").turma("turma1").build();
        Discipline disciplineInDB = Discipline.builder()
        .id(2L).name("name1").classCode("classCode1").turma("turma2").build();

        when(disciplineRepository.findByClassCode("classCode1")).thenReturn(Arrays.asList(disciplineInDB));

        disciplineService.registerDiscipline(discipline);

        verify(disciplineRepository, times(1)).save(discipline);
    }

    @Test
    public void testFindByClassCodeAndTurma() {
        Discipline discipline = Discipline.builder()
        .id(1L).name("name1").classCode("classCode1").turma("turma1").build();

        when(disciplineRepository.findByClassCodeAndTurma("classCode1", "turma1")).thenReturn(java.util.Optional.ofNullable(discipline));

        Discipline result = disciplineService.findByClassCodeAndTurma("classCode1", "turma1");

        assertEquals(discipline, result);
    }

    @Test
    public void testFindByClassCodeAndTurmaNotFound() {
        when(disciplineRepository.findByClassCodeAndTurma("classCode1", "turma1")).thenReturn(java.util.Optional.ofNullable(null));

        DisciplineNotFoundException thrown = assertThrows(
            DisciplineNotFoundException.class,
            () -> disciplineService.findByClassCodeAndTurma("classCode1", "turma1")
        );

        assertEquals("Discipline not found", thrown.getMessage());
    }




    @Test
    public void testFindById() {
        Discipline discipline = Discipline.builder()
        .id(1L).name("name1").classCode("classCode1").turma("turma1").build();

        when(disciplineRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(discipline));

        Discipline result = disciplineService.findById(1L);

        assertEquals(discipline, result);
    }

    @Test
    public void testFindByIdNotFound() {
        when(disciplineRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(null));

        DisciplineNotFoundException thrown = assertThrows(
            DisciplineNotFoundException.class,
            () -> disciplineService.findById(1L)
        );

        assertEquals("Discipline not found", thrown.getMessage());
    }

    @Test
    public void testFindAll() {
        Discipline discipline1 = Discipline.builder()
        .id(1L).name("name1").classCode("classCode1").turma("turma1").build();
        Discipline discipline2 = Discipline.builder()
        .id(2L).name("name2").classCode("classCode2").turma("turma2").build();

        when(disciplineRepository.findAll()).thenReturn(Arrays.asList(discipline1, discipline2));

        List<Discipline> result = disciplineService.findAll();

        assertEquals(2, result.size());
        assertEquals(discipline1, result.get(0));
        assertEquals(discipline2, result.get(1));
    }


}