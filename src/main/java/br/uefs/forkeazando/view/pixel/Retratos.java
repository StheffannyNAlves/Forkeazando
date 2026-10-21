package br.uefs.forkeazando.view.pixel;

import java.util.Optional;

import static br.uefs.forkeazando.view.pixel.Cor.mix;
import static br.uefs.forkeazando.view.pixel.Cor.rgb;

/**
 * Retratos de falantes (24 x 24 pixels = 24 colunas x 12 linhas).
 *
 * A identidade do personagem (cabelo, pele, roupa, acessório) é desenhada uma
 * vez numa base compartilhada; cada expressão só repinta olhos, sobrancelhas e
 * boca. Assim o personagem tem sempre a mesma aparência em qualquer cena.
 */
public final class Retratos {
    public static final int LARGURA = 24;
    public static final int LINHAS = 12;

    public static final String CAIO = "caio";
    public static final String NEUTRA = "neutra";
    public static final String TORTO = "torto";   // sorriso torto (cena 72)
    public static final String LADO = "lado";     // olhar de lado (cena 73)
    public static final String FELIZ = "feliz";
    public static final String SERIA = "seria";
    public static final String TRISTE = "triste";
    public static final String PROCESSANDO = "processando";   // Sistema Acadêmico

    public static final String SERGIO = "sergio";
    public static final String RAFAEL = "rafael";
    public static final String BIA = "bia";
    public static final String DANDARA = "dandara";
    public static final String ZE = "ze";
    public static final String SISTEMA = "sistema";

    /** Início do nome (como aparece em Cena.getPersonagemFalando) de quem cada retrato representa. */
    private static final java.util.Map<String, String> NOMES = java.util.Map.of(
            CAIO, "Caio", SERGIO, "Prof. Dr. Sérgio", RAFAEL, "Prof. Rafael", BIA, "Beatriz",
            DANDARA, "Dandara", ZE, "Zé do", SISTEMA, "Sistema");

    public static String nomeDe(String personagem) { return NOMES.get(personagem); }

    private static final String[] EXPRESSOES = {NEUTRA, FELIZ, SERIA, TRISTE};

    private static final RegistroDeArte REGISTRO = new RegistroDeArte()
            .registrar(CAIO + ":" + NEUTRA, () -> caio(NEUTRA))
            .registrar(CAIO + ":" + TORTO, () -> caio(TORTO))
            .registrar(CAIO + ":" + LADO, () -> caio(LADO));

    static {
        registrarGenerico(SERGIO, RetratosGenericos.SERGIO);
        registrarGenerico(RAFAEL, RetratosGenericos.RAFAEL);
        registrarGenerico(BIA, RetratosGenericos.BIA);
        registrarGenerico(DANDARA, RetratosGenericos.DANDARA);
        registrarGenerico(ZE, RetratosGenericos.ZE);
        REGISTRO.registrar(SISTEMA + ":" + NEUTRA, () -> RetratosGenericos.sistema(NEUTRA));
        REGISTRO.registrar(SISTEMA + ":" + PROCESSANDO, () -> RetratosGenericos.sistema(PROCESSANDO));
    }

    private static void registrarGenerico(String personagem, RetratosGenericos.Perfil perfil) {
        for (String expr : EXPRESSOES) {
            REGISTRO.registrar(personagem + ":" + expr, () -> RetratosGenericos.desenhar(perfil, expr));
        }
    }

    /** Desenha (sem cache) o retrato de qualquer personagem; usado em testes. */
    static Tela desenhar(String personagem, String expressao) {
        return switch (personagem) {
            case CAIO -> caio(expressao);
            case SERGIO -> RetratosGenericos.desenhar(RetratosGenericos.SERGIO, expressao);
            case RAFAEL -> RetratosGenericos.desenhar(RetratosGenericos.RAFAEL, expressao);
            case BIA -> RetratosGenericos.desenhar(RetratosGenericos.BIA, expressao);
            case DANDARA -> RetratosGenericos.desenhar(RetratosGenericos.DANDARA, expressao);
            case ZE -> RetratosGenericos.desenhar(RetratosGenericos.ZE, expressao);
            default -> RetratosGenericos.sistema(expressao);
        };
    }

