package br.uefs.forkeazando.model;

import java.util.ArrayList;
import java.util.List;

public class Estado {
    private Protagonista protagonista;
    private List<PersonagemSecundario> personagensSecundarios = new ArrayList<>();
    private int capituloAtual = 1;
    private int score = 0;
    private int participacao = 0;

    public void iniciar(Protagonista protagonista) {
        this.protagonista = protagonista;
    }

    public void avancarCapitulo() {
        capituloAtual++;
    }

    public void ganharScore(int valor) {
        score += valor;
    }

    public void ganharParticipacao(int valor) {
        participacao += valor;
    }

    public boolean atingiuRequisitoBifurcacao(int scoreMinimo, int participacaoMinima) {
        return score >= scoreMinimo && participacao >= participacaoMinima;
    }

    public Protagonista getProtagonista() { return protagonista; }
    public int getCapituloAtual() { return capituloAtual; }
    public int getScore() { return score; }
    public int getParticipacao() { return participacao; }
}