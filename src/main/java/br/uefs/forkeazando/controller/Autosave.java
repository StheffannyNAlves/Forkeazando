package br.uefs.forkeazando.controller;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.excecao.*;
@FunctionalInterface
public interface Autosave {
        void salvar (Estado estado) throws SaveException;
}
