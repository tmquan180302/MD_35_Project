package com.poly.hopa.models;

import com.google.gson.annotations.SerializedName;

public class Favorite {
    @SerializedName("_id")
    private String favoriteId;
    private User user;
    private Room room;

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
