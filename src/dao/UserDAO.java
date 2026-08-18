package dao;

import model.User;
import java.util.Optional;

public interface UserDAO {
    boolean registerUser(User user);
    Optional<User> login(String email, String password);
    Optional<User> getUserById(int userId); // Add this method signature
}