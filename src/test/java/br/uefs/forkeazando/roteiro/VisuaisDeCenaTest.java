package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.view.pixel.Ilustracoes;
import br.uefs.forkeazando.view.pixel.Retratos;
import br.uefs.forkeazando.model.Final;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
// todos passam
import static org.junit.jupiter.api.Assertions.*;

class VisuaisDeCenaTest {

    private static Map<Integer, Cena> cenasPorId() {
        Map<Integer, Cena> m = new HashMap<>();
        for (Cena c: Roteiro.cenasDosCapitulos()) {
             m.put(c.getId(), c);
        }
        return m;
    }

    @Test
    void todaCenaMapeadaExisteNoRoteiro() {
        Map<Integer, Cena> cenas = cenasPorId();
        for (int id : VisuaisDeCena.todas().keySet()) {
            assertTrue(cenas.containsKey(id), "cena " + id + " não existe no roteiro");
        }
    }

    @Test
    void todaChaveMapeadaTemArteERetratoCadastrados() {
        for (Map.Entry<Integer, ApresentacaoDeCena> e : VisuaisDeCena.todas().entrySet()) {
            ApresentacaoDeCena v = e.getValue();
            if (v.ilustracao() != null) {
                assertTrue(Ilustracoes.existe(v.ilustracao()), "cena " + e.getKey() + ": ilustração inexistente");
            }
            if (v.falante() != null) {
                assertTrue(Retratos.existe(v.falante()), "cena " + e.getKey() + ": retrato inexistente");
                assertTrue(Retratos.linhas(v.falante(), v.expressao()).isPresent());
            }
        }
    }

    @Test
    void retratoSoApareceEmCenasOndeAqueleFalanteRealmenteFala() {
        Map<Integer, Cena> cenas = cenasPorId();
        for (Map.Entry<Integer, ApresentacaoDeCena> e : VisuaisDeCena.todas().entrySet()) {
            String chave = e.getValue().falante();
            if (chave != null) {
                String falante = cenas.get(e.getKey()).getPersonagemFalando();
                assertTrue(falante.toLowerCase().startsWith(Retratos.nomeDe(chave).toLowerCase()),
                        "cena " + e.getKey() + " mostra o retrato de '" + chave + "' mas quem fala é " + falante);
            }
        }
    }

    @Test
    void cenaMapeadaComFalanteNaoNarradorSempreTemRetrato() {
        Map<Integer, Cena> cenas = cenasPorId();
        for (Map.Entry<Integer, ApresentacaoDeCena> e : VisuaisDeCena.todas().entrySet()) {
            String falante = cenas.get(e.getKey()).getPersonagemFalando();
            if (!falante.equalsIgnoreCase("Narrador")) {
                assertNotNull(e.getValue().falante(), "cena " + e.getKey() + ": " + falante + " fala sem retrato");
            }
        }
    }

    @Test
    void todoPersonagemComRetratoApareceEmAoMenosUmaCena() {
        java.util.Set<String> usados = new java.util.HashSet<>();
        for (ApresentacaoDeCena v : VisuaisDeCena.todas().values()) {
            if (v.falante() != null) usados.add(v.falante());
        }
        for (String p : List.of("caio", "sergio", "rafael", "bia", "dandara", "ze", "sistema")) {
            assertTrue(usados.contains(p), "retrato '" + p + "' nunca é usado");
        }
    }

    @Test
    void todaIlustracaoCadastradaEUsadaEmAlgumaCena() {
        java.util.Set<String> usadas = new java.util.HashSet<>();
        for (ApresentacaoDeCena v : VisuaisDeCena.todas().values()) {
            if (v.ilustracao() != null) usadas.add(v.ilustracao());
        }
        for (Final f: Final.values()){
            usadas.add(VisuaisDeFinal.ilustracaoDe(f));
        }
        usadas.add(VisuaisDeFinal.ILUSTRACAO_PADRAO);
        for (String chave : Ilustracoes.chaves()) {
            assertTrue(usadas.contains(chave), "ilustração '" + chave + "' nunca é usada");
        }
    }

    @Test
    void cenaSemEntradaFicaSoComTexto() {
        assertTrue(VisuaisDeCena.paraCena(1).isEmpty());
        assertTrue(VisuaisDeCena.paraCena(-5).isEmpty());
    }

    @Test
    void patioContinuaNaTelaQuandoOCaioFala() {
        String patio = VisuaisDeCena.paraCena(3).orElseThrow().ilustracao();
        for (int id : new int[]{4, 71, 72, 73}) {
            assertEquals(patio, VisuaisDeCena.paraCena(id).orElseThrow().ilustracao());
        }
    }

    @Test
    void mapaCobreOsOitoCapitulosEOPrologo() {
        for (int id : new int[]{700, 0, 101, 200, 300, 400, 451, 500, 550, 600, 650}) {
            assertTrue(VisuaisDeCena.paraCena(id).isPresent(), "sem visual para a cena " + id);
        }
    }
}