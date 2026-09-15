package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.model.CaracteristicasProtagonista;
import br.uefs.forkeazando.view.TelaCaracteristicas;

public class TelaCaracteristicasController {
    private final TelaCaracteristicas view;

    public TelaCaracteristicasController(TelaCaracteristicas view) {
        this.view = view;
    }

    public CaracteristicasProtagonista iniciar() {
        boolean perfeccionista = view.perguntarPersonalidade();
        boolean sociavel = view.perguntarSociavel();
        boolean estudoTeorico = view.perguntarTipoDeEstudo();
        CaracteristicasProtagonista.SituacaoEconomica situacaoEconomica = view.perguntarSituacaoEconomica();
        CaracteristicasProtagonista.NivelVidaSocial nivelVidaSocial = view.perguntarVidaSocial();
        CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio experienciaEmocional = view.perguntarExperienciaEM();

        return new CaracteristicasProtagonista(perfeccionista, sociavel, estudoTeorico,
                nivelVidaSocial, experienciaEmocional, situacaoEconomica);
    }
}