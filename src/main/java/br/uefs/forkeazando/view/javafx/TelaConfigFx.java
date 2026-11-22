package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.model.Configuracoes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Responsável pela apresentação das configurações no JavaFX,
 * Não conhece a persistência nem gerencia a navegação do jogo
 */
public final class TelaConfigFx {

    public VBox criarPainel(
            Configuracoes atuais,
            Consumer<Configuracoes> aoSalvar,
            Runnable aoCancelar) {

        Objects.requireNonNull(atuais);
        Objects.requireNonNull(aoSalvar);
        Objects.requireNonNull(aoCancelar);

        VBox painel = new VBox(12);
        painel.setAlignment(Pos.CENTER_LEFT);
        painel.setPadding(new Insets(26));
        painel.setMaxWidth(760);
        painel.getStyleClass().add("painel-interno");

        Label titulo = new Label("OPÇÕES DE ESTILO");
        titulo.getStyleClass().add("titulo-modal");

        Label descricao = new Label(
                "Personalize sua experiência no Forkeazando."
        );
        descricao.getStyleClass().add("texto-ajuda");

        // Velocidade do texto
        ComboBox<String> velocidade = new ComboBox<>();

        velocidade.getItems().addAll(
                "Lenta",
                "Normal",
                "Rápida"
        );

        velocidade.getSelectionModel().select(
                atuais.getVelocidadeTexto()
        );

        velocidade.setMaxWidth(450);
        velocidade.getStyleClass().add("combo-pixel");

        // Volume da música
        Slider volume = new Slider(
                0,
                100,
                atuais.getVolume()
        );

        volume.setMaxWidth(450);
        volume.setShowTickMarks(true);
        volume.setShowTickLabels(true);
        volume.setMajorTickUnit(25);
        volume.setBlockIncrement(10);

        // Preferências booleanas
        CheckBox confirmarSaida = check(
                "Pedir confirmação antes de sair"
        );

        confirmarSaida.setSelected(
                atuais.isConfirmarSaida()
        );

        CheckBox dicas = check(
                "Mostrar dicas de teclado"
        );

        dicas.setSelected(
                atuais.isDicasAtalho()
        );

        Button salvar = botao("SALVAR PREFERÊNCIAS");

        salvar.setOnAction(e -> {
            // Cria uma cópia, sem alterar as configurações atuais.
            Configuracoes novas = new Configuracoes(atuais);

            novas.setVelocidadeTexto(
                    velocidade.getSelectionModel().getSelectedIndex()
            );

            novas.setVolume(
                    (int) Math.round(volume.getValue())
            );

            novas.setConfirmarSaida(
                    confirmarSaida.isSelected()
            );

            novas.setDicasAtalho(
                    dicas.isSelected()
            );

            // Devolve o resultado ao controlador.
            aoSalvar.accept(novas);
        });

        Button cancelar = botao("CANCELAR");
        cancelar.setOnAction(e -> aoCancelar.run());

        painel.getChildren().addAll(
                titulo,
                descricao,
                rotulo("VELOCIDADE DO TEXTO"),
                velocidade,
                rotulo("VOLUME DA MÚSICA"),
                volume,
                confirmarSaida,
                dicas,
                salvar,
                cancelar
        );

        return painel;
    }

    private static Label rotulo(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("rotulo-pixel");
        return label;
    }

    private static CheckBox check(String texto) {
        CheckBox check = new CheckBox(texto);
        check.getStyleClass().add("check-pixel");
        return check;
    }

    private static Button botao(String texto) {
        Button button = new Button(texto);
        button.getStyleClass().add("botao-voltar");
        AnimacoesFx.instalarHover(button);
        return button;
    }
}