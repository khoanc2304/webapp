package controller;

import dao.userDao.UserDAO;
import model.*;
import service.order.EmailService;
import service.order.IOrderService;
import service.order.OrderService;
import service.product.IProductService;
import service.product.ProductService;
import service.productSold.IProductSoldService;
import service.productSold.ProductSoldService;
import service.user.IUserService;
import service.user.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet(urlPatterns = "/checkout")
public class CheckoutServlet extends HttpServlet {
    private final IProductService productService = new ProductService();
    private final IOrderService orderService = new OrderService();
    private final IProductSoldService productSoldService = new ProductSoldService();
    private final IUserService userService = new UserService();
    private final EmailService emailService = new EmailService();

    public CheckoutServlet() throws SQLException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // Log để kiểm tra
        System.out.println("doGet - UserId from session: " + userId);

        // Kiểm tra trạng thái đăng nhập (bao gồm Google và Facebook)
        if (userId == null) {
            userId = handleSocialLogin(session);
            System.out.println("doGet - UserId after handleSocialLogin: " + userId);
            if (userId == null) {
                session.setAttribute("redirectAfterLogin", req.getRequestURI());
                session.setAttribute("errorMessage", "Vui lòng đăng nhập để tiếp tục thanh toán");
                resp.sendRedirect(req.getContextPath() + "/loginPage.jsp");
                return;
            }
        }

        req.getRequestDispatcher("cart/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Integer userId = (Integer) session.getAttribute("userId");

        // Log để kiểm tra
        System.out.println("doPost - Cart in CheckoutServlet: " + cart);
        if (cart != null) {
            System.out.println("doPost - Cart items: " + cart.getItems());
        }
        System.out.println("doPost - UserId from session (before social login check): " + userId);

        // Kiểm tra trạng thái đăng nhập (bao gồm Google và Facebook)
        if (userId == null) {
            userId = handleSocialLogin(session);
            System.out.println("doPost - UserId after handleSocialLogin: " + userId);
            if (userId == null) {
                session.setAttribute("checkoutData", new Object[]{
                        req.getParameter("fullName"),
                        req.getParameter("phone"),
                        req.getParameter("address"),
                        req.getParameter("email"),
                        req.getParameter("deliveryMethod"),
                        req.getParameter("note"),
                        req.getParameter("paymentMethod")
                });
                session.setAttribute("redirectAfterLogin", req.getRequestURI());
                session.setAttribute("errorMessage", "Vui lòng đăng nhập để tiếp tục thanh toán");
                resp.sendRedirect(req.getContextPath() + "/loginPage.jsp");
                return;
            }
        }

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            req.setAttribute("errorMessage", "Giỏ hàng trống! Vui lòng thêm sản phẩm trước khi thanh toán.");
            req.getRequestDispatcher("cart/cart.jsp").forward(req, resp);
            return;
        }

        // Lấy thông tin từ form thanh toán
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String email = req.getParameter("email");
        String deliveryMethod = req.getParameter("deliveryMethod");
        String note = req.getParameter("note");
        String paymentMethod = req.getParameter("paymentMethod");

        // Tính phí vận chuyển dựa trên phương thức giao hàng
        double shippingFee = deliveryMethod.equals("standard") ? 25000 : 50000;
        double totalPrice = cart.getTotalPrice() + shippingFee;

        // Tạo một orderID ngẫu nhiên
        Random random = new Random();
        int orderID = 10000 + random.nextInt(90000);

        // Tạo đối tượng Order
        Order order = new Order(
                orderID,
                userId,
                new Timestamp(System.currentTimeMillis()),
                totalPrice,
                "Pending"
        );

        // Tạo danh sách chi tiết đơn hàng
        List<OrderDetail> orderDetails = new ArrayList<>();
        int orderDetailID = 1;
        Timestamp dateSold = new Timestamp(System.currentTimeMillis());
        List<ProductSold> productSoldList = new ArrayList<>();

// Map để gộp ProductSold theo productId
        Map<Integer, ProductSold> productSoldMap = new HashMap<>();

