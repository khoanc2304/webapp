package controller;

import model.Product;
import model.ProductReview;
import model.User;
import service.product.IProductService;
import service.product.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@WebServlet({"/","/product"})
public class ProductServlet extends HttpServlet {
    private final IProductService productService = new ProductService();

    public ProductServlet() throws SQLException {
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // Kiểm tra xem người dùng có phải là admin không
        String userRole = (String) req.getSession().getAttribute("userRole");
        User loggedInUser = (User) req.getSession().getAttribute("loggedInUser");
        if (userRole == null && loggedInUser != null) {
            userRole = loggedInUser.getUserRole(); // Lấy từ đối tượng User nếu chưa có
        }

//        if (userRole == null) {
//            resp.sendRedirect(req.getContextPath() + "/login"); // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
//            return;
//        }

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "viewProduct":
                viewProduct(req, resp);
                break;
            case "createProduct":
                if ("customer".equals(userRole)) {
                    resp.sendRedirect(req.getContextPath() + "/product");
                } else {
                    showCreateForm(req, resp);
                }
                break;
            case "updateProduct":
                if ("customer".equals(userRole)) {
                    resp.sendRedirect(req.getContextPath() + "/product");
                } else {
                    showUpdateForm(req, resp);
                }
                break;
            case "deleteProduct":
                if ("customer".equals(userRole)) {
                    resp.sendRedirect(req.getContextPath() + "/product");
                } else {
                    deleteProduct(req, resp);
                }
                break;
            case "product-detail":
                showProductDetail(req, resp);
                break;
            case "list-admin" :
                listProductsAdmin(req, resp);
                break;
            default:
                listProducts(req, resp);
                break;
        }
    }
    private void showProductDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productID = Integer.parseInt(request.getParameter("productID"));
        Product product = productService.getProductById(productID);

        if (product != null) {
            List<ProductReview> reviews = productService.getReviewsByProductId(productID);
            request.setAttribute("product", product);
            request.setAttribute("reviews", reviews);
            request.getRequestDispatcher("/productDetail.jsp").forward(request, response);
        } else {
            response.sendRedirect("error.jsp");
        }
    }

        private void listProductsAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String userRole = (String) req.getSession().getAttribute("userRole");
            if ("admin".equals(userRole)) {
                // Nếu là admin, hiển thị danh sách sản phẩm đầy đủ trong dashboard
                List<Product> productList = productService.getAllForAdmin();
                req.setAttribute("productList", productList);
                req.getRequestDispatcher("/dashboard/products/listProducts.jsp").forward(req, resp);
            }
    }
    private void listProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int pageSize = 9;
        int pageNumber = 1;
        if (req.getParameter("page") != null) {
            pageNumber = Integer.parseInt(req.getParameter("page"));
        }

        // Lấy minPrice và maxPrice từ request
        String minPriceParam = req.getParameter("minPrice");
        String maxPriceParam = req.getParameter("maxPrice");

        Double minPrice = (minPriceParam != null) ? Double.parseDouble(minPriceParam) : null;
        Double maxPrice = (maxPriceParam != null) ? Double.parseDouble(maxPriceParam) : null;


        // Lấy giá trị keyword từ request để tìm kiếm sản phẩm
        String keyword = req.getParameter("keyword");




            // Nếu là customer hoặc chưa đăng nhập, trả về danh sách sản phẩm chung
            List<Product> products;
            int totalProducts;

            if (keyword != null && !keyword.isEmpty()) {
                products = productService.searchProductsByName(keyword);
                totalProducts = products.size();
            } else if (minPrice != null && maxPrice != null) {
                products = productService.getProductsByPriceRange(minPrice, maxPrice, pageNumber, pageSize);
                totalProducts = productService.getTotalProductsByPriceRange(minPrice, maxPrice);
            } else {
                products = productService.getProductsByPage(pageNumber, pageSize);
                totalProducts = productService.getTotalProducts();
            }

            int totalPages = (int) Math.ceil(totalProducts / (double) pageSize);


            req.setAttribute("products", products);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("currentPage", pageNumber);
            req.setAttribute("keyword", keyword);

            req.getRequestDispatcher("product.jsp").forward(req, resp);
        }
