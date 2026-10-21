package br.uefs.forkeazando.view.pixel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
 class PixelTest {

    @Test
   void nenhumPixelFicaSemPintarEmNenhumQuadroDeNenhumaIlustracao() {
        for (String chave : Ilustracoes.chaves()) {
            Animacao animacao = Ilustracoes.animacao(chave).orElseThrow();
            for (int q = 0; q < animacao.total(); q++) {
                Tela tela = animacao.desenhar(q);
                int semPintar = 0;
                for (int y = 0; y < tela.altura(); y++) {
                    for (int x = 0; x < tela.largura(); x++) {
                        if (tela.get(x, y) == 0) semPintar++;
                    }
                }
                assertEquals(0, semPintar, chave + ", quadro " + q + ": pixels sem pintar");
            }
        }
    }

}
