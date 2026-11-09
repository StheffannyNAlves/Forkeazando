package br.uefs.forkeazando.view.pixel;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static br.uefs.forkeazando.view.pixel.TelaTest.semAnsi;
import static org.junit.jupiter.api.Assertions.*;

class IlustracoesTest {

    @Test
    void todasTemAlturaELarguraDaCaixaDoCenaView() {
        for (String chave : Ilustracoes.chaves()) {
            String[] linhas = Ilustracoes.linhas(chave);
            assertEquals(Ilustracoes.LINHAS, linhas.length, chave);
            for (String l : linhas) {
                assertEquals(Ilustracoes.LARGURA, semAnsi(l).length(), chave);
            }
        }
    }

    @Test
    void todasTemAnimacaoEOUltimoQuadroEAArteEstatica() {
        for (String chave : Ilustracoes.chaves()) {
            Animacao a = Ilustracoes.animacao(chave).orElseThrow(() -> new AssertionError("sem animação: " + chave));
            assertArrayEquals(Ilustracoes.linhas(chave), a.renderizar(a.total() - 1),
                    "o último quadro de " + chave + " deve ser a arte final");
        }
    }

    @Test
    void osQuadrosRealmenteMudamAoLongoDaAnimacao() {
        for (String chave : Ilustracoes.chaves()) {
            Animacao a = Ilustracoes.animacao(chave).get();
            assertFalse(Arrays.equals(a.renderizar(0), a.renderizar(a.total() - 1)),
                    "primeiro e último quadro de " + chave + " não podem ser iguais");
        }
    }

    @Test
    void animacaoEDeterministicaMesmoQuadroMesmaImagem() {
        for (String chave : Ilustracoes.chaves()) {
            Animacao a = Ilustracoes.animacao(chave).get();
            assertArrayEquals(a.renderizar(3), a.renderizar(3), chave);
        }
    }

    @Test
    void duracaoTotalDaEntradaFicaCurta() {
        for (String chave : Ilustracoes.chaves()) {
            Animacao a = Ilustracoes.animacao(chave).get();
            assertTrue(a.total() * a.intervaloMs() <= 2500, chave + " demora demais");
        }
    }

    @Test
    void chaveDesconhecidaNaoTemAnimacaoNemArte() {
        assertTrue(Ilustracoes.animacao("nao_existe").isEmpty());
        assertFalse(Ilustracoes.existe("nao_existe"));
    }

    @Test
    void quadroForaDoIntervaloELancado() {
        Animacao a = Ilustracoes.animacao(Ilustracoes.CAP1_PATIO).get();
        assertThrows(IndexOutOfBoundsException.class, () -> a.desenhar(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> a.desenhar(a.total()));
    }
}
