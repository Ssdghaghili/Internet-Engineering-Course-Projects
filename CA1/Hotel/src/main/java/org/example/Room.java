package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Room {
    @JsonProperty("id")
    private int roomId;
    @JsonProperty("capacity")
    private int roomCapacity;

    public Room() {}

    public Room(int roomId, int roomCapacity) {
        this.roomId = roomId;
        this.roomCapacity = roomCapacity;
    }

    public int getRoomId() { return roomId; }
    public int getCapacity() { return roomCapacity; }
}