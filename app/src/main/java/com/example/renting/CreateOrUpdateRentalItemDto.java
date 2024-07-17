package com.example.renting;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class CreateOrUpdateRentalItemDto {

    @SerializedName("droneId")
    private String droneId;

    @SerializedName("pricePerDay")
    private double pricePerDay;

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
}
