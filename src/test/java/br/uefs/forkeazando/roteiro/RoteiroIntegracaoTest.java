package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Final;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RoteiroIntegracaoTest {

    private static List<Cena> todasAsCenas() {
        List<Cena> cenas = new ArrayList<>();
        cenas.addAll(Abertura.criarCenas());
        cenas.addAll(Capitulo1.criarCenas());
        cenas.addAll(Capitulo2.criarCenas());
        cenas.addAll(Capitulo3.criarCenas());
        cenas.addAll(Capitulo4.criarCenas());
        cenas.addAll(Capitulo5A.criarCenas());
        cenas.addAll(Capitulo5B.criarCenas());
        cenas.addAll(Capitulo5C.criarCenas());
        cenas.addAll(Capitulo6.criarCenas());
        cenas.addAll(Capitulo7.criarCenas());
        cenas.addAll(Capitulo8.criarCenas());
        return cenas;
    }

    @Test
    void todoFinalTemAsFlagsConcedidasNoRoteiro() {
        Set<String> concedidas = new HashSet<>();
        for (Cena c : todasAsCenas()) {
            for (Escolha e : c.getEscolhas()) {
                concedidas.addAll(e.getFlagsConcedidas());
            }
        }

        List<String> faltando = new ArrayList<>();
        if (!concedidas.contains(Final.FLAG_CONCLUSAO)) {
            faltando.add(Final.FLAG_CONCLUSAO);
        }
        for (Final f : Final.values()) {
            if (!concedidas.contains(f.getTrilha())) {
                faltando.add(f.getTrilha());
            }
        }

        assertTrue(faltando.isEmpty(), "flags nunca concedidas: " + faltando);
    }
}