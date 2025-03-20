package controller;

import dao.userDao.UserDAO;
import service.product.IProductService;
import service.product.ProductService;
import service.user.IUserService;
import service.user.UserService;
import utils.debug.ErrorDialog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "dashboard", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {
        private IProductService productService = new ProductService();
        private UserService userService = new UserService();
    public DashboardServlet() throws SQLException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int productCount = productService.countProducts();
        int userCount = userService.countUsers();

//        ErrorDialog.showError("Count : " + userCount,"Dashboard");
        List<Object[]> stockByBrandList = productService.getStockByBrand();
        List<Object[]> revenueByBrand = productService.getRevenueByBrand();
        JSONArray stockByBrandArray = new JSONArray();
        for (Object[] stockData : stockByBrandList) {
            JSONObject json = new JSONObject();
            json.put("brand", stockData[0]); // BrandName
            json.put("stock", stockData[1]); // total_stock
            stockByBrandArray.put(json);
        }
        req.setAttribute("userCount", userCount);
        req.setAttribute("productCount", productCount);
        req.setAttribute("stockByBrand", stockByBrandArray.toString());
        req.setAttribute("revenueByBrand", revenueByBrand.toString());

        req.getRequestDispatcher("dashboard/dashboard.jsp").forward(req, resp);
    }


}
