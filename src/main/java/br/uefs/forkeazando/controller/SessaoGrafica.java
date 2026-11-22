package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.adapter.GerenciamentoPartidas;
import br.uefs.forkeazando.dao.ConfiguracoesAdmin;
import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.dao.GerenciadorSlots;
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.*;
import br.uefs.forkeazando.observer.ObservadorPartida;
import br.uefs.forkeazando.roteiro.Roteiro;
import br.uefs.forkeazando.view.ApresentacaoCena;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SessaoGrafica {
    private final Map<Integer, Cena> cenas;
    private final List<ObservadorPartida> observadores = new ArrayList<>();
    private Estado estado;
    private CenasController controladorCenas;
    private final GerenciamentoPartidas gerenciamentoPartidas;
    private final Path pastaProgresso;

    public SessaoGrafica(GerenciamentoPartidas gerenciamentoPartidas) {
        this(gerenciamentoPartidas, GerenciadorSlots.PASTA_PADRAO);
    }

    // Injecao de caminho para testar falhas sem modificar saves reais.
    SessaoGrafica(GerenciamentoPartidas gerenciamentoPartidas, Path pastaProgresso) {
        this.gerenciamentoPartidas = Objects.requireNonNull(gerenciamentoPartidas);
        this.pastaProgresso = Objects.requireNonNull(pastaProgresso);
        this.cenas = Roteiro.todasAsCenas().stream()
                .collect(Collectors.toMap(Cena::getId, Function.identity()));
    }

    public void adicionarObservador(ObservadorPartida observador) {
        observadores.add(Objects.requireNonNull(observador));
    }
    private void notificarObservadores() {
        for (ObservadorPartida observador : observadores) {
            observador.atualizar(estado);
        }
    }

    public CenasController.ResultadoTransicao encaminharEscolha(Escolha escolha) {
        if (controladorCenas == null) {
            throw new IllegalStateException("Controlador da partida não inicializado.");
        }
        CenasController.ResultadoTransicao resultadoTransicao = controladorCenas.registrarEscolha(escolha);
        notificarObservadores();
        return resultadoTransicao;
    }

    public void inicializarControladorPartida(ApresentacaoCena apresentacao)
            throws CarregamentoException, DadosInvalidosException {
        if (!possuiPartida()) {
            throw new IllegalStateException("Nenhuma partida em andamento.");
        }
        this.controladorCenas = criarControlador(estado(), apresentacao);
    }

    private CenasController criarControlador(Estado estadoPartida, ApresentacaoCena apresentacao)
            throws CarregamentoException, DadosInvalidosException {
        Objects.requireNonNull(apresentacao, "A apresentacao nao pode ser nula.");
        GerenciadorProgresso gerenciador = new GerenciadorProgresso(pastaProgresso);
        Progresso progresso = carregarProgressoComRecuperacao(gerenciador);
        return new CenasController(estadoPartida, List.copyOf(cenas.values()),
                apresentacao, progresso, gerenciador,
                estadoAtual -> GerenciadorSlots.salvarAutosave(estadoAtual, pastaProgresso),
                new GatilhoAutosave(5),
                () -> {
                    throw new UnsupportedOperationException(
                            "No JavaFX o salvamento manual e feito pelo menu Salvar (SessaoGrafica.salvarEmSlot); "
                            + "este callback so e usado pelo laco de menu do terminal.");
                }
        );
    }

    public void prepararCenaAtual(){
        if (controladorCenas == null)
            throw new IllegalStateException("Controlador de partida nao iniciado");
        controladorCenas.prepararCenaAtual();
        notificarObservadores();
    }

    public void novaPartida(Protagonista protagonista) {
        Estado novo = new Estado();
        novo.iniciar(protagonista);
        novo.setCenaAtualId(700);
        estado = novo;
        controladorCenas = null;
        notificarObservadores();
    }

    /**
     * Prepara a nova partida por completo antes de substituir a sessao atual
     * Se a leitura do progresso ou a criacao do controlador falhar, a partida
     * anterior e seu controlador permanecem intactos.
     */
    public void novaPartida(Protagonista protagonista, ApresentacaoCena apresentacao)
            throws CarregamentoException, DadosInvalidosException {
        Objects.requireNonNull(protagonista, "O protagonista nao pode ser nulo.");
        Objects.requireNonNull(apresentacao, "A apresentacao nao pode ser nula.");

        Estado novo = new Estado();
        novo.iniciar(protagonista);
        novo.setCenaAtualId(700);

        CenasController preparado = criarControlador(novo, apresentacao);
        this.estado = novo;
        this.controladorCenas = preparado;
        notificarObservadores();
    }


    // Valida os dados da tela de criação e inicia a partida (ver novaPartida com Protagonista)
    public void novaPartida(DadosProtagonista dados, ApresentacaoCena apresentacao)
            throws CarregamentoException, DadosInvalidosException {
        Objects.requireNonNull(dados, "Os dados do protagonista não podem ser nulos.");
        novaPartida(dados.criarProtagonista(), apresentacao);
    }

    // a View fala só com a sessão, que passa para o Adapter

    public List<ResumoSlot> listarSlots() {
        return gerenciamentoPartidas.listarSlots();
    }

    public boolean existeAutosave() {
        return gerenciamentoPartidas.existeAutosave();
    }

    public Estado carregarAutosave() throws CarregamentoException, DadosInvalidosException {
        return gerenciamentoPartidas.carregarAutosave();
    }

    public void excluirSlot(int numeroSlot) throws SaveException {
        gerenciamentoPartidas.excluirSlot(numeroSlot);
    }

    public void excluirAutosave() throws SaveException {
        gerenciamentoPartidas.excluirAutosave();
    }


    public Configuracoes carregarConfiguracoes() throws CarregamentoException, DadosInvalidosException {
        return new Configuracoes(ConfiguracoesAdmin.get());
    }

    public void salvarConfiguracoes(Configuracoes novas) throws SaveException, DadosInvalidosException {
        ConfiguracoesAdmin.salvarConfigPersonalizadas(novas);
    }

    public void carregarPartida(Estado estadoCarregado) {
        validarEstadoCarregado(estadoCarregado);
        this.estado = estadoCarregado;
        controladorCenas = null;
        notificarObservadores();
    }

    // Prepara o novo controlador ANTES de substituir a partida em memoria
    public void carregarPartida(Estado estadoCarregado, ApresentacaoCena apresentacao)
            throws CarregamentoException, DadosInvalidosException {
        validarEstadoCarregado(estadoCarregado);
        CenasController preparado = criarControlador(estadoCarregado, apresentacao);
        this.estado = estadoCarregado;
        this.controladorCenas = preparado;
        notificarObservadores();
    }

    private void validarEstadoCarregado(Estado estadoCarregado) {
        Objects.requireNonNull(estadoCarregado, "O estado carregado nao pode ser nulo");
        List<String> problemas = estadoCarregado.verificarIntegridade();
        if (!problemas.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", problemas));
        }
        if (!cenas.containsKey(estadoCarregado.getCenaAtualId())) {
            throw new IllegalArgumentException("Cena inexistente no roteiro");
        }
    }

    public boolean possuiPartida() {
        return estado != null;
    }

    public Estado estado() {
        if (estado == null) throw new IllegalStateException("Nenhuma partida em andamento.");
        return estado;
    }

    /**
     * Consulta o progresso global persistido do jogador, recuperando de forma
     * idempotente eventuais registros de um autosave concluído ainda não sincronizado.
     * Centraliza o acesso aos dados fora do controlador JavaFX.
     */
    public Progresso consultarProgresso()
            throws CarregamentoException, DadosInvalidosException {

        GerenciadorProgresso gerenciador =
                new GerenciadorProgresso(pastaProgresso);

        return carregarProgressoComRecuperacao(gerenciador);
    }

    private Progresso carregarProgressoComRecuperacao(GerenciadorProgresso gerenciador)
            throws CarregamentoException, DadosInvalidosException {
        Progresso progresso = gerenciador.carregar();
        recuperarDeAutosaveConcluido(gerenciador, progresso);
        return progresso;
    }

    private void recuperarDeAutosaveConcluido(GerenciadorProgresso gerenciador, Progresso progresso) {
        if (!gerenciamentoPartidas.existeAutosave()) {
            return;
        }
        try {
            Estado autosave = gerenciamentoPartidas.carregarAutosave();
            if (autosave == null || !autosave.isPartidaConcluida()) {
                return;
            }
            Progresso candidato = new Progresso(progresso);
            if (!candidato.sincronizarEstado(autosave, LocalDateTime.now())) {
                return;
            }
            gerenciador.salvar(candidato);
            progresso.copiarDe(candidato);
        } catch (CarregamentoException | DadosInvalidosException | SaveException ignored) {
            // Autosave ausente/corrompido ou erro temporário de disco não impede consultar o progresso já salvo.
        }
    }

    public Cena cenaAtual() {
        int id = estado().getCenaAtualId();
        Cena cena = cenas.get(id);
        if (cena == null) throw new IllegalStateException("Cena não encontrada: " + id);
        return cena;
    }

    public List<Escolha> escolhasDisponiveis() {
        return cenaAtual().getEscolhasDisponiveis(estado().getProtagonista());
    }

    public void finalizarPartida() throws SaveException {
        if (controladorCenas == null || !possuiPartida()) {
            throw new IllegalStateException("Nenhuma partida ativa.");
        }
        if (!estado.isPartidaConcluida()) {
            throw new IllegalStateException("A partida ainda não possui um final registrado.");
        }
        // Persiste progresso global antes do autosave, usando a mesma instância do controlador;
        // em caso de falha, a sessão permanece em memória para nova tentativa idempotente.
        controladorCenas.persistirProgressoAtual();
        GerenciadorSlots.salvarAutosave(estado, pastaProgresso);
        estado = null;
        controladorCenas = null;
    }

    public void salvarEmSlot(int numSlot) throws SaveException{
        gerenciamentoPartidas.salvarSlot(estado(), numSlot);
    }

}
