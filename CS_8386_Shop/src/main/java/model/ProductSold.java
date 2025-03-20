package model;

import java.sql.Timestamp;

public class ProductSold {
    private int id;
    private int productId;
    private int quantity;
    private Timestamp dateSold;
    private int userId;

    public ProductSold() {
    }

    public ProductSold(int id, int productId, int quantity, Timestamp dateSold, int userId) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.dateSold = dateSold;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getDateSold() {
        return dateSold;
    }

    public void setDateSold(Timestamp dateSold) {
        this.dateSold = dateSold;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ProductSold{" +
                "id=" + id +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", dateSold=" + dateSold +
                ", userId=" + userId +
                '}';
    }
}
