package controller;

import model.User;
import service.user.IUserService;
import service.user.UserService;
import utils.validate.Validation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@WebServlet(name = "register", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    private IUserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String userRole = req.getParameter("userRole");

        Optional<User> checkUserByUsername = userService.findByUsername(username);
        Optional<User> checkUserByEmail =  userService.findByEmail(email);
        boolean isValidUsername = Validation.validateUsername(username);
        boolean isValidFullName = Validation.validateFullName(fullName);
        boolean isValidEmail = Validation.validateEmail(email);

        if (!isValidUsername || !isValidFullName || !isValidEmail || password == null || password.trim().isEmpty()) {
            req.setAttribute("username", username);
            req.setAttribute("password", password);
            req.setAttribute("fullName", fullName);
            req.setAttribute("email", email);
            session.setAttribute("errorMessage", "Thông tin không hợp lệ. Vui lòng kiểm tra lại!");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if (checkUserByUsername.isPresent()) {
            session.setAttribute("errorMessage", "Tên tài khoản đã tồn tại. Vui lòng chọn tên khác!");
            req.setAttribute("username", username);
            req.setAttribute("password", password);
            req.setAttribute("fullName", fullName);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if (checkUserByEmail.isPresent()) {
            session.setAttribute("errorMessage", "Email đã tồn tại. Vui lòng sử dụng email khác!");
            req.setAttribute("username", username);
            req.setAttribute("password", password);
            req.setAttribute("fullName", fullName);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }
        // Tạo người dùng mới
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        if (userRole == null || userRole.trim().isEmpty()) {
            userRole = "customer";
        }
        newUser.setUserRole(userRole);
        newUser.setUserCreatedDate(LocalDateTime.now());
        newUser.setUserUpdatedDate(LocalDateTime.now());
        newUser.setUserStatus("active");

        try {
            userService.addUser(newUser);
            session.setAttribute("loggedInUser", newUser);
            session.setAttribute("successMessage", "Đăng ký tài khoản thành công!");
            System.out.println(newUser.getUserRole());
            resp.sendRedirect(req.getContextPath() + "/product");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Đã xảy ra lỗi khi tạo người dùng!");
            req.getRequestDispatcher("/product").forward(req, resp);
        }
    }
}