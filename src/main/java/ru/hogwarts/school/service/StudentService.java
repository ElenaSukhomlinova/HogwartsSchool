package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        logger.debug("StudentService initiated with StudentRepository");
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method to create a student");
        logger.debug("Creating student: {}", student);
        return studentRepository.save(student);
    }

    public Student readStudent(Long id) {
        logger.info("Was invoked method for read student with id: {}", id);
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Student student) {
        logger.info("Was invoked method for update student with id: {}", student.getId());
        if (studentRepository.existsById(student.getId())) {
            return studentRepository.save(student);
        }
        return null;
    }

    public Student deleteStudent(Long id) {
        logger.info("Was invoked method for delete student with id: {}", id);
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            studentRepository.deleteById(id);
        }
        logger.debug("Deleted student with id: {}", id);
        return student;
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentByAge(int age) {
        logger.info("Was invoked method for get students by age: {}", age);
        return studentRepository.findAll()
                .stream()
                .filter(student -> student.getAge() == age)
                .toList();
    }

    public Collection <Student> getStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for get students between ages {} and {}", minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty getFacultyByStudentId(Long studentId) {
        logger.info("Was invoked method for get faculty by student id: {}", studentId);
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElse(null);
    }

    public Student getStudentById(Long id) {
        logger.info("Was invoked method for get student by id: {}", id);
        return studentRepository.findById(id).orElse(null);
    }

    public int getTotalCountOfStudents() {
        logger.info("Was invoked method for get total count of students");
        return studentRepository.countStudents();
    }

    public Double getAverageAgeOfStudents() {
        logger.info("Was invoked method for get average age of students");
        return studentRepository.findAverageAge();
    }

    public Collection<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        return studentRepository.findLastFiveStudents();
    }
}
