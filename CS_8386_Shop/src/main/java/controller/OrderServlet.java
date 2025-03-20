package controller;

import model.Order;
import service.order.IOrderService;
import service.order.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/orders")
public class OrderServlet extends HttpServlet {
    private IOrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("updateStatus".equals(action)) {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            String newStatus = req.getParameter("orderStatus");

            // Cập nhật trạng thái đơn hàng
            orderService.updateOrderStatus(orderId, newStatus);

            // Chuyển hướng lại trang danh sách đơn hàng
            resp.sendRedirect("orders");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String userRole = (String) session.getAttribute("userRole");

        // Kiểm tra quyền truy cập
        if (userRole == null || !userRole.equals("admin")) {
            resp.sendRedirect(req.getContextPath() + "/loginPage.jsp");
            return;
        }

        // Lấy danh sách đơn hàng
        List<Order> orders = orderService.getAllOrdersWithUserInfo();
        req.setAttribute("orders", orders);

        // Chuyển hướng đến trang JSP
        req.getRequestDispatcher("/dashboard/order/order.jsp").forward(req, resp);
    }
}
