package com.example.renting;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RentalResponse {
    @SerializedName("totalCount")
    private int totalCount;

    @SerializedName("items")
    private List<RentalData> items;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<RentalData> getItems() {
        return items;
    }

    public void setItems(List<RentalData> items) {
        this.items = items;
    }
}
