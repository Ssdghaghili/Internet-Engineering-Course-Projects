package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Hotel {
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Room room : rooms) {
            Map<String, Object> roomMap = new LinkedHashMap<>();
            roomMap.put("room_id", room.getRoomId());
            roomMap.put("capacity", room.getCapacity());

            List<Map<String, Object>> roomBookings = new ArrayList<>();
            for (Booking booking : bookings) {
                if (booking.getRoomId() == room.getRoomId()) {
                    Map<String, Object> bookingMap = new LinkedHashMap<>();
                    bookingMap.put("id", booking.getBookingId());

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