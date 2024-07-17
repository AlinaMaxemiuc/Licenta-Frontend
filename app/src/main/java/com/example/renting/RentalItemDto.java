package com.example.renting;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class RentalItemDto {

    @SerializedName("droneId")
    private String droneId;

    @SerializedName("daysNumber")
    private int daysNumber;

    @SerializedName("pricePerDay")
    private double pricePerDay;

    @SerializedName("price")
    private double price;


    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public int getDaysNumber() {
        return daysNumber;
    }

    public void setDaysNumber(int daysNumber) {
        this.daysNumber = daysNumber;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void calculateTotalPrice() {
        this.price = this.daysNumber * this.pricePerDay;
    }
}