    private Retratos() {}

    public static boolean existe(String personagem) {
        return REGISTRO.existe(personagem + ":" + NEUTRA);
    }

    /** Linhas ANSI do retrato; expressão desconhecida cai na neutra. Vazio se o personagem não tem retrato. */
    public static Optional<String[]> linhas(String personagem, String expressao) {
        if (personagem == null || !existe(personagem)) {
            return Optional.empty();
        }
        String chave = personagem + ":" + (expressao == null ? NEUTRA : expressao);
        if (!REGISTRO.existe(chave)) {
            chave = personagem + ":" + NEUTRA;
        }
        return Optional.of(REGISTRO.linhas(chave));
    }

    public static java.util.Set<String> chaves() { return REGISTRO.chaves(); }

    // ------------------------------------------------------------------
    //  Cáio Andrade — veterano cínico, olheiras, copo térmico amassado
    // ------------------------------------------------------------------
    private static final int PELE = rgb(184, 126, 96);
    private static final int PELE_L = rgb(204, 148, 114);
    private static final int PELE_S = rgb(150, 100, 78);
    private static final int CAB = rgb(24, 20, 28);
    private static final int CAB_L = rgb(70, 60, 84);
    private static final int MOL = rgb(76, 84, 94);
    private static final int MOL_S = rgb(52, 58, 68);
    private static final int MOL_L = rgb(108, 116, 128);
    private static final int BARBA = rgb(104, 74, 64);
    private static final int OLHEIRA = rgb(122, 80, 78);
    private static final int BRANCO = rgb(236, 236, 246);
    private static final int BOCA = rgb(110, 62, 62);
    private static final int COPO = rgb(30, 120, 134);

    static Tela caio(String expressao) {
        Tela t = baseCaio();
        olhosCaio(t, expressao);
        bocaCaio(t, expressao);
        copoCaio(t);
        int borda = rgb(120, 100, 240);
        for (int i = 0; i < 24; i++) {
            t.px(i, 0, borda);
            t.px(i, 23, borda);
            t.px(0, i, borda);
            t.px(23, i, borda);
        }
        return t;
    }

    /** Região do rosto que muda entre expressões (linhas de pixel, inclusive). */
    static final int ROSTO_Y_MIN = 7;
    static final int ROSTO_Y_MAX = 14;

