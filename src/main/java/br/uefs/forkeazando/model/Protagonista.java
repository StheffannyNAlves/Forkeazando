package br.uefs.forkeazando.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
// Os metodos comentados ainda serão utilizados na fase 2 ou 3
public class Protagonista {
    private CaracteristicasProtagonista caracteristicas;
    private int score = 0;
    private int participacao = 0;
    private int vidaSocial = 0;
    private int confianca = 0;
    private int energia = 0;
    private int energiaMaxima;
    private List<String> historicoFlags = new ArrayList<>();



    public Protagonista(CaracteristicasProtagonista caracteristicas) {
        this.caracteristicas = caracteristicas;
        this.vidaSocial = caracteristicas.getSocialInicial();
        this.confianca = caracteristicas.getConfiancaInicial();
        this.energiaMaxima = caracteristicas.getEnergiaMaximaInicial();
        this.energia = energiaMaxima;


    }

    public enum NivelRelacionamento { ALIADO, COLEGA, RIVAL }

    public static NivelRelacionamento categorizarRelacionamento(float valor) {
        if (valor >= 3) return NivelRelacionamento.ALIADO;
        if (valor <= -3) return NivelRelacionamento.RIVAL;
        return NivelRelacionamento.COLEGA;
    }

    private Map<PersonagemSecundario, Float> relacionamentos = new HashMap<>();

    public void alterarRelacionamento(PersonagemSecundario personagem, float delta) {
        float atual = relacionamentos.getOrDefault(personagem, 0f); // aqui evita que o compilador retorne uma exceção braba(NullPointerException)
        relacionamentos.put(personagem, atual + delta); // garante o acumulo da afinidadde
    }

    public float getRelacionamento(PersonagemSecundario personagem) {
        return relacionamentos.getOrDefault(personagem, 0f);
    }

    public void ganharScore(int valor)
    {
        score += valor;
    }

    public void ganharParticipacao(int valor){

        participacao += valor; // isso aqui poderia entrar no score
    }


    // public void ganharConfianca(int valor)    { confianca += valor; } // Só vai ser utilizado na fase 2

    public void gastarEnergia(int valor) {
        energia -= valor;
    }

    // public void alterarVidaSocial(int valor)  { vidaSocial += valor; } // Só vai ser utilizada na fase 2

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

    // public int getParticipacao()  { return participacao; } // Getter de suporte, mas ainda não utilizado

    public int getConfianca()  { return confianca; }

    public int getVidaSocial() { return vidaSocial; }

    public int getEnergiaMaxima() {
        return energiaMaxima;
    }

    public int getEnergia() {
        return energia;
    }

    public List<String> getHistoricoFlags() { return Collections.unmodifiableList(historicoFlags); }
    // public CaracteristicasProtagonista getCaracteristicas() { return caracteristicas; } // Getter de suporte, mas ainda não utilizado

}

