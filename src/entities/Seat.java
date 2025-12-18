package entities;



public class Seat {

    private int id, flightId;

    private String seatCode, status;



    public Seat(int id, int flightId, String seatCode, String status) {

        this.id=id; this.flightId=flightId; this.seatCode=seatCode; this.status=status;

    }

    public int getId(){ return id; }

    public int getFlightId(){ return flightId; }

    public String getSeatCode(){ return seatCode; }

    public String getStatus(){ return status; }

    public void setStatus(String s){ this.status=s; }

}

