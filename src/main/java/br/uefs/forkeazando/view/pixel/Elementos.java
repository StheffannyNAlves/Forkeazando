package br.uefs.forkeazando.view.pixel;

import static br.uefs.forkeazando.view.pixel.Cor.mix;
import static br.uefs.forkeazando.view.pixel.Cor.rgb;

/** Peças de cenário reutilizáveis pelas ilustrações (pessoas, luz, notebook, letras). */
final class Elementos {

    private Elementos() {}

    static void gradV(Tela t, int x, int y, int w, int h, int c0, int c1) {
        for (int j = 0; j < h; j++) {
            t.rect(x, y + j, w, 1, mix(c0, c1, h <= 1 ? 0 : j / (double) (h - 1)));
        }
    }

    /** Brilho radial suave (luz de tela, lâmpada, sol). */
    static void glow(Tela t, double cx, double cy, double rx, double ry, int cor, double forca) {
        if (forca <= 0) return;
        for (int y = (int) (cy - ry); y <= (int) (cy + ry); y++) {
            for (int x = (int) (cx - rx); x <= (int) (cx + rx); x++) {
                double dx = (x - cx) / rx, dy = (y - cy) / ry;
                double d = Math.sqrt(dx * dx + dy * dy);
                if (d < 1) t.tint(x, y, cor, forca * (1 - d));
            }
        }
    }

    /**
     * Pessoa de frente, ~14 px de altura. (x = centro, yPe = linha dos pés.)
     * braco: 0 = caído, 1 = erguido (direita), 2 = os dois erguidos.
     */
    static void pessoa(Tela t, int x, int yPe, int pele, int cabelo, int roupa, int calca, int braco) {
        t.rect(x - 1, yPe - 3, 2, 3, calca);
        t.rect(x + 1, yPe - 3, 2, 3, calca);
        t.rect(x - 2, yPe - 9, 6, 6, roupa);
        t.rect(x - 2, yPe - 9, 1, 6, mix(roupa, rgb(255, 255, 255), 0.18));
        t.rect(x + 3, yPe - 9, 1, 6, mix(roupa, rgb(0, 0, 0), 0.25));
        if (braco >= 1) { t.rect(x + 4, yPe - 12, 1, 5, roupa); t.px(x + 4, yPe - 13, pele); }
        else t.rect(x + 4, yPe - 8, 1, 4, roupa);
        if (braco >= 2) { t.rect(x - 3, yPe - 12, 1, 5, roupa); t.px(x - 3, yPe - 13, pele); }
        else t.rect(x - 3, yPe - 8, 1, 4, roupa);
        t.rect(x - 1, yPe - 13, 4, 4, pele);
        t.rect(x - 1, yPe - 14, 4, 2, cabelo);
        t.px(x - 2, yPe - 13, cabelo);
    }

    /** Pessoa de costas (cabeça + ombros), com a base cortada por uma mesa/carteira. */
    static void costas(Tela t, int x, int yTopoCabeca, int pele, int cabelo, int roupa, int largura, int altura) {
        t.rect(x - largura / 2, yTopoCabeca + 6, largura, altura, roupa);
        t.rect(x - largura / 2, yTopoCabeca + 6, largura, 1, mix(roupa, rgb(255, 255, 255), 0.2));
        t.rect(x - 1, yTopoCabeca + 5, 3, 2, pele);
        t.elipse(x + 0.5, yTopoCabeca + 2.5, 3.2, 3.6, cabelo);
    }

    static void notebook(Tela t, int x, int y, int w, int h, int telaCor) {
        t.rect(x, y, w, h, rgb(40, 44, 70));
        t.rect(x + 1, y + 1, w - 2, h - 2, telaCor);
        t.rect(x - 2, y + h, w + 4, 2, rgb(150, 156, 176));
        t.rect(x - 2, y + h, w + 4, 1, rgb(210, 214, 230));
    }

    // letras 5x7 usadas em desenhos (dedo no vidro, etc.)
    private static final String[] LETRA_U = {"X...X", "X...X", "X...X", "X...X", "X...X", "X...X", ".XXX."};
    private static final String[] LETRA_E = {"XXXXX", "X....", "X....", "XXXX.", "X....", "X....", "XXXXX"};
    private static final String[] LETRA_R = {"XXXX.", "X...X", "X...X", "XXXX.", "X.X..", "X..X.", "X...X"};

    static String[] letra(char c) {
        return switch (c) { case 'U' -> LETRA_U; case 'E' -> LETRA_E; default -> LETRA_R; };
    }

    /** Desenha a letra revelando só as primeiras `colunas` colunas (traço do dedo). */
    static void letraParcial(Tela t, int x, int y, char c, int colunas, int cor) {
        String[] g = letra(c);
        for (int j = 0; j < g.length; j++) {
            for (int i = 0; i < Math.min(colunas, 5); i++) {
                if (g[j].charAt(i) == 'X') t.rect(x + i, y + j, 1, 1, cor);
            }
        }
    }

    /**
     * Pessoa sentada de frente (busto): cabeça com rosto simples + ombros.
     * longo = cabelo comprido caindo pelos lados; oculos = aro escuro sobre os olhos.
     */
    static void frente(Tela t, int x, int yTopo, int pele, int cabelo, int roupa, int largura, boolean longo, boolean oculos) {
        t.rect(x - largura / 2, yTopo + 8, largura, 14, roupa);
        t.rect(x - largura / 2, yTopo + 8, largura, 1, mix(roupa, rgb(255, 255, 255), 0.2));
        t.rect(x - 1, yTopo + 6, 3, 3, mix(pele, rgb(0, 0, 0), 0.2));
        if (longo) {
            t.rect(x - 4, yTopo + 2, 2, 10, cabelo);
            t.rect(x + 3, yTopo + 2, 2, 10, cabelo);
        }
        t.elipse(x + 0.5, yTopo + 3.5, 3.6, 4.2, pele);
        t.elipse(x + 0.5, yTopo + 1.5, 3.8, 2.6, cabelo);
        t.px(x - 2, yTopo + 4, cabelo);
        t.px(x + 3, yTopo + 4, cabelo);
        t.px(x - 1, yTopo + 4, rgb(24, 20, 28));
        t.px(x + 2, yTopo + 4, rgb(24, 20, 28));
        if (oculos) {
            t.rect(x - 2, yTopo + 4, 2, 1, rgb(18, 16, 32));
            t.rect(x + 1, yTopo + 4, 2, 1, rgb(18, 16, 32));
        }
        t.rect(x, yTopo + 6, 2, 1, mix(pele, rgb(120, 40, 50), 0.5));
    }
}