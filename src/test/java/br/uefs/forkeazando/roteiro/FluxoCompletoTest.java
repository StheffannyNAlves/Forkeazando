package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Protagonista;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de integração da Fase 3: percorre escolhas reais dos JSONs e aplica
 * as consequências na mesma ordem da partida (sem acionar a interface)
 */
class FluxoCompletoTest {

    private static final Map<Integer, String> ESCOLHAS_IC = Map.ofEntries(
            Map.entry(700, "Voltei ao circuito"),
            Map.entry(701, "Relembrar o início pelos planos"),            Map.entry(0, "Acordar sua mãe"),
            Map.entry(1, "Ler o edital"),
            Map.entry(2, "Me apresentar à Malu"),
            Map.entry(3, "Perguntar ao veterano"),
            Map.entry(4, "Desviar da pergunta"),
            Map.entry(73, "Perguntar se ele quer companhia"),
            Map.entry(5, "Ler o datasheet e cruzar"),
            Map.entry(16, "Assumir a coordenação"),
            Map.entry(6, "Assumir a coordenação"),
            Map.entry(7, "Voltar à oficina e investigar"),
            Map.entry(8, "Medir a tensão"),
            Map.entry(100, "Abrir os avisos"),
            Map.entry(101, "Explorar a Feira"),
            Map.entry(116, "Conhecer o grupo de robótica"),
            Map.entry(120, "Ficar para medir de novo"),
            Map.entry(112, "Ouvir até"),
            Map.entry(117, "Se você ainda"),
            Map.entry(118, "Propor estudar em dupla"),
            Map.entry(200, "Só respirar"),
            Map.entry(201, "Respirar fundo"),
            Map.entry(202, "Anotar onde travou"),
            Map.entry(210, "Ela mereceu"),
            Map.entry(211, "Não concorrer agora"),
            Map.entry(214, "Perguntar sobre horários"),
            Map.entry(212, "Fui bem"),
            Map.entry(217, "Compartilhar um método"),
            Map.entry(300, "Conferir a oferta"),
            Map.entry(301, "Perguntar qual"),
            Map.entry(302, "Insistir"),
            Map.entry(303, "Porque quero aprender a pesquisar."),
            Map.entry(304, "Não sei. Mas quero tentar."),
            Map.entry(3045, "Abrir o formulário"),
            Map.entry(3046, "Estudar primeiro"),
            Map.entry(3047, "Prefiro trabalhar por conta"),
            Map.entry(3048, "Sim. Quero ver"),
            Map.entry(3049, "Saber"),
            Map.entry(3050, "Discordar"),
            Map.entry(305, "Priorizar a IC"),
            Map.entry(400, "Falar meu nome"),
            Map.entry(402, "Pedir a Bia"),
            Map.entry(403, "Dizer que entendo"),
            Map.entry(401, "Mandar mensagem"),
            Map.entry(405, "Assumir a coordenação"),
            Map.entry(451, "Ir na assembleia"),
            Map.entry(455, "Dividir as mensagens"),
            Map.entry(460, "Retomar o laboratório"),
            Map.entry(410, "Continuar e registrar"),
            Map.entry(415, "Falar do projeto"),
            Map.entry(416, "Confirmar a candidatura"),
            Map.entry(540, "Encarar as exigências"),
            Map.entry(541, "Pedir prorrogação"),
            Map.entry(550, "Ler a mensagem absurda"),
            Map.entry(555, "Perguntar como ela saiu"),
            Map.entry(554, "Continuar, reduzindo"),
            Map.entry(557, "Deixar ele contar"),
            Map.entry(558, "Perguntar por que ele voltou"),
            Map.entry(559, "Fui eu"),
            Map.entry(560, "Continuar. Uma etapa"),
            Map.entry(567, "Voltar ao projeto"),
            Map.entry(561, "Aceitar."),
            Map.entry(600, "Uma página por dia"),
            Map.entry(601, "Perguntar, com educação"),
            Map.entry(649, "Pagar o restante"),
            Map.entry(650, "Procurar Caio"),
            Map.entry(651, "Aceitar o copo"),
            Map.entry(653, "Ir tirar foto"),
            Map.entry(654, "Contestei uma ordem")
    );

    private static Map<Integer, Cena> jogo() {
        Map<Integer, Cena> mapa = new HashMap<>();
        for (Cena cena : Roteiro.todasAsCenas()) {
            assertNull(mapa.put(cena.getId(), cena),
                    "Cena duplicada no roteiro: " + cena.getId());
        }
        return mapa;
    }

