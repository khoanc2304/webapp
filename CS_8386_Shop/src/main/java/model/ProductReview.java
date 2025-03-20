package model;

import java.sql.Timestamp;

public class ProductReview {
    private int reviewID;
    private int customerID;
    private int productID;
    private int rating;
    private String comment;
    private Timestamp reviewDate;

    public ProductReview(int reviewID, int customerID, int productID, int rating, String comment, Timestamp reviewDate) {
        this.reviewID = reviewID;
        this.customerID = customerID;
        this.productID = productID;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }
    public ProductReview(int customerID, int productID, int rating, String comment, Timestamp reviewDate) {
        this.customerID = customerID;
        this.productID = productID;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public int getReviewID() { return reviewID; }
    public void setReviewID(int reviewID) { this.reviewID = reviewID; }
    public int getCustomerID() { return customerID; }
    public void setCustomerID(int customerID) { this.customerID = customerID; }
    public int getProductID() { return productID; }
    public void setProductID(int productID) { this.productID = productID; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Timestamp getReviewDate() { return reviewDate; }
    public void setReviewDate(Timestamp reviewDate) { this.reviewDate = reviewDate; }

    @Override
    public String toString() {
        return "ProductReview{" +
                "reviewID=" + reviewID +
                ", customerID=" + customerID +
                ", productID=" + productID +
                ", rating=" + rating +
                ", comment='" + comment + "'" +
                ", reviewDate=" + reviewDate +
                '}';
    }
}
