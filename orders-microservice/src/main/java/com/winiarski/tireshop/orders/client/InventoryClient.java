package com.winiarski.tireshop.orders.client;

import com.winiarski.tireshop.orders.exceptions.InventoryTireNotFoundException;
import com.winiarski.tireshop.orders.model.TireDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class InventoryClient {

    private final RestTemplate restTemplate;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;


    public TireDTO getTireById(Long tireId) {
        String url = inventoryServiceUrl + "/api/tires/" + tireId;

        try {
            ResponseEntity<TireDTO> response = restTemplate.getForEntity(url, TireDTO.class);
            return response.getBody();

        } catch (HttpClientErrorException.NotFound ex) {
            // no tires
            throw new InventoryTireNotFoundException(tireId);
        } catch (Exception ex) {
            // any other error
            throw new RuntimeException("Communication error with tire inventory: " + ex.getMessage(), ex);
        }
    }


    public void reduceStock(Long tireId, Integer quantity) {
        String url = inventoryServiceUrl + "/api/tires/" + tireId + "/reduce-quantity?amount=" + quantity;

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, null, Void.class);

        // todo: check this exception
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to decrease tire stock for tire id: " + tireId);
        }
    }
}