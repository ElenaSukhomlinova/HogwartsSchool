package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity <Student> createStudent (@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> readStudent (@PathVariable Long id) {
        Student student = studentService.readStudent(id);
        return student != null
                ? ResponseEntity.ok(student)
                : ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(student);
        return updatedStudent != null
                ? ResponseEntity.ok(updatedStudent)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping ("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        Student deletedStudent = studentService.deleteStudent(id);
        return deletedStudent != null
                ? ResponseEntity.ok(deletedStudent)
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity <Collection<Student>> getAllStudents () {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/age/{age}")
    public ResponseEntity<Collection<Student>> getStudentByAge(@PathVariable int age) {
        Collection<Student> students = studentService.getStudentByAge(age);
        return !students.isEmpty()
                ? ResponseEntity.ok(students)
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/age-between")
    public ResponseEntity<Collection<Student>> getStudentsByAgeBetween(
            @RequestParam int minAge,
            @RequestParam int maxAge) {
        Collection <Student> students = studentService.getStudentsByAgeBetween(minAge,maxAge);
        return !students.isEmpty()
                ? ResponseEntity.ok(students)
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        Faculty faculty = studentService.getFacultyByStudentId(id);
        return faculty != null
                ? ResponseEntity.ok(faculty)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getTotalCountOfStudents() {
        Integer count = studentService.getTotalCountOfStudents();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAgeOfStudents() {
        Double averageAge = studentService.getAverageAgeOfStudents();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("/last-five")
    public ResponseEntity<Collection<Student>> getLastFiveStudents() {
        Collection<Student> students = studentService.getLastFiveStudents();
        return !students.isEmpty()
                ? ResponseEntity.ok(students)
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/names-starting-with-a")
    public ResponseEntity<Collection<String>> getStudentsNamesStartingWithA() {
        Collection<String> names = studentService.getStudentsNamesStartingWithA();
        return !names.isEmpty()
                ? ResponseEntity.ok(names)
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/stream-average-age")
    public ResponseEntity<Double> getAverageAgeUsingStreams() {
        Double averageAge = studentService.getAverageAgeUsingStreams();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("/print-parallel")
    public ResponseEntity<Void> printStudentsParallel() {
        Collection<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Student[] studentArray = students.toArray(new Student[0]);
        int count = Math.min(studentArray.length, 6);

        if (count >= 1) {
            System.out.println(studentArray[0].getName());
        }
        if (count >= 2) {
            System.out.println(studentArray[1].getName());
        }

        if (count >= 3) {
            new Thread(() -> {
                System.out.println(studentArray[2].getName());
                if (count >= 4) {
                    System.out.println(studentArray[3].getName());
                }
            }).start();
        }

        if (count >= 5) {
            new Thread(() -> {
                System.out.println(studentArray[4].getName());
                if (count >= 6) {
                    System.out.println(studentArray[5].getName());
                }
            }).start();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/print-synchronized")
    public ResponseEntity<Void> printStudentsSynchronized() {
        Collection<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Student[] studentArray = students.toArray(new Student[0]);
        int count = Math.min(studentArray.length, 6); // Process up to 6 students

        if (count >= 1) {
            printNameSynchronized(studentArray[0].getName());
        }
        if (count >= 2) {
            printNameSynchronized(studentArray[1].getName());
        }

        if (count >= 3) {
            new Thread(() -> {
                printNameSynchronized(studentArray[2].getName());
                if (count >= 4) {
                    printNameSynchronized(studentArray[3].getName());
                }
            }).start();
        }

        if (count >= 5) {
            new Thread(() -> {
                printNameSynchronized(studentArray[4].getName());
                if (count >= 6) {
                    printNameSynchronized(studentArray[5].getName());
                }
            }).start();
        }

        return ResponseEntity.ok().build();
    }

    private synchronized void printNameSynchronized(String name) {
        System.out.println(name);
    }
}
