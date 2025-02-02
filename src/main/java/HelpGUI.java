import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HelpGUI {
    private JFrame frame;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel submitTicketLabel;
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel emailLabel;
    private JTextField emailTextField;
    private JLabel problemTypeLabel;
    private JComboBox<String> problemTypeComboBox;
    private JLabel descriptionLabel;
    private JTextArea descriptionTextArea;
    private JButton submitButton;
    private JLabel contactUsLabel;
    private JLabel emailContactLabel;
    private JLabel phoneContactLabel;
    private JLabel gpsLabel;

    public HelpGUI() {
        frame = new JFrame("Help");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 450);
        frame.setLayout(new BorderLayout());

        initializeNavigationBar();

        // Create the main panels
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        leftPanel = new JPanel();
        leftPanel.setLayout(null);
        leftPanel.setPreferredSize(new Dimension(320, 400));

        submitTicketLabel = new JLabel("Submit a Ticket");
        submitTicketLabel.setFont(new Font("Arial", Font.BOLD, 16));
        submitTicketLabel.setBounds(20, 10, 150, 25);
        leftPanel.add(submitTicketLabel);

        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 40, 80, 25);
        leftPanel.add(nameLabel);
        nameTextField = new JTextField();
        nameTextField.setBounds(110, 40, 180, 25);
        leftPanel.add(nameTextField);

        emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 80, 80, 25);
        leftPanel.add(emailLabel);
        emailTextField = new JTextField();
        emailTextField.setBounds(110, 80, 180, 25);
        leftPanel.add(emailTextField);

        problemTypeLabel = new JLabel("Type of Problem:");
        problemTypeLabel.setBounds(20, 120, 120, 25);
        leftPanel.add(problemTypeLabel);
        String[] problemTypes = {"General Inquiry", "Technical Issue", "Billing Problem", "Other"};
        problemTypeComboBox = new JComboBox<>(problemTypes);
        problemTypeComboBox.setBounds(150, 120, 140, 25);
        leftPanel.add(problemTypeComboBox);

        descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(20, 160, 80, 25);
        leftPanel.add(descriptionLabel);
        descriptionTextArea = new JTextArea();
        descriptionTextArea.setBounds(20, 190, 290, 120);
        descriptionTextArea.setLineWrap(true);
        leftPanel.add(descriptionTextArea);

        submitButton = new JButton("Submit");
        submitButton.setBounds(90, 320, 140, 25);
        submitButton.setBackground(Color.RED);
        submitButton.setForeground(Color.WHITE);
        leftPanel.add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitTicket();
            }
        });

        mainPanel.add(leftPanel);

        rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setPreferredSize(new Dimension(350, 400));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); // Add padding to the right

        contactUsLabel = new JLabel("Contact Us");
        contactUsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contactUsLabel.setBounds(80, 30, 150, 25);
        rightPanel.add(contactUsLabel);

        emailContactLabel = new JLabel("Email: support@example.com");
        emailContactLabel.setBounds(80, 70, 200, 25);
        rightPanel.add(emailContactLabel);

        phoneContactLabel = new JLabel("Phone: +1234567890");
        phoneContactLabel.setBounds(80, 100, 200, 25);
        rightPanel.add(phoneContactLabel);

        gpsLabel = new JLabel("Location: Atatürk Havalimanı");
        gpsLabel.setBounds(80, 130, 200, 25);
        rightPanel.add(gpsLabel);

        JButton viewMapButton = new JButton("View on Map");
        viewMapButton.setBounds(80, 160, 150, 25);
        viewMapButton.setBackground(Color.RED);
        viewMapButton.setForeground(Color.WHITE);
        viewMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGoogleMaps();
            }
        });
        rightPanel.add(viewMapButton);

        mainPanel.add(rightPanel);

        frame.add(mainPanel, BorderLayout.CENTER);

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
                frame.dispose(); // Close the Help page
                new ReservationGUI();
            }
        });

        helpMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                frame.dispose(); // Close the Help page
                new HelpGUI();
            }
        });

        signUpMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                frame.dispose(); // Close the Help page
                new RegisterGUI();
            }
        });

        signInMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                frame.dispose(); // Close the Help page
                new LoginGUI();
            }
        });

        menuBar.add(bookFlightMenu);
        menuBar.add(helpMenu);
        menuBar.add(signUpMenu);
        menuBar.add(signInMenu);

        frame.setJMenuBar(menuBar);
    }

    private void submitTicket() {
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        String problemType = (String) problemTypeComboBox.getSelectedItem();
        String description = descriptionTextArea.getText();

        // Validate input
        if (name.isEmpty() || email.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
            return;
        }

        // Insert ticket into the database
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO tickets (Name, Email, ProblemType, Description) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, problemType);
            statement.setString(4, description);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(frame, "Ticket submitted successfully!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to submit ticket.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "An error occurred while submitting ticket: " + ex.getMessage());
        }
    }

    private void clearForm() {
        nameTextField.setText("");
        emailTextField.setText("");
        problemTypeComboBox.setSelectedIndex(0);
        descriptionTextArea.setText("");
    }

    private void openGoogleMaps() {
        // Open Google Maps in the default web browser with the company's location
        try {
            String url = "https://www.google.com/maps/search/Turkish+Airlines/@41.0564545,28.9711275,15z/data=!3m1!4b1?entry=ttu"; // Replace with your company's location
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(HelpGUI::new);
    }*/
}
