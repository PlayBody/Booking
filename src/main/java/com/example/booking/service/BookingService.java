package com.example.booking.service;

import com.example.booking.model.Block;
import com.example.booking.model.Booking;
import com.example.booking.model.data.DateInterval;
import com.example.booking.repository.BlockRepository;
import com.example.booking.repository.BookingRepository;
import com.example.booking.util.Logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.booking.util.Logic.isOverlappedDateInterval;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final BlockRepository blockRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, BlockRepository blockRepository) {
        this.bookingRepository = bookingRepository;
        this.blockRepository = blockRepository;
    }

    public Booking createBooking(Booking booking) {
        validateBooking(booking);
        return bookingRepository.save(booking);
    }

    public Optional<Booking> getBooking(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
        validateBooking(updatedBooking);
        return bookingRepository.findById(id).map(booking -> {
            booking.setGuestName(updatedBooking.getGuestName());
            booking.setStartDate(updatedBooking.getStartDate());
            booking.setEndDate(updatedBooking.getEndDate());
            booking.setGuestData(updatedBooking.getGuestData());
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new IllegalArgumentException("Booking id " + id + " not found"));
    }

    public void deleteBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Booking id " + id + " not found");
        }
    }

    public List<DateInterval> getAvailableIntervals(Long propertyId, Long bookingId, DateInterval checkInterval) {
        List<Booking> bookings = bookingRepository.getBookingsByPropertyIdAndIdNot(propertyId, bookingId);
        List<Block> blocks = blockRepository.getBlocksByPropertyId(propertyId);
        List<DateInterval> intervals = bookings.stream()
                .filter(Booking::isEnable)
                .map(Booking::getInterval)
                .filter(interval -> isOverlappedDateInterval(interval, checkInterval))
                .collect(Collectors.toList());
        intervals.addAll(blocks.stream()
                .map(Block::getInterval)
                .filter(interval -> isOverlappedDateInterval(interval, checkInterval))
                .collect(Collectors.toList()));
        return Logic.getSplitIntervals(intervals, checkInterval);
    }

    public List<Booking> updateOverlappedBookings(Block block) {
        List<Booking> bookings = bookingRepository.getBookingsByPropertyId(block.getPropertyId());
        bookings = bookings.stream()
                .filter(Booking::isEnable)
                .filter(booking -> isOverlappedDateInterval(booking.getInterval(), block.getInterval()))
                .collect(Collectors.toList());
        bookings.forEach(Booking::setCancel);
        return bookingRepository.saveAll(bookings);
    }
    private void validateBooking(Booking booking) {
        if (booking.getEndDate().before(booking.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        List<Booking> bookings = bookingRepository.getBookingsByPropertyIdAndIdNot(booking.getPropertyId(), booking.getId());
        List<Block> blocks = blockRepository.getBlocksByPropertyId(booking.getPropertyId());
        long count = bookings.stream()
                .filter(Booking::isEnable)
                .filter(book -> isOverlappedDateInterval(book.getInterval(), booking.getInterval()))
                .count();
        count += blocks.stream()
                .filter(block -> isOverlappedDateInterval(block.getInterval(), booking.getInterval()))
                .count();
        if(count > 0) {
            throw new RuntimeException("Booking interval was overlapped.");
        }
    }
}