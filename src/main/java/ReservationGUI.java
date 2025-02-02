import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class ReservationGUI {
    private JFrame frame;
    private JComboBox<String> departureComboBox;
    private JComboBox<String> arrivalComboBox;
    private JComboBox<Integer> passengersComboBox;
    private JButton searchFlightsButton;
    private JDateChooser departureDateChooser;
    private JDateChooser returnDateChooser;
    private JLabel datesLabel;
    private JLabel returnDateLabel;
    private DatabaseConnection connection;
    private JRadioButton internationalRadioButton;
    private JRadioButton domesticRadioButton;

    private List<JComboBox<String>> departureComboBoxes;
    private List<JComboBox<String>> arrivalComboBoxes;
    private List<JComponent> multiCityComponents;

    public ReservationGUI() {
        frame = new JFrame("Flight Reservation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 350);
        frame.setLayout(null); // Use absolute positioning

        departureComboBoxes = new ArrayList<>();
        arrivalComboBoxes = new ArrayList<>();
        multiCityComponents = new ArrayList<>();

        // Initialize the navigation bar
        initializeNavigationBar();

        // Radio buttons for trip options
        JRadioButton roundTrip = new JRadioButton("Round trip");
        JRadioButton oneWay = new JRadioButton("One way");
        JRadioButton multiCity = new JRadioButton("Multi-city");

        ButtonGroup tripOptions = new ButtonGroup();
        tripOptions.add(roundTrip);
        tripOptions.add(oneWay);
        tripOptions.add(multiCity);

        roundTrip.setBounds(10, 20, 100, 25);
        oneWay.setBounds(120, 20, 100, 25);
        multiCity.setBounds(230, 20, 150, 25);
        frame.add(roundTrip);
        frame.add(oneWay);
        frame.add(multiCity);

        roundTrip.addActionListener(e -> {
            boolean isRoundTripSelected = roundTrip.isSelected();
            returnDateLabel.setVisible(isRoundTripSelected);
            returnDateChooser.setVisible(isRoundTripSelected);
            removeMultiCityComponents();
            searchFlightsButton.setBounds(500, 170, 150, 30);
            passengersComboBox.setBounds(140, 170, 50, 25);

        });

        oneWay.addActionListener(e -> {
            boolean isOneWaySelected = oneWay.isSelected();
            returnDateLabel.setVisible(!isOneWaySelected);
            returnDateChooser.setVisible(!isOneWaySelected);
            removeMultiCityComponents();
            searchFlightsButton.setBounds(500, 170, 150, 30);
            passengersComboBox.setBounds(140, 170, 50, 25);
        });

        // Radio buttons for international and domestic flights
        internationalRadioButton = new JRadioButton("International");
        domesticRadioButton = new JRadioButton("Domestic");

        ButtonGroup flightTypeOptions = new ButtonGroup();
        flightTypeOptions.add(internationalRadioButton);
        flightTypeOptions.add(domesticRadioButton);

        internationalRadioButton.setBounds(10, 50, 100, 25);
        domesticRadioButton.setBounds(120, 50, 100, 25);
        frame.add(internationalRadioButton);
        frame.add(domesticRadioButton);

        // Add action listeners to the radio buttons for international and domestic flights
        internationalRadioButton.addActionListener(e -> populateLocations());
        domesticRadioButton.addActionListener(e -> populateLocations());

        // Add action listener to toggle multi-city components
        multiCity.addActionListener(e -> {
            boolean isMultiCitySelected = multiCity.isSelected();
            returnDateLabel.setVisible(!isMultiCitySelected);
            returnDateChooser.setVisible(!isMultiCitySelected);

            // Show or hide the "Add flight" button based on the multi-city selection
            if (isMultiCitySelected) {
                // Create and add the "Add flight" button
                JButton addFlightButton = new JButton("Add Flight");
                addFlightButton.setBounds(600, 135, 100, 25); // Adjust the position as needed
                frame.add(addFlightButton);
                addFlightButton.addActionListener(addFlightEvent -> addNewFlightSegment());

                multiCityComponents.add(addFlightButton);
                frame.revalidate(); // Refresh the frame to reflect the changes
                frame.repaint();
            } else {
                removeMultiCityComponents();
            }
        });

        // Initialize DatabaseConnection
        connection = new DatabaseConnection();

        // From label and combo box
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setBounds(10, 90, 80, 25);
        frame.add(fromLabel);

        departureComboBox = new JComboBox<>(); // Initialize departureComboBox first
        departureComboBox.setBounds(100, 90, 200, 25);
        frame.add(departureComboBox);
        departureComboBoxes.add(departureComboBox);

        // To label and combo box
        JLabel toLabel = new JLabel("To:");
        toLabel.setBounds(350, 90, 180, 25);
        frame.add(toLabel);

        arrivalComboBox = new JComboBox<>(); // Initialize arrivalComboBox
        arrivalComboBox.setBounds(440, 90, 200, 25);
        frame.add(arrivalComboBox);
        arrivalComboBoxes.add(arrivalComboBox);

        // Now call populateLocations
        populateLocations();

        // Dates label and date choosers
        datesLabel = new JLabel("Departure Date:");
        datesLabel.setBounds(10, 135, 120, 25);
        frame.add(datesLabel);

        departureDateChooser = new JDateChooser();
        departureDateChooser.setBounds(140, 135, 150, 25);
        frame.add(departureDateChooser);

        returnDateLabel = new JLabel("Return Date:");
        returnDateLabel.setBounds(300, 135, 120, 25);
        frame.add(returnDateLabel);

        returnDateChooser = new JDateChooser();
        returnDateChooser.setBounds(440, 135, 150, 25);
        frame.add(returnDateChooser);

        // Passengers label and combo box
        JLabel passengersLabel = new JLabel("Passengers:");
        passengersLabel.setBounds(10, 170, 120, 25);
        frame.add(passengersLabel);

        Integer[] passengerNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        passengersComboBox = new JComboBox<>(passengerNumbers);
        passengersComboBox.setBounds(140, 170, 50, 25);
        frame.add(passengersComboBox);

        // Search Flights button
        searchFlightsButton = new JButton("Search flights");
        searchFlightsButton.setBounds(500, 170, 150, 30);
        searchFlightsButton.setBackground(Color.RED);
        searchFlightsButton.setForeground(Color.WHITE);
        frame.add(searchFlightsButton);
        searchFlightsButton.addActionListener(e -> searchFlights());

        // Set frame visibility
        frame.setVisible(true);
    }

    private void initializeNavigationBar() {
        // Add the navigation bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Align the menu bar to the left
        JMenu bookFlightMenu = new JMenu("Book Flight");
        JMenu helpMenu = new JMenu("Help");
        JMenu signUpMenu = new JMenu("Sign Up");
        JMenu signInMenu = new JMenu("Sign In");

        bookFlightMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                frame.dispose(); // Close the register page
                new ReservationGUI();
            }
        });

        helpMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                frame.dispose(); // Close the register page
                new HelpGUI();;
            }
        });

        signUpMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                frame.dispose(); // Close the register page
                new RegisterGUI();
            }
        });

        signInMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                frame.dispose(); // Close the register page
                new LoginGUI();
            }
        });

        menuBar.add(bookFlightMenu);
        menuBar.add(helpMenu);
        menuBar.add(signUpMenu);
        menuBar.add(signInMenu);


        frame.setJMenuBar(menuBar);

    }

    private void populateLocations() {
        try (Connection conn = connection.getConnection()) {
            String selectedTable = internationalRadioButton.isSelected() ? "international_flights" : "domestic_flights";
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT DISTINCT source, destination FROM " + selectedTable);

            // Clear and repopulate all departure and arrival comboboxes
            for (JComboBox<String> comboBox : departureComboBoxes) {
                comboBox.removeAllItems();
            }
            for (JComboBox<String> comboBox : arrivalComboBoxes) {
                comboBox.removeAllItems();
            }

            while (resultSet.next()) {
                String source = resultSet.getString("source");
                String destination = resultSet.getString("destination");
                for (JComboBox<String> comboBox : departureComboBoxes) {
                    comboBox.addItem(source);
                }
                for (JComboBox<String> comboBox : arrivalComboBoxes) {
                    comboBox.addItem(destination);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to fetch locations from the database: " + e.getMessage());
        }
    }

    private void addNewFlightSegment() {
        // Determine the y-coordinate for the new set of components based on the number of existing flight segments
        int currentComponentCount = frame.getContentPane().getComponentCount();
        int baseY = 190; // Starting y position for the new components
        int incrementY = 50; // Space between each set of components
        int numSegments = (currentComponentCount - 16) / 8; // Adjust this to reflect initial non-segment components

        // Create new labels and combo boxes for the additional flight segment
        JLabel newFromLabel = new JLabel("From:");
        newFromLabel.setBounds(10, baseY + numSegments * incrementY * 2, 80, 25);
        frame.add(newFromLabel);
        multiCityComponents.add(newFromLabel);

        JComboBox<String> newDepartureComboBox = new JComboBox<>();
        newDepartureComboBox.setBounds(100, baseY + numSegments * incrementY * 2, 200, 25);
        frame.add(newDepartureComboBox);
        departureComboBoxes.add(newDepartureComboBox); // Add to the list of departure comboboxes
        multiCityComponents.add(newDepartureComboBox);

        JLabel newToLabel = new JLabel("To:");
        newToLabel.setBounds(350, baseY + numSegments * incrementY * 2, 80, 25);
        frame.add(newToLabel);
        multiCityComponents.add(newToLabel);

        JComboBox<String> newArrivalComboBox = new JComboBox<>();
        newArrivalComboBox.setBounds(440, baseY + numSegments * incrementY * 2, 200, 25);
        frame.add(newArrivalComboBox);
        arrivalComboBoxes.add(newArrivalComboBox); // Add to the list of arrival comboboxes
        multiCityComponents.add(newArrivalComboBox);

        JLabel newDatesLabel = new JLabel("Departure Date:");
        newDatesLabel.setBounds(10, baseY + (numSegments * 2 + 1) * incrementY, 120, 25);
        frame.add(newDatesLabel);
        multiCityComponents.add(newDatesLabel);

        JDateChooser newDepartureDateChooser = new JDateChooser(); // Create a new JDateChooser for each flight segment
        newDepartureDateChooser.setBounds(140, baseY + (numSegments * 2 + 1) * incrementY, 150, 25);
        frame.add(newDepartureDateChooser);
        multiCityComponents.add(newDepartureDateChooser); // Add to the list of multi-city components

        // Repopulate the new combo boxes with locations
        populateLocations();

        // Adjust the positions of the passengers label, combo box, and search button
        int newBaseY = baseY + (numSegments + 1) * incrementY * 2; // Adjust the y-coordinate accordingly

        // Adjust the position of the passengers label and combo box
        JLabel passengersLabel = new JLabel("Passengers:");
        passengersLabel.setBounds(10, newBaseY, 120, 25);
        frame.add(passengersLabel);
        multiCityComponents.add(passengersLabel);

        passengersComboBox.setBounds(140, newBaseY, 50, 25);
        frame.add(passengersComboBox);

        // Adjust the position of the search button
        searchFlightsButton.setBounds(500, newBaseY, 150, 30);
        frame.add(searchFlightsButton);

        // Adjust the frame size to fit the new components
        frame.setSize(frame.getWidth(), newBaseY + 100); // Adjust the frame height accordingly
        frame.revalidate(); // Refresh the frame to reflect the changes
        frame.repaint(); // Repaint to update the UI
    }


    private void removeMultiCityComponents() {
        for (JComponent component : multiCityComponents) {
            frame.getContentPane().remove(component);
        }
        multiCityComponents.clear();
        departureComboBoxes.clear();
        arrivalComboBoxes.clear();

        // Add the initial departure and arrival combo boxes back
        departureComboBoxes.add(departureComboBox);
        arrivalComboBoxes.add(arrivalComboBox);

        // Reset the passengers label and combo box position
        passengersComboBox.setBounds(140, 230, 50, 25);
        searchFlightsButton.setBounds(500, 230, 150, 30);

        frame.setSize(720, 350);
        frame.revalidate();
        frame.repaint();
    }

    private void searchFlights() {
        int numPassengers = (int) passengersComboBox.getSelectedItem();

        // Loop through each flight segment in the multi-city scenario
        for (int i = 0; i < departureComboBoxes.size(); i++) {
            String departure = (String) departureComboBoxes.get(i).getSelectedItem();
            String arrival = (String) arrivalComboBoxes.get(i).getSelectedItem();
            String departureDateString = "";

            // Set the departure date only if it's available
            if (departureDateChooser.isVisible()) {
                Date departureDate = departureDateChooser.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                departureDateString = dateFormat.format(departureDate);
            }

            // Debug: Print out the departure date
            System.out.println("Departure Date for Segment " + (i + 1) + ": " + departureDateString);

            try (Connection conn = connection.getConnection()) {
                // Determine the selected flight type
                String selectedFlightType = internationalRadioButton.isSelected() ? "International" : "Domestic";

                // Prepare SQL statement to fetch flights based on user input and flight type
                String sql = "SELECT * FROM " + selectedFlightType.toLowerCase() + "_flights " +
                        "WHERE source = ? AND destination = ? AND available_seats >= ?";
                // Include the departure date condition if it's available
                if (!departureDateString.isEmpty()) {
                    sql += " AND departure_date = ?";
                }
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, departure);
                    statement.setString(2, arrival);
                    statement.setInt(3, numPassengers);
                    // Set the departure date parameter if it's available
                    if (!departureDateString.isEmpty()) {
                        statement.setString(4, departureDateString);
                    }

                    // Execute query
                    try (ResultSet resultSet = statement.executeQuery()) {
                        // Create a panel to display flight options
                        JPanel panel = new JPanel(new GridLayout(0, 1));

                        while (resultSet.next()) {
                            // Get flight details
                            String flightNumber = resultSet.getString("flight_number");
                            String departureTime = resultSet.getString("departure_time");
                            String arrivalTime = resultSet.getString("arrival_time");
                            int availableSeats = resultSet.getInt("available_seats");
                            double price = resultSet.getDouble("price");

                            // Create a button for each flight
                            JButton flightButton = new JButton(
                                    "Flight Number: " + flightNumber +
                                            ", Departure Time: " + departureTime +
                                            ", Arrival Time: " + arrivalTime +
                                            ", Available Seats: " + availableSeats +
                                            ", Price: $" + price
                            );

                            // Add action listener to the button
                            final String selectedFlightNumber = flightNumber; // Final variable to store flight number
                            final double selectedFlightPrice = price; // Final variable to store flight price
                            flightButton.addActionListener(actionEvent -> {
                                // Handle flight selection (e.g., proceed to payment)
                                JOptionPane.showMessageDialog(frame, "You have selected flight number: " + selectedFlightNumber +  "\n which price is " + (selectedFlightPrice*numPassengers) + "$");
                                processPayment(selectedFlightPrice * numPassengers);
                            });

                            // Add the button to the panel
                            panel.add(flightButton);
                        }

                        // Display search results in a scrollable dialog
                        JScrollPane scrollPane = new JScrollPane(panel);
                        JOptionPane.showMessageDialog(frame, scrollPane, "Available Flights for Segment " + (i + 1), JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to fetch flights from the database: " + ex.getMessage());
            }
        }
    }


    private void processPayment(double amount) {
        // Dummy implementation for payment processing
        JOptionPane.showMessageDialog(frame, "Payment of $" + amount + " processed successfully!");
    }



/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReservationGUI::new);
    }*/
}

