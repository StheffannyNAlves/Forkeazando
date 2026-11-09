package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Final;
import br.uefs.forkeazando.model.Protagonista;

import java.util.Optional;

/**
 * Decide qual ilustração e qual título representam o final alcançado. A regra de QUAL final é
 * continua em {@link Final#identificar}; aqui só se traduz o resultado em chaves de apresentação.
 */
public final class VisuaisDeFinal {

    /** Quando a partida conclui sem uma trilha identificável, usa o portão da formatura. */
    public static final String ILUSTRACAO_PADRAO = "cap8_portao";
    public static final String TITULO_PADRAO = "Formatura";

    private VisuaisDeFinal() {}

    public static String ilustracaoDe(Protagonista p) {
        return Final.identificar(p.getHistoricoFlags())
                .map(VisuaisDeFinal::ilustracaoDe)
                .orElse(ILUSTRACAO_PADRAO);
    }

    public static String ilustracaoDe(Final f) {
        return switch (f) {
            case PESQUISA_BRASIL -> "futuro_pesquisa_brasil";
            case TECNOLOGIA -> "futuro_tecnologia";
            case INTERNACIONAL -> "futuro_internacional";
            case CAMINHO_PROPRIO -> "futuro_independente";
            case TRANSICAO -> "futuro_transicao";
            case IC -> "final_ic";
            case DTEC -> "final_dtec";
            case INDEPENDENTE -> "final_independente";
        };
    }

    public static String tituloDe(Protagonista p) {
        Optional<Final> f = Final.identificar(p.getHistoricoFlags());
        return f.map(Final::getTitulo).orElse(TITULO_PADRAO);
    }
}