package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Protagonista;

public class Roteador {

    public static final int CAPITULO_FINAL = 8;

    public static int proximaCena(int capitulo, Protagonista p) {
        return switch (capitulo) {
            case 0 -> 700;
            case 1 -> 0;
            case 2 -> 100;
            case 3 -> 200;
            case 4 -> 300;

            case 5 -> {
                if (p.temFlag("trilha_ic"))    yield 400;
                if (p.temFlag("trilha_dtec"))  yield 450;
                if (p.temFlag("trilha_indep")) yield 500;
                yield 500;
            }

            case 6 -> 550;

            case 7 -> {
                if (p.temFlag("trilha_ic"))    yield 601;
                if (p.temFlag("trilha_dtec"))  yield 602;
                if (p.temFlag("trilha_indep")) yield 603;
                yield 601;
            }

            case 8 -> 649;

            default -> -1;
        };
    }

    public static boolean temProximoCapitulo(int capituloAtual) {
        return capituloAtual < CAPITULO_FINAL;
    }
}