package br.uefs.forkeazando.view.pixel;

/**
 * Canvas de pixels para o terminal. Cada linha de texto tem 2 pixels de altura
 * (caractere "▀": cima = cor da frente, baixo = cor do fundo), então uma Tela de
 * L colunas x N linhas tem L x 2N pixels, e os pixels ficam quadrados.
 *
 * Textos são uma camada à parte: usam o fundo do pixel de cima da célula.
 *
 * Extraída do MenuPrincipal sem mudar o comportamento de renderizar().
 */
public final class Tela {
    private final int w, h, linhas;
    private final int[][] pix;
    private final char[][] txt;
    private final int[][] txtFg;

    public Tela(int largura, int linhas) {
        this.w = largura;
        this.linhas = linhas;
        this.h = linhas * 2;
        pix = new int[h][w];
        txt = new char[linhas][w];
        txtFg = new int[linhas][w];
    }

    public int largura() { return w; }
    public int linhas() { return linhas; }
    public int altura() { return h; }

    public int get(int x, int y) {
        return (x >= 0 && x < w && y >= 0 && y < h) ? pix[y][x] : 0;
    }

    public void px(int x, int y, int cor) {
        if (x >= 0 && x < w && y >= 0 && y < h) {
            pix[y][x] = cor;
        }
    }

    public void rect(int x, int y, int largura, int altura, int cor) {
        for (int j = y; j < y + altura; j++) {
            for (int i = x; i < x + largura; i++) {
                px(i, j, cor);
            }
        }
    }

    /** Mistura o pixel existente com uma cor (luz, sombra, brilho). */
    public void tint(int x, int y, int cor, double f) {
        px(x, y, Cor.mix(get(x, y), cor, f));
    }

    public void elipse(double cx, double cy, double rx, double ry, int cor) {
        for (int y = (int) (cy - ry) - 1; y <= (int) (cy + ry) + 1; y++) {
            for (int x = (int) (cx - rx) - 1; x <= (int) (cx + rx) + 1; x++) {
                double dx = (x + 0.5 - cx) / rx;
                double dy = (y + 0.5 - cy) / ry;
                if (dx * dx + dy * dy <= 1.0) {
                    px(x, y, cor);
                }
            }
        }
    }

    public void texto(int x, int linha, String s, int cor) {
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

    /** Uma String ANSI por linha de texto, já com reset no fim. */
    public String[] renderizar() {
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