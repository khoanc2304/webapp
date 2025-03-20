package service.productSold;

import dao.productSoldDao.IProductSoldDAO;
import dao.productSoldDao.ProductSoldDAO;
import model.ProductSold;

import java.util.List;

public class ProductSoldService implements IProductSoldService {
    private IProductSoldDAO productSoldDAO = new ProductSoldDAO();
    @Override
    public void create(ProductSold productSold) {
        productSoldDAO.create(productSold);
    }

    @Override
    public ProductSold findById(int id) {
        return productSoldDAO.findById(id);
    }

    @Override
    public List<ProductSold> findByProductId(int productId) {
        return productSoldDAO.findByProductId(productId);
    }

    @Override
    public List<ProductSold> findByUserId(int userId) {
        return  productSoldDAO.findByUserId(userId);
    }

    @Override
    public List<ProductSold> findAll() {
        return productSoldDAO.findAll();
    }

    @Override
    public void update(ProductSold productSold) {
        productSoldDAO.update(productSold);
    }

    @Override
    public void delete(int id) {
        productSoldDAO.delete(id);
    }
}
