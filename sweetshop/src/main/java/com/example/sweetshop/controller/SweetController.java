package com.example.sweetshop.controller;

import com.example.sweetshop.dto.SweetRequestDTO;
import com.example.sweetshop.dto.SweetResponseDTO;
import com.example.sweetshop.exception.BadRequestException;
import com.example.sweetshop.service.SweetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sweets")
@CrossOrigin("*")
public class SweetController {

    private final SweetService sweetService;

    public SweetController(SweetService sweetService) {
        this.sweetService = sweetService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SweetResponseDTO> addSweet(
            @Valid @RequestBody SweetRequestDTO request) {

        return ResponseEntity.ok(sweetService.addSweet(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SweetResponseDTO> updateSweet(
            @PathVariable Long id,
            @Valid @RequestBody SweetRequestDTO request) {

        return ResponseEntity.ok(sweetService.updateSweet(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteSweet(@PathVariable Long id) {
        sweetService.deleteSweet(id);
        return ResponseEntity.ok("Sweet deleted successfully");
    }

    @PostMapping("/{id}/restock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SweetResponseDTO> restockSweet(
            @PathVariable Long id,
            @RequestParam int quantity) {

        return ResponseEntity.ok(sweetService.restockSweet(id, quantity));
    }

    @GetMapping
    public ResponseEntity<List<SweetResponseDTO>> getAllSweets() {
        return ResponseEntity.ok(sweetService.getAllSweets());
    }

    @GetMapping("/search")
    public ResponseEntity<List<SweetResponseDTO>> searchSweets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {


        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(sweetService.searchByName(name));
        }

        if (category != null && !category.isBlank()) {
            return ResponseEntity.ok(sweetService.searchByCategory(category));
        }

        if (minPrice != null && maxPrice != null) {
            if (minPrice.compareTo(maxPrice) > 0) {
                throw new BadRequestException("Min price cannot be greater than max price");
            }
            return ResponseEntity.ok(
                    sweetService.searchByPriceRange(minPrice, maxPrice));
        }

        if (minPrice != null) {
            return ResponseEntity.ok(
                    sweetService.searchByMinPrice(minPrice));
        }

        if (maxPrice != null) {
            return ResponseEntity.ok(
                    sweetService.searchByMaxPrice(maxPrice));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<SweetResponseDTO> purchaseSweet(
            @PathVariable Long id) {

        return ResponseEntity.ok(sweetService.purchaseSweet(id));
    }
}
