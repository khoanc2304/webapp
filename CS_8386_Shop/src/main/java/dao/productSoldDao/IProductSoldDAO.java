package dao.productSoldDao;

import model.ProductSold;

import java.util.List;

public interface IProductSoldDAO {
    void create(ProductSold productSold);
    ProductSold findById(int id);
    List<ProductSold> findByProductId(int productId);
    List<ProductSold> findByUserId(int userId);
    List<ProductSold> findAll();
    void update(ProductSold productSold);
    void delete(int id);
}
