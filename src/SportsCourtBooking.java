import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class SportsCourtBooking {
    public static void main(String[] args) {
        DatabaseConnection db = DatabaseConnection.getInstance();

        // Run Database Migration
        DatabaseMigration.main(args);

        System.out.println("\nWelcome to Sports Court Booking System");

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Masukan role anda (Admin/User):");
            String role = scanner.nextLine();
    
            BookingProxy bookingProxy = new BookingProxy(role);
            
            // // Simulate admin login
            // BookingService adminService = new BookingProxy("Admin");
            // adminService.manageSchedule(); // Access allowed
            // adminService.viewReport(); // Access allowed

            // // Simulate user login
            // BookingService userService = new BookingProxy("User");
            // userService.manageSchedule(); // Access denied
            // userService.viewReport(); // Access denied

            if ("Admin".equalsIgnoreCase(role)) {
                int choice;
                do {
                    System.out.println(
                        "========================\n" +
                        "1. List Bookings\n2. Edit Booking\n3. Delete Booking\n4. Exit");
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice) {
                        case 1:
                            bookingProxy.listBookings();
                            break;
                        case 2:
                            bookingProxy.listBookings();
                            System.out.println("Masukan id booking yang ingin edit:");
                            int idEdit = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Masukan nama lapangan yang baru:");
                            String newCourt = scanner.nextLine();
                            System.out.println("Masukan waktu booking yang baru:");
                            String newTime = scanner.nextLine();
                            Booking newBooking = new Booking.BookingBuilder()
                                    .setCourtType(newCourt)
                                    .setTimeSlot(newTime)
                                    .build();
                            bookingProxy.editBooking(idEdit, newBooking);

                            System.out.printf("Booking berhasil diedit pada Id: %.2f menjadi lapangan: %.2f, dan pada jam %.2f", idEdit, newCourt, newTime);
                            break;
                        case 3:
                            bookingProxy.listBookings();
                            System.out.println("Masukan id booking yang ingin dihapus:");
                            int idDelete = scanner.nextInt();
                            scanner.nextLine();
                            bookingProxy.deleteBooking(idDelete);

                            System.out.printf("Booking pada Id: %.2f berhasil dihapus", idDelete);
                            break;
                        case 4:
                            System.out.println("Keluar dari aplikasi. Goodbye!");
                            break;
                    }
                    
                } while (choice != 4);
            } else {
                int choice;
                do {
                    System.out.println(
                        "========================\n" +
                        "1. Booking Lapangan\n2. List Booking\n3. Exit");
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice) {
                        case 1:
                            // Pilih Lapanagn
                            System.out.println("Pilih Jenis Lapangan (Futsal/Basket/Badminton): ");
                            System.out.print("- ");
                            String courtType = scanner.nextLine();
                            Court court = CourtFactory.getCourt(courtType);
                            System.out.println("\nLapangan yang dipilih: " + court.getCourtName() + " - Rp." + court.getPrice() + "/jam");

                            // Pilih Jam
                            System.out.println("Masukan jam booking lapangan, format (e.g., 10:00-12:30): ");
                            System.out.print("- ");
                            String timeSlot = scanner.nextLine();

                            // hitung durasi
                            double durationInHours = calculateDurationInHours(timeSlot);
                            double totalPrice = court.getPrice() * durationInHours;

                            // Input Payment Method
                            System.out.println("\nPilih Metode Pembayaran (Online/Cash): ");
                            System.out.print("- ");
                            String paymentMethod = scanner.nextLine();

                            // simpan ke database
                            saveBookingToDatabase(court, timeSlot, paymentMethod, totalPrice, db.getConnection());

                            System.out.printf("Booking successful! Total Price: Rp.%.2f (Duration: %.2f hours)%n", totalPrice, durationInHours);
                            break;
                        case 2:
                            bookingProxy.listBookings();
                            break;
                        case 3:
                            System.out.println("Keluar dari Aplikasi. Goodbye!");
                            break;
                    }
                    
                } while (choice != 3);
            } scanner.close();
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

    private static int getCourtId(String courtName, Connection connection) throws SQLException {
        String query = "SELECT id FROM courts WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, courtName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new SQLException("Court not found: " + courtName);
            }
        }
    }

    private static void saveBookingToDatabase(Court court, String timeSlot, String paymentMethod, double totalPrice, Connection connection) {
        String insertBooking = "INSERT INTO bookings (court_id, time_slot, payment_method, price) VALUES (?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertBooking)) {
            int courtId = getCourtId(court.getCourtName(), connection);

            preparedStatement.setInt(1, courtId);  // Menyertakan nama court
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