package br.uefs.forkeazando.view.pixel;

import java.util.Random;

import static br.uefs.forkeazando.view.pixel.Cor.mix;
import static br.uefs.forkeazando.view.pixel.Cor.rgb;
import static br.uefs.forkeazando.view.pixel.Cor.gradiente;

/**
 * Ilustrações de cena (66 x 48 pixels = 66 colunas x 24 linhas de terminal,
 * a mesma largura da caixa do CenaView).
 *
 * Cada cena tem paleta própria; nenhuma é variação do entardecer do menu.
 */
public final class Ilustracoes {
    public static final int LARGURA = 66;
    public static final int LINHAS = 24;

    public static final String ABERTURA_ENTREVISTA = "abertura_entrevista";
    public static final String CAP1_MATRICULA = "cap1_matricula";
    public static final String CAP1_PATIO = "cap1_patio";
    public static final String CAP1_RU = "cap1_ru";
    public static final String CAP1_OFICINA = "cap1_oficina";
    public static final String CAP1_PBL = "cap1_pbl";
    public static final String SALA_GRUPO = "sala_grupo";
    public static final String CAP1_OFICINA_VAZIA = "cap1_oficina_vazia";
    public static final String CAP1_OFICINA_LED = "cap1_oficina_led";
    public static final String CAP1_MODULO8 = "cap1_modulo8";
    public static final String CAP1_ONIBUS = "cap1_onibus";
    public static final String CAP2_MURAL = "cap2_mural";
    public static final String CAP2_ASSEMBLEIA = "cap2_assembleia";
    public static final String CAP2_FEIRA = "cap2_feira";
    public static final String CAP3_PROVA = "cap3_prova";
    public static final String CAP3_NOTAS = "cap3_notas";
    public static final String CAP4_EDITAIS = "cap4_editais";
    public static final String CAP4_AGATA = "cap4_agata";
    public static final String CAP4_PRAZO = "cap4_prazo";
    public static final String CAP5_GREVE = "cap5_greve";
    public static final String CAP5_FREELA = "cap5_freela";
    public static final String CAP6_QUARTO = "cap6_quarto";
    public static final String CAP6_RU = "cap6_ru";
    public static final String CAP7_TCC = "cap7_tcc";
    public static final String CAP7_DEFESA = "cap7_defesa";
    public static final String CAP8_FORMATURA = "cap8_formatura";
    public static final String CAP8_PORTAO = "cap8_portao";
    public static final String FINAL_IC = "final_ic";
    public static final String FINAL_DTEC = "final_dtec";
    public static final String FINAL_INDEPENDENTE = "final_independente";
    public static final String FUTURO_PESQUISA_BRASIL = "futuro_pesquisa_brasil";
    public static final String FUTURO_TECNOLOGIA = "futuro_tecnologia";
    public static final String FUTURO_INTERNACIONAL = "futuro_internacional";
    public static final String FUTURO_INDEPENDENTE = "futuro_independente";
    public static final String FUTURO_TRANSICAO = "futuro_transicao";

    /** Quadros de cada animação de entrada. O ÚLTIMO quadro é sempre a arte final (estática). */
    static final int QUADROS_ENTREVISTA = 12;
    static final int QUADROS_MATRICULA = 14;
    static final int QUADROS_PATIO = 18;

    private static final RegistroDeArte REGISTRO = new RegistroDeArte();
    private static final java.util.Map<String, Animacao> ANIMACOES = new java.util.LinkedHashMap<>();

    /** Cadastra a arte final (último quadro) e a animação de entrada sob a mesma chave. */
    private static void reg(String chave, int quadros, long intervaloMs, java.util.function.IntFunction<Tela> desenho) {
        REGISTRO.registrar(chave, () -> desenho.apply(quadros - 1));
        ANIMACOES.put(chave, new Animacao(quadros, intervaloMs, desenho));
    }

