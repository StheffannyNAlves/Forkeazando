package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.controller.CenasController;
import br.uefs.forkeazando.controller.GatilhoAutosave;
import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.model.*;
import br.uefs.forkeazando.view.ApresentacaoCena;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Teste automatizado de exploração das rotas narrativas do Forkeazando.
 *
 * A complexidade combinatória das decisões, condicionadas por atributos,
 * energia, relacionamentos, flags e escolhas anteriores, tornou inviável
 * verificar manualmente todas as possibilidades de maneira sistemática.
 *
 * Por esse motivo, utilizou-se o agente de IA Google Antigravity como
 * ferramenta auxiliar na implementação da estratégia de exploração.
 *
 * O teste utiliza o motor narrativo real (CenasController), empregando
 * busca por prioridade, deduplicação de estados e limites de expansão
 * para investigar a alcançabilidade das rotas e possíveis inconsistências.
 *
 * A exploração é limitada e não garante cobertura exaustiva de todos
 * os estados possíveis.
 *
 * Autoria do teste: assistida por IA (Google Antigravity).
 * Projeto Forkeazando: desenvolvimento autoral.
 */

class ExploracaoEstadosRoteiroTest {

    private static final Gson GSON = new Gson();
    private static final int LIMITE_ESTADOS_POR_BUCKET_CENA = 22;
    private static final int LIMITE_TOTAL_EXPANSOES = 10000;

    @TempDir
    Path pasta;

    private record NoBusca(Estado estado, int prioridade, long ordem) {}

    private static final ApresentacaoCena NO_OP_VIEW = new ApresentacaoCena() {
        @Override public void resetarCacheVisual() {}
        @Override public void mostrarGaleria(Protagonista protagonista) {}
        @Override public void renderizar(Cena cena, Protagonista p, int semestresExtras, ApresentacaoDeCena visual) {
            String resolvido = NarrativaDinamica.resolver(cena, p, semestresExtras);
            assertFalse(resolvido.contains("{{"),
                    "Placeholder não resolvido na cena " + cena.getId() + ": " + resolvido);
        }
        @Override public void mostrarBencaoCaramelo() {}
        @Override public void mostrarHabilidade(String flag) {}
        @Override public void mostrarItem(String flag) {}
        @Override public void mostrarConquista(Conquista conquista) {}
        @Override public void mostrarMensagem(String mensagem) {}
        @Override public void mostrarImpactoRelacionamento(PersonagemSecundario npc, float valor, Protagonista.NivelRelacionamento nivel) {}
        @Override public void mostrarEpilogos(Protagonista p) {}
        @Override public void mostrarCurriculo(Protagonista p) {}
        @Override public void mostrarTransicao(String capitulo) {}
        @Override public void mostrarChuteDoSistema(Protagonista p) {}
    };

