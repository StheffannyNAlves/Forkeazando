package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.adapter.CarregamentoAdapter;
import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.model.*;
import br.uefs.forkeazando.roteiro.ApresentacaoDeCena;
import br.uefs.forkeazando.roteiro.Epilogos;
import br.uefs.forkeazando.roteiro.NarrativaDinamica;
import br.uefs.forkeazando.view.ApresentacaoCena;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de integração ponta a ponta de todas as rotas principais
 * utilizando o motor real (SessaoGrafica + CenasController + CarregamentoAdapter),
 * cobrindo as 3 trilhas (IC, DTEC, INDEP), os 5 finais de carreira, ambos os
 * protagonistas (Tiana e Miles), apagão, trancamento, salvamento/carregamento e
 * persistência cumulativa em progresso.json
 */
class TodasAsRotasIntegracaoTest {

    @TempDir
    Path pasta;

    private static Map<Integer, String> baseComum(IdentidadeProtagonista identidade) {
        Map<Integer, String> m = new HashMap<>();
        m.put(700, "Voltei ao circuito");
        m.put(701, identidade == IdentidadeProtagonista.STHEFFANNY
                ? "Relembrar o início pelos planos"
                : "Relembrar o início pelas ideias");
        m.put(0, "Acordar sua mãe");
        m.put(1, "Ler o edital");
        m.put(2, "Me apresentar à Malu");
        m.put(3, "Perguntar ao veterano");
        m.put(4, "Desviar da pergunta");
        m.put(73, "Perguntar se ele quer companhia");
        m.put(5, "Ler o datasheet e cruzar");
        m.put(16, "Assumir a coordenação");
        m.put(6, identidade == IdentidadeProtagonista.STHEFFANNY
                ? "Definir critérios de navegação"
                : "Prototipar um caminho");
        m.put(7, "Voltar à oficina e investigar");
        m.put(8, "Medir a tensão");
        m.put(100, "Deixar os avisos");
        m.put(101, "Conhecer o centro acadêmico");
        m.put(102, "Assinar a lista");
        m.put(103, "Sair da equipe");
        m.put(112, "Ouvir até");
        m.put(117, "Se você ainda");
        m.put(118, "Propor estudar em dupla");
        m.put(200, "Só respirar");
        m.put(201, "Respirar fundo");
        m.put(202, "Anotar onde travou");
        m.put(210, "Ela mereceu");
        m.put(211, "Não concorrer agora");
        m.put(214, "Perguntar sobre horários");
        m.put(212, "Fui bem");
        m.put(217, "Compartilhar um método");
        m.put(300, "Conferir a oferta");
        m.put(301, "Perguntar qual");
        m.put(302, "Insistir");
        m.put(303, "Porque quero aprender a pesquisar.");
        m.put(304, "Não sei. Mas quero tentar.");
        m.put(3045, "Abrir o formulário");
        m.put(3046, "Estudar primeiro");
        m.put(3047, "Prefiro trabalhar por conta");
        m.put(3048, "Sim. Quero ver");
        m.put(3049, "Saber");
        m.put(3050, "Discordar");
        // Apagão (caso energia zere em alguma rota — escolhe apoio disponível)
        m.put(971, "Descansar um pouco");
        m.put(972, "Recolher suas coisas");
        // Capítulo 6 base
        m.put(550, "Ler a mensagem absurda");
        m.put(555, "Perguntar como ela saiu");
        m.put(554, "Continuar, reduzindo");
        m.put(557, "Deixar ele contar");
        m.put(558, "Perguntar por que ele voltou");
        m.put(559, "Fui eu");
        m.put(560, "Continuar. Uma etapa");
        // Capítulo 8 base
        m.put(649, "Pagar o restante");
        m.put(650, "Procurar Caio");
        m.put(651, "Aceitar o copo");
        m.put(653, "Ir tirar foto");
        return m;
    }

    private static Protagonista protagonistaPadrao(IdentidadeProtagonista identidade) {
        return new Protagonista(
                identidade == IdentidadeProtagonista.STHEFFANNY ? "Tiana" : "Miles",
                identidade,
                true, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.POSITIVA,
                Protagonista.SituacaoEconomica.ELITE);
    }

    private record RelatorioExecucao(
            Estado estadoFinal,
            List<Integer> cenasVisitadas,
            boolean passouPorApagao,
            boolean exibiuCurriculo,
            boolean exibiuEpilogos,
            int notificacoesObserver,
            List<Conquista> conquistasDesbloqueadas
    ) {}

