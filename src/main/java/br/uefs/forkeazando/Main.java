package br.uefs.forkeazando;

import br.uefs.forkeazando.controller.MenuPrincipalController;
import br.uefs.forkeazando.view.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        MenuPrincipal view = new MenuPrincipal();
        MenuPrincipalController controller = new MenuPrincipalController(view);
        controller.iniciar();
    }
}