package dao;



import entities.Seat;

import java.util.List;



public interface SeatDAO {



    List<Seat> findByFlight(int flightId);



    void insertSeat(int flightId, String seatCode);



    void reserveSeat(int flightId, String seatCode);

}