package com.example.renting;

import com.google.gson.annotations.SerializedName;

public class AvailabilityResponse {
    @SerializedName("result")
    private boolean available;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

