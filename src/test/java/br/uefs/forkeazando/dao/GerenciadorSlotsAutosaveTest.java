package br.uefs.forkeazando.dao;

import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GerenciadorSlotsAutosaveTest {

    @TempDir
    Path pasta;

    private static Estado estado() {
        Estado estado = new Estado();
        estado.iniciar(new Protagonista("Ana", true, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL));
        return estado;
    }

    @Test
    void naoExisteAutosaveNoInicio() {
        assertFalse(GerenciadorSlots.existeAutosave(pasta));
    }

    @Test
    void autosaveGravadoPodeSerExcluido() throws SaveException {
        GerenciadorSlots.salvarAutosave(estado(), pasta);
        assertTrue(GerenciadorSlots.existeAutosave(pasta));

        GerenciadorSlots.excluirAutosave(pasta);

        assertFalse(GerenciadorSlots.existeAutosave(pasta));
    }

    @Test
    void excluirAutosaveInexistenteLancaSaveException() {
        assertThrows(SaveException.class, () -> GerenciadorSlots.excluirAutosave(pasta));
    }

    @Test
    void excluirAutosaveNaoMexeNosSlots() throws SaveException {
        GerenciadorSlots.salvar(estado(), pasta, 1);
        GerenciadorSlots.salvarAutosave(estado(), pasta);

        GerenciadorSlots.excluirAutosave(pasta);

        assertTrue(java.nio.file.Files.exists(GerenciadorSlots.caminhoDoSlot(pasta, 1)));
    }
}