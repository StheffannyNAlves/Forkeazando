package br.uefs.forkeazando.controller;
import br.uefs.forkeazando.model.CaracteristicasProtagonista;
import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.roteiro.Capitulo1;
import br.uefs.forkeazando.view.CenaView;
import br.uefs.forkeazando.view.MenuPrincipal;
import br.uefs.forkeazando.view.TelaCaracteristicas;

import java.util.List;

public class MenuPrincipalController {
    private final MenuPrincipal view;
    private boolean rodando = true;
    // private Estado estado;

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
                CaracteristicasProtagonista c = new TelaCaracteristicasController(new TelaCaracteristicas()).iniciar();
                Protagonista protagonista = new Protagonista(c);
                Estado estado = new Estado();
                estado.iniciar(protagonista);
                List<Cena> cenas = Capitulo1.criarCenas();
                new CenasController(estado, cenas, new CenaView()).iniciar();
                System.out.println("Partida iniciada!");
                return "NOVA PARTIDA";
            case "2": view.mostrarInstrucoes(); return "INSTRUCOES";
            case "3": view.mostrarCreditos(); return "CREDITOS";
            case "0": rodando = false; view.mostrarMensagem("Saindo..."); return "SAIR";
            default: view.mostrarMensagem("Opção inválida!"); return "INVALIDO";
        }
    }
}
