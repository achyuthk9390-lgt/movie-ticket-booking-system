package com.movieticket.dao;

import com.movieticket.config.DBConnection;
import com.movieticket.model.Seat;
import com.movieticket.model.Show;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowDAO {

    public List<Show> getAllShows() throws SQLException {

        String sql = """
                SELECT show_id, movie_id, screen_id,
                       show_date, start_time, end_time
                FROM shows
                ORDER BY show_date, start_time
                """;

        List<Show> shows = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Show show = new Show(
                        rs.getInt("show_id"),
                        rs.getInt("movie_id"),
                        rs.getInt("screen_id"),
                        rs.getObject(
                                "show_date",
                                java.time.LocalDate.class
                        ),
                        rs.getObject(
                                "start_time",
                                java.time.LocalTime.class
                        ),
                        rs.getObject(
                                "end_time",
                                java.time.LocalTime.class
                        )
                );

                shows.add(show);
            }
        }

        return shows;
    }


    public Show findShowById(int showId) throws SQLException {

        String sql = """
                SELECT show_id, movie_id, screen_id,
                       show_date, start_time, end_time
                FROM shows
                WHERE show_id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, showId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Show(
                            rs.getInt("show_id"),
                            rs.getInt("movie_id"),
                            rs.getInt("screen_id"),
                            rs.getObject(
                                    "show_date",
                                    java.time.LocalDate.class
                            ),
                            rs.getObject(
                                    "start_time",
                                    java.time.LocalTime.class
                            ),
                            rs.getObject(
                                    "end_time",
                                    java.time.LocalTime.class
                            )
                    );
                }

                return null;
            }
        }
    }


    public List<Seat> getSeatsForShow(int showId) throws SQLException {

        String sql = """
                SELECT
                    s.seat_id,
                    s.screen_id,
                    s.seat_number,
                    s.seat_type,
                    s.price
                FROM show_seats ss
                JOIN seats s
                    ON ss.seat_id = s.seat_id
                WHERE ss.show_id = ?
                ORDER BY s.seat_id
                """;

        List<Seat> seats = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, showId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Seat seat = new Seat(
                            rs.getInt("seat_id"),
                            rs.getInt("screen_id"),
                            rs.getString("seat_number"),
                            rs.getString("seat_type"),
                            rs.getBigDecimal("price")
                    );

                    seats.add(seat);
                }
            }
        }

        return seats;
    }


    public List<Seat> getAvailableSeats(int showId) throws SQLException {

        String sql = """
                SELECT
                    s.seat_id,
                    s.screen_id,
                    s.seat_number,
                    s.seat_type,
                    s.price
                FROM show_seats ss
                JOIN seats s
                    ON ss.seat_id = s.seat_id
                WHERE ss.show_id = ?
                  AND ss.status = 'AVAILABLE'
                ORDER BY s.seat_id
                """;

        List<Seat> seats = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, showId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Seat seat = new Seat(
                            rs.getInt("seat_id"),
                            rs.getInt("screen_id"),
                            rs.getString("seat_number"),
                            rs.getString("seat_type"),
                            rs.getBigDecimal("price")
                    );

                    seats.add(seat);
                }
            }
        }

        return seats;
    }
}
