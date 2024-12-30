public class BadmintonCourt implements Court{
    private String courtName = "Badminton";
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