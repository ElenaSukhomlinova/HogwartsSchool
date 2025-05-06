package ru.hogwarts.school.ControllersTest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;



import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void createStudent_shouldReturnCreatedStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Test Student");
        student.setAge(20);

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Student\",\"age\":20}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Student"));
    }

    @Test
    void readStudent_shouldReturnStudentWhenExists() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Test Student");
        student.setAge(20);

        when(studentService.readStudent(1L)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Student"));
    }

    @Test
    void readStudent_shouldReturnNotFoundWhenNotExists() throws Exception {
        when(studentService.readStudent(anyLong())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Updated Student");
        student.setAge(21);

        when(studentService.updateStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Updated Student\",\"age\":21}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Student"));
    }

    @Test
    void deleteStudent_shouldReturnDeletedStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Test Student");
        student.setAge(20);

        when(studentService.deleteStudent(1L)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Student"));
    }

    @Test
    void getAllStudents_shouldReturnAllStudents() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Test Student");
        student.setAge(20);

        when(studentService.getAllStudents()).thenReturn(Collections.singletonList(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Student"));
    }

    @Test
    void getStudentByAge_shouldReturnStudentsWithGivenAge() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Test Student");
        student.setAge(20);

        when(studentService.getStudentByAge(20)).thenReturn(Collections.singletonList(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].age").value(20));
    }

    @Test
    void getStudentsByAgeBetween_shouldReturnStudentsInAgeRange() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Test Student");
        student.setAge(20);

        when(studentService.getStudentsByAgeBetween(19, 21))
                .thenReturn(Collections.singletonList(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age-between?minAge=19&maxAge=21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].age").value(20));
    }


}


