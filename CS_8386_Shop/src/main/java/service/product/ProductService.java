package service.product;

import dao.productDao.IProductDAO;
import dao.productDao.ProductDAO;
import model.Product;
import model.ProductReview;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductService implements IProductService {
    private final IProductDAO productDao = new ProductDAO();

    ProductDAO productDAO;
    public ProductService() throws SQLException {
        this.productDAO = new ProductDAO();
        if (this.productDAO == null) {
            throw new RuntimeException(" Lỗi: ProductDAO chưa được khởi tạo!");
        }
    }



    @Override
    public List<Product> getAllForAdmin() {
        List<Product> products = productDAO.selectAllProductsForAdmin();
        System.out.println("ProductService - Số sản phẩm gửi đến Servlet: " + products.size());
        return products;
    }

    @Override
    public Optional<Product> findProductById(int productId) {
        return productDAO.findProductById(productId);
    }

    @Override
    public Optional<Product> findProductByName(String productName) {
        return productDAO.findProductByName(productName);
    }

    @Override
    public Optional<Product> findProductByBrand(String brand) {
        return productDAO.findProductByBrand(brand);
    }

    @Override
    public Optional<Product> findProductByPrice(double price) {
        return productDAO.findProductByPrice(price);
    }

    @Override
    public void insertProduct(Product product) {
        productDAO.insertProduct(product);
    }

    @Override
    public void updateProduct(Product product) {
        productDAO.updateProduct(product);
    }

    @Override
    public void deleteProduct(int id) {
        productDAO.deleteProduct(id);
    }

    public List<Product> getAllForUser() {
        List<Product> products = productDAO.selectAllProductsForUser();
        System.out.println("ProductService - Số sản phẩm gửi đến Servlet: " + products.size());
        return products;
    }

    @Override
    public int getTotalProducts() {
        return productDAO.getTotalProducts();
    }

    @Override
    public List<Product> getProductsByPage(int pageNumber, int pageSize) {
        return productDAO.getProductsByPage(pageNumber, pageSize);
    }

    @Override
    public Product getProductById(int productID) {
        return productDAO.getProductById(productID);
    }

    @Override
    public List<ProductReview> getReviewsByProductId(int productId) {
        return productDAO.getReviewsByProductId(productId);
    }

    @Override
    public ProductReview getReviewById(int reviewID) {
        return productDAO.getReviewById(reviewID);
    }

    @Override
    public boolean addReview(ProductReview review) {
        return productDAO.addReview(review);
    }

    @Override
    public boolean deleteReview(int reviewID) {
        return productDAO.deleteReview(reviewID);
    }
    @Override
    public List<Product> searchProductsByName(String keyword) {
        return productDAO.searchProductsByName(keyword);
    }

    @Override
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice, int pageNumber, int pageSize) {
        return productDAO.getProductsByPriceRange(minPrice,maxPrice,pageNumber,pageSize);
    }

    @Override
    public int getTotalProductsByPriceRange(double minPrice, double maxPrice) {
        return productDAO.getTotalProductsByPriceRange(minPrice,maxPrice);
    }

    @Override
    public int countProducts() {
        return productDAO.countProducts();
    }

    @Override
    public List<Object[]> getStockByBrand() {
        return productDAO.getStockByBrand();
    }

    @Override
    public boolean updateReview(int reviewID, String newComment) {
        return productDAO.updateReview(reviewID,newComment);

    }

    @Override
    public Product findById(int productID) {
        return null;
    }

    @Override
    public void update(Product product) {

    }

    @Override
    public List<Object[]> getRevenueByBrand() {
        return productDAO.getRevenueByBrand();
    }

}
