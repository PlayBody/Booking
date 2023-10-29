package com.example.booking.controller;

import com.example.booking.model.Booking;
import com.example.booking.model.data.DateInterval;
import com.example.booking.payload.response.IntervalsResponse;
import com.example.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.booking.util.Logic.convertString2Date;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.createBooking(booking));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        return bookingService.getBooking(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/check")
    public ResponseEntity<IntervalsResponse> checkAvailableIntervals(@RequestParam Long propertyId, @RequestParam String from, @RequestParam String to) {
        return ResponseEntity.ok(new IntervalsResponse(bookingService.getAvailableIntervals(propertyId, -1L, new DateInterval(convertString2Date(from), convertString2Date(to)))));
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<IntervalsResponse> checkBooking(@PathVariable Long id) {
        Optional<Booking> optionalBooking =  bookingService.getBooking(id);
        if(optionalBooking.isPresent()){
            Booking booking = optionalBooking.get();
            return ResponseEntity.ok(new IntervalsResponse(bookingService.getAvailableIntervals(booking.getPropertyId(), booking.getId(), booking.getInterval())));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @Valid @RequestBody Booking updatedBooking) {
        try {
            return ResponseEntity.ok(bookingService.updateBooking(id, updatedBooking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}