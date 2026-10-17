package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Conquista;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.model.Progresso;
//import br.uefs.forkeazando.view.CenaView;
//import br.uefs.forkeazando.view.Cores;
//import br.uefs.forkeazando.view.MenuPrincipal;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

// Responsabilidade: identificar conquistas atigidas, registrar o desbloqueio e notificar a interface
public class GerenciadorConquistas {
    private final Set<String> desbloqueadas = new HashSet<>();
    private final GerenciadorProgresso gerenciadorProgresso;
    private final Consumer<String> mostrarMensagem;
    private boolean salvamentoPendente;


    public GerenciadorConquistas(GerenciadorProgresso gerenciadorProgresso, Consumer<String> mostrarMensagem) {
        this.gerenciadorProgresso = gerenciadorProgresso;
        this.mostrarMensagem = mostrarMensagem;
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

    public void verificar(Protagonista protagonista, Progresso progresso, CenaViewNotificador notificador) {
        Objects.requireNonNull(protagonista, "Protagonista não pode ser nulo");
        Objects.requireNonNull(progresso, "Progresso não pode ser nulo");
        Objects.requireNonNull(notificador, "Notificador não pode ser nulo");


        for (Conquista conquista : Conquista.values()) {
            if (conquista.atingida(protagonista)
                    && desbloqueadas.add(conquista.getCodigo())) {

                progresso.registrarConquista(
                        conquista,
                        LocalDateTime.now()
                );
                notificador.notificar(conquista);
                salvamentoPendente = true;
            }

            if (salvamentoPendente) {
                try {
                    gerenciadorProgresso.salvar(progresso);
                    salvamentoPendente = false;
                } catch (SaveException e) {
                    mostrarMensagem.accept("A conquista foi desbloqueada nesta partida, mas não foi possível " + "persisti-la: " + conquista.getTitulo() + " - " + e.getMessage());
                }
            }
        }
    }

    @FunctionalInterface
    public interface CenaViewNotificador {
        void notificar(Conquista conquista);
    }
}