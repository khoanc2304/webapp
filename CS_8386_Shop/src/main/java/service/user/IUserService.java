package service.user;

import model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    //
    // Create
    void addUser(User user);

    // Find
    Optional<User> findUserByID(int userID);
    List<User> findAllUsers();
    Optional<User> findUserByUsernameAndPassword(String username, String password);
    Optional<User>  findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> searchUsers(String searchKeyword);

    // Update
    void updateUser(User user);

    //Count
    int countUsers();

    // Delete
    boolean deleteUserByUsername(String username);


}