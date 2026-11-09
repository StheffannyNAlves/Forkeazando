package br.uefs.forkeazando.view.pixel;

import static br.uefs.forkeazando.view.pixel.Cor.mix;
import static br.uefs.forkeazando.view.pixel.Cor.rgb;

/**
 * Gerador de retratos 24x24 a partir de um {@link Perfil}. Identidade (pele, cabelo,
 * roupa, óculos, barba, acessório) vem do perfil e é desenhada igual em todas as
 * expressões; a expressão só repinta sobrancelhas, olhos e boca (linhas 7 a 14).
 */
final class RetratosGenericos {

    enum Cabelo { CURTO, LONGO, CACHEADO, RABO, BONE }

    /** barba: 0 = sem barba; densidade 1 = rala, 2 = cheia. */
    record Perfil(int pele, int cabelo, int roupa, Cabelo estilo, boolean oculos, int barba, int densidadeBarba,
                  boolean jaleco, int fundo) {}

    private static final int ESCURO = rgb(24, 20, 28);
    private static final int BRANCO = rgb(236, 236, 246);
    private static final int BOCA = rgb(110, 56, 58);

    private RetratosGenericos() {}

    static final Perfil SERGIO = new Perfil(rgb(196, 146, 116), rgb(150, 150, 162), rgb(70, 110, 170),
            Cabelo.CURTO, true, rgb(176, 176, 184), 2, false, rgb(40, 60, 110));
    static final Perfil RAFAEL = new Perfil(rgb(188, 130, 98), rgb(62, 42, 32), rgb(240, 240, 246),
            Cabelo.CURTO, false, rgb(104, 76, 62), 1, true, rgb(30, 70, 80));
    static final Perfil BIA = new Perfil(rgb(198, 142, 108), rgb(52, 32, 26), rgb(214, 196, 160),
            Cabelo.LONGO, false, 0, 0, false, rgb(70, 50, 90));
    static final Perfil DANDARA = new Perfil(rgb(112, 72, 54), rgb(26, 20, 34), rgb(190, 50, 60),
            Cabelo.CACHEADO, false, 0, 0, false, rgb(86, 40, 70));
    static final Perfil ZE = new Perfil(rgb(160, 108, 80), rgb(200, 50, 50), rgb(40, 130, 84),
            Cabelo.BONE, false, rgb(100, 72, 58), 1, false, rgb(90, 60, 30));

