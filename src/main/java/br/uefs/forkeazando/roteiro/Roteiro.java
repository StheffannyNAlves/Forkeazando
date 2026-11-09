package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import java.util.List;

public final class Roteiro {
    private Roteiro() {}

    public static List<Cena> cenasDosCapitulos() {
        return CarregadorRoteiroJson.carregar(false);
    }

    public static List<Cena> todasAsCenas() {
        return CarregadorRoteiroJson.carregar(true);
    }
}