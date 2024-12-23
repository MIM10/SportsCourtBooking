class CourtFactory {
    public static Court getCourt(String type) {
        switch (type) {
            case "Futsal":
                return new FutsalCourt();
            case "Basket":
                return new BasketCourt();
            default:
                throw new IllegalArgumentException("Unknown court type.");
        }
    }
}