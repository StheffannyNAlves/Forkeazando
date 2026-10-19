package br.uefs.forkeazando.view.pixel;

import java.util.Random;

import static br.uefs.forkeazando.view.pixel.Cor.gradiente;
import static br.uefs.forkeazando.view.pixel.Cor.mix;
import static br.uefs.forkeazando.view.pixel.Cor.rgb;

/**
 * Arte do menu principal: o portão da UEFS ao entardecer. É a mesma arte que estava no
 * MenuPrincipal; o quadro 0 é idêntico à versão estática anterior. Os outros quadros formam um
 * ciclo suave de 8 (nuvens oscilando, estrelas piscando, o sol respirando, vaga-lumes).
 */
public final class AberturaMenu {

    public static final int LARGURA = 66;
    public static final int LINHAS = 24;
    public static final int QUADROS = 8;

    private AberturaMenu() {}

    public static String[] linhas(int quadro) {
        return desenhar(quadro).renderizar();
    }

    public static Tela desenhar(int q) {
        double fase = 2 * Math.PI * q / QUADROS;
        Tela t = new Tela(LARGURA, LINHAS);
        Random rnd = new Random(863); // semente fixa: a arte é sempre a mesma

        // céu em degradê suave
        int[] ys = {0, 8, 14, 20, 25, 29, 32};
        int[][] cs = {
                {14, 10, 46}, {45, 22, 92}, {96, 38, 128}, {176, 60, 130},
                {240, 100, 90}, {255, 150, 60}, {255, 205, 90}
        };
        for (int y = 0; y < 32; y++) {
            t.rect(0, y, LARGURA, 1, gradiente(y, ys, cs));
        }

        // nuvens do pôr do sol (oscilam alguns pixels; as sorteadas não mudam)
        int deriva = (int) Math.round(2 * Math.sin(fase));
        for (int i = 0; i < 16; i++) {
            int y = 6 + rnd.nextInt(22);
            int x = rnd.nextInt(LARGURA) + deriva;
            int len = 5 + rnd.nextInt(10);
            for (int d = 0; d < len; d++) {
                t.px(x + d, y, mix(t.get(x + d, y), rgb(255, 175, 130), 0.30));
            }
            for (int d = 2; d < len - 2; d++) {
                t.px(x + d, y + 1, mix(t.get(x + d, y + 1), rgb(255, 175, 130), 0.15));
            }
        }

        // estrelas (piscam a partir do quadro 1)
        int[][] estrelas = {{44, 3}, {49, 1}, {53, 5}, {57, 2}, {58, 8}, {46, 9}, {60, 4}, {52, 10}};
        for (int i = 0; i < estrelas.length; i++) {
            int[] e = estrelas[i];
            boolean apagada = q != 0 && (i + q) % 4 == 0;
            t.px(e[0], e[1], apagada ? mix(t.get(e[0], e[1]), rgb(255, 250, 215), 0.35) : rgb(255, 250, 215));
        }

        // caixa d'água ao fundo (direita)
        t.rect(62, 5, 4, 1, rgb(150, 150, 168));
        t.rect(62, 6, 4, 6, rgb(206, 206, 218));
        t.rect(62, 8, 4, 1, rgb(165, 165, 185));
        t.rect(63, 12, 2, 22, rgb(125, 125, 142));

        // sol no horizonte (o brilho respira; o núcleo não muda)
        int solX = 45;
        double brilhoFator = 0.55 + 0.10 * Math.sin(fase);
        int[] brilho = {8, 7, 6, 4, 2};
        for (int i = 0; i < brilho.length; i++) {
            int y = 31 - i;
            for (int dx = -brilho[i]; dx <= brilho[i]; dx++) {
                t.px(solX + dx, y, mix(t.get(solX + dx, y), rgb(255, 225, 150), brilhoFator));
            }
        }
        int[] nucleo = {5, 4, 3, 1};
        for (int i = 0; i < nucleo.length; i++) {
            int y = 31 - i;
            for (int dx = -nucleo[i]; dx <= nucleo[i]; dx++) {
                t.px(solX + dx, y, rgb(255, 247, 205));
            }
        }

        // silhuetas de árvores distantes
        int[] distante = {rgb(34, 22, 56), rgb(40, 26, 62), rgb(48, 32, 70), rgb(56, 38, 76), rgb(56, 38, 76)};
        folhagem(t, rnd, 14, 29, 6, 4, distante, 999);
        folhagem(t, rnd, 39, 29, 3, 3, distante, 999);
        folhagem(t, rnd, 50, 29, 2, 3, distante, 999);

        // viga do portão
        t.rect(3, 16, 59, 2, rgb(220, 214, 228));
        t.rect(3, 18, 59, 2, rgb(196, 190, 206));
        t.rect(3, 20, 59, 2, rgb(146, 140, 162));

        // apoio inclinado à esquerda
        for (int y = 22; y <= 31; y++) {
            t.rect(2 - (y - 22) / 2, y, 5, 1, rgb(166, 160, 182));
        }

        //  guaritas
        t.rect(21, 22, 6, 10, rgb(150, 148, 172));
        t.rect(22, 24, 4, 6, rgb(22, 24, 56));
        t.rect(22, 24, 4, 1, rgb(60, 70, 120));
        t.rect(32, 22, 6, 10, rgb(150, 148, 172));
        t.rect(33, 24, 4, 6, rgb(22, 24, 56));
        t.rect(33, 24, 4, 1, rgb(60, 70, 120));

        // pilar central laranja
        t.rect(27, 12, 5, 20, rgb(205, 95, 35));
        t.rect(30, 12, 2, 20, rgb(160, 65, 25));
        t.rect(27, 12, 5, 1, rgb(228, 125, 55));

        // pilar direito com o brasão
        t.rect(51, 22, 8, 10, rgb(205, 95, 35));
        t.rect(58, 22, 1, 10, rgb(160, 65, 25));
        t.rect(52, 22, 6, 10, rgb(244, 242, 248));
        t.rect(53, 22, 4, 1, rgb(232, 192, 64));
        t.rect(53, 23, 4, 4, rgb(40, 72, 164));
        t.rect(54, 27, 2, 1, rgb(40, 72, 164));
        t.rect(54, 24, 2, 2, rgb(232, 192, 64));

        // chão: asfalto com reflexo do sol perto do horizonte
        for (int y = 32; y < 44; y++) {
            double f = (y - 32) / 11.0;
            int base = mix(rgb(88, 68, 88), rgb(44, 40, 54), f);
            for (int x = 0; x < LARGURA; x++) {
                int c = base;
                if (y <= 35) c = mix(c, rgb(235, 125, 75), (4 - (y - 32)) * 0.07);
                if (rnd.nextInt(100) < 7) c = mix(c, rgb(120, 108, 130), 0.5);
                t.px(x, y, c);
            }
        }

        // gramado dos dois lados
        for (int y = 32; y < 44; y++) {
            int esq = 12 + (y - 32) / 2;
            int dir = 54 - (y - 32) / 2;
            for (int x = 0; x < LARGURA; x++) {
                if (x < esq || x >= dir) {
                    int s = rnd.nextInt(100);
                    int c = s < 20 ? rgb(18, 58, 30) : s < 70 ? rgb(30, 88, 42) : rgb(56, 128, 56);
                    t.px(x, y, c);
                }
            }
        }

        // meio-fio listrado
        for (int y = 36; y <= 37; y++) {
            for (int x = 15; x <= 51; x++) {
                t.px(x, y, ((x / 4) % 2 == 0) ? rgb(238, 238, 248) : rgb(34, 30, 72));
            }
        }

        // copa da árvore
        int[] copa = {rgb(16, 44, 30), rgb(24, 68, 38), rgb(36, 96, 48), rgb(62, 128, 58), rgb(176, 150, 52)};
        folhagem(t, rnd, 10, 6, 14, 9, copa, 20);
        folhagem(t, rnd, 31, 7, 14, 9, copa, 24);
        folhagem(t, rnd, 21, 4, 18, 8, copa, 24);
        folhagem(t, rnd, 20, 11, 16, 5, copa, 24);

        // tronco
        int casca = rgb(94, 60, 34);
        int cascaSombra = rgb(60, 36, 22);
        int cascaLuz = rgb(128, 86, 50);
        t.rect(23, 16, 4, 20, casca);
        t.rect(26, 16, 1, 20, cascaSombra);
        t.rect(23, 16, 1, 20, cascaLuz);
        t.rect(21, 33, 8, 3, casca);
        t.rect(28, 33, 1, 3, cascaSombra);
        t.rect(22, 32, 6, 1, casca);
        for (int y = 17; y < 33; y += 3) {
            t.px(24 + rnd.nextInt(2), y, cascaSombra);
        }

        // cones
        cone(t, 37, 33);
        cone(t, 43, 37);

        // arbustos da frente
        int[] arbusto = {rgb(18, 56, 32), rgb(28, 84, 44), rgb(44, 114, 54), rgb(76, 144, 64), rgb(196, 100, 150)};
        folhagem(t, rnd, 60, 28, 9, 7, arbusto, 999);
        folhagem(t, rnd, 4, 36, 7, 4, arbusto, 999);
        t.px(57, 27, rgb(236, 100, 160));
        t.px(62, 31, rgb(236, 100, 160));
        t.px(64, 26, rgb(236, 100, 160));
        t.px(59, 33, rgb(250, 210, 80));

        // vagalumes (só nos quadros animados)
        if (q != 0) {
            int[][] vl = {{10, 30}, {54, 35}, {36, 27}};
            for (int i = 0; i < vl.length; i++) {
                if ((i + q) % 3 == 0) continue;
                int x = vl[i][0] + (int) Math.round(2 * Math.sin(fase + i));
                int y = vl[i][1] + (int) Math.round(1.5 * Math.cos(fase * 2 + i));
                t.px(x, y, rgb(255, 240, 150));
                t.tint(x + 1, y, rgb(255, 240, 150), 0.35);
                t.tint(x - 1, y, rgb(255, 240, 150), 0.35);
            }
        }

        // faixa escura do rodapé com a frase
        t.rect(0, 44, LARGURA, 4, rgb(14, 12, 30));

        int tinta = rgb(66, 60, 84);
        t.texto(5, 9, "UEFS  Universidade", tinta);
        t.texto(33, 9, "Estadual de Feira de Santana", tinta);
        t.texto(53, 15, "UEFS", rgb(30, 52, 122));
        t.texto(2, 22, "Algumas portas não se abrem", rgb(226, 222, 238));
        t.texto(2, 23, "com uma chave, mas com coragem.", rgb(226, 222, 238));

        return t;
    }

