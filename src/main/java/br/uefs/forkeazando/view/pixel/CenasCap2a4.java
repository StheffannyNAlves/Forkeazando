package br.uefs.forkeazando.view.pixel;

import static br.uefs.forkeazando.view.pixel.Cor.gradiente;
import static br.uefs.forkeazando.view.pixel.Cor.mix;
import static br.uefs.forkeazando.view.pixel.Cor.rgb;
import static br.uefs.forkeazando.view.pixel.Elementos.*;

/** Capítulos 2 a 4: mural, assembleia, feira, prova, notas, editais, ÁGATA e o prazo duplo. */
final class CenasCap2a4 {

    static final int N_MURAL = 10, N_ASSEMBLEIA = 12, N_FEIRA = 12, N_PROVA = 14;
    static final int N_NOTAS = 10, N_EDITAIS = 10, N_AGATA = 14, N_PRAZO = 12;
    private static final int L = Ilustracoes.LARGURA, A = Ilustracoes.LINHAS;

    private CenasCap2a4() {}

    private static final int[] ROUPAS = {
            rgb(200, 60, 70), rgb(60, 110, 190), rgb(230, 190, 60), rgb(90, 160, 100),
            rgb(150, 90, 170), rgb(220, 120, 60), rgb(70, 70, 90), rgb(190, 190, 200)};
    private static final int[] PELES = {
            rgb(224, 172, 140), rgb(130, 84, 62), rgb(190, 130, 96), rgb(96, 62, 46),
            rgb(236, 190, 160), rgb(160, 108, 80)};
    private static final int[] CABELOS = {
            rgb(60, 40, 30), rgb(24, 20, 28), rgb(26, 20, 40), rgb(120, 80, 40), rgb(40, 30, 30)};


