package com.poly.hopa.models.ServerResponse;

import com.poly.hopa.models.Review;
import com.poly.hopa.models.Room;

import java.util.List;

public class ServerResRoom {
    private Room room;
    private Review review;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
