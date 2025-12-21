package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    void testCreateStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Драко Малфой");
        student.setAge(15);

        Mockito.when(studentService.addStudent(any(Student.class)))
                .thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Драко Малфой")))
                .andExpect(jsonPath("$.age", is(15)));
    }

    @Test
    void testDeleteStudent() throws Exception {
        Mockito.doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetStudentsByAge() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Луна Ловгуд");
        student1.setAge(14);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Рон Уизли");
        student2.setAge(14);

        List<Student> students = Arrays.asList(student1, student2);

        Mockito.when(studentService.findByAge(14))
                .thenReturn(students);

        mockMvc.perform(get("/student").param("age", "14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Луна Ловгуд")))
                .andExpect(jsonPath("$[0].age", is(14)))
                .andExpect(jsonPath("$[1].name", is("Рон Уизли")))
                .andExpect(jsonPath("$[1].age", is(14)));
    }

    @Test
    void testGetStudentsByAgeBetween() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Студент 1");
        student1.setAge(15);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Студент 2");
        student2.setAge(16);

        List<Student> students = Arrays.asList(student1, student2);

        Mockito.when(studentService.findByAgeBetween(15, 16))
                .thenReturn(students);

        mockMvc.perform(get("/student/age-between")
                        .param("min", "15")
                        .param("max", "16"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}