package com.example.booking.service;

import com.example.booking.model.Block;
import com.example.booking.model.Booking;
import com.example.booking.repository.BlockRepository;
import com.example.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlockService {

    private final BlockRepository blockRepository;

    @Autowired
    public BlockService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    public Block createBlock(Block block) {
        validateBlock(block);
        return blockRepository.save(block);
    }

    public Optional<Block> getBlock(Long id) {
        return blockRepository.findById(id);
    }

    public List<Block> getAllBlocks() {
        return blockRepository.findAll();
    }

    public Block updateBlock(Long id, Block updatedBlock) {
        validateBlock(updatedBlock);
        return blockRepository.findById(id).map(block -> {
            block.setStartDate(updatedBlock.getStartDate());
            block.setEndDate(updatedBlock.getEndDate());
            return blockRepository.save(block);
        }).orElseThrow(() -> new IllegalArgumentException("Block id " + id + " not found"));
    }

    public void deleteBlock(Long id) {
        // Ensure the block exists before deleting
        if (blockRepository.existsById(id)) {
            blockRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Block id " + id + " not found");
        }
    }
    private void validateBlock(Block block) {
        if (block.getEndDate().before(block.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }
}