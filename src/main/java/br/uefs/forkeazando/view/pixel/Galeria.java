package br.uefs.forkeazando.view.pixel;

import br.uefs.forkeazando.model.Colecionavel;

import java.util.Collection;
import java.util.List;

import static br.uefs.forkeazando.view.pixel.Cor.rgb;

/**
 * Inventário visual: uma fileira de ícones para habilidades e outra para itens, 8 slots por fileira
 * (64 colunas). Slot vazio = silhueta apagada, para o jogador ver o que ainda falta.
 */
public final class Galeria {

    public static final int LARGURA = 66;
    public static final int LINHAS = 12;
    private static final int SLOTS_POR_FILEIRA = 8;
    private static final int FUNDO = rgb(14, 12, 30);

    private Galeria() {}

    /** Quantos colecionáveis do tipo estão em `flags`. */
    public static int contar(Collection<String> flags, Colecionavel.Tipo tipo) {
        int n = 0;
        for (Colecionavel c : Colecionavel.values()) {
            if (c.getTipo() == tipo && flags.contains(c.getFlag())) n++;
        }
        return n;
    }

    public static Tela desenhar(Collection<String> flags) {
        Tela t = new Tela(LARGURA, LINHAS);
        t.rect(0, 0, LARGURA, LINHAS * 2, FUNDO);
        fileira(t, flags, Colecionavel.Tipo.HABILIDADE, "HABILIDADES", 0);
        fileira(t, flags, Colecionavel.Tipo.ITEM, "ITENS", 6);
        return t;
    }

    public static String[] linhas(Collection<String> flags) {
        return desenhar(flags).renderizar();
    }

    private static void fileira(Tela t, Collection<String> flags, Colecionavel.Tipo tipo, String titulo, int linhaInicial) {
        List<Colecionavel> todos = java.util.Arrays.stream(Colecionavel.values()).filter(c -> c.getTipo() == tipo).toList();
        int tenho = contar(flags, tipo);
        t.rect(0, linhaInicial * 2, LARGURA, 2, rgb(30, 28, 66));
        t.texto(1, linhaInicial, titulo, rgb(255, 214, 102));
        String contagem = tenho + "/" + todos.size();
        t.texto(LARGURA - contagem.length() - 1, linhaInicial, contagem, rgb(196, 186, 255));
        for (int i = 0; i < todos.size() && i < SLOTS_POR_FILEIRA; i++) {
            Colecionavel c = todos.get(i);
            Tela icone = Icones.desenhar(c.getFlag(), flags.contains(c.getFlag()));
            int x0 = 1 + i * (Icones.LARGURA + 1);
            int y0 = (linhaInicial + 1) * 2;
            for (int y = 0; y < icone.altura(); y++) {
                for (int x = 0; x < icone.largura(); x++) t.px(x0 + x, y0 + y, icone.get(x, y));
            }
        }
    }
}