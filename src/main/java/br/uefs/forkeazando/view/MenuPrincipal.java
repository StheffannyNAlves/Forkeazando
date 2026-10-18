package br.uefs.forkeazando.view;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MenuPrincipal {

    private static final int ARTE_L = 66;   // largura da arte (colunas)
    private static final int ARTE_A = 24;   // altura da arte (linhas de texto = 48 "pixels")
    private static final int MENU_L = 32;   // largura da caixa do menu (com bordas)
    private static final int ESPACO = 3;    // espaço entre a arte e o menu
    private static final int LARGURA_LADO_A_LADO = ARTE_L + ESPACO + MENU_L;

    //  estilo do menu (truecolor, parecido com a imagem de referência)
    private static final String RST = "\033[0m";
    private static final String BORDA = "\033[38;2;120;100;240m";
    private static final String BG_CAIXA = "\033[48;2;14;12;40m";
    private static final String TXT = "\033[38;2;205;210;255m";
    private static final String TAG = "\033[38;2;196;186;255m";
    private static final String OURO = "\033[1;38;2;255;214;102m";
    private static final String SEL = "\033[1;38;2;255;255;255;48;2;76;56;215m";
    private static final String DICA = "\033[38;2;255;206;84m";

    private static final String[] OPCOES = {
            "[1] Nova Partida",
            "[2] Continuar",
            "[3] Carregar",
            "[4] Instruções",
            "[5] Opções de Estilo",
            "[6] Salvar",
            "[7] Créditos",
            "[8] Meu progresso",
            "[0] Sair"
    };

    private static final String[] TAGLINE = {
            "Entre o que era certo",
            "e o que ainda pode ser."
    };

    private String[] arte;          // linhas já renderizadas (desenhadas uma vez só)
    private int cacheLargura = -1;
    private int cacheAltura = -1;


    //  MENU


    public void mostrarMenu(int selecionado) {
        Terminal terminal = Entrada.terminal();
        PrintWriter out = terminal.writer();
        String nl = System.lineSeparator();

        int larg = terminal.getWidth();
        int alt = terminal.getHeight();

        if (larg < MENU_L + 4) {
            terminal.puts(InfoCmp.Capability.clear_screen);
            out.println("Aumente a largura do terminal.");
            out.flush();
            return;
        }

        // Limpa a tela inteira só quando o tamanho muda; nas outras teclas
        // volta ao topo e sobrescreve (sem piscar).
        if (larg != cacheLargura || alt != cacheAltura) {
            terminal.puts(InfoCmp.Capability.clear_screen);
            cacheLargura = larg;
            cacheAltura = alt;
        }
        if (arte == null) {
            arte = desenharArte();
        }

        boolean comArte = larg >= LARGURA_LADO_A_LADO && alt > ARTE_A;
        List<String> menu = montarMenu(selecionado);

        out.print("\033[H");

        if (comArte) {
            // arte + menu lado a lado, centralizados como UM bloco só
            String margem = " ".repeat((larg - LARGURA_LADO_A_LADO) / 2);
            int deslocamento = (ARTE_A - menu.size()) / 2;

            for (int r = 0; r < ARTE_A; r++) {
                int i = r - deslocamento;
                String direita = (i >= 0 && i < menu.size())
                        ? menu.get(i)
                        : " ".repeat(MENU_L);
                out.print(margem + arte[r] + RST + " ".repeat(ESPACO) + direita + "\033[K" + nl);
            }
        } else {
            // terminal pequeno: só o menu, centralizado
            String margem = " ".repeat((larg - MENU_L) / 2);
            out.print(nl);
            for (String linha : menu) {
                out.print(margem + linha + "\033[K" + nl);
            }
        }

        out.print("\033[J");
        out.flush();
    }

    private List<String> montarMenu(int selecionado) {
        int interno = MENU_L - 2;
        List<String> l = new ArrayList<>();

        l.add(BORDA + "╭" + "─".repeat(interno) + "╮" + RST);
        l.add(caixa(OURO + centralizar("FORKEAZANDO", interno)));
        for (String t : TAGLINE) {
            l.add(caixa(TAG + centralizar(t, interno)));
        }
        l.add(BORDA + "├" + "─".repeat(interno) + "┤" + RST);

        for (int i = 0; i < OPCOES.length; i++) {
            boolean ativo = i == selecionado;
            // alinhado à esquerda: o texto não "pula" quando o ❯ aparece
            String texto = alinharEsquerda(" " + (ativo ? "❯ " : "  ") + OPCOES[i], interno);
            l.add(caixa((ativo ? SEL : TXT) + texto));
        }

        l.add(BORDA + "╰" + "─".repeat(interno) + "╯" + RST);
        l.add(DICA + alinharEsquerda("↑ ↓ navegar   ENTER escolher", MENU_L) + RST);
        return l;
    }

    /** Uma linha da caixa: bordas + fundo escuro + conteúdo já colorido. */
    private String caixa(String interno) {
        return BORDA + BG_CAIXA + "│" + interno + "\033[22m" + BORDA + BG_CAIXA + "│" + RST;
    }

    //  ARTE: portão da UEFS ao entardecer, em pixel art
    //
    //  Cada caractere "▀" mostra 2 pixels (cima = cor da letra, baixo = cor do
    //  fundo). Então a arte tem 66 x 48 pixels, e os pixels ficam quadrados.

    private String[] desenharArte() {
        Tela t = new Tela(ARTE_L, ARTE_A);
        Random rnd = new Random(863); // semente fixa: a arte é sempre a mesma

        // céu em degradê suave (azul-noite -> roxo -> rosa -> laranja -> dourado)
        int[] ys = {0, 8, 14, 20, 25, 29, 32};
        int[][] cs = {
                {14, 10, 46}, {45, 22, 92}, {96, 38, 128}, {176, 60, 130},
                {240, 100, 90}, {255, 150, 60}, {255, 205, 90}
        };
        for (int y = 0; y < 32; y++) {
            t.rect(0, y, ARTE_L, 1, gradiente(y, ys, cs));
        }

        //  nuvens do pôr do sol (faixas claras)
        for (int i = 0; i < 16; i++) {
            int y = 6 + rnd.nextInt(22);
            int x = rnd.nextInt(ARTE_L);
            int len = 5 + rnd.nextInt(10);
            for (int d = 0; d < len; d++) {
                t.px(x + d, y, mix(t.get(x + d, y), rgb(255, 175, 130), 0.30));
            }
            for (int d = 2; d < len - 2; d++) {
                t.px(x + d, y + 1, mix(t.get(x + d, y + 1), rgb(255, 175, 130), 0.15));
            }
        }

        int[][] estrelas = {{44, 3}, {49, 1}, {53, 5}, {57, 2}, {58, 8}, {46, 9}, {60, 4}, {52, 10}};
        for (int[] e : estrelas) {
            t.px(e[0], e[1], rgb(255, 250, 215));
        }

        // caixa d'água ao fundo (direita)
        t.rect(62, 5, 4, 1, rgb(150, 150, 168));
        t.rect(62, 6, 4, 6, rgb(206, 206, 218));
        t.rect(62, 8, 4, 1, rgb(165, 165, 185));
        t.rect(63, 12, 2, 22, rgb(125, 125, 142));

        //  sol no horizonte (brilho + núcleo)
        int solX = 45;
        int[] brilho = {8, 7, 6, 4, 2};
        for (int i = 0; i < brilho.length; i++) {
            int y = 31 - i;
            for (int dx = -brilho[i]; dx <= brilho[i]; dx++) {
                t.px(solX + dx, y, mix(t.get(solX + dx, y), rgb(255, 225, 150), 0.55));
            }
        }
        int[] nucleo = {5, 4, 3, 1};
        for (int i = 0; i < nucleo.length; i++) {
            int y = 31 - i;
            for (int dx = -nucleo[i]; dx <= nucleo[i]; dx++) {
                t.px(solX + dx, y, rgb(255, 247, 205));
            }
        }

        // silhuetas de árvores distantes, vistas por baixo do portão
        int[] distante = {rgb(34, 22, 56), rgb(40, 26, 62), rgb(48, 32, 70), rgb(56, 38, 76), rgb(56, 38, 76)};
        folhagem(t, rnd, 14, 29, 6, 4, distante, 999);
        folhagem(t, rnd, 39, 29, 3, 3, distante, 999);
        folhagem(t, rnd, 50, 29, 2, 3, distante, 999);

        // viga do portão (concreto, com luz em cima e sombra embaixo)
        t.rect(3, 16, 59, 2, rgb(220, 214, 228));
        t.rect(3, 18, 59, 2, rgb(196, 190, 206));
        t.rect(3, 20, 59, 2, rgb(146, 140, 162));

        // apoio inclinado à esquerda (escadinha de pixels)
        for (int y = 22; y <= 31; y++) {
            t.rect(2 - (y - 22) / 2, y, 5, 1, rgb(166, 160, 182));
        }

        // guaritas (cinza com janela escura e reflexo)
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
            for (int x = 0; x < ARTE_L; x++) {
                int c = base;
                if (y <= 35) c = mix(c, rgb(235, 125, 75), (4 - (y - 32)) * 0.07);
                if (rnd.nextInt(100) < 7) c = mix(c, rgb(120, 108, 130), 0.5);
                t.px(x, y, c);
            }
        }

        //  gramado dos dois lados (afunila em perspectiva)
        for (int y = 32; y < 44; y++) {
            int esq = 12 + (y - 32) / 2;
            int dir = 54 - (y - 32) / 2;
            for (int x = 0; x < ARTE_L; x++) {
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

        // copa da árvore (vários "tufos" de folhas; lado do sol fica dourado)
        int[] copa = {rgb(16, 44, 30), rgb(24, 68, 38), rgb(36, 96, 48), rgb(62, 128, 58), rgb(176, 150, 52)};
        folhagem(t, rnd, 10, 6, 14, 9, copa, 20);
        folhagem(t, rnd, 31, 7, 14, 9, copa, 24);
        folhagem(t, rnd, 21, 4, 18, 8, copa, 24);
        folhagem(t, rnd, 20, 11, 16, 5, copa, 24);

        // tronco (na frente de tudo)
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

        // faixa escura do rodapé com a frase
        t.rect(0, 44, ARTE_L, 4, rgb(14, 12, 30));

        // textos desenhados por cima
        int tinta = rgb(66, 60, 84);
        t.texto(5, 9, "UEFP  Universidade", tinta);
        t.texto(33, 9, "Estadual de Fork Perdido", tinta);
        t.texto(53, 15, "UEFP", rgb(30, 52, 122));
        t.texto(2, 22, "Algumas portas não se abrem", rgb(226, 222, 238));
        t.texto(2, 23, "com uma chave, mas com coragem.", rgb(226, 222, 238));

        return t.renderizar();
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


    private static int rgb(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    private static int mix(int a, int b, double f) {
        int r = (int) Math.round(((a >> 16) & 255) * (1 - f) + ((b >> 16) & 255) * f);
        int g = (int) Math.round(((a >> 8) & 255) * (1 - f) + ((b >> 8) & 255) * f);
        int bl = (int) Math.round((a & 255) * (1 - f) + (b & 255) * f);
        return rgb(r, g, bl);
    }

    private static int gradiente(int y, int[] ys, int[][] cs) {
        for (int i = 0; i < ys.length - 1; i++) {
            if (y <= ys[i + 1]) {
                double f = (double) (y - ys[i]) / (ys[i + 1] - ys[i]);
                int[] a = cs[i];
                int[] b = cs[i + 1];
                return mix(rgb(a[0], a[1], a[2]), rgb(b[0], b[1], b[2]), f);
            }
        }
        int[] u = cs[cs.length - 1];
        return rgb(u[0], u[1], u[2]);
    }

    /**
     * Canvas de pixels. Cada linha de texto tem 2 pixels de altura (usando "▀").
     * Textos são uma camada à parte: usam o fundo do pixel de cima da célula.
     */
    public static final class Tela {
        private final int w, h, linhas;
        private final int[][] pix;
        private final char[][] txt;
        private final int[][] txtFg;

        Tela(int w, int linhas) {
            this.w = w;
            this.linhas = linhas;
            this.h = linhas * 2;
            pix = new int[h][w];
            txt = new char[linhas][w];
            txtFg = new int[linhas][w];
        }

        int get(int x, int y) {
            return (x >= 0 && x < w && y >= 0 && y < h) ? pix[y][x] : 0;
        }

        void px(int x, int y, int cor) {
            if (x >= 0 && x < w && y >= 0 && y < h) {
                pix[y][x] = cor;
            }
        }

        void rect(int x, int y, int largura, int altura, int cor) {
            for (int j = y; j < y + altura; j++) {
                for (int i = x; i < x + largura; i++) {
                    px(i, j, cor);
                }
            }
        }

        void texto(int x, int linha, String s, int cor) {
            if (linha < 0 || linha >= linhas) return;
            for (int i = 0; i < s.length(); i++) {
                int xx = x + i;
                if (xx >= 0 && xx < w) {
                    txt[linha][xx] = s.charAt(i);
                    txtFg[linha][xx] = cor;
                }
            }
        }

        private static void cor(StringBuilder sb, boolean frente, int c) {
            sb.append(frente ? "\033[38;2;" : "\033[48;2;")
                    .append((c >> 16) & 255).append(';')
                    .append((c >> 8) & 255).append(';')
                    .append(c & 255).append('m');
        }

        String[] renderizar() {
            String[] saida = new String[linhas];
            for (int r = 0; r < linhas; r++) {
                StringBuilder sb = new StringBuilder();
                int ultimoFg = -1, ultimoBg = -1;
                for (int x = 0; x < w; x++) {
                    int topo = pix[2 * r][x];
                    int base = pix[2 * r + 1][x];
                    char c;
                    int f, b;
                    if (txt[r][x] != 0) {
                        c = txt[r][x];
                        f = txtFg[r][x];
                        b = topo;
                    } else {
                        c = '▀';
                        f = topo;
                        b = base;
                    }
                    if (f != ultimoFg) {
                        cor(sb, true, f);
                        ultimoFg = f;
                    }
                    if (b != ultimoBg) {
                        cor(sb, false, b);
                        ultimoBg = b;
                    }
                    sb.append(c);
                }
                sb.append("\033[0m");
                saida[r] = sb.toString();
            }
            return saida;
        }
    }


    private String centralizar(String texto, int largura) {
        if (texto.length() > largura) texto = texto.substring(0, largura);
        int total = largura - texto.length();
        int esquerda = total / 2;
        return " ".repeat(esquerda) + texto + " ".repeat(total - esquerda);
    }

    private String alinharEsquerda(String texto, int largura) {
        if (texto.length() > largura) texto = texto.substring(0, largura);
        return texto + " ".repeat(largura - texto.length());
    }


    public String lerOpcoes() {
        return Entrada.lerLinha();
    }

    public void mostrarInstrucoes() {
        System.out.println(Cores.CIANO + "\n========================================================");
        System.out.println("                 GUIA DE SOBREVIVÊNCIA");
        System.out.println("========================================================" + Cores.RESET);

        System.out.println("\nBem-vinda a Engenharia de Computação. Para não reprovar logo no primeiro semestre, preste atenção nestas regras:\n");

        System.out.println(Cores.MAGENTA + "[ ESCOLHAS E CONSEQUÊNCIAS ]" + Cores.RESET);
        System.out.println("Leia atentamente cada cena e digite o número da sua escolha.");
        System.out.println("Cada decisão molda a sua " + Cores.AMARELO + "Personalidade (Flags)" + Cores.RESET + ". Suas respostas desbloqueiam opções ocultas no futuro ou bloqueiam caminhos.\n");

        System.out.println(Cores.VERDE + "[ GERENCIAMENTO DE RECURSOS ]" + Cores.RESET);
        System.out.println("Sua vida universitária é movida por três pilares:");

        System.out.println(Cores.VERMELHO + "Energia:" + Cores.RESET + " Sua moeda mais valiosa. Assumir muita responsabilidade drena energia rapidamente.");
        System.out.println(Cores.AMARELO + "Score:" + Cores.RESET + " Seu desempenho técnico. Necessário para provar que você sabe o que está fazendo.");
        System.out.println(Cores.AZUL + "Participação:" + Cores.RESET + " Sua voz ativa nas discussões de PBL. Ficar calada poupa energia, mas bloqueia o acesso a laboratórios.\n");

        System.out.println(Cores.MAGENTA + "[ RELACIONAMENTOS ]" + Cores.RESET);
        System.out.println("Os veteranos, professores e colegas lembram de você. O mercado é feito de networking (e o Módulo 8 também).\n");

        System.out.println(Cores.AMARELO + "Dica:" + Cores.RESET + " O seu background (Situação Econômica e Ensino Médio) define sua Energia Inicial. O sistema não é justo. Adapte-se.\n");

        System.out.println(Cores.CIANO + "Pressione [ENTER] para voltar ao menu principal..." + Cores.RESET);
        Entrada.lerLinha();
    }

    public void mostrarCreditos() {
        System.out.println(Cores.CIANO + "╔══════════════════════════════╗" + Cores.RESET);
        System.out.println(Cores.CIANO + "║           CRÉDITOS           ║" + Cores.RESET);
        System.out.println(Cores.CIANO + "║                              ║" + Cores.RESET);
        System.out.println(Cores.MAGEN + "║       Stheffanny Alves       ║" + Cores.RESET);
        System.out.println(Cores.BRANCO + "║                              ║" + Cores.RESET);
        System.out.println(Cores.CIANO + "║         FORKEAZANDO          ║" + Cores.RESET);
        System.out.println(Cores.CIANO + "║             2026             ║" + Cores.RESET);
        System.out.println(Cores.CIANO + "╚══════════════════════════════╝" + Cores.RESET);
    }

    public void mostrarMensagem(String msg) {
        System.out.println(msg);
    }
}