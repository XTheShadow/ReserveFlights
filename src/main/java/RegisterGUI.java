import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

public class RegisterGUI {
    private JFrame frame;
    private JLabel fNameLabel;
    private JTextField fNameTextField;
    private JLabel lNameLabel;
    private JTextField lNameTextField;
    private JLabel genderLabel;
    private JComboBox<String> genderComboBox; // Combo box for gender selection
    private JLabel emailLabel;
    private JTextField emailTextField;
    private JLabel phoneLabel;
    private JFormattedTextField phoneFormattedTextField; // Formatted text field for phone number
    private JLabel nationalityLabel;
    private JComboBox<String> nationalityComboBox; // Combo box for nationality selection
    private JLabel dobLabel; // Label for Date of Birth
    private JDateChooser dobDateChooser; // JCalendar for Date of Birth
    private JLabel passLabel;
    private JPasswordField passwordField;
    private JLabel rePassLabel;
    private JPasswordField rePasswordField;
    private JButton registerButton;
    private JLabel loginLabel;

    // Checkboxes for terms and conditions
    private JCheckBox termsCheckBox;
    private JCheckBox commercialCheckBox;
    private JCheckBox dataUsageCheckBox;

    public RegisterGUI() {
        frame = new JFrame("Register");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(470, 650); // Increased height to accommodate the checkboxes
        frame.setLayout(null); // Use absolute positioning for simplicity

        // Initialize the navigation bar
        initializeNavigationBar();

        // First name label and text field
        fNameLabel = new JLabel("First Name:");
        fNameLabel.setBounds(20, 40, 80, 25);
        frame.add(fNameLabel);
        fNameTextField = new JTextField();
        fNameTextField.setBounds(110, 40, 200, 25);
        frame.add(fNameTextField);

        // Last name label and text field
        lNameLabel = new JLabel("Last Name:");
        lNameLabel.setBounds(20, 80, 80, 25);
        frame.add(lNameLabel);
        lNameTextField = new JTextField();
        lNameTextField.setBounds(110, 80, 200, 25);
        frame.add(lNameTextField);

        // Gender label and combo box
        genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(20, 120, 80, 25);
        frame.add(genderLabel);
        String[] genders = {"Select", "Male", "Female"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setBounds(110, 120, 200, 25);
        frame.add(genderComboBox);

        // Nationality label and combo box
        nationalityLabel = new JLabel("Nationality:");
        nationalityLabel.setBounds(20, 160, 80, 25);
        frame.add(nationalityLabel);
        String[] nationalities = {"Select", "USA", "UK", "Canada", "Australia", "Türkiye", "Palestine", "Egypt", "Germany", "New Zealand", "Qatar"};
        nationalityComboBox = new JComboBox<>(nationalities);
        nationalityComboBox.setBounds(110, 160, 200, 25);
        frame.add(nationalityComboBox);

        // Date of Birth label and JCalendar
        dobLabel = new JLabel("Date of Birth:");
        dobLabel.setBounds(20, 200, 80, 25);
        frame.add(dobLabel);
        dobDateChooser = new JDateChooser();
        dobDateChooser.setBounds(110, 200, 200, 25);
        frame.add(dobDateChooser);

        // Email label and text field
        emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 240, 80, 25);
        frame.add(emailLabel);
        emailTextField = new JTextField();
        emailTextField.setBounds(110, 240, 200, 25);
        frame.add(emailTextField);

        // Phone number label and formatted text field
        phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(20, 280, 80, 25);
        frame.add(phoneLabel);
        try {
            MaskFormatter phoneMaskFormatter = new MaskFormatter("+###########");
            phoneFormattedTextField = new JFormattedTextField(phoneMaskFormatter);
            phoneFormattedTextField.setBounds(110, 280, 200, 25);
            frame.add(phoneFormattedTextField);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        // Password label and field
        passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 320, 80, 25);
        frame.add(passLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(110, 320, 200, 25);
        frame.add(passwordField);

        // RePassword label and field
        rePassLabel = new JLabel("Rewrite Pass:");
        rePassLabel.setBounds(20, 360, 100, 25);
        frame.add(rePassLabel);
        rePasswordField = new JPasswordField();
        rePasswordField.setBounds(110, 360, 200, 25);
        frame.add(rePasswordField);

        // Terms and conditions checkbox
        termsCheckBox = new JCheckBox("I accept the General Terms and Conditions of the Miles&Smiles Frequent Flyer Program.");
        termsCheckBox.setBounds(20, 400, 420, 25);
        frame.add(termsCheckBox);

        // Commercial messages checkbox
        commercialCheckBox = new JCheckBox("I hereby agree that Turkish Airlines may send commercial electronic messages.");
        commercialCheckBox.setBounds(20, 430, 420, 25);
        frame.add(commercialCheckBox);

        // Personal data usage checkbox
        dataUsageCheckBox = new JCheckBox("I accept my personal data to be used in marketing activities.");
        dataUsageCheckBox.setBounds(20, 460, 420, 25);
        frame.add(dataUsageCheckBox);

        // Register button
        registerButton = new JButton("Register");
        registerButton.setBounds(150, 500, 100, 25); // Adjusted position
        registerButton.setBackground(Color.RED);
        registerButton.setForeground(Color.WHITE);
        frame.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your registration logic here
                try {
                    String firstName = fNameTextField.getText().trim();
                    String lastName = lNameTextField.getText().trim();
                    String email = emailTextField.getText().trim();
                    String phoneNumber = phoneFormattedTextField.getText().trim();
                    if (!phoneNumber.matches("^\\+\\d{11,14}$")) { // Ensures "+" followed by 11-14 digits
                        JOptionPane.showMessageDialog(frame, "Invalid phone number format. Use +XXXXXXXXXXXX");
                        return;
                    }

                    String password = new String(passwordField.getPassword());
                    String rePassword = new String(rePasswordField.getPassword());
                    String gender = (String) genderComboBox.getSelectedItem(); // Get selected gender
                    String nationality = (String) nationalityComboBox.getSelectedItem(); // Get selected nationality

                    // After retrieving firstName/lastName/email:
                    if (firstName.length() > 50) {
                        JOptionPane.showMessageDialog(frame, "First name cannot exceed 50 characters.");
                        return;
                    }
                    if (lastName.length() > 50) {
                        JOptionPane.showMessageDialog(frame, "Last name cannot exceed 50 characters.");
                        return;
                    }
                    if (email.length() > 255) {
                        JOptionPane.showMessageDialog(frame, "Email cannot exceed 255 characters.");
                        return;
                    }

                    // Get Date of Birth from JCalendar and format it
                    if (dobDateChooser.getDate() == null) {
                        JOptionPane.showMessageDialog(frame, "Please select a valid date of birth.");
                        return;
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dob = dateFormat.format(dobDateChooser.getDate());

                    // Check if any field is empty
                    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() ||
                            password.isEmpty() || rePassword.isEmpty()){
                        JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                        return;
                    }

                    if ("Select".equals(gender)) {
                        JOptionPane.showMessageDialog(frame, "Please select a gender.");
                        return;
                    }

                    if ("Select".equals(nationality)) {
                        JOptionPane.showMessageDialog(frame, "Please select a nationality.");
                        return;
                    }

                    // Check if email format is valid
                    Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
                    Matcher emailMatcher = emailPattern.matcher(email);
                    if (!emailMatcher.matches()) {
                        JOptionPane.showMessageDialog(frame, "Invalid email format.");
                        return;
                    }

                    // Check if passwords match
                    if (!password.equals(rePassword)) {
                        JOptionPane.showMessageDialog(frame, "Passwords do not match.");
                        return;
                    }

                    // Check if terms are accepted
                    if (!termsCheckBox.isSelected()) {
                        JOptionPane.showMessageDialog(frame, "You must accept the terms and conditions.");
                        return;
                    }

                    // Insert user data into the database
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        String query = "INSERT INTO users (FirstName, LastName, Email, PhoneNumber, Gender, Nationality, DateOfBirth, PasswordHash) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement statement = conn.prepareStatement(query);
                        statement.setString(1, firstName);
                        statement.setString(2, lastName);
                        statement.setString(3, email);
                        statement.setString(4, phoneNumber);
                        statement.setString(5, gender);
                        statement.setString(6, nationality);
                        statement.setString(7, dob);
                        statement.setString(8, BCrypt.hashpw(password, BCrypt.gensalt()));
                        int rowsInserted = statement.executeUpdate();

                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(frame, "Registration successful!");
                            frame.dispose(); // Close registration window
                            new LoginGUI();  // Open login window
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Registration failed: " + ex.getMessage());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage());
                }
            }
        });

        // Login label (clickable)
        loginLabel = new JLabel("Already a member?");
        loginLabel.setForeground(Color.RED);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.setBounds(260, 490, 180, 25);
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                frame.dispose(); // Close the register page
                new LoginGUI(); // Open the login page
            }
        });
        frame.add(loginLabel);

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
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterGUI::new);
    }*/
}