//    private void searchProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String keyword = request.getParameter("keyword");
//        List<Product> products = productService.searchProductsByName(keyword);
//        request.setAttribute("products", products);
//        request.getRequestDispatcher("product.jsp").forward(request, response);
//

    private void viewProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productIdParam = req.getParameter("productID");

        if (productIdParam == null || productIdParam.isEmpty()) {
            req.getSession().setAttribute("errorMessage", "Sản phẩm không tồn tại!");
            resp.sendRedirect(req.getContextPath() + "/product?action=listProducts");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            Optional<Product> product = productService.findProductById(productId);

            if (product.isEmpty()) {
                req.getSession().setAttribute("errorMessage", "Sản phẩm không tồn tại!");
                resp.sendRedirect(req.getContextPath() + "/product?action=listProducts");
                return;
            }

            Product prod = product.get();
//            ErrorDialog.showError(""+ (prod.getName() + prod.getProductStatus() + prod.getBrandName()), "ProductServlet");
            Date createdDate = java.sql.Timestamp.valueOf(prod.getProduct_createdDate());
            Date updatedDate = java.sql.Timestamp.valueOf(prod.getProduct_updateDate());

            req.setAttribute("product", prod);
            req.setAttribute("createdDate", createdDate);
            req.setAttribute("updatedDate", updatedDate);

            req.getRequestDispatcher("/dashboard/products/viewProduct.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("errorMessage", "ID sản phẩm không hợp lệ!");
            resp.sendRedirect(req.getContextPath() + "/product?action=listProducts");
        }
    }



    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/dashboard/products/createProduct.jsp").forward(req, resp);
    }

    private void showUpdateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productIdParam = req.getParameter("productId");

        if (productIdParam == null || productIdParam.isEmpty()) {
            req.getSession().setAttribute("errorMessage", "Product ID is missing or invalid.");
            resp.sendRedirect(req.getContextPath() + "/products?action=listProducts");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            Optional<Product> product = productService.findProductById(productId);

            if (product.isEmpty()) {
                req.getSession().setAttribute("errorMessage", "Sản phẩm không tồn tại!");
                resp.sendRedirect(req.getContextPath() + "/products?action=listProducts");
                return;
            }

            req.setAttribute("product", product.get());
            req.getRequestDispatcher("/dashboard/products/updateProduct.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("errorMessage", "Product ID is not valid.");
            resp.sendRedirect(req.getContextPath() + "/products?action=listProducts");
        }
    }



    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {

            int productId = Integer.parseInt(req.getParameter("productID"));
            //ErrorDialog.showError(""+ productId , "Product");
            productService.deleteProduct(productId);
            req.getSession().setAttribute("successMessage", "Sản phẩm đã được xóa!");
            resp.sendRedirect(req.getContextPath() + "/product?action=listProducts");
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("errorMessage", "ID sản phẩm không hợp lệ!");
            resp.sendRedirect(req.getContextPath() + "/product?action=listProducts");
        }
    }


    //    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action = req.getParameter("action");
