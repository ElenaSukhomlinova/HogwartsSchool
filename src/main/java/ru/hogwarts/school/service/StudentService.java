package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private Long lastId = 0L;

    public Student createStudent(Student student) {
        student.setId(++ lastId);
        students.put(lastId, student);
        return student;
    }

    public Student readStudent(Long id) {
        return students.get(id);
    }

    public Student updateStudent(Student student) {
        if (students.containsKey(student.getID())) {
            students.put(student.getID(), student);
            return student;
        }
        return null;
    }

    public Student deleteStudent (Long id) {
        return students.remove(id);
    }

    public Map<Long, Student> getAllStudents() {
        return new HashMap<>(students);
    }

    public Collection<Student> getStudentByAge(int age) {
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}
