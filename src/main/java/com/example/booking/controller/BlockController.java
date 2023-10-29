package com.example.booking.controller;

import com.example.booking.model.Block;
import com.example.booking.payload.response.BlockResponse;
import com.example.booking.service.BlockService;
import com.example.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/blocks")
public class BlockController {

    private final BlockService blockService;

    private final BookingService bookingService;

    @Autowired
    public BlockController(BlockService blockService, BookingService bookingService) {
        this.blockService = blockService;
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BlockResponse> createBlock(@Valid @RequestBody Block block) {
        BlockResponse blockResponse = new BlockResponse();
        blockResponse.setBlock(blockService.createBlock(block));
        blockResponse.setChangedBookings(bookingService.updateOverlappedBookings(block));
        return ResponseEntity.ok(blockResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Block> getBlock(@PathVariable Long id) {
        return blockService.getBlock(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Block>> getAllBlocks() {
        return ResponseEntity.ok(blockService.getAllBlocks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlockResponse> updateBlock(@PathVariable Long id, @Valid @RequestBody Block updatedBlock) {
        try {
            BlockResponse blockResponse = new BlockResponse();
            Block block = blockService.updateBlock(id, updatedBlock);
            blockResponse.setBlock(block);
            blockResponse.setChangedBookings(bookingService.updateOverlappedBookings(block));
            return ResponseEntity.ok(blockResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        try {
            blockService.deleteBlock(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}