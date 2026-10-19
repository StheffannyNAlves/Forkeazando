package br.uefs.forkeazando.view.pixel;

import static br.uefs.forkeazando.view.pixel.Cor.gradiente;
import static br.uefs.forkeazando.view.pixel.Cor.mix;
import static br.uefs.forkeazando.view.pixel.Cor.rgb;

/**
 * Cartão de transição entre capítulos: cortina que abre do centro, título e subtítulo.
 * A paleta muda com o número do capítulo (do amanhecer da matrícula à noite da formatura).
 * O último quadro (cortina aberta, texto completo) é o cartão final.
 */
public final class Transicao {

    public static final int LARGURA = 66;
    public static final int LINHAS = 10;
    public static final int QUADROS = 12;

    private static final int[][][] PALETAS = {
            {{14, 10, 46}, {96, 38, 128}},      // 0 prólogo: noite roxa
            {{40, 60, 140}, {255, 170, 90}},    // 1 amanhecer
            {{30, 90, 160}, {255, 214, 120}},   // 2 manhã
            {{40, 80, 90}, {170, 210, 170}},    // 3 prova: verde-água frio
            {{60, 40, 120}, {240, 120, 120}},   // 4 bifurcação
            {{30, 50, 100}, {110, 200, 230}},   // 5 trilhas
            {{20, 30, 70}, {120, 90, 160}},     // 6 crise
            {{40, 30, 90}, {250, 160, 100}},    // 7 última prova
            {{10, 8, 38}, {200, 90, 100}}       // 8 formatura
    };

    private Transicao() {}

    /** "Capítulo 5A — IC" -> 5; qualquer coisa sem número (ex.: "Prólogo") -> 0. */
    public static int numeroDe(String capitulo) {
        if (capitulo == null) return 0;
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+)").matcher(capitulo);
        return m.find() ? Math.min(8, Integer.parseInt(m.group(1))) : 0;
    }

    /** "Capítulo 5A — IC" -> "CAPÍTULO 5A". */
    public static String rotuloDe(String capitulo) {
        if (capitulo == null) return "";
        int i = capitulo.indexOf('—');
        return (i >= 0 ? capitulo.substring(0, i) : capitulo).trim().toUpperCase();
    }

    /** "Capítulo 5A — IC" -> "IC"; sem subtítulo -> "". */
    public static String subtituloDe(String capitulo) {
        if (capitulo == null) return "";
        int i = capitulo.indexOf('—');
        return i >= 0 ? capitulo.substring(i + 1).trim() : "";
    }

    public static Animacao animacao(String capitulo) {
        int n = numeroDe(capitulo);
        String rotulo = espacar(rotuloDe(capitulo));
        String sub = subtituloDe(capitulo);
        return new Animacao(QUADROS, 85, q -> quadro(q, n, rotulo, sub));
    }

    /** Letras separadas por um espaço e palavras por três: "C A P Í T U L O   5 A". */
    private static String espacar(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                sb.append("   ");
            } else {
                if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') sb.append(' ');
                sb.append(c);
            }
        }
        return sb.toString();
    }

    static Tela quadro(int q, int numero, String rotulo, String subtitulo) {
        int[][] pal = PALETAS[Math.max(0, Math.min(8, numero))];
        int c0 = rgb(pal[0][0], pal[0][1], pal[0][2]);
        int c1 = rgb(pal[1][0], pal[1][1], pal[1][2]);
        int alto = LINHAS * 2;
        Tela t = new Tela(LARGURA, LINHAS);
        int[] ys = {0, alto - 1};
        int[][] cs = {pal[0], pal[1]};
        for (int y = 0; y < alto; y++) t.rect(0, y, LARGURA, 1, gradiente(y, ys, cs));
        // skyline do campus no horizonte
        int[] alturas = {3, 5, 4, 7, 4, 3, 6, 4, 5, 3, 4, 6, 3, 5, 4, 3, 6, 4, 5, 3, 4};
        for (int i = 0; i < alturas.length; i++) {
            t.rect(i * 3 + 1, alto - alturas[i], 3, alturas[i], mix(c0, rgb(0, 0, 0), 0.65));
        }
        for (int[] e : new int[][]{{6, 2}, {20, 5}, {44, 3}, {58, 6}, {33, 1}}) {
            if ((e[0] + q) % 4 != 0) t.px(e[0], e[1], rgb(255, 250, 215));
        }
        // plaquinha atrás do texto
        int placa = mix(c0, rgb(0, 0, 0), 0.55);
        t.rect(8, 4, LARGURA - 16, 11, placa);
        t.rect(8, 4, LARGURA - 16, 1, mix(c1, rgb(255, 255, 255), 0.3));
        t.rect(8, 14, LARGURA - 16, 1, mix(c1, rgb(255, 255, 255), 0.3));
        if (q >= 4) t.texto((LARGURA - rotulo.length()) / 2, 3, rotulo, rgb(255, 214, 102));
        if (!subtitulo.isEmpty() && q >= 6) {
            int letras = Math.min(subtitulo.length(), (q - 5) * Math.max(1, subtitulo.length() / (QUADROS - 6) + 1));
            String parte = subtitulo.substring(0, letras);
            t.texto((LARGURA - subtitulo.length()) / 2, 5, parte, rgb(226, 222, 238));
        }
        // cortina abrindo do centro
        double abertura = Math.min(1.0, q / 6.0);
        int meia = (int) Math.round(LARGURA / 2.0 * abertura);
        int ini = LARGURA / 2 - meia, fim = LARGURA / 2 + meia;
        for (int x = 0; x < LARGURA; x++) {
            if (x < ini || x >= fim) t.rect(x, 0, 1, alto, (x / 2) % 2 == 0 ? rgb(24, 12, 30) : rgb(36, 18, 44));
        }
        return t;
    }
}