    @Test
    @DisplayName("Exploração automatizada de estados verifica ausência de becos sem saída, alcançabilidade real e todos os 5 finais")
    void explorarEspacoDeEstadosComMotorReal() {
        List<Cena> todasCenas = Roteiro.todasAsCenas();
        Map<Integer, Cena> porId = new LinkedHashMap<>();
        Set<String> flagsDeDecisao = new HashSet<>();
        int totalEscolhas = 0;

        for (Cena c : todasCenas) {
            porId.put(c.getId(), c);
            for (Escolha e : c.getEscolhas()) {
                totalEscolhas++;
                if (e.getFlagRequerida() != null) {
                    flagsDeDecisao.add(e.getFlagRequerida());
                }
            }
        }
        flagsDeDecisao.addAll(List.of(
                "trilha_ic", "trilha_dtec", "trilha_indep", "trilha_indefinida", "cap4_perdeu_prazo",
                "cap4_plano_internacional", "cap8_resposta_internacional", "cap6_trancou",
                "cap5a_liderou_artigo", "cap5a_registrou_experimento", "cap7_questionou_autoria",
                "cap5b_preparou_exercicio", "cap7_dtec_ultima_aula", "cap7_dtec_nao_deu",
                "industry_electronics", "cap1_depurou_circuito", "ras_curiosa", "tisp_interesse",
                "wie_interesse", "software_fora", "liga_jogos_contato", "cap8_resposta_honesta",
                "cap5c_concluido", "final_formou", "amigo_do_caramelo"
        ));

        List<Protagonista> perfisIniciais = List.of(
                new Protagonista("Tiana", IdentidadeProtagonista.STHEFFANNY, true, true, true,
                        Protagonista.NivelVidaSocial.AMIGUEIRO,
                        Protagonista.ExperienciaEmocionalEnsinoMedio.POSITIVA,
                        Protagonista.SituacaoEconomica.ELITE),
                new Protagonista("Miles", IdentidadeProtagonista.DAVI, false, true, false,
                        Protagonista.NivelVidaSocial.EQUILIBRADO,
                        Protagonista.ExperienciaEmocionalEnsinoMedio.POSITIVA,
                        Protagonista.SituacaoEconomica.ELITE),
                new Protagonista("Tiana", IdentidadeProtagonista.STHEFFANNY, false, false, false,
                        Protagonista.NivelVidaSocial.ISOLADO,
                        Protagonista.ExperienciaEmocionalEnsinoMedio.NEGATIVA,
                        Protagonista.SituacaoEconomica.APERTADA),
                new Protagonista("Miles", IdentidadeProtagonista.DAVI, true, false, true,
                        Protagonista.NivelVidaSocial.AMIGUEIRO,
                        Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                        Protagonista.SituacaoEconomica.ESTAVEL)
        );

        PriorityQueue<NoBusca> fila = new PriorityQueue<>(
                Comparator.comparingInt(NoBusca::prioridade).reversed()
                        .thenComparingLong(NoBusca::ordem));
        Set<String> assinaturasVistas = new HashSet<>();
        Set<String> flagsDescobertasGlobalmente = new HashSet<>();
        Map<String, Integer> visitasPorBucket = new HashMap<>();
        long seq = 0;

        for (Protagonista p : perfisIniciais) {
            Estado inicial = new Estado();
            inicial.iniciar(p);
            inicial.setCenaAtualId(700);
            fila.add(new NoBusca(inicial, 1000, seq++));
            assinaturasVistas.add(assinatura(inicial, flagsDeDecisao));
        }

        Set<Integer> cenasAlcancadas = new TreeSet<>();
        Set<String> escolhasAlcancadas = new HashSet<>();
        Set<Estado.Trilha> trilhasAlcancadas = EnumSet.noneOf(Estado.Trilha.class);
        Set<Final> finaisAlcancados = EnumSet.noneOf(Final.class);
        List<String> errosDeRota = new ArrayList<>();

        GerenciadorProgresso gp = new GerenciadorProgresso(pasta);
        Progresso progresso = new Progresso();

        int expansoes = 0;
        int transicoes = 0;

        while (!fila.isEmpty() && expansoes < LIMITE_TOTAL_EXPANSOES) {
            Estado atualEstado = fila.poll().estado();
            String chaveBucket = bucketDeVisita(atualEstado);
            if (visitasPorBucket.getOrDefault(chaveBucket, 0) >= LIMITE_ESTADOS_POR_BUCKET_CENA) {
                continue;
            }
            visitasPorBucket.merge(chaveBucket, 1, Integer::sum);
            expansoes++;

            CenasController controller = new CenasController(
                    atualEstado, todasCenas, NO_OP_VIEW, progresso, gp,
                    e -> {}, new GatilhoAutosave(1000), () -> {});

            controller.prepararCenaAtual();
            int cenaId = atualEstado.getCenaAtualId();
            cenasAlcancadas.add(cenaId);
            if (atualEstado.getTrilha() != null) {
                trilhasAlcancadas.add(atualEstado.getTrilha());
            }

            Cena cena = porId.get(cenaId);
            assertNotNull(cena, "Cena inexistente: " + cenaId);
            List<Escolha> disponiveis = cena.getEscolhasDisponiveis(atualEstado.getProtagonista());
            assertFalse(disponiveis.isEmpty(), "Beco sem saída real na cena " + cenaId);

            for (int i = 0; i < disponiveis.size(); i++) {
                Estado clone = GSON.fromJson(GSON.toJson(atualEstado), Estado.class);
                CenasController branchController = new CenasController(
                        clone, todasCenas, NO_OP_VIEW, progresso, gp,
                        e -> {}, new GatilhoAutosave(1000), () -> {});

                Escolha escolha = cena.getEscolhasDisponiveis(clone.getProtagonista()).get(i);
                escolhasAlcancadas.add(cenaId + "#" + escolha.getTextoAlternativa());
                transicoes++;

                CenasController.ResultadoTransicao res = branchController.registrarEscolha(escolha);
                if (res == CenasController.ResultadoTransicao.ERRO_DE_ROTA) {
                    errosDeRota.add("Cena " + cenaId + " -> '" + escolha.getTextoAlternativa() + "'");
                } else if (res == CenasController.ResultadoTransicao.FIM_DE_JOGO) {
                    assertTrue(clone.isPartidaConcluida(), "FIM_DE_JOGO sem partidaConcluida na cena " + cenaId);
                    Final.porCodigo(clone.getCodigoFinal()).ifPresent(finaisAlcancados::add);
                } else {
                    String sig = assinatura(clone, flagsDeDecisao);
                    if (assinaturasVistas.add(sig)) {
                        int bonusNovaCena = cenasAlcancadas.contains(clone.getCenaAtualId()) ? 0 : 400;
                        int bonusNovasFlags = 0;
                        for (String fl : clone.getProtagonista().getHistoricoFlags()) {
                            if (flagsDeDecisao.contains(fl) && flagsDescobertasGlobalmente.add(fl)) {
                                bonusNovasFlags += 250;
                            }
                        }
                        Protagonista cp = clone.getProtagonista();
                        int bonusProgresso = clone.getCapituloAtual() * 35
                                + Math.min(cp.getScore(), 30) * 4
                                + Math.min(cp.getParticipacao(), 20) * 6
                                + (cp.getEnergia() >= 15 ? 30 : 0);
                        int prioridade = bonusNovaCena + bonusNovasFlags + bonusProgresso;
                        fila.add(new NoBusca(clone, prioridade, seq++));
                    }
                }
            }
        }

        List<Integer> naoAlcancadas = porId.keySet().stream()
                .filter(id -> !cenasAlcancadas.contains(id) && id != ResolucaoDeFuturo.CENA_RESOLVER_FUTURO)
                .sorted()
                .toList();

        System.out.printf(
                "[EXPLORACAO] Expansoes=%d Transicoes=%d CenasAlcancadas=%d/%d EscolhasAlcancadas=%d/%d Trilhas=%s Finais=%s NaoAlcancadas=%s%n",
                expansoes, transicoes, cenasAlcancadas.size(), porId.size() - 1,
                escolhasAlcancadas.size(), totalEscolhas, trilhasAlcancadas, finaisAlcancados, naoAlcancadas);

        assertTrue(errosDeRota.isEmpty(), "Encontrados erros de rota durante exploração: " + errosDeRota);
        assertEquals(Set.of(Estado.Trilha.IC, Estado.Trilha.DTEC, Estado.Trilha.INDEP), trilhasAlcancadas,
                "Todas as 3 trilhas devem ser dinamicamente alcançáveis");
        assertTrue(cenasAlcancadas.contains(Estado.CENA_APAGAO), "Evento de apagão (970) deve ser alcançável");
        assertTrue(finaisAlcancados.containsAll(List.of(
                        Final.PESQUISA_BRASIL, Final.TECNOLOGIA, Final.INTERNACIONAL,
                        Final.CAMINHO_PROPRIO, Final.TRANSICAO)),
                "Todos os 5 finais de carreira devem ser dinamicamente alcançáveis. Alcançados: " + finaisAlcancados);
        assertTrue(cenasAlcancadas.size() >= 145,
                "Cobertura real de cenas abaixo do esperado: " + cenasAlcancadas.size() + " (não alcançadas: " + naoAlcancadas + ")");
    }

