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