package com.example.smartspace2.dto;

import java.io.Serializable;

public class HistoryItem implements Serializable {
    private long date;
    private String value;

    public HistoryItem() {
    }

    public HistoryItem(long date, String value) {
        this.date = date;
        this.value = value;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
