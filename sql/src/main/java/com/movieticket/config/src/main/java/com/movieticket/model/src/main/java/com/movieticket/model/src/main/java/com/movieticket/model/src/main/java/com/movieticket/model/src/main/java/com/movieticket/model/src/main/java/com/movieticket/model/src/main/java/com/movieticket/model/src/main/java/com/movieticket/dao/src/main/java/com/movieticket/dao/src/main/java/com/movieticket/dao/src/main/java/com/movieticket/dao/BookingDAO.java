package com.movieticket.dao;

import com.movieticket.config.DBConnection;
import com.movieticket.model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public Booking findBookingById(int bookingId) throws SQLException {

        String sql = """
                SELECT booking_id, user_id, show_id,
                       booking_time, total_amount, status
                FROM bookings
                WHERE booking_id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Booking(
                            rs.getInt("booking_id"),
                            rs.getInt("user_id"),
                            rs.getInt("show_id"),
                            rs.getTimestamp("booking_time")
                                    .toLocalDateTime(),
                            rs.getBigDecimal("total_amount"),
                            rs.getString("status")
                    );
                }

                return null;
            }
        }
    }


    public List<Booking> getBookingsByUser(int userId)
            throws SQLException {

        String sql = """
                SELECT booking_id, user_id, show_id,
                       booking_time, total_amount, status
                FROM bookings
                WHERE user_id = ?
                ORDER BY booking_time DESC
                """;

        List<Booking> bookings = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Booking booking = new Booking(
                            rs.getInt("booking_id"),
                            rs.getInt("user_id"),
                            rs.getInt("show_id"),
                            rs.getTimestamp("booking_time")
                                    .toLocalDateTime(),
                            rs.getBigDecimal("total_amount"),
                            rs.getString("status")
                    );

                    bookings.add(booking);
                }
            }
        }

        return bookings;
    }


    public List<Integer> getSeatIdsForBooking(int bookingId)
            throws SQLException {

        String sql = """
                SELECT seat_id
                FROM booking_seats
                WHERE booking_id = ?
                ORDER BY seat_id
                """;

        List<Integer> seatIds = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    seatIds.add(rs.getInt("seat_id"));
                }
            }
        }

        return seatIds;
    }


    public List<Booking> getAllBookings() throws SQLException {

        String sql = """
                SELECT booking_id, user_id, show_id,
                       booking_time, total_amount, status
                FROM bookings
                ORDER BY booking_time DESC
                """;

        List<Booking> bookings = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("user_id"),
                        rs.getInt("show_id"),
                        rs.getTimestamp("booking_time")
                                .toLocalDateTime(),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("status")
                );

                bookings.add(booking);
            }
        }

        return bookings;
    }
}
