package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.adapter.CarregamentoAdapter;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.view.ApresentacaoCena;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SessaoGraficaCarregamentoTest {
    @TempDir Path pasta;

    private static Protagonista personagem(String nome) {
        return new Protagonista(nome, IdentidadeProtagonista.STHEFFANNY,
                false, false, false,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
    }

    private static ApresentacaoCena apresentacaoSemInterfaceGrafica() {
        return (ApresentacaoCena) Proxy.newProxyInstance(
                ApresentacaoCena.class.getClassLoader(),
                new Class<?>[]{ApresentacaoCena.class},
                (proxy, metodo, argumentos) -> null);
    }

    private static Estado estado(String nome) {
        Estado resultado = new Estado();
        resultado.iniciar(personagem(nome));
        resultado.setCenaAtualId(700);
        return resultado;
    }

    @Test
    void falhaNaLeituraDoProgressoPreservaPartidaEControladorAnterior() throws Exception {
        SessaoGrafica sessao = new SessaoGrafica(new CarregamentoAdapter(pasta), pasta);
        sessao.novaPartida(personagem("Atual"));
        ApresentacaoCena apresentacao = apresentacaoSemInterfaceGrafica();
        sessao.inicializarControladorPartida(apresentacao);
        Estado anterior = sessao.estado();

        // Falha ao ler progresso permanente: nao deve substituir a sessao atual
        Files.writeString(pasta.resolve("progresso.json"), "{\"conquistas\": 1}");
        assertThrows(DadosInvalidosException.class,
                () -> sessao.carregarPartida(estado("Outra"), apresentacao));
        assertSame(anterior, sessao.estado());
        assertDoesNotThrow(sessao::prepararCenaAtual,
                "O controlador da partida anterior deve continuar operacional");
    }

    @Test
    void carregamentoValidoPreparaControladorEPermiteProsseguir() throws Exception {
        SessaoGrafica sessao = new SessaoGrafica(new CarregamentoAdapter(pasta), pasta);
        sessao.novaPartida(personagem("Atual"));
        Estado carregado = estado("Carregado");

        assertDoesNotThrow(() -> sessao.carregarPartida(carregado, apresentacaoSemInterfaceGrafica()));
        assertSame(carregado, sessao.estado());
        assertDoesNotThrow(sessao::prepararCenaAtual);
    }
}
