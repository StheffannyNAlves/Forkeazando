package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;
import br.uefs.forkeazando.model.Progresso;
import br.uefs.forkeazando.model.Protagonista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa o fluxo do CenasController sem terminal: o menu é respondido por um
 * SeletorRoteirizado e a tela é uma CenaViewFalsa que só registra chamadas.
 */
class CenasControllerTest {

    private static final String CAP = "Capítulo 1";
    private static final String SALVAR = "Salvar partida";
    private static final String GALERIA = "Abrir galeria";
    private static final String VOLTAR = "Voltar ao menu principal";

    @TempDir
    Path pasta;

    private Protagonista p;
    private Estado estado;
    private CenaViewFalsa view;
    private SeletorRoteirizado seletor;
    private Progresso progresso;
    private GerenciadorProgresso gerenciadorProgresso;
    private AtomicInteger autosaves;
    private AtomicInteger salvamentosManuais;

    @BeforeEach
    void preparar() {
        p = new Protagonista("Ana", false, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
        estado = new Estado();
        estado.iniciar(p);
        estado.setCenaAtualId(0);
        view = new CenaViewFalsa();
        seletor = new SeletorRoteirizado();
        progresso = new Progresso();
        gerenciadorProgresso = new GerenciadorProgresso(pasta);
        autosaves = new AtomicInteger();
        salvamentosManuais = new AtomicInteger();
    }

    private static Cena cena(int id, Escolha... escolhas) {
        Cena c = new Cena(id, CAP, "Narrador", "Texto da cena " + id);
        for (Escolha e : escolhas) {
            c.adicionarEscolha(e);
        }
        return c;
    }

    private static Escolha.Builder escolha(String texto, int destino) {
        return new Escolha.Builder(texto, destino);
    }

    private CenasController controller(int cenasPorAutosave, Autosave autosave, Cena... cenas) {
        return new CenasController(estado, List.of(cenas), view, progresso, gerenciadorProgresso,
                autosave, new GatilhoAutosave(cenasPorAutosave), salvamentosManuais::incrementAndGet, seletor);
    }

    private CenasController controller(Cena... cenas) {
        return controller(1000, e -> autosaves.incrementAndGet(), cenas);
    }

    private void irParaUltimoCapitulo() {
        while (estado.getCapituloAtual() < Estado.CAPITULO_FINAL) {
            estado.avancarCapitulo();
        }
    }

    // ---------- escolha e avanço de cena ----------

    @Test
    void escolherUmaOpcaoAplicaOsEfeitosNoEstadoEAvancaParaADestino() {
        Cena inicio = cena(0, escolha("Estudar", 1).comScoreGanho(5).comParticipacaoGanha(2).build());
        Cena proxima = cena(1, escolha("Fim", 1).build());
        seletor.escolhe("Estudar").escolhe(VOLTAR);

        ResultadoPartida resultado = controller(inicio, proxima).iniciar();

        assertEquals(ResultadoPartida.VOLTAR_AO_MENU, resultado);
        assertEquals(5, p.getScore());
        assertEquals(2, p.getParticipacao());
        assertEquals(1, estado.getCenaAtualId());
        assertEquals(List.of(0, 1), view.cenasRenderizadas);
    }

    @Test
    void menuListaAsEscolhasDaCenaMaisAsTresAcoesExtras() {
        Cena inicio = cena(0, escolha("A", 0).build(), escolha("B", 0).build());
        seletor.escolhe(VOLTAR);

        controller(inicio).iniciar();

        assertEquals(List.of("A", "B", GALERIA, SALVAR, VOLTAR), seletor.menusVistos.get(0));
        assertEquals("O QUE VOCÊ VAI FAZER?", seletor.titulosVistos.get(0));
    }

    @Test
    void escolhaBloqueadaPorFlagNaoApareceNoMenu() {
        Cena inicio = cena(0,
                escolha("Aberta", 0).build(),
                escolha("Trancada", 0).comFlagRequerida("flag_que_nao_tenho").build());
        seletor.escolhe(VOLTAR);

        controller(inicio).iniciar();

        assertEquals(List.of("Aberta", GALERIA, SALVAR, VOLTAR), seletor.menusVistos.get(0));
    }

    @Test
    void escolhaQueConcedeFlagRegistraAFlagNaProtagonista() {
        Cena inicio = cena(0, escolha("Falar", 1).comFlagConcedida("cap1_falou").build());
        Cena proxima = cena(1, escolha("x", 1).build());
        seletor.escolhe("Falar").escolhe(VOLTAR);

        controller(inicio, proxima).iniciar();

        assertTrue(p.temFlag("cap1_falou"));
    }

    @Test
    void escolhaQueMudaRelacionamentoMostraOImpactoNaView() {
        Cena inicio = cena(0, escolha("Conversar", 1)
                .comRelacionamento(PersonagemSecundario.VETERANO, 1f).build());
        Cena proxima = cena(1, escolha("x", 1).build());
        seletor.escolhe("Conversar").escolhe(VOLTAR);

        controller(inicio, proxima).iniciar();

        assertEquals(List.of(PersonagemSecundario.VETERANO), view.impactos);
        assertEquals(1f, p.getRelacionamento(PersonagemSecundario.VETERANO));
    }

    @Test
    void escolhaQueDefineTrilhaGravaNoEstadoEAvisaOJogador() {
        Cena inicio = cena(0, escolha("Procurar orientador", 1).comFlagConcedida("trilha_ic").build());
        Cena proxima = cena(1, escolha("x", 1).build());
        seletor.escolhe("Procurar orientador").escolhe(VOLTAR);

        controller(inicio, proxima).iniciar();

        assertEquals(Estado.Trilha.IC, estado.getTrilha());
        assertTrue(view.temMensagemContendo("Iniciação Científica"));
    }

    @Test
    void transicaoDeCapituloApareceUmaVezPorCapitulo() {
        Cena a = cena(0, escolha("a", 1).build());
        Cena b = cena(1, escolha("b", 2).build());
        Cena c = cena(2, escolha("c", 2).build());
        seletor.indice(0).indice(0).escolhe(VOLTAR);

        controller(a, b, c).iniciar();

        assertEquals(List.of(CAP), view.transicoes);
    }

    // ---------- ações extras do menu ----------

    @Test
    void salvarPartidaChamaOSalvamentoManualEMantemAMesmaCena() {
        Cena inicio = cena(0, escolha("Seguir", 1).build());
        seletor.escolhe(SALVAR).escolhe(VOLTAR);

        controller(inicio).iniciar();

        assertEquals(1, salvamentosManuais.get());
        assertEquals(0, estado.getCenaAtualId());
        assertEquals(List.of(0), view.cenasRenderizadas, "a cena não é redesenhada após salvar");
    }

    @Test
    void abrirGaleriaMostraAGaleriaESegueNaMesmaCena() {
        Cena inicio = cena(0, escolha("Seguir", 1).build());
        seletor.escolhe(GALERIA).escolhe(VOLTAR);

        controller(inicio).iniciar();

        assertEquals(1, view.galeriasAbertas);
        assertEquals(0, estado.getCenaAtualId());
    }

    @Test
    void voltarAoMenuPrincipalNaoAlteraAPartida() {
        Cena inicio = cena(0, escolha("Seguir", 1).comScoreGanho(9).build());
        seletor.escolhe(VOLTAR);

        ResultadoPartida resultado = controller(inicio).iniciar();

        assertEquals(ResultadoPartida.VOLTAR_AO_MENU, resultado);
        assertEquals(0, p.getScore());
        assertEquals(0, estado.getCenaAtualId());
    }

    @Test
    void escVoltaAoMenuPrincipal() {
        Cena inicio = cena(0, escolha("Seguir", 1).comScoreGanho(9).build());
        seletor.cancela();

        ResultadoPartida resultado = controller(inicio).iniciar();

        assertEquals(ResultadoPartida.VOLTAR_AO_MENU, resultado);
        assertEquals(0, p.getScore());
    }

    // ---------- fim de capítulo, fim de jogo, erros de rota ----------

    @Test
    void encerrarCapituloLevaAoPrimeiraCenaDoProximoCapitulo() {
        Cena inicio = cena(0, escolha("Fechar capítulo", Estado.CENA_ENCERRAR).build());
        Cena cap2 = cena(100, escolha("Seguir", 100).build());
        seletor.escolhe("Fechar capítulo").escolhe(VOLTAR);

        ResultadoPartida resultado = controller(inicio, cap2).iniciar();

        assertEquals(ResultadoPartida.VOLTAR_AO_MENU, resultado);
        assertEquals(2, estado.getCapituloAtual());
        assertEquals(100, estado.getCenaAtualId());
    }

    @Test
    void encerrarOUltimoCapituloTerminaOJogo() {
        irParaUltimoCapitulo();
        Cena inicio = cena(0, escolha("Formar", Estado.CENA_ENCERRAR).build());
        seletor.escolhe("Formar");

        ResultadoPartida resultado = controller(inicio).iniciar();

        assertEquals(ResultadoPartida.FIM_DE_JOGO, resultado);
        assertTrue(view.temMensagemContendo("Fim. Obrigado por jogar."));
    }

    @Test
    void cenaSemNenhumaEscolhaDisponivelDevolveErroDeRota() {
        Cena beco = cena(0);

        ResultadoPartida resultado = controller(beco).iniciar();

        assertEquals(ResultadoPartida.ERRO_DE_ROTA, resultado);
        assertTrue(view.temMensagemContendo("erro de rota"));
        assertEquals(0, seletor.titulosVistos.size(), "nenhum menu deve abrir");
    }

    @Test
    void cenaInexistenteLancaExcecaoClara() {
        estado.setCenaAtualId(7);

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> controller(cena(0, escolha("x", 0).build())).iniciar());

        assertTrue(e.getMessage().contains("7"));
    }

