package ru.hogwarts.school.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@Entity

public class Faculty {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String color;

    public Faculty() {}

    public Faculty (Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return id.equals(faculty.id) && name.equals(faculty.name) && color.equals(faculty.color);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, color);
    }
}