    private static final class ApresentacaoGravadora implements ApresentacaoCena {
        final List<Integer> cenasRenderizadas = new ArrayList<>();
        final List<Conquista> conquistas = new ArrayList<>();
        final List<String> mensagens = new ArrayList<>();
        boolean curriculoExibido;
        boolean epilogosExibidos;

        @Override public void resetarCacheVisual() {}
        @Override public void mostrarGaleria(Protagonista protagonista) {}

        @Override
        public void renderizar(Cena cena, Protagonista p, int semestresExtras, ApresentacaoDeCena visual) {
            cenasRenderizadas.add(cena.getId());
            String texto = NarrativaDinamica.resolver(cena, p, semestresExtras);
            assertFalse(texto.contains("{{"),
                    "Placeholder não resolvido na cena " + cena.getId() + ": " + texto);
        }

        @Override public void mostrarBencaoCaramelo() {}
        @Override public void mostrarHabilidade(String flag) {}
        @Override public void mostrarItem(String flag) {}
        @Override public void mostrarConquista(Conquista conquista) { conquistas.add(conquista); }
        @Override public void mostrarMensagem(String mensagem) { mensagens.add(mensagem); }
        @Override public void mostrarImpactoRelacionamento(PersonagemSecundario npc, float valor, Protagonista.NivelRelacionamento nivel) {}
        @Override public void mostrarEpilogos(Protagonista p) {
            epilogosExibidos = true;
            assertFalse(Epilogos.de(p).isEmpty(), "Epílogos não podem estar vazios");
        }
        @Override public void mostrarCurriculo(Protagonista p) {
            curriculoExibido = true;
            assertFalse(Curriculo.linhas(p).isEmpty(), "Currículo não pode estar vazio");
        }
        @Override public void mostrarTransicao(String capitulo) {}
        @Override public void mostrarChuteDoSistema(Protagonista p) {}
    }

    private RelatorioExecucao executarRotaCompleta(
            Protagonista protagonista,
            Map<Integer, String> escolhas,
            boolean salvarERecarregarNoMeio) throws Exception {

        CarregamentoAdapter adapter = new CarregamentoAdapter(pasta);
        SessaoGrafica sessao = new SessaoGrafica(adapter, pasta);
        ApresentacaoGravadora view = new ApresentacaoGravadora();
        AtomicInteger observacoes = new AtomicInteger();
        sessao.adicionarObservador(e -> observacoes.incrementAndGet());

        sessao.novaPartida(protagonista, view);

        boolean recarregou = false;
        int passos = 0;
        int limitePassos = 300;
        Estado estadoFinal = null;

        while (passos++ < limitePassos) {
            sessao.prepararCenaAtual();
            Cena atual = sessao.cenaAtual();

            if (salvarERecarregarNoMeio && !recarregou && sessao.estado().getCapituloAtual() == 5) {
                sessao.salvarEmSlot(1);
                Estado salvo = adapter.carregarSlot(1);
                SessaoGrafica novaSessao = new SessaoGrafica(adapter, pasta);
                novaSessao.adicionarObservador(e -> observacoes.incrementAndGet());
                novaSessao.carregarPartida(salvo, view);
                sessao = novaSessao;
                sessao.prepararCenaAtual();
                atual = sessao.cenaAtual();
                recarregou = true;
            }

            List<Escolha> disponiveis = sessao.escolhasDisponiveis();
            assertFalse(disponiveis.isEmpty(), "Sem escolhas disponíveis na cena " + atual.getId());

            Escolha escolhida = selecionarEscolha(atual, disponiveis, escolhas);
            CenasController.ResultadoTransicao transicao = sessao.encaminharEscolha(escolhida);

            assertNotEquals(CenasController.ResultadoTransicao.ERRO_DE_ROTA, transicao,
                    "Erro de rota ao sair da cena " + atual.getId());

            if (transicao == CenasController.ResultadoTransicao.FIM_DE_JOGO) {
                estadoFinal = sessao.estado();
                sessao.finalizarPartida();
                assertFalse(sessao.possuiPartida(), "Sessão deve ser encerrada após finalizarPartida()");
                break;
            }
        }

        assertNotNull(estadoFinal, "A rota não chegou ao FIM_DE_JOGO dentro de " + limitePassos + " passos");
        boolean apagao = view.cenasRenderizadas.contains(Estado.CENA_APAGAO);
        return new RelatorioExecucao(estadoFinal, view.cenasRenderizadas, apagao,
                view.curriculoExibido, view.epilogosExibidos, observacoes.get(), view.conquistas);
    }

