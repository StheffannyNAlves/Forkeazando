package br.uefs.forkeazando.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EscolhaTest {

    @Test
    void escolhaDeveEstarDisponivelQuandoTodosRequisitosSaoCumpridos() {
        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                CaracteristicasProtagonista.SituacaoEconomica.ESTAVEL
        );
        Protagonista protagonista = new Protagonista(caracteristicas);
        protagonista.ganharScore(10);        // valor >= scoreMinimo
        protagonista.ganharParticipacao(10); // valor >= participacaoMinima

        Escolha escolha = new Escolha.Builder("Teste, teste", 1)
                .comScoreMinimo(8)
                .comParticipacaoMinima(8)
                .comCustoEnergia(10)
                .build();

        assertTrue(escolha.estaDisponivel(protagonista));
    }

    @Test
    void participacaoInsuficiente() {
        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                CaracteristicasProtagonista.SituacaoEconomica.ESTAVEL
        );
        Protagonista protagonista = new Protagonista(caracteristicas);
        protagonista.ganharScore(10);
        protagonista.ganharParticipacao(5); // nesse caso, precisa ser menor

        Escolha escolha = new Escolha.Builder("Teste, teste", 1)
                .comScoreMinimo(8)
                .comParticipacaoMinima(8)
                .comCustoEnergia(10)
                .build();

        assertFalse(escolha.estaDisponivel(protagonista));
    }

    @Test
    void scoreInsuficiente() {
        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                CaracteristicasProtagonista.SituacaoEconomica.ESTAVEL
        );
        Protagonista protagonista = new Protagonista(caracteristicas);
        protagonista.ganharScore(5); // nesse caso, precisa ser menor
        protagonista.ganharParticipacao(15);

        Escolha escolha = new Escolha.Builder("Teste, teste", 1)
                .comScoreMinimo(8)
                .comParticipacaoMinima(8)
                .comCustoEnergia(10)
                .build();

        assertFalse(escolha.estaDisponivel(protagonista));
    }

    @Test
    void flagAusente() {
        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                CaracteristicasProtagonista.SituacaoEconomica.ESTAVEL
        );
        Protagonista protagonista = new Protagonista(caracteristicas);
        protagonista.ganharScore(15);
        protagonista.ganharParticipacao(15);

        Escolha escolha = new Escolha.Builder("Teste, teste", 1)
                .comScoreMinimo(8)
                .comParticipacaoMinima(8)
                .comCustoEnergia(10)
                .comFlagRequerida("--- .-.. / -- ..- -. -.. ---")
                .build();

        assertFalse(escolha.estaDisponivel(protagonista));
    }



    @Test
    void energiaInsuficiente() {
        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                CaracteristicasProtagonista.SituacaoEconomica.ESTAVEL
        );
        Protagonista protagonista = new Protagonista(caracteristicas);
        protagonista.ganharScore(15);
        protagonista.ganharParticipacao(15);

        Escolha escolha = new Escolha.Builder("Teste, teste", 1)
                .comScoreMinimo(8)
                .comParticipacaoMinima(8)
                .comCustoEnergia(9999)
                .build();

        assertFalse(escolha.estaDisponivel(protagonista));
    }


}