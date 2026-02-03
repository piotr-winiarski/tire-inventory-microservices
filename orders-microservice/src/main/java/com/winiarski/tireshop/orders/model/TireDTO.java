package com.winiarski.tireshop.orders.model;

import lombok.Data;

@Data
public class TireDTO {
    private String brand;
    private String model;
    private Integer width;
    private Integer profile;
    private Integer rimDiameter;
}