package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.adapter.CarregamentoAdapter;
import br.uefs.forkeazando.controller.DadosProtagonista;
import br.uefs.forkeazando.model.*;
import br.uefs.forkeazando.view.javafx.StatusNarrativo;
import br.uefs.forkeazando.view.pixel.Ilustracoes;
import br.uefs.forkeazando.view.pixel.PerfilProtagonista;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Auditoria de isolamento, paridade de regras, concordância narrativa,
 * identidade visual e persistência para Tiana (STHEFFANNY) e Miles (DAVI).
 */
class ProtagonistasAuditoriaTest {

    @TempDir
    Path pasta;

    private static Protagonista criar(String nome, IdentidadeProtagonista id) {
        return new DadosProtagonista(nome, id, true, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.POSITIVA,
                Protagonista.SituacaoEconomica.CONFORTAVEL
        ).criarProtagonista();
    }

    @Test
    @DisplayName("Tiana e Miles com mesma origem têm atributos idênticos e flags de identidade exclusivas")
    void paridadeDeRegrasEFlagsDeOrigemExclusivas() {
        Protagonista tiana = criar("Clara", IdentidadeProtagonista.STHEFFANNY);
        Protagonista miles = criar("Lucas", IdentidadeProtagonista.DAVI);
        // Nomes personalizados preservados sem alterar identidade
        assertEquals("Clara", tiana.getNome());
        assertEquals("Lucas", miles.getNome());
        assertEquals(PerfilProtagonista.TIANA, PerfilProtagonista.de(tiana));
        assertEquals(PerfilProtagonista.MILES, PerfilProtagonista.de(miles));

        // Flags exclusivas
        assertTrue(tiana.temFlag("protagonista_stheffanny"));
        assertFalse(tiana.temFlag("protagonista_masculino"));
        assertTrue(miles.temFlag("protagonista_masculino"));
        assertFalse(miles.temFlag("protagonista_stheffanny"));

        // Paridade exata das regras compartilhadas
        assertEquals(tiana.getEnergiaMaxima(), miles.getEnergiaMaxima());
        assertEquals(tiana.getEnergia(), miles.getEnergia());
        assertEquals(tiana.getConfianca(), miles.getConfianca());
        assertEquals(tiana.getVidaSocial(), miles.getVidaSocial());
        assertEquals(tiana.getCarisma(), miles.getCarisma());
        assertEquals(tiana.getLogica(), miles.getLogica());
        assertEquals(tiana.getSanidade(), miles.getSanidade());
    }

    @Test
    @DisplayName("Todas as 165 cenas expandem corretamente referências ao outro protagonista sem vazamento")
    void todasAsCenasRespeitamIdentidadeSemVazamento() {
        Protagonista tiana = criar("Tiana", IdentidadeProtagonista.STHEFFANNY);
        Protagonista miles = criar("Miles", IdentidadeProtagonista.DAVI);
        tiana.adicionarFlag("sistema_contestado");
        miles.adicionarFlag("sistema_contestado");
        tiana.adicionarFlag("cap7_questionou_autoria");
        miles.adicionarFlag("cap7_questionou_autoria");
        tiana.adicionarFlag("item_contato_bia");
        miles.adicionarFlag("item_contato_bia");

        List<Cena> cenas = Roteiro.todasAsCenas();
        assertEquals(165, cenas.size());

        int cenasComVariacao = 0;
        for (Cena cena : cenas) {
            String textoTiana = NarrativaDinamica.resolver(cena, tiana, 0);
            String textoMiles = NarrativaDinamica.resolver(cena, miles, 0);

            assertFalse(textoTiana.contains("{{"), "Placeholder não resolvido para Tiana na cena " + cena.getId());
            assertFalse(textoMiles.contains("{{"), "Placeholder não resolvido para Miles na cena " + cena.getId());

            if (cena.getTextoNarrativo().contains("{{NOME_OUTRO_PROTAGONISTA}}")) {
                assertTrue(textoTiana.contains("Miles"), "Tiana deveria referenciar Miles na cena " + cena.getId());
                assertTrue(textoMiles.contains("Tiana"), "Miles deveria referenciar Tiana na cena " + cena.getId());
            }
            if (cena.getTextoNarrativo().contains("{{OUTRO_CENA}}")) {
                cenasComVariacao++;
            }
        }
        assertTrue(cenasComVariacao >= 10, "Esperadas ao menos 10 cenas com {{OUTRO_CENA}}");

        // Concordância no recurso do orientador (cena 401)
        Cena cena401 = cenas.stream().filter(c -> c.getId() == 401).findFirst().orElseThrow();
        assertTrue(NarrativaDinamica.resolver(cena401, tiana, 0).contains("'Bem-vinda.'"));
        assertTrue(NarrativaDinamica.resolver(cena401, miles, 0).contains("'Bem-vindo.'"));
    }

