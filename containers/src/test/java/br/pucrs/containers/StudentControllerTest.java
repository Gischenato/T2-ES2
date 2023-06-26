package br.pucrs.containers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        StudentController controller = new StudentController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getAllStudentsTest() throws Exception {
        Student student1 = new Student();
        Student student2 = new Student();
        when(service.findAll()).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/student/all"))
                .andExpect(status().isOk());

        verify(service, times(1)).findAll();
    }

    @Test
    public void getStudentByRegistrationNumberTest() throws Exception {
        Student student = new Student();
        when(service.findById(anyLong())).thenReturn(student);

        mockMvc.perform(get("/student/find/12345"))
                .andExpect(status().isOk());

        verify(service, times(1)).findById(anyLong());
    }

    @Test
    public void getStudentByNameTest() throws Exception {
        Student student1 = new Student();
        Student student2 = new Student();

        when(service.findByNameContaining(anyString())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0);
            if(name.equalsIgnoreCase("test")) {
                return Arrays.asList(student1, student2);
            } else {
                return new ArrayList<>();
            }
        });

        mockMvc.perform(get("/student/findByName/test")) 
                .andExpect(status().isOk());            

        verify(service, times(1)).findByNameContaining(anyString());
    }

    @Test
    public void getStudentClassesTest() throws Exception {
        Student student = new Student();
        Discipline discipline1 = new Discipline(); 
        Discipline discipline2 = new Discipline();
        student.setDisciplines(Arrays.asList(discipline1, discipline2));
        when(service.findById(anyLong())).thenReturn(student);

        mockMvc.perform(get("/student/getClasses/12345"))
                .andExpect(status().isOk());

        verify(service, times(1)).getStudentDisciplines(anyLong());
    }
}