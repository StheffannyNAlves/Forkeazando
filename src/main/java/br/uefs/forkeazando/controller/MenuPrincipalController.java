package br.uefs.forkeazando.controller;
import br.uefs.forkeazando.model.Caracteristicas;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.view.MenuPrincipal;
import br.uefs.forkeazando.view.TelaCaracteristicas;

// Refere-se ao MenuPrincipal
public class MenuPrincipalController {
    private final MenuPrincipal view;
    private boolean rodando = true;

    public MenuPrincipalController(MenuPrincipal view) {
        this.view = view;
    }

    public void iniciar() {
        while (rodando){
            view.mostrarMenu();
            String opcao = view.lerOpcoes();
            processarOpcao(opcao);
        }
    }

    private String processarOpcao(String opcao) {
        switch (opcao){
            case "1":
                Caracteristicas c = new TelaCaracteristicasController(new TelaCaracteristicas()).iniciar();
                Protagonista protagonista = new Protagonista(c);
                Estado estado = new Estado();
                estado.iniciar(protagonista);
                System.out.println("Partida iniciada!");
                return "NOVA_PARTIDA";
            case "2": System.out.println("[Instruções....]"); return "INSTRUCOES";
            case "3": System.out.println("[Créditos: Stheffanny e Davi]"); return "CREDITOS";
            case "0": rodando = false; System.out.println("Saindo..."); return "SAIR";
            default: System.out.println("Opção inválida!"); return "INVALIDO";
        }
    }
}
