package com.winiarski.tireshop.orders.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Obsługa sytuacji, gdy zamówienie (Orders) nie istnieje w naszej bazie
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // 2. NOWOŚĆ: Obsługa sytuacji, gdy opony nie ma w Magazynie (Inventory)
    // To łapie Twój nowy wyjątek rzucony przez InventoryClient
    @ExceptionHandler(InventoryTireNotFoundException.class)
    public ResponseEntity<String> handleInventoryTireNotFound(InventoryTireNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY) // Lub 404, ale 422 lepiej pasuje do "błędu walidacji zamówienia"
                .body("Błąd zamówienia: " + ex.getMessage());
    }

    // 3. Błąd połączenia (Inventory leży)
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<String> handleConnectionError(ResourceAccessException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("System magazynowy jest chwilowo niedostępny. Spróbuj później.");
    }

    // 4. Inne błędy HTTP z Magazynu (np. 500 Internal Server Error z Inventory, albo 400 Bad Request)
    // UWAGA: Nie zakładamy już tutaj, że to "brak opony", bo brak opony obsłużyliśmy wyżej!
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientError(HttpClientErrorException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY) // 502 Bad Gateway - bo inny serwis zwrócił błąd
                .body("Magazyn zwrócił błąd techniczny: " + ex.getStatusCode() + " - " + ex.getStatusText());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Wystąpił nieoczekiwany błąd serwera: " + ex.getMessage());
    }
}