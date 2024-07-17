package com.example.renting;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class RentalData {
    @SerializedName("id")
    private String id;

    @SerializedName("customerId")
    private String customerId;

    @SerializedName("startDay")
    private String startDay;

    @SerializedName("endDay")
    private String endDay;

    @SerializedName("total")
    private BigDecimal total;

    @SerializedName("status")
    private int status;

    @SerializedName("paymentMethod")
    private int paymentMethod;

    @SerializedName("rentalItems")
    private List<RentalItem> rentalItems;

    @SerializedName("lastModificationTime")
    private String lastModificationTime;

    @SerializedName("lastModifierId")
    private String lastModifierId;

    @SerializedName("creationTime")
    private String creationTime;

    @SerializedName("creatorId")
    private String creatorId;

    public String getDroneDetails() {
        if (rentalItems != null && !rentalItems.isEmpty()) {
            RentalItem item = rentalItems.get(0);
            return "Drone ID: " + item.getDroneId() + "<br>" +
                    "Price Per Day: " + item.getPricePerDay() + "<br>" +
                    "Days: " + item.getDaysNumber();
        }
        return "No drone details available";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<RentalItem> getRentalItems() {
        return rentalItems;
    }

    public void setRentalItems(List<RentalItem> rentalItems) {
        this.rentalItems = rentalItems;
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

    public String getStatusAsString() {
        switch (status) {
            case 1:
                return "Active";
            case 2:
                return "Completed";
            case 3:
                return "Cancelled";
            default:
                return "Undefined";
        }
    }

    public String getPaymentMethodAsString() {
        switch (paymentMethod) {
            case 1:
                return "Cash";
            case 2:
                return "Card";
            default:
                return "Undefined";
        }
    }
}
