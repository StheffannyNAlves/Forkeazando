package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.adapter.CarregamentoAdapter;
import br.uefs.forkeazando.dao.GerenciadorSlots;
import br.uefs.forkeazando.model.Final;
import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.view.ApresentacaoCena;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SessaoGraficaFinalizacaoTest {
    @TempDir Path pasta;

    private static Protagonista protagonista() {
        return new Protagonista("Teste", IdentidadeProtagonista.STHEFFANNY,
                false, false, false,
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

    @Test
    void finalizarPartidaGravaAutosaveNaPastaInjetadaEEncerraASessao() throws Exception {
        SessaoGrafica sessao = new SessaoGrafica(new CarregamentoAdapter(pasta), pasta);
        sessao.novaPartida(protagonista(), apresentacaoSemJavaFx());
        sessao.estado().concluir(Final.TECNOLOGIA.getCodigo());

        sessao.finalizarPartida();

        assertTrue(Files.exists(GerenciadorSlots.caminhoDoAutosave(pasta)),
                "O autosave deve ser gravado na pasta injetada, e nao na pasta padrao");
        assertTrue(sessao.consultarProgresso().alcancou(Final.TECNOLOGIA),
                "O final concluido deve permanecer registrado no progresso global");
        assertFalse(sessao.possuiPartida(), "A sessao deve ser encerrada apos finalizar");
    }

    @Test
    void finalizarPartidaSemFinalRegistradoLancaExcecaoENaoGravaNada() throws Exception {
        SessaoGrafica sessao = new SessaoGrafica(new CarregamentoAdapter(pasta), pasta);
        sessao.novaPartida(protagonista(), apresentacaoSemJavaFx());
        assertThrows(IllegalStateException.class, sessao::finalizarPartida);
        assertFalse(Files.exists(GerenciadorSlots.caminhoDoAutosave(pasta)));
        assertTrue(sessao.possuiPartida(), "A partida deve continuar em memoria");
    }
}
