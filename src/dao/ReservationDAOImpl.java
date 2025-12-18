package dao;

import entities.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    @Override
    public void reserve(int userId, int flightId, String seatCode, double price) {
        String sql = """
            INSERT INTO reservation(user_id, flight_id, seat_code, reserved_at, price)
            VALUES (?, ?, ?, NOW(), ?)
        """;

        try (Connection cnx = SingletonConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, flightId);
            ps.setString(3, seatCode);
            ps.setDouble(4, price);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Reservation> findByUser(int userId) {
        List<Reservation> list = new ArrayList<>();
        String sql = """
            SELECT r.id, f.airline, f.departure, f.destination,
                   r.seat_code, r.price, r.reserved_at
            FROM reservation r
            JOIN flight f ON r.flight_id = f.id
            WHERE r.user_id = ?
            ORDER BY r.reserved_at DESC
        """;

        try (Connection cnx = SingletonConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Reservation(
                        rs.getInt("id"),
                        rs.getString("airline"),
                        rs.getString("departure"),
                        rs.getString("destination"),
                        rs.getString("seat_code"),
                        rs.getDouble("price"),
                        rs.getTimestamp("reserved_at").toLocalDateTime()
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void delete(int reservationId) {
        try (Connection cnx = SingletonConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement("DELETE FROM reservation WHERE id=?")) {

            ps.setInt(1, reservationId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelReservation(int reservationId) {
        // On peut réutiliser delete
        delete(reservationId);
    }
}
