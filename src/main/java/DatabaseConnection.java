import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String PROPERTIES_FILE = "config.properties";
    private static String JDBC_URL;
    private static String USERNAME;
    private static String PASSWORD;

    static {
        // Load properties once when the class is initialized
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Config file not found: " + PROPERTIES_FILE);
            }
            Properties props = new Properties();
            props.load(input);

            JDBC_URL = props.getProperty("db.url");
            USERNAME = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");

        } catch (IOException ex) {
            throw new RuntimeException("Failed to load database configuration", ex);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}