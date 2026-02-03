package com.winiarski.tireshop.inventory.service;

import com.winiarski.tireshop.inventory.model.Tire;
import com.winiarski.tireshop.inventory.repository.TireRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TireServiceTest {

    @Mock
    private TireRepository tireRepository;

    @InjectMocks
    private TireService tireService;

    @Test
    void shouldReturnAllTires() {
        // given
        Tire tire1 = new Tire(1L, "Michelin", "Pilot Sport 5", 225, 45, 17, 10);
        Tire tire2 = new Tire(2L, "Debica", "Presto", 225, 45, 17, 10);
        when(tireRepository.findAll())
                .thenReturn(List.of(tire1, tire2));

        // when
        List<Tire> result = tireService.getAllTires();

        // then
        assertThat(result).hasSize(2)
                .extracting(Tire::getBrand, Tire::getModel, Tire::getWidth, Tire::getProfile, Tire::getRimDiameter)
                .containsExactlyInAnyOrder(
                        tuple("Michelin", "Pilot Sport 5", 225, 45, 17),
                        tuple("Debica", "Presto", 225, 45, 17)
                );

        verify(tireRepository, times(1)).findAll();
    }

    @Test
    void shouldNotFailIfInventoryIsEmpty() {
        //given
        when(tireRepository.findAll())
                .thenReturn(List.of());

        // when
        List<Tire> result = tireService.getAllTires();

        // then
        assertThat(result).isEmpty();
        verify(tireRepository, times(1)).findAll();
    }

    @Test
    void shouldAddTire() {
        // given
        Tire newTire = new Tire(null, "Goodyear", "Eagle F1", 205, 55, 16, 15);
        Tire savedTire = new Tire(1L, "Goodyear", "Eagle F1", 205, 55, 16, 15);

        when(tireRepository.save(any(Tire.class))).thenReturn(savedTire);

        // when
        Tire result = tireService.addTire(newTire);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBrand()).isEqualTo("Goodyear");
        verify(tireRepository, times(1)).save(newTire);
    }

    @Test
    void shouldReturnTiresByBrand() {
        // given
        String brand = "Michelin";
        Tire tire = new Tire(1L, brand, "Pilot Sport 5", 225, 45, 17, 10);
        Tire tire2 = new Tire(1L, brand, "Pilot Sport 1", 225, 45, 17, 10);

        when(tireRepository.findByBrand(brand)).thenReturn(List.of(tire, tire2));

        // when
        List<Tire> result = tireService.getTiresByBrand(brand);

        // then
        assertThat(result)
                .hasSize(2)
                .allSatisfy(t -> {
                    assertThat(t.getBrand()).isEqualTo("Michelin");
                    assertThat(t.getWidth()).isEqualTo(225);
                    assertThat(t.getProfile()).isEqualTo(45);
                    assertThat(t.getRimDiameter()).isEqualTo(17);
                    assertThat(t.getQuantity()).isGreaterThan(0);
                });

        verify(tireRepository, times(1)).findByBrand(brand);
    }
}
