package br.uefs.forkeazando.view.pixel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TelaTest {

    static String semAnsi(String s) {
        return s.replaceAll("\u001b\\[[0-9;?]*[A-Za-z]", "");
    }

    @Test
    void dimensoesSaoLarguraPorDoisPixelsPorLinha() {
        Tela t = new Tela(10, 4);
        assertEquals(10, t.largura());
        assertEquals(4, t.linhas());
        assertEquals(8, t.altura());
    }

    @Test
    void pixelsForaDaGradeSaoIgnorados() {
        Tela t = new Tela(4, 2);
        assertDoesNotThrow(() -> {
            t.px(-1, 0, 1);
            t.px(4, 0, 1);
            t.px(0, 4, 1);
            t.rect(-3, -3, 20, 20, Cor.rgb(1, 2, 3));
        });
        assertEquals(0, t.get(99, 99));
        assertEquals(Cor.rgb(1, 2, 3), t.get(3, 3));
    }

    @Test
    void renderizarGeraUmaLinhaPorLinhaDeTextoComLarguraExata() {
        Tela t = new Tela(7, 3);
        t.rect(0, 0, 7, 6, Cor.rgb(10, 20, 30));
        String[] linhas = t.renderizar();
        assertEquals(3, linhas.length);
        for (String l : linhas) {
            assertEquals(7, semAnsi(l).length());
            assertTrue(l.endsWith("\033[0m"));
        }
    }

    @Test
    void celulaDeTextoUsaOPixelDeCimaComoFundo() {
        Tela t = new Tela(3, 1);
        int cima = Cor.rgb(200, 0, 0), baixo = Cor.rgb(0, 0, 200);
        t.rect(0, 0, 3, 1, cima);
        t.rect(0, 1, 3, 1, baixo);
        t.texto(1, 0, "X", Cor.rgb(255, 255, 255));
        String linha = t.renderizar()[0];
        assertEquals("▀X▀", semAnsi(linha));
        // o fundo da célula do "X" é o vermelho de cima, não o azul de baixo
        int ix = linha.indexOf('X');
        String antes = linha.substring(0, ix);
        assertTrue(antes.lastIndexOf("48;2;200;0;0") > antes.lastIndexOf("48;2;0;0;200"));
    }

    @Test
    void naoRepeteCodigoDeCorQuandoOPixelSeguinteEIgual() {
        Tela t = new Tela(5, 1);
        t.rect(0, 0, 5, 2, Cor.rgb(9, 9, 9));
        String linha = t.renderizar()[0];
        assertEquals(1, linha.split("38;2;9;9;9", -1).length - 1);
    }

    @Test
    void textoForaDaTelaOuDaLinhaEIgnorado() {
        Tela t = new Tela(3, 1);
        assertDoesNotThrow(() -> {
            t.texto(-2, 0, "abcdef", 0);
            t.texto(0, 5, "x", 0);
            t.texto(0, -1, "x", 0);
        });
        assertEquals(3, semAnsi(t.renderizar()[0]).length());
    }

    @Test
    void misturaDeCoresNosExtremos() {
        int a = Cor.rgb(10, 20, 30), b = Cor.rgb(200, 100, 50);
        assertEquals(a, Cor.mix(a, b, 0));
        assertEquals(b, Cor.mix(a, b, 1));
    }
}
