package br.uefs.forkeazando.controller;

public enum DecisaoSaida {
    SAIR,
    PERGUNTAR_SALVAR,
    ESCOLHER_SLOT;

    public static DecisaoSaida decidir(boolean temAlteracoes, boolean temSlotLivre) {
        return decidir(temAlteracoes, temSlotLivre, true);
    }

    /** Com confirmarSaida desligado, sai direto sem perguntar sobre salvar. */
    public static DecisaoSaida decidir(boolean temAlteracoes, boolean temSlotLivre, boolean confirmarSaida) {
        if (!temAlteracoes || !confirmarSaida) {
            return SAIR;
        }

        if (temSlotLivre) {
            return PERGUNTAR_SALVAR;
        }

        return ESCOLHER_SLOT;
    }
}