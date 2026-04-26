package ino.placement.service;

import ino.placement.dto.ReadinessResponse;
import ino.placement.repository.AssessmentResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadinessService {

    @Autowired
    private AssessmentResultRepository repository;

    public ReadinessResponse evaluateStudent(Long studentId) {

        double coding = getPercentage(studentId, "Coding");
        double aptitude = getPercentage(studentId, "Aptitude");
        double core = getPercentage(studentId, "Interview"); // using Interview as core

        String status;
        String message;

        if (coding < 50) {
            status = "NOT_READY";
            message = "Your coding skills are below required level. Focus on DSA and problem solving.";
        } else if (aptitude < 40) {
            status = "PARTIALLY_READY";
            message = "You are good in coding but need improvement in aptitude.";
        } else if (coding >= 70 && aptitude >= 70 && core >= 70) {
            status = "INTERVIEW_READY";
            message = "You are well prepared for technical interviews.";
        } else {
            status = "PARTIALLY_READY";
            message = "You have moderate performance. Improve weak areas.";
        }

        return new ReadinessResponse(
                status,
                message,
                (int) coding,
                (int) aptitude,
                (int) core
        );
    }

    private double getPercentage(Long studentId, String type) {
        return repository
                .findTopByStudent_IdAndAssessmentTypeOrderByAssessmentDateDesc(studentId, type)
                .map(result -> {
                    if (result.getMaxScore() == 0) return 0.0;
                    return (result.getScoreObtained() / result.getMaxScore()) * 100;
                })
                .orElse(0.0);
    }
}

