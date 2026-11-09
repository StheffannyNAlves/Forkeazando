package br.uefs.forkeazando.view.pixel;

import org.junit.jupiter.api.Test;

import static br.uefs.forkeazando.view.pixel.TelaTest.semAnsi;
import static org.junit.jupiter.api.Assertions.*;

class RetratosTest {

    @Test
    void retratoTemTamanhoFixoDe24x12() {
        for (String expr : new String[]{Retratos.NEUTRA, Retratos.TORTO, Retratos.LADO}) {
            String[] linhas = Retratos.linhas(Retratos.CAIO, expr).orElseThrow();
            assertEquals(Retratos.LINHAS, linhas.length);
            for (String l : linhas) {
                assertEquals(Retratos.LARGURA, semAnsi(l).length());
            }
        }
    }

    @Test
    void mesmaAparenciaEmTodasAsExpressoesSoORostoMuda() {
        Tela neutra = Retratos.caio(Retratos.NEUTRA);
        for (String expr : new String[]{Retratos.TORTO, Retratos.LADO}) {
            Tela outra = Retratos.caio(expr);
            boolean mudouAlgo = false;
            for (int y = 0; y < 24; y++) {
                for (int x = 0; x < 24; x++) {
                    boolean noRosto = y >= Retratos.ROSTO_Y_MIN && y <= Retratos.ROSTO_Y_MAX;
                    if (!noRosto) {
                        assertEquals(neutra.get(x, y), outra.get(x, y),
                                "pixel fora do rosto mudou em " + expr + " (" + x + "," + y + ")");
                    } else if (neutra.get(x, y) != outra.get(x, y)) {
                        mudouAlgo = true;
                    }
                }
            }
            assertTrue(mudouAlgo, expr + " deveria diferir da neutra");
        }
    }

    @Test
    void expressaoDesconhecidaOuNulaCaiNaNeutra() {
        String[] neutra = Retratos.linhas(Retratos.CAIO, Retratos.NEUTRA).get();
        assertArrayEquals(neutra, Retratos.linhas(Retratos.CAIO, "furioso").get());
        assertArrayEquals(neutra, Retratos.linhas(Retratos.CAIO, null).get());
    }

    @Test
    void personagemSemRetratoDevolveVazioSemErro() {
        assertTrue(Retratos.linhas("narrador", null).isEmpty());
        assertTrue(Retratos.linhas(null, null).isEmpty());
        assertFalse(Retratos.existe("ninguem"));
        assertTrue(Retratos.existe(Retratos.CAIO));
    }
}
