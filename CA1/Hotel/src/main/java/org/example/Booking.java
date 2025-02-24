package org.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Booking {
    @JsonProperty("id")
    private int id;
    @JsonProperty("customer_id")
    private int customerId;
    @JsonProperty("room_id")
    private int roomId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date check_in;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date check_out;

    public Booking() {}

    public Booking(int id, int customerId, int roomId, Date check_in, Date check_out) {
        if (check_out.before(check_in)) {
            throw new IllegalArgumentException("Check-out date cannot be before check-in date");
        }
        this.id = id;
        this.customerId = customerId;
        this.roomId = roomId;
        this.check_in = check_in;
        this.check_out = check_out;
    }

    public int getBookingId() { return id; }
    public int getCustomerId() { return customerId; }
    public int getRoomId() { return roomId; }
    public Date getCheckIn() { return check_in; }
    public Date getCheckOut() { return check_out; }

    public int getStayDurationInDays() {
        double exactTime = (double) (check_out.getTime() - check_in.getTime()) / (1000 * 60 * 60 * 24);
        return (int) Math.ceil(exactTime);
    }
}