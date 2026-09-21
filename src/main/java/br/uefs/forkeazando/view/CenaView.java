package br.uefs.forkeazando.view;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;

import java.util.ArrayList;
import java.util.List;

public class CenaView {

    private static final int LARGURA = 60;

    public void renderizar(Cena cena) {
        String corTexto = corPorFalante(cena.getPersonagemFalando());

        System.out.println();
        System.out.println(Cores.CIANO + "╔" + "═".repeat(LARGURA) + "╗" + Cores.RESET);
        imprimirLinha(cena.getCapitulo().toUpperCase(), Cores.BRANCO);
        System.out.println(Cores.CIANO + "╠" + "═".repeat(LARGURA) + "╣" + Cores.RESET);

        if (!cena.getPersonagemFalando().equalsIgnoreCase("Narrador")) {
            imprimirLinha(cena.getPersonagemFalando().toUpperCase(), corTexto);
            imprimirLinha("", corTexto);
        }

        for (String paragrafo : cena.getTextoNarrativo().split("\n")) {
            if (paragrafo.isBlank()) {
                imprimirLinha("", corTexto);
                continue;
            }
            for (String linhaQuebrada : quebrarLinha(paragrafo, LARGURA - 4)) {
                imprimirLinha(linhaQuebrada, corTexto);
            }
        }

        System.out.println(Cores.CIANO + "╚" + "═".repeat(LARGURA) + "╝" + Cores.RESET);
    }

    private String corPorFalante(String personagemFalando) {
        if (personagemFalando.equalsIgnoreCase("Narrador")) {
            return Cores.BRANCO;
        }
        if (personagemFalando.equalsIgnoreCase("Sistema")) {
            return Cores.AMARELO;
        }
        return Cores.MAGENTA;
    }

    public void exibirEscolhas(List<Escolha> disponiveis) {
        System.out.println();
        System.out.println(Cores.CIANO + "╔" + "═".repeat(LARGURA) + "╗" + Cores.RESET);
        imprimirLinha("O QUE VOCÊ VAI FAZER?", Cores.VERDE);
        System.out.println(Cores.CIANO + "╠" + "═".repeat(LARGURA) + "╣" + Cores.RESET);

        for (int i = 0; i < disponiveis.size(); i++) {
            String escolha = "[" + (i + 1) + "] " + disponiveis.get(i).getTextoAlternativa();
            for (String linhaQuebrada : quebrarLinha(escolha, LARGURA - 4)) {
                imprimirLinha(linhaQuebrada, Cores.VERDE);
            }
        }

        System.out.println(Cores.CIANO + "╚" + "═".repeat(LARGURA) + "╝" + Cores.RESET);
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println();
        System.out.println(Cores.AMARELO + mensagem + Cores.RESET);
    }


    private void imprimirLinha(String texto, String cor) {
        int espacos = Math.max(0, LARGURA - texto.length() - 2);
        System.out.println(Cores.CIANO + "║" + Cores.RESET
                + "  " + cor + texto + Cores.RESET + " ".repeat(espacos)
                + Cores.CIANO + "║" + Cores.RESET);
    }


    private List<String> quebrarLinha(String texto, int largura) {
        List<String> linhas = new ArrayList<>();
        String[] palavras = texto.split(" ");
        StringBuilder atual = new StringBuilder();

        for (String palavra : palavras) {
            if (atual.length() + palavra.length() + 1 > largura) {
                linhas.add(atual.toString());
                atual = new StringBuilder();
            }
            if (!atual.isEmpty()) {
                atual.append(" ");
            }
            atual.append(palavra);
        }
        if (!atual.isEmpty()) {
            linhas.add(atual.toString());
        }
        return linhas;
    }
}