package metier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import dao.SingletonConnection; // ta classe pour la connexion JDBC

public class ReservationService {

    
    public void saveReservation(int userId, int flightId, String seatCode, double price) {
        String sql = "INSERT INTO reservation (user_id, flight_id, seat_code, reserved_at, price) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = SingletonConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, flightId);
            ps.setString(3, seatCode);
            ps.setString(4, LocalDateTime.now().toString()); 
            ps.setDouble(5, price); 

            ps.executeUpdate();
            System.out.println("Réservation enregistrée avec succès !");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'enregistrement de la réservation !");
        }
    }
}
