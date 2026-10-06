package br.uefs.forkeazando.model;

import br.uefs.forkeazando.persistencia.GerenciadorSave;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class EstadoTest {

    @TempDir
    Path pastaTemp;

    @Test
    void capituloComecaEm1EAvancaCorretamente() {
        Estado estado = new Estado();

        assertEquals(1, estado.getCapituloAtual());

        estado.avancarCapitulo();

        assertEquals(2, estado.getCapituloAtual());
    }

    @Test
    void cenaAtualComecaEm0EMudaComSet() {
        Estado estado = new Estado();

        assertEquals(0, estado.getCenaAtualId());

        estado.setCenaAtualId(9);

        assertEquals(9, estado.getCenaAtualId());
    }

    @Test
    void iniciarDeveGuardarOProtagonista() {
        Protagonista protagonista = new Protagonista(
                "Stheffanny",
                false,
                false,
                false,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        );

        Estado estado = new Estado();

        estado.iniciar(protagonista);

        assertEquals(protagonista, estado.getProtagonista());
    }

    @Test
    void estadoNovoNaoDeveTerAlteracoes() {
        Estado estado = new Estado();

        assertFalse(estado.temAlteracoes());
    }

    @Test
    void deveMarcarEstadoComoAlterado() {
        Estado estado = new Estado();

        estado.marcarAlterado();

        assertTrue(estado.temAlteracoes());
    }

    @Test
    void deveMarcarEstadoComoSalvo() {
        Estado estado = new Estado();

        estado.marcarAlterado();
        estado.marcarSalvo();

        assertFalse(estado.temAlteracoes());
    }

    @Test
    void marcaDeAlteracaoNaoDeveSerPersistida() throws Exception {
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

        Path arquivo = pastaTemp.resolve("slot1.json");
        GerenciadorSave.salvar(estado, arquivo);

        Estado carregado = GerenciadorSave.carregar(arquivo);

        assertTrue(estado.temAlteracoes());
        assertFalse(carregado.temAlteracoes());
    }
}