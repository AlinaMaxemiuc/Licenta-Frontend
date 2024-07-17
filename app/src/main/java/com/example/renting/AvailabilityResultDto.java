package com.example.renting;

import com.google.gson.annotations.SerializedName;

public class AvailabilityResultDto {
    @SerializedName("available")
    private boolean available;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
