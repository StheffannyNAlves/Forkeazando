package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.adapter.CarregamentoAdapter;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Final;
import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.model.ResumoSlot;
import br.uefs.forkeazando.view.ApresentacaoCena;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Proxy;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/** A View acessa saves só pela sessão; estes testes cobrem essa fachada com pasta temporária. */
class SessaoGraficaSavesTest {
    @TempDir Path pasta;

    private static DadosProtagonista dados(String nome) {
        return new DadosProtagonista(nome, IdentidadeProtagonista.STHEFFANNY, false, false, false,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
    }

    private static ApresentacaoCena apresentacaoSemJavaFx() {
        return (ApresentacaoCena) Proxy.newProxyInstance(
                ApresentacaoCena.class.getClassLoader(),
                new Class<?>[]{ApresentacaoCena.class},
                (proxy, metodo, argumentos) -> null);
    }

    private SessaoGrafica novaSessao() {
        return new SessaoGrafica(new CarregamentoAdapter(pasta), pasta);
    }

    @Test
    void novaPartidaComDadosValidosInicia() throws Exception {
        SessaoGrafica sessao = novaSessao();
        sessao.novaPartida(dados("Ana"), apresentacaoSemJavaFx());

        assertTrue(sessao.possuiPartida());
        assertEquals("Ana", sessao.estado().getProtagonista().getNome());
    }

    @Test
    void salvarSobrescreverEExcluirSlotPelaSessao() throws Exception {
        SessaoGrafica sessao = novaSessao();
        sessao.novaPartida(dados("Ana"), apresentacaoSemJavaFx());

        sessao.salvarEmSlot(1);
        ResumoSlot slot1 = sessao.listarSlots().stream().filter(s -> s.numero() == 1).findFirst().orElseThrow();
        assertEquals(ResumoSlot.Status.OCUPADO, slot1.status());
        assertFalse(sessao.estado().temAlteracoes(), "Salvar no slot deve limpar a marca de alteração");

        sessao.salvarEmSlot(1); // sobrescrita
        sessao.excluirSlot(1);
        ResumoSlot depois = sessao.listarSlots().stream().filter(s -> s.numero() == 1).findFirst().orElseThrow();
        assertEquals(ResumoSlot.Status.VAZIO, depois.status());
    }

    @Test
    void autosaveDeJornadaConcluidaPodeSerLidoEExcluido() throws Exception {
        SessaoGrafica sessao = novaSessao();
        assertFalse(sessao.existeAutosave());
        sessao.novaPartida(dados("Ana"), apresentacaoSemJavaFx());
        sessao.estado().concluir(Final.TECNOLOGIA.getCodigo());
        sessao.finalizarPartida();

        assertTrue(sessao.existeAutosave());
        Estado lido = sessao.carregarAutosave();
        assertTrue(lido.isPartidaConcluida());
        assertEquals("Ana", lido.getProtagonista().getNome());

        sessao.excluirAutosave();
        assertFalse(sessao.existeAutosave());
        assertThrows(SaveException.class, sessao::excluirAutosave,
                "Excluir um autosave que não existe deve sinalizar erro");
    }

    @Test
    void cancelarNaoAlteraSavesExistentes() throws Exception {
        SessaoGrafica sessao = novaSessao();
        sessao.novaPartida(dados("Ana"), apresentacaoSemJavaFx());
        sessao.salvarEmSlot(2);

        // "Cancelar" na View = não chamar nada na sessão: o slot continua íntegro.
        assertEquals(ResumoSlot.Status.OCUPADO,
                sessao.listarSlots().stream().filter(s -> s.numero() == 2).findFirst().orElseThrow().status());
        assertEquals("Ana", new CarregamentoAdapter(pasta).carregarSlot(2).getProtagonista().getNome());
    }
}
