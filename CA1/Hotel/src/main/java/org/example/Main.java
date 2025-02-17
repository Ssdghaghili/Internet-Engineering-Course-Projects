package org.example;

import java.util.*;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonProperty;

//--------------------------------------------------------------------------------------------

class Customer {
    private int ssn;
    private String name;
    private String phone;
    private int age;

    public Customer() {}
    public Customer(int ssn, String name, String phone, int age) {
        this.ssn = ssn;
        this.name = name;
        this.phone = phone;
        this.age = age;
    }

    public int getSsn() { return ssn; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public int getAge() { return age; }
}

class Room {
    @JsonProperty("id")
    private int roomId;
    @JsonProperty("capacity")
    private int roomCapacity;

    public Room() {}
    public Room(int roomId, int roomCapacity) {
        this.roomId = roomId;
        this.roomCapacity = roomCapacity;
    }

    public int getId() { return roomId; }
    public int getCapacity() { return roomCapacity; }
}

class Booking {
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

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public int getRoomId() { return roomId; }
    public Date getCheckIn() { return check_in; }
    public Date getCheckOut() { return check_out; }

    public int getStayDurationInDays() {
       double exactTime = (double) (check_out.getTime() - check_in.getTime()) / (1000 * 60 * 60 * 24);
       return (int) Math.ceil(exactTime);
    }
}

class Hotel {
    private List<Customer> customers = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    public List<Customer> getCustomers() { return customers; }
    public List<Room> getRooms() { return rooms; }
    public List<Booking> getBookings() { return bookings; }

    private Customer findCustomerById(int id) {
        return customers.stream()
                .filter(customer -> customer.getSsn() == id)
                .findFirst()
                .orElse(null);
    }

    private Room findRoomById(int id) {
        return rooms.stream()
                .filter(room -> room.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Room> getRooms(int minCapacity) {
        return rooms.stream()
                .filter(room -> room.getCapacity() >= minCapacity)
                .toList();
    }

    public String getOldestCustomerName() {
        return customers.stream()
                .max(Comparator.comparingInt(Customer::getAge))
                .map(Customer::getName)
                .orElse("No customers available");
    }

    public List<String> getCustomerPhonesByRoomNumber(int roomNumber) {
        return bookings.stream()
                .filter(booking -> booking.getRoomId() == roomNumber)
                .map(booking -> {
                    Customer customer = findCustomerById(booking.getCustomerId());
                    return customer != null ? customer.getPhone() : "Unknown";
                })
                .toList();
    }

    public String logState() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);

            // Save JSON to file
            File file = new File("state.json");
            objectMapper.writeValue(file, this);

            return jsonString;
        } catch (IOException e) {
            e.printStackTrace();
            return "{}"; // Return empty JSON if an error occurs
        }
    }
}

// Example Usage
public class Main {
    public static void main(String[] args) {
        try {
            int minCapacity = 2;
            // Load JSON file from resources
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("data.json");

            if (inputStream == null) {
                throw new RuntimeException("File not found: data.json");
            }

            // Parse JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Hotel hotel = objectMapper.readValue(inputStream, Hotel.class);

            // Outputs
            System.out.println("Oldest Customer: " + hotel.getOldestCustomerName());

            List<Room> rooms = hotel.getRooms(minCapacity);
            System.out.println("Number of rooms with min capacity " +minCapacity + ": " + rooms.size());
            for (Room room : rooms) {
                System.out.println("Room Number(Id): " + room.getId());
            }

            System.out.println("Customer phones for room 102: " + hotel.getCustomerPhonesByRoomNumber(102));

            //state.json
            String jsonOutput = hotel.logState();
            System.out.println("Hotel state saved to state.json.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}