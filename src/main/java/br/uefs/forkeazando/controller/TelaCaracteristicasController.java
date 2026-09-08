// controller/TelaCaracteristicasController.java
package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.model.Caracteristicas;
import br.uefs.forkeazando.view.TelaCaracteristicas;

public class TelaCaracteristicasController {
    private final TelaCaracteristicas view;

    public TelaCaracteristicasController(TelaCaracteristicas view) {
        this.view = view;
    }

    public Caracteristicas iniciar() {
        boolean perfeccionista = view.perguntarPersonalidade();
        boolean sociavel = view.perguntarSociavel();
        boolean estudoTeorico = view.perguntarTipoDeEstudo();
        return new Caracteristicas(perfeccionista, sociavel, estudoTeorico);
    }
}