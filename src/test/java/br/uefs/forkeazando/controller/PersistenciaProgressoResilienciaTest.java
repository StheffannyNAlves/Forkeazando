package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.adapter.CarregamentoAdapter;
import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.dao.GerenciadorSlots;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.*;
import br.uefs.forkeazando.view.ApresentacaoCena;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de regressão para resiliência e idempotência da persistência de progresso global:
 * 1) Consistência memória vs. disco e nova tentativa após falha temporária de gravação.
 * 2) Recuperação idempotente diante de falha entre progresso.json e autosave.json.
 * 3) Ausência de perda de conquistas, colecionáveis e finais entre CenasController e SessaoGrafica.
 */
class PersistenciaProgressoResilienciaTest {

    @TempDir
    Path pasta;

    private static Protagonista protagonista() {
        return new Protagonista("Tiana", IdentidadeProtagonista.STHEFFANNY,
                false, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
    }

    private static ApresentacaoCena apresentacaoNoOp() {
        return (ApresentacaoCena) Proxy.newProxyInstance(
                ApresentacaoCena.class.getClassLoader(),
                new Class<?>[]{ApresentacaoCena.class},
                (proxy, metodo, args) -> null);
    }

    /**
     * Bloqueia a gravação atômica de um arquivo criando um diretório não-vazio no caminho `.tmp`.
     * Funciona de forma determinística independente de permissões POSIX/root.
     */
    private Path bloquearGravacaoAtomica(String nomeArquivo) throws IOException {
        Path tmpDir = pasta.resolve(nomeArquivo + ".tmp");
        Files.createDirectories(tmpDir);
        Files.writeString(tmpDir.resolve("trava"), "bloqueado");
        return tmpDir;
    }

    private void liberarGravacaoAtomica(Path tmpDir) throws IOException {
        Files.deleteIfExists(tmpDir.resolve("trava"));
        Files.deleteIfExists(tmpDir);
    }

    @Test
    @DisplayName("1) Falha de gravação não muta Progresso em memória e permite recuperação idempotente")
    void falhaDeGravacaoNaoMutaProgressoEmMemoriaEPermiteNovaTentativaParaFinal() throws Exception {
        Protagonista p = protagonista();
        Estado estado = new Estado();
        estado.iniciar(p);
        while (estado.getCapituloAtual() < Estado.CAPITULO_FINAL) {
            estado.avancarCapitulo();
        }
        estado.setCenaAtualId(0);

        Escolha encerrar = new Escolha.Builder("Formar", Estado.CENA_ENCERRAR)
                .comFlagConcedida("item_cartao_ru")
                .comFlagConcedida("cap3_concluido")
                .comFlagConcedida("futuro_tecnologia")
                .comFlagConcedida(Final.FLAG_CONCLUSAO)
                .build();
        Cena cenaFinal = new Cena(0, "Capítulo 8", "Narrador", "Encerramento");
        cenaFinal.adicionarEscolha(encerrar);

        Progresso progressoMemoria = new Progresso();
        GerenciadorProgresso gerenciador = new GerenciadorProgresso(pasta);
        CenaViewFalsa view = new CenaViewFalsa();
        CenasController controller = new CenasController(
                estado, List.of(cenaFinal), view, progressoMemoria, gerenciador,
                e -> {}, new GatilhoAutosave(100), () -> {});

        Path bloqueio = bloquearGravacaoAtomica("progresso.json");
        try {
            controller.prepararCenaAtual();
            CenasController.ResultadoTransicao res = controller.registrarEscolha(encerrar);

            assertEquals(CenasController.ResultadoTransicao.FIM_DE_JOGO, res);
            assertTrue(estado.isPartidaConcluida());
            assertEquals(Final.TECNOLOGIA.getCodigo(), estado.getCodigoFinal());

            // Memória deve permanecer fiel ao disco após falha de gravação
            assertFalse(Files.exists(pasta.resolve("progresso.json")));
            assertFalse(progressoMemoria.alcancou(Final.TECNOLOGIA),
                    "Final não persistido não pode constar como salvo em memória");
            assertFalse(progressoMemoria.coletou(Colecionavel.CARTAO_RU),
                    "Colecionável não persistido não pode constar como salvo em memória");
            assertFalse(progressoMemoria.possui(Conquista.PRIMEIRO_PASSO),
                    "Conquista não persistida não pode constar como salva em memória");
        } finally {
            liberarGravacaoAtomica(bloqueio);
        }

        // Recuperação após liberar o disco
        controller.persistirProgressoAtual();

        assertTrue(progressoMemoria.alcancou(Final.TECNOLOGIA));
        assertTrue(progressoMemoria.coletou(Colecionavel.CARTAO_RU));
        assertTrue(progressoMemoria.possui(Conquista.PRIMEIRO_PASSO));

        Progresso recarregado = new GerenciadorProgresso(pasta).carregar();
        assertTrue(recarregado.alcancou(Final.TECNOLOGIA));
        assertTrue(recarregado.coletou(Colecionavel.CARTAO_RU));
        assertTrue(recarregado.possui(Conquista.PRIMEIRO_PASSO));

        // Nova chamada é idempotente e preserva timestamps originais
        LocalDateTime dataFinalAntes = recarregado.quandoAlcancou(Final.TECNOLOGIA).orElseThrow();
        controller.persistirProgressoAtual();
        Progresso aposRepeticao = new GerenciadorProgresso(pasta).carregar();
        assertEquals(dataFinalAntes, aposRepeticao.quandoAlcancou(Final.TECNOLOGIA).orElseThrow());
    }

    @Test
    @DisplayName("1b) Falha de gravação em conquista mantém Progresso em memória consistente e recupera na próxima verificação")
    void falhaDeGravacaoEmConquistaMantemMemoriaFielAoDiscoERecuperaNaProximaVerificacao() throws Exception {
        Protagonista p = protagonista();
        p.adicionarFlag("cap3_concluido");
        Progresso progresso = new Progresso();
        List<String> mensagens = new ArrayList<>();
        List<Conquista> notificadas = new ArrayList<>();
        GerenciadorConquistas gerenciador = new GerenciadorConquistas(new GerenciadorProgresso(pasta), mensagens::add);
        gerenciador.carregarDesbloqueadas(progresso);

        Path bloqueio = bloquearGravacaoAtomica("progresso.json");
        try {
            gerenciador.verificar(p, progresso, notificadas::add);
            assertEquals(List.of(Conquista.PRIMEIRO_PASSO), notificadas);
            assertFalse(progresso.possui(Conquista.PRIMEIRO_PASSO),
                    "Conquista não persistida não deve aparecer como salva no Progresso em memória");
            assertEquals(1, mensagens.size());
        } finally {
            liberarGravacaoAtomica(bloqueio);
        }

        gerenciador.verificar(p, progresso, notificadas::add);

        assertEquals(1, notificadas.size(), "Não deve notificar a conquista novamente no retry");
        assertTrue(progresso.possui(Conquista.PRIMEIRO_PASSO));
        assertTrue(new GerenciadorProgresso(pasta).carregar().possui(Conquista.PRIMEIRO_PASSO));
    }

    @Test
    @DisplayName("2a) Falha no autosave durante finalizarPartida preserva final salvo, mantém sessão e permite retry idempotente")
    void falhaNoAutosaveAposGravarProgressoPreservaFinalESessaoEPermiteRetryIdempotente() throws Exception {
        SessaoGrafica sessao = new SessaoGrafica(new CarregamentoAdapter(pasta), pasta);
        sessao.novaPartida(protagonista(), apresentacaoNoOp());
        sessao.estado().getProtagonista().adicionarFlag("item_cartao_ru");
        sessao.estado().concluir(Final.TECNOLOGIA.getCodigo());

        Path bloqueioAutosave = bloquearGravacaoAtomica("autosave.json");
        try {
            assertThrows(SaveException.class, sessao::finalizarPartida);
            assertTrue(sessao.possuiPartida(), "Sessão deve permanecer ativa em memória após falha no autosave");
            assertFalse(Files.exists(GerenciadorSlots.caminhoDoAutosave(pasta)));

            Progresso jaSalvo = new GerenciadorProgresso(pasta).carregar();
            assertTrue(jaSalvo.alcancou(Final.TECNOLOGIA), "Final já deve estar protegido em progresso.json");
            assertTrue(jaSalvo.coletou(Colecionavel.CARTAO_RU));
        } finally {
            liberarGravacaoAtomica(bloqueioAutosave);
        }

        LocalDateTime dataOriginal = new GerenciadorProgresso(pasta).carregar()
                .quandoAlcancou(Final.TECNOLOGIA).orElseThrow();

        // Retry após recuperar o disco
        sessao.finalizarPartida();

        assertFalse(sessao.possuiPartida());
        assertTrue(Files.exists(GerenciadorSlots.caminhoDoAutosave(pasta)));
        LocalDateTime dataDepois = new GerenciadorProgresso(pasta).carregar()
                .quandoAlcancou(Final.TECNOLOGIA).orElseThrow();
        assertEquals(dataOriginal, dataDepois, "Retry deve ser idempotente e preservar o timestamp original");
    }

    @Test
    @DisplayName("2b) Autosave concluído sem registro correspondente em progresso.json é recuperado automaticamente de forma idempotente")
    void autosaveConcluidoSemProgressoRecuperaFinalAutomaticamenteAoConsultarProgresso() throws Exception {
        // Simula queda entre a gravação de autosave.json (concluído) e progresso.json
        Protagonista p = protagonista();
        p.adicionarFlag("item_cartao_ru");
        p.adicionarFlag("cap3_concluido");
        Estado concluido = new Estado();
        concluido.iniciar(p);
        concluido.concluir(Final.INTERNACIONAL.getCodigo());
        GerenciadorSlots.salvarAutosave(concluido, pasta);

        assertFalse(Files.exists(pasta.resolve("progresso.json")));

        // Nova sessão após "reiniciar o jogo"
        SessaoGrafica novaSessao = new SessaoGrafica(new CarregamentoAdapter(pasta), pasta);
        Progresso recuperado = novaSessao.consultarProgresso();

        assertTrue(recuperado.alcancou(Final.INTERNACIONAL), "Final do autosave concluído deve ser recuperado");
        assertTrue(recuperado.coletou(Colecionavel.CARTAO_RU), "Colecionável do autosave deve ser recuperado");
        assertTrue(recuperado.possui(Conquista.PRIMEIRO_PASSO), "Conquista do autosave deve ser recuperada");
        assertTrue(Files.exists(pasta.resolve("progresso.json")), "Recuperação deve ser persistida em progresso.json");

        LocalDateTime dataRecuperada = recuperado.quandoAlcancou(Final.INTERNACIONAL).orElseThrow();

        // Nova consulta não reescreve nem altera o timestamp
        Progresso segundaConsulta = novaSessao.consultarProgresso();
        assertEquals(dataRecuperada, segundaConsulta.quandoAlcancou(Final.INTERNACIONAL).orElseThrow());
    }

    @Test
    @DisplayName("2c) Autosave corrompido não impede consultar progresso global já persistido")
    void autosaveCorrompidoNaoImpedeConsultarProgresso() throws Exception {
        Progresso existente = new Progresso();
        existente.registrarFinal(Final.TRANSICAO, LocalDateTime.of(2026, 11, 20, 12, 0));
        new GerenciadorProgresso(pasta).salvar(existente);
        Files.writeString(GerenciadorSlots.caminhoDoAutosave(pasta), "{json corrompido");

        SessaoGrafica sessao = new SessaoGrafica(new CarregamentoAdapter(pasta), pasta);
        Progresso lido = sessao.consultarProgresso();

        assertTrue(lido.alcancou(Final.TRANSICAO));
    }

    @Test
    @DisplayName("3) Conquistas e colecionáveis pendentes durante falha temporária são consolidados com o final na SessaoGrafica")
    void conquistasEColecionaveisPendentesDuranteAPartidaNaoSePerdemAoFinalizarNaSessaoGrafica() throws Exception {
        CarregamentoAdapter adapter = new CarregamentoAdapter(pasta);
        SessaoGrafica sessao = new SessaoGrafica(adapter, pasta);
        sessao.novaPartida(protagonista(), apresentacaoNoOp());

        // Simula falha temporária de escrita em progresso.json enquanto o jogador conquista itens/marcos
        Path bloqueioProgresso = bloquearGravacaoAtomica("progresso.json");
        try {
            Protagonista p = sessao.estado().getProtagonista();
            p.adicionarFlag("item_cartao_ru");
            p.adicionarFlag("cap3_concluido");
            sessao.estado().concluir(Final.PESQUISA_BRASIL.getCodigo());

            assertThrows(SaveException.class, sessao::finalizarPartida,
                    "Falha ao persistir progresso global deve impedir encerramento silencioso");
            assertTrue(sessao.possuiPartida(), "Sessão permanece em memória para recuperação");
            assertFalse(Files.exists(pasta.resolve("progresso.json")));
        } finally {
            liberarGravacaoAtomica(bloqueioProgresso);
        }

        // Disco volta a responder: retry consolida final + conquista + colecionável sem divergência de instâncias
        sessao.finalizarPartida();

        assertFalse(sessao.possuiPartida());
        Progresso persistido = new GerenciadorProgresso(pasta).carregar();
        assertTrue(persistido.alcancou(Final.PESQUISA_BRASIL));
        assertTrue(persistido.coletou(Colecionavel.CARTAO_RU));
        assertTrue(persistido.possui(Conquista.PRIMEIRO_PASSO));
    }
}
