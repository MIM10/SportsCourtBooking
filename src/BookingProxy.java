class BookingProxy implements BookingService {
    private final BookingServiceImpl bookingService;
    private final String role;

    public BookingProxy(String role) {
        this.bookingService = new BookingServiceImpl();
        this.role = role;
    }

    @Override
    public void addBooking(Booking booking) {
        if ("User".equalsIgnoreCase(role)) {
            bookingService.addBooking(booking);
        } else {
            System.out.println("Access Denied: User hanya bisa booking.");
        }
    }

    @Override
    public void editBooking(int index, Booking booking) {
        if ("Admin".equalsIgnoreCase(role)) {
            bookingService.editBooking(index, booking);
        } else {
            System.out.println("Access Denied: Hanya admin yang bisa mengedit booking.");
        }
    }

    @Override
    public void deleteBooking(int index) {
        if ("Admin".equalsIgnoreCase(role)) {
            bookingService.deleteBooking(index);
        } else {
            System.out.println("Access Denied: Hanya admin yang bisa menghapus booking.");
        }
    }

    @Override
    public void listBookings() {
        bookingService.listBookings();
    }
}
