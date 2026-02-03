package com.winiarski.tireshop.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winiarski.tireshop.inventory.model.Tire;
import com.winiarski.tireshop.inventory.service.TireService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TireService tireService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllTires() throws Exception {
        // given
        Tire tire = new Tire(1L, "Michelin", "Pilot Sport 5", 225, 45, 17, 10);
        when(tireService.getAllTires()).thenReturn(List.of(tire));

        // when & then
        mockMvc.perform(get("/api/tires"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].brand").value("Michelin"))
                .andExpect(jsonPath("$[0].model").value("Pilot Sport 5"))
                .andExpect(jsonPath("$[0].width").value(225))
                .andExpect(jsonPath("$[0].profile").value(45))
                .andExpect(jsonPath("$[0].rimDiameter").value(17))
                .andExpect(jsonPath("$[0].quantity").value(greaterThan(0)));
    }

    @Test
    void shouldNotErrorIfInventoryEmpty() throws Exception {
        // given
        when(tireService.getAllTires()).thenReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/tires"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldAddNewTire() throws Exception {
        // given
        Tire newTire = new Tire(null, "Debica", "Presto", 195, 65, 15, 50);
        Tire savedTire = new Tire(3L, "Debica", "Presto", 195, 65, 15, 50);

        when(tireService.addTire(any(Tire.class))).thenReturn(savedTire);

        // when
        mockMvc.perform(post("/api/tires")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTire)))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.brand").value("Debica"));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        // given - Opona bez marki (walidacja @NotBlank w modelu)
        Tire invalidTire = new Tire(null, "", "Presto", 195, 65, 15, 50);

        // when & then
        mockMvc.perform(post("/api/tires")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTire)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetTireByBrand() throws Exception {
        // given
        String brand = "Michelin";
        Tire tire = new Tire(1L, brand, "Pilot Sport 5", 225, 45, 17, 10);
        when(tireService.getTiresByBrand(brand)).thenReturn(List.of(tire));

        // when & then
        mockMvc.perform(get("/api/tires/brand/" + brand))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].brand").value(brand));
    }
}