    static Tela desenhar(Perfil p, String expressao) {
        Tela t = new Tela(24, 12);
        int fundoEscuro = mix(p.fundo(), rgb(8, 6, 20), 0.75);
        for (int y = 0; y < 24; y++) {
            for (int x = 0; x < 24; x++) {
                double d = Math.sqrt((x - 12) * (x - 12) + (y - 12) * (y - 12));
                t.px(x, y, mix(p.fundo(), fundoEscuro, Math.min(1, d / 18)));
            }
        }
        int pele = p.pele(), peleS = mix(pele, rgb(0, 0, 0), 0.22), peleL = mix(pele, rgb(255, 255, 255), 0.10);
        // ombros
        for (int y = 18; y < 24; y++) {
            double meia = 9 + (y - 18) * 0.7;
            t.rect((int) (12 - meia), y, (int) (meia * 2), 1, p.roupa());
        }
        t.rect(3, 19, 2, 5, mix(p.roupa(), rgb(255, 255, 255), 0.18));
        t.rect(19, 19, 2, 5, mix(p.roupa(), rgb(0, 0, 0), 0.25));
        if (p.jaleco()) {                        // camiseta escura por baixo do jaleco + bolso com multímetro
            t.rect(10, 17, 4, 7, rgb(70, 76, 90));
            t.rect(9, 17, 1, 3, p.roupa());
            t.rect(14, 17, 1, 3, p.roupa());
            t.rect(16, 20, 3, 3, rgb(240, 200, 40));
            t.rect(17, 21, 1, 1, rgb(30, 30, 40));
        } else {
            t.rect(9, 17, 6, 2, mix(p.roupa(), rgb(0, 0, 0), 0.3));
        }
        t.rect(10, 15, 4, 3, peleS);
        // cabelo comprido por trás
        if (p.estilo() == Cabelo.LONGO) {
            t.rect(4, 7, 3, 15, p.cabelo());
            t.rect(17, 7, 3, 15, p.cabelo());
        }
        if (p.estilo() == Cabelo.CACHEADO) {
            t.elipse(12, 8, 10, 8.4, p.cabelo());
        }
        // cabeça, orelhas, nariz
        t.elipse(12, 10, 6.2, 7.2, pele);
        for (int y = 5; y < 15; y++) {
            if (y >= 9 && y <= 11) t.px(7, y, peleL);
            t.px(17, y, peleS);
        }
        t.rect(5, 10, 1, 3, peleS);
        t.rect(18, 10, 1, 3, peleS);
        t.rect(12, 11, 1, 2, peleS);
        t.px(11, 13, peleS);
        t.px(13, 13, peleS);
        // barba
        if (p.barba() != 0) {
            int yMin = p.densidadeBarba() == 2 ? 12 : 14;
            for (int y = yMin; y <= 16; y++) {
                for (int x = 7; x <= 17; x++) {
                    double dx = (x + 0.5 - 12) / 6.2, dy = (y + 0.5 - 10) / 7.2;
                    boolean dentro = dx * dx + dy * dy <= 1.0 || y == 16 && x >= 9 && x <= 15;
                    boolean sorteio = p.densidadeBarba() == 2 || (x * 7 + y * 3) % 3 == 0;
                    if (dentro && sorteio) t.px(x, y, p.barba());
                }
            }
        }
        // cabelo/boné
        switch (p.estilo()) {
            case CURTO, LONGO, RABO -> {
                t.elipse(12, 5, 7.2, 4.4, p.cabelo());
                t.rect(5, 6, 2, 4, p.cabelo());
                t.rect(17, 6, 2, 4, p.cabelo());
                t.rect(8, 7, 3, 1, p.cabelo());
                t.rect(14, 7, 3, 1, p.cabelo());
                if (p.estilo() == Cabelo.RABO) t.rect(19, 6, 2, 9, p.cabelo());
            }
            case CACHEADO -> {
                t.elipse(12, 10, 6.2, 7.2, pele);
                t.rect(5, 10, 1, 3, peleS);
                t.rect(18, 10, 1, 3, peleS);
                t.rect(12, 11, 1, 2, peleS);
                t.elipse(12, 4.5, 8.5, 4.2, p.cabelo());
                t.rect(5, 5, 2, 6, p.cabelo());
                t.rect(17, 5, 2, 6, p.cabelo());
                for (int i = 0; i < 14; i++) t.px(4 + (i * 5) % 17, 1 + (i * 3) % 8, mix(p.cabelo(), rgb(255, 255, 255), 0.18));
            }
            case BONE -> {
                t.elipse(12, 5.5, 6.6, 4.0, p.cabelo());
                t.rect(5, 7, 15, 2, mix(p.cabelo(), rgb(0, 0, 0), 0.25));    // aba
                t.rect(7, 3, 2, 1, mix(p.cabelo(), rgb(255, 255, 255), 0.3));
            }
        }
        if (p.oculos()) {                          // aro ao redor dos olhos (olhos em x 8..10 e 14..16, y 9..11)
            for (int x0 : new int[]{7, 13}) {
                t.rect(x0, 8, 5, 1, rgb(18, 16, 32));
                t.rect(x0, 12, 5, 1, rgb(18, 16, 32));
                t.rect(x0, 9, 1, 3, rgb(18, 16, 32));
                t.rect(x0 + 4, 9, 1, 3, rgb(18, 16, 32));
            }
            t.rect(12, 9, 1, 1, rgb(18, 16, 32));
        }
        expressao(t, p, expressao);
        int borda = rgb(120, 100, 240);
        for (int i = 0; i < 24; i++) {
            t.px(i, 0, borda);
            t.px(i, 23, borda);
            t.px(0, i, borda);
            t.px(23, i, borda);
        }
        return t;
    }