    @Test
    @DisplayName("Epílogos, finais narrativos e status mantêm concordância de gênero para Tiana e Miles")
    void concordanciaEmEpilogosFinaisEStatus() {
        Protagonista tiana = criar("Tiana", IdentidadeProtagonista.STHEFFANNY);
        Protagonista miles = criar("Miles", IdentidadeProtagonista.DAVI);

        for (Protagonista p : List.of(tiana, miles)) {
            p.adicionarFlag("trilha_ic");
            p.adicionarFlag("cap7_questionou_autoria");
            p.adicionarFlag("cap7_dtec_ultima_aula");
            p.adicionarFlag("cap8_pacote_completo");
            p.adicionarFlag("item_contato_bia");
            p.adicionarFlag("hab_palavra_honesta");
            p.adicionarFlag("futuro_pesquisa_brasil");
            p.ganharScore(50);
            p.ganharParticipacao(40);
        }

        String epilogosMiles = String.join("\n", Epilogos.de(miles).stream().map(Epilogos.Epilogo::texto).toList());
        String epilogosTiana = String.join("\n", Epilogos.de(tiana).stream().map(Epilogos.Epilogo::texto).toList());

        assertTrue(epilogosMiles.contains("meu filho"));
        assertFalse(epilogosMiles.contains("minha filha"));
        assertTrue(epilogosMiles.contains("segundo autor"));
        assertFalse(epilogosMiles.contains("segunda autora"));
        assertTrue(epilogosMiles.contains("como monitor,"));
        assertFalse(epilogosMiles.contains("como monitora"));
        assertTrue(epilogosMiles.contains("estava certo"));
        assertTrue(epilogosMiles.contains("decidiram juntos"));

        assertTrue(epilogosTiana.contains("minha filha"));
        assertTrue(epilogosTiana.contains("segunda autora"));
        assertTrue(epilogosTiana.contains("como monitora"));
        assertTrue(epilogosTiana.contains("estava certa"));
        assertTrue(epilogosTiana.contains("decidiram juntas"));

        assertTrue(FinaisNarrativos.pesquisa(miles).contains("Segundo autor."));
        assertTrue(FinaisNarrativos.pesquisa(tiana).contains("Segunda autora."));
        assertTrue(FinaisNarrativos.independente(miles).contains("sozinho."));
        assertTrue(FinaisNarrativos.independente(tiana).contains("sozinha."));

        // Status narrativo neutro para ambos
        assertTrue(StatusNarrativo.marcos(miles).stream().noneMatch(m -> m.contains("honesta")));
        assertTrue(StatusNarrativo.marcos(tiana).stream().anyMatch(m -> m.contains("honestidade")));
    }

    @Test
    @DisplayName("Identidade personalizada de Tiana e Miles é preservada após salvar e carregar")
    void identidadePreservadaEmSaveELoad() throws Exception {
        CarregamentoAdapter adapter = new CarregamentoAdapter(pasta);

        Estado eTiana = new Estado();
        eTiana.iniciar(criar("Beatriz", IdentidadeProtagonista.STHEFFANNY));
        eTiana.setCenaAtualId(700);
        adapter.salvarSlot(eTiana, 1);

        Estado eMiles = new Estado();
        eMiles.iniciar(criar("Rafael", IdentidadeProtagonista.DAVI));
        eMiles.setCenaAtualId(700);
        adapter.salvarSlot(eMiles, 2);

        Estado lidoTiana = adapter.carregarSlot(1);
        Estado lidoMiles = adapter.carregarSlot(2);

        assertEquals("Beatriz", lidoTiana.getProtagonista().getNome());
        assertEquals(PerfilProtagonista.TIANA, PerfilProtagonista.de(lidoTiana.getProtagonista()));
        assertTrue(lidoTiana.getProtagonista().temFlag("protagonista_stheffanny"));

        assertEquals("Rafael", lidoMiles.getProtagonista().getNome());
        assertEquals(PerfilProtagonista.MILES, PerfilProtagonista.de(lidoMiles.getProtagonista()));
        assertTrue(lidoMiles.getProtagonista().temFlag("protagonista_masculino"));

        // Ilustrações variam entre Tiana e Miles sem falhar
        assertNotNull(Ilustracoes.animacao("cap1_matricula", PerfilProtagonista.TIANA));
        assertNotNull(Ilustracoes.animacao("cap1_matricula", PerfilProtagonista.MILES));
    }
}
