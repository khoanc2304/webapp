package dao.productDao;

import dao.DBConnection;
import model.Product;
import model.ProductReview;
import service.product.ProductService;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO implements IProductDAO  {
    private final Connection connection;

    public ProductDAO() throws SQLException {
        DBConnection db = new DBConnection();
        this.connection = db.getConnection();
    }

    private static final String INSERT_PRODUCT = "INSERT INTO Product (BrandID, Name, Price, Stock, Description, ImageURL, status) VALUES (?, ?, ?, ?, ?, ?, 1)";
    private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM Product WHERE productStatus = 'available'";
    //    private static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM Product WHERE ProductID = ? AND status = 1";
    private static final String SELECT_PRODUCT_BY_ID = "SELECT p.ProductID, p.Name, p.Price, p.Stock, p.Description, p.ImageURL, p.ProductCreatedDate, p.ProductUpdatedDate, b.BrandName " +
            "FROM Product p " +
            "JOIN Brand b ON p.BrandID = b.BrandID " +
            "WHERE p.ProductID = ?";
    private static final String UPDATE_REVIEW = "UPDATE ProductReview SET Comment = ? WHERE ReviewID = ?";


    private static final String SELECT_PRODUCT_BY_NAME = "SELECT * FROM Product WHERE Name = ? AND status = 1";
    private static final String SELECT_PRODUCT_BY_BRAND = "SELECT * FROM Product p JOIN Brand b ON p.BrandID = b.BrandID WHERE b.BrandName = ? AND p.status = 1";
    private static final String SELECT_PRODUCT_BY_PRICE = "SELECT * FROM Product WHERE Price = ? AND status = 1";
    private static final String UPDATE_PRODUCT = "UPDATE Product SET BrandID = ?, Name = ?, Price = ?, Stock = ?, Description = ?, ImageURL = ?, productStatus = ?, ProductUpdatedDate = CURRENT_TIMESTAMP WHERE ProductID = ?";
    private static final String DELETE_PRODUCT = "UPDATE Product SET productStatus = 'unavailable' WHERE ProductID = ?";
    private static final String FIND_PRODUCT_BY_ID = "SELECT p.ProductID, p.BrandID, p.Name, p.Price, p.Stock, p.Description, p.ImageURL, " +
            "p.ProductCreatedDate, p.ProductUpdatedDate, b.BrandName, p.productStatus " +
            "FROM Product p " +
            "JOIN Brand b ON p.BrandID = b.BrandID " +
            "WHERE p.ProductID = ?";
    private static final String FIND_PRODUCT_BY_NAME = "SELECT * FROM Product WHERE Name = ? AND status = 1";
    private static final String FIND_PRODUCT_BY_BRAND = "SELECT * FROM Product p JOIN Brand b ON p.BrandID = b.BrandID WHERE b.BrandName = ? AND p.status = 1";
    private static final String FIND_PRODUCT_BY_PRICE = "SELECT * FROM Product WHERE Price = ? AND status = 1";
    //private static final String SELECT_ALL_PRODUCT_FOR_USER = "SELECT * FROM Product where productStatus = 'available'";
    //private static final String GET_TOTAL_PRICE = "SELECT COUNT(*) FROM Product";
    private static final String COUNT_QUERY = "SELECT COUNT(*) FROM Product";
    private static final String SELECT_STOCK_BY_BRAND = "SELECT b.BrandName, SUM(p.Stock) AS total_stock " +
            "FROM Product p " +
            "JOIN Brand b ON p.BrandID = b.BrandID " +
            "GROUP BY b.BrandID, b.BrandName";
    private static final String SELECT_PRICE_AVG_BY_BRAND = "SELECT b.BrandName, AVG(p.Price) AS AvgPrice\n" +
            "FROM Product p" +
            "JOIN Brand b ON p.BrandID = b.BrandID" +
            "GROUP BY b.BrandID, b.BrandName;";
    private static final String SELECT_PRICE_BY_BRAND = "SELECT b.BrandName, SUM(od.Price * od.Quantity) AS TotalRevenue " +
            "FROM Orders o " +
            "JOIN OrderDetail od ON o.OrderID = od.OrderID " +
            "JOIN Product p ON od.ProductID = p.ProductID " +
            "JOIN Brand b ON p.BrandID = b.BrandID " +
            "WHERE o.orderStatus = 'Completed' " +
            "GROUP BY b.BrandID, b.BrandName";

    @Override
    public List<Product> selectAllProductsForAdmin() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Product";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("ProductID"),
                        rs.getInt("BrandID"),
                        rs.getString("Name"),
                        rs.getString("productStatus"),
                        rs.getDouble("Price"),
                        rs.getInt("Stock"),
                        rs.getString("Description"),
                        rs.getString("ImageURL")

                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public Optional<Product> findProductById(int id) {
        try (PreparedStatement ps = connection.prepareStatement(FIND_PRODUCT_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching product by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> findProductByName(String name) {
        try (PreparedStatement ps = connection.prepareStatement(FIND_PRODUCT_BY_NAME)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> findProductByBrand(String brand) {
        try (PreparedStatement ps = connection.prepareStatement(FIND_PRODUCT_BY_BRAND)) {
            ps.setString(1, brand);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> findProductByPrice(double price) {
        try (PreparedStatement ps = connection.prepareStatement(FIND_PRODUCT_BY_PRICE)) {
            ps.setBigDecimal(1, BigDecimal.valueOf(price));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void insertProduct(Product product) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, product.getBrandID());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getPrice());  // Lưu giá trị BigDecimal vào CSDL
            ps.setInt(4, product.getStock());
            ps.setString(5, product.getDescription());
            ps.setString(6, product.getImageURL());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) {
//        ErrorDialog.showError("" + product.getProductStatus(), "Test");
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_PRODUCT)) {
            ps.setInt(1, product.getBrandID());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getPrice());  // Cập nhật giá trị BigDecimal vào CSDL
            ps.setInt(4, product.getStock());
            ps.setString(5, product.getDescription());
            ps.setString(6, product.getImageURL());
            ps.setString(7, product.getProductStatus());
            ps.setInt(8, product.getProductID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(int id) {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_PRODUCT)) {
            ps.setInt(1, id);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("Không tìm thấy sản phẩm để xóa!");
            } else {
                System.out.println("Đã cập nhật trạng thái sản phẩm thành 'unavailable'.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product mapProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("ProductID"),
                rs.getInt("BrandID"),
                rs.getString("Name"),
                rs.getDouble("Price"),
                rs.getInt("Stock"),
                rs.getString("Description"),
                rs.getString("ImageURL"),
                rs.getTimestamp("ProductCreatedDate") != null ? rs.getTimestamp("ProductCreatedDate").toLocalDateTime() : LocalDateTime.now(),
                rs.getTimestamp("ProductUpdatedDate") != null ? rs.getTimestamp("ProductUpdatedDate").toLocalDateTime() : LocalDateTime.now(),
                rs.getString("BrandName"),
                rs.getString("productStatus")
        );
    }

    @Override
    public List<Product> selectAllProductsForUser() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Product where productStatus = 'available'";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("ProductID"),
                        rs.getInt("BrandID"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Stock"),
                        rs.getString("Description"),
                        rs.getString("ImageURL")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
        }

        System.out.println("Số sản phẩm lấy được từ DB: " + products.size());
        return products;
    }

    @Override
    public int getTotalProducts() {
        int totalProducts = 0;
        String query = "SELECT COUNT(*) FROM Product";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                totalProducts = rs.getInt(1); // Lấy giá trị đếm từ cột đầu tiên
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi lấy tổng số sản phẩm: " + e.getMessage());
        }

        System.out.println("Tổng số sản phẩm trong DB: " + totalProducts);
        return totalProducts;
    }

    @Override
    public List<Product> getProductsByPage(int pageNumber, int pageSize) {
        List<Product> products = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize;
        String query = "SELECT * FROM Product WHERE productStatus = 'available' " +
                "ORDER BY ProductID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";  // Cập nhật câu lệnh SQL

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, offset); // Số dòng cần bỏ qua (OFFSET)
            ps.setInt(2, pageSize); // Số sản phẩm trên mỗi trang (FETCH NEXT)

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("ProductID"),
                            rs.getInt("BrandID"),
                            rs.getString("Name"),
                            rs.getDouble("Price"),
                            rs.getInt("Stock"),
                            rs.getString("Description"),
                            rs.getString("ImageURL")
                    );
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi lấy sản phẩm theo trang: " + e.getMessage());
        }

        System.out.println("Số sản phẩm lấy được từ DB cho trang " + pageNumber + ": " + products.size());
        return products;
    }

    public Product getProductById(int productID) {
        Product product = null;
        String query = "SELECT * FROM Product WHERE ProductID = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, productID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = new Product(
                            rs.getInt("ProductID"),
                            rs.getInt("BrandID"),
                            rs.getString("Name"),
                            rs.getDouble("Price"),
                            rs.getInt("Stock"),
                            rs.getString("Description"),
                            rs.getString("ImageURL")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi lấy thông tin sản phẩm: " + e.getMessage());
        }

        return product;
    }

    @Override
    public List<ProductReview> getReviewsByProductId(int productId) {
        List<ProductReview> reviews = new ArrayList<>();
        String query = "SELECT ReviewID, userID, ProductID, Rating, Comment, ReviewDate " +
                "FROM ProductReview WHERE ProductID = ? ORDER BY ReviewDate DESC";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductReview review = new ProductReview(
                        rs.getInt("ReviewID"),
                        rs.getInt("userID"),
                        rs.getInt("ProductID"),
                        rs.getInt("Rating"),
                        rs.getString("Comment"),
                        rs.getTimestamp("ReviewDate")
                );
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
    public ProductReview getReviewById(int reviewID) {
        String query = "SELECT * FROM ProductReview WHERE ReviewID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, reviewID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new ProductReview(
                        rs.getInt("ReviewID"),
                        rs.getInt("userID"),
                        rs.getInt("ProductID"),
                        rs.getInt("Rating"),
                        rs.getString("Comment"),
                        rs.getTimestamp("ReviewDate")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addReview(ProductReview review) {
        String query = "INSERT INTO ProductReview (userID, ProductID, Rating, Comment, ReviewDate) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, review.getCustomerID());
            ps.setInt(2, review.getProductID());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            ps.setTimestamp(5, review.getReviewDate());

            int rowsInserted = ps.executeUpdate();
            System.out.println("Số dòng đã chèn vào DB: " + rowsInserted);
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi thêm đánh giá vào DB: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteReview(int reviewID) {
        String query = "DELETE FROM ProductReview WHERE ReviewID = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, reviewID);
            return ps.executeUpdate() > 0; // Trả về true nếu delete thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Product> searchProductsByName(String keyword) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Product WHERE Name LIKE ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("ProductID"),
                        rs.getInt("BrandID"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Stock"),
                        rs.getString("Description"),
                        rs.getString("ImageURL")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice, int pageNumber, int pageSize) {
        List<Product> products = new ArrayList<>();
        int offset = (pageNumber - 1) * pageSize;
        String query = "SELECT * FROM Product WHERE Price BETWEEN ? AND ? LIMIT ? OFFSET ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);
            ps.setInt(3, pageSize);
            ps.setInt(4, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("ProductID"),
                            rs.getInt("BrandID"),
                            rs.getString("Name"),
                            rs.getDouble("Price"),
                            rs.getInt("Stock"),
                            rs.getString("Description"),
                            rs.getString("ImageURL")
                    );
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }
    public int getTotalProductsByPriceRange(double minPrice, double maxPrice) {
        int totalProducts = 0;
        String query = "SELECT COUNT(*) FROM Product WHERE Price BETWEEN ? AND ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalProducts = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalProducts;
    }

    @Override
    public int countProducts()  {
        int count = 0;
        DBConnection dbConnection = new DBConnection();
        Connection connection = null;
        try {
            connection = dbConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(COUNT_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    @Override
    public List<Object[]> getStockByBrand() {
        List<Object[]> stockByBrandList = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_STOCK_BY_BRAND);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] stockData = new Object[2];
                stockData[0] = rs.getString("BrandName");
                stockData[1] = rs.getInt("total_stock");
                stockByBrandList.add(stockData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching stock by brand", e);
        }
        return stockByBrandList;
    }

//    @Override
//    public List<Object[]> averagePriceByBrand() {
//        List<Object[]> avgPriceBrandList = new ArrayList<>();
//        try {
//            PreparedStatement ps = connection.prepareStatement(SELECT_PRICE_AVG_BY_BRAND);
//            ResultSet resultSet = ps.executeQuery();
//            while (resultSet.next()) {
//                Object[] stockData = new Object[2];
//                stockData[0] = re
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return List.of();
//    }

    @Override
    public boolean updateReview(int reviewID, String newComment) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_REVIEW)) {
            ps.setString(1, newComment);
            ps.setInt(2, reviewID);

            int rowsAffected = ps.executeUpdate();
            System.out.println("DEBUG: Rows affected in updateReview = " + rowsAffected);

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error while updating review: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Object[]> getRevenueByBrand() {
        List<Object[]> revenueByBrandList = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_PRICE_BY_BRAND);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] revenueData = new Object[2];
                revenueData[0] = rs.getString("BrandName"); // Tên thương hiệu
                revenueData[1] = rs.getDouble("TotalRevenue"); // Tổng doanh thu
                revenueByBrandList.add(revenueData);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return revenueByBrandList;
    }


}
