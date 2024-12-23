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