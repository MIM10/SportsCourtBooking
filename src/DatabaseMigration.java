import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseMigration {
    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getInstance().getConnection();

        try (Statement statement = connection.createStatement()) {
            // SQL script to create the 'bookings' table

            String createCourtsTable = """
                CREATE TABLE IF NOT EXISTS courts (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    type VARCHAR(50) NOT NULL,
                    price_per_hour DECIMAL(10, 2) NOT NULL
                );
            """;

            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    role ENUM('Admin', 'User') NOT NULL
                );
            """;

            String createBookingsTable = """
                CREATE TABLE IF NOT EXISTS bookings (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    court_type VARCHAR(50),
                    time_slot VARCHAR(50),
                    payment_method VARCHAR(50),
                    status ENUM('Pending', 'Confirmed', 'Cancelled') DEFAULT 'Pending',
                    price DECIMAL(10, 2),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                );
            """;

            statement.execute(createCourtsTable);
            statement.execute(createUsersTable);
            statement.execute(createBookingsTable);

            // System.out.println("Table 'bookings' created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}