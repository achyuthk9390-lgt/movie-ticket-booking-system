package com.movieticket.model;

import java.time.LocalDate;

public class Movie {

    private int movieId;
    private String title;
    private String language;
    private int durationMinutes;
    private String genre;
    private LocalDate releaseDate;

    public Movie() {
    }

    public Movie(int movieId, String title, String language,
                 int durationMinutes, String genre,
                 LocalDate releaseDate) {

        this.movieId = movieId;
        this.title = title;
        this.language = language;
        this.durationMinutes = durationMinutes;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public Movie(String title, String language,
                 int durationMinutes, String genre,
                 LocalDate releaseDate) {

        this.title = title;
        this.language = language;
        this.durationMinutes = durationMinutes;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", title='" + title + '\'' +
                ", language='" + language + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", genre='" + genre + '\'' +
                ", releaseDate=" + releaseDate +
                '}';
    }
}