    @Test
    void concluirFinalDeCarreiraMarcaAPartidaComoConcluida() {
        irParaUltimoCapitulo();
        Cena inicio = cena(0, escolha("Formar", Estado.CENA_ENCERRAR)
                .comFlagConcedida("futuro_tecnologia")
                .comFlagConcedida(br.uefs.forkeazando.model.Final.FLAG_CONCLUSAO)
                .build());
        seletor.escolhe("Formar");

        ResultadoPartida resultado = controller(inicio).iniciar();

        assertEquals(ResultadoPartida.FIM_DE_JOGO, resultado);
        assertTrue(estado.isPartidaConcluida());
        assertEquals("jornada_tecnologia", estado.getCodigoFinal());
    }

    // ---------- apagão (energia zerada) ----------

    @Test
    void zerarAEnergiaDisparaOApagaoEDepoisRetomaOCaminhoOriginal() {
        int energiaMaxima = p.getEnergiaMaxima();
        Cena inicio = cena(0, escolha("Virar a noite", 1).comCustoEnergia(p.getEnergia()).build());
        Cena destino = cena(1, escolha("Seguir", 1).build());
        Cena apagao = cena(Estado.CENA_APAGAO, escolha("Dormir", Estado.CENA_RETOMAR).build());
        seletor.escolhe("Virar a noite").escolhe("Dormir").escolhe(VOLTAR);

        controller(inicio, destino, apagao).iniciar();

        assertEquals(List.of(0, Estado.CENA_APAGAO, 1), view.cenasRenderizadas);
        assertEquals(1, estado.getCenaAtualId());
        assertEquals((int) Math.round(energiaMaxima * 0.4), p.getEnergia());
        assertFalse(estado.temDestinoPendente());
    }

