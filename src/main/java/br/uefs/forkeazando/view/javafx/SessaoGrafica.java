package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.controller.GatilhoAutosave;
import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.dao.GerenciadorSlots;
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.model.*;
import br.uefs.forkeazando.controller.CenasController;
import br.uefs.forkeazando.roteiro.Roteiro;
import br.uefs.forkeazando.view.ApresentacaoCena;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SessaoGrafica {
    private final Map<Integer, Cena> cenas;
    private Estado estado;
    private CenasController controladorCenas;
    public SessaoGrafica() {
        this.cenas = Roteiro.todasAsCenas().stream().collect(Collectors.toMap(Cena::getId, Function.identity()));
    }
    public CenasController.ResultadoTransicao encaminharEscolha(Escolha escolha) {
        if (controladorCenas == null) {
            throw new IllegalStateException("Controlador da partida não inicializado.");
        }

        return controladorCenas.registrarEscolha(escolha);
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
    }

    public void novaPartida(Protagonista protagonista) {
        Estado novo = new Estado();
        novo.iniciar(protagonista);
        novo.setCenaAtualId(700);
        estado = novo;
        controladorCenas = null;
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

}
