package model;


public class OrderDetail {

    private int orderDetailID;
    private int orderID;
    private int productID;
    private int quantity;
    private double price;
    private String productName; // Thêm thuộc tính này

    public OrderDetail(int orderDetailID, int orderID, int productID, int quantity, double price) {
        this.orderDetailID = orderDetailID;
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderDetailID() { return orderDetailID; }
    public void setOrderDetailID(int orderDetailID) { this.orderDetailID = orderDetailID; }
    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }
    public int getProductID() { return productID; }
    public void setProductID(int productID) { this.productID = productID; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
