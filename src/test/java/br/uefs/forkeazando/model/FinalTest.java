package br.uefs.forkeazando.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FinalTest {

    @Test
    void cadaCodigoDevolveOFinalCerto() {
        assertEquals(Optional.of(Final.IC), Final.porCodigo("jornada_ic"));
        assertEquals(Optional.of(Final.DTEC), Final.porCodigo("jornada_dtec"));
        assertEquals(Optional.of(Final.INDEPENDENTE), Final.porCodigo("jornada_independente"));
    }

    @Test
    void codigoDesconhecidoOuNuloNaoDevolveNada() {
        assertEquals(Optional.empty(), Final.porCodigo("final_ic"));  // flag do capítulo 1
        assertEquals(Optional.empty(), Final.porCodigo(null));
    }

    @Test
    void codigosETrilhasSaoDistintos() {
        Set<String> codigos = Arrays.stream(Final.values())
                .map(Final::getCodigo).collect(Collectors.toSet());
        Set<String> trilhas = Arrays.stream(Final.values())
                .map(Final::getTrilha).collect(Collectors.toSet());

        assertEquals(Final.values().length, codigos.size());
        assertEquals(Final.values().length, trilhas.size());
    }

    @Test
    void semConclusaoNaoHaFinal() {
        assertEquals(Optional.empty(), Final.identificar(List.of("trilha_ic")));
    }

    @Test
    void conclusaoComTrilhaDevolveOFinalDaTrilha() {
        assertEquals(Optional.of(Final.IC),
                Final.identificar(List.of("final_formou", "trilha_ic")));
        assertEquals(Optional.of(Final.DTEC),
                Final.identificar(List.of("final_formou", "trilha_dtec")));
        assertEquals(Optional.of(Final.INDEPENDENTE),
                Final.identificar(List.of("final_formou", "trilha_indep")));
    }

    @Test
    void conclusaoSemTrilhaNaoDevolveFinal() {
        assertEquals(Optional.empty(),
                Final.identificar(List.of("final_formou", "perfil_sociavel")));
    }

    @Test
    void flagsDoCapitulo1NaoContamComoFinal() {
        assertEquals(Optional.empty(),
                Final.identificar(List.of("final_ic", "final_dtec")));
    }

    @Test
    void trilhaIndefinidaCaiNaIndependente() {
        assertEquals(Optional.of(Final.INDEPENDENTE),
                Final.identificar(List.of("final_formou", "trilha_indefinida", "trilha_indep")));
    }

    @Test
    void comMaisDeUmaTrilhaVenceAPrimeiraDaOrdemDoEpilogo() {
        assertEquals(Optional.of(Final.IC),
                Final.identificar(List.of("final_formou", "trilha_indep", "trilha_ic")));
    }

    @Test
    void listaNulaNaoDevolveFinal() {
        assertEquals(Optional.empty(), Final.identificar(null));
    }
}