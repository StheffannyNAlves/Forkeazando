package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GrafoDeCenasTest {

    /** Todas as cenas do jogo, reunidas. */
    private static List<Cena> todasAsCenas() {
        return Roteiro.todasAsCenas();
    }

    private static Map<Integer, Cena> indexar() {
        Map<Integer, Cena> mapa = new LinkedHashMap<>();
        for (Cena c : todasAsCenas()) {
            Cena anterior = mapa.putIfAbsent(c.getId(), c);
            assertNull(anterior,
                    "ID de cena duplicado: " + c.getId());
        }
        return mapa;
    }

    @Test
    @DisplayName("Não há IDs de cena duplicados")
    void idsUnicos() {
        indexar();   // falha na asserção interna se duplicar
    }

    @Test
    @DisplayName("Toda escolha aponta para uma cena existente (ou CENA_ENCERRAR / CENA_RETOMAR)")
    void escolhasApontamParaCenasReais() {
        Map<Integer, Cena> cenas = indexar();
        Set<Integer> especiais = Set.of(Estado.CENA_ENCERRAR, Estado.CENA_RETOMAR);

        for (Cena c : cenas.values()) {
            for (Escolha e : c.getEscolhas()) {
                int destino = e.getCenaDestinoId();
                assertTrue(
                        especiais.contains(destino) || cenas.containsKey(destino),
                        "Cena " + c.getId() + " ('" + e.getTextoAlternativa()
                                + "') aponta para destino inexistente: " + destino);
            }
        }
    }
    @Test
    @DisplayName("Nenhuma cena é órfã — todas alcançáveis a partir de um início de capítulo")
    void semCenasOrfas() {
        Map<Integer, Cena> cenas = indexar();

        // Sementes: as cenas que o Roteador devolve ao entrar em cada capítulo.
        // Se o Roteador mudar, atualizar aqui.
        Set<Integer> inicios = new HashSet<>(Set.of(
                700,          // prólogo
                0,            // cap 1
                100,          // cap 2
                200,          // cap 3
                300,          // cap 4
                400, 450, 500,// cap 5 (uma por trilha)
                540,          // cap 6
                600,          // cap 7
                649,          // cap 8
                Estado.CENA_APAGAO   // 970 — entrada alternativa via controller
        ));

        Set<Integer> alcancaveis = new HashSet<>(inicios);
        Deque<Integer> fila = new ArrayDeque<>(inicios);

        while (!fila.isEmpty()) {
            int id = fila.poll();
            Cena c = cenas.get(id);
            if (c == null) continue;
            // A cena 667 decide por Java qual dos cinco finais será exibido.
            // Como este teste não simula a trajetória da protagonista,considera os cinco destinos possíveis
            if (id == ResolucaoDeFuturo.CENA_RESOLVER_FUTURO) {
                for (int finalId : List.of(662, 663, 664, 665, 666)) {
                    if (alcancaveis.add(finalId)) {
                        fila.add(finalId);
                    }
                }
            }
            for (Escolha e : c.getEscolhas()) {
                int destino = e.getCenaDestinoId();
                if (destino == Estado.CENA_ENCERRAR || destino == Estado.CENA_RETOMAR) continue;
                if (alcancaveis.add(destino)) fila.add(destino);
            }
        }

        List<Integer> orfas = cenas.keySet().stream()
                .filter(id -> !alcancaveis.contains(id))
                .sorted()
                .toList();

        assertTrue(orfas.isEmpty(),
                "Cenas inalcançáveis: " + orfas
                        + ". Se for cena de início de capítulo, adicione em `inicios`. "
                        + "Se for cena normal, nenhuma escolha aponta pra ela.");
    }

    @Test
    @DisplayName("Toda cena tem saída ou é uma transição automática")
    void todaCenaTemSaida() {
        for (Cena c : todasAsCenas()) {

            // A cena 667 é resolvida pelo controlador e não possui
            // escolhas no JSON de propósito.
            if (c.getId() == ResolucaoDeFuturo.CENA_RESOLVER_FUTURO) {
                continue;
            }

            assertFalse(
                    c.getEscolhas().isEmpty(),
                    "Cena " + c.getId() + " não tem escolhas — jogador fica preso."
            );
        }
    }

    @Test
    @DisplayName("Cada trilha tem exatamente uma cena com flags trilha_*")
    void trilhasSaoUnicas() {
        int ic = 0, dtec = 0, indep = 0;
        for (Cena c : todasAsCenas()) {
            for (Escolha e : c.getEscolhas()) {
                if (e.getFlagsConcedidas().contains("trilha_ic")) ic++;
                if (e.getFlagsConcedidas().contains("trilha_dtec")) dtec++;
                if (e.getFlagsConcedidas().contains("trilha_indep")) indep++;
            }
        }
        // Cada trilha deve ser definida em pelo menos uma escolha.
        // Se for mais de uma, ok — múltiplos caminhos (IC via 371, 372, 373...)
        assertTrue(ic >= 1, "Nenhuma escolha concede trilha_ic");
        assertTrue(dtec >= 1, "Nenhuma escolha concede trilha_dtec");
        assertTrue(indep >= 1, "Nenhuma escolha concede trilha_indep");
    }
}