package com.example.smartspace2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties
public class RoomDto implements Serializable {
    private int id;
    private String title;
    private double temperature;
    private String temperatureRange;
    private double humidity;
    private String humidityRange;
    private double co2;
    private String co2Range;
    private double light;
    private String lightRange;
    private Date date_time;
    @SerializedName("microcontroller")
    private MCDto microcontrollerId;

    public RoomDto(int id, String title, double temp, double humidity, double co2, double light, Date date_time) {
        this.title = title;
        this.temperature = temp;
        this.humidity = humidity;
        this.co2 = co2;
        this.light = light;
        this.date_time = date_time;
    }

    public RoomDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getCo2() {
        return co2;
    }

    public void setCo2(double co2) {
        this.co2 = co2;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public MCDto getMicrocontrollerId() {
        return microcontrollerId;
    }

    public void setMicrocontrollerId(MCDto microcontrollerId) {
        this.microcontrollerId = microcontrollerId;
    }

    public String getTemperatureRange() {
        return temperatureRange;
    }

    public void setTemperatureRange(String temperatureRange) {
        this.temperatureRange = temperatureRange;
    }

    public String getHumidityRange() {
        return humidityRange;
    }

    public void setHumidityRange(String humidityRange) {
        this.humidityRange = humidityRange;
    }

    public String getCo2Range() {
        return co2Range;
    }

    public void setCo2Range(String co2Range) {
        this.co2Range = co2Range;
    }

    public String getLightRange() {
        return lightRange;
    }

    public void setLightRange(String lightRange) {
        this.lightRange = lightRange;
    }
}
