import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class LoginGUI {
    private JFrame frame;
    private JLabel email;
    private JLabel pass;
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JLabel forgotPasswordLabel;
    private JButton loginButton;
    private JLabel newCustomerLabel;

    public LoginGUI() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(480, 250);
        frame.setLayout(null);

        email = new JLabel("Email:");
        email.setBounds(20, 30, 80, 25);
        frame.add(email);

        emailTextField = new JTextField();
        emailTextField.setBounds(110, 30, 200, 25);
        frame.add(emailTextField);

        pass = new JLabel("Password:");
        pass.setBounds(20, 70, 80, 25);
        frame.add(pass);

        passwordField = new JPasswordField();
        passwordField.setBounds(110, 70, 200, 25);
        frame.add(passwordField);

        forgotPasswordLabel = new JLabel("I forgot my password");
        forgotPasswordLabel.setForeground(Color.RED);
        forgotPasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.setBounds(320, 70, 150, 25);
        frame.add(forgotPasswordLabel);
        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JOptionPane.showMessageDialog(frame, "Forgot password functionality not implemented.");
            }
        });

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 110, 100, 25);
        loginButton.setBackground(Color.RED);
        loginButton.setForeground(Color.WHITE);
        frame.add(loginButton);

        loginButton.addActionListener(e -> {
            String email = emailTextField.getText();
            String password = new String(passwordField.getPassword());

            // Check for missing fields
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both email and password.");
                return;
            }

            if (authenticateUser(email, password)) {
                JOptionPane.showMessageDialog(frame, "Login successful!");
                frame.dispose();
                new ReservationGUI();
                // Add code to open your main application window here
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid email or password. Please try again.");
            }
        });

        newCustomerLabel = new JLabel("New Customer?");
        newCustomerLabel.setForeground(Color.RED);
        newCustomerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        newCustomerLabel.setBounds(270, 110, 100, 25);
        frame.add(newCustomerLabel);
        newCustomerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                frame.dispose();
                new RegisterGUI();
            }
        });

        initializeNavigationBar(); // Add navigation bar

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
                JOptionPane.showMessageDialog(frame, "You have to Log in to be able to book");
                /*
                frame.dispose(); // Close the login page
                new ReservationGUI();

                 */
            }
        });

        helpMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                frame.dispose(); // Close the login page
                new HelpGUI();;
            }
        });

        signUpMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                frame.dispose(); // Close the login page
                new RegisterGUI();
            }
        });

        signInMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                frame.dispose(); // Close the login page
                new LoginGUI();
            }
        });

        menuBar.add(bookFlightMenu);
        menuBar.add(helpMenu);
        menuBar.add(signUpMenu);
        menuBar.add(signInMenu);

        frame.setJMenuBar(menuBar);

    }

    private boolean authenticateUser(String email, String password) {
        String query = "SELECT PasswordHash FROM users WHERE Email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("PasswordHash");
                // Verify the entered password against the hashed password
                return BCrypt.checkpw(password, hashedPassword);
            } else {
                return false; // User not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "An error occurred while authenticating: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
