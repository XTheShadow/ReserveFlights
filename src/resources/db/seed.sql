-- Sample Users
INSERT INTO users (FirstName, LastName, Email, PhoneNumber, Gender, Nationality, DateOfBirth, PasswordHash)
VALUES
    ('Ahmed', 'Yilmaz', 'ahmed.yilmaz@example.com', '+905551234567', 'Male', 'Türkiye', '1995-03-15', '$2a$12$xyz123abcABC.5H8pLzQeO7eWtDq1Z2vX3y'),
    ('Emma', 'Wilson', 'emma.wilson@example.com', '+441632960022', 'Female', 'UK', '1988-07-22', '$2a$12$def456ghiJKL.9I7qMzRd.1aBcDe2f3GhIj');


-- Sample International Flights (Turkish Airlines)
INSERT INTO international_flights
(flight_number, airline, source, destination, departure_date, departure_time, arrival_date, arrival_time, price, available_seats)
VALUES
    ('TK001', 'Turkish Airlines', 'Istanbul', 'New York', '2024-11-01', '08:30:00', '2024-11-01', '15:45:00', 850.00, 200),
    ('TK202', 'Turkish Airlines', 'Ankara', 'London', '2024-11-02', '10:00:00', '2024-11-02', '13:30:00', 650.00, 150);


-- Sample National Flights (Turkish Airlines)
INSERT INTO national_flights
(flight_number, airline, source, destination, departure_date, departure_time, arrival_date, arrival_time, price, available_seats)
VALUES
    ('TK101', 'Turkish Airlines', 'Istanbul', 'Ankara', '2024-11-01', '07:00:00', '2024-11-01', '08:00:00', 150.00, 50),
    ('TK102', 'Turkish Airlines', 'Izmir', 'Antalya', '2024-11-02', '09:30:00', '2024-11-02', '10:30:00', 120.00, 30);


-- Sample Reservations
INSERT INTO reservations
(passenger_name, flight_number, departure_datetime, seat_number, contact_email, contact_phone, status)
VALUES
    ('Ahmed Yilmaz', 'TK001', '2024-11-01 08:30:00', 'A12', 'ahmed.yilmaz@example.com', '+905551234567', 'Confirmed'),
    ('Emma Wilson', 'TK202', '2024-11-02 10:00:00', 'B07', 'emma.wilson@example.com', '+441632960022', 'Pending');


-- Sample Support Tickets
INSERT INTO tickets
(Name, Email, ProblemType, Description)
VALUES
    ('Ahmed Yilmaz', 'ahmed.yilmaz@example.com', 'Booking', 'Unable to select seat for flight TK001.'),
    ('Emma Wilson', 'emma.wilson@example.com', 'Refund', 'Requesting refund for cancelled flight.');