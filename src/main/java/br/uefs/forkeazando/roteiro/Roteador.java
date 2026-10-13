
package br.uefs.forkeazando.roteiro;
import br.uefs.forkeazando.model.Protagonista;

public class Roteador {

    public static final int CAPITULO_FINAL = 8;
    private Roteador() {}

    public static int proximaCena(int capitulo, Protagonista p) {
        if (p == null) {
            throw new IllegalArgumentException("O protagonista não pode ser null.");
        }

        return switch (capitulo) {
            case 0 -> 700;
            case 1 -> 0;
            case 2 -> 100;
            case 3 -> 200;
            case 4 -> 300;

            case 5 -> {
                int trilha = identificarTrilha(p);

                yield switch (trilha) {
                    case 1 -> 400;
                    case 2 -> 450;
                    case 3 -> 500;
                    default -> -1;
                };
            }

            case 6 -> 550;

            case 7 -> 600;

            case 8 -> 649;

            default -> -1;
        };
    }


    private static int identificarTrilha(Protagonista p) {
        boolean ic = p.temFlag("trilha_ic");
        boolean dtec = p.temFlag("trilha_dtec");
        boolean independente = p.temFlag("trilha_indep");

        int quantidade = (ic ? 1 : 0)
                + (dtec ? 1 : 0)
                + (independente ? 1 : 0);

        if (quantidade == 0) {
            return 3;
        }

        if (quantidade > 1) {
            return -1;
        }

        if (ic) {
            return 1;
        }

        if (dtec) {
            return 2;
        }

        return 3;
    }


    public static boolean temProximoCapitulo(int capituloAtual) {
        return capituloAtual >= 0
                && capituloAtual < CAPITULO_FINAL;
    }
}
