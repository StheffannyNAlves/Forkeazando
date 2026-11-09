package br.uefs.forkeazando.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ConquistaTest {

    private Protagonista protagonista;

    @BeforeEach
    void setUp() {
        protagonista = new Protagonista(
                "Teste",
                false,
                false,
                false,
                Protagonista.NivelVidaSocial.ISOLADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        );
    }

    @Test
    void deveAtingirConquistaQuandoPossuiFlag() {
        protagonista.adicionarFlag("cap3_concluido");

        assertTrue(Conquista.PRIMEIRO_PASSO.atingida(protagonista));
    }

    @Test
    void naoDeveAtingirConquistaQuandoNaoPossuiFlag() {
        assertFalse(Conquista.PRIMEIRO_PASSO.atingida(protagonista));
    }

    @Test
    void amigoDoCarameloExigeAmizadeEConclusao() {
        protagonista.adicionarFlag("amigo_do_caramelo");
        assertFalse(Conquista.AMIGO_DO_CARAMELO.atingida(protagonista));

        protagonista.adicionarFlag("final_formou");
        assertTrue(Conquista.AMIGO_DO_CARAMELO.atingida(protagonista));
    }

    @Test
    void protagonistaNovoNaoDeveAtingirConquistas() {
        for (Conquista conquista : Conquista.values()) {
            assertFalse(conquista.atingida(protagonista), "Conquista atingida inesperadamente: " + conquista);
        }
    }

    @Test
    void deveEncontrarConquistaPeloCodigo() {
        assertEquals(Conquista.PRIMEIRO_PASSO, Conquista.porCodigo("primeiro_passo").orElseThrow());
    }

    @Test
    void deveDesbloquearSobreviventeQuandoTemFlagDaGreve(){
        protagonista.adicionarFlag("hab_sobreviveu_greve");
        assertTrue(Conquista.SOBREVIVENTE.atingida(protagonista));
    }

    @Test
    void naoDeveDesbloquearSobreviventeQuandoNaoTemFlagDaGreve(){
        assertFalse(Conquista.SOBREVIVENTE.atingida(protagonista));
    }

    @Test
    void deveDesbloquearMarmitaCompartilhadaQuandoTemFlagDeDividirComida(){
        protagonista.adicionarFlag("cap5b_dividiu_comida");
        assertTrue(Conquista.MARMITA_COMPARTILHADA.atingida(protagonista));
    }

    @Test
    void naoDeveDesbloquearMarmitaCompartilhadaQuandoNaoTemFlagDeDividirComida(){
        assertFalse(Conquista.MARMITA_COMPARTILHADA.atingida(protagonista));
    }

    @Test
    void deveDesbloquearSemMascaraQuandoTemFlagDeRespostaVerdadeira(){
        protagonista.adicionarFlag("cap8_resposta_verdadeira");
        assertTrue(Conquista.SEM_MASCARA.atingida(protagonista));
    }

    @Test
    void naoDeveDesbloquearSemMascaraQuandoNaoTemFlagDeRespostaVerdadeira(){
        assertFalse(Conquista.SEM_MASCARA.atingida(protagonista));

    }

    @Test
    void deveDesbloquearAmigoDoCarameloQuandoTemAsDuasFlags() {
        protagonista.adicionarFlag("amigo_do_caramelo");
        protagonista.adicionarFlag("final_formou");

        assertTrue(Conquista.AMIGO_DO_CARAMELO.atingida(protagonista));
    }

    @Test
    void naoDeveDesbloquearAmigoDoCarameloQuandoSoTemFlagDeAmizade() {
        protagonista.adicionarFlag("amigo_do_caramelo");
        assertFalse(Conquista.AMIGO_DO_CARAMELO.atingida(protagonista));
    }
    @Test
    void naoDeveDesbloquearRedeDeApoioQuandoNaoTemAliados() {
        assertFalse(Conquista.REDE_DE_APOIO.atingida(protagonista));
    }

    @Test
    void naoDeveDesbloquearRedeDeApoioQuandoTemDoisAliados() {
        protagonista.alterarRelacionamento(PersonagemSecundario.MALU, 3);
        protagonista.alterarRelacionamento(PersonagemSecundario.VETERANO, 3);
        assertFalse(Conquista.REDE_DE_APOIO.atingida(protagonista));
    }

    @Test
    void deveDesbloquearRedeDeApoioQuandoTemTresAliados() {
        protagonista.alterarRelacionamento(PersonagemSecundario.MALU, 3);
        protagonista.alterarRelacionamento(PersonagemSecundario.VETERANO, 3);
        protagonista.alterarRelacionamento(PersonagemSecundario.DANDARA, 3);

        assertTrue(Conquista.REDE_DE_APOIO.atingida(protagonista));
    }






















    @Test
    void deveRetornarVazioParaCodigoDesconhecidoOuNulo() {
        assertTrue(Conquista.porCodigo("nao_existe").isEmpty());
        assertTrue(Conquista.porCodigo(null).isEmpty());
    }

    @Test
    void codigosDevemSerUnicosETitulosEDescricoesNaoVazios() {
        Set<String> codigos = new HashSet<>();

        for (Conquista conquista : Conquista.values()) {
            assertTrue(
                    codigos.add(conquista.getCodigo()),
                    "Código duplicado: " + conquista.getCodigo()
            );

            assertNotNull(conquista.getTitulo());
            assertFalse(conquista.getTitulo().isBlank());

            assertNotNull(conquista.getDescricao());
            assertFalse(conquista.getDescricao().isBlank());
        }
    }
}