    @Test
    void semEnergiaParaNenhumaEscolhaOApagaoVemAntesDoMenu() {
        p.gastarEnergia(p.getEnergia());
        Cena inicio = cena(0, escolha("Esforço", 0).comCustoEnergia(5).build());
        Cena apagao = cena(Estado.CENA_APAGAO, escolha("Dormir", Estado.CENA_RETOMAR).build());
        seletor.escolhe("Dormir").escolhe(VOLTAR);

        controller(inicio, apagao).iniciar();

        assertEquals(List.of(0, Estado.CENA_APAGAO, 0), view.cenasRenderizadas);
        assertTrue(p.getEnergia() > 0);
    }

    @Test
    void noUltimoCapituloZerarAEnergiaNaoDisparaApagao() {
        irParaUltimoCapitulo();
        Cena inicio = cena(0, escolha("Virar a noite", 1).comCustoEnergia(p.getEnergia()).build());
        Cena destino = cena(1, escolha("Seguir", 1).build());
        seletor.escolhe("Virar a noite").escolhe(VOLTAR);

        controller(inicio, destino).iniciar();

        assertEquals(List.of(0, 1), view.cenasRenderizadas);
        assertEquals(0, p.getEnergia());
    }

    // ---------- autosave ----------

    @Test
    void autosaveDisparaDepoisDeNEscolhasEMostraAviso() {
        Cena a = cena(0, escolha("a", 1).build());
        Cena b = cena(1, escolha("b", 2).build());
        Cena c = cena(2, escolha("c", 2).build());
        seletor.indice(0).indice(0).escolhe(VOLTAR);

        controller(2, e -> autosaves.incrementAndGet(), a, b, c).iniciar();

        assertEquals(1, autosaves.get());
        assertTrue(view.temMensagemContendo("Progresso salvo automaticamente."));
    }

