package br.uefs.forkeazando.view;

import java.util.Scanner;
import br.uefs.forkeazando.view.Cores;

public class MenuPrincipal {

    private final Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        int largura = 50;
        int terminal = 80;
        int larguraCaixa = largura + 2; // bordas
        int offset = (terminal - larguraCaixa) / 2;
        String prefixo = " ".repeat(offset);

        System.out.println();
        System.out.println(prefixo + Cores.CIANO + "╔" + "═".repeat(largura) + "╗" + Cores.RESET);
        System.out.println(prefixo + linha("FORKEAZANDO", largura));
        System.out.println(prefixo + linha("\"Você sabe jogar o jogo?\"", largura));
        System.out.println(prefixo + Cores.CIANO + "╠" + "═".repeat(largura) + "╣" + Cores.RESET);

        System.out.println(prefixo + linha("[1] Nova Partida", largura));
        System.out.println(prefixo + linha("[2] Instruções", largura));
        System.out.println(prefixo + linha("[3] Créditos", largura));
        System.out.println(prefixo + linha("[0] Sair", largura));

        System.out.println(prefixo + Cores.CIANO + "╚" + "═".repeat(largura) + "╝" + Cores.RESET);

        System.out.print(prefixo + Cores.AMARELO + "Escolha > " + Cores.RESET);
    }


    public String lerOpcoes() {
        return scanner.nextLine().trim();
    }

    private String linha(String texto, int largura) {
        int espacoTotal = Math.max(0, largura - texto.length());
        int esquerda = espacoTotal / 2;
        int direita = espacoTotal - esquerda;

        return Cores.CIANO + "║"
                + Cores.RESET
                + " ".repeat(esquerda)
                + texto
                + " ".repeat(direita)
                + Cores.CIANO + "║"
                + Cores.RESET;
    }

    public void mostrarInstrucoes(){
        System.out.println(Cores.CIANO + "[Instruções...]" + Cores.RESET);
    }

    public void mostrarCreditos() {
        System.out.println(Cores.CIANO + "[Créditos: Stheffanny e Davi]" + Cores.RESET);
    }

    public void mostrarMensagem(String msg) {
        System.out.println(msg);
    }

}