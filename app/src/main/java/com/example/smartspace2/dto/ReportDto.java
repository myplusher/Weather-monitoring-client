package com.example.smartspace2.dto;

import java.io.Serializable;

public class ReportDto implements Serializable {
    private String file;

    public ReportDto() {
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
