package br.uefs.forkeazando.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DecisaoSaidaTest {

    @Test
    void semAlteracoesSaiDiretoComSlotLivre() {
        Assertions.assertEquals(
                DecisaoSaida.SAIR,
                DecisaoSaida.decidir(false, true)
        );
    }

    @Test
    void semAlteracoesSaiDiretoSemSlotLivre() {
        assertEquals(
                DecisaoSaida.SAIR,
                DecisaoSaida.decidir(false, false)
        );
    }

    @Test
    void comAlteracoesEComSlotLivrePerguntaSeQuerSalvar() {
        assertEquals(
                DecisaoSaida.PERGUNTAR_SALVAR,
                DecisaoSaida.decidir(true, true)
        );
    }

    @Test
    void comAlteracoesESemSlotLivrePermiteEscolherSlot() {
        assertEquals(
                DecisaoSaida.ESCOLHER_SLOT,
                DecisaoSaida.decidir(true, false)
        );
    }


}