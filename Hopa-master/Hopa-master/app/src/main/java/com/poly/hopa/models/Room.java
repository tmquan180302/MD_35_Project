package com.poly.hopa.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Room {
    @SerializedName("_id")
    private String roomId;
    private String type;
    private int price;
    private List<Convenience> convenience;
    private String description;
    private Boolean isAvailable;
    private List<String> image;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Convenience> getConvenience() {
        return convenience;
    }

    public void setConvenience(List<Convenience> convenience) {
        this.convenience = convenience;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }
}
