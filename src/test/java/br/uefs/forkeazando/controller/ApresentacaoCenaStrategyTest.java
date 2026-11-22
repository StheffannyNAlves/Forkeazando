package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Progresso;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.view.ApresentacaoCena;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Strategy: o CenasController (contexto) recebe uma ApresentacaoCena (estratégia) e produz
 * o mesmo resultado no jogo, qualquer que seja a implementação usada para exibir(terminal ou JavaFx)
 */
class ApresentacaoCenaStrategyTest {
    @TempDir Path pasta;

    /** Resultado observável de uma execução: estado do jogo e cenas que a estratégia exibiu. */
    private record Execucao(int score, int cenaFinal, List<Integer> cenasExibidas) { }

    private Execucao executarFluxoCom(ApresentacaoCena apresentacao, List<Integer> cenasExibidas) {
        Protagonista p = new Protagonista("Ana", IdentidadeProtagonista.STHEFFANNY, false, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
        Estado estado = new Estado();
        estado.iniciar(p);
        estado.setCenaAtualId(0);

        Escolha estudar = new Escolha.Builder("Estudar", 1).comScoreGanho(5).build();
        Cena inicio = new Cena(0, "Capítulo 1", "Narrador", "Texto 0");
        inicio.adicionarEscolha(estudar);
        Cena proxima = new Cena(1, "Capítulo 1", "Narrador", "Texto 1");
        proxima.adicionarEscolha(new Escolha.Builder("Fim", 1).build());

        CenasController controller = new CenasController(estado, List.of(inicio, proxima), apresentacao,
                new Progresso(), new GerenciadorProgresso(pasta), e -> { }, new GatilhoAutosave(1000), () -> { });

        controller.prepararCenaAtual();
        controller.registrarEscolha(estudar);
        controller.prepararCenaAtual();

        return new Execucao(p.getScore(), estado.getCenaAtualId(), cenasExibidas);
    }

    /** Segunda estratégia, totalmente diferente da primeira: só registra o que lhe pediram */
    private static ApresentacaoCena estrategiaQueRegistra(List<Integer> cenasExibidas) {
        return (ApresentacaoCena) Proxy.newProxyInstance(
                ApresentacaoCena.class.getClassLoader(),
                new Class<?>[]{ApresentacaoCena.class},
                (proxy, metodo, args) -> {
                    if (metodo.getName().equals("renderizar")) {
                        cenasExibidas.add(((Cena) args[0]).getId());
                    }
                    return null;
                });
    }

    @Test
    void mesmoFluxoProduzMesmoEstadoComDuasEstrategiasDeApresentacao() {
        CenaViewFalsa terminalFalso = new CenaViewFalsa();
        Execucao comTerminal = executarFluxoCom(terminalFalso, terminalFalso.cenasRenderizadas);

        List<Integer> exibidasPelaSegunda = new ArrayList<>();
        Execucao comOutra = executarFluxoCom(estrategiaQueRegistra(exibidasPelaSegunda), exibidasPelaSegunda);

        assertEquals(5, comTerminal.score());
        assertEquals(comTerminal.score(), comOutra.score(), "As regras não podem depender da apresentação");
        assertEquals(1, comTerminal.cenaFinal());
        assertEquals(comTerminal.cenaFinal(), comOutra.cenaFinal());
        assertEquals(List.of(0, 1), comTerminal.cenasExibidas());
        assertEquals(List.of(0, 1), comOutra.cenasExibidas(), "Cada estratégia recebe as mesmas cenas para exibir");
    }
}
