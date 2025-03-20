package controller;

import model.User;
import service.user.IUserService;
import service.user.UserService;
import utils.validate.Validation;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "users", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private IUserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        if (action == null) {
            action = "listUsers";
        }
        switch (action) {
            case "viewUser":
                viewUser(req, resp);
                break;
            case "createUser":
                showCreateForm(req, resp);
                break;
            case "updateUser":
                showUpdateForm(req, resp);
                break;
            case "deleteUser":
                deleteUser(req, resp);
                break;
            default:
                listUsers(req, resp);
                break;
        }
    }

    private void listUsers(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            session.setAttribute("errorMessage", "Bạn cần phải đăng nhập!");
            try {
                resp.sendRedirect(req.getContextPath() + "/loginPage.jsp");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if ("customer".equals(loggedInUser.getUserRole())) {
            session.setAttribute("warningMessage", "Bạn không có quyền truy cập Dashboard");
            try {
                resp.sendRedirect(req.getContextPath() + "/product");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        String searchKeyword = req.getParameter("searchUser");
        List<User> listUsers;

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            listUsers = userService.searchUsers(searchKeyword.trim());

            if (listUsers.isEmpty()) {
                session.setAttribute("errorMessage", "Không tìm thấy!");
            }
        } else {
            listUsers = userService.findAllUsers();
        }
        req.setAttribute("listUsers", listUsers);

        try {
            req.getRequestDispatcher("/dashboard/users/listUsers.jsp").forward(req,resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        HttpSession session = req.getSession(false);
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            session.setAttribute("errorMessage", "Bạn cần phải đăng nhập!");
            resp.sendRedirect(req.getContextPath() + "/loginPage.jsp");
            return;
        }

        if ("customer".equals(loggedInUser.getUserRole())) {
            session.setAttribute("warningMessage", "Bạn không có quyền xoá người dùng khác!");
            resp.sendRedirect(req.getContextPath() + "/product");
            return;
        }

        Optional<User> userToDelete = userService.findByUsername(username);
        if (userToDelete.isEmpty()) {
            session.setAttribute("errorMessage", "Người dùng không tồn tại!");
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
            return;
        }
        User user= userToDelete.get();

        boolean canDeleteRole = false;
        switch (loggedInUser.getUserRole()) {
            case "admin":
                canDeleteRole = true;
                break;
            case "manager":
                canDeleteRole = "employee".equals(user.getUserRole()) || "customer".equals(user.getUserRole());
                break;
            case "employee":
                canDeleteRole = "customer".equals(user.getUserRole());
                break;
            default:
                canDeleteRole = false;
                break;
        }

        if (loggedInUser.getUsername().equals(username)) {
            session.setAttribute("warningMessage", "Bạn không có quyền tự xoá chính mình với vai trò này!");
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
            return;
        }

        if (!canDeleteRole) {
            session.setAttribute("warningMessage", "Bạn không có quyền xoá người dùng với vai trò này!");
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
            return;
        }

        try {
            boolean success = userService.deleteUserByUsername(username);
            if (success) {
                session.setAttribute("successMessage", "Đã xoá người dùng thành công!");
            } else {
                session.setAttribute("errorMessage", "Đã xảy ra lỗi khi xoá người dùng!");
            }
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Đã xảy ra lỗi khi xoá người dùng!");
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
        }
    }

    private void showUpdateForm(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        HttpSession session = req.getSession(false);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        String userRole = req.getParameter("userRole");

        if (loggedInUser == null) {
            session.setAttribute("errorMessage", "Bạn cần phải đăng nhập!");
            resp.sendRedirect(req.getContextPath() + "/loginPage.jsp");
            return;
        }

        Optional<User> userToUpdate = userService.findByUsername(username);
        User user = userToUpdate.get();
        if (userToUpdate.isEmpty()) {
            session.setAttribute("errorMessage", "Người dùng không tồn tại!");
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
            return;
        }

        boolean canUpdateRole = false;
        switch (loggedInUser.getUserRole()) {
            case "admin":
                canUpdateRole = true;
                break;
            case "manager":
                canUpdateRole = "employee".equals(user.getUserRole()) || "customer".equals(user.getUserRole());
                break;
            case "employee":
                canUpdateRole = "customer".equals(userRole);
                break;
            default:
                canUpdateRole = false;
                break;
        }

        if (loggedInUser.getUsername().equals(username)) {
            canUpdateRole = true;
        }

        if (!canUpdateRole) {
            session.setAttribute("warningMessage", "Bạn không có quyền cập nhật người dùng với vai trò này!");
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
            return;
        }


        req.setAttribute("username", user.getUsername());
        req.setAttribute("fullName", user.getFullName());
        req.setAttribute("email", user.getEmail());
        req.setAttribute("phone", user.getPhone());
        req.setAttribute("address", user.getAddress());
        req.setAttribute("avatar", user.getAvatar());
        req.setAttribute("userCreatedDate", user.getUserCreatedDate());
        req.setAttribute("userUpdatedDate", user.getUserUpdatedDate());
        req.setAttribute("userStatus", user.getUserStatus());
        req.setAttribute("userRole", user.getUserRole());

        req.setAttribute("user", user);
        req.getRequestDispatcher("/dashboard/users/updateUser.jsp").forward(req, resp);
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.getRequestDispatcher("/dashboard/users/createUser.jsp").forward(req, resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void viewUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        String username = req.getParameter("username");

        if (loggedInUser == null) {
            session.setAttribute("warningMessage", "Bạn cần phải đăng nhập!");
            resp.sendRedirect(req.getContextPath() + "/loginPage.jsp");
            return;
        }

        if ("customer".equals(loggedInUser.getUserRole())) {
            session.setAttribute("warningMessage", "Bạn không có quyền xem thông tin người dùng khác!");
            resp.sendRedirect(req.getContextPath() + "/product");
            return;
        }

        Optional<User> optionalUserToView;
        try {
            optionalUserToView = userService.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Không thể truy vấn người dùng!");
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
            return;
        }

        if (optionalUserToView.isEmpty()) {
            session.setAttribute("errorMessage", "Người dùng không tồn tại!");
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
            return;
        }

        User userToView = optionalUserToView.get();
        String loggedInUserRole = loggedInUser.getUserRole();
        String targetUserRole = userToView.getUserRole();

        if (loggedInUser.getUsername().equals(username)) {
            req.setAttribute("user", userToView);
            req.getRequestDispatcher("/dashboard/users/viewUser.jsp").forward(req, resp);
            return;
        }

        boolean canViewRole = false;
        switch (loggedInUserRole) {
            case "admin":
                canViewRole = true;
                break;
            case "manager":
                canViewRole = "employee".equals(targetUserRole) || "customer".equals(targetUserRole);
                break;
            case "employee":
                canViewRole = "customer".equals(targetUserRole);
                break;
        }
        if (!canViewRole) {
            session.setAttribute("warningMessage", "Bạn không có quyền xem thông tin người dùng này!");
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
            return;
        }

        try {
            req.setAttribute("user", userToView);
            req.getRequestDispatcher("/dashboard/users/viewUser.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Đã xảy ra lỗi khi xem thông tin người dùng!");
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        if (action == null) {
            action = "listUsers";
        }
        switch (action) {
            case "createUser":
                createUser(req, resp);
                break;
            case "updateUser":
                updateUser(req,resp);
                break;
            case "deleteUser":
                deleteUser(req, resp);
                break;
        }
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            session.setAttribute("errorMessage", "Bạn cần phải đăng nhập!");
            resp.sendRedirect(req.getContextPath() + "/loginPage.jsp");
            return;
        }

        if ("customer".equals(loggedInUser.getUserRole())) {
            session.setAttribute("warningMessage", "Bạn không có quyền chỉnh sửa người dùng khác!");
            resp.sendRedirect(req.getContextPath() + "/product");
            return;
        }

        String username = req.getParameter("username");
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String avatar = req.getParameter("avatar");
        String userStatus = req.getParameter("userStatus");
        String userRole = req.getParameter("userRole");

        boolean canUpdateRole = false;
        switch (loggedInUser.getUserRole()) {
            case "admin":
                canUpdateRole = true;
                break;
            case "manager":
                canUpdateRole = "employee".equals(userRole) || "customer".equals(userRole);
                break;
            case "employee":
                canUpdateRole = "customer".equals(userRole);
                break;
            default:
                canUpdateRole = false;
                break;
        }

        if (loggedInUser.getUsername().equals(username)) {
            canUpdateRole = true;
        }

        if (!canUpdateRole) {
            session.setAttribute("warningMessage", "Bạn không có quyền cập nhật người dùng với vai trò này!");

            req.setAttribute("username", username);
            req.setAttribute("fullName", fullName != null ? fullName : loggedInUser.getFullName());
            req.setAttribute("email", email != null ? email : loggedInUser.getEmail());
            req.setAttribute("phone", phone != null ? phone : loggedInUser.getPhone());
            req.setAttribute("address", address != null ? address : loggedInUser.getAddress());
            req.setAttribute("avatar", avatar != null ? avatar : loggedInUser.getAvatar());
            req.setAttribute("userStatus", userStatus != null ? userStatus : loggedInUser.getUserStatus());
            req.setAttribute("userRole", userRole != null ? userRole : loggedInUser.getUserRole());

            req.getRequestDispatcher("/dashboard/users/updateUser.jsp").forward(req, resp);
            return;
        }

        Optional<User> userToUpdate = userService.findByUsername(username);
        if (userToUpdate.isEmpty()) {
            session.setAttribute("errorMessage", "Không tìm thấy người dùng!");
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
            return;
        }

        User user = userToUpdate.get();
        user.setFullName(fullName != null ? fullName : user.getFullName());
        user.setEmail(email != null ? email : user.getEmail());
        user.setPhone(phone != null ? phone : user.getPhone());
        user.setAddress(address != null ? address : user.getAddress());
        user.setAvatar(avatar != null ? avatar : user.getAvatar());
        user.setUserStatus(userStatus != null ? userStatus : user.getUserStatus());
        user.setUserRole(userRole != null ? userRole : user.getUserRole());
        user.setUserUpdatedDate(LocalDateTime.now());

        try {
            userService.updateUser(user);
            session.setAttribute("successMessage", "Chỉnh sửa người dùng thành công!");
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật thông tin người dùng!");
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
        }
    }

    private void createUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            session.setAttribute("errorMessage", "Bạn cần phải đăng nhập!");
            resp.sendRedirect(req.getContextPath() + "/loginPage.jsp");
            return;
        }


        if ("customer".equals(loggedInUser.getUserRole())) {
            session.setAttribute("warningMessage", "Bạn không có quyền tạo người dùng khác!");
            resp.sendRedirect(req.getContextPath() + "/product");
            return;
        }

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String userRole = req.getParameter("userRole");


        Optional<User> checkUserByUsername = userService.findByUsername(username);
        Optional<User> checkUserByEmail = userService.findByEmail(email);
        boolean isValidUsername = Validation.validateUsername(username);
        boolean isValidFullName = Validation.validateFullName(fullName);
        boolean isValidEmail = Validation.validateEmail(email);

        if (!isValidUsername || !isValidFullName || !isValidEmail || password == null || password.trim().isEmpty() ||
                userRole == null || userRole.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Thông tin không hợp lệ. Vui lòng kiểm tra lại!");
            req.setAttribute("username", username);
            req.setAttribute("password", password);
            req.setAttribute("fullName", fullName);
            req.setAttribute("email", email);
            req.setAttribute("userRole", userRole);
            req.getRequestDispatcher("/dashboard/users/createUser.jsp").forward(req, resp);
            return;
        }

        if (checkUserByUsername.isPresent()) {
            session.setAttribute("errorMessage", "Tên tài khoản đã tồn tại. Vui lòng chọn tên khác!");
            req.setAttribute("username", username);
            req.setAttribute("password", password);
            req.setAttribute("fullName", fullName);
            req.setAttribute("email", email);
            req.setAttribute("userRole", userRole);
            req.getRequestDispatcher("/dashboard/users/createUser.jsp").forward(req, resp);
            return;
        }

        if (checkUserByEmail.isPresent()) {
            session.setAttribute("errorMessage", "Email đã tồn tại. Vui lòng sử dụng email khác!");
            req.setAttribute("username", username);
            req.setAttribute("password", password);
            req.setAttribute("fullName", fullName);
            req.setAttribute("email", email);
            req.setAttribute("userRole", userRole);
            req.getRequestDispatcher("/dashboard/users/createUser.jsp").forward(req, resp);
            return;
        }

        boolean canCreateRole = false;
        switch (loggedInUser.getUserRole()) {
            case "admin":
                canCreateRole = true;
                break;
            case "manager":
                canCreateRole = "employee".equals(userRole) || "customer".equals(userRole);
                break;
            case "employee":
                canCreateRole = "customer".equals(userRole);
                break;
        }

        if (!canCreateRole) {
            session.setAttribute("warningMessage", "Bạn không có quyền tạo người dùng với vai trò này!");
            req.setAttribute("username", username);
            req.setAttribute("password", password);
            req.setAttribute("fullName", fullName);
            req.setAttribute("email", email);
            req.setAttribute("userRole", userRole);
            req.getRequestDispatcher("/dashboard/users/createUser.jsp").forward(req, resp);
            return;
        }

        // Tạo người dùng mới
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setUserRole(userRole);
        newUser.setUserCreatedDate(LocalDateTime.now());
        newUser.setUserUpdatedDate(LocalDateTime.now());
        newUser.setUserStatus("active");

        try {
            userService.addUser(newUser);
            session.setAttribute("successMessage", "Tạo người dùng mới thành công!");
            resp.sendRedirect(req.getContextPath() + "/users?action=listUsers");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Đã xảy ra lỗi khi tạo người dùng!");
            req.getRequestDispatcher("/dashboard/users/listUsers.jsp").forward(req, resp);
        }
    }
    //
}