    private static void expressao(Tela t, Perfil p, String e) {
        int sobr = p.estilo() == Cabelo.BONE ? ESCURO : mix(p.cabelo(), rgb(0, 0, 0), 0.35);
        for (int x0 : new int[]{8, 14}) {
            if (Retratos.FELIZ.equals(e)) {
                t.rect(x0, 10, 3, 1, ESCURO);                         // olhos sorrindo
                t.px(x0 + 1, 9, mix(p.pele(), rgb(255, 255, 255), 0.1));
            } else {
                t.rect(x0, 10, 3, 1, BRANCO);
                t.px(x0 + 1, 10, ESCURO);
                t.rect(x0, 11, 3, 1, mix(p.pele(), rgb(60, 30, 50), 0.35));
                t.rect(x0, 9, 3, 1, mix(p.pele(), ESCURO, 0.45));    // pálpebra
            }
        }
        switch (e == null ? "" : e) {
            case Retratos.SERIA -> {                                  // sobrancelhas franzidas
                t.rect(8, 7, 2, 1, sobr); t.px(10, 8, sobr);
                t.rect(15, 7, 2, 1, sobr); t.px(14, 8, sobr);
                t.rect(11, 14, 3, 1, BOCA);
            }
            case Retratos.TRISTE -> {
                t.px(8, 8, sobr); t.rect(9, 7, 2, 1, sobr);
                t.rect(14, 7, 2, 1, sobr); t.px(16, 8, sobr);
                t.rect(11, 13, 3, 1, BOCA); t.px(10, 14, BOCA); t.px(14, 14, BOCA);
            }
            case Retratos.FELIZ -> {
                t.rect(8, 7, 3, 1, sobr); t.rect(14, 7, 3, 1, sobr);
                t.rect(10, 14, 5, 1, BOCA); t.px(9, 13, BOCA); t.px(15, 13, BOCA);
                t.rect(11, 13, 3, 1, rgb(244, 240, 238));              // dentes
            }
            default -> {
                t.rect(8, 7, 3, 1, sobr); t.rect(14, 7, 3, 1, sobr);
                t.rect(10, 14, 5, 1, BOCA);
            }
        }
    }

    //  Sistema Acadêmico (ÁGATA): um monitor amarelo com cara
    static Tela sistema(String expressao) {
        Tela t = new Tela(24, 12);
        int amarelo = rgb(250, 200, 60);
        for (int y = 0; y < 24; y++) {
            for (int x = 0; x < 24; x++) {
                double d = Math.sqrt((x - 12) * (x - 12) + (y - 12) * (y - 12));
                t.px(x, y, mix(rgb(40, 34, 14), rgb(10, 8, 20), Math.min(1, d / 18)));
            }
        }
        t.rect(3, 3, 18, 15, rgb(70, 72, 90));
        t.rect(3, 3, 18, 1, rgb(130, 134, 160));
        t.rect(4, 4, 16, 13, rgb(10, 14, 30));
        t.rect(10, 18, 4, 2, rgb(70, 72, 90));
        t.rect(7, 20, 10, 2, rgb(70, 72, 90));
        for (int y = 5; y < 17; y += 2) t.rect(4, y, 16, 1, rgb(14, 20, 40));   // scanlines
        t.rect(7, 7, 3, 3, amarelo);                                            // olhos
        t.rect(14, 7, 3, 3, amarelo);
        if (Retratos.PROCESSANDO.equals(expressao)) {
            t.rect(8, 12, 2, 2, amarelo);
            t.rect(11, 12, 2, 2, amarelo);
            t.rect(14, 12, 2, 2, amarelo);
        } else {
            t.rect(8, 13, 8, 1, amarelo);
        }
        int borda = rgb(120, 100, 240);
        for (int i = 0; i < 24; i++) {
            t.px(i, 0, borda);
            t.px(i, 23, borda);
            t.px(0, i, borda);
            t.px(23, i, borda);
        }
        return t;
    }
}