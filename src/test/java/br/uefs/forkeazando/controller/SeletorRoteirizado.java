package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.view.widget.MenuInterativo.*;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;

/**
 * Seletor falso para testes: responde ao menu na ordem em que as respostas foram
 * previstas, sem terminal. Se o controller pedir mais menus do que o previsto, o
 * teste falha em vez de ficar preso num laço.
 */
class SeletorRoteirizado implements SeletorDeOpcoes {

    private final Deque<Function<List<Opcao>, Resposta>> respostas = new ArrayDeque<>();
    final List<String> titulosVistos = new ArrayList<>();
    final List<List<String>> menusVistos = new ArrayList<>();

    SeletorRoteirizado escolhe(String texto) {
        respostas.add(opcoes -> {
            for (int i = 0; i < opcoes.size(); i++) {
                if (opcoes.get(i).label.equals(texto)) {
                    return new Resposta(Resultado.SELECIONADO, i, null);
                }
            }
            throw new AssertionError("Opção \"" + texto + "\" não existe no menu: " + rotulos(opcoes));
        });
        return this;
    }

    // Escolhe a opção pelo índice
    SeletorRoteirizado indice(int indice) {
        respostas.add(opcoes -> new Resposta(Resultado.SELECIONADO, indice, null));
        return this;
    }

    // Escolhe a última opção do menu
    SeletorRoteirizado ultima() {
        respostas.add(opcoes -> new Resposta(Resultado.SELECIONADO, opcoes.size() - 1, null));
        return this;
    }

    // Simula o jogador apertando Esc
    SeletorRoteirizado cancela() {
        respostas.add(opcoes -> new Resposta(Resultado.CANCELADO, -1, null));
        return this;
    }

    int respostasRestantes() {
        return respostas.size();
    }

    @Override
    public Resposta escolher(List<Opcao> opcoes, String titulo) {
        titulosVistos.add(titulo);
        menusVistos.add(rotulos(opcoes));
        Function<List<Opcao>, Resposta> proxima = respostas.poll();
        if (proxima == null) {
            throw new AssertionError("O controller abriu um menu não previsto no teste: " + titulo);
        }
        return proxima.apply(opcoes);
    }

    private static List<String> rotulos(List<Opcao> opcoes) {
        List<String> rotulos = new ArrayList<>();
        for (Opcao o : opcoes) {
            rotulos.add(o.label);
        }
        return rotulos;
    }
}
