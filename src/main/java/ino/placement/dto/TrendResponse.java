package ino.placement.dto;

import java.time.LocalDate;

public class TrendResponse {
    private LocalDate date;
    private Integer score;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}