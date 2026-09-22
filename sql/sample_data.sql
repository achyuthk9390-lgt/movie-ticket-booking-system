USE movie_ticket_booking;


-- =========================
-- USERS
-- =========================

INSERT INTO users (name, email, phone) VALUES
('Rahul', 'rahul@gmail.com', '9000000001'),
('Ananya', 'ananya@gmail.com', '9000000002'),
('Kiran', 'kiran@gmail.com', '9000000003');


-- =========================
-- MOVIES
-- =========================

INSERT INTO movies
(title, language, duration_minutes, genre, release_date)
VALUES
('The Last Mission', 'English', 140, 'Action', '2026-08-01'),
('City Lights', 'Telugu', 125, 'Drama', '2026-08-15'),
('Final Destination', 'English', 110, 'Thriller', '2026-09-01');


-- =========================
-- THEATRES
-- =========================

INSERT INTO theatres
(theatre_name, city, address)
VALUES
('PVR Cinemas', 'Kurnool', 'Main Road'),
('INOX', 'Kurnool', 'City Centre');


-- =========================
-- SCREENS
-- =========================

INSERT INTO screens
(theatre_id, screen_name, total_seats)
VALUES
(1, 'Screen 1', 10),
(1, 'Screen 2', 10),
(2, 'Screen 1', 10);


-- =========================
-- SEATS
-- =========================

INSERT INTO seats
(screen_id, seat_number, seat_type, price)
VALUES
(1, 'A1', 'REGULAR', 150.00),
(1, 'A2', 'REGULAR', 150.00),
(1, 'A3', 'REGULAR', 150.00),
(1, 'A4', 'REGULAR', 150.00),
(1, 'A5', 'REGULAR', 150.00),
(1, 'B1', 'PREMIUM', 200.00),
(1, 'B2', 'PREMIUM', 200.00),
(1, 'B3', 'PREMIUM', 200.00),
(1, 'B4', 'PREMIUM', 200.00),
(1, 'B5', 'PREMIUM', 200.00);


INSERT INTO seats
(screen_id, seat_number, seat_type, price)
VALUES
(2, 'A1', 'REGULAR', 150.00),
(2, 'A2', 'REGULAR', 150.00),
(2, 'A3', 'REGULAR', 150.00),
(2, 'A4', 'REGULAR', 150.00),
(2, 'A5', 'REGULAR', 150.00),
(2, 'B1', 'PREMIUM', 200.00),
(2, 'B2', 'PREMIUM', 200.00),
(2, 'B3', 'PREMIUM', 200.00),
(2, 'B4', 'PREMIUM', 200.00),
(2, 'B5', 'PREMIUM', 200.00);


INSERT INTO seats
(screen_id, seat_number, seat_type, price)
VALUES
(3, 'A1', 'REGULAR', 150.00),
(3, 'A2', 'REGULAR', 150.00),
(3, 'A3', 'REGULAR', 150.00),
(3, 'A4', 'REGULAR', 150.00),
(3, 'A5', 'REGULAR', 150.00),
(3, 'B1', 'PREMIUM', 200.00),
(3, 'B2', 'PREMIUM', 200.00),
(3, 'B3', 'PREMIUM', 200.00),
(3, 'B4', 'PREMIUM', 200.00),
(3, 'B5', 'PREMIUM', 200.00);


-- =========================
-- SHOWS
-- =========================

INSERT INTO shows
(movie_id, screen_id, show_date, start_time, end_time)
VALUES
(1, 1, '2026-09-25', '10:00:00', '12:20:00'),
(1, 1, '2026-09-25', '18:00:00', '20:20:00'),
(2, 2, '2026-09-25', '14:00:00', '16:05:00'),
(3, 3, '2026-09-26', '19:00:00', '20:50:00');


-- =========================
-- CREATE SHOW SEATS
-- =========================

INSERT INTO show_seats (show_id, seat_id)
SELECT sh.show_id, s.seat_id
FROM shows sh
JOIN seats s
    ON s.screen_id = sh.screen_id;


-- =========================
-- SAMPLE BOOKED SEATS
-- =========================

UPDATE show_seats
SET status = 'BOOKED'
WHERE show_id = 1
AND seat_id IN (1, 2);
