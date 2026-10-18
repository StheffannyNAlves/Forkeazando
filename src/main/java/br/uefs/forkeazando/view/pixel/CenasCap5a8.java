package br.uefs.forkeazando.view.pixel;

import static br.uefs.forkeazando.view.pixel.Cor.gradiente;
import static br.uefs.forkeazando.view.pixel.Cor.mix;
import static br.uefs.forkeazando.view.pixel.Cor.rgb;
import static br.uefs.forkeazando.view.pixel.Elementos.*;

/** Capítulos 5 a 8: greve, freela, o quarto fechado, o RU esfriando, TCC, defesa, formatura e o portão. */
final class CenasCap5a8 {

    static final int N_GREVE = 12, N_FREELA = 12, N_QUARTO = 14, N_RU6 = 10;
    static final int N_TCC = 12, N_DEFESA = 12, N_FORMATURA = 14, N_PORTAO = 14;
    private static final int L = Ilustracoes.LARGURA, A = Ilustracoes.LINHAS;

    private static final int PELE_P = rgb(120, 76, 56), CAB_P = rgb(26, 20, 40), ROXO = rgb(92, 62, 112);

    private CenasCap5a8() {}

    // ------------------------------------------------------------------
    //  Cenas 451 / 460 — greve: o RU fechado sob chuva, faixa balançando.
    // ------------------------------------------------------------------
    static Tela greve(int q) {
        Tela t = new Tela(L, A);
        for (int y = 0; y < 36; y++) t.rect(0, y, L, 1, gradiente(y, new int[]{0, 35}, new int[][]{{104, 112, 128}, {160, 166, 178}}));
        t.rect(4, 12, 58, 24, rgb(200, 196, 186));
        t.rect(4, 12, 58, 1, rgb(226, 222, 212));
        t.rect(16, 18, 34, 18, rgb(122, 126, 138));
        for (int y = 19; y < 36; y += 2) t.rect(16, y, 34, 1, rgb(96, 100, 112));
        int sw = (q % 4 < 2) ? 0 : 1;   // cadeado balança
        t.rect(31 + sw, 29, 3, 3, rgb(240, 200, 60));
        t.rect(31 + sw, 27, 3, 1, rgb(150, 150, 160));
        for (int x = 0; x < 52; x++) {
            int off = (int) Math.round(Math.sin((x + q * 2) * 0.35));
            t.rect(7 + x, 4 + off, 1, 8, rgb(196, 40, 52));
        }
        t.texto(10, 3, "GREVE – RU FECHADO", rgb(255, 255, 255));
        for (int i = 0; i < 34; i++) {   // chuva
            int x = (i * 7 + q * 2) % L, y = (i * 11 + q * 5) % 36;
            t.rect(x, y, 1, 3, rgb(210, 220, 240));
        }
        for (int y = 36; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(96, 100, 112), rgb(60, 62, 76), (y - 36) / 12.0));
        t.rect(8, 40, 22, 1, rgb(140, 150, 176));
        t.rect(40, 43, 18, 1, rgb(140, 150, 176));
        // guarda-chuva e protagonista de costas
        t.elipse(54, 33, 9, 5, rgb(50, 52, 84));
        t.rect(54, 33, 1, 12, rgb(30, 30, 40));
        costas(t, 54, 37, PELE_P, CAB_P, ROXO, 10, 11);
        return t;
    }

    // ------------------------------------------------------------------
    //  Cenas 500–505 — freela: o site pronto, o cliente "visualizou" e sumiu.
    // ------------------------------------------------------------------
    static Tela freela(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 34, rgb(40, 28, 36), rgb(66, 44, 44));
        glow(t, 8, 4, 22, 16, rgb(255, 200, 120), 0.30);
        t.rect(5, 6, 36, 26, rgb(40, 44, 70));
        t.rect(6, 7, 34, 24, rgb(248, 244, 236));
        t.rect(6, 7, 34, 4, rgb(220, 110, 50));
        t.rect(9, 14, 28, 8, rgb(150, 90, 60));
        t.rect(9, 14, 14, 8, rgb(190, 130, 80));
        for (int i = 0; i < 3; i++) t.rect(9, 24 + i * 2, 26 - i * 6, 1, rgb(170, 160, 150));
        t.rect(3, 32, 40, 2, rgb(150, 156, 176));
        // celular com as mensagens
        t.rect(46, 4, 17, 32, rgb(20, 20, 30));
        t.rect(47, 5, 15, 30, rgb(226, 222, 214));
        t.rect(47, 5, 15, 3, rgb(40, 140, 110));
        for (int m = 0; m < 3; m++) {
            if (q >= 2 + m * 3) {
                int y = 10 + m * 8;
                t.rect(50, y, 11, 5, rgb(190, 238, 170));
                t.rect(52, y + 1, 6, 1, rgb(90, 110, 90));
                if (q >= 4 + m * 3 || q == N_FREELA - 1) {   // visualizado
                    t.rect(57, y + 3, 2, 1, rgb(40, 140, 230));
                    t.rect(59, y + 3, 2, 1, rgb(40, 140, 230));
                }
            }
        }
        for (int y = 34; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(70, 46, 40), rgb(38, 26, 28), (y - 34) / 14.0));
        t.rect(0, 34, L, 1, rgb(120, 84, 70));
        costas(t, 28, 33, PELE_P, CAB_P, ROXO, 14, 14);
        t.rect(21, 37, 3, 11, CAB_P);
        t.rect(54, 38, 5, 5, rgb(220, 220, 232));
        t.tint(55 + (q % 2), 36 - (q % 3), rgb(255, 255, 255), 0.35);
        return t;
    }

    // ------------------------------------------------------------------
    //  Cenas 550–554 — quarto fechado: o sol atravessa a parede e some.
    // ------------------------------------------------------------------
    static Tela quarto(int q) {
        double k = q / (double) (N_QUARTO - 1);   // 0 = tarde, 1 = noite
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 34, rgb(226, 204, 170), rgb(206, 182, 148));
        t.rect(6, 4, 14, 14, rgb(96, 76, 60));
        t.rect(7, 5, 12, 12, rgb(250, 226, 160));
        t.rect(12, 5, 1, 12, rgb(96, 76, 60));
        t.rect(3, 4, 4, 16, rgb(150, 86, 96));
        t.rect(19, 4, 4, 16, rgb(150, 86, 96));
        // cama + vulto debaixo do cobertor, cabelo liso no travesseiro
        t.rect(4, 26, 58, 14, rgb(90, 66, 52));
        t.rect(6, 22, 54, 12, rgb(236, 230, 222));
        t.rect(8, 21, 12, 6, rgb(250, 248, 244));
        t.rect(10, 22, 9, 4, CAB_P);
        t.elipse(30, 31, 16, 6, rgb(86, 70, 112));
        t.elipse(30, 30, 15, 4.5, rgb(104, 86, 132));
        t.rect(24, 28, 12, 2, rgb(124, 106, 150));
        t.rect(4, 38, 58, 2, rgb(120, 84, 60));
        for (int y = 40; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(120, 86, 62), rgb(70, 50, 40), (y - 40) / 8.0));
        t.rect(48, 41, 12, 5, rgb(60, 60, 84));   // mochila no chão
        t.rect(8, 42, 12, 3, rgb(60, 64, 80));    // notebook fechado
        // faixa de sol na parede/cama, desliza e some
        double forca = 0.5 * (1 - k);
        for (int y = 5; y < 36; y++) {
            int x0 = 20 + (int) ((y - 5) * 0.9) + q;
            for (int x = x0; x < x0 + 14; x++) t.tint(x, y, rgb(255, 236, 160), forca);
        }
        // escurece o quarto inteiro
        int noite = rgb(18, 26, 70);
        for (int y = 0; y < 48; y++) for (int x = 0; x < L; x++) t.px(x, y, mix(t.get(x, y), noite, 0.45 * k));
        return t;
    }

    // ------------------------------------------------------------------
    //  Cenas 555–559 — RU: o frango esfria, Bia senta do lado sem perguntar.
    // ------------------------------------------------------------------
    static Tela ru6(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 28, rgb(204, 216, 212), rgb(184, 198, 196));
        for (int x = 0; x < L; x += 6) t.rect(x, 0, 1, 28, rgb(168, 184, 182));
        for (int y = 4; y < 28; y += 6) t.rect(0, y, L, 1, rgb(168, 184, 182));
        frente(t, 16, 8, PELE_P, CAB_P, ROXO, 18, true, true);
        frente(t, 50, 8, rgb(190, 130, 96), rgb(60, 36, 26), rgb(214, 196, 160), 18, true, false);
        for (int y = 28; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(190, 170, 140), rgb(150, 130, 104), (y - 28) / 20.0));
        t.rect(0, 28, L, 1, rgb(226, 208, 176));
        t.rect(6, 31, 28, 12, rgb(150, 156, 168));
        t.rect(6, 31, 28, 1, rgb(206, 212, 224));
        t.elipse(20, 37, 10, 4.5, rgb(240, 240, 236));
        t.rect(14, 35, 8, 3, rgb(196, 146, 80));
        t.rect(22, 36, 5, 3, rgb(96, 150, 70));
        t.rect(40, 33, 20, 9, rgb(60, 110, 190));
        t.rect(41, 34, 18, 7, rgb(246, 246, 240));
        t.rect(42, 35, 8, 5, rgb(226, 220, 200));
        t.rect(51, 35, 7, 5, rgb(120, 70, 50));
        int vapor = Math.max(0, 4 - q / 2);   // o vapor some: o prato esfriou
        for (int i = 0; i < vapor; i++) t.tint(16 + (i * 3 + q) % 8, 31 - i * 2, rgb(255, 255, 255), 0.5 - i * 0.08);
        t.rect(33, 38, 6, 1, rgb(210, 214, 224));
        return t;
    }

    // ------------------------------------------------------------------
    //  Cena 600 — último semestre, documento em branco, cansaço de cinco anos.
    // ------------------------------------------------------------------
    static Tela tcc(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 34, rgb(24, 28, 54), rgb(46, 50, 80));
        t.rect(48, 4, 14, 14, rgb(14, 16, 32));
        for (int i = 0; i < 9; i++) t.px(49 + (i * 5) % 12, 5 + (i * 7) % 12, i % 2 == 0 ? rgb(255, 214, 120) : rgb(150, 190, 255));
        for (int i = 0; i < 6; i++) t.rect(4 + i * 5, 3 + (i % 2) * 3, 4, 4, new int[]{rgb(250, 220, 90), rgb(250, 140, 170), rgb(130, 220, 250)}[i % 3]);
        t.rect(6, 12, 38, 24, rgb(44, 48, 74));
        t.rect(7, 13, 36, 22, rgb(250, 250, 248));
        t.rect(7, 13, 36, 3, rgb(70, 100, 190));
        t.rect(10, 18, 30, 1, rgb(226, 226, 232));
        String titulo = "TÍTULO".substring(0, Math.min(6, q));
        t.texto(12, 11, titulo, rgb(30, 34, 60));
        if (q % 2 == 1) t.rect(12 + titulo.length(), 22, 1, 3, rgb(30, 34, 60));
        t.rect(3, 36, 44, 2, rgb(150, 156, 176));
        for (int y = 38; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(44, 34, 48), rgb(26, 22, 32), (y - 38) / 10.0));
        t.rect(0, 38, L, 1, rgb(88, 72, 82));
        t.rect(52, 40, 6, 6, rgb(214, 214, 228));   // café frio, sem vapor
        t.rect(58, 41, 2, 3, rgb(214, 214, 228));
        t.rect(53, 41, 4, 1, rgb(70, 44, 30));
        costas(t, 56, 26, PELE_P, CAB_P, ROXO, 14, 14);
        t.rect(49, 30, 3, 15, CAB_P);
        glow(t, 24, 24, 30, 16, rgb(150, 190, 255), 0.22);
        return t;
    }

    // ------------------------------------------------------------------
    //  Cena 605 — defesa do TCC: o projetor falha na primeira tentativa.
    // ------------------------------------------------------------------
    static Tela defesa(int q) {
        boolean[] liga = {false, false, true, false, false, false, true, true, true, true, true, true};
        boolean on = liga[q];
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 30, rgb(176, 184, 196), rgb(150, 158, 172));
        t.rect(0, 20, L, 10, rgb(120, 92, 70));
        t.rect(0, 20, L, 1, rgb(160, 126, 96));
        t.rect(18, 3, 30, 17, rgb(70, 74, 86));
        if (on) {
            t.rect(19, 4, 28, 15, rgb(248, 248, 244));
            t.rect(19, 4, 28, 3, rgb(60, 100, 190));
            for (int i = 0; i < 4; i++) t.rect(22, 9 + i * 2, 22 - i * 4, 1, rgb(110, 120, 150));
            glow(t, 33, 12, 30, 14, rgb(255, 255, 240), 0.22);
        } else {
            t.rect(19, 4, 28, 15, rgb(20, 40, 150));
            t.texto(24, 5, "SEM SINAL", rgb(230, 235, 255));
        }
        for (int i = 0; i < 3; i++) {
            int x = 14 + i * 19;
            frente(t, x, 18, new int[]{rgb(190, 130, 96), rgb(224, 172, 140), rgb(130, 84, 62)}[i],
                    new int[]{rgb(120, 120, 130), rgb(40, 30, 30), rgb(24, 20, 28)}[i], rgb(40, 44, 70), 16, false, i != 1);
        }
        t.rect(4, 34, 58, 3, rgb(150, 108, 74));
        t.rect(4, 34, 58, 1, rgb(196, 152, 110));
        for (int y = 37; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(100, 74, 56), rgb(60, 44, 38), (y - 37) / 11.0));
        t.rect(24, 36, 12, 1, rgb(30, 30, 40));   // projetor na mesa
        costas(t, 33, 36, PELE_P, CAB_P, ROXO, 16, 12);
        t.rect(25, 40, 3, 8, CAB_P);
        return t;
    }

    // ------------------------------------------------------------------
    //  Cenas 650–653 — formatura: becas, flash da mãe, gorros no ar.
    // ------------------------------------------------------------------
    static Tela formatura(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 14, rgb(120, 24, 40), rgb(170, 40, 56));
        for (int x = 0; x < L; x += 5) t.rect(x, 0, 1, 14, rgb(100, 18, 34));
        t.rect(0, 14, L, 8, rgb(150, 104, 70));
        t.rect(0, 14, L, 1, rgb(196, 150, 100));
        t.rect(28, 8, 10, 14, rgb(90, 60, 40));          // púlpito
        pessoa(t, 33, 20, rgb(200, 150, 120), rgb(120, 120, 130), rgb(30, 30, 44), rgb(30, 30, 44), 0);
        t.rect(6, 3, 8, 3, rgb(200, 200, 210));          // ar-condicionado quebrado
        t.px(10, 7 + (q % 3), rgb(160, 200, 240));
        for (int y = 22; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(60, 44, 56), rgb(30, 22, 34), (y - 22) / 26.0));
        for (int fila = 0; fila < 3; fila++) {
            for (int i = 0; i < 8; i++) {
                int x = 6 + i * 8 + (fila % 2) * 4;
                int y = 26 + fila * 7;
                int k = fila * 8 + i;
                int pele = new int[]{rgb(224, 172, 140), rgb(130, 84, 62), rgb(190, 130, 96), rgb(96, 62, 46)}[k % 4];
                boolean lancado = q >= 8 && (k % 3 == 0);
                if (!lancado) t.rect(x - 3, y - 1, 7, 1, rgb(20, 20, 28));
                costas(t, x, y, pele, rgb(30, 24, 30), rgb(28, 28, 40), 8, 6 + (2 - fila) * 3);
            }
        }
        // gorros no ar (último quadro = apogeu)
        for (int k = 0; k < 24; k += 3) {
            if (q >= 8) {
                int fila = k / 8, i = k % 8;
                int x = 6 + i * 8 + (fila % 2) * 4 + ((k * 7) % 5 - 2) * (q - 7) / 2;
                int y = 26 + fila * 7 - (q - 7) * 3;
                t.rect(x - 3, y, 7, 1, rgb(20, 20, 28));
                t.rect(x - 1, y + 1, 3, 1, rgb(20, 20, 28));
                t.px(x + 3, y + 1, rgb(240, 200, 60));
            }
        }
        if (q == 2 || q == 5 || q == 8 || q == 11) {   // flash do celular da mãe, na 3ª fila
            glow(t, 12, 41, 10, 7, rgb(255, 255, 255), 0.85);
            t.rect(11, 40, 2, 2, rgb(255, 255, 255));
        }
        return t;
    }

    // ------------------------------------------------------------------
    //  Cena 655 — o portão ao anoitecer: "Daqui pra frente é outra coisa."
    // ------------------------------------------------------------------
    static Tela portao(int q) {
        Tela t = new Tela(L, A);
        for (int y = 0; y < 38; y++) t.rect(0, y, L, 1, gradiente(y, new int[]{0, 20, 37}, new int[][]{{10, 8, 38}, {40, 22, 84}, {112, 46, 110}}));
        int[][] est = {{6, 4}, {14, 9}, {22, 3}, {40, 5}, {47, 2}, {56, 8}, {61, 3}, {30, 10}, {51, 12}};
        for (int i = 0; i < est.length; i++) if ((i + q) % 4 != 0) t.px(est[i][0], est[i][1], rgb(255, 250, 215));
        for (int x : new int[]{0, 60}) {                  // árvores ao longe
            t.elipse(x + 3, 26, 8, 9, rgb(18, 14, 40));
            t.rect(x + 2, 30, 2, 8, rgb(18, 14, 40));
        }
        t.rect(5, 15, 56, 4, rgb(130, 126, 150));          // viga
        t.rect(5, 15, 56, 1, rgb(176, 170, 196));
        t.rect(5, 18, 56, 1, rgb(86, 80, 110));
        t.rect(8, 19, 7, 19, rgb(205, 95, 35));
        t.rect(13, 19, 2, 19, rgb(160, 65, 25));
        t.rect(51, 19, 7, 19, rgb(205, 95, 35));
        t.rect(56, 19, 2, 19, rgb(160, 65, 25));
        t.rect(52, 22, 5, 6, rgb(244, 242, 248));
        t.texto(52, 11, "UEFS", rgb(30, 52, 122));
        for (int y = 38; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(52, 40, 70), rgb(24, 20, 36), (y - 38) / 10.0));
        int[] lx = {22, 33, 44};
        for (int i = 0; i < 3; i++) {
            t.rect(lx[i], 22, 1, 16, rgb(30, 26, 44));
            t.rect(lx[i] - 1, 21, 3, 2, rgb(50, 44, 70));
            if (q >= 2 + i * 3) {
                t.rect(lx[i] - 1, 22, 3, 1, rgb(255, 236, 170));
                glow(t, lx[i], 25, 9, 8, rgb(255, 210, 120), 0.55);
                glow(t, lx[i], 40, 9, 4, rgb(255, 200, 110), 0.40);
            }
        }
        costas(t, 33, 35, PELE_P, CAB_P, ROXO, 12, 14);
        t.rect(28, 39, 3, 9, CAB_P);
        return t;
    }
}