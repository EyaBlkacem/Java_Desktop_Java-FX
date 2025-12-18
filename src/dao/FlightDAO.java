package dao;

import entities.Flight;
import java.util.List;
import java.util.Optional;

public interface FlightDAO {
    List<Flight> findAll();
    Optional<Flight> findById(int id);
    void save(Flight flight);
    void update(Flight flight);
    void delete(int id);
}
