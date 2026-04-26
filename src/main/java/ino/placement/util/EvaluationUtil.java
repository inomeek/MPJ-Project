package ino.placement.util;

public class EvaluationUtil {

    public static String evaluate(int score, int maxScore) {
        double percent = (score * 100.0) / maxScore;

        if (percent < 40) return "WEAK";
        else if (percent < 70) return "AVERAGE";
        else return "STRONG";
    }
}