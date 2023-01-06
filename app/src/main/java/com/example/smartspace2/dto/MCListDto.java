package com.example.smartspace2.dto;

import java.io.Serializable;
import java.util.List;

public class MCListDto implements Serializable {
    private List<MCDto> list;

    public MCListDto() {}

    public MCListDto(List<MCDto> list) {
        this.list = list;
    }

    public List<MCDto> getList() {
        return list;
    }

    public void setList(List<MCDto> list) {
        this.list = list;
    }
}
