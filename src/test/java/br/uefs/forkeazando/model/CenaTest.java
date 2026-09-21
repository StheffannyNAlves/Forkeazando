package br.uefs.forkeazando.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CenaTest {

    @Test
    void cenaSemEscolhasDeveRetornarListaVazia() {
        Cena cena = new Cena(0, "Capítulo 1", "Narrador", "teste teste");

        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                CaracteristicasProtagonista.SituacaoEconomica.ESTAVEL
        );
        Protagonista protagonista = new Protagonista(caracteristicas);

        assertTrue(cena.getEscolhasDisposniveis(protagonista).isEmpty());
    }

    @Test
    void cenaDeveFiltrarEscolhaComRequisitoNaoCumprido() {
        Cena cena = new Cena(0, "Capítulo 1", "Narrador", "teste teste");
        cena.adicionarEscolha(new Escolha.Builder("opção livre", 1).build());
        cena.adicionarEscolha(new Escolha.Builder("opção bloqueada", 2).comScoreMinimo(10).build());

        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                CaracteristicasProtagonista.SituacaoEconomica.ESTAVEL
        );
        Protagonista protagonista = new Protagonista(caracteristicas);

        List<Escolha> disponiveis = cena.getEscolhasDisposniveis(protagonista);

        assertEquals(1, disponiveis.size());
        assertEquals("opção livre", disponiveis.get(0).getTextoAlternativa());
    }
}