    private static Tela baseCaio() {
        Tela t = new Tela(LARGURA, LINHAS);
        for (int y = 0; y < 24; y++) {
            for (int x = 0; x < 24; x++) {
                double d = Math.sqrt((x - 12) * (x - 12) + (y - 12) * (y - 12));
                t.px(x, y, mix(rgb(34, 36, 86), rgb(14, 12, 40), Math.min(1, d / 18)));
            }
        }
        // ombros/moletom + capuz + cordões
        for (int y = 18; y < 24; y++) {
            double meia = 9 + (y - 18) * 0.7;
            t.rect((int) (12 - meia), y, (int) (meia * 2), 1, MOL);
        }
        t.rect(3, 19, 2, 5, MOL_L);
        t.rect(19, 19, 2, 5, MOL_S);
        t.rect(8, 17, 8, 3, MOL_S);
        t.rect(9, 17, 6, 1, rgb(40, 44, 54));
        t.rect(11, 20, 1, 4, rgb(150, 156, 168));
        t.rect(13, 20, 1, 4, rgb(150, 156, 168));
        // pescoço + cabeça
        t.rect(10, 15, 4, 3, PELE_S);
        t.elipse(12, 10, 6.2, 7.2, PELE);
        for (int y = 5; y < 15; y++) {
            if (y >= 9 && y <= 11) t.px(7, y, PELE_L);
            t.px(17, y, PELE_S);
        }
        t.px(18, 11, PELE_S);
        t.rect(5, 10, 1, 3, PELE_S);
        t.rect(18, 10, 1, 3, PELE_S);
        // nariz
        t.rect(12, 11, 1, 2, PELE_S);
        t.px(11, 13, PELE_S);
        t.px(13, 13, PELE_S);
        // barba por fazer
        int[][] barba = {{8, 14}, {10, 15}, {12, 15}, {14, 15}, {16, 14}, {9, 15}, {15, 15}, {7, 13}, {17, 13}, {11, 14}, {13, 14}};
        for (int[] b : barba) t.px(b[0], b[1], BARBA);
        // cabelo cacheado bagunçado
        int[][] tufos = {{5, 5}, {7, 3}, {9, 2}, {11, 1}, {13, 2}, {15, 3}, {17, 5}, {4, 8}, {18, 8}, {6, 4}, {16, 4}, {10, 3}, {14, 3}};
        for (int[] b : tufos) t.rect(b[0], b[1], 2, 2, CAB);
        t.rect(6, 5, 12, 3, CAB);
        t.rect(8, 7, 3, 1, CAB);
        t.rect(14, 7, 3, 1, CAB);
        t.rect(5, 8, 2, 3, CAB);
        t.rect(17, 8, 2, 3, CAB);
        int[][] brilho = {{8, 3}, {12, 2}, {15, 4}, {6, 6}, {10, 5}};
        for (int[] b : brilho) t.px(b[0], b[1], CAB_L);
        return t;
    }

    private static void olhosCaio(Tela t, String expressao) {
        for (int x0 : new int[]{8, 14}) {
            t.rect(x0, 9, 3, 1, CAB);
            switch (expressao) {
                case TORTO -> {
                    t.rect(x0, 10, 3, 2, BRANCO);
                    t.rect(x0 + 1, 10, 1, 2, CAB);
                    t.rect(x0, 12, 3, 1, OLHEIRA);
                }
                case LADO -> {
                    t.rect(x0, 10, 3, 1, BRANCO);
                    t.px(x0 + 2, 10, CAB);
                    t.rect(x0, 11, 3, 1, OLHEIRA);
                }
                default -> {   // neutra: pálpebra pesada, olhar julgador
                    t.rect(x0, 10, 3, 1, BRANCO);
                    t.px(x0 + 1, 10, CAB);
                    t.rect(x0, 11, 3, 1, OLHEIRA);
                }
            }
        }
        t.rect(8, 8, 3, 1, CAB);
        if (TORTO.equals(expressao)) {          // uma sobrancelha erguida
            t.rect(14, 7, 3, 1, CAB);
            t.px(17, 8, CAB);
        } else {
            t.rect(14, 8, 3, 1, CAB);
        }
    }

    private static void bocaCaio(Tela t, String expressao) {
        if (TORTO.equals(expressao)) {
            t.rect(10, 14, 4, 1, BOCA);
            t.px(14, 13, BOCA);
            t.px(15, 12, BOCA);
            t.px(12, 14, rgb(240, 236, 236));
        } else if (LADO.equals(expressao)) {
            t.rect(10, 14, 4, 1, BOCA);
            t.px(14, 14, PELE_S);
        } else {
            t.rect(10, 14, 5, 1, BOCA);
        }
    }

    private static void copoCaio(Tela t) {
        t.rect(17, 17, 5, 7, COPO);
        t.rect(17, 17, 1, 7, rgb(120, 214, 224));
        t.rect(18, 20, 3, 2, rgb(16, 70, 84));
        t.px(19, 19, rgb(16, 70, 84));
        t.rect(17, 16, 5, 1, rgb(30, 28, 40));
        t.rect(16, 21, 2, 2, PELE_L);
    }
}