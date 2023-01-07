package com.example.smartspace2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MCDto {

    @SerializedName("id")
    private int id;
    @SerializedName("address")
    private String address;
    @SerializedName("short_name")
    private String shortName;
    @SerializedName("location")
    private String locationName;

    public MCDto() {}

    public MCDto(int id, String address, String shortName) {
        this.id = id;
        this.address = address;
        this.shortName = shortName;
    }

    public MCDto(String address, String shortName) {
        this.address = address;
        this.shortName = shortName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