    static {
        reg(ABERTURA_ENTREVISTA, QUADROS_ENTREVISTA, 90, Ilustracoes::abertura);
        reg(CAP1_MATRICULA, QUADROS_MATRICULA, 110, Ilustracoes::cap1Matricula);
        reg(CAP1_PATIO, QUADROS_PATIO, 85, Ilustracoes::cap1Patio);
        reg(CAP1_RU, CenasCap1.N_RU, 100, CenasCap1::ru);
        reg(CAP1_OFICINA, CenasCap1.N_OFICINA, 100, CenasCap1::oficina);
        reg(CAP1_PBL, CenasCap1.N_PBL, 110, CenasCap1::pbl);
        reg(SALA_GRUPO, CenasCap1.N_GRUPO, 110, CenasCap1::salaGrupo);
        reg(CAP1_OFICINA_VAZIA, CenasCap1.N_VAZIA, 120, CenasCap1::oficinaVazia);
        reg(CAP1_OFICINA_LED, CenasCap1.N_LED, 120, CenasCap1::oficinaLed);
        reg(CAP1_MODULO8, CenasCap1.N_MODULO8, 100, CenasCap1::modulo8);
        reg(CAP1_ONIBUS, CenasCap1.N_ONIBUS, 110, CenasCap1::onibus);
        reg(CAP2_MURAL, CenasCap2a4.N_MURAL, 110, CenasCap2a4::mural);
        reg(CAP2_ASSEMBLEIA, CenasCap2a4.N_ASSEMBLEIA, 100, CenasCap2a4::assembleia);
        reg(CAP2_FEIRA, CenasCap2a4.N_FEIRA, 100, CenasCap2a4::feira);
        reg(CAP3_PROVA, CenasCap2a4.N_PROVA, 110, CenasCap2a4::prova);
        reg(CAP3_NOTAS, CenasCap2a4.N_NOTAS, 120, CenasCap2a4::notas);
        reg(CAP4_EDITAIS, CenasCap2a4.N_EDITAIS, 120, CenasCap2a4::editais);
        reg(CAP4_AGATA, CenasCap2a4.N_AGATA, 110, CenasCap2a4::agata);
        reg(CAP4_PRAZO, CenasCap2a4.N_PRAZO, 120, CenasCap2a4::prazo);
        reg(CAP5_GREVE, CenasCap5a8.N_GREVE, 100, CenasCap5a8::greve);
        reg(CAP5_FREELA, CenasCap5a8.N_FREELA, 120, CenasCap5a8::freela);
        reg(CAP6_QUARTO, CenasCap5a8.N_QUARTO, 120, CenasCap5a8::quarto);
        reg(CAP6_RU, CenasCap5a8.N_RU6, 120, CenasCap5a8::ru6);
        reg(CAP7_TCC, CenasCap5a8.N_TCC, 120, CenasCap5a8::tcc);
        reg(CAP7_DEFESA, CenasCap5a8.N_DEFESA, 120, CenasCap5a8::defesa);
        reg(CAP8_FORMATURA, CenasCap5a8.N_FORMATURA, 110, CenasCap5a8::formatura);
        reg(CAP8_PORTAO, CenasCap5a8.N_PORTAO, 110, CenasCap5a8::portao);
        reg(FINAL_IC, CenasFinais.N_IC, 120, CenasFinais::ic);
        reg(FINAL_DTEC, CenasFinais.N_DTEC, 120, CenasFinais::dtec);
        reg(FINAL_INDEPENDENTE, CenasFinais.N_INDEP, 120, CenasFinais::independente);
        reg(FUTURO_PESQUISA_BRASIL, CenasFuturos.N_PESQUISA, 120, CenasFuturos::pesquisa);
        reg(FUTURO_TECNOLOGIA, CenasFuturos.N_TECNOLOGIA, 120, CenasFuturos::tecnologia);
        reg(FUTURO_INTERNACIONAL, CenasFuturos.N_INTERNACIONAL, 110, CenasFuturos::internacional);
        reg(FUTURO_INDEPENDENTE, CenasFuturos.N_INDEPENDENTE, 120, CenasFuturos::independente);
        reg(FUTURO_TRANSICAO, CenasFuturos.N_TRANSICAO, 120, CenasFuturos::transicao);
    }

    /** Animação de entrada da ilustração, se ela tiver uma. */
    public static java.util.Optional<Animacao> animacao(String chave) {
        return java.util.Optional.ofNullable(ANIMACOES.get(chave));
    }

    private Ilustracoes() {}

    public static boolean existe(String chave) { return REGISTRO.existe(chave); }

    public static String[] linhas(String chave) { return REGISTRO.linhas(chave); }

    public static java.util.Set<String> chaves() { return REGISTRO.chaves(); }

