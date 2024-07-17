package com.example.renting;

import com.google.gson.annotations.SerializedName;

public class ReviewData {
    @SerializedName("id")
    private String id;

    @SerializedName("rating")
    private int rating;

    @SerializedName("content")
    private String content;

    @SerializedName("reviewDate")
    private String reviewDate;

    @SerializedName("droneId")
    private String droneId;

    @SerializedName("customerId")
    private String customerId;

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
