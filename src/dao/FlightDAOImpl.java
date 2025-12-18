package dao;

import entities.Flight;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDAOImpl implements FlightDAO {

    @Override
    public List<Flight> findAll() {
        List<Flight> flights = new ArrayList<>();
        try {
            Connection cnx = SingletonConnection.getConnection();
            if (cnx == null || cnx.isClosed()) {

                System.out.println("🔄 Connexion fermée → reconnexion");

                cnx = SingletonConnection.getConnection();

            }
            PreparedStatement ps = cnx.prepareStatement("SELECT * FROM flight");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                flights.add(new Flight(
                    rs.getInt("id"),
                    rs.getString("airline"),
                    rs.getString("flight_no"),
                    rs.getString("departure"),
                    rs.getString("destination"),
                    rs.getString("depart_time"),
                    rs.getString("arrive_time"),
                    rs.getString("duration"),
                    rs.getDouble("price")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flights;
    }

    @Override
    public Optional<Flight> findById(int id) {
        return findAll().stream().filter(f -> f.getId() == id).findFirst();
    }

    @Override
    public void save(Flight flight) {
        try {
            Connection cnx = SingletonConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(
                "INSERT INTO flight (airline, flight_no, departure, destination, depart_time, arrive_time, duration, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, flight.getAirline());
            ps.setString(2, flight.getFlightNo());
            ps.setString(3, flight.getDeparture());
            ps.setString(4, flight.getDestination());
            ps.setString(5, flight.getDepartTime());
            ps.setString(6, flight.getArriveTime());
            ps.setString(7, flight.getDuration());
            ps.setDouble(8, flight.getPrice());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'ajout du vol : " + e.getMessage());
        }
    }

    @Override
    public void update(Flight flight) {
        try {
            Connection cnx = SingletonConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(
                "UPDATE flight SET airline=?, flight_no=?, departure=?, destination=?, depart_time=?, arrive_time=?, duration=?, price=? WHERE id=?"
            );
            ps.setString(1, flight.getAirline());
            ps.setString(2, flight.getFlightNo());
            ps.setString(3, flight.getDeparture());
            ps.setString(4, flight.getDestination());
            ps.setString(5, flight.getDepartTime());
            ps.setString(6, flight.getArriveTime());
            ps.setString(7, flight.getDuration());
            ps.setDouble(8, flight.getPrice());
            ps.setInt(9, flight.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du vol : " + e.getMessage());
        }
    }

    @Override
    public void delete(int flightId) {
        try {
            Connection cnx = SingletonConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement("DELETE FROM flight WHERE id=?");
            ps.setInt(1, flightId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du vol : " + e.getMessage());
        }
    }
}
