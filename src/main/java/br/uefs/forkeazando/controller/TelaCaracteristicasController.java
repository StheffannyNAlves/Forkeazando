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
        // ADICIONAR AS NOVAS CARACTERISTICAS

        return new CaracteristicasProtagonista(perfeccionista, sociavel, estudoTeorico);
    }
}