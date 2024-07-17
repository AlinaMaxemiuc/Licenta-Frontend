package com.example.renting;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateOrUpdateRentalDto {

    @SerializedName("customerId")
    private String customerId;

    @SerializedName("startDay")
    private String startDay;

    @SerializedName("endDay")
    private String endDay;

    @SerializedName("status")
    private int status;

    @SerializedName("paymentMethod")
    private int paymentMethod;

    @SerializedName("rentalItems")
    private List<RentalItemDto> rentalItems;

    @SerializedName("total")
    private Double total;
    @SerializedName("address")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String input;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
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

    public List<RentalItemDto> getRentalItems() {
        return rentalItems;
    }

    public void setRentalItems(List<RentalItemDto> rentalItems) {
        this.rentalItems = rentalItems;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
