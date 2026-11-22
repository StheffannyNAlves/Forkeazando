package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.view.pixel.Tela;
import java.awt.Font;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/** Rasterização em memória, no executor de arte; não abre janelas AWT. */
final class RasterizadorCenario {
    private static final Font FONTE = new Font(Font.MONOSPACED, Font.BOLD, 10);
    private RasterizadorCenario() {}

    static int[] rasterizar(Tela tela, int escala) {
        int w = tela.largura() * escala, h = tela.altura() * escala;
        BufferedImage imagem = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        int[] dados = ((DataBufferInt) imagem.getRaster().getDataBuffer()).getData();
        for (int y = 0; y < h; y++) for (int x = 0; x < w; x++)
            dados[y * w + x] = 0xFF000000 | tela.get(x / escala, y / escala);
        // A camada textual do terminal antes era descartada na conversão para JavaFX
        var g = imagem.createGraphics();
        try {
            g.setFont(FONTE);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            for (Tela.Texto texto : tela.textos()) {
                for (int i = 0; i < texto.valor().length(); i++) {
                    int x = (texto.coluna() + i) * escala, y = texto.linha() * escala * 2;
                    g.setColor(new Color(tela.get(texto.coluna() + i, texto.linha() * 2)));
                    g.fillRect(x, y, escala, escala * 2);
                    g.setColor(new Color(texto.cor()));
                    g.drawString(String.valueOf(texto.valor().charAt(i)), x, y + escala * 2 - 2);
                }
            }
        } finally { g.dispose(); }
        return dados;
    }
}
