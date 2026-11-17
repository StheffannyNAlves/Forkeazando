package br.uefs.forkeazando.adapter;
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.ResumoSlot;

import java.util.List;
// Para restaurar as partidas
public interface AcessoPartidas {
    boolean existeAutosave();
    List<ResumoSlot> listarSlots();
    Estado carregarSlot(int numeroSlot) throws CarregamentoException, DadosInvalidosException;
    Estado carregarAutosave() throws CarregamentoException, DadosInvalidosException;

}
