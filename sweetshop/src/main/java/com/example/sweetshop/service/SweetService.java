package com.example.sweetshop.service;

import com.example.sweetshop.entity.Sweet;
import com.example.sweetshop.exception.BadRequestException;
import com.example.sweetshop.exception.ResourceNotFoundException;
import com.example.sweetshop.repository.SweetRepository;
import org.springframework.stereotype.Service;
import com.example.sweetshop.dto.SweetRequestDTO;
import com.example.sweetshop.dto.SweetResponseDTO;

import java.math.BigDecimal;
import java.util.List;
@Service
public class SweetService {

    private final SweetRepository sweetRepository;

    public SweetService(SweetRepository sweetRepository) {
        this.sweetRepository = sweetRepository;
    }

    // ---------- ADMIN ----------
    public SweetResponseDTO addSweet(SweetRequestDTO dto) {
        Sweet sweet = Sweet.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();

        return mapToDTO(sweetRepository.save(sweet));
    }

    // ---------- USER + ADMIN ----------
    public List<SweetResponseDTO> getAllSweets() {
        return sweetRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<SweetResponseDTO> searchByName(String name) {
        return sweetRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<SweetResponseDTO> searchByCategory(String category) {
        return sweetRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<SweetResponseDTO> searchByPriceRange(BigDecimal min, BigDecimal max) {
        return sweetRepository.findByPriceBetween(min, max)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<SweetResponseDTO> searchByMinPrice(BigDecimal min) {
        return sweetRepository.findByPriceGreaterThanEqual(min)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<SweetResponseDTO> searchByMaxPrice(BigDecimal max) {
        return sweetRepository.findByPriceLessThanEqual(max)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }


    // ---------- ADMIN ----------
    public SweetResponseDTO updateSweet(Long id, SweetRequestDTO dto) {
        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sweet not found with id " + id));

        sweet.setName(dto.getName());
        sweet.setCategory(dto.getCategory());
        sweet.setPrice(dto.getPrice());
        sweet.setQuantity(dto.getQuantity());

        return mapToDTO(sweetRepository.save(sweet));
    }

    public void deleteSweet(Long id) {
        if (!sweetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sweet not found with id " + id);
        }
        sweetRepository.deleteById(id);
    }

    // ---------- USER ----------
    public SweetResponseDTO purchaseSweet(Long id) {
        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sweet not found with id " + id));

        if (sweet.getQuantity() <= 0) {
            throw new BadRequestException("Sweet out of stock");
        }

        sweet.setQuantity(sweet.getQuantity() - 1);
        return mapToDTO(sweetRepository.save(sweet));
    }

    // ---------- ADMIN ----------
    public SweetResponseDTO restockSweet(Long id, int quantity) {

        if (quantity <= 0) {
            throw new BadRequestException("Restock quantity must be greater than zero");
        }

        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sweet not found with id " + id));

        sweet.setQuantity(sweet.getQuantity() + quantity);
        return mapToDTO(sweetRepository.save(sweet));
    }

    // ---------- HELPER ----------
    private SweetResponseDTO mapToDTO(Sweet sweet) {
        return new SweetResponseDTO(
                sweet.getId(),
                sweet.getName(),
                sweet.getCategory(),
                sweet.getPrice(),
                sweet.getQuantity()
        );
    }
}
