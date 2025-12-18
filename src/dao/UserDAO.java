package dao;

import entities.User;
import java.util.Optional;

public interface UserDAO {

  
    Optional<User> login(String email, String password);


    void register(String username, String email, String password);
}
