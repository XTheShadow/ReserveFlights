# ReserveFlights

## Description
ReserveFlights is a Java-based flight reservation system with a graphical user interface (GUI) and database integration. It allows users to register, log in, and make flight reservations efficiently.

## Features
- User authentication (Login & Registration)
- Flight reservation system
- GUI built using Java Swing
- Database integration for storing user and flight details
- Configuration-based settings

## Installation
### Prerequisites
  - Java 11 or later
  - Gradle (included with Gradle Wrapper)
  - MySQL or compatible database

### Steps
1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/ReserveFlights.git
   cd ReserveFlights
   
2. Set up the database:
    - Create a MySQL database
    - Import src/resources/db/Schema.sql
    - Import src/resources/db/seed.sql (optional test data)

3. Configure the application:
    - Copy src/resources/config.example.properties to src/resources/config.properties
    - Edit config.properties to match your database settings
  
4. Build and run the project:
   ```sh
   git clone https://github.com/yourusername/ReserveFlights.git
   cd ReserveFlights

## Usage
  - Run the application and use the login/register functionality.
  - Navigate through the GUI to make reservations.
  - Admins can manage flight data through the database.

## Technologies Used
  - Java (Swing for GUI)
  - Gradle (Build system)
  - MySQL (Database)

## License
This project is licensed under the MIT License.