    private static Escolha selecionarEscolha(Cena cena, List<Escolha> disponiveis, Map<Integer, String> mapa) {
        String filtro = mapa.get(cena.getId());
        if (filtro != null) {
            String procurado = filtro.toLowerCase(Locale.ROOT);
            List<Escolha> matches = disponiveis.stream()
                    .filter(e -> e.getTextoAlternativa().toLowerCase(Locale.ROOT).contains(procurado))
                    .toList();
            assertEquals(1, matches.size(),
                    "Cena " + cena.getId() + ": esperava 1 escolha contendo '" + filtro + "', encontradas: "
                            + disponiveis.stream().map(Escolha::getTextoAlternativa).toList());
            return matches.get(0);
        }
        if (cena.getId() == Estado.CENA_APAGAO) {
            return disponiveis.get(0);
        }
        assertEquals(1, disponiveis.size(),
                "Cena " + cena.getId() + " possui bifurcação não mapeada: "
                        + disponiveis.stream().map(Escolha::getTextoAlternativa).toList());
        return disponiveis.get(0);
    }

    @Test
    @DisplayName("Todas as 3 trilhas e 5 finais de carreira concluem com sucesso e persistem no progresso global")
    void todasAsCincoRotasFinaisEPersistenciaCumulativa() throws Exception {
        // 1) IC -> PESQUISA_BRASIL (Tiana)
        Map<Integer, String> rotaPesquisa = baseComum(IdentidadeProtagonista.STHEFFANNY);
        rotaPesquisa.put(305, "Priorizar a IC");
        rotaPesquisa.put(400, "Falar meu nome");
        rotaPesquisa.put(402, "Pedir a Bia");
        rotaPesquisa.put(403, "Dizer que entendo");
        rotaPesquisa.put(401, "Mandar mensagem");
        rotaPesquisa.put(405, "Assumir a coordenação");
        rotaPesquisa.put(451, "Ir na assembleia");
        rotaPesquisa.put(455, "Dividir as mensagens");
        rotaPesquisa.put(460, "Retomar o laboratório");
        rotaPesquisa.put(410, "Continuar e registrar");
        rotaPesquisa.put(415, "Falar do projeto");
        rotaPesquisa.put(416, "Confirmar a candidatura");
        rotaPesquisa.put(540, "Encarar as exigências");
        rotaPesquisa.put(541, "Pedir prorrogação");
        rotaPesquisa.put(567, "Voltar ao projeto");
        rotaPesquisa.put(561, "Aceitar.");
        rotaPesquisa.put(600, "Uma página por dia");
        rotaPesquisa.put(601, "Perguntar, com educação");
        rotaPesquisa.put(654, "Contestei uma ordem");

        RelatorioExecucao r1 = executarRotaCompleta(protagonistaPadrao(IdentidadeProtagonista.STHEFFANNY), rotaPesquisa, true);
        assertEquals(Estado.Trilha.IC, r1.estadoFinal().getTrilha());
        assertEquals(Final.PESQUISA_BRASIL.getCodigo(), r1.estadoFinal().getCodigoFinal());
        assertTrue(r1.cenasVisitadas().contains(662));
        assertTrue(r1.exibiuCurriculo());
        assertTrue(r1.exibiuEpilogos());
        assertTrue(r1.notificacoesObserver() > 10);

        // 2) DTEC -> TECNOLOGIA (Miles)
        Map<Integer, String> rotaTecnologia = baseComum(IdentidadeProtagonista.DAVI);
        rotaTecnologia.put(305, "Priorizar a monitoria");
        rotaTecnologia.put(450, "Montar um exercício em etapas");
        rotaTecnologia.put(452, "Perguntar a Bia");
        rotaTecnologia.put(453, "Anotar o aviso");
        rotaTecnologia.put(451, "Ir na assembleia");
        rotaTecnologia.put(455, "Dividir as mensagens");
        rotaTecnologia.put(460, "Conversar com Meira");
        rotaTecnologia.put(470, "Ainda quero");
        rotaTecnologia.put(471, "Devolver perguntas");
        rotaTecnologia.put(540, "Organizar a retomada");
        rotaTecnologia.put(542, "Marcar um horário");
        rotaTecnologia.put(567, "Retomar a monitoria");
        rotaTecnologia.put(562, "Aceitar.");
        rotaTecnologia.put(600, "Uma página por dia");
        rotaTecnologia.put(602, "Dizer que ele está pronto");
        rotaTecnologia.put(654, "Na greve, aprendi");

        RelatorioExecucao r2 = executarRotaCompleta(protagonistaPadrao(IdentidadeProtagonista.DAVI), rotaTecnologia, false);
        assertEquals(Estado.Trilha.DTEC, r2.estadoFinal().getTrilha());
        assertEquals(Final.TECNOLOGIA.getCodigo(), r2.estadoFinal().getCodigoFinal());
        assertTrue(r2.cenasVisitadas().contains(663));

        // 3) INDEP -> CAMINHO_PROPRIO (Tiana)
        Map<Integer, String> rotaProprio = baseComum(IdentidadeProtagonista.STHEFFANNY);
        rotaProprio.put(305, "Não assumir bolsa; explorar projetos independentes");
        rotaProprio.put(500, "Comparar referências");
        rotaProprio.put(501, "Cobrar agora");
        rotaProprio.put(502, "Aceitar o contato");
        rotaProprio.put(503, "Conferir a entrega com o cliente");
        rotaProprio.put(451, "Ir na assembleia");
        rotaProprio.put(455, "Dividir as mensagens");
        rotaProprio.put(460, "Conferir o desfecho do contrato pendente");
        rotaProprio.put(540, "Responder às demandas");
        rotaProprio.put(543, "Negociar prazo e valor");
        rotaProprio.put(567, "Priorizar novos clientes");
        rotaProprio.put(5631, "Aceitar.");
        rotaProprio.put(600, "Uma página por dia");
        rotaProprio.put(603, "Negociar a cláusula 14");
        rotaProprio.put(654, "Entreguei o que consegui");

        RelatorioExecucao r3 = executarRotaCompleta(protagonistaPadrao(IdentidadeProtagonista.STHEFFANNY), rotaProprio, false);
        assertEquals(Estado.Trilha.INDEP, r3.estadoFinal().getTrilha());
        assertEquals(Final.CAMINHO_PROPRIO.getCodigo(), r3.estadoFinal().getCodigoFinal());
        assertTrue(r3.cenasVisitadas().contains(665));

        // 4) Perdeu prazo + Plano internacional -> INTERNACIONAL (Miles)
        Map<Integer, String> rotaInternacional = baseComum(IdentidadeProtagonista.DAVI);
        rotaInternacional.put(7, "Pegar o ônibus e descansar");
        rotaInternacional.put(101, "Explorar a Feira");
        rotaInternacional.put(116, "Observar os estandes");
        rotaInternacional.put(214, "Agradecer e recusar");
        rotaInternacional.put(300, "Conferir a oferta");
        rotaInternacional.put(301, "Perguntar sobre o edital de cooperação acadêmica internacional");
        rotaInternacional.put(3013, "Salvar o edital e montar um plano");
        rotaInternacional.put(305, "Deixar a decisão para depois");
        rotaInternacional.put(306, "Deixar o prazo passar");
        rotaInternacional.put(500, "Comparar referências");
        rotaInternacional.put(501, "Cobrar agora");
        rotaInternacional.put(502, "Aceitar o contato");
        rotaInternacional.put(503, "Aprender a fazer contrato");
        rotaInternacional.put(451, "Ir na assembleia");
        rotaInternacional.put(455, "Dividir as mensagens");
        rotaInternacional.put(460, "Rever documentos e mensagens");
        rotaInternacional.put(540, "Responder às demandas");
        rotaInternacional.put(543, "Negociar prazo e valor");
        rotaInternacional.put(567, "Focar nos projetos independentes");
        rotaInternacional.put(563, "Aceitar.");
        rotaInternacional.put(600, "Uma página por dia");
        rotaInternacional.put(603, "Negociar a cláusula 14");
        rotaInternacional.put(654, "A candidatura internacional exigiu");

        RelatorioExecucao r4 = executarRotaCompleta(protagonistaPadrao(IdentidadeProtagonista.DAVI), rotaInternacional, false);
        assertEquals(Final.INTERNACIONAL.getCodigo(), r4.estadoFinal().getCodigoFinal());
        assertTrue(r4.cenasVisitadas().contains(664));

        // 5) Perdeu prazo + Trancamento -> TRANSICAO (Tiana)
        Map<Integer, String> rotaTransicao = baseComum(IdentidadeProtagonista.STHEFFANNY);
        rotaTransicao.put(7, "Pegar o ônibus e descansar");
        rotaTransicao.put(214, "Agradecer e recusar");
        rotaTransicao.put(305, "Deixar a decisão para depois");
        rotaTransicao.put(306, "Deixar o prazo passar");
        rotaTransicao.put(500, "Aceitar.");
        rotaTransicao.put(501, "Dar mais uma semana");
        rotaTransicao.put(502, "Revisar o combinado");
        rotaTransicao.put(503, "Desistir.");
        rotaTransicao.put(451, "Ir pra casa");
        rotaTransicao.put(455, "Ouvir, sem oferecer ajuda");
        rotaTransicao.put(460, "Rever documentos e mensagens");
        rotaTransicao.put(540, "Responder às demandas");
        rotaTransicao.put(543, "Dizer não");
        rotaTransicao.put(554, "Solicitar trancamento");
        rotaTransicao.put(560, "Trancar o semestre");
        rotaTransicao.put(600, "Começar amanhã");
        rotaTransicao.put(603, "Recusar. Esse preço");
        rotaTransicao.put(654, "Não sabia. Pedi ajuda");

        RelatorioExecucao r5 = executarRotaCompleta(protagonistaPadrao(IdentidadeProtagonista.STHEFFANNY), rotaTransicao, false);
        assertEquals(Final.TRANSICAO.getCodigo(), r5.estadoFinal().getCodigoFinal());
        assertEquals(1, r5.estadoFinal().getSemestresExtras(), "Trancamento deve registrar 1 semestre extra");
        assertTrue(r5.cenasVisitadas().contains(666));

        // Valida persistência cumulativa após "reiniciar" (novo GerenciadorProgresso lendo do disco)
        Progresso persistido = new GerenciadorProgresso(pasta).carregar();
        for (Final f : Final.values()) {
            if (f.isFinalDeCarreira()) {
                assertTrue(persistido.alcancou(f),
                        "Final de carreira deveria estar persistido em progresso.json: " + f);
            }
        }
        assertFalse(persistido.conquistasDesbloqueadas().isEmpty(),
                "Conquistas desbloqueadas devem permanecer persistidas em progresso.json");
        assertFalse(persistido.colecionaveisColetados().isEmpty(),
                "Colecionáveis descobertos devem permanecer persistidos em progresso.json");
    }

