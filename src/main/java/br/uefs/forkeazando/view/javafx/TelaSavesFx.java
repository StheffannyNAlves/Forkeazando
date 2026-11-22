package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.controller.SessaoGrafica;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.ResumoSlot;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static br.uefs.forkeazando.view.javafx.ComponentesFx.botaoAcao;
import static br.uefs.forkeazando.view.javafx.ComponentesFx.novoPainel;
import static br.uefs.forkeazando.view.javafx.ComponentesFx.rotulo;

/**
 * Telas de gerenciamento de saves (salvar, sobrescrever, excluir) e suas confirmações
 * Só apresenta e coleta decisões: toda operação passa pela SessaoGrafica
 */
final class TelaSavesFx {
    private final SessaoGrafica sessao;
    private final Navegacao navegacao;

    TelaSavesFx(SessaoGrafica sessao, Navegacao navegacao) {
        this.sessao = Objects.requireNonNull(sessao);
        this.navegacao = Objects.requireNonNull(navegacao);
    }

    void abrir() {
        VBox painel = novoPainel("GERENCIAR SAVES", "Escolha um slot para salvar ou excluir uma partida.");
        for (ResumoSlot slot : sessao.listarSlots()) {
            VBox bloco = new VBox(8);
            bloco.getChildren().add(rotulo("SLOT " + slot.numero() + " — " + slot.status()));
            if (slot.status() == ResumoSlot.Status.OCUPADO) {
                String data = slot.data() == null ? "Data indisponível"
                        : slot.data().format(DateTimeFormatter.ofPattern("dd/MM/yyyy · HH:mm"));
                bloco.getChildren().add(rotulo(slot.nome() + " | Capítulo " + slot.capitulo() + " | " + data));
            }
            HBox acoes = new HBox(12);
            if (sessao.possuiPartida()) {
                Button salvar = botaoAcao("SALVAR");
                salvar.setOnAction(e -> {
                    if (slot.status() == ResumoSlot.Status.VAZIO) salvarNoSlot(slot.numero());
                    else confirmarSobrescrita(slot.numero());
                });
                acoes.getChildren().add(salvar);
            }
            if (slot.status() != ResumoSlot.Status.VAZIO) {
                Button excluir = botaoAcao("EXCLUIR");
                excluir.setOnAction(e -> confirmarExclusao(slot.numero()));
                acoes.getChildren().add(excluir);
            }
            bloco.getChildren().add(acoes);
            painel.getChildren().add(bloco);
        }
        if (sessao.existeAutosave()) {
            VBox blocoAutosave = new VBox(8);
            blocoAutosave.getChildren().add(rotulo("SALVAMENTO AUTOMÁTICO"));
            Button excluirAuto = botaoAcao("EXCLUIR AUTOSAVE");
            excluirAuto.setOnAction(e -> confirmarExclusaoAutosave());
            blocoAutosave.getChildren().add(excluirAuto);
            painel.getChildren().add(blocoAutosave);
        }
        Button voltar = botaoAcao("VOLTAR AO MENU");
        voltar.setOnAction(e -> navegacao.voltarAoMenu());
        painel.getChildren().add(voltar);
        navegacao.mostrarTela(painel);
    }

    private void salvarNoSlot(int numeroSlot) {
        try {
            sessao.salvarEmSlot(numeroSlot);
            abrir();
            navegacao.mostrarModal("PARTIDA SALVA", "Progresso salvo no slot " + numeroSlot + ".");
        } catch (SaveException ex) {
            navegacao.mostrarModal("ERRO AO SALVAR", ex.getMessage());
        }
    }

    private void confirmarSobrescrita(int numeroSlot) {
        confirmar("SOBRESCREVER PARTIDA?",
                "Os dados anteriores do slot " + numeroSlot + " serão substituídos.",
                "SIM, SOBRESCREVER", () -> salvarNoSlot(numeroSlot));
    }

    private void confirmarExclusao(int numeroSlot) {
        confirmar("EXCLUIR PARTIDA?",
                "O slot " + numeroSlot + " será excluído. Esta ação não pode ser desfeita.",
                "SIM, EXCLUIR", () -> excluirSlot(numeroSlot));
    }

    private void confirmarExclusaoAutosave() {
        confirmar("EXCLUIR AUTOSAVE?",
                "O salvamento automático atual será excluído. Esta ação não pode ser desfeita. "
                        + "Os slots manuais e a partida em memória não serão afetados.",
                "SIM, EXCLUIR", this::excluirAutosave);
    }

    // Painel de confirmação comum: cancelar sempre volta à lista de saves, sem alterar nada
    private void confirmar(String titulo, String mensagem, String textoConfirmar, Runnable aoConfirmar) {
        VBox painel = novoPainel(titulo, mensagem);
        Button confirmar = botaoAcao(textoConfirmar);
        confirmar.setOnAction(e -> aoConfirmar.run());
        Button cancelar = botaoAcao("CANCELAR");
        cancelar.setOnAction(e -> abrir());
        painel.getChildren().addAll(confirmar, cancelar);
        navegacao.mostrarTela(painel);
    }

    private void excluirAutosave() {
        try {
            sessao.excluirAutosave();
            abrir();
            navegacao.mostrarModal("AUTOSAVE EXCLUÍDO", "O salvamento automático foi excluído.");
        } catch (SaveException ex) {
            navegacao.mostrarModal("ERRO AO EXCLUIR", ex.getMessage());
        }
    }

    private void excluirSlot(int numeroSlot) {
        try {
            sessao.excluirSlot(numeroSlot);
            abrir();
            navegacao.mostrarModal("PARTIDA EXCLUÍDA", "O slot " + numeroSlot + " foi excluído.");
        } catch (SaveException ex) {
            navegacao.mostrarModal("ERRO AO EXCLUIR", ex.getMessage());
        }
    }
}
