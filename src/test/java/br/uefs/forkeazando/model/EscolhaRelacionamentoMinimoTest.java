package br.uefs.forkeazando.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EscolhaRelacionamentoMinimoTest {

    private static Protagonista protagonista() {
        return new Protagonista(
                "Teste", false, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
    }

    private static Escolha exigeAlianca() {
        return new Escolha.Builder("ajuda do Caio", 1)
                .comRelacionamentoMinimo(PersonagemSecundario.VETERANO, 3f).build();
    }

    @Test
    void ficaIndisponivelAteARelacaoChegarAoMinimo() {
        Protagonista p = protagonista();
        Escolha e = exigeAlianca();

        assertFalse(e.estaDisponivel(p));
        p.alterarRelacionamento(PersonagemSecundario.VETERANO, 2f);
        assertFalse(e.estaDisponivel(p));
        p.alterarRelacionamento(PersonagemSecundario.VETERANO, 1f);
        assertTrue(e.estaDisponivel(p));
    }

    @Test
    void relacaoComOutroPersonagemNaoLibera() {
        Protagonista p = protagonista();
        p.alterarRelacionamento(PersonagemSecundario.DANDARA, 5f);

        assertFalse(exigeAlianca().estaDisponivel(p));
    }

    @Test
    void ignorarEnergiaNaoIgnoraRelacionamento() {
        Protagonista p = protagonista();
        p.gastarEnergia(p.getEnergiaMaxima());
        Escolha e = new Escolha.Builder("ajuda cara", 1).comCustoEnergia(10)
                .comRelacionamentoMinimo(PersonagemSecundario.VETERANO, 3f).build();

        assertFalse(e.estaDisponivelIgnorandoEnergia(p));

        p.alterarRelacionamento(PersonagemSecundario.VETERANO, 3f);
        assertTrue(e.estaDisponivelIgnorandoEnergia(p));
        assertFalse(e.estaDisponivel(p));
    }
}   