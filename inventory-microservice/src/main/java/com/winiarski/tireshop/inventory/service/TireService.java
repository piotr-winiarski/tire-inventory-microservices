package com.winiarski.tireshop.inventory.service;

import com.winiarski.tireshop.inventory.model.Tire;
import com.winiarski.tireshop.inventory.repository.TireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TireService {

    private final TireRepository tireRepository;

    public List<Tire> getAllTires() {
        return tireRepository.findAll();
    }

    public Tire addTire(Tire tire) {
        return tireRepository.save(tire);
    }

    public List<Tire> getTiresByBrand(String brand) {
        return tireRepository.findByBrand(brand);
    }

    public Tire getTireById(Long id) {
        return tireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tire not found with id: " + id));
    }

    public void reduceQuantity(Long id, int amount) {
        Tire tire = tireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tire not found with id: " + id));

        if(tire.getQuantity() < amount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough tires in stock");
        }
        tire.setQuantity(tire.getQuantity() - amount);
        tireRepository.save(tire);
    }
}
