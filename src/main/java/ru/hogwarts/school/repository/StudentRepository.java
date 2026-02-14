package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> getById(int id) ;

    @Query("SELECT COUNT(s) FROM Student s")
    Integer getTotalStudentsCount();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double getAverageAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5",
            nativeQuery = true)
    List<Student> findLastFiveStudents();
}