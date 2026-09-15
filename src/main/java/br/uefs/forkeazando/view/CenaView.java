package br.uefs.forkeazando.view;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;

import java.util.List;

public class CenaView {
    public void renderizar(Cena cena) {
        int largura = 60;

        System.out.println();
        System.out.println(Cores.CIANO + "╔" + "═".repeat(largura) + "╗" + Cores.RESET);
        System.out.println(Cores.CIANO + "║" + Cores.RESET
                + "  " + cena.getCapitulo().toUpperCase()
                + " ".repeat(Math.max(0, largura - cena.getCapitulo().length() - 2))
                + Cores.CIANO + "║" + Cores.RESET);
        System.out.println(Cores.CIANO + "╠" + "═".repeat(largura) + "╣" + Cores.RESET);

        if (!cena.getPersonagemFalando().equalsIgnoreCase("Narrador")) {
            String personagem = cena.getPersonagemFalando().toUpperCase();

            System.out.println(Cores.CIANO + "║" + Cores.RESET
                    + "  " + personagem
                    + " ".repeat(Math.max(0, largura - personagem.length() - 2))
                    + Cores.CIANO + "║" + Cores.RESET);

            System.out.println(Cores.CIANO + "║" + Cores.RESET
                    + " ".repeat(largura)
                    + Cores.CIANO + "║" + Cores.RESET);
        }

        String[] linhas = cena.getTextoNarrativo().split("\n");

        for (String texto : linhas) {
            System.out.println(Cores.CIANO + "║" + Cores.RESET
                    + "  " + texto
                    + " ".repeat(Math.max(0, largura - texto.length() - 2))
                    + Cores.CIANO + "║" + Cores.RESET);
        }

        System.out.println(Cores.CIANO + "╚" + "═".repeat(largura) + "╝" + Cores.RESET);
    }

    public void exibirEscolhas(List<Escolha> disponiveis) {
        int largura = 60;

        System.out.println();
        System.out.println(Cores.CIANO + "╔" + "═".repeat(largura) + "╗" + Cores.RESET);
        System.out.println(Cores.CIANO + "║" + Cores.RESET
                + "  O QUE VOCÊ VAI FAZER?"
                + " ".repeat(37)
                + Cores.CIANO + "║" + Cores.RESET);
        System.out.println(Cores.CIANO + "╠" + "═".repeat(largura) + "╣" + Cores.RESET);

        for (int i = 0; i < disponiveis.size(); i++) {
            String escolha = "[" + (i + 1) + "] " + disponiveis.get(i).getTextoAlternativa();

            System.out.println(Cores.CIANO + "║" + Cores.RESET
                    + "  " + escolha
                    + " ".repeat(Math.max(0, largura - escolha.length() - 2))
                    + Cores.CIANO + "║" + Cores.RESET);
        }

        System.out.println(Cores.CIANO + "╚" + "═".repeat(largura) + "╝" + Cores.RESET);
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println();
        System.out.println(Cores.AMARELO + mensagem + Cores.RESET);
    }
}