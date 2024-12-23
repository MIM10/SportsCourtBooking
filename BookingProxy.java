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