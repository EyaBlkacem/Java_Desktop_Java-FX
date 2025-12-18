package entities;

public class Flight {

    private int id;
    private String airline;
    private String flightNo;
    private String departure;
    private String destination;
    private String departTime;
    private String arriveTime;
    private String duration;
    private double price;

    public Flight(int id, String airline, String flightNo, String departure,
                  String destination, String departTime, String arriveTime,
                  String duration, double price) {
        this.id = id;
        this.airline = airline;
        this.flightNo = flightNo;
        this.departure = departure;
        this.destination = destination;
        this.departTime = departTime;
        this.arriveTime = arriveTime;
        this.duration = duration;
        this.price = price;
    }

   
    public int getId() { return id; }
    public String getAirline() { return airline; }
    public String getFlightNo() { return flightNo; }
    public String getDeparture() { return departure; }
    public String getDestination() { return destination; }
    public String getDepartTime() { return departTime; }
    public String getArriveTime() { return arriveTime; }
    public String getDuration() { return duration; }
    public double getPrice() { return price; }
}
