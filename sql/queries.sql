USE movie_ticket_booking;


-- =========================
-- 1. VIEW ALL MOVIES
-- =========================

SELECT *
FROM movies;


-- =========================
-- 2. VIEW ALL THEATRES
-- =========================

SELECT *
FROM theatres;


-- =========================
-- 3. FIND SHOWS
-- =========================

SELECT
    sh.show_id,
    m.title AS movie,
    t.theatre_name,
    t.city,
    sc.screen_name,
    sh.show_date,
    sh.start_time,
    sh.end_time
FROM shows sh
JOIN movies m
    ON sh.movie_id = m.movie_id
JOIN screens sc
    ON sh.screen_id = sc.screen_id
JOIN theatres t
    ON sc.theatre_id = t.theatre_id
ORDER BY sh.show_date, sh.start_time;


-- =========================
-- 4. VIEW AVAILABLE SEATS
-- =========================

SELECT
    ss.show_id,
    s.seat_id,
    s.seat_number,
    s.seat_type,
    s.price,
    ss.status
FROM show_seats ss
JOIN seats s
    ON ss.seat_id = s.seat_id
WHERE ss.show_id = 1
ORDER BY s.seat_id;


-- =========================
-- 5. VIEW ONLY AVAILABLE SEATS
-- =========================

SELECT
    s.seat_id,
    s.seat_number,
    s.seat_type,
    s.price
FROM show_seats ss
JOIN seats s
    ON ss.seat_id = s.seat_id
WHERE ss.show_id = 1
AND ss.status = 'AVAILABLE'
ORDER BY s.seat_id;


-- =========================
-- 6. VIEW BOOKED SEATS
-- =========================

SELECT
    s.seat_number,
    s.seat_type,
    s.price
FROM show_seats ss
JOIN seats s
    ON ss.seat_id = s.seat_id
WHERE ss.show_id = 1
AND ss.status = 'BOOKED';


-- =========================
-- 7. VIEW BOOKING DETAILS
-- =========================

SELECT
    b.booking_id,
    u.name AS customer,
    m.title AS movie,
    t.theatre_name,
    sh.show_date,
    sh.start_time,
    b.total_amount,
    b.status,
    b.booking_time
FROM bookings b
JOIN users u
    ON b.user_id = u.user_id
JOIN shows sh
    ON b.show_id = sh.show_id
JOIN movies m
    ON sh.movie_id = m.movie_id
JOIN screens sc
    ON sh.screen_id = sc.screen_id
JOIN theatres t
    ON sc.theatre_id = t.theatre_id
ORDER BY b.booking_time DESC;


-- =========================
-- 8. USER BOOKING HISTORY
-- =========================

SELECT
    b.booking_id,
    m.title AS movie,
    sh.show_date,
    sh.start_time,
    b.total_amount,
    b.status
FROM bookings b
JOIN shows sh
    ON b.show_id = sh.show_id
JOIN movies m
    ON sh.movie_id = m.movie_id
WHERE b.user_id = 1
ORDER BY b.booking_time DESC;


-- =========================
-- 9. BOOKED SEAT DETAILS
-- =========================

SELECT
    b.booking_id,
    s.seat_number,
    bs.price
FROM booking_seats bs
JOIN bookings b
    ON bs.booking_id = b.booking_id
JOIN seats s
    ON bs.seat_id = s.seat_id
ORDER BY b.booking_id;


-- =========================
-- 10. COUNT AVAILABLE SEATS
-- =========================

SELECT
    show_id,
    COUNT(*) AS available_seats
FROM show_seats
WHERE status = 'AVAILABLE'
GROUP BY show_id;


-- =========================
-- 11. COUNT BOOKED SEATS
-- =========================

SELECT
    show_id,
    COUNT(*) AS booked_seats
FROM show_seats
WHERE status = 'BOOKED'
GROUP BY show_id;


-- =========================
-- 12. CONCURRENCY LOCK QUERY
-- =========================
-- Used by Java during booking.
-- The selected row is locked until
-- the transaction is committed or rolled back.

SELECT
    ss.status,
    s.price
FROM show_seats ss
JOIN seats s
    ON ss.seat_id = s.seat_id
WHERE ss.show_id = 1
AND ss.seat_id = 3
FOR UPDATE;
