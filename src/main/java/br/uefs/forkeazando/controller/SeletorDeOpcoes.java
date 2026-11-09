package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.view.widget.MenuInterativo;

import java.util.List;

/**
 * Como o controller pede ao jogador que escolha uma opção. No jogo real é o menu
 * interativo do terminal; nos testes, uma versão com respostas prontas.
 */
@FunctionalInterface
public interface SeletorDeOpcoes {

    MenuInterativo.Resposta escolher(List<MenuInterativo.Opcao> opcoes, String titulo);

    /** Seletor usado no jogo: abre o menu interativo no terminal. */
    static SeletorDeOpcoes terminal() {
        return MenuInterativo::abrir;
    }
}
