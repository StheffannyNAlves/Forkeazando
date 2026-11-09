package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RoteiroIntegracaoTest {

    private static final Set<String> FLAGS_CONCEDIDAS_POR_JAVA = Set.of(
            "cap3_nota_alta_colando",
            "cap3_nota_alta_honesta",
            "cap3_nota_insuficiente",
            "cap3_nota_suficiente_colando",
            "cap3_nota_suficiente_honesta",
            "futuro_pesquisa_brasil",
            "futuro_tecnologia",
            "futuro_internacional",
            "futuro_independente",
            "futuro_transicao"
    );
    private static Set<String> flagsConcedidasOuGeradasPorJava() {
        Set<String> todas = flagsConcedidas();
        todas.addAll(FLAGS_CONCEDIDAS_POR_JAVA);
        return todas;
    }
    private static List<Cena> todasAsCenas() {
        return Roteiro.cenasDosCapitulos();
    }

    private static Set<Integer> idsDasCenas() {
        Set<Integer> ids = new HashSet<>();
        for (Cena c : todasAsCenas()) {
            ids.add(c.getId());
        }
        return ids;
    }

    private static Set<String> flagsConcedidas() {
        Set<String> concedidas = new HashSet<>();

        for (Cena cena : todasAsCenas()) {
            for (Escolha escolha : cena.getEscolhas()) {
                concedidas.addAll(escolha.getFlagsConcedidas());
            }
        }

        return concedidas;
    }

    private static Protagonista protagonistaComTrilha(String trilha) {
        Protagonista p = new Protagonista(
                "Teste", false, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
        if (trilha != null) {
            p.adicionarFlag(trilha);
        }
        return p;
    }


    @Test
    void idsDasCenasSaoUnicos() {
        Set<Integer> vistos = new HashSet<>();
        for (Cena c : todasAsCenas()) {
            assertTrue(vistos.add(c.getId()), "id de cena duplicado: " + c.getId());
        }
    }

    @Test
    void todoDestinoDeEscolhaExiste() {
        Set<Integer> ids = idsDasCenas();
        List<String> problemas = new ArrayList<>();

        for (Cena c : todasAsCenas()) {
            for (Escolha e : c.getEscolhas()) {
                int destino = e.getCenaDestinoId();
                if (destino != Estado.CENA_ENCERRAR && !ids.contains(destino)) {
                    problemas.add("cena " + c.getId() + " -> " + destino);
                }
            }
        }

        assertTrue(problemas.isEmpty(), "destinos inexistentes: " + problemas);
    }

    @Test
    void roteadorSempreApontaParaCenaExistente() {
        Set<Integer> ids = idsDasCenas();
        List<String> problemas = new ArrayList<>();

        for (String trilha : Arrays.asList(null, "trilha_ic", "trilha_dtec", "trilha_indep")) {
            Protagonista p = protagonistaComTrilha(trilha);
            for (int cap = 0; cap <= Roteador.CAPITULO_FINAL; cap++) {
                int cena = Roteador.proximaCena(cap, p);
                if (cap == 5 && trilha == null) {
                    assertTrue(cena == -1, "O Capítulo 5 deve rejeitar uma trilha não definida.");
                    continue;
                }
                if (!ids.contains(cena)) {
                    problemas.add("capítulo " + cap + ", trilha " + trilha + " -> " + cena);
                }
            }
        }

        assertTrue(problemas.isEmpty(), "o Roteador aponta para cena inexistente: " + problemas);
    }

    @Test
    void todaCenaEAlcancavelAPartirDasEntradas() {
        Map<Integer, Cena> porId = new HashMap<>();
        for (Cena c : todasAsCenas()) {
            porId.put(c.getId(), c);
        }

        // entradas: o início do jogo e a primeira cena de cada capítulo, para cada trilha
        Deque<Integer> fila = new ArrayDeque<>();
        Set<Integer> visitadas = new HashSet<>();
        for (String trilha : Arrays.asList("trilha_ic", "trilha_dtec", "trilha_indep")) {
            Protagonista p = protagonistaComTrilha(trilha);
            for (int cap = 0; cap <= Roteador.CAPITULO_FINAL; cap++) {
                fila.add(Roteador.proximaCena(cap, p));
            }
        }

        while (!fila.isEmpty()) {
            int id = fila.poll();
            Cena cena = porId.get(id);
            if (cena == null || !visitadas.add(id)) {
                continue;
            }
            // A cena 667 é resolvida pelo controlador, não por uma escolha JSON.
            if (id == ResolucaoDeFuturo.CENA_RESOLVER_FUTURO) {
                fila.addAll(List.of(662, 663, 664, 665, 666));
            }

            for (Escolha e : cena.getEscolhas()) {
                if (e.getCenaDestinoId() != Estado.CENA_ENCERRAR) {
                    fila.add(e.getCenaDestinoId());
                }
            }
        }

        Set<Integer> orfas = new TreeSet<>(porId.keySet());
        orfas.removeAll(visitadas);

        assertTrue(orfas.isEmpty(), "cenas que nenhuma escolha alcança: " + orfas);
    }


    @Test
    void todoFinalTemAsFlagsConcedidasNoRoteiro() {
        Set<String> concedidas = flagsConcedidasOuGeradasPorJava();

        List<String> faltando = new ArrayList<>();
        if (!concedidas.contains(Final.FLAG_CONCLUSAO)) {
            faltando.add(Final.FLAG_CONCLUSAO);
        }
        for (Final f : Final.values()) {
            if (!concedidas.contains(f.getTrilha())) {
                faltando.add(f.getTrilha());
            }
        }

        assertTrue(faltando.isEmpty(), "flags nunca concedidas: " + faltando);
    }

    @Test
    void todaFlagRequeridaEConcedidaEmAlgumLugar() {
        Set<String> concedidas = flagsConcedidasOuGeradasPorJava();
        Set<String> requeridas = new TreeSet<>();

        for (Cena c : todasAsCenas()) {
            for (Escolha e : c.getEscolhas()) {
                if (e.getFlagRequerida() != null) {
                    requeridas.add(e.getFlagRequerida());
                }
            }
        }

        requeridas.removeAll(concedidas);
        // concedidas pelo próprio Protagonista, e não por escolhas
        requeridas.removeIf(f -> f.startsWith("perfil_") || f.startsWith("origem_"));

        assertTrue(requeridas.isEmpty(), "flags exigidas e nunca concedidas: " + requeridas);
    }

    @Test
    void flagsGeradasPorJavaNaoEstaoDuplicadasNoJson() {
        Set<String> duplicadas = new TreeSet<>();

        for (String flag : flagsConcedidas()) {
            if (flag.startsWith("cap3_nota_")
                    || flag.startsWith("futuro_")) {
                duplicadas.add(flag);
            }
        }

        assertTrue(
                duplicadas.isEmpty(),
                "Flags dinâmicas também concedidas no JSON: " + duplicadas
        );
    }


    @Test
    void todaConquistaEAtingivelComAsFlagsDoRoteiro() {
        Protagonista p = protagonistaComTrilha(null);
        for (String flag : flagsConcedidasOuGeradasPorJava()) {
            p.adicionarFlag(flag);
        }
        p.alterarRelacionamento(PersonagemSecundario.VETERANO, 3f);
        p.alterarRelacionamento(PersonagemSecundario.DANDARA, 3f);
        p.alterarRelacionamento(PersonagemSecundario.ORIENTADOR_IC, 3f);

        List<String> inalcancaveis = new ArrayList<>();
        for (Conquista c : Conquista.values()) {
            if (!c.atingida(p)) {
                inalcancaveis.add(c.getCodigo());
            }
        }

        assertTrue(inalcancaveis.isEmpty(), "conquistas inalcançáveis: " + inalcancaveis);
    }

    @Test
    void cadaTrilhaSaiDaPonteDoCapitulo7ParaASuaEntrada() {
        Cena ponte = todasAsCenas().stream()
                .filter(c -> c.getId() == 599)
                .findFirst()
                .orElseThrow();

        Protagonista ic = protagonistaComTrilha("trilha_ic");
        assertDestinoUnico(ponte, ic, 601, "trilha IC");

        Protagonista dtecAprendeu = protagonistaComTrilha("trilha_dtec");
        dtecAprendeu.adicionarFlag("teo_aprendeu");
        assertDestinoUnico(ponte, dtecAprendeu, 602, "DTEC: Téo aprendeu");

        Protagonista dtecNaoAprendeu = protagonistaComTrilha("trilha_dtec");
        dtecNaoAprendeu.adicionarFlag("teo_nao_aprendeu");
        assertDestinoUnico(ponte, dtecNaoAprendeu, 6022, "DTEC: Téo não aprendeu");

        Protagonista dtecEncaminhado = protagonistaComTrilha("trilha_dtec");
        dtecEncaminhado.adicionarFlag("teo_encaminhado");
        assertDestinoUnico(ponte, dtecEncaminhado, 6022, "DTEC: Téo foi encaminhado");

        Protagonista independente = protagonistaComTrilha("trilha_indep");
        assertDestinoUnico(ponte, independente, 603, "trilha independente");
    }

    private static void assertDestinoUnico(
            Cena cena,
            Protagonista protagonista,
            int destinoEsperado,
            String contexto) {

        List<Escolha> saidas =
                cena.getEscolhasDisponiveis(protagonista);

        assertTrue(
                saidas.size() == 1
                        && saidas.get(0).getCenaDestinoId() == destinoEsperado,
                contexto + ": esperado destino " + destinoEsperado
                        + ", mas foram encontrados "
                        + saidas.stream()
                        .map(Escolha::getCenaDestinoId)
                        .toList()
        );
    }
}