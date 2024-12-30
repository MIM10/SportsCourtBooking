import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseMigration {
    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getInstance().getConnection();

        try (Statement statement = connection.createStatement()) {
            String createCourtsTable = "CREATE TABLE IF NOT EXISTS courts ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(50) UNIQUE NOT NULL,"
                + "price_per_hour DECIMAL(10, 2) NOT NULL"
                + ");";

            String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "username VARCHAR(50) UNIQUE NOT NULL,"
                + "password VARCHAR(255) NOT NULL,"
                + "role ENUM('Admin', 'User') NOT NULL"
                + ");";

            String createBookingsTable = "CREATE TABLE IF NOT EXISTS bookings ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "court_id INT,"
                + "time_slot VARCHAR(50),"
                + "payment_method VARCHAR(50),"
                + "price DECIMAL(10, 2),"
                + " FOREIGN KEY (court_id) REFERENCES courts(id),"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                + ");";

            String insertCourt = "INSERT INTO courts (name, price_per_hour) VALUES"
                + "('Futsal', 50000)," 
                + "('Basket', 70000)," 
                + "('Badminton', 40000)"
                + "ON DUPLICATE KEY UPDATE name = name;";

            statement.execute(createCourtsTable);
            statement.execute(createUsersTable);
            statement.execute(createBookingsTable);
            statement.execute(insertCourt);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}