package metier;

import dao.FlightDAO;
import dao.FlightDAOImpl;
import dao.ReservationDAO;
import dao.ReservationDAOImpl;
import entities.Flight;

import java.util.List;
import java.util.stream.Collectors;

public class FlightService {

    private final FlightDAO flightDAO = new FlightDAOImpl();
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();

    
    public List<Flight> search(String departure, String destination) {
        String dep = departure == null ? "" : departure.trim().toUpperCase();
        String dest = destination == null ? "" : destination.trim().toUpperCase();

        return flightDAO.findAll()                 
                .stream()                          
                .filter(f -> dep.isEmpty()
                        || f.getDeparture().toUpperCase().contains(dep))
                .filter(f -> dest.isEmpty()
                        || f.getDestination().toUpperCase().contains(dest))
                .collect(Collectors.toList());     
    }

    
    public List<Flight> getAllFlights() {
        return flightDAO.findAll();
    }

    
    public void addFlight(Flight flight) {
        flightDAO.save(flight);
    }

  
    public void updateFlight(Flight flight) {
        flightDAO.update(flight);
    }

    
    public void deleteFlight(int id) {
        flightDAO.delete(id);
    }

    
    public void reserveFlight(int userId, int flightId) {
        if (!Session.isLoggedIn()) {
            throw new RuntimeException("Veuillez vous connecter !");
        }

       
        Flight flight = flightDAO.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Vol introuvable !"));

        String seatCode = "A1";
        double price = flight.getPrice();

        reservationDAO.reserve(userId, flightId, seatCode, price);
    }
}
