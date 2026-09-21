package br.uefs.forkeazando.model;

public class PersonagemSecundario {

    private final CaracteristicaPersonagemSecundario caracteristicas;

    private PersonagemSecundario(CaracteristicaPersonagemSecundario caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public CaracteristicaPersonagemSecundario getCaracteristicas() {
        return caracteristicas;
    }

    public static final PersonagemSecundario VETERANO = new PersonagemSecundario(
            new CaracteristicaPersonagemSecundario("Veterano", CaracteristicaPersonagemSecundario.Interesse.ESTABILIDADE)
    );

    public static final PersonagemSecundario ORIENTADOR_IC = new PersonagemSecundario(
            new CaracteristicaPersonagemSecundario("Orientador de IC", CaracteristicaPersonagemSecundario.Interesse.RECONHECIMENTO)
    );

    public static final PersonagemSecundario COLEGA_RIVAL = new PersonagemSecundario(
            new CaracteristicaPersonagemSecundario("Colega/Rival", CaracteristicaPersonagemSecundario.Interesse.OPORTUNIDADE)
    );


    public static final PersonagemSecundario DONO_BAR8 = new PersonagemSecundario(
            new CaracteristicaPersonagemSecundario("Dono do Bar", CaracteristicaPersonagemSecundario.Interesse.OPORTUNIDADE) // aJUSTAR
    );

    public static final PersonagemSecundario PROFESSOR_PBL = new PersonagemSecundario(
            new CaracteristicaPersonagemSecundario("Tutor PBL", CaracteristicaPersonagemSecundario.Interesse.RECONHECIMENTO)
    );



}