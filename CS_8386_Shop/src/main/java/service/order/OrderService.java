package service.order;

import dao.orderDao.IOrderDAO;
import dao.orderDao.OrderDAO;
import model.Order;

import java.sql.SQLException;
import java.util.List;

public class OrderService implements IOrderService {
    private IOrderDAO orderDAO = new OrderDAO();
    @Override
    public void create(Order order) throws SQLException {
        orderDAO.create(order);
    }

    @Override
    public List<Order> getAllOrdersWithUserInfo() {
        return orderDAO.getAllOrdersWithUserInfo();
    }

    @Override
    public void updateOrderStatus(int orderId, String status) {
        orderDAO.updateOrderStatus(orderId, status);
    }
}
