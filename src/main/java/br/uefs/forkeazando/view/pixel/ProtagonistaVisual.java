package br.uefs.forkeazando.view.pixel;

import java.util.Objects;
import static br.uefs.forkeazando.view.pixel.Cor.*;

/** Desenho compartilhado por todas as cenas; perfil e fase pertencem a um único quadro. */
final class ProtagonistaVisual {
    enum Expressao { NEUTRA, SURPRESA, CANSADA, FELIZ }

    private final PerfilProtagonista perfil;
    private final int fase;
    private static final int CONTORNO = rgb(28, 23, 32);
    private static final int ALCA = rgb(224, 203, 158);

    ProtagonistaVisual(PerfilProtagonista perfil, int fase) {
        this.perfil = Objects.requireNonNull(perfil);
        this.fase = Math.floorMod(fase, 12);
    }

    int pele() { return perfil.pele(); }
    int roupa() { return perfil.roupa(); }
    int cabelo() { return perfil.cabelo(); }
    boolean longo() { return perfil.cabeloComprido(); }
    private int respirar() { return fase >= 4 && fase <= 6 ? -1 : 0; }

    void costas(Tela t, int x, int y, int largura, int altura) {
        costas(t, x, y, largura, altura, perfil.roupa());
    }

    void costas(Tela t, int x, int y, int largura, int altura, int traje) {
        int topo = y + respirar();
        int sombra = mix(traje, CONTORNO, 0.30);
        t.rect(x - largura / 2 - 1, topo + 6, largura + 2, altura + 1, CONTORNO);
        t.rect(x - largura / 2, topo + 6, largura, altura, traje);
        t.rect(x - largura / 2, topo + 6, 2, altura, mix(traje, rgb(255, 227, 178), 0.20));
        t.rect(x + largura / 2 - 2, topo + 7, 2, altura - 1, sombra);
        t.rect(x - 1, topo + 4, 3, 3, pele());
        t.elipse(x + 0.5, topo + 2.5, 3.7, 4, cabelo());
        if (longo()) {
            t.rect(x - 4, topo + 2, 3, Math.min(altura + 4, 15), cabelo());
            t.rect(x + 2, topo + 3, 2, Math.min(altura + 3, 13), cabelo());
            t.rect(x - 3, topo + 3, 1, 9, mix(cabelo(), rgb(132, 83, 61), 0.32));
            t.px(x + 3, topo + 6, rgb(238, 185, 78));
        } else {
            cachos(t, x, topo);
            t.rect(x - 2, topo + 5, 5, 1, mix(cabelo(), pele(), 0.30));
            t.rect(x + 2, topo + 7, 1, Math.max(1, altura - 3), sombra);
        }
        // A alça clara de Tiana e a mochila escura de Miles acompanham os retratos.
        int alca = longo() ? ALCA : rgb(132, 121, 99);
        t.rect(x - largura / 2 + 2, topo + 7, 1, Math.max(1, altura - 1), alca);
    }

    void frente(Tela t, int x, int y, int largura, Expressao expressao) {
        frente(t, x, y, largura, expressao, roupa());
    }

