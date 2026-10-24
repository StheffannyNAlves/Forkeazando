package br.uefs.forkeazando.dao;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import br.uefs.forkeazando.model.ResumoSlot;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

class GerenciadorSlotsTest {
    @TempDir
    Path pastaTemp;
    private static Estado estadoValido(String nome) {
        Estado estado = new Estado();
        estado.iniciar(new Protagonista(
                nome, false, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL));
        return estado;
    }
    private static Estado estadoValido() {
        return estadoValido("Stheffanny");
    }

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

        Estado estado = estadoValido();

        assertFalse(Files.exists(pasta));

        GerenciadorSlots.salvar(estado, pasta, 1);

        assertTrue(Files.exists(pasta));
        assertTrue(Files.exists(GerenciadorSlots.caminhoDoSlot(pasta, 1)));
    }

    @Test
    void deveSubstituirSaveExistente() throws Exception {
        Path pasta = pastaTemp.resolve("saves");


        GerenciadorSlots.salvar(estadoValido("Primeiro"), pasta, 1);
        GerenciadorSlots.salvar(estadoValido("Segundo"), pasta, 1);

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

        Estado estado = estadoValido();

        GerenciadorSlots.salvar(estado, pasta, 1);

        assertEquals(
                2,
                GerenciadorSlots.primeiroSlotLivre(pasta).orElseThrow()
        );
    }
    @Test
    void naoDeveDeixarArquivoTemporarioAposSalvar() throws Exception {
        Path pasta = pastaTemp.resolve("saves");

        Estado estado = estadoValido();

        GerenciadorSlots.salvar(estado, pasta, 1);

        Path slot = GerenciadorSlots.caminhoDoSlot(pasta, 1);
        Path temporario = slot.resolveSibling(slot.getFileName() + ".tmp");
        assertFalse(Files.exists(temporario));
    }

    @Test
    void deveLancarSaveExceptionQuandoNaoConsegueCriarPasta() {
        Path pasta = pastaTemp.resolve("saves");
        try {
            Files.createFile(pasta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Estado estado = estadoValido();

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

        List<ResumoSlot> slots = GerenciadorSlots.listar(pasta);

        assertEquals(GerenciadorSlots.TOTAL_SLOTS, slots.size());

        for (int i = 1; i <= GerenciadorSlots.TOTAL_SLOTS; i++) {
            ResumoSlot slot = slots.get(i - 1);

            assertEquals(i, slot.numero());
            assertEquals(ResumoSlot.Status.VAZIO, slot.status());
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

        List<ResumoSlot> slots = GerenciadorSlots.listar(pasta);

        assertEquals(GerenciadorSlots.TOTAL_SLOTS, slots.size());

        ResumoSlot slot = slots.get(0);

        assertEquals(1, slot.numero());
        assertEquals(ResumoSlot.Status.OCUPADO, slot.status());
        assertEquals("Stheffanny", slot.nome());
        assertEquals(1, slot.capitulo());
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

        List<ResumoSlot> slots = GerenciadorSlots.listar(pasta);

        assertEquals(GerenciadorSlots.TOTAL_SLOTS, slots.size());

        assertEquals(ResumoSlot.Status.VAZIO, slots.get(0).status());
        assertEquals(ResumoSlot.Status.OCUPADO, slots.get(1).status());
        assertEquals("Stheffanny", slots.get(1).nome());
        assertEquals(ResumoSlot.Status.VAZIO, slots.get(2).status());
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

        List<ResumoSlot> slots = GerenciadorSlots.listar(pasta);

        assertEquals(GerenciadorSlots.TOTAL_SLOTS, slots.size());

        assertEquals(ResumoSlot.Status.CORROMPIDO, slots.get(0).status());
        assertEquals(ResumoSlot.Status.OCUPADO, slots.get(1).status());
        assertEquals("Stheffanny", slots.get(1).nome());
        assertEquals(ResumoSlot.Status.VAZIO, slots.get(2).status());
    }

    @Test
    void autosaveDeveCriarArquivoSemOcuparSlotENemMarcarSalvo() throws Exception {
        Path pasta = pastaTemp.resolve("saves");
        Estado estado = estadoValido();
        estado.marcarAlterado();

        GerenciadorSlots.salvarAutosave(estado, pasta);

        assertTrue(Files.exists(GerenciadorSlots.caminhoDoAutosave(pasta)));
        assertEquals(OptionalInt.of(1), GerenciadorSlots.primeiroSlotLivre(pasta));
        assertTrue(estado.temAlteracoes());
    }

    @Test
    void salvarEmSlotDeveMarcarComoSalvo() throws Exception {
        Estado estado = estadoValido();
        estado.marcarAlterado();

        GerenciadorSlots.salvar(estado, pastaTemp, 1);

        assertFalse(estado.temAlteracoes());
    }

    @Test
    void autosaveDeveRejeitarEstadoSemProtagonista() {
        assertThrows(SaveException.class,
                () -> GerenciadorSlots.salvarAutosave(new Estado(), pastaTemp));
    }

    @Test
    void excluirDeveRemoverOArquivoDoSlot() throws Exception {
        Path pasta = pastaTemp.resolve("saves");
        GerenciadorSlots.salvar(estadoValido(), pasta, 2);
        assertTrue(Files.exists(GerenciadorSlots.caminhoDoSlot(pasta, 2)));

        GerenciadorSlots.excluir(pasta, 2);

        assertFalse(Files.exists(GerenciadorSlots.caminhoDoSlot(pasta, 2)));
        assertEquals(OptionalInt.of(1), GerenciadorSlots.primeiroSlotLivre(pasta));
    }

    @Test
    void excluirSlotVazioDeveLancarSaveException() {
        Path pasta = pastaTemp.resolve("saves");

        SaveException e = assertThrows(SaveException.class,
                () -> GerenciadorSlots.excluir(pasta, 1));

        assertTrue(e.getMessage().contains("O slot 1 já está vazio"));
    }

    @Test
    void excluirNaoDeveAfetarOutrosSlots() throws Exception {
        Path pasta = pastaTemp.resolve("saves");
        GerenciadorSlots.salvar(estadoValido("A"), pasta, 1);
        GerenciadorSlots.salvar(estadoValido("B"), pasta, 2);

        GerenciadorSlots.excluir(pasta, 1);

        assertTrue(Files.exists(GerenciadorSlots.caminhoDoSlot(pasta, 2)));
    }
}
