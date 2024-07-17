package com.example.renting;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class RentalDetailsDto {

    @SerializedName("id")
    private String id;
    @SerializedName("customerId")
    private String customerId;

    @SerializedName("startDay")
    private String startDay;

    @SerializedName("endDay")
    private String endDay;

    @SerializedName("total")
    private double total;

    @SerializedName("status")
    private int status;

    @SerializedName("paymentMethod")
    private double paymentMethod;

    @SerializedName("rentalItems")
    private List<RentalItemDto> rentalItems;

    @SerializedName("concurrencyStamp")
    private String concurrencyStamp;


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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(double paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<RentalItemDto> getRentalItems() {
        return rentalItems;
    }

    public void setRentalItems(List<RentalItemDto> rentalItems) {
        this.rentalItems = rentalItems;
    }

    public String getConcurrencyStamp() {
        return concurrencyStamp;
    }

    public void setConcurrencyStamp(String concurrencyStamp) {
        this.concurrencyStamp = concurrencyStamp;
    }
}
