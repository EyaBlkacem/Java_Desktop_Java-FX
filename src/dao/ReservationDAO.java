package dao;

import entities.Reservation;

import java.util.List;

public interface ReservationDAO {
    void reserve(int userId, int flightId, String seatCode, double price);
    List<Reservation> findByUser(int userId);
    void cancelReservation(int reservationId);
    void delete(int reservationId);
}
