package br.uefs.forkeazando.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProgressoTest {

    private static final LocalDateTime T1 = LocalDateTime.of(2026, 10, 9, 22, 10);
    private static final LocalDateTime T2 = LocalDateTime.of(2026, 10, 10, 8, 0);
    private Protagonista criarProtagonista() {
        return new Protagonista(
                "Teste",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        );
    }

    @Test
    void progressoNovoNaoTemNenhumFinal() {
        Progresso p = new Progresso();

        assertTrue(p.finaisAlcancados().isEmpty());
        assertFalse(p.alcancou(Final.IC));
    }

    @Test
    void primeiroRegistroDevolveTrueESegundoDevolveFalse() {
        Progresso p = new Progresso();

        assertTrue(p.registrarFinal(Final.IC, T1));
        assertFalse(p.registrarFinal(Final.IC, T2));
    }

    @Test
    void registrarDeNovoNaoAlteraAData() {
        Progresso p = new Progresso();
        p.registrarFinal(Final.IC, T1);
        p.registrarFinal(Final.IC, T2);

        assertEquals(Optional.of(T1), p.quandoAlcancou(Final.IC));
    }

    @Test
    void finalNuncaAlcancadoNaoTemData() {
        assertEquals(Optional.empty(), new Progresso().quandoAlcancou(Final.IC));
    }

    @Test
    void finaisFicamNaOrdemDeRegistro() {
        Progresso p = new Progresso();
        p.registrarFinal(Final.INDEPENDENTE, T1);   // de propósito, fora da ordem do enum
        p.registrarFinal(Final.IC, T2);

        assertEquals(List.of(Final.INDEPENDENTE, Final.IC), p.finaisAlcancados());
        assertTrue(p.alcancou(Final.INDEPENDENTE));
        assertFalse(p.alcancou(Final.DTEC));
    }

    @Test
    void listaDeFinaisNaoPodeSerAlterada() {
        Progresso p = new Progresso();
        p.registrarFinal(Final.IC, T1);
        List<Final> lista = p.finaisAlcancados();

        assertThrows(UnsupportedOperationException.class, () -> lista.add(Final.DTEC));
    }

    @Test
    void registrarComArgumentoNuloLancaExcecao() {
        Progresso p = new Progresso();

        assertThrows(IllegalArgumentException.class, () -> p.registrarFinal(null, T1));
        assertThrows(IllegalArgumentException.class, () -> p.registrarFinal(Final.IC, null));
    }
    @Test
    void consultasComNuloNaoLancamExcecao() {
        Progresso p = new Progresso();

        assertFalse(p.alcancou(null));
        assertEquals(Optional.empty(), p.quandoAlcancou(null));
    }

    @Test
    void sincronizarSemFinalRetornaFalse() {
        Progresso p = new Progresso();
        Protagonista protagonista = criarProtagonista();

        assertFalse(p.sincronizarFinais(protagonista, T1));
        assertTrue(p.finaisAlcancados().isEmpty());
    }

    @Test
    void sincronizarRegistraFinalICComDataInformada() {
        Progresso p = new Progresso();
        Protagonista protagonista = criarProtagonista();
        protagonista.adicionarFlag(Final.FLAG_CONCLUSAO);
        protagonista.adicionarFlag("trilha_ic");

        assertTrue(p.sincronizarFinais(protagonista, T1));
        assertTrue(p.alcancou(Final.IC));
        assertEquals(Optional.of(T1), p.quandoAlcancou(Final.IC));
    }

    @Test
    void sincronizarFinalRepetidoPreservaDataOriginal() {
        Progresso p = new Progresso();
        Protagonista protagonista = criarProtagonista();
        protagonista.adicionarFlag(Final.FLAG_CONCLUSAO);
        protagonista.adicionarFlag("trilha_ic");

        assertTrue(p.sincronizarFinais(protagonista, T1));
        assertFalse(p.sincronizarFinais(protagonista, T2));
        assertEquals(Optional.of(T1), p.quandoAlcancou(Final.IC));
    }

    @Test
    void sincronizarRegistraOutroFinalSemApagarOAnterior() {
        Progresso p = new Progresso();
        Protagonista ic = criarProtagonista();
        Protagonista dtec = criarProtagonista();

        ic.adicionarFlag(Final.FLAG_CONCLUSAO);
        ic.adicionarFlag("trilha_ic");
        dtec.adicionarFlag(Final.FLAG_CONCLUSAO);
        dtec.adicionarFlag("trilha_dtec");

        assertTrue(p.sincronizarFinais(ic, T1));
        assertTrue(p.sincronizarFinais(dtec, T2));

        assertEquals(List.of(Final.IC, Final.DTEC), p.finaisAlcancados());
    }

    @Test
    void sincronizarComArgumentoNuloLancaExcecao() {
        Progresso p = new Progresso();
        Protagonista protagonista = criarProtagonista();

        assertThrows(IllegalArgumentException.class,
                () -> p.sincronizarFinais(null, T1));
        assertThrows(IllegalArgumentException.class,
                () -> p.sincronizarFinais(protagonista, null));
    }



}

