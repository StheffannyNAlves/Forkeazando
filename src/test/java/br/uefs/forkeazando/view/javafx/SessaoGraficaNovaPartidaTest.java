package br.uefs.forkeazando.view.javafx;

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

class SessaoGraficaNovaPartidaTest {
    @TempDir Path pasta;

    private static Protagonista protagonista(String nome) {
        return new Protagonista(nome, IdentidadeProtagonista.STHEFFANNY,
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
    void erroNoProgressoNaoDescartaPartidaAnterior() throws Exception {
        SessaoGrafica sessao = new SessaoGrafica(new CarregamentoAdapter(pasta), pasta);
        ApresentacaoCena apresentacao = apresentacaoSemJavaFx();
        sessao.novaPartida(protagonista("Primeira"), apresentacao);
        Estado anterior = sessao.estado();

        // Arquivo invalido somente no diretorio temporario deste teste
        Files.writeString(pasta.resolve("progresso.json"), "{\"conquistas\": 1}");
        assertThrows(DadosInvalidosException.class,
                () -> sessao.novaPartida(protagonista("Segunda"), apresentacao));

        assertSame(anterior, sessao.estado(), "A partida anterior nao deve ser substituida");
        assertEquals("Primeira", sessao.estado().getProtagonista().getNome());
        assertDoesNotThrow(sessao::prepararCenaAtual,
                "O controlador antigo deve continuar funcional");
    }

    @Test
    void novaPartidaValidaJaFicaPreparadaParaJogar() throws Exception {
        SessaoGrafica sessao = new SessaoGrafica(new CarregamentoAdapter(pasta), pasta);
        ApresentacaoCena apresentacao = apresentacaoSemJavaFx();
        sessao.novaPartida(protagonista("Primeira"), apresentacao);
        Estado anterior = sessao.estado();

        sessao.novaPartida(protagonista("Segunda"), apresentacao);

        assertNotSame(anterior, sessao.estado());
        assertEquals("Segunda", sessao.estado().getProtagonista().getNome());
        assertEquals(700, sessao.estado().getCenaAtualId());
        assertDoesNotThrow(sessao::prepararCenaAtual);
    }
}
