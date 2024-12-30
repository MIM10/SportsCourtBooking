class FutsalCourt implements Court {
    private String courtName = "Futsal";
    private double price = 50000;

    @Override
    public String getCourtName() {
        return courtName;
    }

    @Override
    public double getPrice() {
        return price;
    }
}