        for (Product item : cart.getItems()) {
            OrderDetail detail = new OrderDetail(
                    orderDetailID++,
                    order.getOrderID(),
                    item.getProductID(),
                    item.getCartQuantity(),
                    item.getPrice()
            );
            detail.setProductName(item.getName());
            orderDetails.add(detail);

            try {
                Optional<Product> product = productService.findProductById(item.getProductID());
                if (product != null) {
                    int productId = item.getProductID();
                    if (productSoldMap.containsKey(productId)) {
                        // Nếu đã có ProductSold cho productId, tăng quantity
                        ProductSold existing = productSoldMap.get(productId);
                        existing.setQuantity(existing.getQuantity() + item.getCartQuantity());
                    } else {
                        // Nếu chưa có, tạo mới ProductSold
                        ProductSold productSold = new ProductSold();
                        productSold.setProductId(productId);
                        productSold.setQuantity(item.getCartQuantity());
                        productSold.setDateSold(dateSold);
                        productSold.setUserId(userId);
                        productSoldMap.put(productId, productSold);
                    }
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi xử lý sản phẩm: " + e.getMessage());
                req.setAttribute("errorMessage", "Lỗi khi xử lý sản phẩm: " + e.getMessage());
                req.getRequestDispatcher("cart/checkout.jsp").forward(req, resp);
                return;
            }
        }

// Chuyển Map thành List
        productSoldList = new ArrayList<>(productSoldMap.values());

// Log danh sách ProductSold
        System.out.println("doPost - Number of ProductSold entries: " + productSoldList.size());
        for (ProductSold ps : productSoldList) {
            System.out.println("doPost - ProductSold - Product ID: " + ps.getProductId() + ", Quantity: " + ps.getQuantity());
        }

        order.setOrderDetail(orderDetails);

        // Lưu thông tin khách hàng vào session
        session.setAttribute("customerName", fullName);
        session.setAttribute("customerPhone", phone);
        session.setAttribute("customerAddress", address);
        session.setAttribute("customerEmail", email);

        try {
            orderService.create(order);
            for (ProductSold productSold : productSoldList) {
                productSoldService.create(productSold);
            }
            System.out.println("Đã lưu thành công đơn hàng!");
            // Gửi email xác nhận
            String userEmail = getUserEmail(session);
            if (userEmail != null && !userEmail.isEmpty()) {
                emailService.sendOrderConfirmation(userEmail, "Xác nhận đơn hàng từ 8386 Shop", order);
                System.out.println("Đã gửi email xác nhận đến: " + userEmail);
            } else {
                System.err.println("Không tìm thấy email để gửi xác nhận đơn hàng.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu đơn hàng: " + e.getMessage());
            req.setAttribute("errorMessage", "Lỗi khi lưu đơn hàng: " + e.getMessage());
            req.getRequestDispatcher("cart/checkout.jsp").forward(req, resp);
            return;
        }

        session.removeAttribute("cart");
        req.setAttribute("order", order);
        req.getRequestDispatcher("orderConfirmation.jsp").forward(req, resp);
        UserDAO userDAO = new UserDAO();
        Optional<User> userOptional = userDAO.findUserByID(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPhone(phone);  // Lưu số điện thoại vào User
            user.setAddress(address);  // Lưu địa chỉ vào User
            userDAO.updatePhoneAndAddressUser(user);  // Cập nhật thông tin user vào cơ sở dữ liệu
        }

    }

    // Phương thức xử lý đăng nhập bằng Google/Facebook
    private Integer handleSocialLogin(HttpSession session) {
        String googleName = (String) session.getAttribute("googleName");
        String facebookName = (String) session.getAttribute("facebookName");
        String googleEmail = (String) session.getAttribute("googleEmail");
        String facebookEmail = (String) session.getAttribute("facebookEmail");

        // Log để kiểm tra session
        System.out.println("handleSocialLogin - Google Name: " + googleName);
        System.out.println("handleSocialLogin - Google Email: " + googleEmail);
        System.out.println("handleSocialLogin - Facebook Name: " + facebookName);
        System.out.println("handleSocialLogin - Facebook Email: " + facebookEmail);

        // Xác định email từ Google hoặc Facebook
        String email = null;
        String fullName = null;
        String picture = null;

        if (googleName != null) {
            email = googleEmail;
            fullName = googleName;
            picture = (String) session.getAttribute("googlePicture");
        } else if (facebookName != null) {
            email = facebookEmail;
            fullName = facebookName;
            picture = (String) session.getAttribute("facebookPicture");
        }

        System.out.println("handleSocialLogin - Selected Email: " + email);

        if (email != null && !email.isEmpty()) {
            try {
                // Kiểm tra người dùng trong cơ sở dữ liệu
                System.out.println("handleSocialLogin - Finding user by email: " + email);
                Optional<User> userOptional = userService.findByEmail(email);
                User user;

                if (userOptional.isPresent()) {
                    user = userOptional.get();
                    System.out.println("handleSocialLogin - User found: " + user.getUserID());
                } else {
                    System.out.println("handleSocialLogin - User not found, creating new user...");
                    user = new User();
                    user.setEmail(email);
                    user.setFullName(fullName);
                    user.setAvatar(picture != null ? picture : "");
                    user.setPassword("");
                    user.setUserRole("customer");
                    user.setUserStatus("active");
                    user.setUserCreatedDate(LocalDateTime.now());
                    user.setUserUpdatedDate(LocalDateTime.now());
                    String baseUsername = email.split("@")[0];
                    String username = baseUsername;
                    int suffix = 1;
                    while (userService.findByUsername(username).isPresent()) {
                        username = baseUsername + suffix++;
                    }
                    user.setUsername(username);
                    user.setPhone("");
                    user.setAddress("");
                    try {
                        userService.addUser(user);
                        System.out.println("handleSocialLogin - New user created with email: " + email);
                        user = userService.findByEmail(email).orElse(null);
                    } catch (Exception e) {
                        System.err.println("handleSocialLogin - Lỗi khi tạo user mới: " + e.getMessage());
                        e.printStackTrace();
                        return null;
                    }
                }

                if (user == null) {
                    System.err.println("handleSocialLogin - Không thể lấy hoặc tạo user với email: " + email);
                    return null;
                }

                System.out.println("handleSocialLogin - User ID: " + user.getUserID());
                session.setAttribute("userId", user.getUserID());
                return user.getUserID();
            } catch (Exception e) {
                System.err.println("handleSocialLogin - Lỗi khi xử lý đăng nhập Google/Facebook: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("handleSocialLogin - Không tìm thấy email để xác định userId cho Google/Facebook login.");
            return null;
        }
    }
    // Phương thức lấy email từ session
    private String getUserEmail(HttpSession session) {
        // Ưu tiên email từ form thanh toán (nếu có)
        String customerEmail = (String) session.getAttribute("customerEmail");
        if (customerEmail != null && !customerEmail.isEmpty()) {
            return customerEmail;
        }

        // Nếu không có customerEmail, kiểm tra đăng nhập bằng Google
        String googleEmail = (String) session.getAttribute("googleEmail");
        if (googleEmail != null && !googleEmail.isEmpty()) {
            return googleEmail;
        }

        // Nếu không có googleEmail, kiểm tra đăng nhập bằng Facebook
        String facebookEmail = (String) session.getAttribute("facebookEmail");
        if (facebookEmail != null && !facebookEmail.isEmpty()) {
            return facebookEmail;
        }

        // Nếu không tìm thấy email, trả về null
        return null;
    }
}