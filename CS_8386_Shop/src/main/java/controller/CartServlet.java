package controller;

import model.Cart;
import model.Product;
import service.cart.CartService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(urlPatterns = "/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "addToCart":
                addToCart(req, resp);
                break;
            case "increaseQuantity":
                increaseQuantity(req, resp);
                break;
            case "decreaseQuantity":
                decreaseQuantity(req, resp);
                break;
            default:
                displayCart(req, resp);
                break;
        }
    }

    private void addToCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy thông tin sản phẩm từ tham số
        String productIdParam = req.getParameter("productID");
        String name = req.getParameter("name");
        String priceParam = req.getParameter("price");
        String imageURL = req.getParameter("imageURL");

        // Kiểm tra các tham số
        if (productIdParam == null || name == null || priceParam == null || imageURL == null) {
            StringBuilder errorMessage = new StringBuilder("Thiếu thông tin sản phẩm để thêm vào giỏ hàng: ");
            if (productIdParam == null) errorMessage.append("productID is null, ");
            if (name == null) errorMessage.append("name is null, ");
            if (priceParam == null) errorMessage.append("price is null, ");
            if (imageURL == null) errorMessage.append("imageURL is null, ");
            req.setAttribute("errorMessage", errorMessage.toString());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            double price = Double.parseDouble(priceParam);
            int stock = 1; // Stock mặc định
            int cartQuantity = 1; // CartQuantity mặc định

            // Tạo đối tượng Product
            Product product = new Product();
            product.setProductID(productId);
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);
            product.setCartQuantity(cartQuantity);
            product.setImageURL(imageURL);

            // Lấy hoặc tạo CartService từ session
            HttpSession session = req.getSession();
            CartService sessionCartService;

            // Đồng bộ hóa việc truy cập CartService
            synchronized (session) {
                sessionCartService = (CartService) session.getAttribute("cartService");
                if (sessionCartService == null) {
                    sessionCartService = new CartService();
                    session.setAttribute("cartService", sessionCartService);
                }

                // Thêm sản phẩm vào giỏ hàng qua sessionCartService
                sessionCartService.addProduct(product);
                // Lấy Cart từ CartService và lưu vào session với key "cart"
                Cart cart = sessionCartService.getCart();
                session.setAttribute("cart", cart);
            }

            // Chuyển hướng về trang sản phẩm
            resp.sendRedirect("product");
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Dữ liệu không hợp lệ: " + e.getMessage() +
                    " [productID=" + productIdParam + ", price=" + priceParam + "]");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private void increaseQuantity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productIdParam = req.getParameter("productID");
        if (productIdParam == null) {
            req.setAttribute("errorMessage", "Thiếu productID để tăng số lượng.");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
            return;
        }
        try {
            int productId = Integer.parseInt(productIdParam);
            HttpSession session = req.getSession();
            CartService sessionCartService = (CartService) session.getAttribute("cartService");
            if (sessionCartService != null) {
                sessionCartService.increaseQuantity(productId);
            }
            Cart cart = sessionCartService.getCart();
            session.setAttribute("cart", cart);
            resp.sendRedirect("cart");
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "productID không hợp lệ: " + e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private void decreaseQuantity(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productIdParam = req.getParameter("productID");
        if (productIdParam == null) {
            req.setAttribute("errorMessage", "Thiếu productID để giảm số lượng.");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
            return;
        }
        try {
            int productId = Integer.parseInt(productIdParam);
            HttpSession session = req.getSession();
            CartService sessionCartService = (CartService) session.getAttribute("cartService");
            if (sessionCartService != null) {
                sessionCartService.decreaseQuantity(productId);
                Cart cart = sessionCartService.getCart();
                session.setAttribute("cart", cart);
            }
            resp.sendRedirect("cart");
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "productID không hợp lệ: " + e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private void displayCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("cart/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}