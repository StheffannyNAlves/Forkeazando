package br.uefs.forkeazando.view.pixel;

import java.util.HashMap;
import java.util.Map;

import static br.uefs.forkeazando.view.pixel.Cor.mix;
import static br.uefs.forkeazando.view.pixel.Cor.rgb;

/**
 * Ícones 7x8 pixels (7 colunas x 4 linhas de terminal) dos colecionáveis, desenhados a partir de
 * bitmaps de texto. Colecionável ainda não obtido aparece como silhueta apagada.
 */
public final class Icones {

    public static final int LARGURA = 7;
    public static final int LINHAS = 4;
    static final int FUNDO = rgb(22, 24, 56);

    private static final Map<Character, Integer> PALETA = new HashMap<>();

    static {
        PALETA.put('k', rgb(24, 20, 28));
        PALETA.put('w', rgb(240, 240, 246));
        PALETA.put('g', rgb(150, 156, 170));
        PALETA.put('G', rgb(84, 90, 106));
        PALETA.put('r', rgb(226, 62, 74));
        PALETA.put('o', rgb(240, 130, 40));
        PALETA.put('y', rgb(250, 206, 70));
        PALETA.put('l', rgb(70, 190, 100));
        PALETA.put('L', rgb(30, 120, 64));
        PALETA.put('b', rgb(70, 130, 230));
        PALETA.put('B', rgb(30, 54, 140));
        PALETA.put('c', rgb(40, 170, 186));
        PALETA.put('C', rgb(16, 84, 100));
        PALETA.put('n', rgb(130, 84, 50));
        PALETA.put('s', rgb(224, 172, 140));
        PALETA.put('e', rgb(236, 110, 160));
        PALETA.put('p', rgb(150, 100, 210));
    }

    private Icones() {}

    private static final Map<String, String[]> BITMAPS = new HashMap<>();

    static {
        // habilidades
        BITMAPS.put("hab_olho_edital", new String[]{
                ".......", ".kkkkk.", "kwwbwwk", "kwbkbwk", "kwwbwwk", ".kkkkk.", ".......", "......."});
        BITMAPS.put("hab_estomago_ferro", new String[]{
                ".......", ".ggggg.", "gwwwwwg", "gwGGGwg", "gwGGGwg", "gwwwwwg", ".ggggg.", "......."});
        BITMAPS.put("hab_foco_total", new String[]{
                "..rrr..", ".rwwwr.", "rwrrrwr", "rwrkrwr", "rwrrrwr", ".rwwwr.", "..rrr..", "......."});
        BITMAPS.put("hab_rede_contatos", new String[]{
                ".......", "..yyy..", "..yyy..", "..g.g..", ".g...g.", "yyy.yyy", "yyy.yyy", "......."});
        BITMAPS.put("hab_palavra_honesta", new String[]{
                ".wwwww.", "wwwwwlw", "wlwwlww", "wwllwww", "wwwwwww", ".wwwww.", "..ww...", "..w...."});
        BITMAPS.put("hab_sobreviveu_greve", new String[]{
                ".......", ".ll.ll.", "llllll.", ".llLll.", "..lL...", "..nn...", "..nn...", ".nnnn.."});
        BITMAPS.put("hab_contrato_assinado", new String[]{
                ".wwwww.", ".wgggw.", ".wwwww.", ".wgggw.", ".wwwww.", ".wbbwrw", ".wwwwrw", "......."});
        // ------------ itens ------------
        BITMAPS.put("item_cartao_ru", new String[]{
                ".......", "ooooooo", "ooooooo", "kkkkkkk", "ooooooo", "owwoyyo", "ooooooo", "......."});
        BITMAPS.put("item_esquema_copiado", new String[]{
                "BBBBBBB", "BwwwwwB", "BBBBBwB", "BwyryyB", "BwBBBBB", "BwwwwwB", "BBBBBBB", "......."});
        BITMAPS.put("item_pdfs_ic", new String[]{
                "..wwww.", "..wrrw.", "wwwwww.", "wrrrrw.", "wwwwww.", "wgggww.", "wwwwww.", "......."});
        BITMAPS.put("item_contato_freela", new String[]{
                ".kkkkk.", "kbbbbbk", "kbwwwbk", "kbbbbbk", "kbwwbbk", "kbbbbbk", "kkkkkkk", "......."});
        BITMAPS.put("item_vaga_estagio", new String[]{
                "kkkwkkk", "kwkwkwk", "kkkwkkk", "wwwkwww", "kwkkkwk", "wkwwkkw", "kkkwkwk", "......."});
        BITMAPS.put("item_prova_email", new String[]{
                ".....rr", "wwwwwrr", "wgwwwgw", "wwgwgww", "wwwgwww", "wwwwwww", "wwwwwww", "......."});
        BITMAPS.put("item_copo_caio", new String[]{
                ".kkkkk.", ".ccccc.", ".cCCCc.", ".ccCcc.", ".ccccc.", "..ccc..", "..ccc..", "......."});
        BITMAPS.put("item_contato_bia", new String[]{
                "eeeeeee", "e.sss.e", "e.sss.e", "e..s..e", "eppppee", "epppppe", "eeeeeee", "......."});
    }

    public static boolean existe(String flag) {
        return BITMAPS.containsKey(flag);
    }

    public static java.util.Set<String> flags() {
        return java.util.Collections.unmodifiableSet(BITMAPS.keySet());
    }

    /** Ícone 7x8 px sobre fundo de slot. desbloqueado=false vira silhueta cinza-azulada. */
    public static Tela desenhar(String flag, boolean desbloqueado) {
        String[] bmp = BITMAPS.get(flag);
        if (bmp == null) throw new IllegalArgumentException("Ícone inexistente: " + flag);
        Tela t = new Tela(LARGURA, LINHAS);
        t.rect(0, 0, LARGURA, LINHAS * 2, FUNDO);
        for (int y = 0; y < bmp.length; y++) {
            for (int x = 0; x < LARGURA; x++) {
                char c = bmp[y].charAt(x);
                if (c == '.') continue;
                int cor = PALETA.get(c);
                t.px(x, y, desbloqueado ? cor : mix(FUNDO, rgb(120, 126, 160), 0.30));
            }
        }
        return t;
    }
}