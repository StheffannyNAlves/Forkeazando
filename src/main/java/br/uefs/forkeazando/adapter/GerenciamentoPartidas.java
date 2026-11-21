package br.uefs.forkeazando.adapter;

import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
// Só estabelece o contrato, a classe que a implementa que define o que fazer
public interface GerenciamentoPartidas extends AcessoPartidas{
        void salvarSlot(Estado estado, int numeroSlot) throws  SaveException;

        void excluirSlot(int numeroSlot) throws SaveException;
}
