package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateStudent() {
        Student student = new Student();
        student.setName("Harry Potter");
        student.setAge(15);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Harry Potter");
        assertThat(response.getBody().getAge()).isEqualTo(15);
    }

    @Test
    void testGetStudentById() {
        Student student = new Student();
        student.setName("Hermione Granger");
        student.setAge(15);
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student,
                Student.class
        );
        Long studentId = createResponse.getBody().getId();

        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + studentId,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(studentId);
        assertThat(response.getBody().getName()).isEqualTo("Hermione Granger");
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student();
        student.setName("Ron Weasley");
        student.setAge(15);
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student,
                Student.class
        );
        Long studentId = createResponse.getBody().getId();

        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setName("Ronald Weasley");
        updatedStudent.setAge(16);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> request = new HttpEntity<>(updatedStudent, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                "http://localhost:" + port + "/student",
                HttpMethod.PUT,
                request,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Ronald Weasley");
        assertThat(response.getBody().getAge()).isEqualTo(16);
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student();
        student.setName("Draco Malfoy");
        student.setAge(15);
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student,
                Student.class
        );
        Long studentId = createResponse.getBody().getId();

        restTemplate.delete("http://localhost:" + port + "/student/" + studentId);

        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + studentId,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetStudentsByAge() {
        Student student1 = new Student();
        student1.setName("Luna Lovegood");
        student1.setAge(14);
        restTemplate.postForEntity("http://localhost:" + port + "/student", student1, Student.class);

        Student student2 = new Student();
        student2.setName("Neville Longbottom");
        student2.setAge(15);
        restTemplate.postForEntity("http://localhost:" + port + "/student", student2, Student.class);

        Student student3 = new Student();
        student3.setName("Ginny Weasley");
        student3.setAge(14);
        restTemplate.postForEntity("http://localhost:" + port + "/student", student3, Student.class);

        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student?age=14",
                Student[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()[0].getAge()).isEqualTo(14);
        assertThat(response.getBody()[1].getAge()).isEqualTo(14);
    }

    @Test
    void testGetAllStudents() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/all",
                Student[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}