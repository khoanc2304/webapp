package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User {
    //
    private int userID;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private LocalDateTime userCreatedDate;
    private LocalDateTime userUpdatedDate ;
    private String userStatus;
    private String userRole;


    public User(int userID, String username, String password, String fullName, String email, String phone, String address, String avatar, String userStatus, String userRole) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
        this.userStatus = userStatus;
        this.userRole = userRole;
    }

    public User(int userID, String username, String password, String fullName, String email, String phone, String address, String avatar, LocalDateTime userCreatedDate, LocalDateTime userUpdatedDate, String userStatus, String userRole) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
        this.userCreatedDate = userCreatedDate;
        this.userUpdatedDate = userUpdatedDate;
        this.userStatus = userStatus;
        this.userRole = userRole;
    }

    public LocalDateTime getUserCreatedDate() {
        return userCreatedDate;
    }

    public void setUserCreatedDate(LocalDateTime userCreatedDate) {
        this.userCreatedDate = userCreatedDate;
    }

    public LocalDateTime getUserUpdatedDate() {
        return userUpdatedDate;
    }

    public void setUserUpdatedDate(LocalDateTime userUpdatedDate) {
        this.userUpdatedDate = userUpdatedDate;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public User() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getFormattedCreatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return userCreatedDate.format(formatter);
    }

    public String getFormattedUpdateDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return userUpdatedDate.format(formatter);
    }

}