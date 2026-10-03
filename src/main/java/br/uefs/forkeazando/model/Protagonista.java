package br.uefs.forkeazando.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Protagonista implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum SituacaoEconomica { APERTADA, ESTAVEL, CONFORTAVEL, ELITE }
    public enum ExperienciaEmocionalEnsinoMedio { NEGATIVA, NEUTRA, POSITIVA }
    public enum NivelVidaSocial { ISOLADO, EQUILIBRADO, AMIGUEIRO }
    public enum NivelRelacionamento { ALIADO, COLEGA, RIVAL }

    private final String nome;
    private final boolean perfeccionista;
    private final boolean sociavel;
    private final boolean estudoTeorico;
    private final SituacaoEconomica situacaoEconomica;
    private final ExperienciaEmocionalEnsinoMedio experienciaEmocional;
    private final NivelVidaSocial nivelVidaSocial;

    private int score = 0;
    private int participacao = 0;
    private int vidaSocial;
    private int confianca;
    private int carisma;
    private int logica;
    private int sanidade;
    private int energia;
    private final int energiaMaxima;

    private final List<String> historicoFlags = new ArrayList<>();
    private final Map<PersonagemSecundario, Float> relacionamentos = new HashMap<>();

    public Protagonista(String nome, boolean perfeccionista, boolean sociavel,
                        boolean estudoTeorico, NivelVidaSocial nivelVidaSocial,
                        ExperienciaEmocionalEnsinoMedio experienciaEmocional,
                        SituacaoEconomica situacaoEconomica) {
        this.nome = (nome == null || nome.isBlank()) ? "Calouro" : nome.trim();
        this.perfeccionista = perfeccionista;
        this.sociavel = sociavel;
        this.estudoTeorico = estudoTeorico;
        this.nivelVidaSocial = nivelVidaSocial;
        this.experienciaEmocional = experienciaEmocional;
        this.situacaoEconomica = situacaoEconomica;

        this.confianca = calcularConfiancaInicial();
        this.vidaSocial = calcularSocialInicial();
        this.carisma = sociavel ? 5 : 3;
        this.logica = estudoTeorico ? 7 : 3;
        this.sanidade = calcularSanidadeInicial();
        this.energiaMaxima = calcularEnergiaMaximaInicial();
        this.energia = energiaMaxima;
        gerarFlagsDeOrigem();
    }

    private int limitar(int valor, int minimo, int maximo){
        return Math.clamp(valor, minimo, maximo);
    }
    private int calcularConfiancaInicial() {
        return switch (experienciaEmocional) {
            case NEGATIVA -> 1; case NEUTRA -> 3; case POSITIVA -> 5; };
    }
    private int calcularSocialInicial() {
        return switch (nivelVidaSocial) {
            case ISOLADO -> 1; case EQUILIBRADO -> 3; case AMIGUEIRO -> 5; };
    }
    private int calcularSanidadeInicial() {
        return switch (experienciaEmocional) {
            case NEGATIVA -> 3; case NEUTRA -> 5; case POSITIVA -> 7; };
    }
    private int calcularEnergiaMaximaInicial() {
        int base = switch (situacaoEconomica) {
            case ELITE -> 90; case CONFORTAVEL -> 75; case ESTAVEL -> 60; case APERTADA -> 45; };
        int ajuste = switch (experienciaEmocional) {
            case POSITIVA -> 10; case NEUTRA -> 0; case NEGATIVA -> -10; };
        return base + ajuste;
    }

    private void gerarFlagsDeOrigem() {
        adicionarFlag(perfeccionista ? "perfil_perfeccionista" : "perfil_relaxado");
        adicionarFlag(sociavel ? "perfil_sociavel" : "perfil_reservado");
        adicionarFlag(estudoTeorico ? "perfil_teorico" : "perfil_pratico");
        switch (situacaoEconomica) {
            case APERTADA -> adicionarFlag("origem_apertada");
            case ESTAVEL -> adicionarFlag("origem_estavel");
            case CONFORTAVEL -> adicionarFlag("origem_confortavel");
            case ELITE -> adicionarFlag("origem_elite");
        }
        switch (experienciaEmocional) {
            case NEGATIVA -> adicionarFlag("origem_em_negativa");
            case NEUTRA -> adicionarFlag("origem_em_neutra");
            case POSITIVA -> adicionarFlag("origem_em_positiva");
        }
        switch (nivelVidaSocial) {
            case ISOLADO -> adicionarFlag("origem_isolado");
            case EQUILIBRADO -> adicionarFlag("origem_equilibrado");
            case AMIGUEIRO -> adicionarFlag("origem_amigueiro");
        }
    }

    public static NivelRelacionamento categorizarRelacionamento(float valor) {
        if (valor >= 3) return NivelRelacionamento.ALIADO;
        if (valor <= -3) return NivelRelacionamento.RIVAL;
        return NivelRelacionamento.COLEGA;
    }

    public void alterarRelacionamento(PersonagemSecundario personagem, float delta) {
        float atual = relacionamentos.getOrDefault(personagem, 0f);
        relacionamentos.put(personagem, atual + delta);
    }
    public float getRelacionamento(PersonagemSecundario personagem) {
        return relacionamentos.getOrDefault(personagem, 0f);
    }
    public Map<PersonagemSecundario, Float> getRelacionamentos() {
        return Collections.unmodifiableMap(relacionamentos); // usar pra serialzação
    }



    public void ganharScore(int valor) {
        score += valor;
        limitarAtributos(); }
    public void ganharParticipacao(int valor) {
        participacao += valor;
        limitarAtributos(); }
    public void gastarEnergia(int valor) {
        energia = Math.clamp(energia - valor, 0, energiaMaxima);
    }

    public void limitarAtributos(){
        confianca = limitar(confianca, 0, 10);
        vidaSocial = limitar(vidaSocial, 0, 10);
        carisma = limitar(carisma, 0, 10);
        logica = limitar(logica, 0, 10);
        sanidade = limitar(sanidade, 0, 10);

        energia = limitar(energia, 0, energiaMaxima);

        score = limitar(score, 0, 100);
        participacao = limitar(participacao, 0, 100);
    }

    public void alterarConfianca(int valor)  {
        confianca += valor;
        limitarAtributos();
    }

    public void alterarVidaSocial(int valor) {
        vidaSocial += valor;
        limitarAtributos();
    }
    public void alterarCarisma(int valor) {
        carisma = Math.max(0, carisma + valor);
        limitarAtributos();
    }
    public void alterarLogica(int valor)     {
        logica = Math.max(0, logica + valor);
        limitarAtributos();
    }
    public void alterarSanidade(int valor)   {
        sanidade = Math.max(0, sanidade + valor);
        limitarAtributos();
    }

    public void alterarAtributo(String chave, int delta) {
        switch (chave) {
            case "confianca"    -> alterarConfianca(delta);
            case "vidaSocial"   -> alterarVidaSocial(delta);
            case "carisma"      -> alterarCarisma(delta);
            case "logica"       -> alterarLogica(delta);
            case "sanidade"     -> alterarSanidade(delta);
            case "score"        -> ganharScore(delta);
            case "participação" -> ganharParticipacao(delta);
            case "energia"      -> gastarEnergia(-delta);
            default -> throw new IllegalArgumentException("Atributo desconhecido: " + chave);
        }
    }

    public void adicionarFlag(String flag) {
        if (!historicoFlags.contains(flag)) historicoFlags.add(flag);
    }
    public boolean temFlag(String flag) { return historicoFlags.contains(flag); }
    public boolean atingiuRequisito(int scoreMin, int participacaoMin) {
        return score >= scoreMin && participacao >= participacaoMin;
    }

    public String getRank() {
        if (score < 10) return "Calouro Perdido";
        if (score < 25) return "Estagiário do Caos";
        if (score < 50) return "Sobrevivente do 1º Semestre";
        if (score < 75) return "Veterano Casca Grossa";
        return "Lenda do Campus";
    }

    public String getNome()                          { return nome; }
//    public boolean isPerfeccionista()                { return perfeccionista; }
//    public boolean isSociavel()                      { return sociavel; }
//    public boolean isEstudoTeorico()                 { return estudoTeorico; }
//    public SituacaoEconomica getSituacaoEconomica()  { return situacaoEconomica; }
//    public ExperienciaEmocionalEnsinoMedio getExperienciaEmocional() { return experienciaEmocional; }
//    public NivelVidaSocial getNivelVidaSocial()      { return nivelVidaSocial; }

    public int getScore()         { return score; }
    public int getParticipacao()  { return participacao; }
    public int getConfianca()     { return confianca; }
    public int getVidaSocial()    { return vidaSocial; }
    public int getCarisma()       { return carisma; }
    public int getLogica()        { return logica; }
    public int getSanidade()      { return sanidade; }
    public int getEnergia()       { return energia; }
    public int getEnergiaMaxima() { return energiaMaxima; }

    public List<String> getHistoricoFlags() {
        return Collections.unmodifiableList(historicoFlags);
    }
}