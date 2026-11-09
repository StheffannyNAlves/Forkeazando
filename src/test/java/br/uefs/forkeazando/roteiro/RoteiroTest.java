package br.uefs.forkeazando.roteiro;

import static org.junit.jupiter.api.Assertions.*;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Estado;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class RoteiroTest {

    @Test
    void todasAsCenasNaoSaoVazias() {
        assertFalse(Roteiro.todasAsCenas().isEmpty());
    }

    @Test
    void cenasDosCapitulosNaoTrazemApagao() {
        Set<Integer> ids = new HashSet<>();
        for (Cena c : Roteiro.cenasDosCapitulos()) ids.add(c.getId());
        assertTrue(ids.contains(700));
        assertTrue(ids.contains(0));
        for (int id = Estado.CENA_APAGAO; id <= Estado.CENA_APAGAO + 2; id++) {
            assertFalse(ids.contains(id), "apagão não deve estar em cenasDosCapitulos: " + id);
        }
    }

    @Test
    void idsDeCenaUnicos() {
        List<Cena> todas = Roteiro.todasAsCenas();
        Set<Integer> vistos = new HashSet<>();
        for (Cena c : todas) {
            assertTrue(vistos.add(c.getId()), "ID duplicado: " + c.getId());
        }
    }
}