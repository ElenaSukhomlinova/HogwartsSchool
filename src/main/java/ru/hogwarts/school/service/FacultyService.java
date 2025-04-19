package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.security.PublicKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long lastId = 0;

    public Faculty createFaculty (Faculty faculty) {
        faculty.setId(++ lastId);
        faculties.put(lastId, faculty);
        return faculty;
    }

    public Faculty readFaculty (Long id) {
        return faculties.get(id);
    }

    public Faculty updateFaculty (Faculty faculty) {
        if (faculties.containsKey(faculty.getId())) {
            faculties.put(faculty.getId(),faculty);
            return faculty;
        }
        return null;
    }

    public Faculty deleteFaculty (Long id) {
        return faculties.remove(id);
    }

    public Map<Long, Faculty> getAllFaculties() {
        return new HashMap<>(faculties);
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }
}