    //  Cap. 1 / cenas 3, 4, 71, 72, 73 — pátio da SIECOMP
    //  Caramelo passa; Cáio olha com o copo térmico amassado.
    //  Paleta de meio-dia: céu saturado, sombra dura, concreto.
    static Tela cap1Patio(int quadro) {
        int faltam = QUADROS_PATIO - 1 - quadro;   // 0 no quadro final
        Tela t = new Tela(LARGURA, LINHAS);
        Random rnd = new Random(3);

        // céu
        int[] ysCeu = {0, 22};
        int[][] csCeu = {{70, 140, 226}, {170, 214, 248}};
        for (int y = 0; y < 22; y++) {
            t.rect(0, y, LARGURA, 1, gradiente(y, ysCeu, csCeu));
        }
        // nuvens
        int[][] nuvens = {{10, 5, 9}, {30, 3, 12}, {55, 7, 8}};
        int deriva = faltam / 3;   // as nuvens andam devagar pra direita durante a entrada
        for (int[] n : nuvens) {
            for (int dx = 0; dx < n[2]; dx++) {
                int h = (dx > 1 && dx < n[2] - 2) ? 2 : 1;
                t.rect(n[0] - deriva + dx, n[1] - 1, 1, h + 1, rgb(250, 252, 255));
                t.px(n[0] - deriva + dx, n[1] + h, rgb(206, 224, 244));
            }
        }

        // prédio ao fundo
        t.rect(0, 14, LARGURA, 18, rgb(226, 220, 206));
        t.rect(0, 14, LARGURA, 1, rgb(250, 246, 236));
        for (int x = 2; x < LARGURA - 4; x += 8) {
            t.rect(x, 18, 5, 6, rgb(44, 70, 110));
            t.rect(x, 18, 5, 1, rgb(120, 170, 220));
            t.rect(x + 2, 18, 1, 6, rgb(210, 204, 190));
            t.rect(x, 25, 5, 1, rgb(190, 184, 170));
        }
        t.rect(0, 28, LARGURA, 3, rgb(210, 98, 40));
        t.rect(0, 28, LARGURA, 1, rgb(236, 130, 64));
        t.rect(20, 10, 26, 4, rgb(30, 44, 96));
        t.texto(26, 5, "SIECOMP", rgb(255, 214, 102));

        // passarela coberta à direita (sombra dura)
        t.rect(34, 16, 32, 3, rgb(120, 114, 126));
        t.rect(34, 19, 32, 1, rgb(70, 66, 84));
        for (int x : new int[]{36, 52, 64}) {
            t.rect(x, 19, 2, 14, rgb(184, 178, 190));
            t.rect(x + 1, 19, 1, 14, rgb(130, 124, 140));
        }
        for (int y = 20; y < 32; y++) {
            for (int x = 34; x < LARGURA; x++) {
                t.tint(x, y, rgb(24, 22, 56), 0.34);
            }
        }

        // árvore à esquerda
        t.rect(6, 22, 3, 12, rgb(96, 62, 36));
        t.rect(6, 22, 1, 12, rgb(130, 88, 50));
        int[] cop = {rgb(18, 70, 40), rgb(30, 104, 52), rgb(52, 140, 62), rgb(98, 176, 78)};
        int[][] tufos = {{7, 14, 9, 7}, {3, 11, 5, 5}, {12, 11, 5, 5}};
        for (int[] f : tufos) {
            int cx = f[0], cy = f[1], rx = f[2], ry = f[3];
            for (int by = cy - ry; by <= cy + ry; by += 2) {
                for (int bx = cx - rx; bx <= cx + rx; bx += 2) {
                    double dx = (double) (bx - cx) / rx;
                    double dy = (double) (by - cy) / ry;
                    double d = dx * dx + dy * dy + (rnd.nextDouble() - 0.5) * 0.3;
                    if (d <= 1) {
                        int s = rnd.nextInt(100);
                        if (by < cy) s = Math.min(99, s + 25);
                        int c = s < 25 ? cop[0] : s < 60 ? cop[1] : s < 90 ? cop[2] : cop[3];
                        t.rect(bx, by, 2, 2, c);
                    }
                }
            }
        }

        // chão: calçada de concreto com juntas
        for (int y = 32; y < 48; y++) {
            t.rect(0, y, LARGURA, 1, mix(rgb(200, 194, 184), rgb(150, 142, 138), (y - 32) / 16.0));
        }
        for (int x = 0; x < LARGURA; x += 11) {
            t.rect(x, 32, 1, 16, rgb(168, 160, 154));
        }
        t.rect(0, 36, LARGURA, 1, rgb(172, 164, 158));
        t.rect(0, 41, LARGURA, 1, rgb(162, 154, 150));
        for (int y = 40; y < 45; y++) {
            for (int x = 40; x < 58; x++) {
                t.tint(x, y, rgb(60, 56, 80), 0.45);
            }
        }

        caio(t);
        // Caramelo atravessa o pátio andando até parar perto do Cáio
        caramelo(t, 20 - faltam * 2, faltam % 2);
        return t;
    }

