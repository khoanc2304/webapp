package model;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    private int orderID;
    private int userID;
    private Timestamp orderDate;
    private double totalPrice;
    private String orderStatus;
    private List<OrderDetail> orderDetail;
    private String fullName;
    private String email;
    private String phone;   // Thêm thuộc tính phone
    private String address;

    public Order(int orderID, int userID, Timestamp orderDate, double totalPrice, String orderStatus) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", userID=" + userID +
                ", orderDate=" + orderDate +
                ", totalPrice=" + totalPrice +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }
    public void setOrderDetail(List<OrderDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public List<OrderDetail> getOrderDetail() {
        return orderDetail;
    }
}
