package br.uefs.forkeazando.model;
// Aqui é só para o protagonista, as caracteristicas dos personagens secundarios serão definidas em outra class
public class CaracteristicasProtagonista {



    public enum SituacaoEconomica{ APERTADA, CONFORTAVEL, ESTAVEL, ELITE }
    public enum ExperienciaEmocionalEnsinoMedio { NEGATIVA, NEUTRA, POSITIVA }
    public enum NivelVidaSocial {ISOLADO, EQUILIBRADO, AMIGUEIRO}

    private final boolean perfeccionista;
    private final boolean sociavel;
    private final boolean estudoTeorico;
    private final SituacaoEconomica situacaoEconomica;
    private final ExperienciaEmocionalEnsinoMedio experienciaEmocionalEnsinoMedio;
    private final NivelVidaSocial nivelVidaSocial;


    public CaracteristicasProtagonista(boolean perfeccionista, boolean sociavel, boolean estudoTeorico, NivelVidaSocial nivelVidaSocial, ExperienciaEmocionalEnsinoMedio experienciaEmocionalEnsinoMedio,
                                       SituacaoEconomica situacaoEconomica) {
        this.perfeccionista = perfeccionista;
        this.sociavel = sociavel;
        this.estudoTeorico = estudoTeorico;
        this.nivelVidaSocial = nivelVidaSocial;
        this.situacaoEconomica = situacaoEconomica;
        this.experienciaEmocionalEnsinoMedio = experienciaEmocionalEnsinoMedio;

    }


    public int getNivelSocialInicial() {
        return switch (experienciaEmocionalEnsinoMedio){
            case NEGATIVA -> 1;
            case NEUTRA -> 3;
            case POSITIVA -> 5;
        };
    }

    public int getSocialInicial(){
        return switch (nivelVidaSocial){
            case ISOLADO -> 1;
            case EQUILIBRADO -> 3;
            case AMIGUEIRO -> 5;
        };
    }

    public boolean isPerfeccionista() { return perfeccionista; }
    public boolean isSociavel() { return sociavel; }
    public boolean isEstudoTeorico() { return estudoTeorico; }
    public  SituacaoEconomica getSituacaoEconomica() { return situacaoEconomica; }


}