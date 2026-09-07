package dao;

import model.User;

import java.util.List;

public interface UserDAO extends MunicipalDAO {

    boolean registerUser(User user);

    User authenticate(String email, String password);

    User getUserById(int id);

    List<User> getAllUsers();
}