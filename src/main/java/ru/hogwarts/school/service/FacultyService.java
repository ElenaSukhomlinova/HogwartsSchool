package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty (Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty readFaculty (Long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty updateFaculty (Faculty faculty) {
        if (facultyRepository.existsById(faculty.getId())) {
            return facultyRepository.save(faculty);
        }
        return null;
    }

    public Faculty deleteFaculty (Long id) {
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty != null) {
            facultyRepository.deleteById(id);
        }
        return faculty;
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        return facultyRepository.findAll()
                .stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .toList();
    }

    public Collection<Faculty> findFacultiesByNameOrColorIgnoreCase(String nameOrColor) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);
    }

    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudents)
                .orElse(Collections.emptyList());
    }

    public Optional<String> getLongestFacultyName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length));
    }
}
