package ino.placement.util;

import ino.placement.entity.AssessmentResult;

import java.util.*;

public class ReadinessUtil {

    public static double calculateOverall(List<AssessmentResult> list) {

        if (list == null || list.isEmpty()) return 0;

        // Normalize & keep latest per type
        Map<String, AssessmentResult> latestMap = new HashMap<>();

        for (AssessmentResult a : list) {

            if (a.getAssessmentType() == null || a.getAssessmentDate() == null) continue;

            String type = a.getAssessmentType().trim().toLowerCase();

            if (!latestMap.containsKey(type) ||
                a.getAssessmentDate().isAfter(latestMap.get(type).getAssessmentDate())) {
                latestMap.put(type, a);
            }
        }

        double total = 0;
        int count = 0;

        for (AssessmentResult a : latestMap.values()) {

            if (a.getMaxScore() == null || a.getMaxScore() == 0) continue;

            double percent = (a.getScoreObtained() / a.getMaxScore()) * 100;

            // 🔥 Clamp between 0–100
            percent = Math.max(0, Math.min(100, percent));

            total += percent;
            count++;
        }

        return count == 0 ? 0 : total / count;
    }

    public static String classify(double score) {
        if (score < 40) return "NOT READY";
        else if (score < 70) return "PARTIALLY READY";
        else return "READY";
    }
}