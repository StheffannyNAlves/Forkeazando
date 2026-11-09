package br.uefs.forkeazando.roteiro;

import static org.junit.jupiter.api.Assertions.*;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.roteiro.Epilogos.*;
import org.junit.jupiter.api.*;

class EpilogosTest {

    private Protagonista p;

    @BeforeEach
    void setUp() {
        p = new Protagonista("Teste", false, false, false,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
    }

    private void flags(String... fs) { for (String f : fs) p.adicionarFlag(f); }
    private java.util.List<String> titulos() {
        return Epilogos.de(p).stream().map(Epilogo::titulo).toList();
    }

    @Test
    void todaPartidaTemOsBlocosFixosEOCaminhoPorUltimo() {
        var t = titulos();
        assertTrue(t.contains("ZÉ DO MÓDULO 8"));
        assertTrue(t.contains("O QUE FICA"));
        assertTrue(t.contains("A PERGUNTA DO COMEÇO"));
        assertTrue(t.get(t.size() - 1).startsWith("SEU CAMINHO"));
    }

    @Test
    void nenhumBlocoTemTextoVazio() {
        flags("mentoria_caio", "caio_revelacao", "trilha_ic",
                "cap7_questionou_autoria", "cap8_foi_formatura", "futuro_tecnologia",
                "cap6_trancou", "hab_sobreviveu_greve", "sistema_chutou_ambiguo");
        for (Epilogo e : Epilogos.de(p)) {
            assertFalse(e.titulo().isBlank());
            assertFalse(e.texto().isBlank(), "bloco vazio: " + e.titulo());
        }
    }

    @Test
    void flagsMudamOsBlocos() {
        assertFalse(titulos().contains("CÁIO"));
        flags("mentoria_caio");
        assertTrue(titulos().contains("CÁIO"));
    }

    @Test
    void caminhoDefineUltimoBloco() {
        String[][] casos = {
                {"futuro_pesquisa_brasil", "SEU CAMINHO — PESQUISA NO BRASIL"},
                {"futuro_tecnologia", "SEU CAMINHO — TECNOLOGIA APLICADA"},
                {"futuro_internacional", "SEU CAMINHO — ALÉM DAS FRONTEIRAS"},
                {"futuro_independente", "SEU CAMINHO — CARREIRA INDEPENDENTE"},
                {"futuro_transicao", "SEU CAMINHO — RECOMEÇAR SEM APAGAR O PASSADO"},
        };
        for (String[] c : casos) {
            Protagonista q = new Protagonista("x", false, false, false,
                    Protagonista.NivelVidaSocial.EQUILIBRADO,
                    Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                    Protagonista.SituacaoEconomica.ESTAVEL);
            q.adicionarFlag(c[0]);
            var ts = Epilogos.de(q).stream().map(Epilogo::titulo).toList();
            assertEquals(c[1], ts.get(ts.size() - 1));
        }
    }

    @Test
    void bemEstarMudaQuemVoceVirou() {
        p.alterarAtributo("confianca", -p.getConfianca() + 1);
        p.alterarAtributo("sanidade", -p.getSanidade() + 1);
        assertEquals(BemEstarFinal.FRAGILIZADO, Epilogos.avaliarBemEstar(p));

        p.alterarAtributo("confianca", 8);
        p.alterarAtributo("sanidade", 8);
        p.alterarAtributo("vidaSocial", 8);
        assertEquals(BemEstarFinal.ESTAVEL, Epilogos.avaliarBemEstar(p));
    }

    @Test
    void desempenhoAcademicoSegueOsLimites() {
        p.ganharScore(29 - p.getScore());
        p.ganharParticipacao(29 - p.getParticipacao());
        assertEquals(DesempenhoAcademico.DIFICULDADE, Epilogos.avaliarDesempenhoAcademico(p));

        p.ganharScore(70 - p.getScore());
        p.ganharParticipacao(50 - p.getParticipacao());
        assertEquals(DesempenhoAcademico.DESTAQUE, Epilogos.avaliarDesempenhoAcademico(p));
    }

    @Test
    void mesmaEntradaMesmosEpilogos() {
        flags("trilha_dtec", "cap6_pediu_ajuda");
        assertEquals(Epilogos.de(p), Epilogos.de(p));
    }

    @Test
    void sistemaChutouAmbiguoProduzBlocoProprio() {
        flags("sistema_chutou_ambiguo");
        var t = titulos();
        assertTrue(t.stream().anyMatch(s -> s.equals("O QUE O ÁGATA NÃO ENTENDEU")),
                "esperado bloco do ÁGATA pros ambíguos");
    }
}