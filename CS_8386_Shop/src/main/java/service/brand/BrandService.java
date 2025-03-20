package service.brand;

import dao.brandDao.BrandDAO;
import model.Brand;

import java.sql.SQLException;
import java.util.List;

public class BrandService implements IBrandService {
    private final BrandDAO brandDAO = new BrandDAO();

    @Override
    public void addBrand(Brand brand) throws SQLException {
        brandDAO.addBrand(brand);
    }

    @Override
    public List<Brand> getAllBrands() throws SQLException {
        return brandDAO.getAllBrands();
    }

    @Override
    public Brand getBrandById(int brandID) throws SQLException {
        return brandDAO.getBrandById(brandID);
    }

    @Override
    public void updateBrand(Brand brand) throws SQLException {
        brandDAO.updateBrand(brand);
    }

    @Override
    public void deleteBrand(int brandID) throws SQLException {
        brandDAO.deleteBrand(brandID);
    }
}
