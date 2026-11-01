package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.dao.GerenciadorProgresso;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ProgressoControllerTest {

    @TempDir Path pasta;

    @Test
    void visualizarComProgressoNuloAvisaEmVezDeFalhar() {
        MensagensRegistradas mensagens = new MensagensRegistradas();
        ProgressoController controller = new ProgressoController(mensagens, new GerenciadorProgresso(pasta));

        assertDoesNotThrow(() -> controller.visualizar(null));

        assertEquals(1, mensagens.erros.size());
        assertTrue(mensagens.erros.getFirst().contains("progresso.json"));
    }

    @Test
    void registrarSemPartidaAvisaEmVezDeFalhar() {
        MensagensRegistradas mensagens = new MensagensRegistradas();
        ProgressoController controller = new ProgressoController(mensagens, new GerenciadorProgresso(pasta));

        assertDoesNotThrow(() -> controller.registrar(null, null));

        assertEquals(1, mensagens.erros.size());
    }
}