    @Test
    void autosaveNaoDisparaAntesDoLimiar() {
        Cena a = cena(0, escolha("a", 1).build());
        Cena b = cena(1, escolha("b", 1).build());
        seletor.indice(0).indice(0).escolhe(VOLTAR);

        controller(5, e -> autosaves.incrementAndGet(), a, b).iniciar();

        assertEquals(0, autosaves.get());
    }

    @Test
    void falhaNoAutosaveAvisaOJogadorEOJogoContinua() {
        Cena a = cena(0, escolha("a", 1).build());
        Cena b = cena(1, escolha("b", 1).build());
        Autosave quebrado = e -> {
            throw new SaveException("disco cheio");
        };
        seletor.indice(0).escolhe(VOLTAR);

        ResultadoPartida resultado = controller(1, quebrado, a, b).iniciar();

        assertEquals(ResultadoPartida.VOLTAR_AO_MENU, resultado);
        assertTrue(view.temMensagemContendo("Não foi possível salvar automaticamente: disco cheio"));
    }

    // ---------- progresso e conquistas ----------

    @Test
    void escolhaQueCumpreRequisitoDeConquistaNotificaEPersiste() {
        Cena inicio = cena(0, escolha("Concluir", 1).comFlagConcedida("cap3_concluido").build());
        Cena proxima = cena(1, escolha("x", 1).build());
        seletor.escolhe("Concluir").escolhe(VOLTAR);

        controller(inicio, proxima).iniciar();

        assertEquals(List.of(br.uefs.forkeazando.model.Conquista.PRIMEIRO_PASSO), view.conquistas);
        assertTrue(java.nio.file.Files.exists(pasta.resolve("progresso.json")));
    }

    // ---------- construtor ----------

    @Test
    void construtorRejeitaDependenciasNulas() {
        List<Cena> cenas = List.of(cena(0));
        GatilhoAutosave gatilho = new GatilhoAutosave(5);
        Autosave autosave = e -> { };
        Runnable manual = () -> { };

        assertThrows(NullPointerException.class, () -> new CenasController(estado, cenas, view, progresso,
                gerenciadorProgresso, null, gatilho, manual, seletor));
        assertThrows(NullPointerException.class, () -> new CenasController(estado, cenas, view, progresso,
                gerenciadorProgresso, autosave, null, manual, seletor));
        assertThrows(NullPointerException.class, () -> new CenasController(estado, cenas, view, progresso,
                gerenciadorProgresso, autosave, gatilho, null, seletor));
        assertThrows(NullPointerException.class, () -> new CenasController(estado, cenas, view, progresso,
                gerenciadorProgresso, autosave, gatilho, manual, null));
    }
}
