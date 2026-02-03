package com.winiarski.tireshop.inventory.repository;

import com.winiarski.tireshop.inventory.model.Tire;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class TireRepositoryTest {

    @Autowired
    private TireRepository tireRepository;

    @Test
    void shouldFindByBrand() {
        // given
        Tire tire = new Tire(null, "Pirelli", "P Zero", 245, 40, 18, 4);
        tireRepository.save(tire);

        // when
        List<Tire> result = tireRepository.findByBrand("Pirelli");

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBrand()).isEqualTo("Pirelli");
        assertThat(result.get(0).getModel()).isEqualTo("P Zero");
        assertThat(result.get(0).getWidth()).isEqualTo(245);
        assertThat(result.get(0).getProfile()).isEqualTo(40);
        assertThat(result.get(0).getRimDiameter()).isEqualTo(18);
        assertThat(result.get(0).getQuantity()).isEqualTo(4);
    }

    @Test
    void shouldReturnEmptyListWhenBrandNotFound() {
        // when
        List<Tire> result = tireRepository.findByBrand("NonExistentBrand");

        // then
        assertThat(result).isEmpty();
    }
}
