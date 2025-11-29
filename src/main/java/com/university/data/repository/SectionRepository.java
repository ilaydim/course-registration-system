package com.university.data.repository;

import com.university.data.entity.Course;
import com.university.data.entity.Section;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findByCourseAndTerm(Course course, String term);
    List<Section> findByTerm(String term);
}





