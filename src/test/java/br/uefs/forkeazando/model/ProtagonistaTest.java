package br.uefs.forkeazando.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
// REVER
class ProtagonistaTest {

    private Protagonista protagonista;

    @BeforeEach
    void setUp() {
        protagonista = new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        );
    }

    @Test
    void deveIniciarRelacionamentoComValorZero() {
        float relacionamento = protagonista.getRelacionamento(
                PersonagemSecundario.BIA
        );

        assertEquals(0f, relacionamento, 0.001f);
    }

    @Test
    void deveAlterarRelacionamentoPositivamente() {
        protagonista.alterarRelacionamento(
                PersonagemSecundario.BIA, 2f
        );

        assertEquals(
                2f,
                protagonista.getRelacionamento(PersonagemSecundario.BIA),
                0.001f
        );
    }

    @Test
    void deveAcumularAlteracoesDeRelacionamento() {
        protagonista.alterarRelacionamento(
                PersonagemSecundario.BIA, 2f
        );

        protagonista.alterarRelacionamento(
                PersonagemSecundario.BIA, -3.5f
        );

        assertEquals(
                -1.5f,
                protagonista.getRelacionamento(PersonagemSecundario.BIA),
                0.001f
        );
    }

    @Test
    void deveSomarValoresPositivosNaMesmaChave() {
        protagonista.alterarRelacionamento(
                PersonagemSecundario.BIA, 2f
        );

        protagonista.alterarRelacionamento(
                PersonagemSecundario.BIA, 1.5f
        );

        assertEquals(
                3.5f,
                protagonista.getRelacionamento(PersonagemSecundario.BIA),
                0.001f
        );
    }

    @Test
    void devePermitirAlteracaoNegativaNoRelacionamento() {
        protagonista.alterarRelacionamento(
                PersonagemSecundario.BIA, -2f
        );

        assertEquals(
                -2f,
                protagonista.getRelacionamento(PersonagemSecundario.BIA),
                0.001f
        );
    }

    @Test
    void deveManterRelacionamentosIndependentes() {
        protagonista.alterarRelacionamento(
                PersonagemSecundario.BIA, 3f
        );

        protagonista.alterarRelacionamento(
                PersonagemSecundario.VETERANO, -2f
        );

        assertEquals(
                3f,
                protagonista.getRelacionamento(PersonagemSecundario.BIA),
                0.001f
        );

        assertEquals(
                -2f,
                protagonista.getRelacionamento(PersonagemSecundario.VETERANO),
                0.001f
        );
    }

    @Test
    void deveManterRelacionamentoNaoAlteradoEmZero() {
        protagonista.alterarRelacionamento(
                PersonagemSecundario.BIA, 3f
        );

        assertEquals(
                0f,
                protagonista.getRelacionamento(PersonagemSecundario.VETERANO),
                0.001f
        );
    }

    @Test
    void deveInicializarAtributosCorretamente() {
        assertEquals("Stheffanny", protagonista.getNome());
        assertEquals(3, protagonista.getConfianca());
        assertEquals(3, protagonista.getVidaSocial());
        assertEquals(5, protagonista.getCarisma());
        assertEquals(7, protagonista.getLogica());
        assertEquals(5, protagonista.getSanidade());
        assertEquals(60, protagonista.getEnergiaMaxima());
        assertEquals(60, protagonista.getEnergia());
    }

    @Test
    void deveUsarCalouroQuandoNomeForNulo() {
        Protagonista protagonista = new Protagonista(
                null,
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        );

        assertEquals("Calouro", protagonista.getNome());
    }

    @Test
    void deveUsarCalouroQuandoNomeForVazio() {
        Protagonista protagonista = new Protagonista(
                "   ",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        );

        assertEquals("Calouro", protagonista.getNome());
    }

    @Test
    void deveRemoverEspacosDasExtremidadesDoNome() {
        Protagonista protagonista = new Protagonista(
                "  Ana  ",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        );

        assertEquals("Ana", protagonista.getNome());
    }

    @Test
    void deveInicializarValoresMinimos() {
        Protagonista protagonista = new Protagonista(
                "Calouro",
                false,
                false,
                false,
                Protagonista.NivelVidaSocial.ISOLADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEGATIVA,
                Protagonista.SituacaoEconomica.APERTADA
        );

        assertEquals(1, protagonista.getConfianca());
        assertEquals(1, protagonista.getVidaSocial());
        assertEquals(3, protagonista.getCarisma());
        assertEquals(3, protagonista.getLogica());
        assertEquals(3, protagonista.getSanidade());
        assertEquals(35, protagonista.getEnergiaMaxima());
        assertEquals(35, protagonista.getEnergia());
    }

    @Test
    void deveInicializarValoresMaximos() {
        Protagonista protagonista = new Protagonista(
                "Calouro",
                true,
                true,
                true,
                Protagonista.NivelVidaSocial.AMIGUEIRO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.POSITIVA,
                Protagonista.SituacaoEconomica.ELITE
        );

        assertEquals(5, protagonista.getConfianca());
        assertEquals(5, protagonista.getVidaSocial());
        assertEquals(5, protagonista.getCarisma());
        assertEquals(7, protagonista.getLogica());
        assertEquals(7, protagonista.getSanidade());
        assertEquals(100, protagonista.getEnergiaMaxima());
        assertEquals(100, protagonista.getEnergia());
    }

    @Test
    void deveGerarFlagsDeOrigemCorretamente() {
        assertTrue(protagonista.temFlag("perfil_relaxado"));
        assertTrue(protagonista.temFlag("perfil_sociavel"));
        assertTrue(protagonista.temFlag("perfil_teorico"));
        assertTrue(protagonista.temFlag("origem_estavel"));
        assertTrue(protagonista.temFlag("origem_em_neutra"));
        assertTrue(protagonista.temFlag("origem_equilibrado"));
    }


    // TESTES getRank()

    @Test
    void deveRetornarCalouroPerdidoNoScoreInicial() {
        assertEquals("Calouro Perdido", protagonista.getRank());
    }
    @Test
    void deveRetornarCalouroPerdidoNoUltimoValorDaPrimeiraFaixa() {
        protagonista.ganharScore(9);

        assertEquals("Calouro Perdido", protagonista.getRank());
    }

    @Test
    void deveRetornarEstagiarioDoCaosNoPrimeiroValorDaSegundaFaixa() {
        protagonista.ganharScore(10);

        assertEquals("Estagiário do Caos", protagonista.getRank());
    }

    @Test
    void deveRetornarEstagiarioDoCaosNoUltimoValorDaSegundaFaixa() {
        protagonista.ganharScore(24);

        assertEquals("Estagiário do Caos", protagonista.getRank());
    }

    @Test
    void deveRetornarSobreviventeDoPrimeiroSemestreNoPrimeiroValorDaTerceiraFaixa() {
        protagonista.ganharScore(25);

        assertEquals("Sobrevivente do 1º Semestre", protagonista.getRank());
    }

    @Test
    void deveRetornarSobreviventeDoPrimeiroSemestreNoUltimoValorDaTerceiraFaixa() {
        protagonista.ganharScore(49);

        assertEquals("Sobrevivente do 1º Semestre", protagonista.getRank());
    }

    @Test
    void deveRetornarVeteranoCascaGrossaNoPrimeiroValorDaQuartaFaixa() {
        protagonista.ganharScore(50);

        assertEquals("Veterano Casca Grossa", protagonista.getRank());
    }

    @Test
    void deveRetornarVeteranoCascaGrossaNoUltimoValorDaQuartaFaixa() {
        protagonista.ganharScore(74);

        assertEquals("Veterano Casca Grossa", protagonista.getRank());
    }

    @Test
    void deveRetornarLendaDoCampusNoPrimeiroValorDaUltimaFaixa() {
        protagonista.ganharScore(75);

        assertEquals("Lenda do Campus", protagonista.getRank());
    }

    @Test
    void deveRetornarLendaDoCampusNoScoreMaximo() {
        protagonista.ganharScore(100);

        assertEquals("Lenda do Campus", protagonista.getRank());
    }


    // limites dos atributos
    @Test
    void deveLimitarAtributosMinimosEmZero() {
        protagonista.alterarConfianca(-10);
        protagonista.alterarVidaSocial(-10);
        protagonista.alterarCarisma(-10);
        protagonista.alterarLogica(-10);
        protagonista.alterarSanidade(-10);

        assertEquals(0, protagonista.getConfianca());
        assertEquals(0, protagonista.getVidaSocial());
        assertEquals(0, protagonista.getCarisma());
        assertEquals(0, protagonista.getLogica());
        assertEquals(0, protagonista.getSanidade());
    }

    @Test
    void deveLimitarAtributosMaximosEmDez() {
        protagonista.alterarConfianca(20);
        protagonista.alterarVidaSocial(20);
        protagonista.alterarCarisma(20);
        protagonista.alterarLogica(20);
        protagonista.alterarSanidade(20);

        assertEquals(10, protagonista.getConfianca());
        assertEquals(10, protagonista.getVidaSocial());
        assertEquals(10, protagonista.getCarisma());
        assertEquals(10, protagonista.getLogica());
        assertEquals(10, protagonista.getSanidade());
    }

    // energia
    @Test
    void deveGastarEnergia() {
        protagonista.gastarEnergia(20);

        assertEquals(40, protagonista.getEnergia());
    }

    @Test
    void deveLimitarEnergiaMinimaEmZero() {
        protagonista.gastarEnergia(1000);

        assertEquals(0, protagonista.getEnergia());
    }

    @Test
    void deveLimitarEnergiaMaximaAoRecuperar() {
        protagonista.gastarEnergia(20);
        protagonista.alterarAtributo("energia", 1000);

        assertEquals(
                protagonista.getEnergiaMaxima(),
                protagonista.getEnergia()
        );
    }

    // alteracao de atributo
    @Test
    void deveAlterarConfiancaPelaChave() {
        protagonista.alterarAtributo("confianca", 2);

        assertEquals(5, protagonista.getConfianca());
    }

    @Test
    void deveAlterarVidaSocialPelaChave() {
        protagonista.alterarAtributo("vidaSocial", 2);

        assertEquals(5, protagonista.getVidaSocial());
    }

    @Test
    void deveAlterarCarismaPelaChave() {
        protagonista.alterarAtributo("carisma", 2);

        assertEquals(7, protagonista.getCarisma());
    }

    @Test
    void deveAlterarLogicaPelaChave() {
        protagonista.alterarAtributo("logica", 2);

        assertEquals(9, protagonista.getLogica());
    }

    @Test
    void deveAlterarSanidadePelaChave() {
        protagonista.alterarAtributo("sanidade", 2);

        assertEquals(7, protagonista.getSanidade());
    }

    @Test
    void deveAlterarScorePelaChave() {
        protagonista.alterarAtributo("score", 20);

        assertEquals(20, protagonista.getScore());
    }

    @Test
    void deveAlterarParticipacaoPelaChave() {
        protagonista.alterarAtributo("participacao", 20);

        assertEquals(20, protagonista.getParticipacao());
    }

    @Test
    void deveAlterarEnergiaPelaChave() {
        protagonista.alterarAtributo("energia", -20);

        assertEquals(40, protagonista.getEnergia());
    }

    @Test
    void deveLancarExcecaoParaAtributoDesconhecido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> protagonista.alterarAtributo("atributoInexistente", 5)
        );
    }

    // verificacao das flags
    @Test
    void deveAdicionarFlagSemDuplicar() {
        protagonista.adicionarFlag("teste");
        protagonista.adicionarFlag("teste");

        assertEquals(
                1,
                protagonista.getHistoricoFlags().stream()
                        .filter(flag -> flag.equals("teste"))
                        .count()
        );
    }

    @Test
    void deveImpedirAlteracaoExternaDasFlags() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> protagonista.getHistoricoFlags().add("flag_invalida")
        );
    }

    @Test
    void deveIdentificarSePossuiFlag() {
        protagonista.adicionarFlag("teste");

        assertTrue(protagonista.temFlag("teste"));
        assertFalse(protagonista.temFlag("outra_flag"));
    }

    // atingir requisito
    @Test
    void deveAtingirRequisitoQuandoEstiverExatamenteNosMinimos() {
        protagonista.ganharScore(50);
        protagonista.ganharParticipacao(30);

        assertTrue(
                protagonista.atingiuRequisito(50, 30)
        );
    }

    @Test
    void naoDeveAtingirRequisitoQuandoScoreEstiverAbaixoDoMinimo() {
        protagonista.ganharScore(49);
        protagonista.ganharParticipacao(30);

        assertFalse(
                protagonista.atingiuRequisito(50, 30)
        );
    }

    @Test
    void naoDeveAtingirRequisitoQuandoParticipacaoEstiverAbaixoDoMinimo() {
        protagonista.ganharScore(50);
        protagonista.ganharParticipacao(29);

        assertFalse(
                protagonista.atingiuRequisito(50, 30)
        );
    }

    @Test
    void naoDeveAtingirRequisitoQuandoAmbosEstiveremAbaixoDoMinimo() {
        protagonista.ganharScore(49);
        protagonista.ganharParticipacao(29);

        assertFalse(
                protagonista.atingiuRequisito(50, 30)
        );
    }

    // categorização de relacionamento
    @Test
    void deveCategorizarRelacionamentoComoAliadoNoLimiteSuperior() {
        assertEquals(
                Protagonista.NivelRelacionamento.ALIADO,
                Protagonista.categorizarRelacionamento(3f)
        );
    }

    @Test
    void deveCategorizarRelacionamentoComoColegaAbaixoDoLimiteSuperior() {
        assertEquals(
                Protagonista.NivelRelacionamento.COLEGA,
                Protagonista.categorizarRelacionamento(2.999f)
        );
    }

    @Test
    void deveCategorizarRelacionamentoComoRivalNoLimiteInferior() {
        assertEquals(
                Protagonista.NivelRelacionamento.RIVAL,
                Protagonista.categorizarRelacionamento(-3f)
        );
    }

    @Test
    void deveCategorizarRelacionamentoComoColegaAcimaDoLimiteInferior() {
        assertEquals(
                Protagonista.NivelRelacionamento.COLEGA,
                Protagonista.categorizarRelacionamento(-2.999f)
        );
    }

}