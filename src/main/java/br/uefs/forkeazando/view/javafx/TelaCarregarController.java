
package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.adapter.AcessoPartidas;
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.ResumoSlot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Consumer;

public class TelaCarregarController {

    @FXML private Label infoSlot1;
    @FXML private Label infoSlot2;
    @FXML private Label infoSlot3;

    @FXML private Button btnCarregarSlot1;
    @FXML private Button btnCarregarSlot2;
    @FXML private Button btnCarregarSlot3;
    @FXML private Button btnCarregarAutosave;
    @FXML private Button btnVoltar;

    private AcessoPartidas acessoPartidas;
    private Consumer<Estado> aoSelecionarPartida;
    private Consumer<String> aoErro;
    private Runnable aoVoltarMenu;

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void configurar(
            AcessoPartidas acessoPartidas,
            Consumer<Estado> aoSelecionarPartida,
            Consumer<String> aoErro,
            Runnable aoVoltarMenu) {

        this.acessoPartidas = Objects.requireNonNull(acessoPartidas);
        this.aoSelecionarPartida = Objects.requireNonNull(aoSelecionarPartida);
        this.aoErro = Objects.requireNonNull(aoErro);
        this.aoVoltarMenu = Objects.requireNonNull(aoVoltarMenu);

        atualizarSlots();
    }

    private void atualizarSlots() {
        for (ResumoSlot slot : acessoPartidas.listarSlots()) {
            mostrarSlot(slot);
        }

        btnCarregarAutosave.setDisable(
                !acessoPartidas.existeAutosave()
        );
    }

    private void mostrarSlot(ResumoSlot slot) {
        Label informacao = switch (slot.numero()) {
            case 1 -> infoSlot1;
            case 2 -> infoSlot2;
            case 3 -> infoSlot3;
            default -> throw new IllegalArgumentException(
                    "Slot desconhecido: " + slot.numero());
        };

        Button botao = switch (slot.numero()) {
            case 1 -> btnCarregarSlot1;
            case 2 -> btnCarregarSlot2;
            case 3 -> btnCarregarSlot3;
            default -> throw new IllegalArgumentException(
                    "Slot desconhecido: " + slot.numero());
        };

        switch (slot.status()) {
            case VAZIO -> {
                informacao.setText("Slot vazio");
                botao.setDisable(true);
            }

            case CORROMPIDO -> {
                informacao.setText("Arquivo corrompido");
                botao.setDisable(true);
            }

            case OCUPADO -> {
                String data = slot.data() == null
                        ? "Data indisponível"
                        : slot.data().format(FORMATO_DATA);

                informacao.setText(
                        slot.nome()
                                + "  |  Capítulo " + slot.capitulo()
                                + "\nScore: " + slot.score()
                                + "  |  " + data
                );

                botao.setDisable(false);
            }
        }
    }

    @FXML
    private void aoCarregarSlot(ActionEvent evento) {
        int numeroSlot;

        if (evento.getSource() == btnCarregarSlot1) {
            numeroSlot = 1;
        } else if (evento.getSource() == btnCarregarSlot2) {
            numeroSlot = 2;
        } else if (evento.getSource() == btnCarregarSlot3) {
            numeroSlot = 3;
        } else {
            return;
        }

        try {
            Estado carregado = acessoPartidas.carregarSlot(numeroSlot);
            aoSelecionarPartida.accept(carregado);

        } catch (CarregamentoException | DadosInvalidosException e) {
            aoErro.accept(e.getMessage());
            atualizarSlots();
        }
    }

    @FXML
    private void aoCarregarAutosave() {
        try {
            Estado carregado = acessoPartidas.carregarAutosave();
            aoSelecionarPartida.accept(carregado);

        } catch (CarregamentoException | DadosInvalidosException e) {
            aoErro.accept(e.getMessage());
            atualizarSlots();
        }
    }

    @FXML
    private void aoVoltar() {
        aoVoltarMenu.run();
    }
}
