package br.uefs.forkeazando.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PersonagemSecundarioTest {
    @Test
    void personagemDeveSerIgualAElePropio() {
        CaracteristicaPersonagemSecundario p = new CaracteristicaPersonagemSecundario("Teste", CaracteristicaPersonagemSecundario.Interesse.ESTABILIDADE);
        assertEquals(p, p);
    }

    @Test
    void doisPersonagensDiferentesNuncaSaoIguais() {
        CaracteristicaPersonagemSecundario p1 = new CaracteristicaPersonagemSecundario("A", CaracteristicaPersonagemSecundario.Interesse.ESTABILIDADE);
        CaracteristicaPersonagemSecundario p2 = new CaracteristicaPersonagemSecundario("A", CaracteristicaPersonagemSecundario.Interesse.ESTABILIDADE);
        assertNotEquals(p1, p2);
    }

    @Test
    void idsDevemSerSequenciaisSemRepetir() {
        CaracteristicaPersonagemSecundario p1 = new CaracteristicaPersonagemSecundario("A", CaracteristicaPersonagemSecundario.Interesse.ESTABILIDADE);
        CaracteristicaPersonagemSecundario p2 = new CaracteristicaPersonagemSecundario("B", CaracteristicaPersonagemSecundario.Interesse.ESTABILIDADE);
        assertEquals(p1.getId() + 1, p2.getId());
    }
}
