package com.university.config;

import com.university.data.entity.Course;
import com.university.data.entity.Section;
import com.university.data.entity.Student;
import com.university.data.repository.CourseRepository;
import com.university.data.repository.SectionRepository;
import com.university.data.repository.StudentRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    public DataInitializer(StudentRepository studentRepository,
                           CourseRepository courseRepository,
                           SectionRepository sectionRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
    }

    @PostConstruct
    @Transactional
    public void seed() {
        if (studentRepository.count() > 0) {
            return;
        }
        Student alice = createStudent("1001", "Alice Demir", "alice@university.com");
        Student bob = createStudent("1002", "Bob Yılmaz", "bob@university.com");
        Student charlie = createStudent("1003", "Charlie Kaya", "charlie@university.com");
        Student diana = createStudent("1004", "Diana Şahin", "diana@university.com");
        Student emma = createStudent("1005", "Emma Öztürk", "emma@university.com");
        studentRepository.save(alice);
        studentRepository.save(bob);
        studentRepository.save(charlie);
        studentRepository.save(diana);
        studentRepository.save(emma);

        Course soap = createCourse("CS-501", "Service Oriented Architectures", 4);
        Course dist = createCourse("CS-410", "Distributed Systems", 4);
        Course db = createCourse("CS-301", "Database Systems", 3);
        Course algo = createCourse("CS-201", "Algorithms and Data Structures", 4);
        Course web = createCourse("CS-401", "Web Application Development", 3);
        courseRepository.save(soap);
        courseRepository.save(dist);
        courseRepository.save(db);
        courseRepository.save(algo);
        courseRepository.save(web);

        sectionRepository.save(createSection(soap, "2024-FALL", 2));
        sectionRepository.save(createSection(dist, "2024-FALL", 2));
        sectionRepository.save(createSection(db, "2024-FALL", 3));
        sectionRepository.save(createSection(algo, "2024-FALL", 3));
        sectionRepository.save(createSection(web, "2024-FALL", 2));

        log.info("Sample data seeded.");
    }

    private Student createStudent(String number, String name, String email) {
        Student student = new Student();
        student.setStudentNumber(number);
        student.setFullName(name);
        student.setEmail(email);
        return student;
    }

    private Course createCourse(String code, String title, int credits) {
        Course course = new Course();
        course.setCode(code);
        course.setTitle(title);
        course.setCredits(credits);
        return course;
    }

    private Section createSection(Course course, String term, int capacity) {
        Section section = new Section();
        section.setCourse(course);
        section.setTerm(term);
        section.setCapacity(capacity);
        return section;
    }
}


