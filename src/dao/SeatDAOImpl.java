package dao;



import entities.Seat;

import java.sql.*;

import java.util.ArrayList;

import java.util.List;



public class SeatDAOImpl implements SeatDAO {



    @Override

    public List<Seat> findByFlight(int flightId){



        List<Seat> seats = new ArrayList<>();



        try {

            Connection cnx = SingletonConnection.getConnection();

            PreparedStatement ps = cnx.prepareStatement(

                "SELECT * FROM seat WHERE flight_id=?"

            );

            ps.setInt(1, flightId);

            ResultSet rs = ps.executeQuery();



            while(rs.next()){

                seats.add(new Seat(

                    rs.getInt("id"),

                    rs.getInt("flight_id"),

                    rs.getString("seat_code"),

                    rs.getString("status")

                ));

            }



        } catch (Exception e){

            e.printStackTrace();

        }



        return seats;

    }



    @Override

    public void insertSeat(int flightId, String seatCode){



        try {

            Connection cnx = SingletonConnection.getConnection();

            PreparedStatement ps = cnx.prepareStatement(

                "INSERT INTO seat(flight_id, seat_code, status) VALUES (?, ?, 'AVAILABLE')"

            );

            ps.setInt(1, flightId);

            ps.setString(2, seatCode);

            ps.executeUpdate();



        } catch (Exception e){

            e.printStackTrace();

        }

    }



    @Override

    public void reserveSeat(int flightId, String seatCode){



        try {

            Connection cnx = SingletonConnection.getConnection();

            PreparedStatement ps = cnx.prepareStatement(

                "UPDATE seat SET status='RESERVED' WHERE flight_id=? AND seat_code=?"

            );

            ps.setInt(1, flightId);

            ps.setString(2, seatCode);

            ps.executeUpdate();



        } catch (Exception e){

            throw new RuntimeException("Seat already reserved ❌");

        }

    }

}