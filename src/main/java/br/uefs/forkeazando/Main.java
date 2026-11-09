package br.uefs.forkeazando;

import br.uefs.forkeazando.controller.MenuPrincipalController;
import br.uefs.forkeazando.dao.ConfiguracoesAdmin;
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.view.Entrada;
import br.uefs.forkeazando.view.MenuPrincipal;
import org.jline.terminal.Terminal;
import br.uefs.forkeazando.model.Configuracoes;

public class Main {
    public static void main(String[] args) throws CarregamentoException, DadosInvalidosException {
        if (Terminal.TYPE_DUMB.equals(Entrada.terminal().getType())) {
            System.out.println("Execute este jogo em um terminal (não no console da IDE).");
            return;
        }
        Configuracoes config;
        try {
            config = ConfiguracoesAdmin.get();
        } catch (CarregamentoException | DadosInvalidosException e) {
            System.out.println("Configurações inválidas (" + e.getMessage() + "). Usando valores padrão.");
            System.out.print("Pressione ENTER para continuar...");
            Entrada.lerLinha();
            config = ConfiguracoesAdmin.restaurarPadrao();
        }
        MenuPrincipal view = new MenuPrincipal(config.isDicasAtalho());
        MenuPrincipalController controller = new MenuPrincipalController(view);
        controller.iniciar();
    }
}