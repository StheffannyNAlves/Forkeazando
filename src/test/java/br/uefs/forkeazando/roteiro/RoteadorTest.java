package br.uefs.forkeazando.roteiro;

import static org.junit.jupiter.api.Assertions.*;

import br.uefs.forkeazando.model.Protagonista;
import org.junit.jupiter.api.Test;

class RoteadorTest {

    private static Protagonista p(String... flags) {
        Protagonista p = new Protagonista("Teste", false, false, false,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
        for (String f : flags) p.adicionarFlag(f);
        return p;
    }

    @Test
    void proximaCenaPorTrilha() {
        assertEquals(400, Roteador.proximaCena(5, p("trilha_ic")));
        assertEquals(450, Roteador.proximaCena(5, p("trilha_dtec")));
        assertEquals(500, Roteador.proximaCena(5, p("trilha_indep")));
    }

    @Test
    void duasTrilhasGeramErroDeRota() {
        assertEquals(-1, Roteador.proximaCena(5, p("trilha_ic", "trilha_dtec")));
    }

    @Test
    void limitesDeCapitulo() {
        assertFalse(Roteador.temProximoCapitulo(-1));
        assertTrue(Roteador.temProximoCapitulo(0));
        assertTrue(Roteador.temProximoCapitulo(7));
        assertFalse(Roteador.temProximoCapitulo(8));
    }
}