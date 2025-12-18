package dao;

import entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

   
    @Override
    public Optional<User> login(String email, String password) {
        try {
            Connection cnx = SingletonConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(
                "SELECT * FROM user WHERE email = ? AND password = ?"
            );
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                );
                return Optional.of(user);
            }

        } catch (Exception e) {
            System.out.println("Erreur DAO login : " + e.getMessage());
        }

        return Optional.empty();
    }

   
    @Override
    public void register(String username, String email, String password) {
        try {
            Connection cnx = SingletonConnection.getConnection();
            PreparedStatement ps = cnx.prepareStatement(
                "INSERT INTO user(username, email, password) VALUES (?, ?, ?)"
            );
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'inscription ❌");
        }
    }
}
