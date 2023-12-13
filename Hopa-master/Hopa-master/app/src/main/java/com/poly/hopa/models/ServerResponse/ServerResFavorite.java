package com.poly.hopa.models.ServerResponse;

import com.poly.hopa.models.Room;

public class ServerResFavorite {
    private Room room;
    private String message;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
