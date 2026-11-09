package br.uefs.forkeazando.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonagemSecundarioTest {

    @Test
    void porCodigoDevolveAMesmaInstanciaDaConstante() {
        assertSame(PersonagemSecundario.BIA, PersonagemSecundario.porCodigo("BIA"));
    }

    @Test
    void porCodigoResolveTodosOsPersonagensDoJogo() {
        assertSame(PersonagemSecundario.VETERANO, PersonagemSecundario.porCodigo("VETERANO"));
        assertSame(PersonagemSecundario.ORIENTADOR_IC, PersonagemSecundario.porCodigo("ORIENTADOR_IC"));
        assertSame(PersonagemSecundario.PROFESSOR_DTEC, PersonagemSecundario.porCodigo("PROFESSOR_DTEC"));
        assertSame(PersonagemSecundario.PROFESSOR_PBL, PersonagemSecundario.porCodigo("PROFESSOR_PBL"));
        assertSame(PersonagemSecundario.DANDARA, PersonagemSecundario.porCodigo("DANDARA"));
        assertSame(PersonagemSecundario.ZE_MODULO8, PersonagemSecundario.porCodigo("ZE_MODULO8"));
        assertSame(PersonagemSecundario.SISTEMA, PersonagemSecundario.porCodigo("SISTEMA"));
        assertSame(PersonagemSecundario.ALAN, PersonagemSecundario.porCodigo("ALAN"));
        assertSame(PersonagemSecundario.MALU, PersonagemSecundario.porCodigo("MALU"));
    }

    @Test
    void codigoInexistenteLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> PersonagemSecundario.porCodigo("NAO_EXISTE"));
    }

    @Test
    void getCodigoBateComACriacao() {
        assertEquals("BIA", PersonagemSecundario.BIA.getCodigo());
    }
}