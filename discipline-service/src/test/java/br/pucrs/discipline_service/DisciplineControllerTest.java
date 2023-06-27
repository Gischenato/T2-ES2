package br.pucrs.discipline_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.pucrs.discipline_service.errors.DisciplineAlreadyRegisteredException;
import br.pucrs.discipline_service.errors.DisciplineNotFoundException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class DisciplineControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DisciplineService disciplineService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        DisciplineController disciplineController = new DisciplineController(disciplineService);
        mockMvc = MockMvcBuilders.standaloneSetup(disciplineController).build();
    }

    @Test
    public void testRegisterDiscipline() throws Exception {
        mockMvc.perform(post("/discipline/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"id\": 1, \"name\": \"name1\", \"classCode\": \"classCode1\", \"turma\": \"turma1\" }"))
            .andExpect(status().isCreated())
            .andExpect(content().string("Discipline registered"));

        verify(disciplineService, times(1)).registerDiscipline(any(Discipline.class));
    }
  
    @Test
    public void testRegisterDisciplineAlreadyExists() throws Exception {
        doThrow(new DisciplineAlreadyRegisteredException("Discipline already registered"))
            .when(disciplineService).registerDiscipline(any(Discipline.class));

        mockMvc.perform(post("/discipline/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"id\": 1, \"name\": \"name1\", \"classCode\": \"classCode1\", \"turma\": \"turma1\" }"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Discipline already registered"));
    }

    @Test
    public void testGetAllDisciplines() throws Exception {
    Discipline discipline1 = Discipline.builder()
    .id(1L).name("name1").classCode("classCode1").turma("turma1").build();
    Discipline discipline2 = Discipline.builder()
    .id(2L).name("name2").classCode("classCode2").turma("turma2").build();

    List<Discipline> disciplines = Arrays.asList(discipline1, discipline2);

    when(disciplineService.findAll()).thenReturn(disciplines);

    String expectedJson = "["
    + "{ \"id\": 1, \"name\": \"name1\", \"classCode\": \"classCode1\", \"turma\": \"turma1\" },"
    + "{ \"id\": 2, \"name\": \"name2\", \"classCode\": \"classCode2\", \"turma\": \"turma2\" }"
    + "]";

    mockMvc.perform(get("/discipline/all")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson));
    }

    @Test
    public void testGetDisciplineByClassCodeAndTurma() throws Exception {
        Discipline discipline = Discipline.builder()
                .id(1L).name("name1").classCode("classCode1").turma("turma1").build();

        when(disciplineService.findByClassCodeAndTurma("classCode1", "turma1")).thenReturn(discipline);

        mockMvc.perform(get("/discipline/find/classCode1/turma1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("name1")))
            .andExpect(jsonPath("$.classCode", is("classCode1")))
            .andExpect(jsonPath("$.turma", is("turma1")));
    }

    @Test
    public void testGetDisciplineByClassCodeAndTurma_NotFound() throws Exception {
        when(disciplineService.findByClassCodeAndTurma("classCode1", "turma1"))
            .thenThrow(new DisciplineNotFoundException("Discipline not found"));

        mockMvc.perform(get("/discipline/find/classCode1/turma1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

}