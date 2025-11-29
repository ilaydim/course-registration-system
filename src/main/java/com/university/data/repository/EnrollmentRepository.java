package com.university.data.repository;

import com.university.data.entity.Enrollment;
import com.university.data.entity.EnrollmentStatus;
import com.university.data.entity.Section;
import com.university.data.entity.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    long countBySectionAndStatus(Section section, EnrollmentStatus status);
    Optional<Enrollment> findByStudentAndSection(Student student, Section section);
    List<Enrollment> findByStudentAndStatus(Student student, EnrollmentStatus status);
}





