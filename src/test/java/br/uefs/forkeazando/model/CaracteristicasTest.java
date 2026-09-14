package br.uefs.forkeazando.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CaracteristicasTest {

    @Test
    void deveArmazenarCaracteristicasCorretamente() {
        CaracteristicasProtagonista caracteristicas =
                new CaracteristicasProtagonista(true, false, true);

        assertTrue(caracteristicas.isPerfeccionista());
        assertFalse(caracteristicas.isSociavel());
        assertTrue(caracteristicas.isEstudoTeorico());
    }

    @Test
    void deveArmazenarCaracteristicasFalsasCorretamente() {
        CaracteristicasProtagonista caracteristicas =
                new CaracteristicasProtagonista(false, true, false);

        assertFalse(caracteristicas.isPerfeccionista());
        assertTrue(caracteristicas.isSociavel());
        assertFalse(caracteristicas.isEstudoTeorico());
    }
}