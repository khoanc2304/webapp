package controller;

import model.Brand;
import service.brand.BrandService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/brand")
public class BrandServlet extends HttpServlet {
    private static BrandService brandService = new BrandService();

    // Phương thức kiểm tra quyền truy cập
    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false); // Không tạo session mới
        if (session != null) {
            String userRole = (String) session.getAttribute("userRole");
            return "admin".equals(userRole);
        }
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Kiểm tra quyền truy cập
        if (!isAdmin(req)) {
            HttpSession session = req.getSession(true);
            session.setAttribute("errorMessage", "Bạn không có quyền truy cập trang này. Chỉ admin mới được phép truy cập.");
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) {
            action = "list"; // Mặc định hiển thị danh sách
        }
        try {
            switch (action) {
                case "list":
                    listBrands(req, resp);
                    break;
                case "add":
                    showAddForm(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "delete":
                    deleteBrand(req, resp);
                    break;
                default:
                    listBrands(req, resp);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Kiểm tra quyền truy cập
        if (!isAdmin(req)) {
            HttpSession session = req.getSession(true);
            session.setAttribute("errorMessage", "Bạn không có quyền truy cập trang này. Chỉ admin mới được phép truy cập.");
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) {
            action = "list";
        }
        try {
            switch (action) {
                case "add":
                    addBrand(req, resp);
                    break;
                case "edit":
                    updateBrand(req, resp);
                    break;
                default:
                    listBrands(req, resp);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listBrands(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        List<Brand> brands = brandService.getAllBrands();
        req.setAttribute("brands", brands);
        req.getRequestDispatcher("/dashboard/brand/brandList.jsp").forward(req, resp);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/dashboard/brand/brandForm.jsp").forward(req, resp);
    }

    private void addBrand(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String brandName = req.getParameter("brandName");
        String country = req.getParameter("country");
        String imageURL = req.getParameter("imageURL");

        Brand newBrand = new Brand(0, brandName, country, imageURL);
        brandService.addBrand(newBrand);
        resp.sendRedirect(req.getContextPath() + "/brand?action=list");
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int brandID = Integer.parseInt(req.getParameter("brandID"));
        Brand existingBrand = brandService.getBrandById(brandID);
        req.setAttribute("brand", existingBrand);
        req.getRequestDispatcher("/dashboard/brand/brandForm.jsp").forward(req, resp);
    }

    private void updateBrand(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        int brandID = Integer.parseInt(req.getParameter("brandID"));
        String brandName = req.getParameter("brandName");
        String country = req.getParameter("country");
        String imageURL = req.getParameter("imageURL");

        Brand brand = new Brand(brandID, brandName, country, imageURL);
        brandService.updateBrand(brand);
        resp.sendRedirect(req.getContextPath() + "/brand?action=list");
    }

    private void deleteBrand(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        int brandID = Integer.parseInt(req.getParameter("brandID"));
        brandService.deleteBrand(brandID);
        resp.sendRedirect(req.getContextPath() + "/brand?action=list");
    }
}