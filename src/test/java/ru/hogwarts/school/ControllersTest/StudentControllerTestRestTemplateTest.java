package ru.hogwarts.school.ControllersTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/student";
    }

    @Test
    void createStudent_shouldReturnCreatedStudent() {
        Student student = new Student();
        student.setName("Test Student");
        student.setAge(20);
        student.setId(1L);


        ResponseEntity<Student> response = restTemplate.postForEntity(
                getBaseUrl(),
                student,
                Student.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Test Student", response.getBody().getName());
    }

    @Test
    void readStudent_shouldReturnStudentWhenExists() {
        Student student = new Student();
        student.setName("Test Student");
        student.setAge(20);
        student.setId(2L);
        Student created = restTemplate.postForObject(getBaseUrl(), student, Student.class);

        ResponseEntity<Student> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + created.getId(),
                Student.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(created.getId(), response.getBody().getId());
    }

    @Test
    void readStudent_shouldReturnNotFoundWhenNotExists() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                getBaseUrl() + "/999999",
                Student.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent() {
        Student student = new Student();
        student.setName("Test Student");
        student.setAge(20);
        student.setId(2L);
        Student created = restTemplate.postForObject(getBaseUrl(), student, Student.class);

        created.setName("Updated Name");
        HttpEntity<Student> request = new HttpEntity<>(created);
        ResponseEntity<Student> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT,
                request,
                Student.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Name", response.getBody().getName());
    }

    @Test
    void deleteStudent_shouldDeleteStudent() {
        Student student = new Student();
        student.setName("Test Student");
        student.setAge(20);
        student.setId(1L);
        Student created = restTemplate.postForObject(getBaseUrl(), student, Student.class);

        ResponseEntity<Student> deleteResponse = restTemplate.exchange(
                getBaseUrl() + "/" + created.getId(),
                HttpMethod.DELETE,
                null,
                Student.class
        );

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertNotNull(deleteResponse.getBody());
        assertEquals(created.getId(), deleteResponse.getBody().getId());

        ResponseEntity<Student> getResponse = restTemplate.getForEntity(
                getBaseUrl() + "/" + created.getId(),
                Student.class
        );
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void getAllStudents_shouldReturnAllStudents() {
        Student student = new Student();
        student.setName("Test Student");
        student.setAge(20);
        student.setId(3L);
        restTemplate.postForObject(getBaseUrl(), student, Student.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                getBaseUrl(),
                Collection.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getStudentByAge_shouldReturnStudentsWithGivenAge() {
        Student student = new Student();
        student.setName("Test Student");
        student.setAge(20);
        student.setId(4L);
        restTemplate.postForObject(getBaseUrl(), student, Student.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                getBaseUrl() + "/age/20",
                Collection.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getStudentsByAgeBetween_shouldReturnStudentsInAgeRange() {
        Student student1 = new Student();
        student1.setName("Student 1");
        student1.setAge(20);
        student1.setId(2L);
        restTemplate.postForObject(getBaseUrl(), student1, Student.class);

        Student student2 = new Student();
        student2.setName("Student 2");
        student2.setAge(25);
        student2.setId(2L);
        restTemplate.postForObject(getBaseUrl(), student2, Student.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                getBaseUrl() + "/age-between?minAge=19&maxAge=21",
                Collection.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

}