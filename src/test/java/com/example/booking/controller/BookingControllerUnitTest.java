package com.example.booking.controller;

import com.example.booking.model.Booking;
import com.example.booking.model.data.DateInterval;
import com.example.booking.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private Booking booking;
    private List<Booking> bookings;

    private DateInterval dateInterval;

    @BeforeEach
    public void setup() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        booking = new Booking();
        booking.setGuestName("Ami");
        booking.setGuestData("Rampage");
        booking.setPropertyId(1L);
        booking.setStartDate(calendar.getTime());
        calendar.add(Calendar.DATE, 10);
        booking.setEndDate(calendar.getTime());

        bookings = Arrays.asList(booking);

        dateInterval = new DateInterval(booking.getStartDate(), booking.getEndDate());
    }

    @Test
    public void shouldCreateBooking() throws Exception {
        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(booking)));

        verify(bookingService, times(1)).createBooking(any(Booking.class));
    }


    @Test
    public void shouldGetBooking() throws Exception {
        when(bookingService.getBooking(booking.getId())).thenReturn(Optional.of(booking));

        mockMvc.perform(get("/api/bookings/" + booking.getId()))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getBooking(booking.getId());
    }

    @Test
    public void shouldGetAllBookings() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(bookings);

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    public void shouldCheckAvailableIntervals() throws Exception {
        when(bookingService.getAvailableIntervals(any(), any(), any())).thenReturn(Arrays.asList(dateInterval));

        mockMvc.perform(get("/api/bookings/check")
                        .param("propertyId", "1")
                        .param("from", "2023-12-12")
                        .param("to", "2024-05-05"))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getAvailableIntervals(any(), any(), any());
    }

    @Test
    public void shouldCheckBooking() throws Exception {
        when(bookingService.getBooking(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingService.getAvailableIntervals(any(), any(), any())).thenReturn(Arrays.asList(dateInterval));

        mockMvc.perform(get("/api/bookings/check/" + booking.getId()))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getBooking(booking.getId());
        verify(bookingService, times(1)).getAvailableIntervals(any(), any(), any());
    }

    @Test
    public void shouldUpdateBooking() throws Exception {
        when(bookingService.updateBooking(any(), any())).thenReturn(booking);

        mockMvc.perform(put("/api/bookings/" + booking.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).updateBooking(any(), any());
    }

    @Test
    public void shouldDeleteBooking() throws Exception {
        doNothing().when(bookingService).deleteBooking(booking.getId());

        mockMvc.perform(delete("/api/bookings/" + booking.getId()))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).deleteBooking(booking.getId());
    }
}
