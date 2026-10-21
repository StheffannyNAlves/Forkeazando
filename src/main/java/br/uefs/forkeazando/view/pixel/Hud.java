package br.uefs.forkeazando.view.pixel;

/**
 * HUD de duas linhas no cabeçalho da cena: rank + indicador de pausa, e as três barras
 * (energia, score, participação). Cada linha devolve o texto com ANSI e o tamanho VISÍVEL,
 * porque a caixa do CenaView precisa do tamanho sem os códigos de cor para alinhar a borda.
 */

public final class Hud {

    public record Linha(String ansi, int visivel) {}

    private static final String RST = "\033[0m";
    private static final String CINZA = fg(150, 154, 180);
    private static final String VAZIO = fg(64, 66, 96);

    private Hud() {}

    private static String fg(int r, int g, int b) {
        return "\033[38;2;" + r + ";" + g + ";" + b + "m";
    }

    /** "RANK: X" à esquerda, indicador de pausa à direita, tudo em `largura` colunas visíveis. */
    public static Linha status(String rank, String indicadorPause, int largura) {
        String esq = "RANK: " + rank;
        int espacos = Math.max(1, largura - esq.length() - indicadorPause.length());
        String plano = esq + " ".repeat(espacos) + indicadorPause;
        if (plano.length() > largura) plano = plano.substring(0, largura);
        String ansi = CINZA + esq + " ".repeat(espacos) + indicadorPause + RST;
        if (esq.length() + 1 + indicadorPause.length() > largura) ansi = CINZA + plano + RST;
        return new Linha(ansi, plano.length());
    }

    /** ♥ energia (verde/amarelo/vermelho conforme o nível), ★ score, ◆ participação. */
    public static Linha barras(int energia, int energiaMax, int score, int participacao) {
        StringBuilder ansi = new StringBuilder();
        StringBuilder plano = new StringBuilder();

        double nivel = energiaMax <= 0 ? 0 : Math.max(0, Math.min(1, energia / (double) energiaMax));
        String corEnergia = nivel > 0.5 ? fg(110, 220, 130) : nivel > 0.25 ? fg(255, 206, 84) : fg(244, 84, 96);
        segmento(ansi, plano, "♥", fg(255, 84, 110), corEnergia, nivel, 10, energia + "/" + energiaMax);
        ansi.append("  ");
        plano.append("  ");
        segmento(ansi, plano, "★", fg(255, 214, 102), fg(255, 190, 70), fracao(score), 6, String.valueOf(score));
        ansi.append("  ");
        plano.append("  ");
        segmento(ansi, plano, "◆", fg(110, 190, 255), fg(80, 160, 255), fracao(participacao), 6, String.valueOf(participacao));
        return new Linha(ansi.toString(), plano.length());
    }

    private static double fracao(int valor) {
        return Math.max(0, Math.min(1, valor / 100.0));
    }

    private static void segmento(StringBuilder ansi, StringBuilder plano, String icone, String corIcone,
                                 String corBarra, double nivel, int blocos, String numero) {
        int cheios = (int) Math.round(nivel * blocos);
        ansi.append(corIcone).append(icone).append(' ').append(corBarra).append("█".repeat(cheios))
                .append(VAZIO).append("░".repeat(blocos - cheios)).append(' ').append(CINZA).append(numero).append(RST);
        plano.append(icone).append(' ').append("█".repeat(cheios)).append("░".repeat(blocos - cheios))
                .append(' ').append(numero);
    }
}