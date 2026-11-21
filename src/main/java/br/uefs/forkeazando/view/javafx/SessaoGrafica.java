package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.adapter.GerenciamentoPartidas;
import br.uefs.forkeazando.controller.GatilhoAutosave;
import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.dao.GerenciadorSlots;
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.*;
import br.uefs.forkeazando.controller.CenasController;
import br.uefs.forkeazando.observer.ObservadorPartida;
import br.uefs.forkeazando.roteiro.Roteiro;
import br.uefs.forkeazando.view.ApresentacaoCena;

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
    public SessaoGrafica(GerenciamentoPartidas gerenciamentoPartidas) {
        this.gerenciamentoPartidas = gerenciamentoPartidas;
        this.cenas = Roteiro.todasAsCenas().stream().collect(Collectors.toMap(Cena::getId, Function.identity()));
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
        Objects.requireNonNull(apresentacao, "A apresentação não pode ser nula.");
        GerenciadorProgresso gerenciador = new GerenciadorProgresso(GerenciadorSlots.PASTA_PADRAO);

        Progresso progresso = gerenciador.carregar();
        this.controladorCenas = new CenasController(estado(), List.copyOf(cenas.values()), apresentacao, progresso, gerenciador,
                estadoAtual -> GerenciadorSlots.salvarAutosave(estadoAtual, GerenciadorSlots.PASTA_PADRAO), new GatilhoAutosave(5),
                () -> {
                    throw new UnsupportedOperationException(
                            "Salvamento manual ainda não integrado ao JavaFX.");
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

    public void carregarPartida(Estado estadoCarregado){
        Objects.requireNonNull(estadoCarregado, "O estado carregado não pode está nulo");
        List<String> problemas = estadoCarregado.verificarIntegridade();

        if (!problemas.isEmpty()) {
            throw new IllegalArgumentException(
                    String.join("; ", problemas)
            );
        }

        if (!cenas.containsKey(estadoCarregado.getCenaAtualId())) {
            throw new IllegalArgumentException("Cena inexistente no roteiro");
        }

        this.estado = estadoCarregado;
        controladorCenas = null;
        notificarObservadores();
    }

    public boolean possuiPartida() {
        return estado != null;
    }

    public Estado estado() {
        if (estado == null) throw new IllegalStateException("Nenhuma partida em andamento.");
        return estado;
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
        GerenciadorSlots.salvarAutosave(estado, GerenciadorSlots.PASTA_PADRAO);
        estado = null;
        controladorCenas = null;
    }

    public void salvarEmSlot(int numSlot) throws SaveException{
        gerenciamentoPartidas.salvarSlot(estado(), numSlot);
    }

}