    void frente(Tela t, int x, int y, int largura, Expressao expressao, int traje) {
        int topo = y + respirar();
        t.rect(x - largura / 2 - 1, topo + 8, largura + 2, 15, CONTORNO);
        t.rect(x - largura / 2, topo + 8, largura, 14, traje);
        t.rect(x - largura / 2, topo + 8, largura, 1, mix(traje, rgb(255, 230, 190), 0.24));
        t.rect(x + largura / 2 - 2, topo + 9, 2, 13, mix(traje, CONTORNO, 0.30));
        t.rect(x - 1, topo + 6, 3, 4, mix(pele(), CONTORNO, 0.20));
        if (longo()) {
            t.rect(x - 5, topo + 1, 3, 15, cabelo());
            t.rect(x + 3, topo + 1, 3, 15, cabelo());
            t.rect(x - 4, topo + 3, 1, 10, mix(cabelo(), rgb(126, 83, 65), 0.36));
        } else if (traje == roupa()) {
            t.rect(x - 1, topo + 9, 3, 12, rgb(221, 213, 190));
            t.rect(x - 3, topo + 9, 1, 8, mix(traje, rgb(190, 206, 170), 0.35));
        }
        t.elipse(x + 0.5, topo + 3.5, 3.7, 4.3, pele());
        t.elipse(x + 0.5, topo + 1, 4.1, 2.5, cabelo());
        if (!longo()) cachos(t, x, topo - 1);
        boolean piscar = fase == 9 || expressao == Expressao.CANSADA;
        t.rect(x - 2, topo + 4, piscar ? 2 : 1, 1, CONTORNO);
        t.rect(x + 2, topo + 4, piscar ? 2 : 1, 1, CONTORNO);
        int boca = mix(pele(), rgb(113, 44, 45), 0.65);
        t.rect(x, topo + 6, 2, expressao == Expressao.SURPRESA ? 2 : 1, boca);
        if (expressao == Expressao.FELIZ) t.px(x + 2, topo + 5, boca);
        if (longo()) {
            t.px(x - 4, topo + 6, rgb(243, 190, 77));
            t.px(x + 4, topo + 6, rgb(243, 190, 77));
        }
        t.rect(x - largura / 2 + 2, topo + 9, 1, 12, longo() ? ALCA : rgb(132, 121, 99));
    }

    void pessoa(Tela t, int x, int yPe, int passo) {
        int perna = perfil.calca();
        t.rect(x - 2, yPe - 6, 2, 6 - passo, perna);
        t.rect(x + 1, yPe - 6, 2, 5 + passo, mix(perna, CONTORNO, 0.25));
        t.rect(x - 3, yPe - 1 - passo, 3, 1, rgb(218, 209, 190));
        t.rect(x + 1, yPe - 1 + passo, 3, 1, rgb(218, 209, 190));
        t.rect(x - 3, yPe - 11, 7, 6, roupa());
        t.rect(x - 3, yPe - 11, 1, 6, mix(roupa(), rgb(255, 230, 180), 0.24));
        t.rect(x - 4, yPe - 10, 1, 5, longo() ? pele() : roupa());
        t.rect(x + 4, yPe - 10, 1, 5, longo() ? pele() : roupa());
        t.rect(x - 2, yPe - 15, 5, 5, pele());
        t.rect(x - 2, yPe - 15, 5, 2, cabelo());
        if (longo()) {
            t.rect(x - 3, yPe - 14, 2, 9, cabelo());
            t.rect(x + 2, yPe - 14, 1, 8, cabelo());
            t.px(x + 3, yPe - 11, rgb(238, 185, 78));
        } else {
            for (int i = -2; i <= 2; i += 2) t.rect(x + i, yPe - 16 - Math.abs(i) % 3 / 2, 2, 2, cabelo());
            t.rect(x, yPe - 10, 2, 5, rgb(221, 213, 190));
        }
        t.px(x + 1, yPe - 12, CONTORNO);
        t.rect(x - 2, yPe - 10, 1, 6, longo() ? ALCA : rgb(132, 121, 99));
    }

    void mao(Tela t, int x, int y, int largura) {
        t.rect(x - 3, y, 4, 4, roupa());
        t.rect(x, y, largura, 3, pele());
        t.rect(x + 1, y - 1, Math.max(1, largura - 3), 1, mix(pele(), rgb(255, 217, 160), 0.25));
    }

    void alcancar(Tela t, int x, int y, int destinoX, int destinoY, boolean palma) {
        int cotoveloX = (x + destinoX) / 2 + 3;
        int cotoveloY = (y + destinoY) / 2 + 4;
        segmento(t, x, y, cotoveloX, cotoveloY, roupa());
        segmento(t, cotoveloX, cotoveloY, destinoX, destinoY, pele());
        t.rect(destinoX - 1, destinoY - 1, palma ? 5 : 3, palma ? 4 : 2, pele());
        t.px(destinoX + 1, destinoY - 2, mix(pele(), rgb(255, 219, 170), 0.20));
    }

