import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class BookingServiceImpl implements BookingService {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public void addBooking(Booking booking) {
        try {
            String query = "INSERT INTO bookings (court_id, time_slot) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            Court court = CourtFactory.getCourt(booking.getCourtType());
            // Mendapatkan ID court dari database berdasarkan nama court
            int courtId = getCourtIdFromDatabase(court.getCourtName());
            stmt.setInt(1, courtId);
            stmt.setString(2, booking.getTimeSlot());
        
            stmt.executeUpdate();
            System.out.println("Booking added: " + booking);
        } catch (SQLException e) {
            System.err.println("Failed to add booking: " + e.getMessage());
        }
    }

    private int getCourtIdFromDatabase(String courtName) throws SQLException {
        String query = "SELECT id FROM courts WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, courtName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("Court not found: " + courtName);
            }
        }
    }

    @Override
    public void editBooking(int index, Booking booking) {
        try {
            String query = "UPDATE bookings SET court_id = ?, time_slot = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            Court court = CourtFactory.getCourt(booking.getCourtType());
            int courtId = getCourtIdFromDatabase(court.getCourtName());
            stmt.setInt(1, courtId);
            stmt.setString(2, booking.getTimeSlot());
            stmt.setInt(3, index);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Booking updated: " + booking);
            } else {
                System.out.println("Booking not found.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to update booking: " + e.getMessage());
        }
    }

    @Override
    public void deleteBooking(int index) {
        try {
            String query = "DELETE FROM bookings WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, index);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Booking deleted.");
            } else {
                System.out.println("Booking not found.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to delete booking: " + e.getMessage());
        }
    }


    @Override
    public void listBookings() {
        try {
            String query = "SELECT b.id, c.name AS court_name, b.time_slot FROM bookings b JOIN courts c ON b.court_id = c.id";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Court: " + rs.getString("court_name") +
                        ", Time Slot: " + rs.getString("time_slot"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve bookings: " + e.getMessage());
        }
    }
}
