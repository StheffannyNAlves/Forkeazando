package br.uefs.forkeazando.roteiro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Tabela cena -> apresentação visual. Vive ao lado do roteiro, mas fora das
 * classes de cena: o texto, as escolhas e os saves não dependem de imagens.
 * Todas as cenas atuais possuem ambiente; IDs desconhecidos continuam opcionais.
 *
 * A ilustração só é (re)desenhada quando a chave muda em relação à cena anterior,
 * então várias cenas seguidas no mesmo lugar compartilham a mesma chave.
 * Ilustração null = mantém a atual; só o retrato muda.
 */
public final class VisuaisDeCena {

    private static final Map<Integer, ApresentacaoDeCena> MAPA = new HashMap<>();

    private VisuaisDeCena() {}

    private static void arte(String ilustracao, int... cenas) {
        for (int id : cenas) MAPA.put(id, ApresentacaoDeCena.soIlustracao(ilustracao));
    }

    private static void fala(int cena, String ilustracao, String falante, String expressao) {
        MAPA.put(cena, ApresentacaoDeCena.comRetrato(ilustracao, falante, expressao));
    }

    static {
        arte("abertura_entrevista", 700, 701);

        arte("cap1_matricula", 0);
        arte("cap1_ru", 2);
        arte("cap1_patio", 3);
        fala(4, "cap1_patio", "caio", "neutra");
        fala(71, "cap1_patio", "caio", "neutra");
        fala(72, "cap1_patio", "caio", "torto");    // "É o primeiro sorriso que você vê nele"
        fala(73, "cap1_patio", "caio", "lado");     // "Ele para. O copo fica no ar."
        fala(5, "cap1_oficina", "rafael", "seria");
        arte("cap1_pbl", 6, 16);
        arte("cap1_oficina_vazia", 8);
        arte("cap1_oficina_led", 9);
//        fala(10, null, "sergio", "neutra");
//        arte("sala_grupo", 11);
//        fala(12, "sala_grupo", "sergio", "seria");
        arte("cap1_modulo8", 13);
        arte("cap1_onibus", 14);
        fala(15, "cap1_modulo8", "ze", "feliz");

        arte("cap2_mural", 101);
        arte("cap2_assembleia", 105, 106);
        arte("cap1_ru", 107);
        fala(112, "cap1_patio", "caio", "neutra");
        fala(113, "cap1_patio", "caio", "lado");
        fala(114, "cap1_patio", "caio", "neutra");
        arte("cap2_feira", 116, 120, 121, 122, 123);

        arte("cap3_prova", 200, 201, 202, 204, 205, 207);
        fala(208, null, "caio", "neutra");
        arte("cap3_notas", 210);
        fala(211, "cap3_notas", "dandara", "triste");
        fala(213, null, "rafael", "seria");

        arte("cap4_editais", 300);
        fala(301, "cap4_editais", "caio", "neutra");
        fala(303, "sala_grupo", "sergio", "neutra");
        fala(304, "cap1_oficina", "rafael", "seria");
        for (int id = 3045; id <= 3049; id++) fala(id, "cap4_agata", "sistema", "neutra");
        fala(3050, "cap4_agata", "sistema", "processando");
        arte("cap4_prazo", 306, 371, 372, 373);

        arte("sala_grupo", 400, 402);
        fala(403, "sala_grupo", "bia", "neutra");
        fala(415, "sala_grupo", "sergio", "neutra");
        arte("cap5_greve", 451, 460);
        fala(453, null, "bia", "seria");
        fala(455, "cap1_ru", "dandara", "triste");
        fala(470, "cap1_oficina", "rafael", "neutra");
        arte("cap5_freela", 500, 501, 503, 505);
        fala(502, "cap5_freela", "bia", "neutra");

        arte("cap6_quarto", 550, 551, 552, 554);
        fala(553, "cap6_quarto", "caio", "neutra");
        fala(555, "cap6_ru", "bia", "neutra");
        fala(556, "cap6_ru", "bia", "neutra");
        fala(557, "cap6_ru", "caio", "neutra");
        fala(558, "cap6_ru", "caio", "lado");
        fala(559, "cap6_ru", "caio", "torto");
        fala(561, "sala_grupo", "sergio", "neutra");
        fala(562, "cap1_oficina", "rafael", "neutra");
        fala(563, null, "ze", "feliz");

        arte("cap7_tcc", 600);
        fala(601, null, "sergio", "neutra");
        arte("cap7_defesa", 605);

        arte("cap8_formatura", 650, 653, 656);
        fala(651, "cap8_formatura", "caio", "torto");
        fala(652, "cap8_formatura", "dandara", "feliz");
        arte("abertura_entrevista", 654);           // "A mesma sala. Mesa de vidro."
        arte("cap8_portao", 655);
        arte("futuro_pesquisa_brasil", 662);
        arte("futuro_tecnologia", 663);
        arte("futuro_internacional", 664);
        arte("futuro_independente", 665);
        arte("futuro_transicao", 666);
        // Cada cena possui ambiente explícito, inclusive ao carregar um save diretamente nela.
        arte("caderno_inicio", 10300, 10301, 212, 214, 215, 216, 217, 218, 219, 3001, 3002, 540, 541, 542, 543, 567);
        arte("cap1_matricula", 1);
        arte("cap1_pbl", 7, 102, 109);
        arte("oficina_extensao", 103);
        arte("auditorio_campus", 108);
        arte("cap2_mural", 111);
        arte("cap1_onibus", 100, 115, 209);
        fala(124, "cap2_mural", "bia", "neutra");
        fala(117, "cap1_ru", "caio", "neutra");
        fala(118, "cap1_ru", "dandara", "neutra");
        fala(208, "cap1_patio", "caio", "neutra");
        fala(213, "cap3_prova", "rafael", "seria");
        arte("cap3_notas", 2105, 2107);
        fala(3011, "cap4_editais", "caio", "neutra");
        fala(3012, "cap4_editais", "caio", "neutra");
        fala(3013, "cap4_editais", "caio", "neutra");
        arte("secretaria", 302, 374, 565, 649);
        arte("cap4_agata", 3051, 3052);
        arte("cap4_prazo", 305);
        arte("sala_grupo", 401, 405, 410, 416, 417, 418);
        fala(4101, "sala_grupo", "sergio", "neutra");
        arte("cap1_oficina", 450, 471);
        arte("cap1_patio", 452);
        fala(453, "cap5_greve", "bia", "seria");
        arte("cap5_freela", 506);
        fala(5576, "ponto_onibus", "caio", "neutra");
        fala(5591, "ponto_onibus", "caio", "neutra");
        fala(5592, "ponto_onibus", "caio", "neutra");
        arte("ponto_onibus", 560, 564, 661);
        fala(563, "cap5_freela", "ze", "feliz");
        fala(5631, "cap5_freela", "ze", "feliz");
        arte("cap7_tcc", 599, 667);
        fala(601, "sala_grupo", "sergio", "neutra");
        fala(6011, "sala_grupo", "sergio", "neutra");
        fala(6012, "sala_grupo", "sergio", "neutra");
        arte("cap1_oficina_led", 602);
        arte("cap1_oficina", 6022);
        arte("futuro_independente", 603);
        arte("celebracao_casa", 656);
        arte("memoria_bancada", 660);
        arte("corredor_apoio", 970, 971, 972);
        fala(541, "caderno_inicio", "sergio", "neutra");
        fala(3050, "cap4_agata_resultado", "sistema", "processando");
        arte("cap4_agata_resultado", 3051, 3052);
    }

    public static Optional<ApresentacaoDeCena> paraCena(int cenaId) {
        return Optional.ofNullable(MAPA.get(cenaId));
    }

    public static Map<Integer, ApresentacaoDeCena> todas() {
        return java.util.Collections.unmodifiableMap(MAPA);
    }
}
