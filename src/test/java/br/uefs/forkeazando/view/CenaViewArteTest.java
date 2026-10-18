package br.uefs.forkeazando.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CenaViewArteTest {

    @Test
    void arteSoCabeSeOTerminalForLargoEAltoOSuficiente() {
        assertTrue(CenaView.arteCabe(66, 30));
        assertTrue(CenaView.arteCabe(120, 50));
        assertFalse(CenaView.arteCabe(65, 40), "uma coluna a menos quebraria a caixa");
        assertFalse(CenaView.arteCabe(100, 29));
        assertFalse(CenaView.arteCabe(0, 0), "terminal sem tamanho (não-tty)");
    }
}