    private static void segmento(Tela t, int x0, int y0, int x1, int y1, int cor) {
        int passos = Math.max(1, Math.max(Math.abs(x1 - x0), Math.abs(y1 - y0)));
        for (int i = 0; i <= passos; i++)
            t.rect(x0 + (x1 - x0) * i / passos, y0 + (y1 - y0) * i / passos, 2, 2, cor);
    }

    void dormindo(Tela t, int x, int y) {
        t.elipse(x, y, longo() ? 5.5 : 3.5, 2.6, cabelo());
        t.rect(x + 2, y, 3, 2, pele());
        if (longo()) t.rect(x - 5, y, 5, 5, cabelo());
        else cachos(t, x - 1, y - 1);
    }

    /** Perfil maior usado no SISU; reação e luz pertencem à cena, a aparência ao personagem. */
    void matricula(Tela t, boolean surpresa, double luz) {
        int peleLuz = mix(pele(), rgb(151, 184, 241), 0.20 * luz);
        int brilhoRoupa = mix(roupa(), rgb(170, 194, 241), 0.25 * luz);
        for (int y = 27; y < 48; y++) {
            int meia = 7 + (int) ((y - 27) * 0.55);
            t.rect(15 - meia, y, meia * 2 + 3, 1, roupa());
            t.rect(15 + meia, y, 2, 1, brilhoRoupa);
        }
        if (!longo()) t.rect(18, 29, 3, 14, rgb(221, 213, 190));
        t.rect(17, 25, 5, 5, mix(pele(), CONTORNO, 0.30));
        t.elipse(21, 19, 6.6, 7.6, peleLuz);
        t.rect(26, 17, 3, 6, peleLuz);
        t.px(29, 21, peleLuz);
        t.rect(24, surpresa ? 14 : 15, 4, 1, CONTORNO);
        boolean piscar = fase == 9 && !surpresa;
        t.rect(26, 18, 2, piscar ? 1 : 2, CONTORNO);
        if (!piscar) t.px(27, 18, rgb(244, 227, 195));
        t.rect(25, 23, 3, surpresa ? 2 : 1, rgb(108, 44, 43));
        t.elipse(19, 12, 7.6, 5, cabelo());
        if (longo()) {
            t.rect(11, 13, 7, 20, cabelo());
            t.rect(12, 18, 1, 14, mix(cabelo(), rgb(133, 82, 58), 0.40));
            t.rect(22, 11, 6, 3, cabelo());
            t.rect(19, 21, 2, 4, rgb(238, 185, 78));
            t.px(20, 23, peleLuz);
        } else {
            for (int i = 0; i < 7; i++) t.elipse(13 + i * 2, 9 + (i % 3), 2, 2.2, cabelo());
            t.rect(14, 13, 3, 7, cabelo());
            t.px(16, 9, mix(cabelo(), rgb(97, 91, 103), 0.55));
        }
        t.rect(21, 33, 9, 3, roupa());
        t.rect(21, 33, 9, 1, brilhoRoupa);
        mao(t, 29, 34, 3);
    }

    private void cachos(Tela t, int x, int y) {
        t.rect(x - 3, y, 7, 2, cabelo());
        for (int[] cacho : new int[][]{{-3, 0}, {-1, -2}, {1, -3}, {3, -1}, {4, 1}})
            t.elipse(x + cacho[0] + 0.5, y + cacho[1] + 0.5, 1.4, 1.5, cabelo());
        int reflexo = mix(cabelo(), rgb(91, 85, 102), 0.40);
        t.px(x - 1, y - 2, reflexo);
        t.px(x + 2, y - 2, reflexo);
        t.px(x + 4, y, reflexo);
    }
}
