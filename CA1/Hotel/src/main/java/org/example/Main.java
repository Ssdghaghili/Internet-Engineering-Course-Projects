package org.example;

import java.io.*;
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