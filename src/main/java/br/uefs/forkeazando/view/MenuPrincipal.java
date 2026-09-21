package br.uefs.forkeazando.view;

import java.util.Scanner;

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

    public void mostrarInstrucoes() {
        System.out.println(Cores.CIANO + "\n========================================================");
        System.out.println("                 GUIA DE SOBREVIVÊNCIA");
        System.out.println("========================================================" + Cores.RESET);

        System.out.println("\nBem-vinda a Engenharia de Computação. Para não reprovar logo no primeiro semestre, preste atenção nestas regras:\n");

        System.out.println(Cores.MAGENTA + "[ ESCOLHAS E CONSEQUÊNCIAS ]" + Cores.RESET);
        System.out.println("Leia atentamente cada cena e digite o número da sua escolha.");
        System.out.println("Cada decisão molda a sua " + Cores.AMARELO + "Personalidade (Flags)" + Cores.RESET + ". Suas respostas desbloqueiam opções ocultas no futuro ou bloqueiam caminhos.\n");

        System.out.println(Cores.VERDE + "[ GERENCIAMENTO DE RECURSOS ]" + Cores.RESET);
        System.out.println("Sua vida universitária é movida por três pilares:");

        System.out.println(Cores.VERMELHO + "Energia:" + Cores.RESET + " Sua moeda mais valiosa. Assumir muita responsabilidade drena energia rapidamente.");
        System.out.println(Cores.AMARELO + "Score:" + Cores.RESET + " Seu desempenho técnico. Necessário para provar que você sabe o que está fazendo.");
        System.out.println(Cores.AZUL+ "Participação:" + Cores.RESET + " Sua voz ativa nas discussões de PBL. Ficar calada poupa energia, mas bloqueia o acesso a laboratórios.\n");

        System.out.println(Cores.MAGENTA + "[ RELACIONAMENTOS ]" + Cores.RESET);
        System.out.println("Os veteranos, professores e colegas lembram de você. O mercado é feito de networking (e o Módulo 8 também).\n");

        System.out.println(Cores.AMARELO + "Dica:" + Cores.RESET + " O seu background (Situação Econômica e Ensino Médio) define sua Energia Inicial. O sistema não é justo. Adapte-se.\n");

        System.out.println(Cores.CIANO + "Pressione [ENTER] para voltar ao menu principal..." + Cores.RESET);

        try {
            System.in.read();
        } catch (Exception e) {}
    }
    public void mostrarCreditos() {
        System.out.println(Cores.CIANO + "[Créditos: Stheffanny e Davi]" + Cores.RESET);
    }

    public void mostrarMensagem(String msg) {
        System.out.println(msg);
    }

}