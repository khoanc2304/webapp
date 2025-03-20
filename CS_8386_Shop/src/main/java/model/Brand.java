package model;

public class Brand {
    private int brandID;
    private String brandName;
    private String country;
    private String imageURL;

    // Constructor mặc định
    public Brand() {
    }

    // Constructor đầy đủ
    public Brand(int brandID, String brandName, String country, String imageURL) {
        this.brandID = brandID;
        this.brandName = brandName;
        this.country = country;
        this.imageURL = imageURL;
    }

    // Getter và Setter
    public int getBrandID() {
        return brandID;
    }

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "brandID=" + brandID +
                ", brandName='" + brandName + '\'' +
                ", country='" + country + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}