    /**
     * Mancha de folhas feita de blocos 2x2 com tons variados:
     * mais escuro embaixo, mais claro em cima, e dourado do lado do sol.
     * p = {escuro, médio, claro, muito claro, quente}
     */
    private static void folhagem(Tela t, Random rnd, int cx, int cy, int rx, int ry, int[] p, int xQuente) {
        for (int by = cy - ry - 1; by <= cy + ry + 1; by += 2) {
            for (int bx = cx - rx - 1; bx <= cx + rx + 1; bx += 2) {
                double dx = (bx + 1 - cx) / (double) rx;
                double dy = (by + 1 - cy) / (double) ry;
                double d = dx * dx + dy * dy + (rnd.nextDouble() - 0.5) * 0.35; // borda irregular
                if (d > 1.0) continue;

                int sorteio = rnd.nextInt(100);
                if (dy > 0.45) sorteio /= 2;                          // embaixo: sombra
                if (dy < -0.4) sorteio = Math.min(99, sorteio + 25);  // em cima: luz
                int cor = sorteio < 28 ? p[0] : sorteio < 62 ? p[1] : sorteio < 88 ? p[2] : p[3];
                if (bx > xQuente && rnd.nextInt(100) < 30) cor = p[4];

                t.rect(bx, by, 2, rnd.nextInt(100) < 85 ? 2 : 1, cor);
            }
        }
    }

    private static void cone(Tela t, int x, int y) {
        int laranja = rgb(255, 122, 26);
        t.px(x, y, laranja);
        t.rect(x - 1, y + 1, 3, 1, rgb(250, 240, 230));
        t.rect(x - 1, y + 2, 3, 1, laranja);
        t.rect(x - 2, y + 3, 5, 1, rgb(60, 50, 70));
    }
}