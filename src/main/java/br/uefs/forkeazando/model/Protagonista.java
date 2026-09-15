package br.uefs.forkeazando.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Protagonista {
    private CaracteristicasProtagonista caracteristicas;
    private int score = 0;
    private int participacao = 0;
    private int vidaSocial = 0;
    private int confianca = 0;
    private List<String> historicoFlags = new ArrayList<>();


    public Protagonista(CaracteristicasProtagonista caracteristicas) {
        this.caracteristicas = caracteristicas;
        this.vidaSocial = caracteristicas.getNivelSocialInicial();
    }

    public void ganharScore(int valor)
    {
        score += valor;
    }

    public void ganharParticipacao(int valor){
        participacao += valor; // isso aqui poderia entrar no score
    }

    public void ganharConfianca(int valor)    { confianca += valor; }
    public void alterarVidaSocial(int valor)  { vidaSocial += valor; }

    public void adicionarFlag(String flag) {
        if (!historicoFlags.contains(flag)) historicoFlags.add(flag);
    }

    public boolean temFlag(String flag) {
        return historicoFlags.contains(flag);
    }

    public boolean atingiuRequisito(int scoreMin, int participacaoMin) {
        return score >= scoreMin && participacao >= participacaoMin;
    }

    public int getScore() { return score; }
    public int getParticipacao()  { return participacao; }
    public int getConfianca()  { return confianca; }
    public int getVidaSocial() { return vidaSocial; }
    public List<String> getHistoricoFlags() { return Collections.unmodifiableList(historicoFlags); }
    public CaracteristicasProtagonista getCaracteristicas() { return caracteristicas; }
}

