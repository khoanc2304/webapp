package dao.userDao;

import dao.DBConnection;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements IUserDAO {
    // Các câu truy vấn SQL
    private static final String INSERT_USER = "INSERT INTO Users (username, password, fullName, email, phone, address, avatar, userCreatedDate, userUpdatedDate, userStatus, userRole) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_USERS = "SELECT * FROM Users";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM Users WHERE userID = ?";
    private static final String SELECT_USER_BY_USERNAME_AND_PASSWORD = "SELECT * FROM Users WHERE username = ? AND password = ?";
    private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM Users WHERE username = ?";
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM Users WHERE email = ?";
    private static final String SEARCH_USERS = "SELECT * FROM Users WHERE 1=1 " +
            "AND (username LIKE ? OR fullName LIKE ? OR email LIKE ? OR phone LIKE ? OR address LIKE ? OR userRole LIKE ? OR userStatus LIKE ?)";
    private static final String UPDATE_USER = "UPDATE Users SET username = ?, password = ?, fullName = ?, email = ?, phone = ?, address = ?, avatar = ?, userCreatedDate = ?, userUpdatedDate = ?, userStatus = ?, userRole = ? WHERE userID = ?";
    private static final String DELETE_USER_BY_USERNAME = "DELETE FROM Users WHERE username = ?";
    private static final String COUNT_QUERY = "SELECT COUNT(*) FROM Users";
    private static final String UPDATE_PHONE_ADDRESS = "UPDATE Users SET phone = ?, address = ?, userUpdatedDate = CURRENT_TIMESTAMP WHERE userID = ?";

    @Override
    public void addUser(User user) {
        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFullName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhone());
            preparedStatement.setString(6, user.getAddress());
            preparedStatement.setString(7, user.getAvatar());
            preparedStatement.setTimestamp(8, Timestamp.valueOf(user.getUserCreatedDate()));
            preparedStatement.setTimestamp(9, Timestamp.valueOf(user.getUserUpdatedDate()));
            preparedStatement.setString(10, user.getUserStatus());
            preparedStatement.setString(11, user.getUserRole());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Hoặc bạn có thể ném ngoại lệ tùy ý.
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findUserByID(int userID) {
        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public Optional<User> findUserByUsernameAndPassword(String username, String password) {
        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERNAME);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> searchUsers(String searchKeyword) {
        List<User> users = new ArrayList<>();
        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_USERS);
            String searchPattern = "%" + searchKeyword + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            preparedStatement.setString(3, searchPattern);
            preparedStatement.setString(4, searchPattern);
            preparedStatement.setString(5, searchPattern);
            preparedStatement.setString(6, searchPattern);
            preparedStatement.setString(7, searchPattern);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void updateUser(User user) {
        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFullName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhone());
            preparedStatement.setString(6, user.getAddress());
            preparedStatement.setString(7, user.getAvatar());
            preparedStatement.setTimestamp(8, Timestamp.valueOf(user.getUserCreatedDate()));
            preparedStatement.setTimestamp(9, Timestamp.valueOf(user.getUserUpdatedDate()));
            preparedStatement.setString(10, user.getUserStatus());
            preparedStatement.setString(11, user.getUserRole());
            preparedStatement.setInt(12, user.getUserID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteUserByUsername(String username) {
        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_USERNAME);
            preparedStatement.setString(1, username);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int countUsers() {
        int count = 0;
        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(COUNT_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return count;
    }

    @Override
    public User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("userID"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("fullName"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getString("avatar"),
                rs.getTimestamp("userCreatedDate").toLocalDateTime(),
                rs.getTimestamp("userUpdatedDate").toLocalDateTime(),
                rs.getString("userStatus"),
                rs.getString("userRole")
        );
    }

    @Override
    public void updatePhoneAndAddressUser(User user) {
        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(UPDATE_PHONE_ADDRESS);
            ps.setString(1, user.getPhone());
            ps.setString(2, user.getAddress());
            ps.setInt(3, user.getUserID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating user info", e);
        }
    }
}