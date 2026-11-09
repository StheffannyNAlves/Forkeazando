package br.uefs.forkeazando.view.pixel;

import static br.uefs.forkeazando.view.pixel.Cor.gradiente;
import static br.uefs.forkeazando.view.pixel.Cor.mix;
import static br.uefs.forkeazando.view.pixel.Cor.rgb;
import static br.uefs.forkeazando.view.pixel.Elementos.*;

/** Uma ilustração para cada final (Final.IC, Final.DTEC, Final.INDEPENDENTE). */
final class CenasFinais {

    static final int N_IC = 12, N_DTEC = 12, N_INDEP = 12;
    private static final int L = Ilustracoes.LARGURA, A = Ilustracoes.LINHAS;
    private static final int PELE_P = rgb(120, 76, 56), CAB_P = rgb(26, 20, 40), ROXO = rgb(92, 62, 112);

    private CenasFinais() {}


    //  "A Cientista" — o amanhecer entra pela janela e a curva do gráfico fecha.
    static Tela ic(int q) {
        double k = q / (double) (N_IC - 1);
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 36, mix(rgb(52, 40, 80), rgb(150, 130, 150), k), mix(rgb(40, 34, 70), rgb(120, 100, 120), k));
        // janela com amanhecer
        t.rect(3, 3, 24, 28, rgb(60, 50, 70));
        for (int y = 4; y < 30; y++) {
            int topo = mix(rgb(14, 10, 46), rgb(110, 170, 235), k);
            int hor = mix(rgb(96, 38, 128), rgb(255, 196, 120), k);
            t.rect(4, y, 22, 1, gradiente(y - 4, new int[]{0, 25}, new int[][]{{(topo >> 16) & 255, (topo >> 8) & 255, topo & 255}, {(hor >> 16) & 255, (hor >> 8) & 255, hor & 255}}));
        }
        t.elipse(15, 30 - k * 16, 4.5, 4.5, rgb(255, 238, 170));
        for (int[] p : new int[][]{{4, 24, 5, 6}, {10, 22, 6, 8}, {18, 25, 5, 5}, {22, 23, 4, 7}}) {
            t.rect(p[0], p[1], p[2], p[3], rgb(36, 30, 64));
        }
        t.rect(14, 3, 1, 28, rgb(60, 50, 70));
        t.rect(3, 18, 24, 1, rgb(60, 50, 70));
        // monitor com o gráfico se completando
        t.rect(32, 6, 28, 22, rgb(40, 44, 70));
        t.rect(33, 7, 26, 20, rgb(12, 20, 46));
        t.rect(35, 9, 1, 15, rgb(120, 140, 200));
        t.rect(35, 24, 22, 1, rgb(120, 140, 200));
        int ate = (int) Math.round(k * 21);
        for (int i = 0; i <= ate; i++) {
            int y = 23 - (int) Math.round(12 * (1 - Math.exp(-i / 6.0)));
            t.px(36 + i, y, rgb(110, 230, 150));
            if (i % 5 == 0) t.px(36 + i, y - 1, rgb(255, 214, 102));
        }
        t.texto(46, 5, "Fig. 1", rgb(200, 220, 255));
        t.rect(30, 28, 32, 2, rgb(150, 156, 176));
        // mesa, papers, caneca com vapor
        for (int y = 34; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(120, 84, 60), rgb(66, 46, 40), (y - 34) / 14.0));
        t.rect(0, 34, L, 1, rgb(176, 130, 96));
        t.rect(50, 36, 12, 2, rgb(246, 246, 240));
        t.rect(51, 34, 11, 2, rgb(226, 226, 232));
        t.rect(40, 36, 5, 5, rgb(236, 236, 242));
        t.tint(41 + (q % 2), 34 - q % 3, rgb(255, 255, 255), 0.5);
        glow(t, 15, 24, 24, 18, rgb(255, 214, 150), 0.25 * k);
        costas(t, 22, 33, PELE_P, CAB_P, ROXO, 14, 14);
        t.rect(15, 37, 3, 11, CAB_P);
        return t;
    }

    //  "Mão na Massa" — a placa que a gente montou liga LED por LED.
    static Tela dtec(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 34, rgb(50, 70, 92), rgb(78, 98, 118));
        t.rect(2, 3, 30, 17, rgb(40, 52, 66));                         // painel de ferramentas
        for (int x = 4; x < 31; x += 4) for (int y = 5; y < 19; y += 4) t.px(x, y, rgb(70, 86, 104));
        t.rect(5, 6, 2, 9, rgb(190, 60, 60)); t.rect(11, 6, 2, 11, rgb(220, 190, 60));
        t.rect(17, 7, 4, 2, rgb(170, 176, 190)); t.rect(24, 6, 2, 10, rgb(60, 120, 200));
        pessoa(t, 54, 36, rgb(190, 130, 96), rgb(24, 20, 28), rgb(60, 110, 190), rgb(40, 44, 70), 0);
        pessoa(t, 62, 36, rgb(224, 172, 140), rgb(120, 80, 40), rgb(200, 70, 80), rgb(40, 44, 70), q > 6 ? 1 : 0);
        for (int y = 34; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(150, 112, 78), rgb(80, 58, 46), (y - 34) / 14.0));
        t.rect(0, 34, L, 1, rgb(200, 160, 118));
        t.rect(18, 26, 30, 9, rgb(30, 120, 70));                       // placa
        t.rect(18, 26, 30, 1, rgb(70, 190, 120));
        t.rect(22, 29, 6, 4, rgb(24, 20, 28));
        t.rect(32, 29, 8, 3, rgb(24, 20, 28));
        for (int x = 19; x < 47; x += 3) t.px(x, 28, rgb(180, 220, 190));
        for (int i = 0; i < 5; i++) {
            boolean on = q >= 2 + i * 2;
            int x = 21 + i * 5;
            t.rect(x, 24, 2, 2, on ? rgb(120, 255, 150) : rgb(30, 80, 50));
            if (on) glow(t, x + 1, 25, 5, 4, rgb(120, 255, 150), 0.55);
        }
        t.rect(52, 20, 11, 7, rgb(30, 34, 48));                        // display
        t.rect(53, 21, 9, 5, rgb(120, 170, 110));
        if (q >= 10) t.texto(55, 11, "OK", rgb(20, 60, 30));
        frente(t, 10, 22, PELE_P, CAB_P, rgb(240, 240, 246), 18, true, true);
        t.rect(5, 40, 10, 8, rgb(240, 240, 246));
        return t;
    }


    //  "O Contato" — o celular acende com um projeto novo; o calendário vai fechando.
    static Tela independente(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 36, rgb(70, 48, 64), rgb(100, 66, 70));
        glow(t, 33, 4, 34, 20, rgb(255, 210, 140), 0.22);
        t.rect(3, 4, 14, 15, rgb(240, 236, 226));                      // calendário na parede
        t.rect(3, 4, 14, 3, rgb(200, 60, 70));
        for (int i = 0; i < 12; i++) t.rect(4 + (i % 4) * 3, 9 + (i / 4) * 3, 2, 2, rgb(190, 190, 200));
        int marcas = Math.min(8, q);
        for (int i = 0; i < marcas; i++) t.rect(4 + (i % 4) * 3, 9 + (i / 4) * 3, 2, 2, rgb(70, 190, 100));
        t.rect(46, 4, 17, 20, rgb(60, 50, 80));                        // janela com fim de tarde
        for (int y = 5; y < 23; y++) t.rect(47, y, 15, 1, gradiente(y - 5, new int[]{0, 17}, new int[][]{{120, 80, 140}, {250, 150, 90}}));
        t.rect(54, 4, 1, 20, rgb(60, 50, 80));
        t.elipse(6, 38, 5, 3, rgb(130, 84, 50));                        // vaso
        for (int[] f : new int[][]{{3, 28}, {6, 26}, {9, 29}, {5, 31}}) t.elipse(f[0] + 1, f[1], 3, 3, rgb(60, 160, 90));
        for (int y = 36; y < 48; y++) t.rect(0, y, L, 1, mix(rgb(130, 92, 66), rgb(70, 50, 42), (y - 36) / 12.0));
        t.rect(0, 36, L, 1, rgb(190, 146, 108));
        notebook(t, 20, 22, 24, 13, rgb(220, 232, 250));
        for (int i = 0; i < 4; i++) t.rect(23, 25 + i * 2, 16 - (i % 2) * 5, 1, rgb(130, 140, 180));
        // celular recebendo mensagem
        boolean acesa = q >= 3;
        t.rect(48, 28, 9, 12, rgb(20, 20, 30));
        t.rect(49, 29, 7, 10, acesa ? rgb(230, 230, 224) : rgb(30, 30, 44));
        if (acesa) {
            int w = Math.min(6, 1 + (q - 3) * 2);
            t.rect(49, 31, w, 3, rgb(190, 238, 170));
            if (q >= 7) { t.rect(53, 36, 2, 1, rgb(40, 140, 230)); t.rect(55, 36, 1, 1, rgb(40, 140, 230)); }
            glow(t, 52, 34, 8, 8, rgb(255, 255, 255), 0.25);
        }
        costas(t, 12, 34, PELE_P, CAB_P, ROXO, 14, 14);
        t.rect(5, 38, 3, 10, CAB_P);
        return t;
    }
}