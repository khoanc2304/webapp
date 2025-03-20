package dao.productSoldDao;

import dao.DBConnection;
import model.ProductSold;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductSoldDAO implements IProductSoldDAO {
    private static final String INSERT_PRODUCT_SOLD = "INSERT INTO ProductSold (productId, quantity, dateSold, userId) VALUES (?, ?, ?, ?)";
    private static final String SELECT_PRODUCT_SOLD_BY_ID = "SELECT * FROM ProductSold WHERE id = ?";
    private static final String SELECT_PRODUCT_SOLD_BY_PRODUCT_ID = "SELECT * FROM ProductSold WHERE productId = ?";
    private static final String SELECT_PRODUCT_SOLD_BY_USER_ID = "SELECT * FROM ProductSold WHERE userId = ?";
    private static final String SELECT_ALL_PRODUCT_SOLD = "SELECT * FROM ProductSold";
    private static final String UPDATE_PRODUCT_STOCK = "UPDATE Product SET stock = stock - ? WHERE productID = ?";
    private static final String UPDATE_PRODUCT_SOLD = "UPDATE ProductSold SET productId = ?, quantity = ?, dateSold = ?, userId = ? WHERE id = ?";
    private static final String DELETE_PRODUCT_SOLD = "DELETE FROM ProductSold WHERE id = ?";

    private DBConnection dbConnection = new DBConnection();
    @Override
    public void create(ProductSold productSold) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatementInsert = connection.prepareStatement(INSERT_PRODUCT_SOLD, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement preparedStatementUpdateStock = connection.prepareStatement(UPDATE_PRODUCT_STOCK)) {

            connection.setAutoCommit(false); // Start transaction

            // 1. Chèn sản phẩm bán vào bảng ProductSold
            preparedStatementInsert.setInt(1, productSold.getProductId());
            preparedStatementInsert.setInt(2, productSold.getQuantity());
            preparedStatementInsert.setTimestamp(3, productSold.getDateSold());
            preparedStatementInsert.setInt(4, productSold.getUserId());
            preparedStatementInsert.executeUpdate();

            // Lấy ID được tạo tự động cho ProductSold
            try (ResultSet generatedKeys = preparedStatementInsert.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    productSold.setId(generatedKeys.getInt(1));
                }
            }

            // 2. Cập nhật số lượng sản phẩm trong kho
            preparedStatementUpdateStock.setInt(1, productSold.getQuantity());
            preparedStatementUpdateStock.setInt(2, productSold.getProductId());
            preparedStatementUpdateStock.executeUpdate();

            // Commit transaction nếu mọi thứ thành công
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection connection = dbConnection.getConnection()) {
                connection.rollback(); // Rollback transaction nếu có lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public ProductSold findById(int id) {
        ProductSold productSold = null;
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_SOLD_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    productSold = mapResultSetToProductSold(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productSold;

    }

    @Override
    public List<ProductSold> findByProductId(int productId) {
        List<ProductSold> productSoldList = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_SOLD_BY_PRODUCT_ID)) {
            preparedStatement.setInt(1, productId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    productSoldList.add(mapResultSetToProductSold(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productSoldList;
    }

    @Override
    public List<ProductSold> findByUserId(int userId) {
        List<ProductSold> productSoldList = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_SOLD_BY_USER_ID)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    productSoldList.add(mapResultSetToProductSold(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productSoldList;
    }

    @Override
    public List<ProductSold> findAll() {
        List<ProductSold> productSoldList = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCT_SOLD);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                productSoldList.add(mapResultSetToProductSold(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productSoldList;
    }

    @Override
    public void update(ProductSold productSold) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_SOLD)) {
            preparedStatement.setInt(1, productSold.getProductId());
            preparedStatement.setInt(2, productSold.getQuantity());
            preparedStatement.setTimestamp(3, productSold.getDateSold());
            preparedStatement.setInt(4, productSold.getUserId());
            preparedStatement.setInt(5, productSold.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT_SOLD)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private ProductSold mapResultSetToProductSold(ResultSet rs) throws SQLException {
        ProductSold productSold = new ProductSold();
        productSold.setId(rs.getInt("id"));
        productSold.setProductId(rs.getInt("productId"));
        productSold.setQuantity(rs.getInt("quantity"));
        productSold.setDateSold(rs.getTimestamp("dateSold"));
        productSold.setUserId(rs.getInt("userId"));
        return productSold;
    }
}
