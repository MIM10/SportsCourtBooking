import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class SportsCourtBooking {
    public static void main(String[] args) {
        DatabaseConnection db = DatabaseConnection.getInstance();

        // Run Database Migration
        DatabaseMigration.main(args);

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("\nWelcome to Sports Court Booking System");

            // Input Court Type
            System.out.println("Choose a court type (Futsal/Basket): ");
            System.out.print("- ");
            String courtType = scanner.nextLine();
            Court court = CourtFactory.getCourt(courtType);
            System.out.println("\nSelected Court: " + court.courtName + " - $" + court.pricePerHour + " per hour");

            // Input Time Slot
            System.out.println("Enter the time slot in 24-hour format (e.g., 10:00-12:30): ");
            System.out.print("- ");
            String timeSlot = scanner.nextLine();

            // Parsing Time Slot to Calculate Duration
            double durationInHours = calculateDurationInHours(timeSlot);

            // Hitung Total Harga berdasarkan Durasi
            double totalPrice = court.pricePerHour * durationInHours;

            // Input Payment Method
            System.out.println("\nChoose payment method (Online/Cash): ");
            System.out.print("- ");
            String paymentMethod = scanner.nextLine();

            // Save booking to database
            saveBookingToDatabase(court, timeSlot, paymentMethod, totalPrice, db.getConnection());

            System.out.printf("Booking successful! Total Price: $%.2f (Duration: %.2f hours)%n", totalPrice, durationInHours);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double calculateDurationInHours(String timeSlot) {
        try {
            String[] times = timeSlot.split("-");
            LocalTime startTime = LocalTime.parse(times[0].trim()); // Format 24 jam
            LocalTime endTime = LocalTime.parse(times[1].trim());

            long durationInMinutes = ChronoUnit.MINUTES.between(startTime, endTime);
            return durationInMinutes / 60.0; // Konversi menit ke jam
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time slot format. Use format like '10:00-12:30'.");
        }
    }

    private static void saveBookingToDatabase(Court court, String timeSlot, String paymentMethod, double totalPrice, Connection connection) {
        String insertBooking = "INSERT INTO bookings (court_type, time_slot, payment_method, price) VALUES (?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertBooking)) {
            preparedStatement.setString(1, court.courtName);  // Menyertakan nama court
            preparedStatement.setString(2, timeSlot);
            preparedStatement.setString(3, paymentMethod);
            preparedStatement.setDouble(4, totalPrice); // Menyertakan harga total

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("\nBooking saved to database.");
            } else {
                System.out.println("Failed to save booking to database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}