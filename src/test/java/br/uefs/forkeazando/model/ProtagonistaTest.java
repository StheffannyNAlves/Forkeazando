package br.uefs.forkeazando.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProtagonistaTest {

    @Test
    void energiaMaximaDeveSer100ParaEliteEPositiva() {
        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.POSITIVA,
                CaracteristicasProtagonista.SituacaoEconomica.ELITE
        );

        Protagonista protagonista = new Protagonista(caracteristicas);

        assertEquals(100, protagonista.getEnergiaMaxima());
    }

    @Test
    void energiaMaximaDeveSer35ParaApertadaENegativa(){
        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEGATIVA,
                CaracteristicasProtagonista.SituacaoEconomica.APERTADA
        );

        Protagonista protagonista = new Protagonista(caracteristicas);

        assertEquals(35, protagonista.getEnergiaMaxima());
    }

    @Test
    void confiancaDeveSer3ParaExperienciaNeutra() {
        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                CaracteristicasProtagonista.SituacaoEconomica.ESTAVEL
        );

        Protagonista protagonista = new Protagonista(caracteristicas);

        assertEquals(3, protagonista.getConfianca());
    }

    @Test
    void nivelSocialDeveSer3ParaEquilibrado() {
        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                CaracteristicasProtagonista.SituacaoEconomica.ESTAVEL
        );

        Protagonista protagonista = new Protagonista(caracteristicas);

        assertEquals(3, protagonista.getVidaSocial());
    }

    @Test
    void ganhaScore() {
        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                CaracteristicasProtagonista.SituacaoEconomica.ESTAVEL
        );

        Protagonista protagonista = new Protagonista(caracteristicas);

        assertEquals(0, protagonista.getScore());

        protagonista.ganharScore(5);

        assertEquals(5, protagonista.getScore());
    }




    @Test
    void gastarEnergia() { // prova que a energia pode ficar negativa
        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEGATIVA,
                CaracteristicasProtagonista.SituacaoEconomica.APERTADA
        );
        Protagonista protagonista = new Protagonista(caracteristicas);

        assertEquals(35, protagonista.getEnergia());
        protagonista.gastarEnergia(50);
        assertEquals(-15, protagonista.getEnergia());
    }
    @Test
    void adicionarFlag() {
        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEGATIVA,
                CaracteristicasProtagonista.SituacaoEconomica.APERTADA
        );
        Protagonista protagonista = new Protagonista(caracteristicas);
        assertEquals(0, protagonista.getHistoricoFlags().size());

        protagonista.adicionarFlag("teste1");
        protagonista.adicionarFlag("teste1");


        assertEquals(1, protagonista.getHistoricoFlags().size());
    }




}