package com.example.renting;

public class LoginRequest {
    private String userNameOrEmailAddress;
    private String password;

    public LoginRequest(String userNameOrEmailAddress, String password) {
        this.userNameOrEmailAddress = userNameOrEmailAddress;
        this.password = password;
    }

    public String getUserNameOrEmailAddress() {
        return userNameOrEmailAddress;
    }

    public void setUserNameOrEmailAddress(String userNameOrEmailAddress) {
        this.userNameOrEmailAddress = userNameOrEmailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
