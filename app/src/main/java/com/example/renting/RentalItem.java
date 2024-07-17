package com.example.renting;

import com.google.gson.annotations.SerializedName;

public class RentalItem {
    @SerializedName("id")
    private String id;

    @SerializedName("rentalId")
    private String rentalId;

    @SerializedName("droneId")
    private String droneId;

    @SerializedName("daysNumber")
    private int daysNumber;
    @SerializedName("pricePerDay")
    private Double pricePerDay;

    @SerializedName("price")
    private Double price;
    @SerializedName("lastModificationTime")
    private String lastModificationTime;

    @SerializedName("lastModifierId")
    private String lastModifierId;

    @SerializedName("creationTime")
    private String creationTime;

    @SerializedName("creatorId")
    private String creatorId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = rentalId;
    }

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

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(String lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

    public String getLastModifierId() {
        return lastModifierId;
    }

    public void setLastModifierId(String lastModifierId) {
        this.lastModifierId = lastModifierId;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}
