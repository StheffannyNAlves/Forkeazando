package br.uefs.forkeazando.adapter;

import br.uefs.forkeazando.dao.GerenciadorSave;
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.ResumoSlot;
import br.uefs.forkeazando.dao.GerenciadorSlots;

import java.nio.file.Path;
import java.util.List;

// O objetivo desse adapter é
public class CarregamentoAdapter implements AcessoPartidas {
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

}
