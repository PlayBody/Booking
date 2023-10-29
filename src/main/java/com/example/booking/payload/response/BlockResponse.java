package com.example.booking.payload.response;

import com.example.booking.model.Block;
import com.example.booking.model.Booking;

import java.util.List;

public class BlockResponse {
    private Block block;
    private List<Booking> changedBookings;

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public List<Booking> getChangedBookings() {
        return changedBookings;
    }

    public void setChangedBookings(List<Booking> changedBookings) {
        this.changedBookings = changedBookings;
    }
}
