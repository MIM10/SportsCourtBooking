// File: DatabaseConnection.java
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;

// class DatabaseConnection {
//     private static DatabaseConnection instance;
//     private Connection connection;

//     private DatabaseConnection() {
//         try {
//             // Connect to the existing database named "sportsdb"
//             connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/sportsdb", "root", "");
//             System.out.println("Connected to the database successfully.");
//         } catch (SQLException e) {
//             e.printStackTrace();
//             throw new RuntimeException("Failed to connect to the database.");
//         }
//     }

//     public static DatabaseConnection getInstance() {
//         if (instance == null) {
//             instance = new DatabaseConnection();
//         }
//         return instance;
//     }

//     public Connection getConnection() {
//         return connection;
//     }
// }

// // File: DatabaseMigration.java
// import java.sql.Connection;
// import java.sql.SQLException;
// import java.sql.Statement;

// public class DatabaseMigration {
//     public static void main(String[] args) {
//         Connection connection = DatabaseConnection.getInstance().getConnection();

//         try (Statement statement = connection.createStatement()) {
//             // SQL script to create the 'bookings' table

//             String createCourtsTable = """
//                 CREATE TABLE IF NOT EXISTS courts (
//                     id INT AUTO_INCREMENT PRIMARY KEY,
//                     name VARCHAR(50) NOT NULL,
//                     type VARCHAR(50) NOT NULL,
//                     price_per_hour DECIMAL(10, 2) NOT NULL
//                 );
//             """;

//             String createUsersTable = """
//                 CREATE TABLE IF NOT EXISTS users (
//                     id INT AUTO_INCREMENT PRIMARY KEY,
//                     username VARCHAR(50) UNIQUE NOT NULL,
//                     password VARCHAR(255) NOT NULL,
//                     role ENUM('Admin', 'User') NOT NULL
//                 );
//             """;

//             String createBookingsTable = """
//                 CREATE TABLE IF NOT EXISTS bookings (
//                     id INT AUTO_INCREMENT PRIMARY KEY,
//                     court_type VARCHAR(50),
//                     time_slot VARCHAR(50),
//                     payment_method VARCHAR(50),
//                     status ENUM('Pending', 'Confirmed', 'Cancelled') DEFAULT 'Pending',
//                     price DECIMAL(10, 2),
//                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
//                 );
//             """;

//             statement.execute(createCourtsTable);
//             statement.execute(createUsersTable);
//             statement.execute(createBookingsTable);

//             // System.out.println("Table 'bookings' created or already exists.");
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
// }

// // File: Court.java
// abstract class Court {
//     protected String courtName;
//     protected double pricePerHour;

//     public abstract void displayDetails();
// }

// // File: FutsalCourt.java
// class FutsalCourt extends Court {
//     public FutsalCourt() {
//         courtName = "Futsal Court";
//         pricePerHour = 100.0;
//     }

//     @Override
//     public void displayDetails() {
//         System.out.println(courtName + " - $" + pricePerHour + " per hour");
//     }
// }

// // File: BasketCourt.java
// class BasketCourt extends Court {
//     public BasketCourt() {
//         courtName = "Basket Court";
//         pricePerHour = 120.0;
//     }

//     @Override
//     public void displayDetails() {
//         System.out.println(courtName + " - $" + pricePerHour + " per hour");
//     }
// }

// // File: CourtFactory.java
// class CourtFactory {
//     public static Court getCourt(String type) {
//         switch (type) {
//             case "Futsal":
//                 return new FutsalCourt();
//             case "Basket":
//                 return new BasketCourt();
//             default:
//                 throw new IllegalArgumentException("Unknown court type.");
//         }
//     }
// }

// // File: BookingService.java
// interface BookingService {
//     void manageSchedule();
//     void viewReport();
// }

// // File: BookingServiceImpl.java
// class BookingServiceImpl implements BookingService {
//     @Override
//     public void manageSchedule() {
//         System.out.println("Managing schedule...");
//     }

//     @Override
//     public void viewReport() {
//         System.out.println("Viewing report...");
//     }
// }

// // File: BookingProxy.java
// class BookingProxy implements BookingService {
//     private final BookingServiceImpl bookingService;
//     private final String role;

//     public BookingProxy(String role) {
//         this.bookingService = new BookingServiceImpl();
//         this.role = role;
//     }

//     @Override
//     public void manageSchedule() {
//         if ("Admin".equalsIgnoreCase(role)) {
//             bookingService.manageSchedule();
//         } else {
//             System.out.println("Access Denied: Only Admins can manage schedules.");
//         }
//     }

