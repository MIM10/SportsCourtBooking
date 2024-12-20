// File: DatabaseConnection.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            // Connect to the existing database named "sportsdb"
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sportsdb", "root", "password");
            System.out.println("Connected to the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

// File: Court.java
abstract class Court {
    protected String courtName;
    protected double pricePerHour;

    public abstract void displayDetails();
}

// File: FutsalCourt.java
class FutsalCourt extends Court {
    public FutsalCourt() {
        courtName = "Futsal Court";
        pricePerHour = 100.0;
    }

    @Override
    public void displayDetails() {
        System.out.println(courtName + " - $" + pricePerHour + " per hour");
    }
}

// File: BasketCourt.java
class BasketCourt extends Court {
    public BasketCourt() {
        courtName = "Basket Court";
        pricePerHour = 120.0;
    }

    @Override
    public void displayDetails() {
        System.out.println(courtName + " - $" + pricePerHour + " per hour");
    }
}

// File: CourtFactory.java
class CourtFactory {
    public static Court getCourt(String type) {
        switch (type) {
            case "Futsal":
                return new FutsalCourt();
            case "Basket":
                return new BasketCourt();
            default:
                throw new IllegalArgumentException("Unknown court type.");
        }
    }
}

// File: BookingService.java
interface BookingService {
    void manageSchedule();
    void viewReport();
}

// File: BookingServiceImpl.java
class BookingServiceImpl implements BookingService {
    @Override
    public void manageSchedule() {
        System.out.println("Managing schedule...");
    }

    @Override
    public void viewReport() {
        System.out.println("Viewing report...");
    }
}

// File: BookingProxy.java
class BookingProxy implements BookingService {
    private final BookingServiceImpl bookingService;
    private final String role;

    public BookingProxy(String role) {
        this.bookingService = new BookingServiceImpl();
        this.role = role;
    }

    @Override
    public void manageSchedule() {
        if ("Admin".equalsIgnoreCase(role)) {
            bookingService.manageSchedule();
        } else {
            System.out.println("Access Denied: Only Admins can manage schedules.");
        }
    }

    @Override
    public void viewReport() {
        if ("Admin".equalsIgnoreCase(role)) {
            bookingService.viewReport();
        } else {
            System.out.println("Access Denied: Only Admins can view reports.");
        }
    }
}

// File: Booking.java
class Booking {
    private final String courtType;
    private final String timeSlot;
    private final String paymentMethod;

    private Booking(BookingBuilder builder) {
        this.courtType = builder.courtType;
        this.timeSlot = builder.timeSlot;
        this.paymentMethod = builder.paymentMethod;
    }

    public static class BookingBuilder {
        private String courtType;
        private String timeSlot;
        private String paymentMethod;

        public BookingBuilder setCourtType(String courtType) {
            this.courtType = courtType;
            return this;
        }

        public BookingBuilder setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
            return this;
        }

        public BookingBuilder setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }

    @Override
    public String toString() {
        return "Booking [CourtType=" + courtType + ", TimeSlot=" + timeSlot + ", PaymentMethod=" + paymentMethod + "]";
    }
}

// File: SportsCourtBooking.java
public class SportsCourtBooking {
    public static void main(String[] args) {
        DatabaseConnection db = DatabaseConnection.getInstance();

        // Factory Pattern: Create a Court
        Court futsalCourt = CourtFactory.getCourt("Futsal");
        futsalCourt.displayDetails();

        // Proxy Pattern: Access control
        BookingService adminService = new BookingProxy("Admin");
        adminService.manageSchedule();
        adminService.viewReport();

        BookingService userService = new BookingProxy("User");
        userService.manageSchedule();
        userService.viewReport();

        // Builder Pattern: Create a booking
        Booking booking = new Booking.BookingBuilder()
                .setCourtType("Futsal")
                .setTimeSlot("10:00 AM - 12:00 PM")
                .setPaymentMethod("Online")
                .build();

        System.out.println(booking);

        System.out.println("Application is running.");
    }
}
