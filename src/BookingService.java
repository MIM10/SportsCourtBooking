interface BookingService {
    void addBooking(Booking booking);
    void editBooking(int index, Booking booking);
    void deleteBooking(int index);
    void listBookings();
}