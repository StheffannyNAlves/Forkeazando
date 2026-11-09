package br.uefs.forkeazando.dao;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
// Testa....
class GerenciadorSaveIntegridadeTest {

    @TempDir
    Path pasta;

    private static final String PROTAGONISTA_VALIDA = """
            {"nome":"Teste","energia":40,"energiaMaxima":60,"score":10,"participacao":5,
             "confianca":3,"vidaSocial":3,"carisma":3,"logica":3,"sanidade":5,
             "historicoFlags":["perfil_relaxado"],"relacionamentos":{"BIA":1.0}}""";

    private Path gravar(String json) throws IOException {
        Path arquivo = pasta.resolve("save.json");
        Files.writeString(arquivo, json);
        return arquivo;
    }

    private static String estadoJson(String protagonista, String extras) {
        return "{\"protagonista\":" + protagonista + extras + "}";
    }

    @Test
    void saveAdulteradoMasConsistenteCarrega() throws Exception {
        Path arquivo = gravar(estadoJson(PROTAGONISTA_VALIDA, ",\"capituloAtual\":3,\"cenaAtualId\":100"));

        Estado estado = GerenciadorSave.carregar(arquivo);

        assertEquals(3, estado.getCapituloAtual());
        assertEquals("Teste", estado.getProtagonista().getNome());
    }

    @Test
    void estadoSalvoPeloJogoSempreEPassaNaValidacao() throws Exception {
        Estado estado = new Estado();
        estado.iniciar(new Protagonista("Ana", true, true, true,
                Protagonista.NivelVidaSocial.AMIGUEIRO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.POSITIVA,
                Protagonista.SituacaoEconomica.ELITE));
        Path arquivo = pasta.resolve("novo.json");

        GerenciadorSave.salvar(estado, arquivo);

        assertDoesNotThrow(() -> GerenciadorSave.carregar(arquivo));
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource(delimiter = '|', value = {
            "energia negativa           | \"energia\":40            | \"energia\":-50",
            "energia acima do máximo    | \"energia\":40            | \"energia\":999",
            "score acima de 100         | \"score\":10              | \"score\":9999",
            "atributo acima de 10       | \"carisma\":3             | \"carisma\":77",
            "nome em branco             | \"nome\":\"Teste\"        | \"nome\":\"  \"",
            "energia máxima zerada      | \"energiaMaxima\":60      | \"energiaMaxima\":0",
            "flag nula no histórico     | \"historicoFlags\":[\"perfil_relaxado\"] | \"historicoFlags\":[null]",
            "personagem desconhecido    | \"BIA\":1.0               | \"FANTASMA\":1.0"
    })
    void protagonistaInvalidaLancaDadosInvalidos(String caso, String trecho, String substituto) throws IOException {
        Path arquivo = gravar(estadoJson(PROTAGONISTA_VALIDA.replace(trecho.strip(), substituto.strip()), ""));

        assertThrows(DadosInvalidosException.class, () -> GerenciadorSave.carregar(arquivo), caso);
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource(delimiter = '|', value = {
            "capítulo inexistente     | ,\"capituloAtual\":99",
            "capítulo zero            | ,\"capituloAtual\":0",
            "cena negativa            | ,\"cenaAtualId\":-5",
            "trilha desconhecida      | ,\"trilha\":\"BANANA\"",
            "final desconhecido       | ,\"codigoFinal\":\"naoexiste\"",
            "concluída sem final      | ,\"partidaConcluida\":true",
            "semestres extras negativo| ,\"semestresExtras\":-1"
    })
    void estadoInvalidoLancaDadosInvalidos(String caso, String extras) throws IOException {
        Path arquivo = gravar(estadoJson(PROTAGONISTA_VALIDA, extras.strip()));

        assertThrows(DadosInvalidosException.class, () -> GerenciadorSave.carregar(arquivo), caso);
    }

    @Test
    void mensagemListaTodosOsProblemasEncontrados() throws IOException {
        String ruim = PROTAGONISTA_VALIDA.replace("\"energia\":40", "\"energia\":-1")
                .replace("\"score\":10", "\"score\":500");
        Path arquivo = gravar(estadoJson(ruim, ""));

        DadosInvalidosException e = assertThrows(DadosInvalidosException.class,
                () -> GerenciadorSave.carregar(arquivo));

        assertTrue(e.getMessage().contains("energia"));
        assertTrue(e.getMessage().contains("score"));
    }

    @Test
    void tipoErradoViraCarregamentoException() throws IOException {
        Path arquivo = gravar(estadoJson(PROTAGONISTA_VALIDA, ",\"capituloAtual\":\"abc\""));

        assertThrows(CarregamentoException.class, () -> GerenciadorSave.carregar(arquivo));
    }

    @Test
    void slotComSaveInvalidoApareceComoCorrompidoNaListagem() throws IOException {
        Files.writeString(GerenciadorSlots.caminhoDoSlot(pasta, 1),
                estadoJson(PROTAGONISTA_VALIDA.replace("\"energia\":40", "\"energia\":-50"), ""));

        var resumo = GerenciadorSlots.listar(pasta).get(0);

        assertEquals(br.uefs.forkeazando.model.ResumoSlot.Status.CORROMPIDO, resumo.status());
    }

    @Test
    void falhaDeGravacaoContinuaLancandoSaveException() {
        Path inexistente = pasta.resolve("nao_existe").resolve("save.json");

        assertThrows(SaveException.class, () -> GerenciadorSave.salvar(new Estado(), inexistente));
    }
}