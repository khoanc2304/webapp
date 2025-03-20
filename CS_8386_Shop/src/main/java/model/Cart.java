package model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> items;

    // Constructor
    public Cart() {
        this.items = new ArrayList<>();
    }

    // Getter và Setter
    public List<Product> getItems() {
        return items;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }

    // Tính tổng tiền
    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(product -> product.getPrice() * product.getCartQuantity())
                .sum();
    }
}
