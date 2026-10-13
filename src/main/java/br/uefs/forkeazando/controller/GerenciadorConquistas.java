
package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.model.Conquista;
import br.uefs.forkeazando.model.Protagonista;

import java.util.HashSet;
import java.util.Set;

public class GerenciadorConquistas {
    private final Set<String> desbloqueadas = new HashSet<>();

    public void verificar(Protagonista protagonista, CenaViewNotificador notificador) {
        for (Conquista conquista : Conquista.values()) {
            if (conquista.atingida(protagonista)
                    && desbloqueadas.add(conquista.getCodigo())) {
                notificador.notificar(conquista);
            }
        }
    }

    @FunctionalInterface
    public interface CenaViewNotificador {
        void notificar(Conquista conquista);
    }
}
