package com.movieticket.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Show {

    private int showId;
    private int movieId;
    private int screenId;
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public Show() {
    }

    public Show(int showId, int movieId, int screenId,
                LocalDate showDate,
                LocalTime startTime,
                LocalTime endTime) {

        this.showId = showId;
        this.movieId = movieId;
        this.screenId = screenId;
        this.showDate = showDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Show(int movieId, int screenId,
                LocalDate showDate,
                LocalTime startTime,
                LocalTime endTime) {

        this.movieId = movieId;
        this.screenId = screenId;
        this.showDate = showDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Show{" +
                "showId=" + showId +
                ", movieId=" + movieId +
                ", screenId=" + screenId +
                ", showDate=" + showDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
