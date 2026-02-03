package com.winiarski.tireshop.inventory.repository;

import com.winiarski.tireshop.inventory.model.Tire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TireRepository extends JpaRepository<Tire, Long> {
    List<Tire> findByBrand(String brand);
}
