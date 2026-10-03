package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.persistencia.ConfiguracoesAdmin;
import br.uefs.forkeazando.roteiro.*;
import br.uefs.forkeazando.view.CenaView;
import br.uefs.forkeazando.view.Cores;
import br.uefs.forkeazando.view.Entrada;
import br.uefs.forkeazando.view.MenuPrincipal;
import br.uefs.forkeazando.view.TelaCaracteristicas;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipalController {

    private final MenuPrincipal view;
    private boolean rodando = true;

    public MenuPrincipalController(MenuPrincipal view) {
        this.view = view;
    }

    public void iniciar() {
        while (rodando) {
            view.mostrarMenu();
            String opcao = view.lerOpcoes();
            processarOpcao(opcao);
        }
    }

    private void processarOpcao(String opcao) {
        switch (opcao) {
            case "1" -> novaPartida();
            case "2" -> view.mostrarMensagem("Continuar — em construção.");
            case "3" -> view.mostrarMensagem("Carregar — em construção.");
            case "4" -> view.mostrarInstrucoes();
            case "5" -> abrirConfiguracoes();
            case "6" -> {
                view.mostrarCreditos();
                System.out.print("Pressione ENTER para voltar...");
                Entrada.lerLinha();
            }
            case "0" -> {
                rodando = false;
                view.mostrarMensagem("Saindo...");
            }
            default -> view.mostrarMensagem("Opção inválida!");
        }
    }

    private void abrirConfiguracoes() {
        try {
            var config = ConfiguracoesAdmin.get();
            new TelaConfiguracoesController(config).iniciar();
        } catch (CarregamentoException e) {
            view.mostrarMensagem(Cores.VERMELHO + "Erro ao carregar config: " + e.getMessage() + Cores.RESET);
        } catch (DadosInvalidosException e) {
            view.mostrarMensagem(Cores.VERMELHO + "Config inválida: " + e.getMessage() + Cores.RESET);
        }
    }

    private void novaPartida() {
        Protagonista protagonista =
                new TelaCaracteristicasController(new TelaCaracteristicas()).iniciar();

        Estado estado = new Estado();
        estado.iniciar(protagonista);
        estado.setCenaAtualId(700);

        List<Cena> cenas = new ArrayList<>();
        cenas.addAll(Abertura.criarCenas());
        cenas.addAll(Capitulo1.criarCenas());
        cenas.addAll(Capitulo2.criarCenas());
        cenas.addAll(Capitulo3.criarCenas());
        cenas.addAll(Capitulo4.criarCenas());
        cenas.addAll(Capitulo5A.criarCenas());
        cenas.addAll(Capitulo5B.criarCenas());
        cenas.addAll(Capitulo5C.criarCenas());
        cenas.addAll(Capitulo6.criarCenas());
        cenas.addAll(Capitulo7.criarCenas());
        cenas.addAll(Capitulo8.criarCenas());

        new CenasController(estado, cenas, new CenaView()).iniciar();
    }
}