    @Test
    @DisplayName("Rota com origem apertada e experiência negativa atravessa o evento de apagão e conclui normalmente")
    void rotaComApagaoERetomadaAteOFinal() throws Exception {
        Protagonista vulneravel = new Protagonista(
                "Tiana", IdentidadeProtagonista.STHEFFANNY,
                false, false, false,
                Protagonista.NivelVidaSocial.ISOLADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEGATIVA,
                Protagonista.SituacaoEconomica.APERTADA);
        assertEquals(35, vulneravel.getEnergiaMaxima());

        Map<Integer, String> rotaComGastoAlto = baseComum(IdentidadeProtagonista.STHEFFANNY);
        rotaComGastoAlto.put(6, "Assumir a mesa");
        rotaComGastoAlto.put(7, "Pegar o ônibus e descansar");
        rotaComGastoAlto.put(201, "Enfrentar tudo");
        rotaComGastoAlto.put(214, "Agradecer e recusar");
        rotaComGastoAlto.put(212, "Passei, mas preciso revisar");
        rotaComGastoAlto.put(216, "Aceitar a média");
        rotaComGastoAlto.put(300, "Procurar Caio");
        rotaComGastoAlto.put(305, "Não concorrer e reconsiderar");
        rotaComGastoAlto.put(500, "Aceitar.");
        rotaComGastoAlto.put(501, "Dar mais uma semana");
        rotaComGastoAlto.put(502, "Revisar o combinado");
        rotaComGastoAlto.put(503, "Aprender a fazer contrato");
        rotaComGastoAlto.put(451, "Ir pra casa");
        rotaComGastoAlto.put(455, "Ouvir, sem oferecer ajuda");
        rotaComGastoAlto.put(460, "Rever documentos e mensagens");
        rotaComGastoAlto.put(540, "Responder às demandas");
        rotaComGastoAlto.put(543, "Dizer não");
        rotaComGastoAlto.put(554, "Solicitar trancamento");
        rotaComGastoAlto.put(560, "Trancar o semestre");
        rotaComGastoAlto.put(600, "Começar amanhã");
        rotaComGastoAlto.put(603, "Negociar a cláusula 14");
        rotaComGastoAlto.put(649, "Comemorar em casa");
        rotaComGastoAlto.put(654, "Entreguei o que consegui");

        RelatorioExecucao r = executarRotaCompleta(vulneravel, rotaComGastoAlto, false);
        assertTrue(r.passouPorApagao(), "Protagonista com energia baixa deveria acionar o evento de apagão (970)");
        assertTrue(r.estadoFinal().isPartidaConcluida());
        assertEquals(Final.CAMINHO_PROPRIO.getCodigo(), r.estadoFinal().getCodigoFinal());
    }
}
