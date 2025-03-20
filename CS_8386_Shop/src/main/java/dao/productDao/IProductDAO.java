package dao.productDao;

import model.Product;
import model.ProductReview;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IProductDAO <t> {
    //get all i4 product
    List<Product> selectAllProductsForAdmin();
    // search
    Optional<Product> findProductById(int id);
    Optional<Product> findProductByName(String name);
    Optional<Product> findProductByBrand(String brand);
    Optional<Product> findProductByPrice(double price);
    //create
    void insertProduct(Product product);
    //update
    void updateProduct(Product product);
    //delete
    void deleteProduct(int id);
    // map RS -> Product
    Product mapProduct(ResultSet rs) throws SQLException;

    List<Product> selectAllProductsForUser();
    int getTotalProducts();
    List<Product> getProductsByPage(int pageNumber, int pageSize);
    Product getProductById(int productID);
    List<ProductReview> getReviewsByProductId(int productId);
    ProductReview getReviewById(int reviewID);
    boolean addReview(ProductReview review);
    boolean deleteReview(int reviewID);
    List<Product> searchProductsByName(String keyword);
    List<Product> getProductsByPriceRange(double minPrice, double maxPrice, int pageNumber, int pageSize);
    int getTotalProductsByPriceRange(double minPrice, double maxPrice);



    int countProducts();
    List<Object[]> getStockByBrand() ;
    //List<Object[]> averagePriceByBrand();
    boolean updateReview(int reviewID, String newComment);
    List<Object[]> getRevenueByBrand();
}
