package com.example.sweetshop.repository;

import com.example.sweetshop.entity.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface SweetRepository extends JpaRepository<Sweet, Long> {

    List<Sweet> findByNameContainingIgnoreCase(String name);

    List<Sweet> findByCategoryIgnoreCase(String category);

    List<Sweet> findByPriceBetween(BigDecimal min, BigDecimal max);

    List<Sweet> findByPriceGreaterThanEqual(BigDecimal min);

    List<Sweet> findByPriceLessThanEqual(BigDecimal max);

}
