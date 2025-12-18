package entities;



import java.time.LocalDateTime;



public class Reservation {



    private int id;

    private String airline;

    private String departure;

    private String destination;

    private String seatCode;

    private double price;

    private LocalDateTime reservedAt;



    public Reservation(int id, String airline, String departure,

                       String destination, String seatCode,

                       double price, LocalDateTime reservedAt) {



        this.id = id;

        this.airline = airline;

        this.departure = departure;

        this.destination = destination;

        this.seatCode = seatCode;

        this.price = price;

        this.reservedAt = reservedAt;

    }



    public int getId() { return id; }

    public String getAirline() { return airline; }

    public String getDeparture() { return departure; }

    public String getDestination() { return destination; }

    public String getSeatCode() { return seatCode; }

    public double getPrice() { return price; }

    public LocalDateTime getReservedAt() { return reservedAt; }



    @Override

    public String toString() {

        return airline + " | " +

               departure + " → " + destination +

               " | Seat " + seatCode +

               " | $" + price;

    }

}