    private static String bucketDeVisita(Estado e) {
        Protagonista p = e.getProtagonista();
        return e.getCenaAtualId()
                + "|" + e.getTrilha()
                + "|" + p.temFlag("cap4_plano_internacional")
                + "|" + p.temFlag("cap4_perdeu_prazo")
                + "|" + p.temFlag("cap6_trancou");
    }

    private static String assinatura(Estado e, Set<String> flagsRelevantes) {
        Protagonista p = e.getProtagonista();
        List<String> relevantes = new ArrayList<>();
        for (String f : p.getHistoricoFlags()) {
            if (flagsRelevantes.contains(f)) {
                relevantes.add(f);
            }
        }
        Collections.sort(relevantes);
        int energiaBucket = p.getEnergia() == 0 ? 0 : (p.getEnergia() < 15 ? 1 : (p.getEnergia() < 35 ? 2 : 3));
        int scoreBucket = p.getScore() >= 18 ? 3 : (p.getScore() >= 14 ? 2 : (p.getScore() >= 6 ? 1 : 0));
        int partBucket = p.getParticipacao() >= 12 ? 1 : 0;
        return e.getCapituloAtual() + "|" + e.getCenaAtualId() + "|" + e.getTrilha()
                + "|" + e.temDestinoPendente() + "|" + energiaBucket + "|" + scoreBucket + "|" + partBucket
                + "|" + relevantes;
    }
}
