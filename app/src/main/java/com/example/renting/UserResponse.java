package com.example.renting;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("tenantId")
    private String tenantId;
    @SerializedName("userName")
    private String userName;
    @SerializedName("name")
    private String name;
    @SerializedName("surname")
    private String surname;
    @SerializedName("email")
    private String email;
    @SerializedName("emailConfirmed")
    private boolean emailConfirmed;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("phoneNumberConfirmed")
    private boolean phoneNumberConfirmed;
    @SerializedName("isActive")
    private boolean isActive;
    @SerializedName("lockoutEnabled")
    private boolean lockoutEnabled;
    @SerializedName("accessFailedCount")
    private int accessFailedCount;
    @SerializedName("lockoutEnd")
    private String lockoutEnd;
    @SerializedName("concurrencyStamp")
    private String concurrencyStamp;
    @SerializedName("entityVersion")
    private int entityVersion;
    @SerializedName("lastPasswordChangeTime")
    private String lastPasswordChangeTime;
    @SerializedName("isDeleted")
    private boolean isDeleted;
    @SerializedName("deleterId")
    private String deleterId;
    @SerializedName("deletionTime")
    private String deletionTime;
    @SerializedName("lastModificationTime")
    private String lastModificationTime;
    @SerializedName("lastModifierId")
    private String lastModifierId;
    @SerializedName("creationTime")
    private String creationTime;
    @SerializedName("creatorId")
    private String creatorId;
    @SerializedName("id")
    private String id;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isPhoneNumberConfirmed() {
        return phoneNumberConfirmed;
    }

    public void setPhoneNumberConfirmed(boolean phoneNumberConfirmed) {
        this.phoneNumberConfirmed = phoneNumberConfirmed;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isLockoutEnabled() {
        return lockoutEnabled;
    }

    public void setLockoutEnabled(boolean lockoutEnabled) {
        this.lockoutEnabled = lockoutEnabled;
    }

    public int getAccessFailedCount() {
        return accessFailedCount;
    }

    public void setAccessFailedCount(int accessFailedCount) {
        this.accessFailedCount = accessFailedCount;
    }

    public String getLockoutEnd() {
        return lockoutEnd;
    }

    public void setLockoutEnd(String lockoutEnd) {
        this.lockoutEnd = lockoutEnd;
    }

    public String getConcurrencyStamp() {
        return concurrencyStamp;
    }

    public void setConcurrencyStamp(String concurrencyStamp) {
        this.concurrencyStamp = concurrencyStamp;
    }

    public int getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(int entityVersion) {
        this.entityVersion = entityVersion;
    }

    public String getLastPasswordChangeTime() {
        return lastPasswordChangeTime;
    }

    public void setLastPasswordChangeTime(String lastPasswordChangeTime) {
        this.lastPasswordChangeTime = lastPasswordChangeTime;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getDeleterId() {
        return deleterId;
    }

    public void setDeleterId(String deleterId) {
        this.deleterId = deleterId;
    }

    public String getDeletionTime() {
        return deletionTime;
    }

    public void setDeletionTime(String deletionTime) {
        this.deletionTime = deletionTime;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
