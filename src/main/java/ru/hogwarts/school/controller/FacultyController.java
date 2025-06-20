package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController (FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFaculty);
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> readFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.readFaculty(id);
        return faculty != null
                ? ResponseEntity.ok(faculty)
                : ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<Faculty> updateFaculty (@RequestBody Faculty faculty) {
        Faculty updatedFaculty = facultyService.updateFaculty(faculty);
        return updatedFaculty != null
                ? ResponseEntity.ok(updatedFaculty)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        Faculty deletedFaculty = facultyService.deleteFaculty(id);
        return deletedFaculty != null
                ? ResponseEntity.ok(deletedFaculty)
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<Collection<Faculty>> getFacultiesByColor(@PathVariable String color) {
        Collection<Faculty> faculties = facultyService.getFacultiesByColor(color);
        return !faculties.isEmpty()
                ? ResponseEntity.ok(faculties)
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<Faculty>> findFaculties (@RequestParam String nameOrColor) {
        Collection<Faculty> faculties = facultyService.findFacultiesByNameOrColorIgnoreCase(nameOrColor);
        return !faculties.isEmpty()
                ?ResponseEntity.ok(faculties)
                :ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<Collection<Student>> getFacultyStudents(@PathVariable Long id) {
        Collection<Student> students = facultyService.getStudentsByFacultyId(id);
        return !students.isEmpty()
                ? ResponseEntity.ok(students)
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/longest-name")
    public ResponseEntity<String> getLongestFacultyName() {
        Optional<String> longestName = facultyService.getLongestFacultyName();
        return longestName.isPresent()
                ? ResponseEntity.ok(longestName.get())
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/fast-sum")
    public ResponseEntity<Integer> calculateFastSum() {
        int n = 1_000_000;
        // Используем формулу суммы арифметической прогрессии: S = n*(a1+an)/2
        int sum = n * (1 + n) / 2;
        return ResponseEntity.ok(sum);
    }
}
