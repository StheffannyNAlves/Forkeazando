
package br.uefs.forkeazando.view.pixel;

import static br.uefs.forkeazando.view.pixel.Cor.mix;
import static br.uefs.forkeazando.view.pixel.Cor.rgb;
import static br.uefs.forkeazando.view.pixel.Elementos.*;

/**
 * Uma ilustração animada para cada final de carreira escolhido no último capítulo
 * (Final.PESQUISA_BRASIL, TECNOLOGIA, INTERNACIONAL, CAMINHO_PROPRIO e TRANSICAO).
 * O último quadro de cada animação é a arte final estática.
 */
final class CenasFuturos {

    static final int N_PESQUISA = 12, N_TECNOLOGIA = 12, N_INTERNACIONAL = 12, N_INDEPENDENTE = 12, N_TRANSICAO = 12;
    private static final int L = Ilustracoes.LARGURA, A = Ilustracoes.LINHAS;
    private static final int PELE_P = rgb(120, 76, 56), CAB_P = rgb(26, 20, 40), ROXO = rgb(92, 62, 112);

    private CenasFuturos() {}

    private static void bancada(Tela t, int yTopo, int claro, int escuro, int brilho) {
        for (int y = yTopo; y < 48; y++) t.rect(0, y, L, 1, mix(claro, escuro, (y - yTopo) / (double) (48 - yTopo)));
        t.rect(0, yTopo, L, 1, brilho);
    }

