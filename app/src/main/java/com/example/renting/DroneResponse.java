package com.example.renting;

import java.util.List;

public class DroneResponse {
    private int totalCount;
    private List<DroneData> items;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<DroneData> getItems() {
        return items;
    }

    public void setItems(List<DroneData> items) {
        this.items = items;
    }
}
