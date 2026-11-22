package br.uefs.forkeazando.adapter;

import br.uefs.forkeazando.dao.GerenciadorSlots;
import br.uefs.forkeazando.excecao.SaveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa isoladamente a exclusao de autosave pelo Adapter
 */
class CarregamentoAdapterAutosaveTest {

    @TempDir
    Path pasta;

    @Test
    void excluirAutosavePreservaOsTresSlotsManuais() throws IOException, SaveException {
        GerenciamentoPartidas partidas = new CarregamentoAdapter(pasta);
        Path autosave = GerenciadorSlots.caminhoDoAutosave(pasta);
        Files.writeString(autosave, "conteudo do autosave");

        // Os arquivos sao sentinelas: o teste verifica que seus bytes nao mudam.
        for (int numero = 1; numero <= GerenciadorSlots.TOTAL_SLOTS; numero++) {
            Path arquivo = GerenciadorSlots.caminhoDoSlot(pasta, numero);
            Files.writeString(arquivo, "conteudo original do slot " + numero);
        }

        assertTrue(partidas.existeAutosave());

        partidas.excluirAutosave();

        assertFalse(partidas.existeAutosave());
        assertFalse(Files.exists(autosave));

        for (int numero = 1; numero <= GerenciadorSlots.TOTAL_SLOTS; numero++) {
            Path arquivo = GerenciadorSlots.caminhoDoSlot(pasta, numero);
            assertTrue(Files.exists(arquivo), "O slot " + numero + " nao deve ser excluido");
            assertEquals("conteudo original do slot " + numero, Files.readString(arquivo),
                    "O conteudo do slot " + numero + " nao deve mudar");
        }
    }

    @Test
    void excluirAutosaveInexistenteInformaOErro() {
        GerenciamentoPartidas partidas = new CarregamentoAdapter(pasta);

        SaveException erro = assertThrows(SaveException.class, partidas::excluirAutosave);

        assertTrue(erro.getMessage().contains("Não existe autosave"));
        assertFalse(partidas.existeAutosave());
    }

    @Test
    void autosaveCorrompidoPodeSerExcluidoSemLeituraDoJson() throws IOException, SaveException {
        GerenciamentoPartidas partidas = new CarregamentoAdapter(pasta);
        Path autosave = GerenciadorSlots.caminhoDoAutosave(pasta);
        Files.writeString(autosave, "{ json invalido ");

        assertTrue(partidas.existeAutosave());
        assertDoesNotThrow(partidas::excluirAutosave);
        assertFalse(Files.exists(autosave));
    }
}
