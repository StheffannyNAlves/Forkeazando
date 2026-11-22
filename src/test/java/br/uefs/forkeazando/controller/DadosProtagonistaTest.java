package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Protagonista;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DadosProtagonistaTest {
    private static DadosProtagonista dados(String nome, IdentidadeProtagonista identidade) {
        return new DadosProtagonista(nome, identidade, true, false, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
    }

    @Test
    void criaProtagonistaComNomeSemEspacosNasPontas() {
        Protagonista p = dados("  Tiana  ", IdentidadeProtagonista.STHEFFANNY).criarProtagonista();

        assertEquals("Tiana", p.getNome(), "O nome deve vir sem espaços nas pontas");
    }

    @Test
    void nomeEmBrancoOuNuloERejeitado() {
        assertThrows(IllegalArgumentException.class, () -> dados("   ", IdentidadeProtagonista.DAVI));
        assertThrows(IllegalArgumentException.class, () -> dados(null, IdentidadeProtagonista.DAVI));
    }

    @Test
    void identidadeNulaERejeitadaComMensagemParaOJogador() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dados("Miles", null));
        assertTrue(ex.getMessage().contains("Tiana ou Miles"));
    }

    @Test
    void caracteristicasIniciaisNulasSaoRejeitadas() {
        assertThrows(IllegalArgumentException.class, () -> new DadosProtagonista("Miles",
                IdentidadeProtagonista.DAVI, false, false, false,
                null, Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL));
    }
}