//     @Override
//     public void viewReport() {
//         if ("Admin".equalsIgnoreCase(role)) {
//             bookingService.viewReport();
//         } else {
//             System.out.println("Access Denied: Only Admins can view reports.");
//         }
//     }
// }

// // File: Booking.java
// class Booking {
//     private final String courtType;
//     private final String timeSlot;
//     private final String paymentMethod;

//     private Booking(BookingBuilder builder) {
//         this.courtType = builder.courtType;
//         this.timeSlot = builder.timeSlot;
//         this.paymentMethod = builder.paymentMethod;
//     }

//     public static class BookingBuilder {
//         private String courtType;
//         private String timeSlot;
//         private String paymentMethod;

//         public BookingBuilder setCourtType(String courtType) {
//             this.courtType = courtType;
//             return this;
//         }

//         public BookingBuilder setTimeSlot(String timeSlot) {
//             this.timeSlot = timeSlot;
//             return this;
//         }

//         public BookingBuilder setPaymentMethod(String paymentMethod) {
//             this.paymentMethod = paymentMethod;
//             return this;
//         }

//         public Booking build() {
//             return new Booking(this);
//         }
//     }

//     @Override
//     public String toString() {
//         return "Booking [CourtType=" + courtType + ", TimeSlot=" + timeSlot + ", PaymentMethod=" + paymentMethod + "]";
//     }
// }

// // File: SportsCourtBooking.java
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.SQLException;
// import java.time.LocalTime;
// import java.time.temporal.ChronoUnit;
// import java.util.Scanner;

// public class SportsCourtBooking {
//     public static void main(String[] args) {
//         DatabaseConnection db = DatabaseConnection.getInstance();

//         // Run Database Migration
//         DatabaseMigration.main(args);

//         try (Scanner scanner = new Scanner(System.in)) {
//             System.out.println("\nWelcome to Sports Court Booking System");

//             // Input Court Type
//             System.out.println("Choose a court type (Futsal/Basket): ");
//             System.out.print("- ");
//             String courtType = scanner.nextLine();
//             Court court = CourtFactory.getCourt(courtType);
//             System.out.println("\nSelected Court: " + court.courtName + " - $" + court.pricePerHour + " per hour");

//             // Input Time Slot
//             System.out.println("Enter the time slot in 24-hour format (e.g., 10:00-12:30): ");
//             System.out.print("- ");
//             String timeSlot = scanner.nextLine();

//             // Parsing Time Slot to Calculate Duration
//             double durationInHours = calculateDurationInHours(timeSlot);

//             // Hitung Total Harga berdasarkan Durasi
//             double totalPrice = court.pricePerHour * durationInHours;

//             // Input Payment Method
//             System.out.println("\nChoose payment method (Online/Cash): ");
//             System.out.print("- ");
//             String paymentMethod = scanner.nextLine();

//             // Save booking to database
//             saveBookingToDatabase(court, timeSlot, paymentMethod, totalPrice, db.getConnection());

//             System.out.printf("Booking successful! Total Price: $%.2f (Duration: %.2f hours)%n", totalPrice, durationInHours);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     private static double calculateDurationInHours(String timeSlot) {
//         try {
//             String[] times = timeSlot.split("-");
//             LocalTime startTime = LocalTime.parse(times[0].trim()); // Format 24 jam
//             LocalTime endTime = LocalTime.parse(times[1].trim());

//             long durationInMinutes = ChronoUnit.MINUTES.between(startTime, endTime);
//             return durationInMinutes / 60.0; // Konversi menit ke jam
//         } catch (Exception e) {
//             throw new IllegalArgumentException("Invalid time slot format. Use format like '10:00-12:30'.");
//         }
//     }

//     private static void saveBookingToDatabase(Court court, String timeSlot, String paymentMethod, double totalPrice, Connection connection) {
//         String insertBooking = "INSERT INTO bookings (court_type, time_slot, payment_method, price) VALUES (?, ?, ?, ?);";

//         try (PreparedStatement preparedStatement = connection.prepareStatement(insertBooking)) {
//             preparedStatement.setString(1, court.courtName);  // Menyertakan nama court
//             preparedStatement.setString(2, timeSlot);
//             preparedStatement.setString(3, paymentMethod);
//             preparedStatement.setDouble(4, totalPrice); // Menyertakan harga total

//             int rowsInserted = preparedStatement.executeUpdate();
//             if (rowsInserted > 0) {
//                 System.out.println("\nBooking saved to database.");
//             } else {
//                 System.out.println("Failed to save booking to database.");
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
// }