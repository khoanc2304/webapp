package controller;

import model.User;
import model.UserGoogle;
import service.user.IUserService;
import service.user.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "login", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    //
    private final IUserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.getRequestDispatcher("/loginPage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String googleToken = req.getParameter("googleToken");
        HttpSession session = req.getSession(true);
        try {
            if (googleToken != null && !googleToken.isEmpty()) {
                // Đăng nhập qua Google chỉ dành cho khách hàng
                UserGoogle userGoogle = LoginGoogleServlet.getUserInfo(googleToken);
                if (userGoogle != null) {
                    // Lưu thông tin người dùng vào session (không cần tạo mới người dùng trong cơ sở dữ liệu)
                    session.setAttribute("googleId", userGoogle.getId());
                    session.setAttribute("googleName", userGoogle.getName());
                    session.setAttribute("googleEmail", userGoogle.getEmail());
                    session.setAttribute("googlePicture", userGoogle.getPicture());

                    // Đăng nhập thành công và điều hướng người dùng
                    session.setAttribute("successMessage", "Đăng nhập thành công");
                    resp.sendRedirect(req.getContextPath() + "/");
                    return;
                } else {
                    session.setAttribute("errorMessage", "Không thể lấy thông tin người dùng từ Google.");
                    resp.sendRedirect(req.getContextPath() + "/loginPage.jsp");
                    return;
                }
            } else {
                // Đăng nhập thông qua cơ sở dữ liệu
                Optional<User> loginUser = userService.findUserByUsernameAndPassword(username, password);
                if (loginUser.isPresent()) {
                    User user = loginUser.get();
                    session.setAttribute("userId",user.getUserID());
                    session.setAttribute("loggedInUser", user.getFullName());
                    session.setAttribute("userAvatar", user.getAvatar());
                    session.setAttribute("userRole", user.getUserRole());
                    session.setAttribute("loggedInUser", user);

                    String userRole = user.getUserRole();
                    if ("admin".equals(userRole)) {
                        resp.sendRedirect("/dashboard");
                    } else if ("manager".equals(userRole)) {
                        resp.sendRedirect("/dashboard");
                    } else if ("employee".equals(userRole)) {
                        resp.sendRedirect(req.getContextPath() + "/dashboard/dashboard.jsp");
                    } else {
                        resp.sendRedirect(req.getContextPath() + "/product");
                    }
                    session.setAttribute("successMessage", "Đăng nhập thành công");
                } else {
                    session.setAttribute("errorMessage", "Sai tài khoản hoặc mật khẩu! Vui lòng thử lại!");
                    resp.sendRedirect(req.getContextPath() + "/WEB-INF/view/login/loginPage.jsp");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Đã xảy ra lỗi trong quá trình đăng nhập. Vui lòng thử lại sau.");
            resp.sendRedirect(req.getContextPath() + "/WEB-INF/view/login/loginPage.jsp");
        }
    }
    }
