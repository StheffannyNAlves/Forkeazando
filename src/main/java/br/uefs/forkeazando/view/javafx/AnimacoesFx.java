package br.uefs.forkeazando.view.javafx;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public final class AnimacoesFx {
    private AnimacoesFx() {}

    public static void instalarHover(Button botao) {
        botao.hoverProperty().addListener((obs, anterior, hover) -> {
            boolean selecionado = botao.getStyleClass().contains("selecionado");
            animarEscala(botao, hover ? 1.025 : (selecionado ? 1.015 : 1.0));
        });
    }

    public static void destacarSelecao(Button botao, List<Button> opcoes) {
        for (Button opcao : opcoes) {
            if (opcao != botao && !opcao.isHover()) {
                animarEscala(opcao, 1.0);
            }
        }
        animarEscala(botao, 1.015);
    }

    private static void animarEscala(Node alvo, double destino) {
        ScaleTransition escala = new ScaleTransition(Duration.millis(145), alvo);
        escala.setToX(destino);
        escala.setToY(destino);
        escala.play();
    }

    public static void entradaTela(Node tela) {
        tela.setOpacity(0);
        tela.setTranslateY(16);
        FadeTransition aparecer = new FadeTransition(Duration.millis(240), tela);
        aparecer.setFromValue(0);
        aparecer.setToValue(1);
        TranslateTransition deslizar = new TranslateTransition(Duration.millis(280), tela);
        deslizar.setFromY(16);
        deslizar.setToY(0);
        new ParallelTransition(aparecer, deslizar).play();
    }

    public static void entradaModal(StackPane modal) {
        modal.setOpacity(0);
        FadeTransition fundo = new FadeTransition(Duration.millis(180), modal);
        fundo.setFromValue(0);
        fundo.setToValue(1);
        fundo.play();

        if (!modal.getChildren().isEmpty()) {
            Node janela = modal.getChildren().get(0);
            janela.setScaleX(0.965);
            janela.setScaleY(0.965);
            ScaleTransition ampliar = new ScaleTransition(Duration.millis(210), janela);
            ampliar.setToX(1);
            ampliar.setToY(1);
            ampliar.play();
        }
    }

    public static void entradaBotoes(Parent painel) {
        List<Button> botoes = new ArrayList<>();
        coletarBotoes(painel, botoes);

        for (int i = 0; i < botoes.size(); i++) {
            Button botao = botoes.get(i);
            botao.setOpacity(0);
            botao.setTranslateY(8);
            PauseTransition aguardar = new PauseTransition(Duration.millis(65 + Math.min(i, 12) * 45));
            aguardar.setOnFinished(e -> {
                FadeTransition aparecer = new FadeTransition(Duration.millis(195), botao);
                aparecer.setFromValue(0);
                aparecer.setToValue(1);
                TranslateTransition deslizar = new TranslateTransition(Duration.millis(195), botao);
                deslizar.setFromY(8);
                deslizar.setToY(0);
                new ParallelTransition(aparecer, deslizar).play();
            });
            aguardar.play();
        }
    }

    private static void coletarBotoes(Parent raiz, List<Button> encontrados) {
        for (Node filho : raiz.getChildrenUnmodifiable()) {
            if (filho instanceof Button botao) {
                encontrados.add(botao);
            } else if (filho instanceof Parent grupo) {
                coletarBotoes(grupo, encontrados);
            }
        }
    }


    public static void revelarDialogo(Label narrativa) {
        revelarDialogo(narrativa, 1);
    }

    public static void revelarDialogo(Label narrativa, int velocidade) {
        String completo = narrativa.getText();
        if (completo == null || completo.isEmpty()) {
            return;
        }
        int total = completo.codePointCount(0, completo.length());
        int quadros = Math.min(total, 72);
        int passo = Math.max(1, (total + quadros - 1) / quadros);
        int[] exibidos = {0};
        narrativa.setText("");
        int intervaloMs = switch (velocidade) {
            case 0 -> 45;   // Lenta
            case 2 -> 12;   // Rápida
            default -> 24;  // Normal
        };

        Timeline linhaDoTempo = new Timeline(
                new KeyFrame(Duration.millis(intervaloMs), e -> {exibidos[0] = Math.min(total, exibidos[0] + passo);
                            int ate = completo.offsetByCodePoints(0, exibidos[0]);
                            narrativa.setText(completo.substring(0, ate));}));
        linhaDoTempo.setCycleCount((total + passo - 1) / passo);
        linhaDoTempo.setOnFinished(e -> narrativa.setText(completo));
        narrativa.setOnMouseClicked(e -> {linhaDoTempo.stop();narrativa.setText(completo);});
        linhaDoTempo.play();
    }

    public static void entradaRetrato(ImageView retrato) {
        retrato.setOpacity(0);
        retrato.setTranslateX(-12);
        FadeTransition aparecer = new FadeTransition(Duration.millis(260), retrato);
        aparecer.setFromValue(0);
        aparecer.setToValue(1);
        TranslateTransition aproximar = new TranslateTransition(Duration.millis(260), retrato);
        aproximar.setFromX(-12);
        aproximar.setToX(0);
        new ParallelTransition(aparecer, aproximar).play();
    }

    public static void pulsarHud(Label hud) {
        if (hud == null || hud.getScene() == null) return;
        ScaleTransition pulso = new ScaleTransition(Duration.millis(190), hud);
        pulso.setFromX(1.018);
        pulso.setFromY(1.018);
        pulso.setToX(1);
        pulso.setToY(1);
        pulso.play();
    }
}
