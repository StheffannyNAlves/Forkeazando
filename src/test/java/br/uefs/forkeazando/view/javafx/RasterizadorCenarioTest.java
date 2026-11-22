package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.view.pixel.Tela;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RasterizadorCenarioTest {
    @Test void camadaTextualApareceNosPixelsSemModificarAFonte() {
        Tela fonte = new Tela(20, 4);
        fonte.rect(0, 0, 20, 8, 0x102030);
        fonte.texto(2, 1, "UEFP", 0xffdd88);
        int[] imagem = RasterizadorCenario.rasterizar(fonte, 6);
        assertEquals(120 * 48, imagem.length);
        assertTrue(java.util.Arrays.stream(imagem).anyMatch(pixel -> pixel == 0xffffdd88));
        assertEquals(0x102030, fonte.get(3, 3));
        assertEquals(0xff102030, imagem[0]);
    }
}
