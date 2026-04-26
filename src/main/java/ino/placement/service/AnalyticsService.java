package ino.placement.service;

import ino.placement.entity.AssessmentResult;
import ino.placement.repository.AssessmentResultRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final AssessmentResultRepository repo;

    public AnalyticsService(AssessmentResultRepository repo) {
        this.repo = repo;
    }

    public List<AssessmentResult> getTimeline(Long studentId) {
        return repo.findByStudentIdOrderByAssessmentDate(studentId);
    }

    public Map<String, Double> getLatestScores(Long studentId) {
        return repo.findByStudentId(studentId).stream()
            .collect(Collectors.toMap(
                AssessmentResult::getAssessmentType,
                AssessmentResult::getScoreObtained,
                (existing, replacement) -> replacement // Keep the latest if duplicate keys
            ));
    }
}