package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Protagonista;
import java.util.Set;

public final class CenasDeChefe {
    private static final Set<Integer> IDS = Set.of(201, 305, 451, 550, 601, 649);

    private CenasDeChefe() {}

    public static boolean ehChefe(int id) {
        return IDS.contains(id);
    }

    public static boolean recebeBencaoDoCaramelo(int id, Protagonista p) {
        return ehChefe(id) && p.temFlag("amigo_do_caramelo");
    }
}