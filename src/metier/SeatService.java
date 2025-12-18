package metier;



import dao.SeatDAO;

import dao.SeatDAOImpl;

import entities.Seat;



import java.util.List;

import java.util.Map;

import java.util.stream.Collectors;



public class SeatService {



    private final SeatDAO dao = new SeatDAOImpl();



    public Map<String, Seat> seatMap(int flightId){

        return dao.findByFlight(flightId)

                .stream()

                .collect(Collectors.toMap(Seat::getSeatCode, s -> s));

    }



  

    public void ensureSeatsExist(int flightId){



        List<Seat> seats = dao.findByFlight(flightId);



        if(!seats.isEmpty()) return;



        for(char r = 'A'; r <= 'H'; r++){

            for(int c = 1; c <= 6; c++){

                dao.insertSeat(flightId, r + "" + c);

            }

        }

    }



    public void confirmReservation(int flightId, String seatCode){

        dao.reserveSeat(flightId, seatCode);

    }

}