    private static Protagonista protagonista(IdentidadeProtagonista identidade) {
        String nome = identidade == IdentidadeProtagonista.STHEFFANNY
                ? "Stheffanny" : "Davi";
        return new Protagonista(
                nome,
                identidade,
                true, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.POSITIVA,
                Protagonista.SituacaoEconomica.CONFORTAVEL);
    }

    private static int avancarAte(Map<Integer, Cena> jogo, Protagonista protagonista, int inicio, int destino, Map<Integer, String> decisoes, Set<Integer> decisoesUsadas) {
        int atual = inicio;
        Set<Integer> passagens = new HashSet<>();

        while (atual != destino) {
            assertTrue(passagens.add(atual),
                    "Loop inesperado no percurso narrativo, cena " + atual);
            Cena cena = jogo.get(atual);
            assertNotNull(cena, "Cena ausente no roteiro: " + atual);

            // O controlador normalmente registra a nota na cena de resultado
            if (ResultadoCalculo.ehCenaDeResultado(atual) || atual == 212) {
                ResultadoCalculo.assegurarNota(protagonista, atual);
            }

            List<Escolha> disponiveis = cena.getEscolhasDisponiveis(protagonista);
            assertFalse(disponiveis.isEmpty(),
                    "Beco sem saída na cena " + atual + " para " + protagonista.getNome());

            String textoEsperado = decisoes.get(atual);
            Escolha escolhida;
            if (textoEsperado != null) {
                String procurado = textoEsperado.toLowerCase(Locale.ROOT);
                List<Escolha> correspondentes = disponiveis.stream()
                        .filter(e -> e.getTextoAlternativa().toLowerCase(Locale.ROOT)
                                .contains(procurado))
                        .toList();
                assertEquals(1, correspondentes.size(),
                        "Cena " + atual + ": esperava uma escolha contendo '" + textoEsperado
                                + "'. Disponíveis: " + disponiveis.stream()
                                .map(Escolha::getTextoAlternativa).toList());
                escolhida = correspondentes.get(0);
                decisoesUsadas.add(atual);
            } else {
                // Não adivinhamos qual caminho tomar se há mais de uma opção
                assertEquals(1, disponiveis.size(),
                        "A cena " + atual + " tem uma bifurcação não definida no teste: "
                                + disponiveis.stream().map(Escolha::getTextoAlternativa).toList());
                escolhida = disponiveis.get(0);
            }

            for (String flag : escolhida.getFlagsConcedidas()) {
                protagonista.adicionarFlag(flag);
            }
            for (var impacto : escolhida.getImpactosRelacionamento().entrySet()) {
                protagonista.alterarRelacionamento(impacto.getKey(), impacto.getValue());
            }
            for (var impacto : escolhida.getAtributosAlterados().entrySet()) {
                protagonista.alterarAtributo(impacto.getKey(), impacto.getValue());
            }
            protagonista.ganharScore(escolhida.getScoreGanho());
            protagonista.ganharParticipacao(escolhida.getParticipacaoGanha());
            protagonista.gastarEnergia(escolhida.getCustoEnergia());

            atual = escolhida.getCenaDestinoId();
        }
        return atual;
    }

    private static void descansar(Protagonista p) {
        p.recuperarEnergia(p.getEnergiaMaxima());
    }

