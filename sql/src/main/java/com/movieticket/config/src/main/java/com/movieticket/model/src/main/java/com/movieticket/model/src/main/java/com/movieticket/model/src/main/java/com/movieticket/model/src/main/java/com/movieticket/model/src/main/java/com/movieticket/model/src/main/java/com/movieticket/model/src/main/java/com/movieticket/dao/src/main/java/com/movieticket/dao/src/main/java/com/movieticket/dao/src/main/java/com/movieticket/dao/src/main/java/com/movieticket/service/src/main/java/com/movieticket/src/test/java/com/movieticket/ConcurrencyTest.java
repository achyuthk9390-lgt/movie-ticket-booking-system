package com.movieticket;

import com.movieticket.service.BookingService;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class ConcurrencyTest {

    public static void main(String[] args)
            throws InterruptedException {

        BookingService bookingService =
                new BookingService();

        /*
         * Test data
         *
         * User 1 and User 2
         * try to book the same seat.
         */
        int user1 = 1;
        int user2 = 2;

        int showId = 1;
        int seatId = 3;

        CountDownLatch startLatch =
                new CountDownLatch(1);

        Thread userThread1 = new Thread(() -> {

            try {

                startLatch.await();

                int bookingId =
                        bookingService.bookTickets(
                                user1,
                                showId,
                                Arrays.asList(seatId)
                        );

                System.out.println(
                        "User 1 booking successful."
                );

                System.out.println(
                        "Booking ID: " + bookingId
                );

            } catch (SQLException e) {

                System.out.println(
                        "User 1 booking failed: "
                                + e.getMessage()
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }

        });


        Thread userThread2 = new Thread(() -> {

            try {

                startLatch.await();

                int bookingId =
                        bookingService.bookTickets(
                                user2,
                                showId,
                                Arrays.asList(seatId)
                        );

                System.out.println(
                        "User 2 booking successful."
                );

                System.out.println(
                        "Booking ID: " + bookingId
                );

            } catch (SQLException e) {

                System.out.println(
                        "User 2 booking failed: "
                                + e.getMessage()
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }

        });


        userThread1.start();
        userThread2.start();

        /*
         * Both threads start booking
         * at approximately the same time.
         */
        startLatch.countDown();

        userThread1.join();
        userThread2.join();

        System.out.println();
        System.out.println(
                "Concurrency test completed."
        );
    }
}
