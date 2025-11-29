package com.university.data.repository;

import com.university.data.entity.Section;
import com.university.data.entity.Student;
import com.university.data.entity.WaitlistEntry;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitlistRepository extends JpaRepository<WaitlistEntry, Long> {
    Optional<WaitlistEntry> findByStudentAndSection(Student student, Section section);
    List<WaitlistEntry> findBySectionOrderByPositionAsc(Section section);
    long countBySection(Section section);
}





