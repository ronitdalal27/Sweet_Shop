package com.example.sweetshop.service;

import com.example.sweetshop.dto.SweetRequestDTO;
import com.example.sweetshop.dto.SweetResponseDTO;
import com.example.sweetshop.entity.Sweet;
import com.example.sweetshop.exception.ResourceNotFoundException;
import com.example.sweetshop.repository.SweetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SweetServiceTest {

    @Mock
    private SweetRepository sweetRepository;

    @InjectMocks
    private SweetService sweetService;

    @Test
    void shouldAddNewSweet() {

        SweetRequestDTO request = new SweetRequestDTO();
        request.setName("Ladoo");
        request.setCategory("Indian");
        request.setPrice(BigDecimal.valueOf(10));
        request.setQuantity(100);

        Sweet savedEntity = Sweet.builder()
                .id(1L)
                .name("Ladoo")
                .category("Indian")
                .price(BigDecimal.valueOf(10))
                .quantity(100)
                .build();

        when(sweetRepository.save(any(Sweet.class))).thenReturn(savedEntity);

        SweetResponseDTO response = sweetService.addSweet(request);

        assertEquals("Ladoo", response.getName());
        assertEquals("Indian", response.getCategory());
        assertEquals(100, response.getQuantity());
    }


    @Test
    void shouldReturnAllSweets() {

        when(sweetRepository.findAll()).thenReturn(List.of(
                Sweet.builder()
                        .id(1L)
                        .name("Ladoo")
                        .category("Indian")
                        .price(BigDecimal.valueOf(10))
                        .quantity(20)
                        .build(),
                Sweet.builder()
                        .id(2L)
                        .name("Barfi")
                        .category("Indian")
                        .price(BigDecimal.valueOf(15))
                        .quantity(30)
                        .build()
        ));

        List<SweetResponseDTO> result = sweetService.getAllSweets();

        assertEquals(2, result.size());
    }


    @Test
    void shouldSearchSweetsByName() {

        when(sweetRepository.findByNameContainingIgnoreCase("lad"))
                .thenReturn(List.of(
                        Sweet.builder()
                                .id(1L)
                                .name("Ladoo")
                                .category("Indian")
                                .price(BigDecimal.valueOf(50))
                                .quantity(10)
                                .build()
                ));

        List<SweetResponseDTO> result = sweetService.searchByName("lad");

        assertEquals(1, result.size());
        assertEquals("Ladoo", result.get(0).getName());
    }

    @Test
    void shouldSearchSweetsByCategory() {

        when(sweetRepository.findByCategoryIgnoreCase("bengali"))
                .thenReturn(List.of(
                        Sweet.builder()
                                .id(1L)
                                .name("Rasgulla")
                                .category("Bengali")
                                .price(BigDecimal.valueOf(30))
                                .quantity(20)
                                .build()
                ));

        List<SweetResponseDTO> result = sweetService.searchByCategory("bengali");

        assertEquals(1, result.size());
        assertEquals("Rasgulla", result.get(0).getName());
    }

    @Test
    void shouldSearchSweetsByPriceRange() {

        when(sweetRepository.findByPriceBetween(
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(30)
        )).thenReturn(List.of(
                Sweet.builder()
                        .id(1L)
                        .name("Ladoo")
                        .category("Indian")
                        .price(BigDecimal.valueOf(20))
                        .quantity(10)
                        .build()
        ));

        List<SweetResponseDTO> result =
                sweetService.searchByPriceRange(
                        BigDecimal.valueOf(10),
                        BigDecimal.valueOf(30)
                );

        assertEquals(1, result.size());
        assertEquals("Ladoo", result.get(0).getName());
    }

    @Test
    void shouldUpdateSweetSuccessfully() {

        Sweet existing = Sweet.builder()
                .id(1L)
                .name("Old")
                .category("OldCat")
                .price(BigDecimal.valueOf(10))
                .quantity(5)
                .build();

        SweetRequestDTO request = new SweetRequestDTO();
        request.setName("New");
        request.setCategory("NewCat");
        request.setPrice(BigDecimal.valueOf(20));
        request.setQuantity(10);

        when(sweetRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(sweetRepository.save(any(Sweet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SweetResponseDTO response = sweetService.updateSweet(1L, request);

        assertEquals("New", response.getName());
        assertEquals(10, response.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingSweet() {

        SweetRequestDTO request = new SweetRequestDTO();
        request.setName("New");
        request.setCategory("Cat");
        request.setPrice(BigDecimal.valueOf(20));
        request.setQuantity(10);

        when(sweetRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> sweetService.updateSweet(99L, request)
        );

        assertEquals("Sweet not found with id 99", ex.getMessage());
    }


    @Test
    void shouldDeleteSweetSuccessfully() {

        when(sweetRepository.existsById(1L)).thenReturn(true);

        sweetService.deleteSweet(1L);

        verify(sweetRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingSweet() {

        when(sweetRepository.existsById(99L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> sweetService.deleteSweet(99L)
        );

        assertEquals("Sweet not found with id 99", ex.getMessage());
    }
}
