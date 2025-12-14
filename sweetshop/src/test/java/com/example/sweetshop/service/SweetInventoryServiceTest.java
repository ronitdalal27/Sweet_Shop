package com.example.sweetshop.service;

import com.example.sweetshop.dto.SweetResponseDTO;
import com.example.sweetshop.entity.Sweet;
import com.example.sweetshop.exception.BadRequestException;
import com.example.sweetshop.exception.ResourceNotFoundException;
import com.example.sweetshop.repository.SweetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SweetInventoryServiceTest {

    @Mock
    private SweetRepository sweetRepository;

    @InjectMocks
    private SweetService sweetService;

    @Test
    void shouldPurchaseSweetAndReduceQuantity() {

        Sweet sweet = Sweet.builder()
                .id(1L)
                .name("Kaju Katli")
                .category("Dry")
                .price(BigDecimal.valueOf(500))
                .quantity(10)
                .build();

        when(sweetRepository.findById(1L)).thenReturn(Optional.of(sweet));
        when(sweetRepository.save(any(Sweet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SweetResponseDTO response = sweetService.purchaseSweet(1L);

        assertEquals(9, response.getQuantity());
        verify(sweetRepository, times(1)).save(sweet);
    }

    @Test
    void shouldThrowExceptionWhenSweetIsOutOfStock() {

        Sweet sweet = Sweet.builder()
                .id(1L)
                .name("Rasgulla")
                .category("Milk")
                .price(BigDecimal.valueOf(20))
                .quantity(0)
                .build();

        when(sweetRepository.findById(1L)).thenReturn(Optional.of(sweet));

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> sweetService.purchaseSweet(1L)
        );

        assertEquals("Sweet out of stock", ex.getMessage());
        verify(sweetRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenSweetNotFoundDuringPurchase() {

        when(sweetRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> sweetService.purchaseSweet(1L)
        );

        assertEquals("Sweet not found with id 1", ex.getMessage());
    }

    @Test
    void shouldRestockSweetAndIncreaseQuantity() {

        Sweet sweet = Sweet.builder()
                .id(1L)
                .name("Ladoo")
                .category("Dry")
                .price(BigDecimal.valueOf(10))
                .quantity(5)
                .build();

        when(sweetRepository.findById(1L)).thenReturn(Optional.of(sweet));
        when(sweetRepository.save(any(Sweet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SweetResponseDTO response = sweetService.restockSweet(1L, 10);

        assertEquals(15, response.getQuantity());
        verify(sweetRepository, times(1)).save(sweet);
    }

    @Test
    void shouldThrowExceptionWhenRestockQuantityIsZeroOrNegative() {

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> sweetService.restockSweet(1L, 0)
        );

        assertEquals("Restock quantity must be greater than zero", ex.getMessage());
        verify(sweetRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenSweetNotFoundDuringRestock() {

        when(sweetRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> sweetService.restockSweet(1L, 5)
        );

        assertEquals("Sweet not found with id 1", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPurchasingNonExistingSweet() {

        when(sweetRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> sweetService.purchaseSweet(99L)
        );

        assertEquals("Sweet not found with id 99", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRestockingNonExistingSweet() {

        when(sweetRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> sweetService.restockSweet(99L, 10)
        );

        assertEquals("Sweet not found with id 99", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRestockQuantityIsInvalid() {

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> sweetService.restockSweet(1L, 0)
        );

        assertEquals("Restock quantity must be greater than zero", ex.getMessage());
    }

}