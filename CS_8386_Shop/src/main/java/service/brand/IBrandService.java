package service.brand;

import model.Brand;

import java.sql.SQLException;
import java.util.List;

public interface IBrandService {
    void addBrand(Brand brand) throws SQLException;
    List<Brand> getAllBrands() throws SQLException;
    Brand getBrandById(int brandID) throws SQLException;
    void updateBrand(Brand brand) throws SQLException;
    void deleteBrand(int brandID) throws SQLException;
}
