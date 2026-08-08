package dao;

import model.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    boolean registerUser(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(int userId);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int userId);
}