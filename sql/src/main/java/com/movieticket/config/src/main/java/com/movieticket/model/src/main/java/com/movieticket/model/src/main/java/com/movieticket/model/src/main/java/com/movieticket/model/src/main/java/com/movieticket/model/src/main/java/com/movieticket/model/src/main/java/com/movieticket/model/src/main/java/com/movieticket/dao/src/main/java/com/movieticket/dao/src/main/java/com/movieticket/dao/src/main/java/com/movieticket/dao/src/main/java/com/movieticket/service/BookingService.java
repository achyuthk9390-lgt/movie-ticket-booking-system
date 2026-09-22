package com.movieticket.service;

import com.movieticket.config.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookingService {

    public int bookTickets(int userId, int showId, List<Integer> seatIds)
            throws SQLException {

        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one seat is required."
            );
        }

        List<Integer> uniqueSeatIds = new ArrayList<>();

        for (Integer seatId : seatIds) {
            if (seatId == null || seatId <= 0) {
                throw new IllegalArgumentException(
                        "Invalid seat ID."
                );
            }

            if (!uniqueSeatIds.contains(seatId)) {
                uniqueSeatIds.add(seatId);
            }
        }

        uniqueSeatIds.sort(Integer::compareTo);

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {

                BigDecimal totalAmount = BigDecimal.ZERO;

                /*
                 * Lock every requested seat row.
                 *
                 * FOR UPDATE prevents two transactions
                 * from booking the same seat simultaneously.
                 */
                String lockSql = """
                        SELECT ss.status, s.price
                        FROM show_seats ss
                        JOIN seats s
                            ON ss.seat_id = s.seat_id
                        WHERE ss.show_id = ?
                          AND ss.seat_id = ?
                        FOR UPDATE
                        """;

                try (PreparedStatement ps =
                             con.prepareStatement(lockSql)) {

                    for (int seatId : uniqueSeatIds) {

                        ps.setInt(1, showId);
                        ps.setInt(2, seatId);

                        try (ResultSet rs = ps.executeQuery()) {

                            if (!rs.next()) {
                                throw new SQLException(
                                        "Seat " + seatId +
                                        " does not exist for this show."
                                );
                            }

                            String status = rs.getString("status");

                            if (!"AVAILABLE".equals(status)) {
                                throw new SQLException(
                                        "Seat " + seatId +
                                        " is already booked."
                                );
                            }

                            totalAmount = totalAmount.add(
                                    rs.getBigDecimal("price")
                            );
                        }
                    }
                }

                /*
                 * Create booking record.
                 */
                String bookingSql = """
                        INSERT INTO bookings
                        (user_id, show_id, total_amount, status)
                        VALUES (?, ?, ?, 'CONFIRMED')
                        """;

                int bookingId;

                try (PreparedStatement ps =
                             con.prepareStatement(
                                     bookingSql,
                                     Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, userId);
                    ps.setInt(2, showId);
                    ps.setBigDecimal(3, totalAmount);

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "Could not create booking."
                            );
                        }

                        bookingId = rs.getInt(1);
                    }
                }

                /*
                 * Change seats from AVAILABLE to BOOKED.
                 */
                String updateSeatSql = """
                        UPDATE show_seats
                        SET status = 'BOOKED'
                        WHERE show_id = ?
                          AND seat_id = ?
                          AND status = 'AVAILABLE'
                        """;

                /*
                 * Store seats belonging to the booking.
                 */
                String bookingSeatSql = """
                        INSERT INTO booking_seats
                        (booking_id, seat_id, price)
                        SELECT ?, seat_id, price
                        FROM seats
                        WHERE seat_id = ?
                        """;

                try (PreparedStatement updatePs =
                             con.prepareStatement(updateSeatSql);
                     PreparedStatement bookingSeatPs =
                             con.prepareStatement(bookingSeatSql)) {

                    for (int seatId : uniqueSeatIds) {

                        updatePs.setInt(1, showId);
                        updatePs.setInt(2, seatId);

                        int updatedRows =
                                updatePs.executeUpdate();

                        if (updatedRows != 1) {
                            throw new SQLException(
                                    "Seat " + seatId +
                                    " became unavailable."
                            );
                        }

                        bookingSeatPs.setInt(1, bookingId);
                        bookingSeatPs.setInt(2, seatId);

                        bookingSeatPs.executeUpdate();
                    }
                }

                /*
                 * All operations succeeded.
                 */
                con.commit();

                return bookingId;

            } catch (Exception e) {

                /*
                 * If anything fails, undo every operation
                 * performed inside this transaction.
                 */
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                if (e instanceof SQLException) {
                    throw (SQLException) e;
                }

                throw new SQLException(
                        "Booking failed.",
                        e
                );
            }
        }
    }


    public void cancelBooking(int bookingId, int userId)
            throws SQLException {

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {

                /*
                 * Lock the booking record.
                 */
                String bookingSql = """
                        SELECT show_id, status
                        FROM bookings
                        WHERE booking_id = ?
                          AND user_id = ?
                        FOR UPDATE
                        """;

                int showId;
                String status;

                try (PreparedStatement ps =
                             con.prepareStatement(bookingSql)) {

                    ps.setInt(1, bookingId);
                    ps.setInt(2, userId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "Booking not found."
                            );
                        }

                        showId = rs.getInt("show_id");
                        status = rs.getString("status");
                    }
                }

                if ("CANCELLED".equals(status)) {
                    throw new SQLException(
                            "Booking is already cancelled."
                    );
                }

                /*
                 * Lock seats belonging to this booking.
                 */
                String seatSql = """
                        SELECT seat_id
                        FROM booking_seats
                        WHERE booking_id = ?
                        ORDER BY seat_id
                        FOR UPDATE
                        """;

                List<Integer> seatIds = new ArrayList<>();

                try (PreparedStatement ps =
                             con.prepareStatement(seatSql)) {

                    ps.setInt(1, bookingId);

                    try (ResultSet rs = ps.executeQuery()) {

                        while (rs.next()) {
                            seatIds.add(
                                    rs.getInt("seat_id")
                            );
                        }
                    }
                }

                /*
                 * Release the seats.
                 */
                String updateSeatSql = """
                        UPDATE show_seats
                        SET status = 'AVAILABLE'
                        WHERE show_id = ?
                          AND seat_id = ?
                        """;

                try (PreparedStatement ps =
                             con.prepareStatement(updateSeatSql)) {

                    for (int seatId : seatIds) {

                        ps.setInt(1, showId);
                        ps.setInt(2, seatId);

                        ps.executeUpdate();
                    }
                }

                /*
                 * Mark booking as cancelled.
                 */
                String cancelSql = """
                        UPDATE bookings
                        SET status = 'CANCELLED'
                        WHERE booking_id = ?
                          AND user_id = ?
                        """;

                try (PreparedStatement ps =
                             con.prepareStatement(cancelSql)) {

                    ps.setInt(1, bookingId);
                    ps.setInt(2, userId);

                    ps.executeUpdate();
                }

                con.commit();

            } catch (Exception e) {

                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                if (e instanceof SQLException) {
                    throw (SQLException) e;
                }

                throw new SQLException(
                        "Cancellation failed.",
                        e
                );
            }
        }
    }
}
