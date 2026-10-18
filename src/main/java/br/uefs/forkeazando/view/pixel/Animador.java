package br.uefs.forkeazando.view.pixel;

import java.io.PrintStream;
import java.util.function.LongConsumer;

/**
 * Toca uma {@link Animacao} no terminal sem piscar e sem repintar tudo:
 * o primeiro quadro é impresso normalmente; os seguintes sobem o cursor até o
 * topo da arte e reescrevem SÓ as linhas que mudaram. Ao terminar o cursor
 * está logo abaixo da imagem, exatamente como depois de imprimir uma arte estática.
 *
 * O tempo entre quadros vem de fora ({@code dormir}), para respeitar a pausa
 * do jogo e para os testes não esperarem de verdade.
 */
public final class Animador {

    private static final String RST = "\033[0m";
    private static final String NL = "\r\n";

    private Animador() {}

    /** FORKEAZANDO_SEM_ANIMACAO=1 desliga as animações (terminais lentos, gravação, acessibilidade). */
    public static boolean habilitada() {
        String v = System.getenv("FORKEAZANDO_SEM_ANIMACAO");
        return v == null || v.isBlank() || v.equals("0");
    }

    /** Imprime só o quadro final (arte estática). */
    public static void mostrarFinal(Animacao animacao, PrintStream out) {
        for (String linha : animacao.renderizar(animacao.total() - 1)) {
            out.println(linha + RST);
        }
    }

    public static void tocar(Animacao animacao, PrintStream out, LongConsumer dormir) {
        String[] anterior = animacao.renderizar(0);
        for (String linha : anterior) {
            out.println(linha + RST);
        }
        if (animacao.total() == 1) {
            return;
        }
        out.print("\033[?25l");   // esconde o cursor durante a animação
        out.flush();
        try {
            for (int q = 1; q < animacao.total(); q++) {
                dormir.accept(animacao.intervaloMs());
                String[] atual = animacao.renderizar(q);
                out.print("\033[" + atual.length + "A");
                for (int r = 0; r < atual.length; r++) {
                    if (!atual[r].equals(anterior[r])) {
                        out.print("\r" + atual[r] + RST);
                    }
                    out.print(NL);
                }
                out.flush();
                anterior = atual;
            }
        } finally {
            out.print("\033[?25h");
            out.flush();
        }
    }
}