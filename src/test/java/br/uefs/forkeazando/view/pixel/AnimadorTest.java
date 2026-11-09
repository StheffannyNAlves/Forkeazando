package br.uefs.forkeazando.view.pixel;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimadorTest {

    /** 3 linhas x 2 colunas; a cada quadro só a linha 1 muda de cor. */
    private static Animacao tresQuadros() {
        return new Animacao(3, 40, q -> {
            Tela t = new Tela(2, 3);
            t.rect(0, 0, 2, 6, Cor.rgb(5, 5, 5));
            t.rect(0, 2, 2, 2, Cor.rgb(10 * (q + 1), 0, 0));
            return t;
        });
    }

    private static String tocar(Animacao a, List<Long> esperas) throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buf, true, StandardCharsets.UTF_8);
        Animador.tocar(a, out, esperas::add);
        return buf.toString(StandardCharsets.UTF_8);
    }

    @Test
    void esperaUmIntervaloEntreQuadrosEnaoAntesDoPrimeiro() throws Exception {
        List<Long> esperas = new ArrayList<>();
        tocar(tresQuadros(), esperas);
        assertEquals(List.of(40L, 40L), esperas);
    }

    @Test
    void reescreveSomenteAsLinhasQueMudaram() throws Exception {
        String saida = tocar(tresQuadros(), new ArrayList<>());
        // quadros 1 e 2 sobem 3 linhas e reescrevem apenas a linha do meio
        String[] partes = saida.split("\033\\[3A", -1);
        assertEquals(3, partes.length, "dois redesenhos = duas subidas de cursor");
        for (int i = 1; i < partes.length; i++) {
            long reescritas = partes[i].chars().filter(c -> c == '\r').count()
                    - partes[i].split("\r\n", -1).length + 1;   // descarta o \r dos CRLF
            assertEquals(1, reescritas, "só a linha do meio deve ser reescrita");
        }
    }

    @Test
    void escondeOCursorEDevolveMesmoSeADormidaFalhar() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buf, true, StandardCharsets.UTF_8);
        assertThrows(IllegalStateException.class, () -> Animador.tocar(tresQuadros(), out, ms -> {
            throw new IllegalStateException("interrompido");
        }));
        String s = buf.toString(StandardCharsets.UTF_8);
        assertTrue(s.contains("\033[?25l"));
        assertTrue(s.endsWith("\033[?25h"), "o cursor precisa voltar a aparecer");
    }

    @Test
    void animacaoDeUmQuadroSoImprimeELiberaSemMexerNoCursor() throws Exception {
        Animacao a = new Animacao(1, 40, q -> new Tela(2, 2));
        List<Long> esperas = new ArrayList<>();
        String s = tocar(a, esperas);
        assertTrue(esperas.isEmpty());
        assertFalse(s.contains("\033[?25l"));
    }

    @Test
    void mostrarFinalImprimeApenasOUltimoQuadro() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buf, true, StandardCharsets.UTF_8);
        Animador.mostrarFinal(tresQuadros(), out);
        String s = buf.toString(StandardCharsets.UTF_8);
        assertTrue(s.contains("30;0;0"), "cor do último quadro");
        assertFalse(s.contains("10;0;0"));
    }

    @Test
    void parametrosInvalidosSaoRejeitados() {
        assertThrows(IllegalArgumentException.class, () -> new Animacao(0, 10, q -> new Tela(1, 1)));
        assertThrows(IllegalArgumentException.class, () -> new Animacao(2, -1, q -> new Tela(1, 1)));
    }
}
