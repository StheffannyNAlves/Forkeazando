package br.uefs.forkeazando.view.widget;

import br.uefs.forkeazando.view.Entrada;

import org.jline.terminal.Terminal;
import java.util.function.IntConsumer;

import java.util.List;

public class MenuInterativo {

    public enum Resultado { SELECIONADO, CANCELADO }
    private static boolean menuJaRenderizado = false;

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
        return 1;
    }



    private static void renderizar(Terminal terminal, List<Opcao> opcoes, int selecionado, int colunas, String titulo, boolean estiloConfigs) {

        java.io.PrintWriter out = terminal.writer();

        int total = opcoes.size();

        // Volta ao início do menu somente quando ele já foi desenhado.
        // A primeira renderização acontece abaixo da narrativa.
        if (menuJaRenderizado) {
            out.print("\033[" + (total + 2) + "A");
            out.print("\033[J");
        }

        if (titulo != null && !titulo.isBlank()) {
            out.println("\033[1;36m" + titulo + "\033[0m");
            out.println();
        }

        for (int i = 0; i < total; i++) {
            Opcao opcao = opcoes.get(i);
            String texto = opcao.renderizar();


            if (i == selecionado) {
                out.print("\033[1;33m ❯ " + texto + "\033[0m");
            } else {
                out.print("\033[90m   " + texto + "\033[0m");
            }


            out.println("\033[K");
        }

        out.flush();
        menuJaRenderizado = true;
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
        return abrir(opcoes, titulo, false);
    }

    public static Resposta abrir(List<Opcao> opcoes, String titulo, boolean estiloConfigs) {
        return abrir(opcoes, titulo, estiloConfigs, null);
    }

    private static void notificar(IntConsumer callback, int indice) {
        if (callback != null) callback.accept(indice);
    }
    public static Resposta abrir(List<Opcao> opcoes, String titulo, boolean estiloConfigs, IntConsumer aoMudarValor) {
        if (opcoes == null || opcoes.isEmpty()) {
            return new Resposta(Resultado.CANCELADO, -1, null);
        }

        int total = opcoes.size();
        int colunas = calcularColunas(total);
        int selecionado = 0;

        Terminal terminal = Entrada.terminal();   // o terminal da Entrada, sem criar outro

        Entrada.TECLAS.clear();                   // descarta teclas que sobraram de telas anteriores
        menuJaRenderizado = false;
        try {
            while (true) {
                renderizar(terminal, opcoes, selecionado, colunas, titulo, estiloConfigs);

                String tecla = Entrada.TECLAS.take();   // cada volta espera uma tecla nova

                switch (tecla) {
                    case "UP"    -> selecionado = moverCima(selecionado, colunas);
                    case "DOWN"  -> selecionado = moverBaixo(selecionado, total, colunas);
                    case "LEFT"  -> {
                        moverEsquerda(opcoes.get(selecionado));
                        notificar(aoMudarValor, selecionado);
                    }
                    case "RIGHT" -> {
                        moverDireita(opcoes.get(selecionado));
                        notificar(aoMudarValor, selecionado);
                    }
                    case "ENTER" -> {
                        terminal.writer().print("\033[" + (total + 2) + "A");
                        terminal.writer().print("\033[J");
                        terminal.writer().flush();

                        menuJaRenderizado = false;

                        return new Resposta(
                                Resultado.SELECIONADO,
                                selecionado,
                                coletarValores(opcoes)
                        );
                    }

                    case "EXIT" -> {
                        terminal.writer().print("\033[" + (total + 2) + "A");
                        terminal.writer().print("\033[J");
                        terminal.writer().flush();

                        menuJaRenderizado = false;

                        return new Resposta(Resultado.CANCELADO, -1, null);
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new Resposta(Resultado.CANCELADO, -1, null);
        }
    }
    public static Resposta abrir(List<Opcao> opcoes, String titulo, IntConsumer renderizador) {

        if (opcoes == null || opcoes.isEmpty()) {
            return new Resposta(Resultado.CANCELADO, -1, null);
        }

        int total = opcoes.size();
        int colunas = 1;
        int selecionado = 0;
        Entrada.TECLAS.clear();

        try {
            while (true) {
                renderizador.accept(selecionado);

                String tecla = Entrada.TECLAS.take();

                switch (tecla) {
                    case "UP"    -> selecionado = moverCima(selecionado, colunas);
                    case "DOWN"  -> selecionado = moverBaixo(selecionado, total, colunas);
                    case "LEFT"  -> moverEsquerda(opcoes.get(selecionado));
                    case "RIGHT" -> moverDireita(opcoes.get(selecionado));
                    case "ENTER" -> {
                        return new Resposta(
                                Resultado.SELECIONADO,
                                selecionado,
                                coletarValores(opcoes));
                    }
                    case "EXIT" -> {
                        return new Resposta(Resultado.CANCELADO, -1, null);
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new Resposta(Resultado.CANCELADO, -1, null);
        }
    }

}