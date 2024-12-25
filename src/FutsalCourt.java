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