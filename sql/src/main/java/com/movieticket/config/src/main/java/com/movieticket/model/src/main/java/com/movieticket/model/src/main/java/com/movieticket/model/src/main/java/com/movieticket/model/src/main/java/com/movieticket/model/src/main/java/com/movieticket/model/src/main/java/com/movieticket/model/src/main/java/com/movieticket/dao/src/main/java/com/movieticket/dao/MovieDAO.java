package com.movieticket.dao;

import com.movieticket.config.DBConnection;
import com.movieticket.model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    public int createMovie(Movie movie) throws SQLException {

        String sql = """
                INSERT INTO movies
                (title, language, duration_minutes, genre, release_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getLanguage());
            ps.setInt(3, movie.getDurationMinutes());
            ps.setString(4, movie.getGenre());
            ps.setObject(5, movie.getReleaseDate());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }

                throw new SQLException("Failed to create movie.");
            }
        }
    }

    public Movie findMovieById(int movieId) throws SQLException {

        String sql = """
                SELECT movie_id, title, language,
                       duration_minutes, genre, release_date
                FROM movies
                WHERE movie_id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, movieId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Movie(
                            rs.getInt("movie_id"),
                            rs.getString("title"),
                            rs.getString("language"),
                            rs.getInt("duration_minutes"),
                            rs.getString("genre"),
                            rs.getObject(
                                    "release_date",
                                    java.time.LocalDate.class
                            )
                    );
                }

                return null;
            }
        }
    }

    public List<Movie> getAllMovies() throws SQLException {

        String sql = """
                SELECT movie_id, title, language,
                       duration_minutes, genre, release_date
                FROM movies
                ORDER BY title
                """;

        List<Movie> movies = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Movie movie = new Movie(
                        rs.getInt("movie_id"),
                        rs.getString("title"),
                        rs.getString("language"),
                        rs.getInt("duration_minutes"),
                        rs.getString("genre"),
                        rs.getObject(
                                "release_date",
                                java.time.LocalDate.class
                        )
                );

                movies.add(movie);
            }
        }

        return movies;
    }
}
