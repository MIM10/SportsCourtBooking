import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            // Connect to the existing database named "sportsdb"
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sportsdb", "root", "");
            System.out.println("Koneksi ke databse berhasil.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal terhubung ke database.");
        }
    }

    public static DatabaseConnection getInstance() {
        // Lazy Initialization
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
