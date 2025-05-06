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
class FacultyControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/faculty";
    }

    @Test
    void createFaculty_shouldReturnCreatedFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("Red");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                getBaseUrl(),
                faculty,
                Faculty.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Test Faculty", response.getBody().getName());
    }

    @Test
    void readFaculty_shouldReturnFacultyWhenExists() {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("Red");
        Faculty created = restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + created.getId(),
                Faculty.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(created.getId(), response.getBody().getId());
    }

    @Test
    void readFaculty_shouldReturnNotFoundWhenNotExists() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getBaseUrl() + "/999999",
                Faculty.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateFaculty_shouldReturnUpdatedFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("Red");
        Faculty created = restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        created.setName("Updated Faculty");
        HttpEntity<Faculty> request = new HttpEntity<>(created);
        ResponseEntity<Faculty> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT,
                request,
                Faculty.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Faculty", response.getBody().getName());
    }

    @Test
    void deleteFaculty_shouldDeleteFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("Red");

        Faculty created = restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        ResponseEntity<Faculty> deleteResponse = restTemplate.exchange(
                getBaseUrl() + "/" + created.getId(),
                HttpMethod.DELETE,
                null,
                Faculty.class
        );

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertNotNull(deleteResponse.getBody());
        assertEquals(created.getId(), deleteResponse.getBody().getId());

        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity(
                getBaseUrl() + "/" + created.getId(),
                Faculty.class
        );
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void getAllFaculties_shouldReturnAllFaculties() {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("Red");
        restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                getBaseUrl(),
                Collection.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getFacultiesByColor_shouldReturnFacultiesWithGivenColor() {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("Red");
        restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                getBaseUrl() + "/color/Red",
                Collection.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void findFaculties_shouldReturnFacultiesByNameOrColor() {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("Red");
        restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                getBaseUrl() + "/search?nameOrColor=Test",
                Collection.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getFacultyStudents_shouldReturnStudentsForFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("Red");
        Faculty createdFaculty = restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        Student student = new Student();
        student.setName("Test Student");
        student.setAge(20);
        student.setFaculty(createdFaculty);
        restTemplate.postForObject(
                "http://localhost:" + port + "/student",
                student,
                Student.class
        );

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + createdFaculty.getId() + "/students",
                Collection.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }
}
