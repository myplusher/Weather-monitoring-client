package com.example.smartspace2.dto;

import java.io.Serializable;
import java.util.List;

public class RoomListDto implements Serializable {
    private List<RoomDto> list;

    public RoomListDto(List<RoomDto> list) {
        this.list = list;
    }

    public RoomListDto() {
    }

    public List<RoomDto> getList() {
        return list;
    }

    public void setList(List<RoomDto> list) {
        this.list = list;
    }
}
