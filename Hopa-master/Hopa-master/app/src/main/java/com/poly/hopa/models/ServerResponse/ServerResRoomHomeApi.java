package com.poly.hopa.models.ServerResponse;

import com.poly.hopa.models.Service;

import java.util.List;

public class ServerResRoomHomeApi {
    List<ServerResRoomHome> roomHigh;
    List<Service> service;
    List<ServerResRoomHome> roomLow;

    public List<ServerResRoomHome> getRoomHigh() {
        return roomHigh;
    }

    public void setRoomHigh(List<ServerResRoomHome> roomHigh) {
        this.roomHigh = roomHigh;
    }

    public List<Service> getService() {
        return service;
    }

    public void setService(List<Service> service) {
        this.service = service;
    }

    public List<ServerResRoomHome> getRoomLow() {
        return roomLow;
    }

    public void setRoomLow(List<ServerResRoomHome> roomLow) {
        this.roomLow = roomLow;
    }
}