//
//        if (action == null) {
//            action = "listProducts";
//        }
//
//        switch (action) {
//            case "createProduct":
//                createProduct(req, resp);
//                break;
//            case "updateProduct":
//                updateProduct(req, resp);
//                break;
//                case "deleteProduct":
//                    deleteProduct(req, resp);
//                    break;
//
//            default:
//                resp.sendRedirect(req.getContextPath() + "/products?action=listProducts");
//                break;
//        }
//    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String action = req.getParameter("action");
        String userRole = (String) req.getSession().getAttribute("userRole");

        switch (action) {
            case "createProduct":
            case "updateProduct":
            case "deleteProduct":
                if (!"admin".equals(userRole)) {
                    resp.sendRedirect(req.getContextPath() + "/product");
                    return;
                }
                if ("createProduct".equals(action)) createProduct(req, resp);
                else if ("updateProduct".equals(action)) updateProduct(req, resp);
                else if ("deleteProduct".equals(action)) deleteProduct(req, resp);
                break;
            case "add-review":
                addReview(req, resp);
                break;
            case "delete-review":
                deleteReview(req, resp);
                break;
//            case "edit-review":
//                updateReview(req, resp);
//                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/product?action=listProducts");
                break;
        }
    }

    private void updateReview(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int reviewId = Integer.parseInt(req.getParameter("reviewID"));
            String newComment = req.getParameter("newComment");

            System.out.println("DEBUG: Update reviewID = " + reviewId + ", New Comment = " + newComment);

            if (newComment == null || newComment.trim().isEmpty()) {
                System.out.println("Error: newComment is empty!");
                req.setAttribute("errorMessage", "Nội dung bình luận không được để trống.");
                req.getRequestDispatcher("error.jsp").forward(req, resp);
                return;
            }

            boolean updated = productService.updateReview(reviewId, newComment);
            if (updated) {
                System.out.println("DEBUG: Review updated successfully!");

                // Kiểm tra dữ liệu có thực sự cập nhật chưa
                ProductReview updatedReview = productService.getReviewById(reviewId);
                System.out.println("DEBUG: New Comment from DB = " + updatedReview.getComment());

                resp.sendRedirect("product-detail?productID=" + req.getParameter("productID"));
            } else {
                System.out.println("Error: Failed to update review in database.");
                req.setAttribute("errorMessage", "Cập nhật đánh giá không thành công.");
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid reviewID format.");
            req.setAttribute("errorMessage", "ID đánh giá không hợp lệ.");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Lỗi hệ thống.");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private void deleteReview(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        String userRole = (String) req.getSession().getAttribute("userRole");

        if (userId == null) {
            resp.sendRedirect("loginPage.jsp");
            return;
        }

        try {
            // Kiểm tra request nhận được gì
            System.out.println("Raw reviewID: " + req.getParameter("reviewID"));
            System.out.println("Raw productID: " + req.getParameter("productID"));

            String reviewIdParam = req.getParameter("reviewID");
            String productIdParam = req.getParameter("productID");

            // Kiểm tra xem reviewID có bị null hoặc rỗng không
            if (reviewIdParam == null || reviewIdParam.isEmpty() || productIdParam == null || productIdParam.isEmpty()) {
                System.out.println("Error: reviewID or productID is missing.");
                req.setAttribute("errorMessage", "ID đánh giá hoặc sản phẩm không hợp lệ.");
                req.getRequestDispatcher("error.jsp").forward(req, resp);
                return;
            }

            int reviewId = Integer.parseInt(reviewIdParam);
            int productId = Integer.parseInt(productIdParam);

            // Kiểm tra reviewID có hợp lệ không
            if (reviewId <= 0 || productId <= 0) {
                System.out.println("Invalid reviewID or productID: " + reviewId + ", " + productId);
                req.setAttribute("errorMessage", "ID đánh giá hoặc sản phẩm không hợp lệ.");
                req.getRequestDispatcher("error.jsp").forward(req, resp);
                return;
            }

            System.out.println("Attempting to delete reviewID: " + reviewId + " for productID: " + productId);

            // Kiểm tra review có tồn tại không
            ProductReview review = productService.getReviewById(reviewId);
            if (review == null) {
                System.out.println("Review not found in database.");
                req.setAttribute("errorMessage", "Không tìm thấy đánh giá để xóa.");
                req.getRequestDispatcher("error.jsp").forward(req, resp);
                return;
            }

            // Kiểm tra quyền xóa (chỉ admin hoặc chủ bài đánh giá mới có thể xóa)
            if (userId.equals(review.getCustomerID()) || "admin".equals(userRole)) {
                boolean isDeleted = productService.deleteReview(reviewId);

                if (isDeleted) {
                    System.out.println("Review deleted successfully!");
                    resp.sendRedirect("product-detail?productID=" + productId);
                } else {
                    System.out.println("Failed to delete review from database.");
                    req.setAttribute("errorMessage", "Không thể xóa đánh giá. Hãy thử lại!");
                    req.getRequestDispatcher("error.jsp").forward(req, resp);
                }
            } else {
                System.out.println("Unauthorized attempt to delete review.");
                req.setAttribute("errorMessage", "Bạn không có quyền xóa đánh giá này!");
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid reviewID format: " + e.getMessage());
            req.setAttribute("errorMessage", "Dữ liệu không hợp lệ. Định dạng số bị sai.");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private void addReview(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        String userRole = (String) req.getSession().getAttribute("userRole");

        if (userId == null || !"customer".equals(userRole)) {
            resp.sendRedirect("loginPage.jsp");
            return;
        }

        try {
            int productId = Integer.parseInt(req.getParameter("productID"));
            int rating = Integer.parseInt(req.getParameter("rating"));
            String comment = req.getParameter("comment");

            // Debug xem comment nhận vào có bị lỗi không
            System.out.println("Comment Received: " + comment);

            ProductReview newReview = new ProductReview(userId, productId, rating, comment, new Timestamp(System.currentTimeMillis()));

            boolean isSuccess = productService.addReview(newReview);
            if (isSuccess) {
                resp.sendRedirect("product-detail?productID=" + productId);
            } else {
                resp.sendRedirect("error.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp");
        }
    }


    private void createProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int brandId = Integer.parseInt(req.getParameter("brandId"));
            String name = req.getParameter("name");
            double price = Double.parseDouble(req.getParameter("price"));  // Sử dụng BigDecimal
            int stock = Integer.parseInt(req.getParameter("stock"));
            String description = req.getParameter("description");
            String imageUrl = req.getParameter("imageUrl");

            Product newProduct = new Product(0, brandId, name, price, stock, description, imageUrl);
            productService.insertProduct(newProduct);

            req.getSession().setAttribute("successMessage", "Sản phẩm đã được thêm thành công!");
            resp.sendRedirect(req.getContextPath() + "/products?action=listProducts");
        } catch (Exception e) {
            req.getSession().setAttribute("errorMessage", "Lỗi khi thêm sản phẩm!");
            resp.sendRedirect(req.getContextPath() + "/products?action=createProduct");
        }
    }

    private void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(req.getParameter("productId"));
            int brandId = Integer.parseInt(req.getParameter("brandId"));
            String name = req.getParameter("name");
            double price = Double.parseDouble(req.getParameter("price"));
            int stock = Integer.parseInt(req.getParameter("stock"));
            String description = req.getParameter("description");
            String imageUrl = req.getParameter("imageUrl");
//            ErrorDialog.showError(""+  req.getParameter("productStatus"), "Product");
            String productStatus = req.getParameter("productStatus"); // Lấy trạng thái từ form

            // 🔥 Debug: Kiểm tra giá trị productStatus từ form
            System.out.println("DEBUG - Trạng thái sản phẩm từ form: " + productStatus);

            Optional<Product> existingProduct = productService.findProductById(productId);
            if (existingProduct.isEmpty()) {
                req.getSession().setAttribute("errorMessage", "Sản phẩm không tồn tại!");
                resp.sendRedirect(req.getContextPath() + "/products?action=listProducts");
                return;
            }

            Product updatedProduct = existingProduct.get();
            updatedProduct.setBrandID(brandId);
            updatedProduct.setName(name);
            updatedProduct.setPrice(price);
            updatedProduct.setStock(stock);
            updatedProduct.setDescription(description);
            updatedProduct.setImageURL(imageUrl);
            updatedProduct.setProductStatus(productStatus); // Cập nhật trạng thái sản phẩm

            //  Debug: Kiểm tra giá trị trước khi cập nhật vào DB
            System.out.println("DEBUG - Trạng thái sản phẩm trước khi cập nhật DB: " + updatedProduct.getProductStatus());

            productService.updateProduct(updatedProduct);

            req.getSession().setAttribute("successMessage", "Sản phẩm đã được cập nhật thành công!");
            resp.sendRedirect(req.getContextPath() + "/product?action=listProducts");
        } catch (Exception e) {
            e.printStackTrace(); // Debug lỗi nếu có
            req.getSession().setAttribute("errorMessage", "Lỗi khi cập nhật sản phẩm!");
            resp.sendRedirect(req.getContextPath() + "/product?action=updateProduct&productId=" + req.getParameter("productId"));
        }
    }
}
