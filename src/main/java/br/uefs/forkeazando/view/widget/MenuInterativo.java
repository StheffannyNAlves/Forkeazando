package br.uefs.forkeazando.view.widget;

import br.uefs.forkeazando.view.Entrada;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.util.List;

public class MenuInterativo {

    public enum Resultado { SELECIONADO, CANCELADO }


    public static class Opcao {
        public final String label;
        public final boolean editavel;
        public final String[] valores;
        public int indiceValor;

        public Opcao(String label) {
            this.label = label;
            this.editavel = false;
            this.valores = null;
            this.indiceValor = 0;
        }

        public Opcao(String label, String[] valores) {
            this.label = label;
            this.valores = valores;
            this.editavel = (valores != null && valores.length > 0);
            this.indiceValor = 0;
        }

        public String renderizar() {
            if (!editavel) return label;
            return label + ": < " + valores[indiceValor] + " >";
        }
    }


    public static class Resposta {
        public final Resultado resultado;
        public final int indiceOpcao;
        public final int[] valoresEscolhidos;

        public Resposta(Resultado r, int idx, int[] valores) {
            this.resultado = r;
            this.indiceOpcao = idx;
            this.valoresEscolhidos = valores;
        }
    }


    private static int calcularColunas(int total) {
        if (total <= 6)
            return 1;
        return (total + 1) / 2;
    }


    private static void renderizar(Terminal terminal,
                                   List<Opcao> opcoes,
                                   int selecionado,
                                   int colunas,
                                   String titulo) {

        java.io.PrintWriter out = terminal.writer();
        out.print("\033c");
        out.flush();

        if (titulo != null && !titulo.isBlank()) {
            out.println(titulo);
            out.println();
        }

        int total = opcoes.size();
        int linhas = (total + colunas - 1) / colunas;

        int larguraCelula = 0;
        for (Opcao o : opcoes) {
            int tam = o.renderizar().length();
            if (tam > larguraCelula) larguraCelula = tam;
        }
        larguraCelula += 4;

        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                int indice = linha * colunas + coluna;
                if (indice >= total) break;

                Opcao opcao = opcoes.get(indice);
                String texto = opcao.renderizar();
                String padded = String.format("%-" + larguraCelula + "s", texto);

                if (indice == selecionado) {
                    out.print("\033[7m" + padded + "\033[0m");
                } else {
                    out.print(padded);
                }
            }
            out.println();
        }

        out.flush();
    }




    private static int moverCima(int selecionado, int colunas) {
        int linha = selecionado / colunas;
        if (linha == 0) return selecionado;
        return selecionado - colunas;
    }

    private static int moverBaixo(int selecionado, int total, int colunas) {
        int proximo = selecionado + colunas;
        if (proximo >= total) return selecionado;
        return proximo;
    }


    private static void moverEsquerda(Opcao opcao) {
        if (!opcao.editavel) return;

        if (opcao.indiceValor == 0) {
            opcao.indiceValor = opcao.valores.length - 1;
        } else {
            opcao.indiceValor--;
        }
    }

    private static void moverDireita(Opcao opcao) {
        if (!opcao.editavel) return;

        if (opcao.indiceValor == opcao.valores.length - 1) {
            opcao.indiceValor = 0;
        } else {
            opcao.indiceValor++;
        }
    }


    private static int[] coletarValores(List<Opcao> opcoes) {
        int quantidade = 0;
        for (Opcao o : opcoes) if (o.editavel) quantidade++;

        int[] valores = new int[quantidade];
        int posicao = 0;
        for (Opcao o : opcoes) {
            if (o.editavel) valores[posicao++] = o.indiceValor;
        }
        return valores;
    }


    public static Resposta abrir(List<Opcao> opcoes, String titulo) {
        if (opcoes == null || opcoes.isEmpty()) {
            return new Resposta(Resultado.CANCELADO, -1, null);
        }

        int total = opcoes.size();
        int colunas = calcularColunas(total);
        int selecionado = 0;

        try (Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build()) {

            terminal.enterRawMode();
            String tecla = Entrada.TECLAS.take();

            while (true) {
                renderizar(terminal, opcoes, selecionado, colunas, titulo);

                if (tecla == null) continue;

                switch (tecla) {
                    case "UP"    -> selecionado = moverCima(selecionado, colunas);
                    case "DOWN"  -> selecionado = moverBaixo(selecionado, total, colunas);
                    case "LEFT"  -> moverEsquerda(opcoes.get(selecionado));
                    case "RIGHT" -> moverDireita(opcoes.get(selecionado));
                    case "ENTER" -> {
                        terminal.puts(
                                org.jline.utils.InfoCmp.Capability.clear_screen
                        );
                        terminal.flush();
                        return new Resposta(Resultado.SELECIONADO,
                                selecionado,
                                coletarValores(opcoes));
                    }
                    case "EXIT"  -> {
                        terminal.puts(
                                org.jline.utils.InfoCmp.Capability.clear_screen
                        );
                        terminal.flush();
                        return new Resposta(Resultado.CANCELADO, -1, null);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Erro no terminal: " + e.getMessage());
            return new Resposta(Resultado.CANCELADO, -1, null);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}