package br.uefs.forkeazando.roteiro;

import static org.junit.jupiter.api.Assertions.*;

import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.roteiro.SistemaAcademico.Perfil;
import org.junit.jupiter.api.Test;

class SistemaAcademicoTest {

    private static Protagonista respostas(String q1, String q2, String q3, String q4) {
        Protagonista p = new Protagonista("Teste", false, false, false,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
        p.adicionarFlag("sistema_q1_" + q1);
        p.adicionarFlag("sistema_q2_" + q2);
        p.adicionarFlag("sistema_q3_" + q3);
        p.adicionarFlag("sistema_q4_" + q4);
        return p;
    }

    @Test
    void perfilIC() {
        assertEquals(Perfil.IC, SistemaAcademico.classificar(
                respostas("estudar", "sozinha", "visivel", "planejar")));
    }

    @Test
    void perfilDTEC() {
        assertEquals(Perfil.DTEC, SistemaAcademico.classificar(
                respostas("mexer", "grupo", "visivel", "improvisar")));
    }

    @Test
    void perfilINDEP() {
        assertEquals(Perfil.INDEP, SistemaAcademico.classificar(
                respostas("mexer", "sozinha", "invisivel", "improvisar")));
    }

    @Test
    void determinismoClassificacao() {
        Protagonista p = respostas("estudar", "grupo", "invisivel", "improvisar");
        Perfil primeira = SistemaAcademico.classificar(p);
        for (int i = 0; i < 100; i++) {
            assertEquals(primeira, SistemaAcademico.classificar(p));
        }
    }

    @Test
    void chuteAdicionaFlagIdempotente() {
        Protagonista p = respostas("estudar", "sozinha", "visivel", "planejar");
        SistemaAcademico.chute(p);
        SistemaAcademico.chute(p);   // segunda chamada não duplica
        long qtd = p.getHistoricoFlags().stream()
                .filter(f -> f.startsWith("sistema_chutou_"))
                .count();
        assertEquals(1, qtd, "chute() deve ser idempotente, mas gerou " + qtd + " flags");
    }
}