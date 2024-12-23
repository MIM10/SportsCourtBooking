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