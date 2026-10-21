package br.uefs.forkeazando.view.pixel;

import static br.uefs.forkeazando.view.pixel.Cor.gradiente;
import static br.uefs.forkeazando.view.pixel.Cor.mix;
import static br.uefs.forkeazando.view.pixel.Cor.rgb;
import static br.uefs.forkeazando.view.pixel.Elementos.*;

final class CenasCap1 {

    static final int N_RU = 12, N_OFICINA = 12, N_PBL = 12, N_GRUPO = 12;
    static final int N_VAZIA = 10, N_LED = 12, N_MODULO8 = 12, N_ONIBUS = 14;
    private static final int L = Ilustracoes.LARGURA, A = Ilustracoes.LINHAS;

    private CenasCap1() {}

    //  Cena 2 / 107 / 455 — RU: a fila avança. Paleta: azulejo verde + bege.
    static Tela ru(int q) {
        int f = N_RU - 1 - q;
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 14, rgb(236, 222, 178), rgb(224, 206, 158));
        t.rect(0, 14, L, 8, rgb(120, 176, 150));
        for (int x = 0; x < L; x += 4) t.rect(x, 14, 1, 8, rgb(98, 150, 126));
        for (int y = 16; y < 22; y += 2) t.rect(0, y, L, 1, rgb(98, 150, 126));
        for (int x : new int[]{6, 26}) {
            t.rect(x, 3, 12, 8, rgb(120, 140, 170));
            t.rect(x + 1, 4, 10, 6, rgb(210, 232, 250));
            t.rect(x + 6, 3, 1, 8, rgb(120, 140, 170));
        }
        t.rect(44, 3, 18, 9, rgb(40, 52, 46));
        t.rect(44, 3, 18, 1, rgb(150, 110, 70));
        t.texto(45, 2, "FRANGO C/", rgb(250, 246, 220));
        t.texto(45, 3, "QUIABO  R$2", rgb(255, 214, 102));
        for (int y = 22; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(214, 196, 160), rgb(168, 148, 118), (y - 22) / 26.0));
        for (int x = 0; x < L; x += 9) t.rect(x, 22, 1, 26, rgb(190, 170, 136));
        // balcão de inox com cubas e vapor
        t.rect(30, 22, 36, 9, rgb(186, 194, 204));
        t.rect(30, 22, 36, 1, rgb(236, 240, 246));
        t.rect(30, 30, 36, 1, rgb(110, 118, 130));
        int[] comida = {rgb(228, 196, 90), rgb(150, 98, 52), rgb(92, 150, 70), rgb(236, 230, 210)};
        for (int i = 0; i < 4; i++) {
            t.rect(33 + i * 8, 24, 7, 4, rgb(90, 98, 110));
            t.rect(34 + i * 8, 25, 5, 2, comida[i]);
            for (int k = 0; k < 3; k++) t.tint(34 + i * 8 + (k + q + i) % 3, 21 - ((q + i * 2 + k * 2) % 6), rgb(255, 255, 255), 0.40 - k * 0.1);
        }
        // fila
        int[] roupa = {rgb(200, 60, 70), rgb(60, 110, 190), rgb(92, 62, 112), rgb(230, 190, 60), rgb(90, 160, 100), rgb(220, 120, 60)};
        int[] pele = {rgb(224, 172, 140), rgb(130, 84, 62), rgb(120, 76, 56), rgb(190, 130, 96), rgb(236, 190, 160), rgb(160, 108, 80)};
        int[] cab = {rgb(60, 40, 30), rgb(24, 20, 28), rgb(26, 20, 40), rgb(120, 80, 40), rgb(40, 30, 30), rgb(24, 20, 28)};
        for (int i = 5; i >= 0; i--) {
            int x = 52 - i * 8 - f;
            pessoa(t, x, 44, pele[i], cab[i], roupa[i], rgb(40, 44, 70), i == 4 && q % 4 < 2 ? 1 : 0);
            if (i == 2) t.rect(x - 4, 36, 1, 7, cab[i]);   // cabelo liso comprido da protagonista
        }
        return t;
    }

    //  Cena 5 / 304 / 562 — oficina de eletrônica: LEDs piscando, fumaça.
    static Tela oficina(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 30, rgb(70, 86, 108), rgb(96, 112, 134));
        t.rect(10, 0, 46, 2, rgb(236, 244, 255));
        glow(t, 33, 2, 30, 14, rgb(220, 235, 255), 0.18);
        t.rect(34, 5, 28, 13, rgb(150, 156, 170));
        t.rect(35, 6, 26, 11, rgb(244, 246, 250));
        t.texto(37, 3, "LED piscando.", rgb(30, 50, 120));
        t.texto(37, 4, "Se sair fumaça,", rgb(30, 50, 120));
        t.texto(37, 5, "desliga a fonte.", rgb(200, 40, 50));
        // osciloscópio
        t.rect(4, 12, 15, 15, rgb(60, 64, 76));
        t.rect(6, 14, 11, 9, rgb(8, 24, 16));
        for (int x = 0; x < 11; x++) t.px(6 + x, 18 + (int) Math.round(2 * Math.sin((x + q) * 0.9)), rgb(90, 255, 140));
        t.rect(7, 25, 2, 1, rgb(200, 200, 210));
        t.rect(11, 25, 2, 1, rgb(200, 200, 210));
        // bancada
        t.rect(0, 30, L, 6, rgb(156, 114, 78));
        t.rect(0, 30, L, 1, rgb(196, 152, 108));
        for (int y = 36; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(96, 68, 50), rgb(60, 42, 34), (y - 36) / 12.0));
        // protoboard + LEDs
        t.rect(22, 26, 26, 5, rgb(232, 232, 226));
        t.rect(22, 26, 26, 1, rgb(214, 60, 60));
        t.rect(22, 30, 26, 1, rgb(60, 90, 200));
        for (int x = 24; x < 47; x += 2) t.px(x, 28, rgb(80, 80, 90));
        t.rect(30, 28, 1, 3, rgb(240, 200, 40));
        t.rect(36, 26, 1, 3, rgb(40, 150, 220));
        for (int i = 0; i < 4; i++) {
            boolean on = (q + i) % 4 < 2;
            int x = 26 + i * 6;
            t.rect(x, 24, 2, 2, on ? rgb(255, 70, 70) : rgb(110, 24, 28));
            if (on) glow(t, x + 1, 25, 4, 3, rgb(255, 80, 80), 0.45);
        }
        for (int k = 0; k < 6; k++) {   // fumaça sobe do 3º LED
            t.tint(statSway(38, q, k), 22 - k * 2, rgb(200, 200, 210), Math.max(0.12, 0.55 - k * 0.08));
            t.tint(statSway(38, q, k) + 1, 22 - k * 2, rgb(200, 200, 210), Math.max(0.1, 0.4 - k * 0.07));
        }
        // fonte de bancada
        t.rect(52, 22, 11, 8, rgb(70, 76, 90));
        t.rect(54, 24, 7, 3, rgb(10, 20, 14));
        t.rect(55, 25, 5, 1, rgb(255, 80, 70));
        return t;
    }

    private static int statSway(int x, int q, int k) {
        return x + ((q + k * 3) % 3) - 1;
    }

    //  Cena 6 / 16 — PBL, sala 203: o tutor cola o Problema 2 no quadro.
    static Tela pbl(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 34, rgb(206, 210, 200), rgb(186, 192, 184));
        gradV(t, 0, 34, L, 14, rgb(124, 106, 96), rgb(78, 66, 64));       // piso
        t.rect(0, 0, L, 1, rgb(244, 246, 240));
        t.rect(18, 3, 36, 17, rgb(130, 136, 150));
        t.rect(19, 4, 34, 15, rgb(246, 248, 250));
        t.texto(21, 3, "PROBLEMA 2", rgb(40, 60, 130));
        int yFolha = q < 6 ? 2 + q * 2 : 12;     // a folha desce até o quadro
        yFolha = Math.min(yFolha, 11);
        t.rect(33, yFolha, 12, 7, rgb(250, 250, 244));
        t.rect(33, yFolha, 12, 1, rgb(190, 190, 200));
        for (int i = 1; i < 6; i += 2) t.rect(34, yFolha + i, 9 - i % 3, 1, rgb(110, 120, 150));
        t.rect(36, yFolha - 1, 6, 1, rgb(200, 200, 205));
        pessoa(t, 12, 34, rgb(200, 150, 120), rgb(60, 40, 30), rgb(50, 50, 78), rgb(40, 40, 60), q < 6 ? 1 : 0);
        int[] roupa = {rgb(190, 70, 80), rgb(70, 130, 190), rgb(92, 62, 112), rgb(220, 180, 70), rgb(80, 150, 110)};
        int[] pele = {rgb(224, 172, 140), rgb(190, 130, 96), rgb(120, 76, 56), rgb(236, 190, 160), rgb(160, 108, 80)};
        int[] cab = {rgb(60, 40, 30), rgb(24, 20, 28), rgb(26, 20, 40), rgb(120, 80, 40), rgb(24, 20, 28)};
        int[] xs = {22, 33, 44, 54, 28};
        for (int i = 0; i < 4; i++) costas(t, xs[i], 27 + (i % 2), pele[i], cab[i], roupa[i], 9, 8);
        t.elipse(33, 41, 25, 6.5, rgb(112, 80, 56));
        t.elipse(33, 40, 25, 6.0, rgb(166, 120, 82));
        t.elipse(33, 39, 22, 4.5, rgb(178, 134, 94));
        t.rect(28, 36, 10, 3, rgb(60, 64, 90));
        t.rect(28, 36, 10, 1, rgb(160, 170, 200));
        t.rect(44, 38, 6, 2, rgb(246, 246, 240));
        costas(t, 14, 38, pele[2], cab[2], roupa[2], 12, 12);
        costas(t, 52, 38, pele[4], cab[4], roupa[4], 12, 12);
        return t;
    }

    //  Cena 11/12 (Cap.1), 303 (Cap.4), 400-415 (Cap.5) — sala do grupo.
    static Tela salaGrupo(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 33, rgb(238, 224, 194), rgb(218, 198, 160));
        glow(t, 33, 0, 36, 18, rgb(255, 236, 190), 0.30);
        int[] xs = {3, 24, 45};
        for (int b = 0; b < 3; b++) {
            t.rect(xs[b], 4, 19, 16, rgb(150, 150, 162));
            t.rect(xs[b] + 1, 5, 17, 14, rgb(246, 246, 242));
            int linhas = b < 2 ? 6 : Math.min(6, 1 + q / 2);
            for (int i = 0; i < linhas; i++) {
                int len = 6 + (i * 5 + b * 3) % 9;
                int cor = b < 2 ? rgb(176, 176, 190) : (i % 3 == 2 ? rgb(200, 50, 60) : rgb(40, 60, 140));
                t.rect(xs[b] + 3, 7 + i * 2, len, 1, cor);
            }
        }
        t.texto(47, 4, "ACO", rgb(40, 60, 140));
        pessoa(t, 58, 34, rgb(200, 150, 120), rgb(40, 30, 30), rgb(60, 90, 120), rgb(40, 44, 70), q < N_GRUPO - 1 ? 1 : 0);
        t.rect(0, 33, L, 4, rgb(120, 84, 58));
        for (int y = 37; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(150, 108, 74), rgb(96, 68, 50), (y - 37) / 11.0));
        for (int x : new int[]{10, 30, 48}) {
            t.rect(x, 30, 3, 3, rgb(240, 236, 226));
            t.rect(x + 3, 31, 1, 1, rgb(240, 236, 226));
            t.tint(x + (q % 2), 28 - (q % 3), rgb(255, 255, 255), 0.4);
        }
        costas(t, 14, 36, rgb(190, 130, 96), rgb(24, 20, 28), rgb(70, 110, 160), 12, 12);
        costas(t, 36, 38, rgb(120, 76, 56), rgb(26, 20, 40), rgb(92, 62, 112), 12, 12);
        costas(t, 54, 37, rgb(224, 172, 140), rgb(120, 80, 40), rgb(170, 80, 70), 12, 12);
        return t;
    }

    //  Cena 8 / 9 — oficina vazia à tarde; o LED acende; Cáio na porta.
    static Tela oficinaVazia(int q) {
        return oficinaTarde(q, 0.0, false);
    }

    static Tela oficinaLed(int q) {
        double g = q < 3 ? 0 : q == 3 ? 1.0 : q == 4 ? 0.15 : Math.min(1.0, 0.6 + (q - 5) * 0.08);
        return oficinaTarde(q, g, q >= 7);
    }

    private static Tela oficinaTarde(int q, double led, boolean caio) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 32, rgb(22, 40, 52), rgb(40, 62, 76));
        // luz laranja do fim da tarde entrando pela janela
        t.rect(48, 4, 15, 15, rgb(60, 52, 70));
        for (int y = 5; y < 18; y++) t.rect(49, y, 13, 1, gradiente(y - 5, new int[]{0, 12}, new int[][]{{120, 70, 110}, {250, 150, 80}}));
        t.rect(55, 4, 1, 15, rgb(60, 52, 70));
        glow(t, 56, 12, 20, 16, rgb(255, 160, 80), 0.16);
        // osciloscópio zumbindo
        t.rect(16, 9, 26, 19, rgb(58, 62, 74));
        t.rect(16, 9, 26, 1, rgb(92, 98, 112));
        t.rect(19, 12, 20, 11, rgb(6, 22, 14));
        for (int x = 0; x < 20; x++) t.px(19 + x, 17 + (int) Math.round(3 * Math.sin((x + q * 2) * 0.55)), rgb(90, 255, 140));
        t.rect(19, 17, 20, 1, rgb(18, 70, 40));
        t.rect(21, 25, 3, 2, rgb(190, 190, 200));
        t.rect(28, 25, 3, 2, rgb(190, 190, 200));
        // bancada
        t.rect(0, 31, L, 5, rgb(120, 88, 62));
        t.rect(0, 31, L, 1, rgb(166, 124, 88));
        for (int y = 36; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(70, 50, 42), rgb(36, 28, 30), (y - 36) / 12.0));
        // circuito montado torto + fio amarelo saindo pela beirada
        for (int i = 0; i < 4; i++) t.rect(36 + i, 28 + i, 18, 1, rgb(226, 226, 220));
        t.rect(40, 27, 14, 1, rgb(214, 60, 60));
        for (int x = 38; x < 54; x += 2) t.px(x, 30, rgb(80, 80, 90));
        t.rect(54, 31, 8, 1, rgb(240, 200, 40));
        t.rect(61, 32, 1, 5, rgb(240, 200, 40));
        int ledCor = mix(rgb(90, 20, 24), rgb(255, 70, 70), led);
        t.rect(47, 25, 2, 2, ledCor);
        if (led > 0) glow(t, 48, 26, 9, 7, rgb(255, 70, 70), 0.6 * led);
        // protagonista de costas, de roxo, cabelo liso comprido
        costas(t, 11, 20, rgb(120, 76, 56), rgb(26, 20, 40), rgb(92, 62, 112), 16, 24);
        t.rect(3, 24, 3, 18, rgb(26, 20, 40));
        t.rect(16, 24, 2, 14, rgb(26, 20, 40));
        t.rect(5, 29, 12, 3, mix(rgb(92, 62, 112), rgb(250, 150, 80), 0.25));
        if (led > 0.5) t.rect(15, 28, 2, 8, mix(rgb(92, 62, 112), rgb(255, 80, 80), 0.3));
        // Cáio aparece na porta (contraluz do corredor)
        if (caio) {
            t.rect(56, 14, 9, 25, rgb(250, 214, 150));
            pessoa(t, 60, 39, rgb(176, 120, 92), rgb(24, 20, 28), rgb(76, 84, 94), rgb(40, 44, 70), 0);
            t.rect(63, 29, 2, 3, rgb(30, 120, 134));
            glow(t, 60, 30, 8, 14, rgb(250, 214, 150), 0.25);
        }
        return t;
    }

    //  Cena 13 / 15 — Módulo 8: dominó, cerveja gelada, ninguém fala de prova.
    static Tela modulo8(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 34, rgb(40, 24, 52), rgb(78, 40, 60));
        int[] bulbos = {rgb(255, 214, 102), rgb(255, 130, 90), rgb(120, 220, 255), rgb(190, 255, 150)};
        for (int i = 0; i < 11; i++) {
            int x = 2 + i * 6, y = 3 + (int) Math.round(2.5 * Math.sin(i * 0.55));
            t.px(x, y - 1, rgb(60, 50, 60));
            if ((i + q) % 3 != 0) { t.rect(x, y, 2, 2, bulbos[i % 4]); glow(t, x, y, 5, 4, bulbos[i % 4], 0.25); }
            else t.rect(x, y, 2, 2, mix(bulbos[i % 4], rgb(40, 24, 52), 0.7));
        }
        int[] roupa = {rgb(200, 70, 80), rgb(80, 150, 200), rgb(230, 190, 60)};
        int[] pele = {rgb(190, 130, 96), rgb(224, 172, 140), rgb(120, 76, 56)};
        int[] cab = {rgb(24, 20, 28), rgb(120, 80, 40), rgb(26, 20, 40)};
        int[] xs = {12, 32, 54};
        for (int i = 0; i < 3; i++) pessoa(t, xs[i], 36, pele[i], cab[i], roupa[i], rgb(40, 44, 70), (q / 2 + i) % 3 == 0 ? 1 : 0);
        for (int y = 30; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(156, 108, 66), rgb(98, 66, 44), (y - 30) / 18.0));
        t.rect(0, 30, L, 1, rgb(206, 156, 100));
        int[][] pecas = {{20, 33}, {26, 33}, {32, 34}, {38, 36}, {44, 38}, {38, 41}, {32, 43}, {26, 44}};
        for (int i = 0; i < pecas.length; i++) {
            int slam = (q == 5 && i == pecas.length - 1) ? -3 : 0;   // última peça bate na mesa
            int x = pecas[i][0], y = pecas[i][1] + slam;
            t.rect(x, y, 5, 3, rgb(246, 244, 232));
            t.px(x + 1, y + 1, rgb(30, 30, 40));
            t.px(x + 3, y + 1, rgb(30, 30, 40));
            t.rect(x + 2, y, 1, 3, rgb(150, 140, 120));
        }
        for (int x : new int[]{6, 54}) {
            t.rect(x, 33, 5, 9, rgb(240, 196, 80));
            t.rect(x, 33, 1, 9, rgb(255, 235, 150));
            t.rect(x, 31, 5, 2, rgb(252, 250, 244));
            t.rect(x, 41, 5, 1, rgb(180, 130, 40));
        }
        return t;
    }


    //  Cena 14 — ônibus: o vidro embaçado, "UEFP" escrito com o dedo, depois apagado.

    static Tela onibus(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 36, rgb(20, 24, 56), rgb(44, 34, 72));
        int[][] bokeh = {{10, 8, 3}, {22, 22, 2}, {30, 12, 4}, {44, 26, 3}, {52, 10, 3}, {58, 24, 2}, {16, 28, 2}, {38, 6, 2}, {48, 18, 4}};
        int[] cores = {rgb(255, 180, 90), rgb(250, 250, 230), rgb(120, 170, 255), rgb(255, 110, 90)};
        for (int i = 0; i < bokeh.length; i++) {
            t.elipse(bokeh[i][0], bokeh[i][1], bokeh[i][2], bokeh[i][2], mix(rgb(30, 30, 60), cores[i % 4], 0.85));
        }
        boolean[][] limpo = new boolean[48][L];
        char[] letras = {'U', 'E', 'F', 'P'};
        int[] x0 = {14, 27, 40};
        for (int i = 0; i < 3; i++) {
            int colunas = Math.max(0, Math.min(5, (q - (1 + i * 3)) * 2));
            String[] g = letra(letras[i]);
            for (int j = 0; j < 7; j++) for (int c = 0; c < colunas; c++) {
                if (g[j].charAt(c) == 'X') for (int dy = 0; dy < 2; dy++) for (int dx = 0; dx < 2; dx++)
                    limpo[10 + j * 2 + dy][x0[i] + c * 2 + dx] = true;
            }
        }
        if (q >= 10) {   // a palma apaga uma faixa
            int ate = Math.min(60, 3 + (q - 9) * 15);
            for (int y = 8; y < 28; y++) for (int x = 3; x < ate; x++) limpo[y][x] = true;
        }
        for (int y = 3; y < 35; y++) for (int x = 3; x < 63; x++) {
            if (!limpo[y][x]) t.px(x, y, mix(t.get(x, y), rgb(170, 180, 206), 0.62));
        }
        for (int[] g : new int[][]{{8, 6}, {20, 10}, {34, 16}, {47, 8}, {56, 20}}) {   // gotas
            t.rect(g[0], g[1], 1, 3 + (q % 2), rgb(220, 230, 250));
        }
        t.rect(0, 0, L, 3, rgb(14, 16, 32));
        t.rect(0, 0, 3, 36, rgb(14, 16, 32));
        t.rect(63, 0, 3, 36, rgb(14, 16, 32));
        t.rect(0, 35, L, 13, rgb(18, 18, 36));
        t.rect(0, 35, L, 1, rgb(60, 60, 90));
        t.rect(0, 37, L, 1, rgb(230, 190, 60));
        costas(t, 12, 30, rgb(120, 76, 56), rgb(26, 20, 40), rgb(92, 62, 112), 14, 14);
        t.rect(5, 34, 3, 14, rgb(26, 20, 40));
        return t;
    }
}