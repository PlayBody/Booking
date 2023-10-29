package com.example.booking.repository;

import com.example.booking.model.Block;
import com.example.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> getBlocksByPropertyId(Long propertyId);
    List<Block> getBlocksByPropertyIdAndIdNot(Long propertyId, Long id);
}