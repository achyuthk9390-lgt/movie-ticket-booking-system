# Movie Ticket Booking System

A Java-based Movie Ticket Booking System developed using Java, JDBC, MySQL, and Maven.

The system allows users to view movies and shows, check seat availability, book multiple seats, view bookings, and cancel bookings.

A major feature of the project is concurrency-safe seat booking using database transactions and row-level locking with `SELECT ... FOR UPDATE`.

---

## Features

- View available movies
- View movie shows
- View available seats for a show
- Create users
- Book one or multiple seats
- Calculate total booking amount
- View booking details
- View user booking history
- Cancel bookings
- Prevent double booking of the same seat
- Transaction management using commit and rollback
- Concurrent booking test

---

## Technologies Used

- Java 17
- JDBC
- MySQL
- Maven
- SQL
- GitHub

---

## Project Architecture

The project follows a simple layered architecture:

```text
Main
 |
 v
DAO Layer
 |
 v
Service Layer
 |
 v
JDBC
 |
 v
MySQL Database
