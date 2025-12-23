package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;


import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    // Тестовые данные
    private Faculty gryffindor;
    private Faculty slytherin;
    private Faculty hufflepuff;
    private Faculty ravenclaw;

    @BeforeEach
    void setUp() {
        gryffindor = new Faculty("Грифиндор", "Красный");
        slytherin = new Faculty("Слизерин", "Зеленый");
        hufflepuff = new Faculty("Пуффендуй", "Желтый");
        ravenclaw = new Faculty("Когтевран", "Синий");
    }

    @Test
    void testCreateFaculty() throws Exception {
        Faculty newFaculty = new Faculty();
        newFaculty.setId(1L);
        newFaculty.setName("Новый факультет");
        newFaculty.setColor("Фиолетовый");

        Faculty savedFaculty = new Faculty("Новый факультет", "Фиолетовый");

        when(facultyService.addFaculty(any(Faculty.class)))
                .thenReturn(savedFaculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Новый факультет"))
                .andExpect(jsonPath("$.color").value("Фиолетовый"));
    }

    @Test
    void testDeleteFaculty() throws Exception {
        when(facultyService.deleteFaculty(1L))
                .thenReturn(gryffindor);

        mockMvc.perform(delete("/faculty/{id}", 1L))
                .andExpect(status().isOk());
    }
}