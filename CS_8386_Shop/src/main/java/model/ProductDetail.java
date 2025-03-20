package model;

public class ProductDetail {
    private int detailID;
    private int productID;
    private String attributeName;
    private String attributeValue;

    public ProductDetail(int detailID, int productID, String attributeName, String attributeValue) {
        this.detailID = detailID;
        this.productID = productID;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public int getDetailID() { return detailID; }
    public void setDetailID(int detailID) { this.detailID = detailID; }
    public int getProductID() { return productID; }
    public void setProductID(int productID) { this.productID = productID; }
    public String getAttributeName() { return attributeName; }
    public void setAttributeName(String attributeName) { this.attributeName = attributeName; }
    public String getAttributeValue() { return attributeValue; }
    public void setAttributeValue(String attributeValue) { this.attributeValue = attributeValue; }

    @Override
    public String toString() {
        return "ProductDetail{" +
                "detailID=" + detailID +
                ", productID=" + productID +
                ", attributeName='" + attributeName + "'" +
                ", attributeValue='" + attributeValue + "'" +
                '}';
    }
}
