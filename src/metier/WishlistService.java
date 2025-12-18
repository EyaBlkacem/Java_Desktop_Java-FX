package metier;

import dao.SingletonConnection;
import entities.WishlistItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistService {

  
    public void add(int userId, int flightId) {

        String sql = "INSERT INTO wishlist(user_id, flight_id) VALUES (?, ?)";

        try (PreparedStatement ps =
                     SingletonConnection.getConnection().prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, flightId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Erreur ajout wishlist ❌");
        }
    }

    
    public List<WishlistItem> findByUser(int userId) {

        List<WishlistItem> list = new ArrayList<>();

        String sql = """
            SELECT 
                w.id,
                f.id AS flight_id,
                f.airline,
                f.departure,
                f.destination,
                f.price
            FROM wishlist w
            JOIN flight f ON w.flight_id = f.id
            WHERE w.user_id = ?
        """;

        try (Connection cnx = SingletonConnection.getConnection();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new WishlistItem(
                        rs.getInt("id"),
                        rs.getInt("flight_id"),
                        rs.getString("airline"),
                        rs.getString("departure"),
                        rs.getString("destination"),
                        rs.getDouble("price")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    
    public void delete(int wishlistId) {

        try (PreparedStatement ps =
                     SingletonConnection.getConnection()
                             .prepareStatement("DELETE FROM wishlist WHERE id = ?")) {

            ps.setInt(1, wishlistId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}