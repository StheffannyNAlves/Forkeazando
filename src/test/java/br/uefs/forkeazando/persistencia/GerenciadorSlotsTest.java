package br.uefs.forkeazando.persistencia;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

class GerenciadorSlotsTest {
    @TempDir
    Path pastaTemp;

    @Test
    void deveRetornarPrimeiroQuandoPastaNaoExiste() throws Exception{
        Path pasta = pastaTemp.resolve("saves");

        OptionalInt result = GerenciadorSlots.primeiroSlotLivre(pasta);
        assertEquals(OptionalInt.of(1), result);

    }
    @Test
    void deveEncontrarSegundoSlotQuandoOPrimeiroOcupado() throws IOException {
        Path pasta = pastaTemp.resolve("saves");
        Files.createDirectories(pasta);
        Files.createFile(GerenciadorSlots.caminhoDoSlot(pasta, 1));
        OptionalInt result = GerenciadorSlots.primeiroSlotLivre(pasta);
        assertEquals(OptionalInt.of(2), result);
    }

    @Test
    void deveEncontrarPrimeiroSlotMesmoComBuraco() throws IOException {
        Path pasta = pastaTemp.resolve("saves");
        Files.createDirectories(pasta);
        Files.createFile(GerenciadorSlots.caminhoDoSlot(pasta, 2));

        OptionalInt resultado = GerenciadorSlots.primeiroSlotLivre(pasta);

        assertEquals(OptionalInt.of(1), resultado);
    }

    @Test
    void deveIndicarQueNaoHaSlotLivreQuandoTodosEstaoOcupados() throws IOException {
        Path pasta = pastaTemp.resolve("saves");
        Files.createDirectories(pasta);

        for (int i = 1; i <= GerenciadorSlots.TOTAL_SLOTS; i++) {
            Files.createFile(GerenciadorSlots.caminhoDoSlot(pasta, i));
        }

        OptionalInt resultado = GerenciadorSlots.primeiroSlotLivre(pasta);

        assertEquals(OptionalInt.empty(), resultado);
    }

    @Test
    void deveConsiderarSaveCorrompidoComoSlotOcupado() throws IOException {
        Path pasta = pastaTemp.resolve("saves");
        Files.createDirectories(pasta);

        Path slot1 = GerenciadorSlots.caminhoDoSlot(pasta, 1);
        Files.writeString(slot1, "isso nao e um json valido");

        OptionalInt resultado = GerenciadorSlots.primeiroSlotLivre(pasta);

        assertEquals(OptionalInt.of(2), resultado);
    }

    @Test
    void arquivoTemporarioNaoDeveOcuparSlot() throws IOException {
        Path pasta = pastaTemp.resolve("saves");
        Files.createDirectories(pasta);

        Path slot1 = GerenciadorSlots.caminhoDoSlot(pasta, 1);
        Path temporario = slot1.resolveSibling(
                slot1.getFileName() + ".tmp"
        );

        Files.writeString(temporario, "save temporario");

        OptionalInt resultado = GerenciadorSlots.primeiroSlotLivre(pasta);

        assertEquals(OptionalInt.of(1), resultado);
    }

    @Test
    void deveRejeitarSlotMenorQueUm() {
        Path pasta = pastaTemp.resolve("saves");

        assertThrows(
                IllegalArgumentException.class,
                () -> GerenciadorSlots.caminhoDoSlot(pasta, 0)
        );
    }

    @Test
    void deveRejeitarSlotMaiorQueOTotal() {
        Path pasta = pastaTemp.resolve("saves");

        assertThrows(
                IllegalArgumentException.class,
                () -> GerenciadorSlots.caminhoDoSlot(
                        pasta,
                        GerenciadorSlots.TOTAL_SLOTS + 1
                )
        );
    }



    @Test
    void deveSalvarEstadoNoSlotDois() throws Exception {
        Path pasta = pastaTemp.resolve("saves");

        Protagonista protagonista = new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        );

        Estado estado = new Estado();
        estado.iniciar(protagonista);

        GerenciadorSlots.salvar(estado, pasta, 2);

        Path arquivo = GerenciadorSlots.caminhoDoSlot(pasta, 2);

        assertTrue(Files.exists(arquivo));

        Estado carregado = GerenciadorSave.carregar(arquivo);

