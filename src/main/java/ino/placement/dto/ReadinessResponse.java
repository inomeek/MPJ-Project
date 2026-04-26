package ino.placement.dto;

public class ReadinessResponse {

    private String status;
    private String message;

    private int codingScore;
    private int aptitudeScore;
    private int coreScore;

    public ReadinessResponse(String status, String message,
                             int codingScore, int aptitudeScore, int coreScore) {
        this.status = status;
        this.message = message;
        this.codingScore = codingScore;
        this.aptitudeScore = aptitudeScore;
        this.coreScore = coreScore;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getCodingScore() {
        return codingScore;
    }

    public int getAptitudeScore() {
        return aptitudeScore;
    }

    public int getCoreScore() {
        return coreScore;
    }
}