    //  Cena 101 — mural do módulo 1: cartazes presos com fita crepe.
    static Tela mural(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 36, rgb(214, 208, 196), rgb(190, 184, 172));
        t.rect(5, 3, 56, 33, rgb(104, 72, 46));
        t.rect(6, 4, 54, 31, rgb(194, 150, 102));
        for (int i = 0; i < 40; i++) t.px(7 + (i * 13) % 52, 5 + (i * 7) % 29, rgb(168, 126, 84));
        int[][] cart = {{9, 8, 14, 18}, {27, 6, 14, 20}, {45, 9, 12, 16}};
        int[] topo = {rgb(60, 100, 200), rgb(200, 50, 60), rgb(240, 190, 40)};
        String[] nome = {"CA COMP", "DCE", "?"};
        for (int i = 0; i < 3; i++) {
            int x = cart[i][0], y = cart[i][1], w = cart[i][2], h = cart[i][3];
            t.rect(x + 1, y + 1, w, h, rgb(120, 90, 62));
            t.rect(x, y, w, h, rgb(250, 246, 232));
            t.rect(x, y + 1, w, 4, topo[i]);
            t.texto(x + 2, (y + 1) / 2 + (((y + 1) % 2 == 0) ? 0 : 1), nome[i], rgb(255, 255, 255));
            for (int k = 0; k < 4; k++) t.rect(x + 2, y + 7 + k * 3, w - 4 - (k % 2) * 3, 1, rgb(170, 170, 180));
            t.rect(x + w / 2 - 1, y - 1, 3, 2, rgb(236, 220, 176));
            t.rect(x - 1, y + h - 1, 3, 2, rgb(236, 220, 176));
        }
        int dx = (N_MURAL - 1 - q) / 3 + q % 2;   // a ponta solta do terceiro cartaz balança e vai assentando
        t.rect(45 + 10 + dx, 9 + 14, 3, 3, rgb(226, 222, 208));
        t.rect(45 + 11 + dx, 9 + 17, 2, 1, rgb(120, 90, 62));
        for (int y = 36; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(150, 150, 156), rgb(110, 110, 120), (y - 36) / 12.0));
        costas(t, 14, 33, rgb(120, 76, 56), rgb(26, 20, 40), rgb(92, 62, 112), 14, 14);
        t.rect(6, 38, 3, 10, rgb(26, 20, 40));
        costas(t, 52, 35, rgb(190, 130, 96), rgb(24, 20, 28), rgb(70, 110, 160), 12, 12);
        return t;
    }

    //  Cenas 105 / 106 — assembleia no pátio: faixas, megafone, quarenta pessoas.
    static Tela assembleia(int q) {
        Tela t = new Tela(L, A);
        for (int y = 0; y < 22; y++) t.rect(0, y, L, 1, gradiente(y, new int[]{0, 21}, new int[][]{{150, 170, 198}, {214, 220, 228}}));
        for (int[] n : new int[][]{{8, 4, 12}, {38, 3, 14}}) {
            for (int dx = 0; dx < n[2]; dx++) t.rect(n[0] + dx, n[1], 1, (dx > 1 && dx < n[2] - 2) ? 3 : 2, rgb(236, 240, 246));
        }
        t.rect(0, 20, L, 8, rgb(196, 190, 178));
        for (int x = 2; x < L; x += 9) t.rect(x, 22, 4, 6, rgb(176, 170, 156));
        for (int x : new int[]{2, 60}) {
            t.rect(x + 2, 12, 2, 16, rgb(84, 56, 34));
            t.elipse(x + 3, 10, 6, 6, rgb(38, 110, 56));
            t.elipse(x + 1, 8, 4, 4, rgb(60, 140, 70));
        }
        for (int y = 28; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(150, 146, 138), rgb(110, 106, 100), (y - 28) / 20.0));
        // faixas ondulando contra a terceirização do RU
        int[][] faixas = {{8, 11, 24}, {36, 11, 24}};
        String[] txt = {"RU R$ 2", "NÃO À R$ 4"};
        for (int b = 0; b < 2; b++) {
            for (int x = 0; x < faixas[b][2]; x++) {
                int off = (int) Math.round(Math.sin((x + q * 2) * 0.45));
                t.rect(faixas[b][0] + x, faixas[b][1] + off, 1, 9, b == 0 ? rgb(196, 40, 52) : rgb(40, 70, 170));
            }
            t.texto(faixas[b][0] + 2, 6, txt[b], rgb(255, 255, 255));
            t.rect(faixas[b][0], 20, 1, 9, rgb(84, 56, 34));
            t.rect(faixas[b][0] + faixas[b][2] - 1, 20, 1, 9, rgb(84, 56, 34));
        }
        // multidão em três fileiras
        int[] pes = {31, 38, 45};
        int n = 0;
        for (int fila = 0; fila < 3; fila++) {
            for (int i = 0; i < 9; i++) {
                int x = 4 + i * 7 + (fila % 2) * 3;
                int k = n++;
                pessoa(t, x, pes[fila], PELES[k % PELES.length], CABELOS[k % CABELOS.length], ROUPAS[(k * 3) % ROUPAS.length],
                        rgb(40, 44, 70), ((k + q / 2) % 6 == 0) ? 1 : 0);
            }
        }
        // megafone: ondas de som
        t.rect(31, 27, 4, 3, rgb(240, 200, 40));
        int ondas = 1 + q % 3;
        for (int r = 1; r <= ondas; r++) {
            for (int a = -2; a <= 2; a++) t.tint(36 + r * 3 - Math.abs(a) / 2, 28 + a * 2, rgb(255, 255, 255), 0.7 - r * 0.15);
        }
        return t;
    }

    //  Cenas 116–123 — Feira de Graduação: estandes, bandeirolas, balões.
    static Tela feira(int q) {
        int f = N_FEIRA - 1 - q;
        Tela t = new Tela(L, A);
        for (int y = 0; y < 30; y++) t.rect(0, y, L, 1, gradiente(y, new int[]{0, 29}, new int[][]{{60, 150, 240}, {170, 220, 250}}));
        t.elipse(58, 7, 4, 4, rgb(255, 238, 150));
        for (int y = 30; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(206, 190, 160), rgb(160, 144, 120), (y - 30) / 18.0));
        int[] corTenda = {rgb(240, 110, 170), rgb(70, 130, 220), rgb(80, 180, 110)};
        String[] nome = {"WIE", "RAS", "TISP"};
        for (int s = 0; s < 3; s++) {
            int x = 2 + s * 22;
            for (int i = 0; i < 18; i++) t.rect(x + i, 14, 1, 7, (i / 3) % 2 == 0 ? corTenda[s] : rgb(250, 250, 250));
            t.rect(x - 1, 21, 20, 1, mix(corTenda[s], rgb(0, 0, 0), 0.3));
            t.rect(x + 1, 21, 1, 10, rgb(110, 110, 120));
            t.rect(x + 16, 21, 1, 10, rgb(110, 110, 120));
            t.rect(x + 1, 28, 16, 4, rgb(246, 244, 236));
            t.texto(x + 7 - (nome[s].length() > 3 ? 1 : 0), 14, nome[s], rgb(40, 40, 70));
        }
        // bandeirolas ao vento
        for (int i = 0; i < 16; i++) {
            int x = 2 + i * 4, y = 3 + (int) Math.round(1.5 * Math.sin((i + q) * 0.8));
            t.rect(x, y, 3, 3, new int[]{rgb(255, 90, 90), rgb(255, 210, 70), rgb(90, 200, 255), rgb(120, 230, 130)}[i % 4]);
            t.px(x + 1, y - 1, rgb(80, 70, 70));
        }
        // balões subindo
        int[][] bal = {{12, 20}, {34, 17}, {56, 22}};
        int[] cb = {rgb(255, 90, 140), rgb(255, 220, 70), rgb(100, 190, 255)};
        for (int i = 0; i < 3; i++) {
            int y = bal[i][1] - (q * 1) - i;
            t.elipse(bal[i][0], y, 2.5, 3, cb[i]);
            t.px(bal[i][0] - 1, y - 1, rgb(255, 255, 255));
            t.rect(bal[i][0], y + 3, 1, 6, rgb(240, 240, 240));
        }
        // crianças de camiseta azul de colégio público caminhando
        for (int i = 0; i < 5; i++) {
            int x = (6 + i * 13 - f * 2 + L) % L;
            pessoa(t, x, 46, PELES[i % PELES.length], CABELOS[i % CABELOS.length], rgb(40, 90, 200), rgb(60, 60, 80), 0);
        }
        return t;
    }

    //  Cenas 200–207 — prova de Cálculo I: o relógio não anda, a sala sim.

    static Tela prova(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 38, rgb(150, 164, 150), rgb(126, 142, 130));
        t.rect(6, 0, 54, 2, rgb(240, 250, 244));
        glow(t, 33, 2, 34, 12, rgb(230, 250, 235), 0.2);
        t.rect(4, 5, 22, 10, rgb(34, 54, 44));
        t.rect(4, 5, 22, 1, rgb(150, 110, 70));
        t.texto(6, 3, "CÁLCULO I", rgb(240, 244, 230));
        t.texto(6, 5, "∫ x² dx = ?", rgb(255, 214, 102));
        // relógio redondo: ponteiro dos segundos gira
        t.elipse(44, 10, 7, 7, rgb(60, 60, 64));
        t.elipse(44, 10, 6, 6, rgb(246, 246, 240));
        for (int[] m : new int[][]{{44, 5}, {44, 15}, {39, 10}, {49, 10}}) t.px(m[0], m[1], rgb(30, 30, 36));
        t.rect(44, 7, 1, 3, rgb(30, 30, 36));
        t.rect(44, 10, 2, 1, rgb(30, 30, 36));
        double ang = Math.toRadians(q * 30 - 90);
        for (int r = 1; r <= 5; r++) t.px(44 + (int) Math.round(r * Math.cos(ang)), 10 + (int) Math.round(r * Math.sin(ang)), rgb(214, 40, 50));
        // alunos de costas, cabeça baixa
        int n = 0;
        for (int fila = 0; fila < 3; fila++) {
            for (int i = 0; i < 5; i++) {
                int x = 8 + i * 13 + (fila % 2) * 4;
                int y = 17 + fila * 8;
                int k = n++;
                costas(t, x, y + ((k + q) % 7 == 0 ? 1 : 0), PELES[k % PELES.length], CABELOS[k % CABELOS.length], ROUPAS[(k * 2) % ROUPAS.length], 8, 4);
                t.rect(x - 4, y + 11, 9, 2, rgb(176, 150, 112));   // carteira
            }
        }
        for (int y = 38; y < 48; y++) t.rect(0, y, L, 1, rgb(160, 130, 96));
        t.rect(18, 40, 30, 8, rgb(246, 246, 240));
        t.rect(18, 40, 30, 1, rgb(200, 200, 210));
        for (int i = 0; i < 3; i++) t.rect(20, 42 + i * 2, 12 - i * 3, 1, rgb(90, 100, 160));
        t.rect(52, 41 + (q % 2), 1, 7, rgb(40, 40, 70));
        return t;
    }

    //  Cenas 210 / 211 — resultado no mural: a nota ao lado da sua.
    static Tela notas(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 38, rgb(190, 200, 214), rgb(160, 172, 190));
        t.rect(4, 2, 58, 36, rgb(110, 116, 130));
        t.rect(5, 3, 56, 34, rgb(246, 248, 250));
        t.rect(5, 3, 56, 4, rgb(60, 80, 150));
        t.texto(8, 2, "RESULTADO – CÁLCULO I", rgb(255, 255, 255));
        String[] notas = {"7,0", "4,5", "9,0", "6,5", "2,0", "8,5", "5,0", "3,5"};
        int rowSel = 4, rowDan = 5;
        boolean fim = q == N_NOTAS - 1;
        int varredura = q % 8;
        for (int i = 0; i < 8; i++) {
            int y = 9 + i * 3;
            if (fim && i == rowSel) t.rect(6, y - 1, 54, 3, rgb(255, 226, 120));
            else if (fim && i == rowDan) t.rect(6, y - 1, 54, 3, rgb(170, 230, 170));
            else if (!fim && i == varredura) t.rect(6, y - 1, 54, 3, rgb(220, 226, 250));
            t.rect(9, y, 14 + (i * 5) % 10, 1, rgb(120, 126, 150));
            t.texto(50, (y) / 2, notas[i], i == rowDan && fim ? rgb(20, 110, 40) : rgb(30, 34, 60));
        }
        for (int y = 38; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(130, 136, 150), rgb(96, 100, 112), (y - 38) / 10.0));
        costas(t, 22, 34, rgb(120, 76, 56), rgb(26, 20, 40), rgb(92, 62, 112), 14, 14);
        t.rect(15, 38, 3, 10, rgb(26, 20, 40));
        costas(t, 42, 34, rgb(110, 70, 52), rgb(24, 20, 28), rgb(200, 60, 70), 14, 14);
        t.elipse(42, 36, 6, 6, rgb(24, 20, 28));
        return t;
    }


    //  Cenas 300 / 301 dois editais colados um sobre o outro.
    static Tela editais(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 38, rgb(70, 80, 120), rgb(150, 150, 170));
        t.rect(2, 2, 62, 36, rgb(214, 208, 196));
        int[] xs = {5, 35};
        String[] titulo = {"EDITAL IC", "EDITAL DTEC"};
        for (int i = 0; i < 2; i++) {
            t.rect(xs[i] + 1, 4, 26, 32, rgb(130, 122, 110));
            t.rect(xs[i], 3, 26, 32, rgb(252, 252, 248));
            t.rect(xs[i], 3, 26, 6, i == 0 ? rgb(60, 100, 200) : rgb(40, 150, 100));
            t.texto(xs[i] + 3, 3, titulo[i], rgb(255, 255, 255));
            for (int k = 0; k < 8; k++) t.rect(xs[i] + 2, 12 + k * 2, 22 - (k % 3) * 4, 1, rgb(176, 176, 188));
            t.rect(xs[i] + 8, 2, 10, 3, rgb(226, 206, 160));
            t.rect(xs[i] + 8, 33, 10, 3, rgb(226, 206, 160));
        }
        if (q >= 4) {   // carimbo: o prazo é o mesmo para os dois
            t.rect(37, 24, 22, 7, rgb(200, 40, 50));
            t.rect(38, 25, 20, 5, rgb(252, 252, 248));
            t.texto(39, 13, "SÁB 23:59", rgb(200, 40, 50));
        }
        int peel = Math.min(5, q);   // canto do edital de cima descolando
        for (int i = 0; i < peel; i++) t.rect(29 - i, 35 - i, i + 1, 1, rgb(226, 222, 212));
        for (int y = 38; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(150, 150, 160), rgb(110, 110, 124), (y - 38) / 10.0));
        costas(t, 14, 34, rgb(120, 76, 56), rgb(26, 20, 40), rgb(92, 62, 112), 14, 14);
        t.rect(7, 38, 3, 10, rgb(26, 20, 40));
        return t;
    }

    //  Cenas 3045–3050 — o ÁGATA pede um perfil e diz "Processando...".
    static Tela agata(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 48, rgb(10, 12, 30), rgb(26, 22, 56));
        glow(t, 33, 24, 30, 24, rgb(250, 200, 60), 0.10);
        t.rect(19, 1, 28, 46, rgb(52, 54, 74));
        t.rect(20, 2, 26, 44, rgb(18, 20, 36));
        t.rect(22, 5, 22, 38, rgb(12, 16, 40));
        t.rect(22, 5, 22, 5, rgb(250, 200, 60));
        t.texto(31, 3, "ÁGATA", rgb(30, 24, 10));
        for (int i = 0; i < 4; i++) t.rect(24, 13 + i * 2, 18 - (i % 2) * 5, 1, rgb(120, 130, 170));
        t.rect(24, 23, 18, 4, rgb(30, 40, 90));
        t.rect(24, 23, 18, 1, rgb(90, 130, 230));
        t.rect(24, 29, 18, 4, rgb(30, 40, 90));
        t.rect(24, 29, 18, 1, rgb(90, 130, 230));
        t.rect(24, 36, 18, 2, rgb(30, 36, 64));
        t.rect(24, 36, q * 18 / (N_AGATA - 1), 2, rgb(250, 200, 60));
        t.texto(23, 20, "Processando", rgb(250, 200, 60));
        t.texto(34, 20, ".".repeat(1 + q % 3), rgb(250, 200, 60));
        if (q % 2 == 1) {   // glitch: linhas deslocadas
            t.rect(22, 14 + (q % 5) * 4, 22, 1, rgb(250, 90, 120));
            t.rect(26, 15 + (q % 5) * 4, 14, 1, rgb(90, 220, 250));
        }
        return t;
    }

    //  Cenas 306 / 371–374 — 23h58, IC ou DTEC, "preciso escolher AGORA".
    static Tela prazo(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 34, rgb(16, 12, 34), rgb(30, 22, 52));
        t.rect(6, 4, 36, 30, rgb(40, 44, 70));
        t.rect(7, 5, 34, 28, rgb(190, 208, 238));
        t.rect(7, 5, 34, 4, rgb(70, 100, 190));
        t.rect(11, 15, 12, 8, rgb(60, 100, 200));
        t.rect(11, 15, 12, 1, rgb(130, 170, 250));
        t.texto(15, 8, "IC", rgb(255, 255, 255));
        t.rect(25, 15, 13, 8, rgb(40, 150, 100));
        t.rect(25, 15, 13, 1, rgb(110, 220, 170));
        t.texto(28, 8, "DTEC", rgb(255, 255, 255));
        t.rect(11, 26, 27, 1, rgb(120, 130, 170));
        int[][] cursor = {{15, 18}, {23, 24}, {30, 18}, {23, 12}};
        int[] c = cursor[q % 4];
        t.rect(c[0], c[1], 2, 4, rgb(255, 255, 255));
        t.rect(c[0] + 2, c[1] + 3, 2, 1, rgb(255, 255, 255));
        glow(t, 24, 20, 26, 18, rgb(150, 190, 255), 0.30);
        t.rect(46, 8, 17, 12, rgb(10, 8, 20));
        t.rect(46, 8, 17, 1, rgb(60, 50, 90));
        t.texto(48, 6, q >= 6 ? "23:59" : "23:58", rgb(255, 70, 80));
        t.rect(46, 24, 17, 1, rgb(60, 50, 90));
        for (int y = 34; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(54, 36, 46), rgb(30, 22, 30), (y - 34) / 14.0));
        t.rect(0, 34, L, 1, rgb(100, 72, 76));
        costas(t, 52, 30, rgb(120, 76, 56), rgb(26, 20, 40), rgb(92, 62, 112), 14, 14);
        t.rect(45, 34, 3, 14, rgb(26, 20, 40));
        t.rect(4, 38, 5, 5, rgb(214, 214, 230));
        return t;
    }
}