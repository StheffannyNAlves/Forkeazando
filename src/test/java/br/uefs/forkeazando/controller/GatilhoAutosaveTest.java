package br.uefs.forkeazando.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GatilhoAutosaveTest {

    @Test
    void naoSalvaAntesDoLimite() {
        GatilhoAutosave g = new GatilhoAutosave(5);
        for (int i = 0; i < 4; i++) g.registrarCenaConcluida();
        assertFalse(g.deveSalvar());
    }

    @Test
    void salvaNaQuintaCenaEReiniciaAContagem() {
        GatilhoAutosave g = new GatilhoAutosave(5);
        for (int i = 0; i < 5; i++) g.registrarCenaConcluida();
        assertTrue(g.deveSalvar());
        assertFalse(g.deveSalvar());
    }

    @Test
    void rejeitaLimiteInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new GatilhoAutosave(0));
    }
}