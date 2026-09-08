package br.uefs.forkeazando.view;

import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        int largura = 30;

        System.out.println(Cores.CIANO + "╔" + "═".repeat(largura) + "╗" + Cores.RESET);
        System.out.println(linha("NOME DO JOGO", largura));
        System.out.println(Cores.CIANO + "╠" + "═".repeat(largura) + "╣" + Cores.RESET);

        System.out.println(linha("1. Nova Partida", largura));
        System.out.println(linha("2. Instruções", largura));
        System.out.println(linha("3. Créditos", largura));
        System.out.println(linha("0. Sair", largura));

        System.out.println(Cores.CIANO + "╚" + "═".repeat(largura) + "╝" + Cores.RESET);

        System.out.print(Cores.AMARELO + "Escolha: " + Cores.RESET);
    }

    public String lerOpcoes() {
        return scanner.nextLine().trim();
    }

    private String linha(String texto, int largura) {
        int espaco = Math.max(0, largura - texto.length());

        return Cores.CIANO + "║"
                + Cores.RESET
                + texto
                + " ".repeat(espaco)
                + Cores.CIANO + "║"
                + Cores.RESET;
    }
}