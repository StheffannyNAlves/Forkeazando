package br.uefs.forkeazando.roteiro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Tabela cena -> apresentação visual. Vive ao lado do roteiro, mas fora das
 * classes de cena: o texto, as escolhas e os saves não dependem de imagens.
 * Cena sem entrada aqui é exibida só com texto, como antes.
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
        // ---------------- Prólogo ----------------
        arte("abertura_entrevista", 700, 701);

        // ---------------- Capítulo 1 ----------------
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
        arte("cap1_oficina_led", 9);                // o LED acende; Cáio aparece na porta
        fala(10, null, "sergio", "neutra");
        arte("sala_grupo", 11);
        fala(12, "sala_grupo", "sergio", "seria");
        arte("cap1_modulo8", 13);
        arte("cap1_onibus", 14);
        fala(15, "cap1_modulo8", "ze", "feliz");

        // ---------------- Capítulo 2 ----------------
        arte("cap2_mural", 101);
        arte("cap2_assembleia", 105, 106);
        arte("cap1_ru", 107);
        fala(112, "cap1_patio", "caio", "neutra");
        fala(113, "cap1_patio", "caio", "lado");
        fala(114, "cap1_patio", "caio", "neutra");
        arte("cap2_feira", 116, 118, 120, 121, 122, 123);

        // ---------------- Capítulo 3 ----------------
        arte("cap3_prova", 200, 201, 202, 204, 205, 207);
        fala(208, null, "caio", "neutra");
        arte("cap3_notas", 210);
        fala(211, "cap3_notas", "dandara", "triste");
        fala(213, null, "rafael", "seria");

        // ---------------- Capítulo 4 ----------------
        arte("cap4_editais", 300);
        fala(301, "cap4_editais", "caio", "neutra");
        fala(303, "sala_grupo", "sergio", "neutra");
        fala(304, "cap1_oficina", "rafael", "seria");
        for (int id = 3045; id <= 3049; id++) fala(id, "cap4_agata", "sistema", "neutra");
        fala(3050, "cap4_agata", "sistema", "processando");
        arte("cap4_prazo", 306, 371, 372, 373);

        // ---------------- Capítulo 5 ----------------
        arte("sala_grupo", 400, 402);
        fala(403, "sala_grupo", "bia", "neutra");
        fala(415, "sala_grupo", "sergio", "neutra");
        arte("cap5_greve", 451, 460);
        fala(453, null, "bia", "seria");
        fala(455, "cap1_ru", "dandara", "triste");
        arte("cap5_freela", 500, 501, 503, 505);
        fala(502, "cap5_freela", "bia", "neutra");

        // ---------------- Capítulo 6 ----------------
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

        // ---------------- Capítulo 7 ----------------
        arte("cap7_tcc", 600);
        fala(601, null, "sergio", "neutra");
        arte("cap7_defesa", 605);

        // ---------------- Capítulo 8 ----------------
        arte("cap8_formatura", 650, 653);
        fala(651, "cap8_formatura", "caio", "torto");
        fala(652, "cap8_formatura", "dandara", "feliz");
        arte("abertura_entrevista", 654);           // "A mesma sala. Mesa de vidro."
        arte("cap8_portao", 655);
    }

    public static Optional<ApresentacaoDeCena> paraCena(int cenaId) {
        return Optional.ofNullable(MAPA.get(cenaId));
    }

    public static Map<Integer, ApresentacaoDeCena> todas() {
        return java.util.Collections.unmodifiableMap(MAPA);
    }
}