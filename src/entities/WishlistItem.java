package entities;

public class WishlistItem {

    private int id;
    private int flightId;
    private String airline;
    private String departure;
    private String destination;
    private double price;

    public WishlistItem(int id, int flightId, String airline,
                        String departure, String destination, double price) {
        this.id = id;
        this.flightId = flightId;
        this.airline = airline;
        this.departure = departure;
        this.destination = destination;
        this.price = price;
    }

    public int getId() { return id; }
    public int getFlightId() { return flightId; }

    @Override
    public String toString() {
        return "✈ " + airline + " | " + departure + " → " + destination + " | $" + price;
    }
}