package com.example.smartspace2.dto;

import com.google.gson.annotations.SerializedName;

public class MCDto {

    @SerializedName("id")
    private int id;
    @SerializedName("address")
    private String address;
    @SerializedName("shortname")
    private String shortname;
    @SerializedName("location")
    private String locationName;
    @SerializedName("location_id")
    private int locationID;

    public MCDto() {}

    public MCDto(int id, String address, String shortName, String locationName) {
        this.id = id;
        this.address = address;
        this.shortname = shortName;
        this.locationName = locationName;
    }

    public MCDto(String address, String shortName) {
        this.address = address;
        this.shortname = shortName;
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

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }
}
