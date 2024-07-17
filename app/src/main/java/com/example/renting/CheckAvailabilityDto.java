package com.example.renting;

import com.google.gson.annotations.SerializedName;

public class CheckAvailabilityDto {
    @SerializedName("droneId")
    private String droneId;
    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    public CheckAvailabilityDto(String droneId, String startDate, String endDate) {
        this.droneId = droneId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
