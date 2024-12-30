public class CourtFactory {
    public static Court getCourt(String type) {
        switch (type) {
            case "Futsal":
                return new FutsalCourt();
            case "Basket":
                return new BasketCourt();
            case "Badminton":
                return new BadmintonCourt();
            default:
                throw new IllegalArgumentException("Lapangan " + type + " tidak tersedia.");
        }
    }
}