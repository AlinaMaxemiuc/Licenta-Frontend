package com.example.renting;

public class PasswordCheckResponse {
    private int result;
    private String description;

    public boolean isValid() {
        return result == 1;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
