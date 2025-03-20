package dao.userDao;

import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public interface IUserDAO {
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

    // Delete
    boolean deleteUserByUsername(String username);

    //Count
    int countUsers();

    // Phương thức ánh xạ từ ResultSet sang đối tượng User
    User mapUser(ResultSet rs) throws SQLException;

    //update phone & address
    void updatePhoneAndAddressUser(User user);
}