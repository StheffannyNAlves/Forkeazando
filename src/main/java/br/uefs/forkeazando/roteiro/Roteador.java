package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Estado;
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
            case 6 -> 540;
            case 7 -> 600;
            case 8 -> 649;
            default -> -1;
        };
    }

    public static int proximaCena(int capitulo, Estado estado) {
        if (estado == null || estado.getProtagonista() == null) {
            throw new IllegalArgumentException(
                    "O estado e o protagonista não podem ser null.");
        }
        if (capitulo != 5 || estado.getTrilha() == null) {
            return proximaCena(capitulo, estado.getProtagonista());
        }
        return switch (estado.getTrilha()) {
            case IC -> 400;
            case DTEC -> 450;
            case INDEP -> 500;
        };
    }

    // FIX: fallback por sistema_chutou_* quando nenhuma flag trilha_* existe.
    // Caso aconteça (save corrompido, partida legada, ou o jogador contestar
    // o Sistema e não se inscrever), o chute do ÁGATA é a última rota válida.
    private static int identificarTrilha(Protagonista p) {
        boolean ic = p.temFlag("trilha_ic");
        boolean dtec = p.temFlag("trilha_dtec");
        boolean indep = p.temFlag("trilha_indep");
        int quantidade = (ic ? 1 : 0) + (dtec ? 1 : 0) + (indep ? 1 : 0);

        if (quantidade > 1) return -1;   // conflito → erro de rota
        if (ic) return 1;
        if (dtec) return 2;
        if (indep) return 3;

        // nenhuma trilha explícita → usa o chute do Sistema
        if (p.temFlag("sistema_chutou_ic"))   return 1;
        if (p.temFlag("sistema_chutou_dtec")) return 2;
        if (p.temFlag("sistema_chutou_indep")) return 3;

        return -1;
    }

    public static boolean temProximoCapitulo(int capituloAtual) {
        return capituloAtual >= 0 && capituloAtual < CAPITULO_FINAL;
    }
}