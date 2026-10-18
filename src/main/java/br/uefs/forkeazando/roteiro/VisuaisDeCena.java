package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.view.pixel.Ilustracoes;

import java.util.Map;
import java.util.Optional;

/**
 * Tabela cena -> apresentação visual. Vive ao lado do roteiro, mas fora das
 * classes de cena: o texto, as escolhas e os saves não dependem de imagens.
 * Cena sem entrada aqui é exibida só com texto, como antes.
 */
public final class VisuaisDeCena {

    private VisuaisDeCena() {}

    private static final Map<Integer, ApresentacaoDeCena> MAPA = Map.of(
            // Prólogo — entrevista, "Cinco anos depois" (700 e 701 compartilham a imagem)
            700, ApresentacaoDeCena.soIlustracao("abertura_entrevista"),
            701, ApresentacaoDeCena.soIlustracao("abertura_entrevista"),
            // Capítulo 1 — 5h47, aprovação no SISU
            0, ApresentacaoDeCena.soIlustracao("cap1_matricula"),
            // Capítulo 1 — oficina vazia, LED apagado
            8, ApresentacaoDeCena.soIlustracao(
                    Ilustracoes.CAP1_OFICINA_VAZIA
            ),

            // Capítulo 1 — Cáio aparece na porta; o LED acende
            9, ApresentacaoDeCena.soIlustracao(
                    Ilustracoes.CAP1_OFICINA_LED
            ),
            // Capítulo 1 — pátio da SIECOMP: Caramelo passa, Cáio olha
            3, ApresentacaoDeCena.soIlustracao("cap1_patio"),
            // Cáio fala; a ilustração do pátio continua na tela
            4, ApresentacaoDeCena.comRetrato("cap1_patio", "caio", "neutra"),
            71, ApresentacaoDeCena.comRetrato("cap1_patio", "caio", "neutra"),
            72, ApresentacaoDeCena.comRetrato("cap1_patio", "caio", "torto"),   // "É o primeiro sorriso que você vê nele"
            73, ApresentacaoDeCena.comRetrato("cap1_patio", "caio", "lado")     // "Ele para. O copo fica no ar."
    );

    public static Optional<ApresentacaoDeCena> paraCena(int cenaId) {
        return Optional.ofNullable(MAPA.get(cenaId));
    }

    public static Map<Integer, ApresentacaoDeCena> todas() {
        return MAPA;
    }
}