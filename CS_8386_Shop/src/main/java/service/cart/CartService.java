package service.cart;

import dao.cartDao.CartDAO;
import model.Cart;
import model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartService implements ICartService{
    private CartDAO cartDAO;
    private Cart cart;
    // Thêm getter để truy cập items
    public List<Product> getItems() {
        return cartDAO.getItems();
    }

    // Constructor để khởi tạo cartDAO
    public CartService() {
        List<Product> items = new ArrayList<>(); // Danh sách sản phẩm trong giỏ hàng
        this.cartDAO = new CartDAO(items);       // Khởi tạo cartDAO
        this.cart = new Cart();                  // Khởi tạo cart
        this.cart.setItems(items);
    }

    @Override
    public void addProduct(Product product) {
        cartDAO.addProduct(product);
    }

    @Override
    public void increaseQuantity(int productID) {
        cartDAO.increaseQuantity(productID);
    }

    @Override
    public void decreaseQuantity(int productID) {
        cartDAO.decreaseQuantity(productID);
    }

    @Override
    public void removeProduct(int productID) {
        cartDAO.removeProduct(productID);
    }

    @Override
    public void updateQuantity(int productID, int cartQuantity) {
        cartDAO.updateQuantity(productID,cartQuantity);
    }

    @Override
    public double getTotalPrice() {
        return cartDAO.getTotalPrice();
    }

    @Override
    public void clear() {
        cartDAO.clear();
    }

    @Override
    public Cart getCart() {
        return cart;
    }
}
