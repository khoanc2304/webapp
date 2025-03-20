package service.cart;

import model.Cart;
import model.Product;

public interface ICartService {
    void addProduct(Product product);

    void increaseQuantity(int productID);

    void decreaseQuantity(int productID);

    void removeProduct(int productID);

    void updateQuantity(int productID, int cartQuantity);

    double getTotalPrice();

    void clear();
    Cart getCart();
}
