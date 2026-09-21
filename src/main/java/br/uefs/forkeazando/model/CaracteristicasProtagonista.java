package br.uefs.forkeazando.model;
// parte de protagonista

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


    public int getConfiancaInicial() {
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

    public int getEnergiaMaximaInicial() {
        int base = switch (situacaoEconomica) {
            case ELITE -> 90;
            case CONFORTAVEL -> 75;
            case ESTAVEL -> 60;
            case APERTADA -> 45;
        };

        int ajuste = switch (experienciaEmocionalEnsinoMedio) {
            case POSITIVA -> 10;
            case NEUTRA -> 0;
            case NEGATIVA -> -10;
        };

        return base + ajuste;
    }
    public boolean isPerfeccionista() { return perfeccionista; }
    public boolean isSociavel() { return sociavel; }
    public boolean isEstudoTeorico() { return estudoTeorico; }
    public  SituacaoEconomica getSituacaoEconomica() { return situacaoEconomica; }


}