    //  Abertura / cena 700 — "Cinco anos depois." sala de entrevista
    //  Paleta fria (azul-aço + vidro), oposta ao entardecer do menu.
    //  Animação: a luz fluorescente pisca até estabilizar; depois a caneta
    //  do candidato bate na mesa (nervosismo).
    static Tela abertura(int quadro) {
        double[] luzes = {0.15, 1.0, 0.25, 1.0, 0.45, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        double luz = luzes[Math.min(quadro, luzes.length - 1)];
        int caneta = (quadro >= 4 && quadro % 2 == 0) ? 1 : 0;   // quadro final (ímpar) = 0

        Tela t = new Tela(LARGURA, LINHAS);
        Random rnd = new Random(700);

        for (int y = 0; y < 37; y++) {
            t.rect(0, y, LARGURA, 1, mix(rgb(52, 64, 92), rgb(86, 100, 128), y / 37.0));
        }

        // janela com persiana, dusk lá fora
        t.rect(6, 5, 22, 21, rgb(30, 36, 56));
        int[] ysJ = {0, 8, 16, 20};
        int[][] csJ = {{70, 74, 130}, {150, 96, 150}, {236, 142, 110}, {250, 190, 120}};
        for (int y = 6; y < 25; y++) {
            t.rect(7, y, 20, 1, gradiente(y - 6, ysJ, csJ));
        }
        int[][] predios = {{8, 3, 6}, {12, 4, 9}, {17, 3, 5}, {21, 5, 8}};
        for (int[] p : predios) {
            t.rect(p[0], 24 - p[2], p[1], p[2], rgb(40, 40, 76));
            for (int yy = 24 - p[2] + 1; yy < 24; yy += 2) {
                if (rnd.nextDouble() < 0.6) t.px(p[0] + 1, yy, rgb(255, 214, 120));
            }
        }
        for (int y = 6; y < 25; y += 3) {
            t.rect(7, y, 20, 1, rgb(38, 44, 68));
        }
        t.rect(6, 5, 22, 1, rgb(110, 124, 150));
        t.rect(6, 25, 22, 1, rgb(110, 124, 150));
        t.rect(16, 5, 1, 21, rgb(110, 124, 150));

        // painel de luz fria no teto + brilho caindo na parede (escala com a luz)
        for (int y = 0; y < 12; y++) {
            for (int x = 24; x < 62; x++) {
                double f = Math.max(0.0, 0.30 - y * 0.025) * luz;
                if (f > 0) t.tint(x, y, rgb(220, 232, 255), f);
            }
        }
        t.rect(28, 0, 30, 2, mix(rgb(90, 98, 124), rgb(238, 244, 255), luz));
        t.rect(28, 2, 30, 1, mix(rgb(70, 78, 104), rgb(170, 186, 220), luz));

        // aviso na parede
        t.rect(52, 8, 9, 7, rgb(30, 34, 56));
        t.rect(53, 9, 7, 5, rgb(224, 222, 232));
        for (int yy : new int[]{10, 12}) {
            t.rect(54, yy, 5, 1, rgb(150, 150, 170));
        }

        // pessoa da banca (sem nome, rosto neutro, lendo o currículo)
        int pele = rgb(176, 122, 92), peleS = rgb(146, 98, 76);
        int cabelo = rgb(28, 22, 34), blazer = rgb(36, 42, 74);
        for (int y = 21; y < 36; y++) {
            double meia = 6 + (y - 21) * 0.7;
            t.rect((int) (39 - meia), y, (int) (meia * 2), 1, blazer);
        }
        t.rect(36, 21, 6, 3, rgb(232, 234, 244));
        t.rect(38, 21, 2, 3, rgb(200, 204, 220));
        t.rect(37, 18, 4, 4, peleS);
        t.elipse(39, 13, 5.2, 6.4, pele);
        t.elipse(40, 14, 4.6, 6.0, pele);
        for (int y = 9; y < 19; y++) t.px(43, y, peleS);
        t.elipse(39, 9, 6, 4.6, cabelo);
        t.rect(33, 9, 2, 5, cabelo);
        t.rect(35, 11, 7, 1, cabelo);
        t.px(34, 8, rgb(70, 62, 86));
        t.px(37, 6, rgb(70, 62, 86));
        t.rect(35, 13, 3, 2, rgb(20, 22, 40));
        t.rect(40, 13, 3, 2, rgb(20, 22, 40));
        t.px(36, 14, rgb(150, 190, 230));
        t.px(41, 14, rgb(150, 190, 230));
        t.rect(38, 13, 2, 1, rgb(20, 22, 40));
        t.rect(37, 17, 4, 1, rgb(110, 60, 60));
        t.px(39, 15, peleS);

        // notebook com adesivo de time (escudo verde/branco, genérico)
        t.rect(27, 25, 24, 12, rgb(158, 164, 182));
        t.rect(27, 25, 24, 1, rgb(206, 212, 228));
        t.rect(27, 36, 24, 1, rgb(96, 100, 124));
        t.rect(50, 25, 1, 12, rgb(122, 128, 150));
        t.rect(36, 28, 7, 6, rgb(24, 128, 70));
        t.rect(37, 34, 5, 1, rgb(24, 128, 70));
        t.rect(38, 35, 3, 1, rgb(24, 128, 70));
        t.rect(38, 28, 1, 6, rgb(246, 246, 240));
        t.rect(40, 28, 1, 6, rgb(246, 246, 240));
        t.rect(36, 28, 7, 1, rgb(246, 246, 240));
        t.px(39, 31, rgb(246, 220, 80));
        t.rect(24, 36, 4, 2, pele);
        t.rect(51, 36, 4, 2, pele);

        // mesa de vidro
        for (int y = 37; y < 48; y++) {
            t.rect(0, y, LARGURA, 1, mix(rgb(142, 188, 204), rgb(54, 84, 106), Math.min(1.0, (y - 37) / 8.0)));
        }
        t.rect(0, 37, LARGURA, 1, rgb(214, 238, 246));
        for (int i = 0; i < 9; i++) {
            t.tint(40 + i, 38 + i, rgb(240, 250, 255), 0.35 * luz);
            t.tint(41 + i, 38 + i, rgb(240, 250, 255), 0.20 * luz);
            t.tint(8 + i, 40 + i / 2, rgb(255, 170, 120), 0.22);
        }
        for (int y = 38; y < 42; y++) {
            for (int x = 28; x < 50; x++) {
                t.tint(x, y, rgb(158, 164, 182), 0.28 - (y - 38) * 0.06);
            }
        }

        // primeiro plano: currículo, caneta na mão, copo d'água
        t.rect(8, 40, 15, 7, rgb(244, 244, 250));
        t.rect(8, 40, 15, 1, rgb(206, 206, 222));
        t.rect(9, 41, 6, 1, rgb(50, 56, 90));
        for (int yy : new int[]{43, 44, 45}) {
            t.rect(9, yy, 12, 1, rgb(160, 164, 188));
        }
        t.rect(9, 44, 7, 1, rgb(214, 70, 78));
        t.rect(13, 45, 10, 1, rgb(160, 164, 188));
        t.rect(46, 42 - caneta, 9, 3, rgb(198, 142, 112));
        t.rect(46, 41 - caneta, 5, 1, rgb(198, 142, 112));
        t.rect(52, 40 - caneta, 1, 5, rgb(30, 30, 50));
        t.rect(42, 43, 5, 1, rgb(214, 214, 230));
        t.rect(58, 39, 4, 6, rgb(176, 214, 232));
        t.rect(58, 39, 1, 6, rgb(230, 246, 252));
        t.rect(59, 42, 3, 3, rgb(120, 170, 200));
        t.rect(58, 45, 4, 1, rgb(70, 110, 140));
        return t;
    }

    //  Cap. 1 / cena 0 — 5h47, SISU, "Nossa."
    //  Quarto às escuras com UMA fonte de luz: a tela.
    //  Animação: a tela liga (brilho sobe e a luz invade o quarto), o banner
    //  APROVADO(A) aparece e só então ela reage (sobrancelha, boca aberta).
    //  O ":" do relógio pisca durante tudo.
    static Tela cap1Matricula(int quadro) {
        double b = quadro < 2 ? 0.0 : Math.min(1.0, (quadro - 1) / 5.0);   // brilho da tela
        boolean banner = quadro >= 7;
        boolean texto = quadro >= 8;
        boolean reacao = quadro >= 8;
        boolean doisPontos = quadro % 2 == 1;   // quadro final (13) = visível

        Tela t = new Tela(LARGURA, LINHAS);
        Random rnd = new Random(547);

        for (int y = 0; y < 48; y++) {
            t.rect(0, y, LARGURA, 1, mix(rgb(10, 12, 32), rgb(22, 20, 44), y / 48.0));
        }

        // janela com a primeira luz da manhã
        t.rect(4, 4, 15, 13, rgb(60, 52, 90));
        int[] ysJ = {0, 6, 10};
        int[][] csJ = {{26, 30, 74}, {86, 62, 120}, {232, 120, 84}};
        for (int y = 5; y < 16; y++) {
            t.rect(5, y, 13, 1, gradiente(y - 5, ysJ, csJ));
        }
        t.rect(11, 4, 1, 13, rgb(60, 52, 90));
        t.rect(4, 10, 15, 1, rgb(60, 52, 90));
        t.px(7, 6, rgb(255, 250, 215));
        t.px(14, 7, rgb(255, 250, 215));
        t.px(9, 8, rgb(255, 250, 215));
        t.rect(3, 17, 17, 1, rgb(48, 44, 78));
        int[][] telhados = {{5, 4, 2}, {10, 5, 3}, {16, 2, 1}};
        for (int[] r : telhados) {
            t.rect(r[0], 15 - r[2], r[1], r[2], rgb(18, 18, 44));
        }

        // estante à direita
        t.rect(52, 6, 11, 17, rgb(24, 24, 50));
        for (int yy : new int[]{10, 15, 20}) {
            t.rect(52, yy, 11, 1, rgb(40, 40, 76));
        }
        int[] livros = {rgb(86, 56, 100), rgb(56, 86, 110), rgb(110, 80, 60), rgb(70, 100, 84)};
        for (int i = 0; i < livros.length; i++) {
            t.rect(54 + i * 2, 7, 2, 3, livros[i]);
            t.rect(54 + i * 2, 12, 2, 3, mix(livros[i], rgb(10, 10, 30), 0.4));
        }

        // relógio digital 5:47
        t.rect(55, 29, 9, 5, rgb(18, 16, 30));
        t.rect(55, 29, 9, 1, rgb(46, 42, 70));
        int vermelho = rgb(255, 70, 80);
        t.texto(56, 15, doisPontos ? "5:47" : "5 47", vermelho);

        // mesa
        t.rect(0, 36, LARGURA, 12, rgb(44, 30, 40));
        t.rect(0, 36, LARGURA, 1, rgb(92, 64, 70));
        for (int x = 0; x < LARGURA; x += 5) {
            t.px(x + rnd.nextInt(4), 38 + rnd.nextInt(7), rgb(60, 42, 52));
        }
        t.rect(3, 31, 5, 5, rgb(214, 214, 230));
        t.rect(3, 31, 1, 5, rgb(250, 250, 255));
        t.rect(8, 32, 1, 3, rgb(214, 214, 230));
        t.rect(12, 37, 6, 3, rgb(24, 24, 44));
        t.rect(12, 37, 6, 1, rgb(60, 60, 96));

        // notebook: tela que liga
        int apagado = rgb(20, 22, 44);
        t.rect(30, 12, 26, 20, rgb(40, 44, 70));
        t.rect(31, 13, 24, 18, mix(apagado, rgb(206, 226, 252), b));
        for (int y = 13; y < 31; y++) {
            for (int x = 31; x < 55; x++) {
                t.tint(x, y, rgb(255, 255, 255), 0.18 * (1 - (y - 13) / 18.0) * b);
            }
        }
        if (b > 0.3) {
            t.rect(31, 13, 24, 2, mix(apagado, rgb(70, 110, 196), b));
            t.rect(32, 13, 4, 1, mix(apagado, rgb(240, 240, 255), b));
            t.rect(32, 16, 22, 1, mix(apagado, rgb(140, 166, 210), b));
            t.rect(33, 26, 14, 1, mix(apagado, rgb(160, 176, 206), b));
            t.rect(33, 28, 9, 1, mix(apagado, rgb(160, 176, 206), b));
        }
        if (banner) {
            t.rect(33, 19, 20, 5, rgb(34, 160, 96));
            t.rect(33, 19, 20, 1, rgb(90, 214, 150));
            if (texto) t.texto(35, 10, "APROVADO(A)", rgb(255, 255, 255));
        }
        t.rect(28, 32, 30, 2, rgb(150, 156, 176));
        t.rect(28, 32, 30, 1, rgb(210, 214, 230));
        t.rect(29, 34, 28, 2, rgb(34, 34, 56));

        // halo da tela invadindo o quarto
        for (int y = 8; y < 48; y++) {
            for (int x = 18; x < 66; x++) {
                double dx = x - 43, dy = (y - 22) * 1.6;
                double d = Math.sqrt(dx * dx + dy * dy);
                double f = Math.max(0.0, 0.34 - d * 0.0105) * b;
                boolean naTela = x >= 30 && x < 56 && y >= 12 && y < 32;
                if (f > 0 && !naTela) t.tint(x, y, rgb(120, 170, 255), f);
            }
        }

        // protagonista de perfil (pele negra, cabelo liso), luz azul no rosto
        int pele = rgb(120, 76, 56);
        int peleLuz = mix(pele, rgb(156, 104, 80), b);
        peleLuz = mix(peleLuz, rgb(120, 170, 255), 0.20 * b);
        int cabelo = rgb(26, 20, 40);
        int cabLuz = mix(cabelo, rgb(70, 78, 140), b);
        int moletom = rgb(92, 62, 112);
        int molLuz = mix(moletom, rgb(130, 120, 190), b);
        for (int y = 27; y < 48; y++) {
            double meia = 7 + (y - 27) * 0.55;
            t.rect((int) (15 - meia), y, (int) (meia * 2 + 3), 1, moletom);
            t.px((int) (15 + meia + 2), y, molLuz);
            t.px((int) (15 + meia + 1), y, molLuz);
        }
        t.rect(6, 28, 7, 5, mix(moletom, rgb(10, 10, 30), 0.35));
        t.rect(17, 25, 5, 5, mix(pele, rgb(40, 30, 60), 0.35));
        t.elipse(21, 19, 6.6, 7.6, pele);
        t.rect(26, 17, 3, 6, pele);
        t.rect(28, 20, 1, 2, pele);
        for (int y = 13; y < 26; y++) {
            for (int x = 23; x < 30; x++) {
                if (t.get(x, y) == pele) t.px(x, y, peleLuz);
            }
        }
        t.rect(26, 24, 3, 1, peleLuz);
        if (reacao) {
            t.rect(25, 23, 3, 1, rgb(122, 52, 72));   // boca entreaberta (surpresa)
            t.rect(26, 24, 2, 1, rgb(66, 28, 48));
        } else {
            t.rect(25, 23, 3, 1, rgb(90, 40, 54));    // boca fechada
        }
        t.rect(19, 21, 3, 2, mix(pele, rgb(190, 100, 90), 0.30));
        // óculos: lente reflete o verde do APROVADO quando ele aparece
        t.rect(24, 17, 5, 3, rgb(18, 16, 32));
        t.rect(25, 18, 3, 1, banner ? rgb(150, 236, 196) : mix(rgb(30, 34, 70), rgb(150, 190, 255), b));
        t.px(27, 17, rgb(255, 255, 255));
        t.rect(17, 18, 7, 1, rgb(18, 16, 32));
        if (reacao) {
            t.rect(23, 14, 4, 1, rgb(46, 30, 50));    // sobrancelha erguida
            t.px(26, 15, rgb(46, 30, 50));
        } else {
            t.rect(23, 15, 4, 1, rgb(46, 30, 50));
        }
        t.rect(18, 19, 2, 3, mix(pele, rgb(40, 30, 60), 0.2));
        // cabelo liso e comprido
        t.elipse(19, 13, 7.6, 5.0, cabelo);
        t.rect(22, 11, 6, 3, cabelo);
        t.rect(25, 13, 3, 1, cabelo);
        t.rect(11, 13, 7, 18, cabelo);
        t.rect(12, 28, 6, 6, cabelo);
        t.rect(17, 9, 8, 1, cabLuz);
        t.rect(26, 11, 2, 2, cabLuz);
        t.rect(18, 25, 1, 5, cabelo);
        t.px(13, 22, rgb(48, 40, 74));
        t.px(14, 26, rgb(48, 40, 74));
        t.px(12, 18, rgb(48, 40, 74));
        // braço até o teclado
        t.rect(21, 33, 9, 3, moletom);
        t.rect(21, 33, 9, 1, molLuz);
        t.rect(29, 34, 3, 2, peleLuz);
        return t;
    }

    /** Cáio: corpo inteiro, de frente, copo térmico amassado na mão. */
    private static void caio(Tela t) {
        int pele = rgb(176, 120, 92), peleS = rgb(140, 92, 72);
        int cab = rgb(24, 20, 28);
        int mol = rgb(76, 84, 94), molS = rgb(52, 58, 68), molL = rgb(104, 112, 122);
        int calca = rgb(40, 44, 70);
        // pernas + tênis
        t.rect(46, 34, 4, 9, calca);
        t.rect(51, 34, 4, 9, calca);
        t.rect(45, 43, 6, 2, rgb(230, 230, 236));
        t.rect(51, 43, 6, 2, rgb(230, 230, 236));
        t.rect(45, 44, 6, 1, rgb(120, 120, 140));
        t.rect(51, 44, 6, 1, rgb(120, 120, 140));
        // moletom
        t.rect(44, 23, 13, 12, mol);
        t.rect(44, 23, 2, 12, molL);
        t.rect(55, 23, 2, 12, molS);
        t.rect(44, 33, 13, 2, molS);
        t.rect(48, 28, 5, 3, molS);
        // alças da mochila
        t.rect(47, 23, 1, 11, rgb(30, 30, 40));
        t.rect(53, 23, 1, 11, rgb(30, 30, 40));
        // braços
        t.rect(42, 24, 3, 11, mol);
        t.rect(42, 35, 3, 2, pele);
        t.rect(57, 24, 3, 7, mol);
        t.rect(57, 30, 3, 2, pele);
        // capuz, pescoço, cabeça
        t.rect(47, 21, 7, 3, molS);
        t.rect(48, 19, 5, 3, peleS);
        t.elipse(50.5, 15, 5, 6, pele);
        t.rect(53, 11, 2, 9, peleS);
        // cabelo cacheado bagunçado
        int[][] tufos = {{46, 8}, {48, 7}, {50, 6}, {52, 7}, {54, 8}, {45, 10}, {55, 10}, {46, 12}, {54, 12}};
        for (int[] b : tufos) {
            t.rect(b[0], b[1], 2, 2, cab);
        }
        t.rect(47, 9, 7, 3, cab);
        t.px(49, 7, rgb(64, 56, 78));
        t.px(52, 8, rgb(64, 56, 78));
        // rosto: pálpebra pesada, olheiras, sem sorriso
        t.rect(48, 14, 2, 1, rgb(236, 236, 244));
        t.rect(52, 14, 2, 1, rgb(236, 236, 244));
        t.px(49, 14, cab);
        t.px(53, 14, cab);
        t.rect(48, 13, 2, 1, cab);
        t.rect(52, 13, 2, 1, cab);
        t.rect(48, 15, 2, 1, rgb(112, 74, 74));
        t.rect(52, 15, 2, 1, rgb(112, 74, 74));
        t.px(51, 16, peleS);
        t.rect(49, 18, 4, 1, rgb(100, 56, 56));
        for (int x : new int[]{48, 50, 52, 49, 51}) {
            t.px(x, 19, rgb(100, 72, 62));
        }
        // copo térmico amassado (azul-petróleo, tampa preta)
        t.rect(58, 25, 4, 7, rgb(30, 120, 134));
        t.rect(58, 25, 1, 7, rgb(120, 214, 224));
        t.rect(59, 28, 3, 2, rgb(16, 70, 84));
        t.px(60, 27, rgb(16, 70, 84));
        t.px(61, 30, rgb(16, 70, 84));
        t.rect(58, 24, 4, 1, rgb(30, 28, 40));
        t.rect(57, 30, 3, 2, pele);
    }

    /** Caramelo caminhando (cachorro do DCE). x = onde começa o corpo; passo alterna as pernas. */
    private static void caramelo(Tela t, int x, int passo) {
        int car = rgb(204, 142, 70), carS = rgb(160, 100, 48), carL = rgb(236, 186, 110);
        int olho = rgb(30, 24, 30);
        int o = x - 20;   // deslocamento em relação à posição final
        t.rect(20 + o, 37, 12, 4, car);
        t.rect(20 + o, 37, 12, 1, carL);
        t.rect(20 + o, 40, 12, 1, carS);
        int escura = passo == 0 ? 3 : 2;   // pernas em diagonal: um par levanta, o outro apoia
        int clara = passo == 0 ? 2 : 3;
        t.rect(21 + o, 41, 2, escura, carS);
        t.rect(24 + o, 41, 2, clara, car);
        t.rect(28 + o, 41, 2, escura, carS);
        t.rect(31 + o, 41, 1, clara, car);
        t.rect(32 + o, 35, 5, 4, car);
        t.rect(36 + o, 37, 3, 2, carL);
        t.px(38 + o, 37, olho);
        t.rect(32 + o, 35, 2, 3, carS);
        t.px(35 + o, 36, olho);
        t.rect(18 + o, 35, 2, 3, car);
        t.px(17 + o, passo == 0 ? 34 : 35, carL);   // rabo balança
        for (int i = 19; i < 36; i++) {
            t.tint(i + o, 44, rgb(70, 64, 80), 0.5);
        }
    }
}