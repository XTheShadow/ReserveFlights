
-- Create the 'Users' table
CREATE TABLE users (
   id INT AUTO_INCREMENT PRIMARY KEY,
   FirstName VARCHAR(50) NOT NULL,
   LastName VARCHAR(50) NOT NULL,
   Email VARCHAR(255) NOT NULL UNIQUE,
   PhoneNumber VARCHAR(15) NOT NULL, -- Format: +123456789012
   Gender VARCHAR(10) NOT NULL,      -- Values: "Male", "Female"
   Nationality VARCHAR(50) NOT NULL,
   DateOfBirth DATE NOT NULL,        -- DATE type (YYYY-MM-DD)
   PasswordHash VARCHAR(60) NOT NULL -- BCrypt hashed password (60 chars)
);


-- International Flights Table
CREATE TABLE international_flights (
    id INT AUTO_INCREMENT PRIMARY KEY,
    flight_number VARCHAR(10) NOT NULL UNIQUE,
    airline VARCHAR(100) NOT NULL,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_date DATE NOT NULL,
    departure_time TIME NOT NULL,
    arrival_date DATE NOT NULL,
    arrival_time TIME NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    available_seats INT NOT NULL CHECK (available_seats >= 0)
);


-- National Flights Table
CREATE TABLE national_flights (
    id INT AUTO_INCREMENT PRIMARY KEY,
    flight_number VARCHAR(10) NOT NULL UNIQUE,
    airline VARCHAR(100) NOT NULL,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_date DATE NOT NULL,
    departure_time TIME NOT NULL,
    arrival_date DATE NOT NULL,
    arrival_time TIME NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    available_seats INT NOT NULL CHECK (available_seats >= 0)
);


-- Reservations Table
CREATE TABLE reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    passenger_name VARCHAR(255) NOT NULL,
    flight_number VARCHAR(50) NOT NULL,
    departure_datetime DATETIME NOT NULL,
    seat_number VARCHAR(20) NOT NULL,
    contact_email VARCHAR(255) NOT NULL,
    contact_phone VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'Pending' CHECK (status IN ('Pending', 'Confirmed', 'Cancelled')),
    -- Optional foreign key (if users table exists)
    FOREIGN KEY (contact_email) REFERENCES users(Email) ON DELETE CASCADE
);


-- Tickets Table (Support Tickets)
CREATE TABLE tickets (
     TicketID INT AUTO_INCREMENT PRIMARY KEY,
     Name VARCHAR(255) NOT NULL,
     Email VARCHAR(255) NOT NULL,
     ProblemType VARCHAR(100) NOT NULL CHECK (ProblemType IN ('Booking', 'Cancellation', 'Refund', 'Other')),
     Description TEXT NOT NULL,
     SubmissionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- Optional foreign key (if users table exists)
     FOREIGN KEY (Email) REFERENCES users(Email) ON DELETE CASCADE
);