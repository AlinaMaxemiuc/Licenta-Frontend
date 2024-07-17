package com.example.renting;

public class RegisterRequest {
    private String userName;
    private String emailAddress;
    private String password;
    private String appName;
    private String phoneNumber;

    public RegisterRequest(String userName, String emailAddress, String password, String appName, String phoneNumber) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.appName = appName;
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
