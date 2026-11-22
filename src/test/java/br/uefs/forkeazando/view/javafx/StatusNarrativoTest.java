package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Protagonista;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatusNarrativoTest {
    private static Protagonista protagonista() {
        return new Protagonista("Tiana", IdentidadeProtagonista.STHEFFANNY,
                false, false, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
    }

    @Test void naoExibeFlagsTecnicasNemDadosDeOrigemComoDecisoes() {
        Protagonista p = protagonista();
        p.adicionarFlag("protagonista_stheffanny");
        p.adicionarFlag("perfil_teorico");
        p.adicionarFlag("cap3_colou_de_fato");
        p.adicionarFlag("flag_interna_desconhecida");
        assertEquals(List.of("Recorreu à cola durante uma avaliação."), StatusNarrativo.marcos(p));
    }

    @Test void preservaOrdemDosMarcosSemDuplicar() {
        Protagonista p = protagonista();
        p.adicionarFlag("cap4_leu_editais");
        p.adicionarFlag("mentoria_caio");
        p.adicionarFlag("cap4_leu_editais");
        assertEquals(2, StatusNarrativo.marcos(p).size());
        assertTrue(StatusNarrativo.marcos(p).getFirst().contains("editais"));
    }

    @Test void objetivoConsideraPrologoEApagao() {
        Estado estado = new Estado();
        estado.iniciar(protagonista());
        estado.setCenaAtualId(700);
        assertTrue(StatusNarrativo.objetivo(estado).contains("começou"));
        estado.setCenaAtualId(Estado.CENA_APAGAO);
        assertTrue(StatusNarrativo.objetivo(estado).contains("esgotamento"));
    }

    @Test void objetivoSegueCapituloSemAlterarEstado() {
        Estado estado = new Estado();
        estado.iniciar(protagonista());
        estado.setCenaAtualId(1);
        estado.avancarCapitulo();
        estado.avancarCapitulo();
        assertTrue(StatusNarrativo.objetivo(estado).contains("avaliação"));
        assertEquals(3, estado.getCapituloAtual());
    }
}
