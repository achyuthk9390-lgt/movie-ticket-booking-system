DROP DATABASE IF EXISTS movie_ticket_booking;

CREATE DATABASE movie_ticket_booking;

USE movie_ticket_booking;


-- =========================
-- USERS
-- =========================

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- =========================
-- MOVIES
-- =========================

CREATE TABLE movies (
    movie_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    language VARCHAR(50),
    duration_minutes INT NOT NULL,
    genre VARCHAR(100),
    release_date DATE
);


-- =========================
-- THEATRES
-- =========================

CREATE TABLE theatres (
    theatre_id INT PRIMARY KEY AUTO_INCREMENT,
    theatre_name VARCHAR(150) NOT NULL,
    city VARCHAR(100) NOT NULL,
    address VARCHAR(255)
);


-- =========================
-- SCREENS
-- =========================

CREATE TABLE screens (
    screen_id INT PRIMARY KEY AUTO_INCREMENT,
    theatre_id INT NOT NULL,
    screen_name VARCHAR(100) NOT NULL,
    total_seats INT NOT NULL,

    FOREIGN KEY (theatre_id)
        REFERENCES theatres(theatre_id)
);


-- =========================
-- SEATS
-- =========================

CREATE TABLE seats (
    seat_id INT PRIMARY KEY AUTO_INCREMENT,
    screen_id INT NOT NULL,
    seat_number VARCHAR(20) NOT NULL,
    seat_type ENUM('REGULAR', 'PREMIUM') DEFAULT 'REGULAR',
    price DECIMAL(10,2) NOT NULL,

    UNIQUE (screen_id, seat_number),

    FOREIGN KEY (screen_id)
        REFERENCES screens(screen_id)
);


-- =========================
-- SHOWS
-- =========================

CREATE TABLE shows (
    show_id INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INT NOT NULL,
    screen_id INT NOT NULL,
    show_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,

    FOREIGN KEY (movie_id)
        REFERENCES movies(movie_id),

    FOREIGN KEY (screen_id)
        REFERENCES screens(screen_id),

    UNIQUE (screen_id, show_date, start_time)
);


-- =========================
-- SHOW SEATS
-- =========================
-- This table stores seat availability
-- separately for every show.

CREATE TABLE show_seats (
    show_id INT NOT NULL,
    seat_id INT NOT NULL,

    status ENUM('AVAILABLE', 'BOOKED')
        DEFAULT 'AVAILABLE',

    PRIMARY KEY (show_id, seat_id),

    FOREIGN KEY (show_id)
        REFERENCES shows(show_id)
        ON DELETE CASCADE,

    FOREIGN KEY (seat_id)
        REFERENCES seats(seat_id)
);


-- =========================
-- BOOKINGS
-- =========================

CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    show_id INT NOT NULL,

    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    total_amount DECIMAL(10,2) NOT NULL,

    status ENUM('CONFIRMED', 'CANCELLED')
        DEFAULT 'CONFIRMED',

    FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    FOREIGN KEY (show_id)
        REFERENCES shows(show_id)
);


-- =========================
-- BOOKING SEATS
-- =========================

CREATE TABLE booking_seats (
    booking_id INT NOT NULL,
    seat_id INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,

    PRIMARY KEY (booking_id, seat_id),

    FOREIGN KEY (booking_id)
        REFERENCES bookings(booking_id)
        ON DELETE CASCADE,

    FOREIGN KEY (seat_id)
        REFERENCES seats(seat_id)
);


-- =========================
-- INDEXES
-- =========================

CREATE INDEX idx_shows_movie_date
ON shows(movie_id, show_date);

CREATE INDEX idx_show_seats_status
ON show_seats(show_id, status);

CREATE INDEX idx_bookings_user
ON bookings(user_id);
