package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Conquista;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.model.Progresso;


import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

/** Identifica conquistas atingidas, registra o desbloqueio e notifica a interface */
public class GerenciadorConquistas {
    private final Set<String> desbloqueadas = new HashSet<>();
    private final Map<Conquista, LocalDateTime> pendentes = new LinkedHashMap<>();
    private final GerenciadorProgresso gerenciadorProgresso;
    private final Consumer<String> mostrarMensagem;


    public GerenciadorConquistas(GerenciadorProgresso gerenciadorProgresso, Consumer<String> mostrarMensagem) {
        this.gerenciadorProgresso = gerenciadorProgresso;
        this.mostrarMensagem = mostrarMensagem;
    }

    public void carregarDesbloqueadas(Progresso progresso) {
        if (progresso == null) {
            throw new IllegalArgumentException("Progresso não pode ser nulo");
        }

        desbloqueadas.clear();
        pendentes.clear();

        for (Conquista conquista : progresso.conquistasDesbloqueadas()) {
            desbloqueadas.add(conquista.getCodigo());
        }
    }

    void marcarPersistidas(Progresso progresso) {
        if (progresso == null) {
            return;
        }
        pendentes.keySet().removeIf(progresso::possui);
        for (Conquista conquista : progresso.conquistasDesbloqueadas()) {
            desbloqueadas.add(conquista.getCodigo());
        }
    }

    public void verificar(Protagonista protagonista, Progresso progresso, CenaViewNotificador notificador) {
        Objects.requireNonNull(protagonista, "Protagonista não pode ser nulo");
        Objects.requireNonNull(progresso, "Progresso não pode ser nulo");
        Objects.requireNonNull(notificador, "Notificador não pode ser nulo");

        List<Conquista> novas = new ArrayList<>();

        for (Conquista conquista : Conquista.values()) {
            if (conquista.atingida(protagonista)
                    && desbloqueadas.add(conquista.getCodigo())) {

                pendentes.putIfAbsent(conquista, LocalDateTime.now());
                notificador.notificar(conquista);
                novas.add(conquista);
            }
        }

        if (pendentes.isEmpty()) {
            return;
        }

        Progresso candidato = new Progresso(progresso);
        pendentes.forEach(candidato::registrarConquista);

        try {
            gerenciadorProgresso.salvar(candidato);
            progresso.copiarDe(candidato);
            pendentes.clear();
        } catch (SaveException e) {
            // Progresso em memória permanece fiel ao disco; pendentes serão tentadas novamente.
            mostrarMensagem.accept("A conquista foi desbloqueada nesta partida, mas não foi possível persisti-la"
                    + (novas.isEmpty() ? "" : ": " + titulos(novas)) + " - " + e.getMessage());
        }
    }

    private static String titulos(List<Conquista> conquistas) {
        return String.join(", ", conquistas.stream().map(Conquista::getTitulo).toList());
    }

    @FunctionalInterface
    public interface CenaViewNotificador {
        void notificar(Conquista conquista);
    }
}