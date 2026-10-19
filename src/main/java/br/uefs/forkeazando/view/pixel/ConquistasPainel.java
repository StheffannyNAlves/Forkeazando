package br.uefs.forkeazando.view.pixel;

import br.uefs.forkeazando.model.Conquista;
import br.uefs.forkeazando.model.Progresso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static br.uefs.forkeazando.view.pixel.Cor.rgb;

/**
 * Tela de conquistas: troféu, contador com barra e uma medalha por conquista (dourada se
 * desbloqueada, cadeado cinza se ainda não descoberta). Só monta as linhas; quem imprime é a view.
 */
public final class ConquistasPainel {

    private static final String RST = "\033[0m";
    private static final String OURO = "\033[1;38;2;255;214;102m";
    private static final String BRANCO = "\033[38;2;226;222;238m";
    private static final String CINZA = "\033[38;2;130;134;166m";
    private static final String CIANO = "\033[38;2;110;200;230m";
    private static final int LARGURA_TEXTO = 52;

    private ConquistasPainel() {}

    static Tela trofeu() {
        Tela t = new Tela(Icones.LARGURA, Icones.LINHAS);
        t.rect(0, 0, 7, 8, Icones.FUNDO);
        String[] bmp = {"yyyyyyy", "yYyyyYy", ".yyyyy.", "..yyy..", "...y...", "..yyy..", ".ynnny.", "......."};
        for (int y = 0; y < 8; y++) for (int x = 0; x < 7; x++) {
            char c = bmp[y].charAt(x);
            if (c == 'y') t.px(x, y, rgb(250, 206, 70));
            else if (c == 'Y') t.px(x, y, rgb(200, 150, 30));
            else if (c == 'n') t.px(x, y, rgb(130, 84, 50));
        }
        return t;
    }

    static Tela medalha(boolean desbloqueada) {
        Tela t = new Tela(Icones.LARGURA, Icones.LINHAS);
        t.rect(0, 0, 7, 8, Icones.FUNDO);
        if (desbloqueada) {
            t.rect(2, 0, 1, 2, rgb(226, 62, 74));
            t.rect(4, 0, 1, 2, rgb(70, 130, 230));
            t.elipse(3.5, 4.5, 3, 3, rgb(250, 206, 70));
            t.elipse(3.5, 4.5, 2, 2, rgb(255, 232, 140));
            t.px(3, 4, rgb(200, 150, 30));
            t.px(4, 5, rgb(200, 150, 30));
        } else {
            int cinza = rgb(96, 100, 130);
            t.rect(2, 1, 3, 1, cinza);              // arco do cadeado
            t.rect(2, 2, 1, 1, cinza);
            t.rect(4, 2, 1, 1, cinza);
            t.rect(1, 3, 5, 4, cinza);
            t.rect(3, 4, 1, 2, Icones.FUNDO);       // buraco da chave
        }
        return t;
    }

    public static List<String> linhas(Progresso progresso, Function<LocalDateTime, String> formatoData) {
        Conquista[] catalogo = Conquista.values();
        long desbloqueadas = java.util.Arrays.stream(catalogo).filter(progresso::possui).count();
        int total = catalogo.length;
        int pct = total == 0 ? 0 : (int) (desbloqueadas * 100 / total);

        List<String> saida = new ArrayList<>();
        String[] troLinhas = trofeu().renderizar();
        int cheios = total == 0 ? 0 : (int) (desbloqueadas * 20 / total);
        String[] direita = {
                OURO + "FORKEAZANDO" + RST,
                OURO + "CONQUISTAS" + RST,
                BRANCO + "Desbloqueadas: " + desbloqueadas + "/" + total + " (" + pct + "%)" + RST,
                "\033[38;2;250;206;70m" + "█".repeat(cheios) + "\033[38;2;64;66;96m" + "░".repeat(20 - cheios) + RST
        };
        for (int i = 0; i < 4; i++) saida.add("  " + troLinhas[i] + RST + "  " + direita[i]);
        saida.add("");

        for (Conquista c : catalogo) {
            boolean tem = progresso.possui(c);
            String[] med = medalha(tem).renderizar();
            List<String> texto = new ArrayList<>();
            if (tem) {
                texto.add(OURO + c.getTitulo() + RST);
                List<String> desc = quebrar(c.getDescricao(), LARGURA_TEXTO);
                texto.add(BRANCO + desc.get(0) + RST);
                texto.add(desc.size() > 1 ? BRANCO + desc.get(1) + RST : "");
                texto.add(CIANO + "Desbloqueada em: " + progresso.quandoDesbloqueou(c).map(formatoData).orElse("Data indisponível") + RST);
            } else {
                texto.add(CINZA + "???" + RST);
                texto.add(CINZA + "Conquista ainda não descoberta." + RST);
                texto.add("");
                texto.add("");
            }
            for (int i = 0; i < 4; i++) saida.add("  " + med[i] + RST + "  " + texto.get(i));
            saida.add("");
        }
        return saida;
    }

    static List<String> quebrar(String texto, int largura) {
        List<String> linhas = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        for (String palavra : texto.split(" ")) {
            if (atual.length() > 0 && atual.length() + palavra.length() + 1 > largura) {
                linhas.add(atual.toString());
                atual = new StringBuilder();
            }
            if (atual.length() > 0) atual.append(' ');
            atual.append(palavra);
        }
        if (atual.length() > 0) linhas.add(atual.toString());
        return linhas;
    }
}