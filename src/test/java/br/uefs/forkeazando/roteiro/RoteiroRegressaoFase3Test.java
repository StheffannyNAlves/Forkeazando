package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Protagonista;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class RoteiroRegressaoFase3Test {
    private static Map<Integer, Cena> cenas() {
        return Roteiro.todasAsCenas().stream()
                .collect(Collectors.toMap(Cena::getId, Function.identity()));
    }

    private static Protagonista personagem(IdentidadeProtagonista identidade) {
        return new Protagonista(identidade == IdentidadeProtagonista.STHEFFANNY ? "Tiana" : "Miles",
                identidade, true, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.POSITIVA,
                Protagonista.SituacaoEconomica.CONFORTAVEL);
    }

    private static Escolha escolha(Cena cena, String trecho) {
        return cena.getEscolhas().stream()
                .filter(e -> e.getTextoAlternativa().contains(trecho))
                .findFirst().orElseThrow(() -> new AssertionError(
                        "Escolha desapareceu na cena " + cena.getId() + ": " + trecho));
    }

    @Test
    void prologoPreservaBifurcacaoDeIdentidade() {
        var roteiro = cenas();
        Protagonista tiana = personagem(IdentidadeProtagonista.STHEFFANNY);
        Protagonista miles = personagem(IdentidadeProtagonista.DAVI);
        assertEquals(1, roteiro.get(701).getEscolhasDisponiveis(tiana).size());
        assertEquals(1, roteiro.get(701).getEscolhasDisponiveis(miles).size());
        assertEquals(10300, roteiro.get(701).getEscolhasDisponiveis(tiana).get(0).getCenaDestinoId());
        assertEquals(10301, roteiro.get(701).getEscolhasDisponiveis(miles).get(0).getCenaDestinoId());
        assertEquals(0, roteiro.get(10300).getEscolhas().get(0).getCenaDestinoId());
        assertEquals(0, roteiro.get(10301).getEscolhas().get(0).getCenaDestinoId());
    }

    @Test
    void escolhasDoPblDesbloqueiamConsequenciasEmCapitulosPosteriores() {
        var roteiro = cenas();
        Protagonista tiana = personagem(IdentidadeProtagonista.STHEFFANNY);
        Protagonista miles = personagem(IdentidadeProtagonista.DAVI);
        Escolha tianaPbl = escolha(roteiro.get(6), "Definir critérios de navegação");
        Escolha milesPbl = escolha(roteiro.get(6), "Prototipar um caminho do menu");
        assertTrue(tianaPbl.estaDisponivel(tiana));
        assertFalse(tianaPbl.estaDisponivel(miles));
        assertTrue(milesPbl.estaDisponivel(miles));
        assertFalse(milesPbl.estaDisponivel(tiana));
        assertTrue(tianaPbl.getFlagsConcedidas().contains("cap1_pbl_planejou_testes"));
        assertTrue(milesPbl.getFlagsConcedidas().contains("cap1_pbl_prototipo_revisado"));
        tiana.adicionarFlag("cap1_pbl_planejou_testes");
        miles.adicionarFlag("cap1_pbl_prototipo_revisado");
        assertTrue(escolha(roteiro.get(109), "Usar os critérios de teste do PBL").estaDisponivel(tiana));
        assertTrue(escolha(roteiro.get(109), "Mostrar como a revisão de um protótipo").estaDisponivel(miles));
    }

    @Test
    void entrevistaFinalPreservaRespostasCondicionais() {
        var roteiro = cenas();
        assertTrue(escolha(roteiro.get(654), "Aprendi a planejar testes")
                .getFlagsConcedidas().contains("cap8_resposta_aprendizado_metodo"));
        assertTrue(escolha(roteiro.get(654), "Passei a registrar os meus erros")
                .getFlagsConcedidas().contains("cap8_resposta_metodo_construido"));
        assertTrue(escolha(roteiro.get(455), "Dividir as mensagens")
                .getFlagsConcedidas().contains("cap5b_assumiu_aviso_escola"));
    }

    @Test
    void consequenciaEhCarregadaENarrativaDoOutroProtagonistaEhExpandida() {
        var roteiro = cenas();
        Escolha escolha = roteiro.get(700).getEscolhas().get(1);
        assertNotNull(escolha.getTextoConsequencia());
        assertTrue(escolha.getTextoConsequencia().contains("Minha primeira explicação estava errada"));        Protagonista tiana = personagem(IdentidadeProtagonista.STHEFFANNY);
        Protagonista miles = personagem(IdentidadeProtagonista.DAVI);
        String textoTiana = NarrativaProtagonistas.resolver(roteiro.get(6), tiana);
        String textoMiles = NarrativaProtagonistas.resolver(roteiro.get(6), miles);
        assertTrue(textoTiana.contains("Miles"));
        assertTrue(textoMiles.contains("Tiana"));
        assertFalse(textoTiana.contains("{{OUTRO_CENA}}"));
        assertFalse(textoMiles.contains("{{NOME_OUTRO_PROTAGONISTA}}"));
    }
}
