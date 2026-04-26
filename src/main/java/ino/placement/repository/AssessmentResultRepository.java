package ino.placement.repository;

import ino.placement.entity.AssessmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {
    
    // Finds all results for a specific student
    List<AssessmentResult> findByStudentId(Long studentId);
    
    // Finds results ordered by date (Required for Analytics trend)
    List<AssessmentResult> findByStudentIdOrderByAssessmentDate(Long studentId);
    
    // Checks if a student already has a specific type (Required for Upsert logic)
    Optional<AssessmentResult> findByStudentIdAndAssessmentType(Long studentId, String assessmentType);

    Optional<AssessmentResult> findTopByStudent_IdAndAssessmentTypeOrderByAssessmentDateDesc(
        Long studentId, String assessmentType
);
}