package dao.brandDao;

import dao.DBConnection;
import model.Brand;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrandDAO {
    private static final String INSERT_BRAND_SQL = "INSERT INTO brand (brandName, country, imageURL) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_BRANDS = "SELECT * FROM brand";
    private static final String SELECT_BRAND_BY_ID = "SELECT * FROM brand WHERE brandID = ?";
    private static final String UPDATE_BRAND_SQL = "UPDATE brand SET brandName = ?, country = ?, imageURL = ? WHERE brandID = ?";
    private static final String DELETE_BRAND_SQL = "DELETE FROM brand WHERE brandID = ?";


    public BrandDAO() {

    }
    // Thêm thương hiệu mới
    public void addBrand(Brand brand) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BRAND_SQL)) {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Connection is not valid.");
            }
            preparedStatement.setString(1, brand.getBrandName());
            preparedStatement.setString(2, brand.getCountry());
            preparedStatement.setString(3, brand.getImageURL());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding brand: " + e.getMessage(), e);
        }
    }

    // Lấy tất cả thương hiệu
    public List<Brand> getAllBrands() throws SQLException {
        List<Brand> brands = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BRANDS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Connection is not valid.");
            }
            while (resultSet.next()) {
                int brandID = resultSet.getInt("brandID");
                String brandName = resultSet.getString("brandName");
                String country = resultSet.getString("country");
                String imageURL = resultSet.getString("imageURL");
                brands.add(new Brand(brandID, brandName, country, imageURL));
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving brands: " + e.getMessage(), e);
        }
        return brands;
    }

    // Lấy thông tin thương hiệu theo ID
    public Brand getBrandById(int brandID) throws SQLException {
        Brand brand = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BRAND_BY_ID)) {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Connection is not valid.");
            }
            preparedStatement.setInt(1, brandID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String brandName = resultSet.getString("brandName");
                String country = resultSet.getString("country");
                String imageURL = resultSet.getString("imageURL");
                brand = new Brand(brandID, brandName, country, imageURL);
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving brand by ID: " + e.getMessage(), e);
        }
        return brand;
    }

    // Cập nhật thông tin thương hiệu
    public void updateBrand(Brand brand) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BRAND_SQL)) {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Connection is not valid.");
            }
            preparedStatement.setString(1, brand.getBrandName());
            preparedStatement.setString(2, brand.getCountry());
            preparedStatement.setString(3, brand.getImageURL());
            preparedStatement.setInt(4, brand.getBrandID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating brand: " + e.getMessage(), e);
        }
    }

    // Xóa thương hiệu
    public void deleteBrand(int brandID) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BRAND_SQL)) {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Connection is not valid.");
            }
            preparedStatement.setInt(1, brandID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error deleting brand: " + e.getMessage(), e);
        }
    }
}
