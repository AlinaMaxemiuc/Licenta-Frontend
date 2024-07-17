package com.example.renting;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class DroneData implements Parcelable {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("model")
    private String model;

    @SerializedName("utility")
    private int utility;

    @SerializedName("category")
    private int category;

    @SerializedName("productionYear")
    private String productionYear;

    @SerializedName("pricePerDay")
    private Double pricePerDay;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getUtility() {
        return utility;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(String productionYear) {
        this.productionYear = productionYear;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
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

    public String getCategoryAsString() {
        switch (category) {
            case 1:
                return "Small";
            case 2:
                return "Medium";
            case 3:
                return "Large";
            default:
                return "Undefined";
        }
    }

    public String getUtilityAsString() {
        switch (utility) {
            case 1:
                return "Farming";
            case 2:
                return "PhotographyVideo";
            case 3:
                return "Transport";
            case 4:
                return "Monitoring";
            default:
                return "Undefined";
        }
    }


    // Parcelable implementation
    protected DroneData(Parcel in) {
        id = in.readString();
        name = in.readString();
        model = in.readString();
        utility = in.readInt();
        category = in.readInt();
        productionYear = in.readString();
        if (in.readByte() == 0) {
            pricePerDay = null;
        } else {
            pricePerDay = in.readDouble();
        }
    }

    public static final Creator<DroneData> CREATOR = new Creator<DroneData>() {
        @Override
        public DroneData createFromParcel(Parcel in) {
            return new DroneData(in);
        }

        @Override
        public DroneData[] newArray(int size) {
            return new DroneData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(model);
        dest.writeInt(utility);
        dest.writeInt(category);
        dest.writeString(productionYear);
        if (pricePerDay == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(pricePerDay);
        }
    }
}
