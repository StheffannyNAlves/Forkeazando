package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.dao.GerenciadorSlots;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.model.ResumoSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa salvar, carregar, sobrescrever e excluir saves sem terminal: o menu é
 * respondido por um SeletorRoteirizado e os arquivos vão para uma pasta temporária.
 */
class GerenciadorSavesControllerTest {

    private static final int TOTAL = GerenciadorSlots.TOTAL_SLOTS;

    @TempDir
    Path pasta;

    private SeletorRoteirizado seletor;
    private MensagensRegistradas mensagens;
    private GerenciadorSavesController controller;

    @BeforeEach
    void preparar() {
        seletor = new SeletorRoteirizado();
        mensagens = new MensagensRegistradas();
        controller = new GerenciadorSavesController(mensagens, pasta, seletor);
    }

    private static Estado partida(String nome) {
        Protagonista p = new Protagonista(nome, false, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
        Estado estado = new Estado();
        estado.iniciar(p);
        return estado;
    }

    private void gravarSlot(int numero, String nome) throws SaveException {
        GerenciadorSlots.salvar(partida(nome), pasta, numero);
    }

    private String nomeNoSlot(int numero) throws Exception {
        return GerenciadorSlots.carregar(pasta, numero).getProtagonista().getNome();
    }

    private ResumoSlot.Status statusDoSlot(int numero) {
        return GerenciadorSlots.listar(pasta).get(numero - 1).status();
    }

    // ---------- salvar ----------

    @Test
    void salvarEmSlotVazioGravaOArquivoEDevolveONumeroDoSlot() throws Exception {
        seletor.indice(1);

        OptionalInt resultado = controller.salvarPartida(partida("Ana"));

        assertEquals(OptionalInt.of(2), resultado);
        assertEquals("Ana", nomeNoSlot(2));
        assertTrue(mensagens.informacoes.contains("Partida salva no Slot 2!"));
        assertEquals(1, seletor.titulosVistos.size(), "slot vazio não pede confirmação");
    }

    @Test
    void menuDeSalvarListaOsSlotsMaisCancelar() {
        seletor.ultima();

        controller.salvarPartida(partida("Ana"));

        assertEquals(TOTAL + 1, seletor.menusVistos.get(0).size());
        assertEquals("Cancelar", seletor.menusVistos.get(0).get(TOTAL));
        assertEquals("Escolha onde salvar a partida", seletor.titulosVistos.get(0));
    }

    @Test
    void cancelarNaoSalvaNada() {
        seletor.ultima();

        OptionalInt resultado = controller.salvarPartida(partida("Ana"));

        assertTrue(resultado.isEmpty());
        for (int i = 1; i <= TOTAL; i++) {
            assertEquals(ResumoSlot.Status.VAZIO, statusDoSlot(i));
        }
    }

    @Test
    void escNaoSalvaNada() {
        seletor.cancela();

        assertTrue(controller.salvarPartida(partida("Ana")).isEmpty());
        assertEquals(ResumoSlot.Status.VAZIO, statusDoSlot(1));
    }

    @Test
    void sobrescreverSlotOcupadoPedeConfirmacaoEGravaQuandoConfirmada() throws Exception {
        gravarSlot(1, "Antiga");
        seletor.indice(0).escolhe("Sim, sobrescrever");

        OptionalInt resultado = controller.salvarPartida(partida("Nova"));

        assertEquals(OptionalInt.of(1), resultado);
        assertEquals("Nova", nomeNoSlot(1));
        assertEquals("Sobrescrever Slot 1?", seletor.titulosVistos.get(1));
    }

    @Test
    void recusarASobrescritaPreservaOSaveAntigo() throws Exception {
        gravarSlot(1, "Antiga");
        seletor.indice(0).escolhe("Não");

        OptionalInt resultado = controller.salvarPartida(partida("Nova"));

        assertTrue(resultado.isEmpty());
        assertEquals("Antiga", nomeNoSlot(1));
    }

    @Test
    void escNaConfirmacaoDeSobrescritaPreservaOSaveAntigo() throws Exception {
        gravarSlot(1, "Antiga");
        seletor.indice(0).cancela();

        assertTrue(controller.salvarPartida(partida("Nova")).isEmpty());
        assertEquals("Antiga", nomeNoSlot(1));
    }

    @Test
    void falhaDeGravacaoMostraErroEDevolveVazio() throws IOException {
        // Um diretório no lugar do arquivo do slot impede a gravação.
        Files.createDirectories(GerenciadorSlots.caminhoDoSlot(pasta, 1));
        seletor.indice(0).escolhe("Sim, sobrescrever");

        OptionalInt resultado = controller.salvarPartida(partida("Ana"));

        assertTrue(resultado.isEmpty());
        assertEquals(1, mensagens.erros.size());
        assertTrue(mensagens.erros.get(0).startsWith("Erro ao salvar"));
    }

    // ---------- carregar ----------

    @Test
    void carregarSlotOcupadoDevolveAPartidaEOnumeroDoSlot() throws Exception {
        gravarSlot(3, "Bia");
        seletor.indice(2);

        Optional<GerenciadorSavesController.PartidaSelecionada> r = controller.carregarPartida();

        assertTrue(r.isPresent());
        assertEquals(3, r.get().numeroSlot());
        assertEquals("Bia", r.get().estado().getProtagonista().getNome());
    }

    @Test
    void carregarSlotVazioAvisaEDevolveVazio() {
        seletor.indice(0);

        assertTrue(controller.carregarPartida().isEmpty());
        assertTrue(mensagens.informacoes.contains("Esse slot está vazio."));
    }

    @Test
    void carregarSlotCorrompidoAvisaEDevolveVazio() throws IOException {
        Files.writeString(GerenciadorSlots.caminhoDoSlot(pasta, 1), "isto não é json {{{");
        seletor.indice(0);

        assertTrue(controller.carregarPartida().isEmpty());
        assertTrue(mensagens.informacoes.contains("Esse save está corrompido."));
    }

    @Test
    void carregarAutosaveExistenteDevolveAPartidaSemNumeroDeSlot() throws Exception {
        GerenciadorSlots.salvarAutosave(partida("Auto"), pasta);
        seletor.indice(TOTAL);

        Optional<GerenciadorSavesController.PartidaSelecionada> r = controller.carregarPartida();

        assertTrue(r.isPresent());
        assertNull(r.get().numeroSlot());
        assertEquals("Auto", r.get().estado().getProtagonista().getNome());
    }

    @Test
    void carregarAutosaveInexistenteAvisa() {
        seletor.indice(TOTAL);

        assertTrue(controller.carregarPartida().isEmpty());
        assertTrue(mensagens.informacoes.contains("Ainda não existe um autosave."));
    }

    @Test
    void cancelarOuEscNoCarregarDevolveVazio() {
        seletor.ultima().cancela();

        assertTrue(controller.carregarPartida().isEmpty());
        assertTrue(controller.carregarPartida().isEmpty());
        assertTrue(mensagens.erros.isEmpty());
    }

    // ---------- excluir ----------

    @Test
    void excluirSaveConfirmadoApagaOSlot() throws Exception {
        gravarSlot(2, "Ana");
        seletor.escolhe("Excluir save").indice(1).escolhe("Sim, excluir");

        controller.gerenciarSaves();

        assertEquals(ResumoSlot.Status.VAZIO, statusDoSlot(2));
        assertEquals(List.of("Slot 2 excluído."), mensagens.sucessos);
    }

    @Test
    void excluirSaveRecusadoMantemOSlot() throws Exception {
        gravarSlot(2, "Ana");
        seletor.escolhe("Excluir save").indice(1).escolhe("Não");

        controller.gerenciarSaves();

        assertEquals("Ana", nomeNoSlot(2));
        assertTrue(mensagens.sucessos.isEmpty());
    }

    @Test
    void excluirSlotVazioApenasAvisa() {
        seletor.escolhe("Excluir save").indice(0);

        controller.gerenciarSaves();

        assertTrue(mensagens.informacoes.contains("Esse slot já está vazio."));
        assertEquals(2, seletor.titulosVistos.size(), "slot vazio não pede confirmação");
    }

    @Test
    void excluirAutosaveConfirmadoApagaOArquivo() throws Exception {
        GerenciadorSlots.salvarAutosave(partida("Auto"), pasta);
        seletor.escolhe("Excluir autosave").escolhe("Sim, excluir");

        controller.gerenciarSaves();

        assertFalse(GerenciadorSlots.existeAutosave(pasta));
        assertEquals(List.of("Autosave excluído."), mensagens.sucessos);
    }

    @Test
    void excluirAutosaveRecusadoMantemOArquivo() throws Exception {
        GerenciadorSlots.salvarAutosave(partida("Auto"), pasta);
        seletor.escolhe("Excluir autosave").escolhe("Não");

        controller.gerenciarSaves();

        assertTrue(GerenciadorSlots.existeAutosave(pasta));
    }

    @Test
    void excluirAutosaveQueNaoExisteAvisaSemPedirConfirmacao() {
        seletor.escolhe("Excluir autosave");

        controller.gerenciarSaves();

        assertTrue(mensagens.informacoes.contains("Ainda não existe um autosave."));
        assertEquals(1, seletor.titulosVistos.size());
    }

    @Test
    void voltarEEscNoMenuDeGerenciarNaoFazemNada() throws Exception {
        gravarSlot(1, "Ana");
        GerenciadorSlots.salvarAutosave(partida("Auto"), pasta);
        seletor.escolhe("Voltar").cancela();

        controller.gerenciarSaves();
        controller.gerenciarSaves();

        assertEquals("Ana", nomeNoSlot(1));
        assertTrue(GerenciadorSlots.existeAutosave(pasta));
    }

    // ---------- escolherSobrescrita (fim de partida / nova partida) ----------

    @Test
    void prosseguirSemSalvarDevolveTrueSemGravar() {
        seletor.indice(TOTAL);

        boolean seguir = controller.escolherSobrescrita(partida("Ana"), true);

        assertTrue(seguir);
        assertEquals(ResumoSlot.Status.VAZIO, statusDoSlot(1));
    }

    @Test
    void cancelarDevolveFalseSemGravar() {
        seletor.indice(TOTAL + 1);

        assertFalse(controller.escolherSobrescrita(partida("Ana"), true));
        assertEquals(ResumoSlot.Status.VAZIO, statusDoSlot(1));
    }

    @Test
    void semOpcaoDeProsseguirOMenuTemApenasSlotsECancelar() {
        seletor.ultima();

        boolean seguir = controller.escolherSobrescrita(partida("Ana"), false);

        assertFalse(seguir);
        assertEquals(TOTAL + 1, seletor.menusVistos.get(0).size());
    }

    @Test
    void escolherSlotVazioGravaEDevolveTrue() throws Exception {
        seletor.indice(0);

        assertTrue(controller.escolherSobrescrita(partida("Ana"), true));
        assertEquals("Ana", nomeNoSlot(1));
    }

    @Test
    void escolherSlotOcupadoRecusandoConfirmacaoDevolveFalseEMantemOAntigo() throws Exception {
        gravarSlot(1, "Antiga");
        seletor.indice(0).escolhe("Não");

        assertFalse(controller.escolherSobrescrita(partida("Nova"), true));
        assertEquals("Antiga", nomeNoSlot(1));
    }

    @Test
    void escNoMenuDeSobrescritaDevolveFalse() {
        seletor.cancela();

        assertFalse(controller.escolherSobrescrita(partida("Ana"), true));
    }

    // ---------- salvar conclusão ----------

    @Test
    void salvarConclusaoGravaAutosaveESlotQuandoHaSlot() throws Exception {
        controller.salvarConclusao(partida("Fim"), 2);

        assertTrue(GerenciadorSlots.existeAutosave(pasta));
        assertEquals("Fim", nomeNoSlot(2));
    }

    @Test
    void salvarConclusaoSemSlotGravaSoOAutosave() {
        controller.salvarConclusao(partida("Fim"), null);

        assertTrue(GerenciadorSlots.existeAutosave(pasta));
        assertEquals(ResumoSlot.Status.VAZIO, statusDoSlot(1));
    }

    @Test
    void autosavePeloControllerGravaOArquivoDoAutosave() throws Exception {
        controller.salvarAutosave(partida("Auto"));

        assertTrue(GerenciadorSlots.existeAutosave(pasta));
    }
}
