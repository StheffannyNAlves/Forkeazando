package br.uefs.forkeazando.controller;

public enum DecisaoSaida {
    SAIR,
    PERGUNTAR_SALVAR,
    ESCOLHER_SLOT;

    public static DecisaoSaida decidir(
            boolean temAlteracoes,
            boolean temSlotLivre) {

        if (!temAlteracoes) {
            return SAIR;
        }

        if (temSlotLivre) {
            return PERGUNTAR_SALVAR;
        }

        return ESCOLHER_SLOT;
    }
}