package com.winiarski.tireshop.inventory.controller;

import com.winiarski.tireshop.inventory.model.Tire;
import com.winiarski.tireshop.inventory.service.TireService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tires")
@RequiredArgsConstructor
public class TireController {
    private final TireService tireService;

    @GetMapping
    public ResponseEntity<List<Tire>> getAllTires() {
        return ResponseEntity.ok(tireService.getAllTires());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tire> getTireById(@PathVariable Long id) {
        return ResponseEntity.ok(tireService.getTireById(id));
    }

    @PostMapping
    public ResponseEntity<Tire> addTire(@Valid @RequestBody Tire tire) {
        Tire savedTire = tireService.addTire(tire);
        return new ResponseEntity<>(savedTire, HttpStatus.CREATED);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Tire>> getTiresByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(tireService.getTiresByBrand(brand));
    }

    @PutMapping("/{id}/reduce-quantity")
    public ResponseEntity<Void> reduceQuantity(@PathVariable Long id, @RequestParam int amount) {
        tireService.reduceQuantity(id, amount);
        return ResponseEntity.ok().build();
    }
}
