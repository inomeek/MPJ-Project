package ino.placement.service;

import ino.placement.entity.*;
import ino.placement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class AssessmentResultService {

    private final AssessmentResultRepository repo;
    private final StudentRepository studentRepo;

    public AssessmentResultService(AssessmentResultRepository repo, StudentRepository studentRepo) {
        this.repo = repo;
        this.studentRepo = studentRepo;
    }

    @Transactional
    public AssessmentResult saveOrUpdate(Long studentId, AssessmentResult incoming) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        AssessmentResult record = repo.findByStudentIdAndAssessmentType(studentId, incoming.getAssessmentType())
                .orElse(new AssessmentResult());

        record.setStudent(student);
        record.setAssessmentType(incoming.getAssessmentType());
        record.setScoreObtained(incoming.getScoreObtained());
        record.setAssessmentDate(LocalDate.now());
        record.setMaxScore(100.0); // Standardizing to 100 for Placement metrics

        return repo.save(record);
    }

    public List<AssessmentResult> getAll(Long studentId) {
        return repo.findByStudentId(studentId);
    }
}
