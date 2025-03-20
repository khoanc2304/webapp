package dao.orderDao;

import model.Order;

import java.sql.SQLException;
import java.util.List;

public interface IOrderDAO {
    void create(Order order) throws SQLException;
    List<Order> getAllOrdersWithUserInfo();
    void updateOrderStatus(int orderId, String status);
}
