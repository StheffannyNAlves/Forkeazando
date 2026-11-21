package br.uefs.forkeazando.adapter;

import br.uefs.forkeazando.dao.GerenciadorSave;
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.ResumoSlot;
import br.uefs.forkeazando.dao.GerenciadorSlots;

import java.nio.file.Path;
import java.util.List;

/**
 * O objetivo desse Adapter é "traduzir" as operações de acesso
 * e gerenciamento de partidas para as chamadas dos métodos
 * de persistência de GerenciadorSlots e GerenciadorSave,
 * sem expor essas classes a quem utiliza a interface
 */
public class CarregamentoAdapter implements GerenciamentoPartidas {
    private final Path pastaSaves;

    public CarregamentoAdapter(Path pastaSaves) {
        this.pastaSaves = pastaSaves;
    }

    @Override
    public boolean existeAutosave() {
        return GerenciadorSlots.existeAutosave(pastaSaves);
    }

    @Override
    public List<ResumoSlot> listarSlots() {
        return GerenciadorSlots.listar(pastaSaves);
    }

    @Override
    public Estado carregarSlot(int numeroSlot) throws CarregamentoException, DadosInvalidosException {
        return GerenciadorSlots.carregar(pastaSaves, numeroSlot);
    }

    @Override
    public Estado carregarAutosave() throws CarregamentoException, DadosInvalidosException {
        Path caminho = GerenciadorSlots.caminhoDoAutosave(pastaSaves);
        return GerenciadorSave.carregar(caminho);
    }

    @Override
    public void salvarSlot(Estado estado, int numeroSlot) throws SaveException {
        GerenciadorSlots.salvar(estado, pastaSaves, numeroSlot);
    }

    @Override
    public void excluirSlot(int numeroSlot) throws SaveException {
        GerenciadorSlots.excluir(pastaSaves, numeroSlot);
    }
}
