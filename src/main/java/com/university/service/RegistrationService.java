package com.university.service;

import com.university.api.schema.ScheduleEntry;
import com.university.data.entity.Course;
import com.university.data.entity.Enrollment;
import com.university.data.entity.EnrollmentStatus;
import com.university.data.entity.Section;
import com.university.data.entity.Student;
import com.university.data.entity.WaitlistEntry;
import com.university.data.repository.CourseRepository;
import com.university.data.repository.EnrollmentRepository;
import com.university.data.repository.SectionRepository;
import com.university.data.repository.StudentRepository;
import com.university.data.repository.WaitlistRepository;
import com.university.service.dto.RegistrationResult;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private static final String DEFAULT_TERM = "2024-FALL";

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final WaitlistRepository waitlistRepository;

    public RegistrationService(StudentRepository studentRepository,
                               CourseRepository courseRepository,
                               SectionRepository sectionRepository,
                               EnrollmentRepository enrollmentRepository,
                               WaitlistRepository waitlistRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.waitlistRepository = waitlistRepository;
    }

    @Transactional
    public RegistrationResult registerStudent(Long studentId, String courseCode) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            return RegistrationResult.failure("Öğrenci bulunamadı: " + studentId);
        }
        Optional<Course> courseOpt = courseRepository.findByCode(courseCode);
        if (courseOpt.isEmpty()) {
            return RegistrationResult.failure("Ders bulunamadı: " + courseCode);
        }

        Section section = sectionRepository.findByCourseAndTerm(courseOpt.get(), DEFAULT_TERM)
                .orElse(null);
        if (section == null) {
            return RegistrationResult.failure("Dersin " + DEFAULT_TERM + " döneminde açılmış sınıfı yok.");
        }

        Student student = studentOpt.get();

        Optional<Enrollment> existing = enrollmentRepository.findByStudentAndSection(student, section);
        if (existing.isPresent() && existing.get().getStatus() == EnrollmentStatus.ENROLLED) {
            return RegistrationResult.failure("Öğrenci zaten derse kayıtlı.");
        }

        long currentEnrollment = enrollmentRepository.countBySectionAndStatus(section, EnrollmentStatus.ENROLLED);
        if (currentEnrollment >= section.getCapacity()) {
            return handleWaitlist(student, section);
        }

        Enrollment enrollment = existing.orElseGet(Enrollment::new);
        enrollment.setStudent(student);
        enrollment.setSection(section);
        enrollment.setStatus(EnrollmentStatus.ENROLLED);
        enrollmentRepository.save(enrollment);
        return RegistrationResult.success("Kayıt başarıyla tamamlandı.");
    }

    @Transactional
    public RegistrationResult dropStudent(Long studentId, String courseCode) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findByCode(courseCode);

        if (studentOpt.isEmpty() || courseOpt.isEmpty()) {
            return RegistrationResult.failure("Öğrenci veya ders bulunamadı.");
        }

        Section section = sectionRepository.findByCourseAndTerm(courseOpt.get(), DEFAULT_TERM)
                .orElse(null);
        if (section == null) {
            return RegistrationResult.failure("Dersin " + DEFAULT_TERM + " döneminde açılmış sınıfı yok.");
        }

        Student student = studentOpt.get();

        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByStudentAndSection(student, section);
        if (enrollmentOpt.isEmpty()) {
            return RegistrationResult.failure("Öğrenci derste kayıtlı değil.");
        }

        Enrollment enrollment = enrollmentOpt.get();
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        enrollmentRepository.save(enrollment);

        waitlistRepository.findByStudentAndSection(student, section)
                .ifPresent(waitlistRepository::delete);

        promoteNextCandidate(section);

        return RegistrationResult.success("Kayıt silindi.");
    }

    @Transactional(readOnly = true)
    public List<ScheduleEntry> listSchedule(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Öğrenci bulunamadı: " + studentId));
        List<Enrollment> enrollments = enrollmentRepository.findByStudentAndStatus(student, EnrollmentStatus.ENROLLED);
        return enrollments.stream()
                .map(this::toScheduleEntry)
                .toList();
    }

    private ScheduleEntry toScheduleEntry(Enrollment enrollment) {
        ScheduleEntry entry = new ScheduleEntry();
        entry.setCourseCode(enrollment.getSection().getCourse().getCode());
        entry.setCourseTitle(enrollment.getSection().getCourse().getTitle());
        entry.setSectionId(enrollment.getSection().getId());
        entry.setTerm(enrollment.getSection().getTerm());
        entry.setStatus(enrollment.getStatus().name());
        return entry;
    }

    private RegistrationResult handleWaitlist(Student student, Section section) {
        boolean alreadyQueued = waitlistRepository.findByStudentAndSection(student, section).isPresent();
        if (alreadyQueued) {
            return RegistrationResult.failure("Öğrenci bekleme listesinde.");
        }
        long count = waitlistRepository.countBySection(section);
        WaitlistEntry entry = new WaitlistEntry();
        entry.setStudent(student);
        entry.setSection(section);
        entry.setPosition((int) count + 1);
        waitlistRepository.save(entry);
        Enrollment waitlisted = new Enrollment();
        waitlisted.setStudent(student);
        waitlisted.setSection(section);
        waitlisted.setStatus(EnrollmentStatus.WAITLISTED);
        enrollmentRepository.save(waitlisted);
        return RegistrationResult.success("Kontenjan dolu. Bekleme listesine eklendi (Sıra " + entry.getPosition() + ").");
    }

    private void promoteNextCandidate(Section section) {
        List<WaitlistEntry> queue = waitlistRepository.findBySectionOrderByPositionAsc(section);
        if (queue.isEmpty()) {
            return;
        }
        WaitlistEntry next = queue.get(0);
        waitlistRepository.delete(next);

        Enrollment enrollment = enrollmentRepository.findByStudentAndSection(next.getStudent(), section)
                .orElseThrow();
        enrollment.setStatus(EnrollmentStatus.ENROLLED);
        enrollmentRepository.save(enrollment);

        // Update positions
        for (int i = 1; i < queue.size(); i++) {
            WaitlistEntry entry = queue.get(i);
            entry.setPosition(i);
            waitlistRepository.save(entry);
        }
    }
}





