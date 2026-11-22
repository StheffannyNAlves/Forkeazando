package br.uefs.forkeazando.view.javafx;

import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** Texto com espaço reservado: revelar a fala não desloca decisões nem a rolagem. */
public final class DialogoFx extends VBox {
    private final Label texto;
    private final Button completar = new Button("MOSTRAR TEXTO  ·  ESPAÇO / ENTER");
    private Timeline digitacao;

    public DialogoFx(String narrativa) {
        super(8);
        getStyleClass().add("corpo-dialogo");
        Label reserva = criarTexto(narrativa);
        reserva.setOpacity(0);
        reserva.setMouseTransparent(true);
        reserva.setAccessibleText("");
        texto = criarTexto(narrativa);
        StackPane linhas = new StackPane(reserva, texto);
        linhas.setAlignment(Pos.TOP_LEFT);
        completar.getStyleClass().add("botao-texto");
        completar.setOnAction(e -> completar());
        getChildren().addAll(linhas, completar);
        setFillWidth(true);
        setMinWidth(0);
    }

    private static Label criarTexto(String valor) {
        Label label = new Label(valor);
        label.getStyleClass().add("texto-narrativa");
        label.setWrapText(true);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMinWidth(0);
        label.setMinHeight(USE_PREF_SIZE);
        return label;
    }

    public void iniciar(int velocidade) {
        digitacao = AnimacoesFx.revelarDialogo(texto, velocidade);
        if (digitacao == null) {
            terminar();
            return;
        }
        var fim = digitacao.getOnFinished();
        digitacao.setOnFinished(e -> {
            if (fim != null) fim.handle(e);
            terminar();
        });
        texto.textProperty().addListener((obs, antes, depois) -> {
            if (digitacao != null && digitacao.getStatus() == javafx.animation.Animation.Status.STOPPED) terminar();
        });
    }

    public boolean completar() {
        boolean estavaDigitando = AnimacoesFx.completarDialogo(texto);
        if (estavaDigitando) terminar();
        return estavaDigitando;
    }

    private void terminar() {
        completar.setText("◆  LEITURA COMPLETA");
        completar.setDisable(true);
    }

    public void encerrar() {
        completar();
        AnimacoesFx.pararArvore(this);
        digitacao = null;
    }
}
