package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Conquista;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.model.Progresso;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class GerenciadorConquistas {
    private final Set<String> desbloqueadas = new HashSet<>();
    private final GerenciadorProgresso gerenciadorProgresso;

    public GerenciadorConquistas(GerenciadorProgresso gerenciadorProgresso) {
        this.gerenciadorProgresso = gerenciadorProgresso;
    }

    public void carregarDesbloqueadas(Progresso progresso) {
        if (progresso == null) {
            throw new IllegalArgumentException("Progresso não pode ser nulo");
        }

        desbloqueadas.clear();

        for (Conquista conquista : progresso.conquistasDesbloqueadas()) {
            desbloqueadas.add(conquista.getCodigo());
        }
    }

    public void verificar(
            Protagonista protagonista,
            Progresso progresso,
            CenaViewNotificador notificador
    ) {
        for (Conquista conquista : Conquista.values()) {
            if (conquista.atingida(protagonista)
                    && desbloqueadas.add(conquista.getCodigo())) {

                progresso.registrarConquista(
                        conquista,
                        LocalDateTime.now()
                );

                try {
                    gerenciadorProgresso.salvar(progresso);
                    notificador.notificar(conquista);
                } catch (SaveException e) {
                    System.err.println("A conquista foi desbloqueada nesta partida, mas não foi possível " + "persisti-la: " + conquista.getTitulo() + " - " + e.getMessage());
                    notificador.notificar(conquista);
                }
            }
        }
    }

    @FunctionalInterface
    public interface CenaViewNotificador {
        void notificar(Conquista conquista);
    }
}