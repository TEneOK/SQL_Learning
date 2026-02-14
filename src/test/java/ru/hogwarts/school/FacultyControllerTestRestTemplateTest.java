package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Scarlet");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty",
                faculty,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
        assertThat(response.getBody().getColor()).isEqualTo("Scarlet");
    }

    @Test
    void testGetFacultyById() {
        Faculty faculty = new Faculty();
        faculty.setName("Slytherin");
        faculty.setColor("Green");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty",
                faculty,
                Faculty.class
        );
        Long facultyId = createResponse.getBody().getId();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + facultyId,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(facultyId);
        assertThat(response.getBody().getName()).isEqualTo("Slytherin");
    }

    @Test
    void testUpdateFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty",
                faculty,
                Faculty.class
        );
        Long facultyId = createResponse.getBody().getId();

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(facultyId);
        updatedFaculty.setName("Hufflepuff House");
        updatedFaculty.setColor("Yellow and Black");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Faculty> request = new HttpEntity<>(updatedFaculty, headers);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.PUT,
                request,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Hufflepuff House");
        assertThat(response.getBody().getColor()).isEqualTo("Yellow and Black");
    }

    @Test
    void testDeleteFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Ravenclaw");
        faculty.setColor("Blue");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty",
                faculty,
                Faculty.class
        );
        Long facultyId = createResponse.getBody().getId();

        restTemplate.delete("http://localhost:" + port + "/faculty/" + facultyId);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + facultyId,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetFacultiesByColor() {
        Faculty faculty1 = new Faculty();
        faculty1.setName("Gryffindor");
        faculty1.setColor("Red");
        restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty1, Faculty.class);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Slytherin");
        faculty2.setColor("Green");
        restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty2, Faculty.class);

        Faculty faculty3 = new Faculty();
        faculty3.setName("Another Red Faculty");
        faculty3.setColor("Red");
        restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty3, Faculty.class);

        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty?color=Red",
                Faculty[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()[0].getColor()).isEqualTo("Red");
        assertThat(response.getBody()[1].getColor()).isEqualTo("Red");
    }

    @Test
    void testGetAllFaculties() {
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/all",
                Faculty[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testGetFacultyByColorOrName() {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("Test Color");
        restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        ResponseEntity<Faculty[]> responseByName = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/search?name=Test Faculty",
                Faculty[].class
        );

        ResponseEntity<Faculty[]> responseByColor = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/search?color=Test Color",
                Faculty[].class
        );

        assertThat(responseByName.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseByName.getBody()).isNotNull();
        assertThat(responseByName.getBody().length).isGreaterThan(0);

        assertThat(responseByColor.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseByColor.getBody()).isNotNull();
        assertThat(responseByColor.getBody().length).isGreaterThan(0);
    }
}