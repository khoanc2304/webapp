package service.user;

import dao.userDao.IUserDAO;
import dao.userDao.UserDAO;
import model.User;

import java.util.List;
import java.util.Optional;

public class UserService implements IUserService {
    //
    private final IUserDAO userDao = new UserDAO();

    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }

    @Override
    public Optional<User> findUserByID(int userID) {
        return userDao.findUserByID(userID);
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }

    @Override
    public Optional<User> findUserByUsernameAndPassword(String username, String password) {
        return userDao.findUserByUsernameAndPassword(username, password);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public List<User> searchUsers(String searchKeyword) {
        return userDao.searchUsers(searchKeyword);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public int countUsers() {
        return userDao.countUsers();
    }

    @Override
    public boolean deleteUserByUsername(String username) {
        return userDao.deleteUserByUsername(username);
    }
}