    @ParameterizedTest
    @EnumSource(IdentidadeProtagonista.class)
    @DisplayName("Fase 3: percurso completo de IC até o final de pesquisa")
    void partidaIcCompleta(IdentidadeProtagonista identidade) {
        Map<Integer, Cena> jogo = jogo();
        Protagonista p = protagonista(identidade);
        Set<Integer> decisoesUsadas = new HashSet<>();
        Map<Integer, String> escolhasDaPartida = new HashMap<>(ESCOLHAS_IC);
        if (identidade == IdentidadeProtagonista.DAVI) {
            escolhasDaPartida.put(701, "Relembrar o início pelas ideias");
        }

        assertEquals(identidade == IdentidadeProtagonista.STHEFFANNY, p.temFlag("protagonista_stheffanny"));
        assertEquals(identidade == IdentidadeProtagonista.DAVI, p.temFlag("protagonista_masculino"));

        // Prólogo revisado: entrevista -> memória de 2024.2 -> matrícula
        assertEquals(0, avancarAte(jogo, p, 700, 0, escolhasDaPartida, decisoesUsadas));
        assertTrue(p.temFlag("prologo_resposta_conviccao"));

        // Testa o início de cada capítulo, decisões reais e encerramento
        int[] entradas = {0, 100, 200, 300, 400, 540, 600};
        for (int capitulo = 1; capitulo <= 7; capitulo++) {
            int inicio = Roteador.proximaCena(capitulo, p);
            assertEquals(entradas[capitulo - 1], inicio,
                    "Início inesperado do capítulo " + capitulo);
            assertEquals(Estado.CENA_ENCERRAR,
                    avancarAte(jogo, p, inicio, Estado.CENA_ENCERRAR,
                            escolhasDaPartida, decisoesUsadas),
                    "O capítulo " + capitulo + " não foi concluído");

            switch (capitulo) {
                case 1 -> {
                    assertTrue(p.temFlag("cap1_depurou_circuito"));
                    assertTrue(p.temFlag("pbl_coordenador"));
                }
                case 2 -> {
                    assertTrue(p.temFlag("caio_frase"));
                    assertTrue(p.temFlag("estuda_com_dandara"));
                }
                case 3 -> {
                    assertTrue(p.temFlag("cap3_nota_alta_honesta"));
                    assertTrue(p.temFlag("cap3_compartilhou_metodo"));
                }
                case 4 -> {
                    assertTrue(p.temFlag("trilha_ic"));
                    assertFalse(p.temFlag("trilha_dtec"));
                    assertFalse(p.temFlag("trilha_indep"));
                }
                case 5 -> {
                    assertTrue(p.temFlag("cap5_concluido"));
                    assertTrue(p.temFlag("bolsista_ic"));
                    assertTrue(p.temFlag("cap5b_dividiu_comida"));
                    assertTrue(p.temFlag("hab_sobreviveu_greve"));
                }
                case 6 -> {
                    assertTrue(p.temFlag("caio_revelacao"));
                    assertTrue(p.temFlag("cap6_continuou"));
                    assertTrue(p.temFlag("cap6_ic_recomeco"));
                }
                case 7 -> assertTrue(p.temFlag("cap7_questionou_autoria"));
                default -> fail("Capítulo inesperado: " + capitulo);
            }
            descansar(p);
        }

        assertEquals(649, Roteador.proximaCena(8, p));
        assertEquals(ResolucaoDeFuturo.CENA_RESOLVER_FUTURO,
                avancarAte(jogo, p, 649, ResolucaoDeFuturo.CENA_RESOLVER_FUTURO,
                        escolhasDaPartida, decisoesUsadas));
        assertTrue(p.temFlag("cap8_resposta_autoria"));

        // A cena 667 não possui escolha JSON; é resolvida pelo código do jogo
        int cenaFinal = ResolucaoDeFuturo.resolverCena(p);
        assertEquals(662, cenaFinal, "A trajetória IC deveria levar ao futuro de pesquisa");
        assertTrue(p.temFlag("futuro_pesquisa_brasil"));
        assertEquals(Estado.CENA_ENCERRAR,
                avancarAte(jogo, p, cenaFinal, Estado.CENA_ENCERRAR,
                        escolhasDaPartida, decisoesUsadas));
        assertTrue(p.temFlag("cap8_concluido"));
        assertTrue(p.temFlag("final_formou"));

        // Garante que nenhuma decisão planejada ficou inacessível no percurso
        assertEquals(ESCOLHAS_IC.keySet(), decisoesUsadas,
                "Uma ou mais bifurcações do percurso IC não foram visitadas");
    }

    @Test
    @DisplayName("Fase 3: Davi possui um prólogo próprio sem herdar identidade de Stheffanny")
    void prologoDaviRespeitaIdentidade() {
        Map<Integer, Cena> jogo = jogo();
        Protagonista davi = protagonista(IdentidadeProtagonista.DAVI);
        Set<Integer> usadas = new HashSet<>();
        Map<Integer, String> escolhasDavi = Map.of(
                700, "Na hora não soube",
                701, "Relembrar o início pelas ideias");        assertTrue(davi.temFlag("protagonista_masculino"));
        assertFalse(davi.temFlag("protagonista_stheffanny"));
        assertEquals(0, avancarAte(jogo, davi, 700, 0, escolhasDavi, usadas));
        assertTrue(davi.temFlag("prologo_resposta_aberta"));
        assertEquals(escolhasDavi.keySet(), usadas);
    }
}
