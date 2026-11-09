package br.uefs.forkeazando.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurriculoTest {
    private Protagonista protagonista;

    @BeforeEach
    void setUp() {
        protagonista = new Protagonista(
                "Teste",
                false,
                false,
                false,
                Protagonista.NivelVidaSocial.ISOLADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        );
    }
    private Protagonista comFlags(String... flags) {
        for (String f : flags) protagonista.adicionarFlag(f);
        return protagonista;
    }

    private static boolean temLinha(java.util.List<Curriculo.Linha> ls, String trecho) {
        return ls.stream().anyMatch(l ->
                l.texto().toLowerCase().contains(trecho.toLowerCase()));
    }


    @Test
    @DisplayName("Currículo vazio tem FORMAÇÃO e pelo menos 3 LACUNAS")
    void curriculoVazio() {
        var ls = Curriculo.linhas(protagonista);
        assertTrue(temLinha(ls, "Engenharia de Computação"));
        long lacunas = ls.stream()
                .filter(l -> l.categoria() == Curriculo.Categoria.LACUNAS).count();
        assertTrue(lacunas >= 3, "esperado >= 3 lacunas, tem " + lacunas);
    }

    @Test
    @DisplayName("trilha_ic + bolsista_ic → linha positiva de IC")
    void icBolsista() {
        var ls = Curriculo.linhas(comFlags("trilha_ic", "bolsista_ic"));
        assertTrue(temLinha(ls, "Iniciação Científica"));
        assertTrue(temLinha(ls, "FAPESB"));
    }

    @Test
    @DisplayName("trilha_dtec + dtec_greve_incerta → linha com greve")
    void dtecGreve() {
        var ls = Curriculo.linhas(comFlags("trilha_dtec", "dtec_greve_incerta"));
        assertTrue(temLinha(ls, "Monitoria de DTEC"));
        assertTrue(temLinha(ls, "interrompida pela greve"));
    }

    @Test
    @DisplayName("cap3_colou_de_fato → integridade negativa")
    void colouDeFato() {
        var ls = Curriculo.linhas(comFlags("cap3_colou_de_fato"));
        assertTrue(temLinha(ls, "Colou em uma prova"));
    }

    @Test
    @DisplayName("cap7_questionou_autoria → integridade positiva")
    void questionouAutoria() {
        var ls = Curriculo.linhas(comFlags("cap7_questionou_autoria"));
        assertTrue(temLinha(ls, "Questionou ordem de autores"));
    }

    @Test
    @DisplayName("cap5c_cliente_pagou vs nao_pagou geram linhas distintas")
    void freela() {
        // Instâncias separadas porque cada teste muta o próprio protagonista.
        Protagonista pago = new Protagonista(
                "Pago", false, false, false,
                Protagonista.NivelVidaSocial.ISOLADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
        pago.adicionarFlag("cap5c_cliente_pagou");

        Protagonista naoPago = new Protagonista(
                "Não pago", false, false, false,
                Protagonista.NivelVidaSocial.ISOLADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
        naoPago.adicionarFlag("cap5c_cliente_nao_pagou");

        assertTrue(temLinha(Curriculo.linhas(pago), "(pago)"));
        assertTrue(temLinha(Curriculo.linhas(naoPago), "(não pago)"));
    }

    @Test
    @DisplayName("pesoPositivo cresce com flags positivas")
    void pesoPositivo() {
        int vazio = Curriculo.pesoPositivo(protagonista);
        comFlags("trilha_ic", "bolsista_ic", "cap1_depurou_circuito",
                "cap7_questionou_autoria", "cap7_tcc_planejou");
        int rico = Curriculo.pesoPositivo(protagonista);
        assertTrue(rico > vazio, rico + " > " + vazio);
    }

    @Test
    @DisplayName("se tem trabalho remunerado, a lacuna de remuneração some")
    void lacunaRemuneracaoSome() {
        var comBolsa = Curriculo.linhas(comFlags("bolsista_ic", "trilha_ic"));
        assertFalse(temLinha(comBolsa, "Sem experiência remunerada formal"));
    }
}