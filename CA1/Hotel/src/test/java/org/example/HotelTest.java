package org.example;

import org.junit.jupiter.api.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HotelTest {
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        // Setup test data before each test
        hotel = new Hotel();

        // Add customers
        hotel.getCustomers().add(new Customer(1, "Alice", "123456789", 30));
        hotel.getCustomers().add(new Customer(2, "Bob", "987654321", 45));
        hotel.getCustomers().add(new Customer(3, "Charlie", "555666777", 25));

        // Add rooms
        hotel.getRooms().add(new Room(101, 2));
        hotel.getRooms().add(new Room(102, 4));
        hotel.getRooms().add(new Room(103, 1));
    }

    @AfterEach
    void tearDown() {
        hotel = null;
    }

    @Test
    void testGetOldestCustomerName() {
        assertEquals("Bob", hotel.getOldestCustomerName(), "Oldest customer should be Bob");
    }

    @Test
    void testGetRoomsByCapacity() {
        List<Room> rooms = hotel.getRooms(2);
        assertEquals(2, rooms.size(), "Expected 2 rooms with capacity >= 2");
    }

    @Test
    void testCustomerPhonesByRoom() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Add a booking
        hotel.getBookings().add(new Booking(1, 1, 102, sdf.parse("2025-02-14 14:00:00"),
                sdf.parse("2025-02-16 12:00:00")));

        List<String> phones = hotel.getCustomerPhonesByRoomNumber(102);
        assertEquals(1, phones.size());
        assertEquals("123456789", phones.get(0));
    }

    @Test
    void testInvalidBookingCheckOutBeforeCheckIn() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date checkIn = sdf.parse("2025-02-16 14:00:00");
        Date checkOut = sdf.parse("2025-02-15 12:00:00"); // Invalid date

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Booking(4, 1, 101, checkIn, checkOut)
        );

        assertEquals("Check-out date cannot be before check-in date", exception.getMessage());
    }

    @Test
    void testStayDurationCalculation() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        hotel.addBooking(new Booking(1, 1, 101, sdf.parse("2025-02-14 14:00:00"), sdf.parse("2025-02-16 15:00:00")));
        //Booking booking = new Booking(1, 1, 101, sdf.parse("2025-02-14 14:00:00"), sdf.parse("2025-02-16 15:00:00"));
        assertEquals(3, hotel.getBookings().get(0).getStayDurationInDays(), "Stay duration should be 3 days");
        //assertEquals(3, booking.getStayDurationInDays(), "Stay duration should be 3 days");
    }
}
