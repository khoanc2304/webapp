package dao.cartDao;

import model.Product;

import java.util.List;

public class CartDAO {
    private final List<Product> items ;

    public CartDAO(List<Product> items) {
        this.items = items;
    }
    public List<Product> getItems() {
        return items;
    }


    public void addProduct(Product product) {
        // Log trước khi thêm
        System.out.println("CartDAO - Before adding product - Items: " + items);
        System.out.println("CartDAO - Adding product: " + product.getProductID() + ", Quantity: " + product.getCartQuantity());

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        for (Product p : items) {
            if (p.getProductID() == product.getProductID()) {
                p.setCartQuantity(p.getCartQuantity() + product.getCartQuantity());
                System.out.println("CartDAO - Product already exists, updated quantity: " + p.getCartQuantity());
                return;
            }
        }
        // Nếu sản phẩm chưa có, thêm mới vào giỏ hàng
        items.add(product);
        System.out.println("CartDAO - Product added as new: " + product.getProductID());
    }

    // Tăng số lượng sản phẩm trong giỏ hàng
    public void increaseQuantity(int productID) {
        for (Product p : items) {
            if (p.getProductID() == productID) {
                p.setCartQuantity(p.getCartQuantity() + 1);
                break;
            }
        }
    }

    // Giảm số lượng sản phẩm trong giỏ hàng
    public void decreaseQuantity(int productID) {
        for (Product p : items) {
            if (p.getProductID() == productID) {
                if (p.getCartQuantity() > 1) {
                    p.setCartQuantity(p.getCartQuantity() - 1);
                } else {
                    // Nếu số lượng về 0, xóa sản phẩm khỏi giỏ hàng
                    items.remove(p);
                }
                break;
            }
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeProduct(int productID) {
        items.removeIf(product -> product.getProductID() == productID);
    }

    // Cập nhật số lượng sản phẩm
    public void updateQuantity(int productID, int cartQuantity) {
        for (Product p : items) {
            if (p.getProductID() == productID) {
                p.setCartQuantity(cartQuantity); // Không kiểm tra stock
                break;
            }
        }
    }

    // Tính tổng tiền
    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(product -> product.getPrice() * product.getCartQuantity())
                .sum();
    }

    // Xóa toàn bộ giỏ hàng
    public void clear() {
        items.clear();
    }
}
