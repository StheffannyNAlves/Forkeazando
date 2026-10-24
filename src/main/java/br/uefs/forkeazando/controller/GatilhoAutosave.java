package br.uefs.forkeazando.controller;

// Decide quando o autosave por progressão deve ocorrer
public class GatilhoAutosave {
    private final int cenasPorAutosave;
    private int cenasConcluidas;

    public GatilhoAutosave(int cenasPorAutosave) {
        if (cenasPorAutosave < 1) {
            throw new IllegalArgumentException("cenasPorAutosave deve ser pelo menos 1");
        }
        this.cenasPorAutosave = cenasPorAutosave;
    }

    public void registrarCenaConcluida() {
        cenasConcluidas++;
    }

    // Devolve true uma vez a cada N cenas concluídas e reinicia a contagem
    public boolean deveSalvar() {
        if (cenasConcluidas < cenasPorAutosave) {
            return false;
        }
        cenasConcluidas = 0;
        return true;
    }
}