        assertEquals("Stheffanny", carregado.getProtagonista().getNome());
    }

    @Test
    void deveCriarPastaSavesSeNaoExistir() throws Exception {
        Path pasta = pastaTemp.resolve("saves");

        Estado estado = new Estado();
        estado.iniciar(new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));

        assertFalse(Files.exists(pasta));

        GerenciadorSlots.salvar(estado, pasta, 1);

        assertTrue(Files.exists(pasta));
        assertTrue(Files.exists(
                GerenciadorSlots.caminhoDoSlot(pasta, 1)
        ));
    }

    @Test
    void deveSubstituirSaveExistente() throws Exception {
        Path pasta = pastaTemp.resolve("saves");

        Estado primeiro = new Estado();
        primeiro.iniciar(new Protagonista(
                "Primeiro",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));

        Estado segundo = new Estado();
        segundo.iniciar(new Protagonista(
                "Segundo",
                true,
                false,
                false,
                Protagonista.NivelVidaSocial.ISOLADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEGATIVA,
                Protagonista.SituacaoEconomica.APERTADA
        ));

        GerenciadorSlots.salvar(primeiro, pasta, 1);
        GerenciadorSlots.salvar(segundo, pasta, 1);

        Estado carregado = GerenciadorSave.carregar(
                GerenciadorSlots.caminhoDoSlot(pasta, 1)
        );

        assertEquals("Segundo", carregado.getProtagonista().getNome());
    }

    @Test
    void deveRejeitarSlotInvalidoAoSalvar() {
        Path pasta = pastaTemp.resolve("saves");
        Estado estado = new Estado();

        assertThrows(
                IllegalArgumentException.class,
                () -> GerenciadorSlots.salvar(estado, pasta, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> GerenciadorSlots.salvar(
                        estado,
                        pasta,
                        GerenciadorSlots.TOTAL_SLOTS + 1
                )
        );
    }
    @Test
    void deveConsiderarSlotOcupadoDepoisDeSalvar() throws Exception {
        Path pasta = pastaTemp.resolve("saves");

        Estado estado = new Estado();
        estado.iniciar(new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));

        GerenciadorSlots.salvar(estado, pasta, 1);

        assertEquals(
                2,
                GerenciadorSlots.primeiroSlotLivre(pasta).orElseThrow()
        );
    }
    @Test
    void naoDeveDeixarArquivoTemporarioAposSalvar() throws Exception {
        Path pasta = pastaTemp.resolve("saves");

        Estado estado = new Estado();
        estado.iniciar(new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));

        GerenciadorSlots.salvar(estado, pasta, 1);

        Path temporario = GerenciadorSlots
                .caminhoDoSlot(pasta, 1)
                .resolveSibling("slot1.json.tmp");

        assertFalse(Files.exists(temporario));
    }
    @Test
    void deveRejeitarNumeroDeSlotInvalido() {
        Path pasta = pastaTemp.resolve("saves");

        Estado estado = new Estado();
        estado.iniciar(new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));

        assertThrows(
                IllegalArgumentException.class,
                () -> GerenciadorSlots.salvar(estado, pasta, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> GerenciadorSlots.salvar(
                        estado, pasta, GerenciadorSlots.TOTAL_SLOTS + 1
                )
        );
    }

    @Test
    void deveRejeitarSlotInvalidoAoSalvarSemCriarPasta() {
        Path pasta = pastaTemp.resolve("saves");

        Estado estado = new Estado();
        estado.iniciar(new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));

        assertThrows(
                IllegalArgumentException.class,
                () -> GerenciadorSlots.salvar(estado, pasta, 0)
        );

        assertFalse(Files.exists(pasta));
    }
    @Test
    void deveLancarSaveExceptionQuandoNaoConsegueCriarPasta() {
        Path pasta = pastaTemp.resolve("saves");
        try {
            Files.createFile(pasta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Estado estado = new Estado();
        estado.iniciar(new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));

        SaveException excecao = assertThrows(
                SaveException.class,
                () -> GerenciadorSlots.salvar(estado, pasta, 1)
        );

        assertInstanceOf(IOException.class, excecao.getCause());
    }

    @Test
    void estadoInvalidoNaoDeveSubstituirSaveExistente() throws Exception {
        Path pasta = pastaTemp.resolve("saves");

        Estado valido = new Estado();
        valido.iniciar(new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));

        GerenciadorSlots.salvar(valido, pasta, 1);

        Estado invalido = new Estado();

        assertThrows(
                SaveException.class,
                () -> GerenciadorSlots.salvar(invalido, pasta, 1)
        );

        Estado carregado = GerenciadorSave.carregar(
                GerenciadorSlots.caminhoDoSlot(pasta, 1)
        );

        assertEquals(
                "Stheffanny",
                carregado.getProtagonista().getNome()
        );
    }


    @Test
    void naoDeveSalvarEstadoSemProtagonista() {
        Path pasta = pastaTemp.resolve("saves");
        Estado estado = new Estado();

        SaveException excecao = assertThrows(
                SaveException.class,
                () -> GerenciadorSlots.salvar(estado, pasta, 1)
        );

        assertTrue(excecao.getMessage().contains("protagonista"));
        assertFalse(Files.exists(pasta));
    }

    @Test
    void deveMarcarEstadoComoSalvoDepoisDeSalvarComSucesso() throws Exception {
        Path pasta = pastaTemp.resolve("saves");

        Estado estado = new Estado();
        estado.iniciar(new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));

        estado.marcarAlterado();

        assertTrue(estado.temAlteracoes());

        GerenciadorSlots.salvar(estado, pasta, 1);

        assertFalse(estado.temAlteracoes());
    }

    @Test
    void deveManterEstadoAlteradoQuandoSalvarFalhar() throws Exception {
        Path pasta = pastaTemp.resolve("saves");
        Files.createFile(pasta);

        Estado estado = new Estado();
        estado.iniciar(new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));

        estado.marcarAlterado();

        SaveException excecao = assertThrows(
                SaveException.class,
                () -> GerenciadorSlots.salvar(estado, pasta, 1)
        );

        assertInstanceOf(IOException.class, excecao.getCause());
        assertTrue(estado.temAlteracoes());
    }

    @Test
    void deveCarregarEstadoDeUmSlot() throws Exception {
        Path pasta = pastaTemp.resolve("saves");

        Estado estado = new Estado();
        estado.iniciar(new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));
        estado.setCenaAtualId(700);

        GerenciadorSlots.salvar(estado, pasta, 1);

        Estado carregado = GerenciadorSlots.carregar(pasta, 1);

        assertNotNull(carregado);
        assertNotNull(carregado.getProtagonista());
        assertEquals("Stheffanny", carregado.getProtagonista().getNome());
        assertEquals(700, carregado.getCenaAtualId());
    }

    @Test
    void deveLancarCarregamentoExceptionAoCarregarSlotVazio() {
        Path pasta = pastaTemp.resolve("saves");

        assertThrows(
                CarregamentoException.class,
                () -> GerenciadorSlots.carregar(pasta, 1)
        );
    }

    @Test
    void deveRejeitarSlotInvalidoAoCarregar() {
        Path pasta = pastaTemp.resolve("saves");

        assertThrows(
                IllegalArgumentException.class,
                () -> GerenciadorSlots.carregar(pasta, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> GerenciadorSlots.carregar(
                        pasta,
                        GerenciadorSlots.TOTAL_SLOTS + 1
                )
        );
    }

    @Test
    void deveListarTodosOsSlotsComoVaziosQuandoPastaNaoExiste() {
        Path pasta = pastaTemp.resolve("saves");

        List<String> linhas = GerenciadorSlots.listar(pasta);

        assertEquals(GerenciadorSlots.TOTAL_SLOTS, linhas.size());

        for (int i = 1; i <= GerenciadorSlots.TOTAL_SLOTS; i++) {
            String linha = linhas.get(i - 1);

            assertTrue(linha.contains("Slot " + i));
            assertTrue(linha.toLowerCase().contains("vazio"));
        }
    }

    @Test
    void deveListarSlotOcupadoComNomeECapitulo() throws Exception {
        Path pasta = pastaTemp.resolve("saves");

        Estado estado = new Estado();
        estado.iniciar(new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));

        GerenciadorSlots.salvar(estado, pasta, 1);

        List<String> linhas = GerenciadorSlots.listar(pasta);

        assertEquals(GerenciadorSlots.TOTAL_SLOTS, linhas.size());

        String linha = linhas.get(0);

        assertTrue(linha.contains("Slot 1"));
        assertTrue(linha.toLowerCase().contains("ocupado"));
        assertTrue(linha.contains("Stheffanny"));
        assertTrue(linha.contains("Capítulo 1"));
    }

    @Test
    void deveListarSlotsOcupadosEVazios() throws Exception {
        Path pasta = pastaTemp.resolve("saves");

        Estado estado = new Estado();
        estado.iniciar(new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));

        GerenciadorSlots.salvar(estado, pasta, 2);

        List<String> linhas = GerenciadorSlots.listar(pasta);

        assertEquals(GerenciadorSlots.TOTAL_SLOTS, linhas.size());

        assertTrue(linhas.get(0).toLowerCase().contains("vazio"));
        assertTrue(linhas.get(1).toLowerCase().contains("ocupado"));
        assertTrue(linhas.get(1).contains("Stheffanny"));
        assertTrue(linhas.get(2).toLowerCase().contains("vazio"));
    }

    @Test
    void deveListarSlotCorrompidoSemDerrubarOsDemais() throws Exception {
        Path pasta = pastaTemp.resolve("saves");
        Files.createDirectories(pasta);

        Path slot1 = GerenciadorSlots.caminhoDoSlot(pasta, 1);
        Files.writeString(slot1, "{ json inválido");

        Estado estado = new Estado();
        estado.iniciar(new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        ));

        GerenciadorSlots.salvar(estado, pasta, 2);

        List<String> linhas = GerenciadorSlots.listar(pasta);

        assertEquals(GerenciadorSlots.TOTAL_SLOTS, linhas.size());

        assertTrue(linhas.get(0).toLowerCase().contains("corrompido"));
        assertTrue(linhas.get(1).toLowerCase().contains("ocupado"));
        assertTrue(linhas.get(1).contains("Stheffanny"));
        assertTrue(linhas.get(2).toLowerCase().contains("vazio"));
    }

}
