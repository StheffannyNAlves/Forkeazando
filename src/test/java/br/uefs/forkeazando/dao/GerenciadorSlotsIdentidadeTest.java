package br.uefs.forkeazando.dao;

import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Protagonista;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GerenciadorSlotsIdentidadeTest {

    @TempDir Path pasta;

    private static Estado novaPartida(String nome) {
        Estado estado = new Estado();
        estado.iniciar(new Protagonista(nome, IdentidadeProtagonista.STHEFFANNY,
                false, true, true, Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL));
        return estado;
    }

    @Test
    void umaPartidaNaoPodeSerGravadaEmDoisSlots() throws Exception {
        Estado partida = novaPartida("Ana");
        GerenciadorSlots.salvar(partida, pasta, 1);
        partida.marcarAlterado();

        SaveException erro = assertThrows(SaveException.class,
                () -> GerenciadorSlots.salvar(partida, pasta, 2));

        assertTrue(erro.getMessage().contains("slot 1"));
        assertFalse(Files.exists(GerenciadorSlots.caminhoDoSlot(pasta, 2)));
        assertTrue(partida.temAlteracoes(), "Falha nao deve marcar o estado como salvo");
    }

    @Test
    void podeAtualizarSlotDeOrigemMesmoAposCarregar() throws Exception {
        Estado original = novaPartida("Ana");
        GerenciadorSlots.salvar(original, pasta, 2);

        Estado carregada = GerenciadorSlots.carregar(pasta, 2);
        carregada.setCenaAtualId(701);
        GerenciadorSlots.salvar(carregada, pasta, 2);

        assertEquals(original.getIdPartida(), carregada.getIdPartida());
        assertEquals(701, GerenciadorSlots.carregar(pasta, 2).getCenaAtualId());
        assertThrows(SaveException.class,
                () -> GerenciadorSlots.salvar(carregada, pasta, 3));
    }

    @Test
    void partidasDiferentesComMesmoNomePodemOcuparSlotsDiferentes() throws Exception {
        Estado primeira = novaPartida("Ana");
        Estado segunda = novaPartida("Ana");

        assertNotEquals(primeira.getIdPartida(), segunda.getIdPartida());

        GerenciadorSlots.salvar(primeira, pasta, 1);
        assertDoesNotThrow(() -> GerenciadorSlots.salvar(segunda, pasta, 2));
        assertTrue(Files.exists(GerenciadorSlots.caminhoDoSlot(pasta, 2)));
    }

    @Test
    void conflitoNaoDeveSubstituirOutraPartida() throws Exception {
        Estado primeira = novaPartida("Ana");
        Estado segunda = novaPartida("Bia");
        GerenciadorSlots.salvar(primeira, pasta, 1);
        GerenciadorSlots.salvar(segunda, pasta, 3);

        assertThrows(SaveException.class,
                () -> GerenciadorSlots.salvar(primeira, pasta, 3));

        assertEquals(segunda.getIdPartida(),
                GerenciadorSlots.carregar(pasta, 3).getIdPartida());
    }

    @Test
    void excluirSlotAnteriorLiberaPartidaParaOutroSlot() throws Exception {
        Estado partida = novaPartida("Ana");
        GerenciadorSlots.salvar(partida, pasta, 1);
        GerenciadorSlots.excluir(pasta, 1);

        assertDoesNotThrow(() -> GerenciadorSlots.salvar(partida, pasta, 3));
    }

    @Test
    void autosaveNaoOcupaSlotManualNemMudaIdentidade() throws Exception {
        Estado partida = novaPartida("Ana");
        GerenciadorSlots.salvarAutosave(partida, pasta);
        Estado recuperada = GerenciadorSave.carregar(
                GerenciadorSlots.caminhoDoAutosave(pasta));

        assertEquals(partida.getIdPartida(), recuperada.getIdPartida());
        assertDoesNotThrow(() -> GerenciadorSlots.salvar(recuperada, pasta, 1));
    }

    @Test
    void saveLegadoSemIdentificadorPodeSerAtualizadoEmSeuSlot()
            throws Exception {
        Estado antiga = novaPartida("Ana");
        Path slot = GerenciadorSlots.caminhoDoSlot(pasta, 1);
        GerenciadorSlots.salvar(antiga, pasta, 1);

        JsonObject json = JsonParser.parseString(Files.readString(slot)).getAsJsonObject();
        json.getAsJsonObject("estado").remove("idPartida");
        Files.writeString(slot, json.toString());

        Estado recuperada = GerenciadorSlots.carregar(pasta, 1);
        Estado segundaLeitura = GerenciadorSlots.carregar(pasta, 1);
        assertEquals(recuperada.getIdPartida(), segundaLeitura.getIdPartida());

        assertThrows(SaveException.class,
                () -> GerenciadorSlots.salvar(recuperada, pasta, 2));

        GerenciadorSlots.salvar(recuperada, pasta, 1);
        assertEquals(recuperada.getIdPartida(),
                GerenciadorSlots.carregar(pasta, 1).getIdPartida());
    }
}
