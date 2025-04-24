package ru.hogwarts.school.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class Student {
    @Id
    @GeneratedValue

    private Long id;
    private String name;
    private int age;

    public Student() {}

    public Student (Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getID () {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return age == student.age && id.equals(student.id) && name.equals(student.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, age);
    }

}
