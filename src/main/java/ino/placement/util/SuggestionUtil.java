package ino.placement.util;

import ino.placement.entity.AssessmentResult;

import java.util.*;

public class SuggestionUtil {

    public static List<String> getSuggestions(List<AssessmentResult> list) {

        Map<String, Double> avgMap = new HashMap<>();

        for (AssessmentResult a : list) {
            double percent = (a.getScoreObtained() * 100.0) / a.getMaxScore();

            avgMap.put(a.getAssessmentType(),
                    avgMap.getOrDefault(a.getAssessmentType(), 0.0) + percent);
        }

        List<String> suggestions = new ArrayList<>();

        for (String type : avgMap.keySet()) {
            double avg = avgMap.get(type);

            if (avg < 40) {
                suggestions.add(type + ": Weak → Practice basics, daily problems");
            } else if (avg < 70) {
                suggestions.add(type + ": Average → Improve consistency");
            }
        }

        return suggestions;
    }
}