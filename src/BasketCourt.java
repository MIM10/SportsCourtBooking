class BasketCourt implements Court {
    private String courtName = "Basket";
    private double price = 70000;

    @Override
    public String getCourtName() {
        return courtName;
    }

    @Override
    public double getPrice() {
        return price;
    }
}