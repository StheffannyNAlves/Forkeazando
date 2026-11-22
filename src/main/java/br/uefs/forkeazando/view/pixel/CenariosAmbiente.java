package br.uefs.forkeazando.view.pixel;

import static br.uefs.forkeazando.view.pixel.Cor.*;
import static br.uefs.forkeazando.view.pixel.Elementos.glow;

/** Repouso cíclico sobre a composição final: não repete acontecimentos da entrada. */
final class CenariosAmbiente {
    private CenariosAmbiente() {}
    static Tela desenhar(String chave, Tela t, int q) {
        double fase = q * Math.PI * 2 / 12.0;
        switch (chave) {
            case "abertura_entrevista" -> luz(t, 33, 3, fase);
            case "cap1_matricula" -> { cursor(t, 44, 21, q); luz(t, 38, 22, fase); }
            case "cap1_patio" -> { folhas(t, 8, 16, fase); nuvem(t, 39, 4, fase); }
            case "cap1_ru" -> { vapor(t, 28, 29, fase); luz(t, 32, 6, fase); }
            case "cap1_oficina" -> { osciloscopio(t, 6, 14, 11, 9, fase); luz(t, 33, 2, fase); }
            case "cap1_oficina_led", "cap1_oficina_vazia" -> osciloscopio(t, 19, 12, 20, 11, fase);
            case "final_dtec" -> luz(t, 32, 25, fase);
            case "cap1_pbl" -> cantoPapel(t, 43, 17, fase);
            case "sala_grupo" -> { vapor(t, 11, 28, fase); luz(t, 33, 4, fase); }
            case "cap1_modulo8" -> { luz(t, 33, 8, fase); vapor(t, 8, 30, fase); }
            case "cap1_onibus" -> chuva(t, q, 4, 4, 58, 28);
            case "cap2_mural", "cap4_editais" -> cantoPapel(t, 56, 27, fase);
            case "cap2_assembleia" -> { folhas(t, 5, 10, fase); cantoPapel(t, 27, 17, fase); }
            case "cap2_feira" -> { folhas(t, 12, 4, fase); luz(t, 34, 20, fase); }
            case "cap3_prova" -> { ponteiro(t, q); luz(t, 34, 2, fase); }
            case "cap3_notas" -> cantoPapel(t, 58, 34, fase);
            case "cap4_agata", "cap4_agata_resultado" -> { cursor(t, 37, 40, q); luz(t, 32, 23, fase); }
            case "cap4_prazo" -> { cursor(t, 36, 27, q); luz(t, 24, 20, fase); }
            case "cap5_greve" -> chuva(t, q, 1, 14, 64, 21);
            case "cap5_freela" -> { cursor(t, 33, 27, q); luz(t, 15, 13, fase); }
            case "cap6_quarto" -> poeira(t, 16, 11, fase);
            case "cap6_ru" -> { luz(t, 33, 9, fase); cantoPapel(t, 56, 37, fase); }
            case "cap7_tcc" -> { cursor(t, 18, 22, q); estrelas(t, fase, 49, 5, 11, 11); }
            case "cap7_defesa" -> luz(t, 33, 11, fase);
            case "cap8_formatura" -> { luz(t, 33, 5, fase); cantoPapel(t, 35, 21, fase); }
            case "cap8_portao" -> { estrelas(t, fase, 5, 3, 54, 10); luz(t, 33, 25, fase); }
            case "final_ic", "futuro_pesquisa_brasil" -> { folhas(t, 11, 16, fase); luz(t, 21, 18, fase); }
            case "final_independente" -> { folhas(t, 6, 26, fase); luz(t, 52, 34, fase); }
            case "futuro_independente" -> { vapor(t, 59, 37, fase); cursor(t, 49, 30, q); }
            case "futuro_tecnologia" -> { luz(t, 49, 31, fase); onda(t, q); }
            case "futuro_internacional" -> { estrelas(t, fase, 5, 6, 54, 12); luz(t, 33, 4, fase); }
            case "futuro_transicao" -> { estrelas(t, fase, 4, 3, 56, 16); luz(t, 56, 28, fase); }
            case "caderno_inicio" -> { luz(t, 19, 27, fase); estrelas(t, fase, 43, 5, 16, 9); }
            case "secretaria" -> { cursor(t, 49, 28, q); folhas(t, 58, 22, fase); }
            case "oficina_extensao" -> { cursor(t, 15, 28, q); cursor(t, 36, 28, (q + 4) % 12); }
            case "auditorio_campus" -> { luz(t, 33, 14, fase); poeira(t, 29, 12, fase); }
            case "ponto_onibus" -> { luz(t, 52, 22, fase); nuvem(t, 28, 7, fase); }
            case "celebracao_casa" -> { luz(t, 15, 5, fase); luz(t, 46, 5, fase + 1.2); }
            case "corredor_apoio" -> { poeira(t, 17, 17, fase); folhas(t, 36, 22, fase); }
            case "memoria_bancada" -> poeira(t, 34, 12, fase);
            default -> throw new IllegalArgumentException("Ambiente sem perfil visual: " + chave);
        }
        return t;
    }
    private static void luz(Tela t, int x, int y, double fase) {
        glow(t, x, y, 9, 6, rgb(255, 215, 157), 0.018 + 0.024 * (1 + Math.sin(fase)));
    }
    private static void cursor(Tela t, int x, int y, int q) {
        if (q < 6) t.rect(x, y, 1, 2, rgb(168, 215, 189));
    }
    private static void vapor(Tela t, int x, int y, double fase) {
        for (int i = 0; i < 3; i++) {
            int dx = (int) Math.round(Math.sin(fase + i) * 1.3);
            t.tint(x + dx, y - i * 2, rgb(240, 224, 209), 0.25 + 0.12 * Math.sin(fase + i));
        }
    }
    private static void folhas(Tela t, int x, int y, double fase) {
        int d = (int) Math.round(Math.sin(fase));
        t.tint(x + d, y, rgb(135, 182, 112), 0.20);
        t.tint(x + 2, y - d, rgb(174, 200, 136), 0.18);
        t.tint(x - 2, y + d, rgb(113, 164, 105), 0.22);
    }
    private static void nuvem(Tela t, int x, int y, double fase) {
        int d = (int) Math.round(Math.sin(fase));
        for (int i = 0; i < 3; i++) t.tint(x + i + d, y, rgb(247, 222, 201), 0.20);
    }
    private static void cantoPapel(Tela t, int x, int y, double fase) {
        int d = (int) Math.round(Math.sin(fase));
        t.tint(x + d, y, rgb(248, 236, 204), 0.25);
        t.tint(x, y + 1, rgb(96, 75, 102), 0.12 + Math.abs(Math.sin(fase)) * 0.12);
    }
    private static void poeira(Tela t, int x, int y, double fase) {
        for (int i = 0; i < 4; i++) {
            int dx = (int) Math.round(2 * Math.sin(fase + i));
            int dy = (int) Math.round(Math.cos(fase + i));
            t.tint(x + i * 3 + dx, y + i * 2 + dy, rgb(247, 220, 167), 0.12 + 0.10 * (1 + Math.sin(fase + i)));
        }
    }
    private static void chuva(Tela t, int q, int x, int y, int w, int h) {
        for (int i = 0; i < 16; i++) {
            int xx = x + (i * 13 + q) % w, yy = y + (i * 7 + q * 2) % h;
            t.tint(xx, yy, rgb(204, 224, 246), 0.42);
            t.tint(xx, yy + 1, rgb(204, 224, 246), 0.25);
        }
    }
    private static void estrelas(Tela t, double fase, int x, int y, int w, int h) {
        for (int i = 0; i < 6; i++) t.tint(x + (i * 13) % w, y + (i * 7) % h,
                rgb(255, 237, 192), 0.15 + 0.13 * (1 + Math.sin(fase + i)));
    }
    private static void ponteiro(Tela t, int q) {
        // Ponteiro ambiental suave; não reinicia a prova nem altera a nota.
        for (int y = 4; y <= 16; y++) for (int x = 38; x <= 50; x++) {
            if (t.get(x, y) == rgb(214, 40, 50)) t.px(x, y, rgb(246, 246, 240));
        }
        double a = q * Math.PI * 2 / 12;
        for (int r = 2; r < 5; r++) t.px(44 + (int) Math.round(Math.cos(a) * r),
                10 + (int) Math.round(Math.sin(a) * r), rgb(191, 59, 71));
    }
    private static void osciloscopio(Tela t, int x, int y, int w, int h, double fase) {
        t.rect(x, y, w, h, rgb(8, 24, 16));
        t.rect(x, y + h / 2, w, 1, rgb(18, 60, 35));
        for (int i = 0; i < w; i++) t.px(x + i,
                y + h / 2 + (int) Math.round((h / 2 - 1) * Math.sin(i * 0.55 + fase)), rgb(95, 237, 143));
    }
    private static void onda(Tela t, int q) {
        for (int x = 0; x < 16; x++) t.px(5 + x, 14 + (int) Math.round(Math.sin((x + q) * 0.8)), rgb(109, 229, 159));
    }
}
