package com.winiarski.tireshop.orders.exceptions;

public class InventoryTireNotFoundException extends RuntimeException {
    public InventoryTireNotFoundException(Long tireId) {
        super("Opona o id " + tireId + " nie została znaleziona w magazynie.");
    }
}