    // "Aceito" — a janela do laboratório, o gráfico que fecha e o carimbo no artigo.
    static Tela pesquisa(int q) {
        double k = q / (double) (N_PESQUISA - 1);
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 36, rgb(204, 208, 198), rgb(176, 184, 176));
        // janela com o campus: mangueira, prédio de telhado vermelho e sol baixo
        t.rect(3, 3, 26, 27, rgb(70, 78, 86));
        gradV(t, 4, 4, 24, 25, mix(rgb(110, 164, 230), rgb(255, 188, 120), 0.6 * k), mix(rgb(196, 224, 244), rgb(255, 226, 170), 0.6 * k));
        t.elipse(22, 22 - 4 * k, 3.5, 3.5, rgb(255, 240, 180));
        t.rect(18, 21, 10, 8, rgb(222, 208, 182));
        t.rect(17, 20, 12, 1, rgb(180, 94, 70));
        for (int x = 19; x < 27; x += 3) t.rect(x, 23, 2, 2, rgb(80, 110, 150));
        t.rect(10, 20, 2, 9, rgb(98, 66, 42));
        t.elipse(11, 16, 7, 5, rgb(40, 110, 60));
        t.elipse(8, 17, 4, 3, rgb(30, 90, 52));
        t.elipse(15, 15, 4, 3, rgb(66, 140, 72));
        t.rect(16, 3, 1, 27, rgb(70, 78, 86));
        t.rect(3, 16, 26, 1, rgb(70, 78, 86));
        // quadro com o gráfico de barras crescendo
        t.rect(34, 5, 25, 18, rgb(110, 112, 124));
        t.rect(35, 6, 23, 16, rgb(244, 244, 238));
        t.texto(37, 3, "RESULTADOS", rgb(70, 72, 96));
        for (int i = 0; i < 5; i++) {
            int h = (int) Math.round((3 + i * 2.2) * Math.min(1, k * 1.5));
            t.rect(38 + i * 4, 20 - h, 3, h, i == 4 ? rgb(70, 190, 110) : rgb(70, 120, 200));
        }
        t.rect(37, 20, 20, 1, rgb(150, 154, 170));
        // bancada: bandeirinha, pilha de artigos e o artigo recebendo o carimbo
        bancada(t, 36, rgb(130, 92, 66), rgb(70, 50, 42), rgb(190, 146, 108));
        t.rect(6, 28, 1, 9, rgb(190, 190, 200));
        t.rect(7, 28, 9, 6, rgb(40, 150, 80));
        t.elipse(11.5, 31, 3.5, 2.2, rgb(250, 214, 60));
        t.elipse(11.5, 31, 1.5, 1.5, rgb(40, 80, 170));
        t.rect(40, 38, 15, 9, rgb(246, 246, 240));
        t.rect(41, 36, 15, 2, rgb(226, 226, 232));
        for (int i = 0; i < 3; i++) t.rect(42, 40 + i * 2, 11 - i * 2, 1, rgb(160, 164, 178));
        if (q >= 8) {
            int alt = q >= 9 ? 4 : 2;
            t.rect(45, 41, 9, alt, rgb(200, 52, 62));
            if (q >= 9) t.texto(46, 20, "ACEITO", rgb(255, 240, 240));
        }
        glow(t, 15, 18, 22, 16, rgb(255, 214, 150), 0.22 * k);
        costas(t, 26, 33, PELE_P, CAB_P, ROXO, 14, 14);
        return t;
    }

    // "Boot OK" — a placa liga, a onda do osciloscópio estabiliza e o log fecha no notebook.
    static Tela tecnologia(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 34, rgb(22, 28, 46), rgb(42, 50, 68));
        // osciloscópio
        t.rect(3, 6, 24, 20, rgb(70, 78, 92));
        t.rect(5, 8, 16, 13, rgb(6, 16, 10));
        for (int i = 0; i < 16; i += 4) t.rect(5 + i, 8, 1, 13, rgb(14, 40, 26));
        t.rect(5, 14, 16, 1, rgb(14, 40, 26));
        double amp = q < 6 ? 5 - q * 0.6 : 1.4;
        for (int x = 0; x < 16; x++) {
            int y = 14 + (int) Math.round(amp * Math.sin((x + q * 2) * 0.8));
            t.px(5 + x, y, rgb(96, 255, 150));
        }
        t.elipse(24, 11, 1.5, 1.5, rgb(160, 164, 172));
        t.elipse(24, 17, 1.5, 1.5, rgb(160, 164, 172));
        glow(t, 13, 14, 16, 12, rgb(80, 240, 130), 0.22);
        // notebook com o log de boot
        t.rect(40, 5, 24, 19, rgb(36, 40, 62));
        t.rect(41, 6, 22, 17, rgb(10, 14, 28));
        for (int i = 0; i < Math.min(5, q / 2 + 1); i++) t.rect(43, 8 + i * 2, 7 + (i * 5) % 9, 1, rgb(110, 230, 150));
        if (q >= 9) {
            t.rect(43, 18, 16, 4, rgb(30, 110, 60));
            t.texto(45, 9, "BOOT OK", rgb(220, 255, 230));
        }
        // bancada e a placa
        bancada(t, 34, rgb(86, 76, 70), rgb(44, 38, 36), rgb(140, 120, 100));
        t.rect(26, 28, 28, 14, rgb(26, 112, 70));
        t.rect(26, 28, 28, 1, rgb(66, 176, 116));
        for (int x = 28; x < 52; x += 4) {
            t.rect(x, 31, 1, 8, rgb(170, 220, 170));
            t.rect(x, 36, 3, 1, rgb(170, 220, 170));
        }
        t.rect(34, 31, 9, 7, rgb(24, 24, 30));
        for (int i = 0; i < 5; i++) { t.px(33, 31 + i, rgb(200, 204, 210)); t.px(43, 31 + i, rgb(200, 204, 210)); }
        boolean aceso = q >= 4 && q % 4 < 3;
        t.rect(48, 30, 3, 2, aceso ? rgb(120, 255, 150) : rgb(30, 80, 50));
        if (aceso) glow(t, 49, 31, 7, 6, rgb(120, 255, 150), 0.5);
        t.rect(54, 36, 10, 1, rgb(200, 200, 210));
        t.rect(60, 30, 1, 6, rgb(200, 200, 210));
        costas(t, 14, 31, PELE_P, CAB_P, ROXO, 14, 14);
        return t;
    }

    // "Embarque" — o avião sobe pela janela do terminal enquanto a mala espera.
    static Tela internacional(int q) {
        double k = q / (double) (N_INTERNACIONAL - 1);
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 26, mix(rgb(70, 60, 140), rgb(46, 40, 110), k), mix(rgb(255, 150, 90), rgb(255, 120, 100), k));
        t.elipse(48, 25 + 2 * k, 6, 6, rgb(255, 226, 150));
        for (int[] e : new int[][]{{8, 4}, {22, 7}, {40, 3}, {57, 8}}) t.px(e[0], e[1], rgb(240, 240, 255));
        t.rect(0, 24, L, 4, rgb(70, 66, 90));
        for (int x = 0; x < L; x += 6) t.rect(x, 26, 3, 1, rgb(240, 220, 120));
        // avião subindo
        int ax = 6 + (int) Math.round(k * 40), ay = 23 - (int) Math.round(k * 16);
        t.rect(ax, ay, 16, 3, rgb(246, 246, 252));
        t.rect(ax + 16, ay + 1, 2, 2, rgb(246, 246, 252));
        t.rect(ax, ay - 3, 4, 3, rgb(190, 196, 214));
        for (int i = 0; i < 4; i++) t.rect(ax + 5 + i * 2, ay + 3 + i, 4, 1, rgb(190, 196, 214));
        for (int x = ax + 5; x < ax + 15; x += 3) t.px(x, ay + 1, rgb(70, 110, 200));
        t.rect(Math.max(0, ax - 12), ay + 1, Math.min(12, ax), 1, rgb(255, 214, 164));
        // caixilhos da janela panorâmica e painel de embarque
        for (int x = 0; x < L; x += 22) t.rect(x, 0, 2, 30, rgb(40, 36, 56));
        t.rect(0, 28, L, 2, rgb(40, 36, 56));
        t.rect(14, 1, 38, 6, rgb(16, 14, 26));
        t.texto(22, 1, (q % 4 < 3 || q == N_INTERNACIONAL - 1) ? "EMBARQUE" : "        ", rgb(255, 196, 70));
        // piso do terminal, mala, passaporte e cartão
        bancada(t, 30, rgb(96, 88, 108), rgb(46, 42, 62), rgb(130, 122, 148));
        t.rect(44, 34, 11, 13, rgb(210, 82, 60));
        t.rect(44, 34, 11, 1, rgb(240, 124, 100));
        t.rect(48, 30, 2, 4, rgb(60, 60, 72));
        t.rect(46, 38, 7, 1, rgb(150, 52, 40));
        t.rect(57, 40, 7, 6, rgb(30, 58, 112));
        t.elipse(60.5, 43, 1.5, 1.5, rgb(232, 192, 70));
        t.rect(57, 35, 8, 4, rgb(246, 246, 240));
        t.rect(58, 36, 4, 1, rgb(60, 90, 190));
        costas(t, 22, 28, PELE_P, CAB_P, ROXO, 14, 14);
        t.rect(14, 34, 3, 12, rgb(70, 50, 40));
        return t;
    }

    // "Contrato" — a assinatura se desenha na linha enquanto o café esfria.
    static Tela independente(int q) {
        double k = q / (double) (N_INDEPENDENTE - 1);
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 36, rgb(140, 102, 82), rgb(112, 80, 66));
        // janela do café com a cidade
        t.rect(40, 3, 23, 22, rgb(66, 50, 44));
        gradV(t, 41, 4, 21, 20, mix(rgb(120, 170, 230), rgb(250, 190, 130), k), mix(rgb(200, 224, 244), rgb(255, 224, 170), k));
        int[] alt = {8, 12, 6, 14, 9};
        for (int i = 0; i < alt.length; i++) {
            t.rect(41 + i * 4, 24 - alt[i], 4, alt[i], mix(rgb(96, 108, 150), rgb(150, 120, 140), i / 4.0));
            t.px(42 + i * 4, 26 - alt[i], rgb(255, 232, 160));
        }
        t.rect(51, 3, 1, 22, rgb(66, 50, 44));
        // planta e luminária
        t.rect(5, 22, 1, 12, rgb(70, 110, 70));
        t.elipse(6, 19, 4, 4, rgb(56, 140, 80));
        t.elipse(3, 22, 3, 3, rgb(40, 110, 64));
        t.rect(22, 3, 1, 8, rgb(40, 36, 36));
        t.rect(18, 10, 9, 3, rgb(232, 176, 70));
        glow(t, 22, 14, 14, 14, rgb(255, 214, 140), 0.35);
        // mesa
        bancada(t, 34, rgb(130, 90, 62), rgb(72, 50, 40), rgb(186, 140, 100));
        // contrato com a assinatura sendo desenhada
        t.rect(6, 36, 22, 11, rgb(246, 246, 240));
        t.texto(8, 18, "CONTRATO", rgb(70, 72, 96));
        for (int i = 0; i < 2; i++) t.rect(8, 39 + i * 2, 17 - i * 4, 1, rgb(160, 164, 178));
        t.rect(8, 44, 14, 1, rgb(110, 112, 130));
        int tracos = (int) Math.round(k * 13);
        for (int i = 0; i < tracos; i++) t.px(9 + i, 43 - (int) Math.round(1.6 * Math.sin(i * 0.9)), rgb(30, 40, 120));
        t.rect(18 + tracos / 2, 40 - (q >= 4 ? 0 : 0), 1, 3, rgb(60, 60, 70));
        // notebook, caneca com vapor e celular
        notebook(t, 36, 24, 20, 11, rgb(222, 234, 250));
        for (int i = 0; i < 3; i++) t.rect(39, 27 + i * 2, 14 - i * 3, 1, rgb(130, 140, 184));
        t.rect(58, 40, 5, 5, rgb(240, 236, 228));
        t.rect(63, 41, 1, 3, rgb(240, 236, 228));
        t.tint(59 + (q % 2), 38 - q % 3, rgb(255, 255, 255), 0.6);
        t.rect(30, 42, 4, 6, rgb(24, 24, 34));
        t.rect(31, 43, 2, 4, rgb(120, 190, 240));
        costas(t, 46, 31, PELE_P, CAB_P, ROXO, 14, 14);
        return t;
    }

    // "Bifurcação" — a protagonista segue pela trilha até a porta acesa, levando o que viveu.
    static Tela transicao(int q) {
        double k = q / (double) (N_TRANSICAO - 1);
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 28, rgb(30, 24, 76), mix(rgb(210, 112, 112), rgb(250, 160, 100), k));
        for (int[] e : new int[][]{{6, 3}, {15, 8}, {26, 2}, {40, 5}, {52, 9}, {60, 3}}) t.px(e[0], e[1], rgb(240, 240, 255));
        glow(t, 14, 9, 9, 9, rgb(250, 244, 214), 0.25);
        t.elipse(14, 9, 3.5, 3.5, rgb(250, 244, 214));
        t.px(13, 8, rgb(226, 218, 186));
        t.px(15, 10, rgb(226, 218, 186));
        glow(t, 33, 28, 30, 12, rgb(255, 190, 120), 0.5);
        int morro = rgb(36, 32, 66);
        t.elipse(8, 32, 26, 8, morro);
        t.elipse(58, 32, 26, 8, morro);
        t.rect(0, 30, L, 8, morro);
        // porta acesa no fim da trilha da direita
        t.rect(53, 22, 7, 11, rgb(30, 26, 52));
        t.rect(54, 23, 5, 10, mix(rgb(255, 214, 140), rgb(255, 240, 190), k));
        glow(t, 56.5, 28, 14, 12, rgb(255, 204, 124), 0.35 + 0.35 * k);
        // chão e as duas trilhas que se abrem
        gradV(t, 0, 36, L, 12, rgb(56, 46, 68), rgb(30, 26, 42));
        int terra = rgb(160, 122, 98);
        for (int y = 30; y < 48; y++) {
            int abre = Math.max(0, (y - 36) * 2);
            if (y < 36) t.rect(32, y, 3, 1, mix(terra, rgb(80, 60, 80), (36 - y) / 8.0));
            else {
                t.rect(31 - abre / 2, y, 3 + abre / 2, 1, terra);
                t.rect(33 + abre / 4, y, 3 + abre / 2, 1, terra);
            }
        }
        for (int i = 0; i < 18; i++) t.rect(35 + i, 35 - i / 2, 3, 1, mix(terra, rgb(255, 204, 130), i / 20.0));
        // placa com duas setas
        t.rect(18, 24, 2, 14, rgb(96, 70, 52));
        t.rect(11, 25, 12, 3, rgb(178, 132, 92));
        t.rect(23, 25, 2, 3, rgb(178, 132, 92));
        t.rect(14, 30, 12, 3, rgb(150, 110, 78));
        t.rect(11, 30, 3, 3, rgb(150, 110, 78));
        // protagonista (de costas, com mochila) caminhando trilha acima
        int y0 = 34 - (int) Math.round(k * 8);
        costas(t, 33, y0, PELE_P, CAB_P, ROXO, 12, 12);
        t.rect(27, y0 + 7, 2, 7, rgb(170, 100, 60));
        t.rect(38, y0 + 7, 2, 7, rgb(170, 100, 60));
        return t;
    }
}
