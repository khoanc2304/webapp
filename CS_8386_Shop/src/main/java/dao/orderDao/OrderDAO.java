package dao.orderDao;

import dao.DBConnection;
import model.Order;
import model.OrderDetail;
import model.ProductSold;
import service.productSold.IProductSoldService;
import service.productSold.ProductSoldService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements IOrderDAO {
    private static final String INSERT_ORDER = "INSERT INTO Orders (orderID, userId, orderDate, totalPrice, orderStatus) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_ORDER_DETAIL = "INSERT INTO OrderDetail (orderID, productID, quantity, price) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_PRODUCT_STOCK = "UPDATE Product SET stock = stock - ? WHERE productID = ?";
    private static final String SELECT_ALL_ORDERS_WITH_USER_INFO =
            "SELECT o.OrderID, u.fullName, u.email, u.phone, u.address, o.OrderDate, o.TotalPrice, o.orderStatus " +
                    "FROM Orders o " +
                    "JOIN Users u ON o.userID = u.userID " +
                    "ORDER BY o.OrderDate DESC";
    private static final String UPDATE_STATUS = "UPDATE Orders SET orderStatus = ? WHERE OrderID = ?";
    private DBConnection dbConnection = new DBConnection();
    private IProductSoldService productSoldService = new ProductSoldService();
    @Override
    public void create(Order order) throws SQLException {
        try (Connection connection = dbConnection.getConnection()) {
            // Bắt đầu transaction để đảm bảo tính toàn vẹn dữ liệu
            connection.setAutoCommit(false);

            // Chèn đơn hàng vào bảng Orders
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER)) {
                preparedStatement.setInt(1, order.getOrderID());
                preparedStatement.setInt(2, order.getUserID());
                preparedStatement.setTimestamp(3, order.getOrderDate());
                preparedStatement.setDouble(4, order.getTotalPrice());
                preparedStatement.setString(5, order.getOrderStatus());
                preparedStatement.executeUpdate();
            }

            // Chèn chi tiết đơn hàng vào bảng OrderDetails và cập nhật số lượng sản phẩm trong kho
            try (PreparedStatement preparedStatementOrderDetail = connection.prepareStatement(INSERT_ORDER_DETAIL);
                 PreparedStatement preparedStatementUpdateProduct = connection.prepareStatement(UPDATE_PRODUCT_STOCK)) {

                // Lặp qua các chi tiết đơn hàng
                for (OrderDetail detail : order.getOrderDetail()) {
                    // Thêm chi tiết vào bảng OrderDetails
                    preparedStatementOrderDetail.setInt(1, order.getOrderID());
                    preparedStatementOrderDetail.setInt(2, detail.getProductID());
                    preparedStatementOrderDetail.setInt(3, detail.getQuantity());
                    preparedStatementOrderDetail.setDouble(4, detail.getPrice());
                    preparedStatementOrderDetail.addBatch();

//                    // Cập nhật số lượng kho trong bảng Product
//                    preparedStatementUpdateProduct.setInt(1, detail.getQuantity());
//                    preparedStatementUpdateProduct.setInt(2, detail.getProductID());
//                    preparedStatementUpdateProduct.addBatch();

                    // Lưu thông tin bán hàng vào bảng ProductSold
//                    ProductSold productSold = new ProductSold();
//                    productSold.setProductId(detail.getProductID());
//                    productSold.setQuantity(detail.getQuantity());
//                    productSold.setDateSold(order.getOrderDate());
//                    productSold.setUserId(order.getUserID());
                    //productSoldService.create(productSold);
                }

                // Thực hiện các batch
                preparedStatementOrderDetail.executeBatch();
//                preparedStatementUpdateProduct.executeBatch();
            }

            // Commit transaction nếu mọi thứ thành công
            connection.commit();
        } catch (SQLException e) {
            // Nếu có lỗi, rollback transaction
            System.err.println("Lỗi khi lưu đơn hàng hoặc chi tiết đơn hàng: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Order> getAllOrdersWithUserInfo() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ORDERS_WITH_USER_INFO);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("OrderID"),
                        0, // userID không cần thiết ở đây, để 0
                        rs.getTimestamp("OrderDate"),
                        rs.getDouble("TotalPrice"),
                        rs.getString("orderStatus")
                );
                order.setFullName(rs.getString("fullName"));
                order.setEmail(rs.getString("email"));
                order.setPhone(rs.getString("phone"));
                order.setAddress(rs.getString("address"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách đơn hàng: " + e.getMessage());
        }
        return orders;
    }

    @Override
    public void updateOrderStatus(int orderId, String status) {
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(UPDATE_STATUS)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
