package br.uefs.forkeazando.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstadoAplicarEscolhaTest {

    private Estado estado;
    private Protagonista p;

    @BeforeEach
    void preparar() {
        p = new Protagonista("Ana", false, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
        estado = new Estado();
        estado.iniciar(p);
    }

    private static Escolha.Builder escolha() {
        return new Escolha.Builder("x", 1);
    }

    @Test
    void aplicaScoreParticipacaoEnergiaEMarcaAlterado() {
        int energiaAntes = p.getEnergia();

        estado.aplicarEscolha(escolha().comScoreGanho(7).comParticipacaoGanha(4).comCustoEnergia(10).build());

        assertEquals(7, p.getScore());
        assertEquals(4, p.getParticipacao());
        assertEquals(energiaAntes - 10, p.getEnergia());
        assertTrue(estado.temAlteracoes());
    }

    @Test
    void concedeFlags() {
        estado.aplicarEscolha(escolha().comFlagConcedida("cap1_falou").build());

        assertTrue(p.temFlag("cap1_falou"));
    }

    @Test
    void relacionamentoRecebeBonusQuandoInteresseCoincide() {
        Escolha e = escolha()
                .comRelacionamento(PersonagemSecundario.VETERANO, 1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE)
                .build();

        List<Estado.ImpactoRelacionamento> impactos = estado.aplicarEscolha(e);

        assertEquals(1, impactos.size());
        assertEquals(2f, impactos.get(0).valor());
        assertEquals(2f, p.getRelacionamento(PersonagemSecundario.VETERANO));
    }

    @Test
    void relacionamentoSemBonusQuandoInteresseDiferente() {
        Escolha e = escolha()
                .comRelacionamento(PersonagemSecundario.VETERANO, 1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.CONHECIMENTO)
                .build();

        estado.aplicarEscolha(e);

        assertEquals(1f, p.getRelacionamento(PersonagemSecundario.VETERANO));
    }

    @Test
    void impactoInformaONivelResultante() {
        Estado.ImpactoRelacionamento impacto = estado.aplicarEscolha(
                escolha().comRelacionamento(PersonagemSecundario.BIA, 3f).build()).get(0);

        assertEquals(Protagonista.NivelRelacionamento.ALIADO, impacto.nivel());
    }

    @Test
    void alteraAtributos() {
        int logicaAntes = p.getLogica();

        estado.aplicarEscolha(escolha().comAtributo("logica", 2).build());

        assertEquals(logicaAntes + 2, p.getLogica());
    }

    @Test
    void trancamentoSoAdicionaUmSemestreExtra() {
        Escolha tranca = escolha().comFlagConcedida("cap6_trancou").build();

        estado.aplicarEscolha(tranca);
        estado.aplicarEscolha(tranca);

        assertEquals(1, estado.getSemestresExtras());
    }

    @Test
    void primeiraTrilhaConcedidaFicaRegistrada() {
        estado.aplicarEscolha(escolha().comFlagConcedida("trilha_ic").build());

        assertEquals(Estado.Trilha.IC, estado.getTrilha());
    }

    @Test
    void naoPermiteTrocarDeTrilha() {
        estado.aplicarEscolha(escolha().comFlagConcedida("trilha_ic").build());

        assertThrows(IllegalStateException.class,
                () -> estado.aplicarEscolha(escolha().comFlagConcedida("trilha_dtec").build()));
    }

    @Test
    void recuperaTrilhaDePartidaAntigaPelasFlags() {
        p.adicionarFlag("trilha_dtec");

        estado.registrarTrilhaConcedida(List.of());

        assertEquals(Estado.Trilha.DTEC, estado.getTrilha());
    }

    @Test
    void rejeitaDuasTrilhasNoMesmoHistorico() {
        p.adicionarFlag("trilha_ic");
        p.adicionarFlag("trilha_indep");

        assertThrows(IllegalStateException.class, () -> estado.registrarTrilhaConcedida(List.of()));
    }

    @Test
    void escolhaNulaELancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> estado.aplicarEscolha(null));
    }

    @Test
    void estadoNovoEIntegro() {
        assertTrue(estado.verificarIntegridade().isEmpty());
    }

    @Test
    void estadoSemProtagonistaEInvalido() {
        assertFalse(new Estado().verificarIntegridade().isEmpty());
    }
}