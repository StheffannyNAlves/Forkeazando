package br.uefs.forkeazando.view.pixel;

import static br.uefs.forkeazando.view.pixel.Cor.*;
import static br.uefs.forkeazando.view.pixel.Elementos.*;

final class CenasComplementares {
    static final int QUADROS = 18;
    private static final int L = Ilustracoes.LARGURA, A = Ilustracoes.LINHAS;
    private static final int ROXO = rgb(100, 69, 132), PELE = rgb(151, 100, 75), CABELO = rgb(35, 26, 43);
    private CenasComplementares() {}

    private static Tela sala(int topo, int baixo) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 34, topo, baixo);
        gradV(t, 0, 34, L, 14, rgb(125, 88, 68), rgb(58, 39, 52));
        t.rect(0, 34, L, 1, rgb(197, 143, 100));
        return t;
    }

    private static void janela(Tela t, int x, int y, boolean noite) {
        t.rect(x - 1, y - 1, 23, 23, rgb(69, 49, 67));
        gradV(t, x, y, 21, 21, noite ? rgb(28, 23, 70) : rgb(238, 153, 127),
                noite ? rgb(106, 59, 111) : rgb(255, 223, 162));
        t.elipse(x + 15, y + 7, 3, 3, rgb(255, 229, 170));
        t.rect(x, y + 15, 21, 6, rgb(61, 57, 87));
        for (int i = 0; i < 5; i++) t.rect(x + i * 4, y + 10 + i % 3, 3, 11 - i % 3, rgb(69, 61, 94));
        t.rect(x + 10, y, 1, 21, rgb(72, 52, 71));
        t.rect(x, y + 11, 21, 1, rgb(72, 52, 71));
    }

    private static void planta(Tela t, int x, int y) {
        t.rect(x, y, 1, 8, rgb(90, 130, 80));
        t.elipse(x - 2, y, 3, 2, rgb(69, 141, 96));
        t.elipse(x + 2, y - 3, 3, 2, rgb(107, 173, 111));
        t.rect(x - 3, y + 7, 7, 5, rgb(194, 112, 89));
        t.rect(x - 4, y + 6, 9, 2, rgb(226, 147, 109));
    }

    static Tela caderno(int q) {
        Tela t = sala(rgb(100, 65, 118), rgb(157, 106, 127));
        janela(t, 41, 3, true);
        t.rect(5, 3, 24, 15, rgb(71, 43, 82));
        for (int i = 0; i < 6; i++) {
            int x = 7 + i * 3, y = 6 + (i % 2) * 5;
            t.rect(x, y, 3, 4, i % 2 == 0 ? rgb(249, 213, 138) : rgb(147, 211, 193));
        }
        t.rect(18, 19, 1, 9, rgb(54, 36, 60));
        t.rect(13, 18, 12, 3, rgb(255, 204, 123));
        glow(t, 19, 27, 19, 13, rgb(255, 208, 138), 0.25);
        t.rect(0, 33, L, 15, rgb(137, 91, 74));
        t.rect(0, 33, L, 1, rgb(224, 169, 120));
        t.rect(12, 34, 31, 13, rgb(246, 230, 198));
        t.rect(26, 34, 1, 13, rgb(179, 147, 139));
        for (int i = 0; i < Math.min(4, q / 3 + 1); i++) t.rect(15, 36 + i * 3, 8 - i % 3, 1, rgb(119, 102, 143));
        t.rect(30, 38, 9, 6, rgb(42, 99, 102));
        t.rect(33, 39, 4, 4, rgb(34, 38, 53));
        for (int i = 0; i < 4; i++) { t.px(32, 39 + i, rgb(215, 187, 118)); t.px(37, 39 + i, rgb(215, 187, 118)); }
        t.rect(48, 37, 8, 8, rgb(36, 29, 54));
        t.rect(49, 38, 6, 6, rgb(108, 186, 193));
        if (q > 9) t.rect(50, 39, 4, 2, rgb(213, 249, 223));
        planta(t, 5, 22);
        t.texto(13, 17, "IDEIAS", rgb(93, 58, 104));
        return t;
    }

    static Tela secretaria(int q) {
        Tela t = sala(rgb(218, 204, 194), rgb(171, 171, 183));
        t.rect(4, 3, 58, 6, rgb(76, 61, 113));
        t.texto(11, 2, "ATENDIMENTO", rgb(255, 226, 164));
        t.rect(6, 12, 14, 18, rgb(241, 234, 213));
        for (int i = 0; i < 4; i++) t.rect(8, 15 + i * 3, 10 - i % 2 * 3, 1, rgb(154, 149, 163));
        frente(t, 42, 14, rgb(179, 125, 93), rgb(55, 39, 48), rgb(67, 115, 124), 16, true, false);
        t.rect(2, 29, 62, 6, rgb(143, 96, 75));
        t.rect(2, 29, 62, 1, rgb(225, 173, 128));
        t.rect(37, 23, 15, 8, rgb(39, 44, 68));
        t.rect(38, 24, 13, 6, rgb(151, 207, 200));
        for (int i = 0; i < Math.min(3, q / 4 + 1); i++) t.rect(39, 25 + i * 2, 7, 1, rgb(64, 122, 125));
        t.rect(22, 28, 12, 4, rgb(250, 237, 211));
        t.rect(24, 29, 6, 1, rgb(119, 102, 135));
        costas(t, 15, 32, PELE, CABELO, ROXO, 13, 14);
        planta(t, 59, 23);
        return t;
    }

    static Tela extensao(int q) {
        Tela t = sala(rgb(207, 223, 202), rgb(152, 184, 176));
        t.rect(6, 3, 54, 13, rgb(55, 83, 78));
        t.rect(6, 3, 54, 1, rgb(192, 156, 105));
        t.texto(13, 3, "CRIAR E TESTAR", rgb(244, 240, 200));
        for (int i = 0; i < 3; i++) {
            int x = 4 + i * 21;
            t.rect(x, 20, 16, 12, rgb(54, 60, 82));
            t.rect(x + 1, 21, 14, 10, rgb(233, 237, 217));
            int mover = q % 6 < 3 ? 0 : 1;
            for (int b = 0; b < 3; b++) t.rect(x + 3 + b % 2, 23 + b * 2 + mover, 7 - b, 2,
                    b % 2 == 0 ? rgb(231, 155, 71) : rgb(143, 100, 166));
            t.rect(x - 1, 32, 18, 2, rgb(176, 148, 111));
            costas(t, x + 7, 34, i == 1 ? rgb(111, 71, 53) : rgb(204, 146, 103), CABELO,
                    i % 2 == 0 ? rgb(60, 116, 174) : rgb(213, 116, 97), 10, 11);
        }
        return t;
    }

    static Tela auditorio(int q) {
        Tela t = sala(rgb(71, 58, 110), rgb(124, 94, 132));
        t.rect(12, 3, 42, 22, rgb(40, 35, 65));
        t.rect(13, 4, 40, 20, rgb(220, 227, 218));
        t.rect(13, 4, 40, 4, rgb(63, 93, 141));
        t.texto(23, 2, "PROJETOS", rgb(246, 232, 184));
        for (int i = 0; i < Math.min(5, q / 3 + 1); i++) t.rect(18, 11 + i * 2, 22 - i * 3, 1, rgb(129, 146, 155));
        glow(t, 33, 16, 24, 16, rgb(224, 235, 222), 0.14);
        pessoa(t, 57, 30, rgb(187, 128, 92), CABELO, rgb(70, 131, 128), rgb(40, 37, 56), 0);
        for (int row = 0; row < 2; row++) for (int i = 0; i < 7; i++)
            costas(t, 6 + i * 9, 33 + row * 8, i % 2 == 0 ? PELE : rgb(214, 158, 120), CABELO,
                    i % 3 == 0 ? ROXO : rgb(69, 82, 108), 7, 8);
        return t;
    }

    static Tela ponto(int q) {
        Tela t = new Tela(L, A);
        gradV(t, 0, 0, L, 38, rgb(49, 38, 96), rgb(231, 135, 117));
        t.elipse(50, 23, 5, 5, rgb(255, 211, 147));
        t.rect(1, 21, 64, 15, rgb(74, 62, 94));
        t.rect(5, 8, 44, 3, rgb(68, 61, 86));
        t.rect(7, 11, 2, 26, rgb(105, 98, 119));
        t.rect(45, 11, 2, 26, rgb(105, 98, 119));
        t.rect(12, 28, 28, 3, rgb(179, 122, 85));
        t.rect(14, 31, 2, 6, rgb(70, 52, 66));
        t.rect(36, 31, 2, 6, rgb(70, 52, 66));
        t.rect(53, 8, 2, 29, rgb(80, 74, 101));
        t.rect(50, 8, 9, 8, rgb(230, 199, 135));
        t.texto(51, 5, "PONTO", rgb(49, 50, 80));
        pessoa(t, 23, 35, PELE, CABELO, ROXO, rgb(55, 48, 80), 0);
        pessoa(t, 36, 35, rgb(194, 141, 107), CABELO, rgb(72, 111, 134), rgb(44, 43, 64), 0);
        gradV(t, 0, 37, L, 11, rgb(76, 63, 84), rgb(36, 29, 54));
        t.rect(0, 38, L, 1, rgb(199, 143, 113));
        int luz = Math.max(0, q - 8);
        t.rect(54 + luz / 4, 41, 4, 1, rgb(246, 203, 128));
        return t;
    }

    static Tela casa(int q) {
        Tela t = sala(rgb(123, 71, 106), rgb(186, 112, 123));
        janela(t, 39, 3, true);
        for (int i = 0; i < 11; i++) {
            int x = 3 + i * 6, y = 3 + (i % 3);
            t.px(x, y, rgb(70, 44, 75));
            t.rect(x, y + 1, 2, 2, (q + i) % 5 == 0 ? rgb(211, 137, 125) : rgb(255, 219, 151));
        }
        frente(t, 16, 21, rgb(191, 130, 91), CABELO, rgb(163, 93, 119), 16, true, false);
        frente(t, 48, 21, PELE, CABELO, ROXO, 16, true, false);
        t.rect(0, 34, L, 14, rgb(158, 95, 76));
        t.rect(0, 34, L, 1, rgb(232, 172, 120));
        t.elipse(33, 39, 10, 3, rgb(239, 213, 174));
        t.rect(27, 35, 13, 6, rgb(238, 191, 133));
        t.rect(27, 35, 13, 2, rgb(252, 232, 195));
        t.rect(12, 38, 4, 6, rgb(210, 228, 209));
        t.rect(50, 38, 4, 6, rgb(238, 216, 176));
        return t;
    }

    static Tela corredor(int q) {
        Tela t = sala(rgb(170, 191, 180), rgb(214, 218, 193));
        janela(t, 4, 4, false);
        t.rect(42, 4, 18, 29, rgb(106, 99, 109));
        t.rect(43, 5, 16, 28, rgb(122, 134, 133));
        t.rect(46, 8, 10, 6, rgb(215, 228, 211));
        t.texto(45, 5, "APOIO", rgb(51, 89, 83));
        t.rect(55, 21, 2, 2, rgb(220, 189, 118));
        t.rect(8, 27, 26, 3, rgb(152, 104, 82));
        t.rect(10, 30, 2, 5, rgb(84, 65, 74));
        t.rect(30, 30, 2, 5, rgb(84, 65, 74));
        costas(t, 21, 23, PELE, CABELO, ROXO, 11, 11);
        planta(t, 37, 23);
        glow(t, 16 + q / 6, 18, 15, 13, rgb(255, 230, 166), 0.12);
        return t;
    }

    static Tela memoria(int q) {
        Tela t = sala(rgb(63, 47, 83), rgb(105, 75, 104));
        t.rect(5, 3, 56, 39, rgb(246, 226, 193));
        t.rect(7, 5, 52, 27, rgb(78, 86, 108));
        t.rect(9, 23, 48, 7, rgb(139, 104, 83));
        t.rect(17, 18, 27, 9, rgb(222, 216, 190));
        for (int x = 19; x < 42; x += 3) for (int y = 20; y < 25; y += 2) t.px(x, y, rgb(153, 146, 137));
        t.rect(25, 14, 2, 8, rgb(185, 78, 77));
        t.rect(38, 17, 2, 7, rgb(51, 56, 80));
        t.rect(29, 16, 3, 3, rgb(102, 66, 59)); // LED apagado: a memória mantém o sentido do final.
        t.rect(12, 9, 12, 8, rgb(42, 48, 71));
        t.rect(13, 10, 10, 5, rgb(121, 174, 172));
        if (q > 7) t.texto(11, 18, "MEDIR ANTES DE SUPOR", rgb(89, 59, 94));
        glow(t, 33, 24, 30, 22, rgb(255, 213, 161), 0.07);
        return t;
    }
}
