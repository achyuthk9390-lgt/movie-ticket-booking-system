package com.movieticket;

import com.movieticket.dao.BookingDAO;
import com.movieticket.dao.MovieDAO;
import com.movieticket.dao.ShowDAO;
import com.movieticket.dao.UserDAO;
import com.movieticket.model.Booking;
import com.movieticket.model.Movie;
import com.movieticket.model.Seat;
import com.movieticket.model.Show;
import com.movieticket.model.User;
import com.movieticket.service.BookingService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static final UserDAO userDAO = new UserDAO();
    private static final MovieDAO movieDAO = new MovieDAO();
    private static final ShowDAO showDAO = new ShowDAO();
    private static final BookingDAO bookingDAO = new BookingDAO();
    private static final BookingService bookingService =
            new BookingService();

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("   MOVIE TICKET BOOKING SYSTEM");
        System.out.println("=================================");

        while (true) {

            System.out.println();
            System.out.println("1. View Movies");
            System.out.println("2. View Shows");
            System.out.println("3. View Available Seats");
            System.out.println("4. Create User");
            System.out.println("5. Book Tickets");
            System.out.println("6. View Booking");
            System.out.println("7. View User Bookings");
            System.out.println("8. Cancel Booking");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");

            int choice = readInt();

            try {

                switch (choice) {

                    case 1:
                        viewMovies();
                        break;

                    case 2:
                        viewShows();
                        break;

                    case 3:
                        viewAvailableSeats();
                        break;

                    case 4:
                        createUser();
                        break;

                    case 5:
                        bookTickets();
                        break;

                    case 6:
                        viewBooking();
                        break;

                    case 7:
                        viewUserBookings();
                        break;

                    case 8:
                        cancelBooking();
                        break;

                    case 9:
                        System.out.println(
                                "Thank you for using the system."
                        );
                        return;

                    default:
                        System.out.println(
                                "Invalid choice."
                        );
                }

            } catch (SQLException e) {

                System.out.println(
                        "Database error: " + e.getMessage()
                );

            } catch (Exception e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }
    }


    private static void viewMovies() throws SQLException {

        System.out.println();
        System.out.println("========== MOVIES ==========");

        List<Movie> movies = movieDAO.getAllMovies();

        if (movies.isEmpty()) {
            System.out.println("No movies found.");
            return;
        }

        for (Movie movie : movies) {

            System.out.println(
                    "ID: " + movie.getMovieId()
                            + " | "
                            + movie.getTitle()
                            + " | "
                            + movie.getLanguage()
                            + " | "
                            + movie.getGenre()
                            + " | "
                            + movie.getDurationMinutes()
                            + " minutes"
            );
        }
    }


    private static void viewShows() throws SQLException {

        System.out.println();
        System.out.println("========== SHOWS ==========");

        List<Show> shows = showDAO.getAllShows();

        if (shows.isEmpty()) {
            System.out.println("No shows found.");
            return;
        }

        for (Show show : shows) {

            Movie movie =
                    movieDAO.findMovieById(show.getMovieId());

            System.out.println(
                    "Show ID: " + show.getShowId()
                            + " | Movie: "
                            + movie.getTitle()
                            + " | Screen: "
                            + show.getScreenId()
                            + " | Date: "
                            + show.getShowDate()
                            + " | Time: "
                            + show.getStartTime()
                            + " - "
                            + show.getEndTime()
            );
        }
    }


    private static void viewAvailableSeats()
            throws SQLException {

        System.out.println();
        System.out.println(
                "========== AVAILABLE SEATS =========="
        );

        System.out.print("Enter show ID: ");
        int showId = readInt();

        List<Seat> seats =
                showDAO.getAvailableSeats(showId);

        if (seats.isEmpty()) {
            System.out.println(
                    "No available seats."
            );
            return;
        }

        for (Seat seat : seats) {

            System.out.println(
                    "Seat ID: " + seat.getSeatId()
                            + " | Seat: "
                            + seat.getSeatNumber()
                            + " | Type: "
                            + seat.getSeatType()
                            + " | Price: "
                            + seat.getPrice()
            );
        }
    }


    private static void createUser()
            throws SQLException {

        System.out.println();
        System.out.println("========== CREATE USER ==========");

        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter phone: ");
        String phone = sc.nextLine();

        User user =
                new User(name, email, phone);

        int userId =
                userDAO.createUser(user);

        System.out.println(
                "User created successfully."
        );

        System.out.println(
                "User ID: " + userId
        );
    }


    private static void bookTickets()
            throws SQLException {

        System.out.println();
        System.out.println(
                "========== BOOK TICKETS =========="
        );

        System.out.print("Enter user ID: ");
        int userId = readInt();

        User user =
                userDAO.findUserById(userId);

        if (user == null) {
            System.out.println(
                    "User not found."
            );
            return;
        }

        System.out.print("Enter show ID: ");
        int showId = readInt();

        Show show =
                showDAO.findShowById(showId);

        if (show == null) {
            System.out.println(
                    "Show not found."
            );
            return;
        }

        viewAvailableSeatsForBooking(showId);

        System.out.print(
                "Enter number of seats: "
        );

        int count = readInt();

        if (count <= 0) {
            System.out.println(
                    "Invalid number of seats."
            );
            return;
        }

        List<Integer> seatIds =
                new ArrayList<>();

        for (int i = 1; i <= count; i++) {

            System.out.print(
                    "Enter seat ID " + i + ": "
            );

            seatIds.add(readInt());
        }

        int bookingId =
                bookingService.bookTickets(
                        userId,
                        showId,
                        seatIds
                );

        System.out.println();
        System.out.println(
                "Booking successful."
        );

        System.out.println(
                "Booking ID: " + bookingId
        );
    }


    private static void viewAvailableSeatsForBooking(
            int showId) throws SQLException {

        List<Seat> seats =
                showDAO.getAvailableSeats(showId);

        System.out.println();
        System.out.println(
                "Available seats:"
        );

        for (Seat seat : seats) {

            System.out.println(
                    "ID: " + seat.getSeatId()
                            + " | "
                            + seat.getSeatNumber()
                            + " | "
                            + seat.getSeatType()
                            + " | ₹"
                            + seat.getPrice()
            );
        }
    }


    private static void viewBooking()
            throws SQLException {

        System.out.println();
        System.out.println(
                "========== BOOKING DETAILS =========="
        );

        System.out.print("Enter booking ID: ");
        int bookingId = readInt();

        Booking booking =
                bookingDAO.findBookingById(
                        bookingId
                );

        if (booking == null) {
            System.out.println(
                    "Booking not found."
            );
            return;
        }

        System.out.println(
                "Booking ID: "
                        + booking.getBookingId()
        );

        System.out.println(
                "User ID: "
                        + booking.getUserId()
        );

        System.out.println(
                "Show ID: "
                        + booking.getShowId()
        );

        System.out.println(
                "Booking Time: "
                        + booking.getBookingTime()
        );

        System.out.println(
                "Total Amount: ₹"
                        + booking.getTotalAmount()
        );

        System.out.println(
                "Status: "
                        + booking.getStatus()
        );

        List<Integer> seatIds =
                bookingDAO.getSeatIdsForBooking(
                        bookingId
                );

        System.out.println(
                "Seat IDs: " + seatIds
        );
    }


    private static void viewUserBookings()
            throws SQLException {

        System.out.println();
        System.out.println(
                "========== USER BOOKINGS =========="
        );

        System.out.print("Enter user ID: ");
        int userId = readInt();

        List<Booking> bookings =
                bookingDAO.getBookingsByUser(
                        userId
                );

        if (bookings.isEmpty()) {
            System.out.println(
                    "No bookings found."
            );
            return;
        }

        for (Booking booking : bookings) {

            System.out.println(
                    "Booking ID: "
                            + booking.getBookingId()
                            + " | Show ID: "
                            + booking.getShowId()
                            + " | Amount: ₹"
                            + booking.getTotalAmount()
                            + " | Status: "
                            + booking.getStatus()
                            + " | Time: "
                            + booking.getBookingTime()
            );
        }
    }


    private static void cancelBooking()
            throws SQLException {

        System.out.println();
        System.out.println(
                "========== CANCEL BOOKING =========="
        );

        System.out.print("Enter booking ID: ");
        int bookingId = readInt();

        System.out.print("Enter user ID: ");
        int userId = readInt();

        bookingService.cancelBooking(
                bookingId,
                userId
        );

        System.out.println(
                "Booking cancelled successfully."
        );
    }


    private static int readInt() {

        while (true) {

            try {

                String input =
                        sc.nextLine().trim();

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.print(
                        "Enter a valid number: "
                );
            }
        }
    }
}
