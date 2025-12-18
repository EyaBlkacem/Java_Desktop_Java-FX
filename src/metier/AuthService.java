package metier;

import dao.UserDAO;
import dao.UserDAOImpl;
import entities.User;

import java.util.Optional;

public class AuthService {

    private final UserDAO userDAO = new UserDAOImpl();

    public Optional<User> login(String email, String password) {
        return userDAO.login(email, password);
    }

    public void register(String username, String email, String password) {
        userDAO.register(username, email, password);
    }
}
