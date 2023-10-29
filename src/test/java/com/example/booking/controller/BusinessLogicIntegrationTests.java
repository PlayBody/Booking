package com.example.booking.controller;

import com.example.booking.model.Block;
import com.example.booking.model.Booking;
import com.example.booking.payload.response.BadRequestResponse;
import com.example.booking.payload.response.BlockResponse;
import com.example.booking.payload.response.IntervalsResponse;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BusinessLogicIntegrationTests {

    private final String baseUrl = "http://localhost:8080/api";

    @Autowired
    private TestRestTemplate restTemplate;

    public Date convertString2Date(String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(dateString);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    @Order(1)
    public void test_create_booking_save_success() {
        Booking booking1 = new Booking();
        booking1.setPropertyId(1L);
        booking1.setGuestName("John Doe");
        booking1.setStartDate(convertString2Date("2024-01-01"));
        booking1.setEndDate(convertString2Date("2025-01-01"));
        booking1.setGuestData("Test data1");

        // when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        String url = baseUrl + "/bookings";

        ResponseEntity<Booking> response1 = restTemplate.postForEntity(url, booking1, Booking.class);

        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response1.getBody()).getId()).isEqualTo(1);

    }

    @Test
    @Order(2)
    public void test_create_booking_but_not_save_because_overlapped_with_first_book() {
        Booking booking2 = new Booking();
        booking2.setPropertyId(1L);
        booking2.setGuestName("John Doe");
        booking2.setStartDate(convertString2Date("2024-02-05"));
        booking2.setEndDate(convertString2Date("2025-02-05"));
        booking2.setGuestData("Test data2");


        String url = baseUrl + "/bookings";
        ResponseEntity<BadRequestResponse> response2 = restTemplate.postForEntity(url, booking2, BadRequestResponse.class);

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(response2.getBody()).getErrors().get("error")).isEqualTo("Booking interval was overlapped.");
    }

    @Test
    @Order(3)
    public void test_create_booking_save_success_because_not_overlapped_with_first_book() {
        // test 3

        Booking booking3 = new Booking();
        booking3.setPropertyId(1L);
        booking3.setGuestName("John Doe");
        booking3.setStartDate(convertString2Date("2025-02-05"));
        booking3.setEndDate(convertString2Date("2026-02-05"));
        booking3.setGuestData("Test data3");


        String url = baseUrl + "/bookings";
        ResponseEntity<Booking> response3 = restTemplate.postForEntity(url, booking3, Booking.class);

        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response3.getBody()).getId()).isEqualTo(2);
    }


    @Test
    @Order(4)
    public void test_create_block_save_success_and_return_disabled_bookings() {
        // test 4

        Block block4 = new Block();
        block4.setPropertyId(1L);
        block4.setStartDate(convertString2Date("2025-01-06"));
        block4.setEndDate(convertString2Date("2026-01-01"));

        // when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        String url = baseUrl + "/blocks";

        ResponseEntity<BlockResponse> response4 = restTemplate.postForEntity(url, block4, BlockResponse.class);

        assertThat(response4.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response4.getBody()).getBlock()).isNotNull();
        List<Booking> bookingList = Objects.requireNonNull(response4.getBody()).getChangedBookings();
        assertThat(bookingList.size()).isEqualTo(1);
        assertThat(bookingList.get(0).getId()).isEqualTo(2);
        assertThat(bookingList.get(0).getGuestData()).isEqualTo("Test data3");
    }

    @Test
    @Order(5)
    public void test_update_booking_but_failed_because_overlapped() {
        Booking updatedBooking = new Booking();
        updatedBooking.setId(1L);
        updatedBooking.setPropertyId(1L);
        updatedBooking.setGuestName("Jane Doe"); // Guest name is changed
        updatedBooking.setStartDate(convertString2Date("2024-01-01"));
        updatedBooking.setEndDate(convertString2Date("2025-01-07"));
        updatedBooking.setGuestData("Test data1 Update");

        // Define the request URL
        String url = baseUrl + "/bookings/1";

        // Make the HTTP PUT request
        restTemplate.put(url, updatedBooking);

        // Make the HTTP GET request to verify the update
        ResponseEntity<Booking> response = restTemplate.getForEntity(url, Booking.class);

        // Verify the status code and response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getGuestData()).isEqualTo("Test data1");
    }

    @Test
    @Order(6)
    public void test_update_booking_success() {
        Booking updatedBooking = new Booking();
        updatedBooking.setId(1L);
        updatedBooking.setPropertyId(1L);
        updatedBooking.setGuestName("Jane Doe"); // Guest name is changed
        updatedBooking.setStartDate(convertString2Date("2024-01-01"));
        updatedBooking.setEndDate(convertString2Date("2025-01-01"));
        updatedBooking.setGuestData("Test data1 Update");

        // Define the request URL
        String url = baseUrl + "/bookings/1";

        // Make the HTTP PUT request
        restTemplate.put(url, updatedBooking);

        // Make the HTTP GET request to verify the update
        ResponseEntity<Booking> response = restTemplate.getForEntity(url, Booking.class);

        // Verify the status code and response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getGuestData()).isEqualTo("Test data1 Update");
    }

    @Test
    @Order(7)
    public void test_available_intervals() {
        // Define the request URL
        String url = baseUrl + "/bookings/check?propertyId=1&from=2023-11-30&to=2027-01-01";

        // Make the HTTP GET request to verify the update
        ResponseEntity<IntervalsResponse> response = restTemplate.getForEntity(url, IntervalsResponse.class);

        // Verify the status code and response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getDateIntervals().size()).isEqualTo(3);
        assertThat(Objects.requireNonNull(response.getBody()).getDateIntervals().get(0).getEnd().toString()).isEqualTo("Mon Jan 01 00:00:00 JST 2024");
        assertThat(Objects.requireNonNull(response.getBody()).getDateIntervals().get(1).getStart().toString()).isEqualTo("Wed Jan 01 00:00:00 JST 2025");
        assertThat(Objects.requireNonNull(response.getBody()).getDateIntervals().get(1).getEnd().toString()).isEqualTo("Mon Jan 06 00:00:00 JST 2025");
        assertThat(Objects.requireNonNull(response.getBody()).getDateIntervals().get(2).getStart().toString()).isEqualTo("Thu Jan 01 00:00:00 JST 2026");
        assertThat(Objects.requireNonNull(response.getBody()).getDateIntervals().get(2).getEnd().toString()).isEqualTo("Fri Jan 01 00:00:00 JST 2027");
    }
}