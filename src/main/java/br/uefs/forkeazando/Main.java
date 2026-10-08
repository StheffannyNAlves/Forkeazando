package br.uefs.forkeazando;

import br.uefs.forkeazando.controller.MenuPrincipalController;
import br.uefs.forkeazando.view.Entrada;
import br.uefs.forkeazando.view.MenuPrincipal;
import org.jline.terminal.Terminal;
// SEM MUDANÇAS

public class Main {
    public static void main(String[] args) {
        if (Terminal.TYPE_DUMB.equals(Entrada.terminal().getType())) {
            System.out.println(
                    "Execute este jogo em um terminal (não no console da IDE).");
            return;
        }
        MenuPrincipal view = new MenuPrincipal();
        MenuPrincipalController controller = new MenuPrincipalController(view);
        controller.iniciar();
    }
}