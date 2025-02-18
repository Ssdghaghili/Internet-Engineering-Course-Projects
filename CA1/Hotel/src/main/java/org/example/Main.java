package org.example;

import java.util.*;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.io.FileWriter;
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

    public int getRoomId() { return roomId; }
    public int getCapacity() { return roomCapacity; }
}

class Booking {
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
                .filter(room -> room.getRoomId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean addCustomer(Customer customer) {
        boolean exists = customers.stream().anyMatch(c -> c.getSsn() == customer.getSsn());
        if (exists) {
            throw new IllegalArgumentException("Customer with SSN " + customer.getSsn() + " already exists.");
        }
        return customers.add(customer);
    }

    public boolean addRoom(Room room) {
        boolean exists = rooms.stream().anyMatch(r -> r.getRoomId() == room.getRoomId());
        if (exists) {
            throw new IllegalArgumentException("Room with ID " + room.getRoomId() + " already exists.");
        }
        return rooms.add(room);
    }

    public boolean addBooking(Booking booking) {
        boolean exists = bookings.stream().anyMatch(b -> b.getBookingId() == booking.getBookingId());
        if (exists) {
            throw new IllegalArgumentException("Booking with ID " + booking.getBookingId() + " already exists.");
        }
        return bookings.add(booking);
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

    public void validateData() {
        Set<Integer> customerSSNs = new HashSet<>();
        Set<Integer> roomIds = new HashSet<>();
        Set<Integer> bookingIds = new HashSet<>();

        for (Customer customer : customers) {
            if (!customerSSNs.add(customer.getSsn())) {
                throw new IllegalArgumentException("Duplicate SSN found: " + customer.getSsn());
            }
        }

        for (Room room : rooms) {
            if (!roomIds.add(room.getRoomId())) {
                throw new IllegalArgumentException("Duplicate Room ID found: " + room.getRoomId());
            }
        }

        for (Booking booking : bookings) {
            if (!bookingIds.add(booking.getBookingId())) {
                throw new IllegalArgumentException("Duplicate Booking ID found: " + booking.getBookingId());
            }
        }
    }

    public String logStated() {
        List<Map<String, Object>> roomsState = new ArrayList<>();
        // Formatter for the date output.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Room room : rooms) {
            Map<String, Object> roomMap = new LinkedHashMap<>();
            roomMap.put("room_id", room.getRoomId());
            roomMap.put("capacity", room.getCapacity());

            // Gather bookings for this room.
            List<Map<String, Object>> roomBookings = new ArrayList<>();
            for (Booking booking : bookings) {
                if (booking.getRoomId() == room.getRoomId()) {
                    Map<String, Object> bookingMap = new LinkedHashMap<>();
                    bookingMap.put("id", booking.getBookingId());

                    // Add customer details for this booking.
                    Customer customer = findCustomerById(booking.getCustomerId());
                    if (customer != null) {
                        Map<String, Object> customerMap = new LinkedHashMap<>();
                        customerMap.put("ssn", customer.getSsn());
                        customerMap.put("name", customer.getName());
                        customerMap.put("phone", customer.getPhone());
                        customerMap.put("age", customer.getAge());
                        bookingMap.put("customer", customerMap);
                    } else {
                        bookingMap.put("customer", null);
                    }
                    bookingMap.put("check_in", sdf.format(booking.getCheckIn()));
                    bookingMap.put("check_out", sdf.format(booking.getCheckOut()));

                    roomBookings.add(bookingMap);
                }
            }
            roomMap.put("bookings", roomBookings);
            roomsState.add(roomMap);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(roomsState);
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
        }
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            int minCapacity = 2;
            // Load Json file
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("data.json");

            if (inputStream == null) {
                throw new RuntimeException("File not found: data.json");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Hotel hotel = objectMapper.readValue(inputStream, Hotel.class);

            hotel.validateData(); // check for duplicates in Json file

            // Outputs
            System.out.println("Oldest Customer: " + hotel.getOldestCustomerName());
            System.out.println("Customer phones for room 102: " + hotel.getCustomerPhonesByRoomNumber(102));
            List<Room> rooms = hotel.getRooms(minCapacity);
            System.out.println("Number of rooms with min capacity " +minCapacity + ": " + rooms.size());
            for (Room room : rooms) {
                System.out.println("Room Number(Id): " + room.getRoomId());
            }

            //state.json
            String stateJson = hotel.logStated();
            File stateFile = new File("state.json");
            try (FileWriter writer = new FileWriter(stateFile)) {
                writer.write(stateJson);
            }
            System.out.println("Hotel state written to state.json");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}