package br.uefs.forkeazando.view.javafx;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
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
    private static final Object CHAVE_ESCALA = new Object();

    private AnimacoesFx() {}

    public static void instalarHover(Button botao) {
        botao.hoverProperty().addListener((obs, anterior, hover) -> {
            if (!botao.isPressed()) {
                boolean selecionado = botao.getStyleClass().contains("selecionado");
                animarEscala(botao, hover ? 1.025 : (selecionado ? 1.012 : 1.0));
            }
        });

        botao.pressedProperty().addListener((obs, anterior, pressionado) -> {
            boolean selecionado = botao.getStyleClass().contains("selecionado");
            double alvo = pressionado ? 0.985
                    : botao.isHover() ? 1.025
                    : selecionado ? 1.012 : 1.0;
            animarEscala(botao, alvo);
        });
    }

    public static void destacarSelecao(Button botao, List<Button> opcoes) {
        for (Button opcao : opcoes) {
            if (opcao.isPressed()) continue;
            double alvo = opcao.isHover() ? 1.025
                    : opcao == botao ? 1.012 : 1.0;
            animarEscala(opcao, alvo);
        }
    }

    private static void animarEscala(Node alvo, double destino) {
        Object anterior = alvo.getProperties().get(CHAVE_ESCALA);
        if (anterior instanceof Animation animacao) {
            animacao.stop();
        }

        if (Math.abs(alvo.getScaleX() - destino) < 0.001
                && Math.abs(alvo.getScaleY() - destino) < 0.001) {
            alvo.getProperties().remove(CHAVE_ESCALA);
            return;
        }

        ScaleTransition transicao = new ScaleTransition(Duration.millis(125), alvo);
        transicao.setToX(destino);
        transicao.setToY(destino);
        transicao.setInterpolator(Interpolator.EASE_BOTH);
        alvo.getProperties().put(CHAVE_ESCALA, transicao);
        transicao.setOnFinished(e -> {
            if (alvo.getProperties().get(CHAVE_ESCALA) == transicao) {
                alvo.getProperties().remove(CHAVE_ESCALA);
            }
        });
        transicao.play();
    }

    public static void entradaTela(Node tela) {
        tela.setOpacity(0);
        tela.setTranslateY(12);

        FadeTransition opacidade = new FadeTransition(Duration.millis(230), tela);
        opacidade.setFromValue(0);
        opacidade.setToValue(1);
        opacidade.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition deslizar = new TranslateTransition(Duration.millis(280), tela);
        deslizar.setFromY(12);
        deslizar.setToY(0);
        deslizar.setInterpolator(Interpolator.EASE_OUT);
        new ParallelTransition(opacidade, deslizar).play();
    }

    public static void entradaModal(StackPane modal) {
        modal.setOpacity(0);
        FadeTransition fundo = new FadeTransition(Duration.millis(170), modal);
        fundo.setToValue(1);
        fundo.play();

        if (!modal.getChildren().isEmpty()) {
            Node janela = modal.getChildren().get(0);
            janela.setTranslateY(10);
            janela.setScaleX(0.985);
            janela.setScaleY(0.985);

            TranslateTransition deslocar = new TranslateTransition(Duration.millis(210), janela);
            deslocar.setToY(0);
            deslocar.setInterpolator(Interpolator.EASE_OUT);

            ScaleTransition ampliar = new ScaleTransition(Duration.millis(210), janela);
            ampliar.setToX(1);
            ampliar.setToY(1);
            ampliar.setInterpolator(Interpolator.EASE_OUT);

            new ParallelTransition(deslocar, ampliar).play();
        }
    }

    public static void entradaBotoes(Parent painel) {
        List<Button> botoes = new ArrayList<>();
        coletarBotoes(painel, botoes);

        for (int i = 0; i < botoes.size(); i++) {
            // Listas longas não precisam de uma animação em cada item.
            if (i >= 9) break;

            Button botao = botoes.get(i);
            botao.setOpacity(0);
            botao.setTranslateY(6);

            PauseTransition aguardar = new PauseTransition(
                    Duration.millis(55 + i * 38));
            aguardar.setOnFinished(e -> {
                if (botao.getScene() == null) return;

                FadeTransition opacidade = new FadeTransition(Duration.millis(175), botao);
                opacidade.setToValue(1);

                TranslateTransition deslocar = new TranslateTransition(Duration.millis(190), botao);
                deslocar.setToY(0);
                deslocar.setInterpolator(Interpolator.EASE_OUT);

                new ParallelTransition(opacidade, deslocar).play();
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

    public static Timeline revelarDialogo(Label narrativa) {
        return revelarDialogo(narrativa, 1);
    }

    public static Timeline revelarDialogo(Label narrativa, int velocidade) {
        String texto = narrativa.getText();
        if (texto == null || texto.isEmpty()) return null;

        int total = texto.codePointCount(0, texto.length());
        int quantidadeQuadros = Math.min(total, 130);
        int caracteresPorQuadro = Math.max(1,
                (total + quantidadeQuadros - 1) / quantidadeQuadros);
        int intervaloMs = switch (velocidade) {
            case 0 -> 39;
            case 2 -> 12;
            default -> 22;
        };

        int[] visiveis = {0};
        narrativa.setText("");

        Timeline digitacao = new Timeline(new KeyFrame(
                Duration.millis(intervaloMs), e -> {
            visiveis[0] = Math.min(total, visiveis[0] + caracteresPorQuadro);
            int fim = texto.offsetByCodePoints(0, visiveis[0]);
            narrativa.setText(texto.substring(0, fim));
        }));
        digitacao.setCycleCount((total + caracteresPorQuadro - 1) / caracteresPorQuadro);
        digitacao.setOnFinished(e -> narrativa.setText(texto));
        narrativa.setOnMouseClicked(e -> {
            digitacao.stop();
            narrativa.setText(texto);
        });
        digitacao.play();
        return digitacao;
    }

    public static void entradaRetrato(ImageView retrato) {
        retrato.setOpacity(0);
        retrato.setTranslateX(-9);

        FadeTransition aparecer = new FadeTransition(Duration.millis(215), retrato);
        aparecer.setToValue(1);

        TranslateTransition deslizar = new TranslateTransition(Duration.millis(240), retrato);
        deslizar.setToX(0);
        deslizar.setInterpolator(Interpolator.EASE_OUT);

        new ParallelTransition(aparecer, deslizar).play();
    }

    public static void entradaIlustracao(ImageView ilustracao) {
        ilustracao.setOpacity(0);
        FadeTransition aparecer = new FadeTransition(Duration.millis(300), ilustracao);
        aparecer.setToValue(1);
        aparecer.play();
    }

    public static void pulsarHud(Label hud) {
        if (hud == null || hud.getScene() == null) return;
        ScaleTransition pulso = new ScaleTransition(Duration.millis(170), hud);
        pulso.setFromX(1.012);
        pulso.setFromY(1.012);
        pulso.setToX(1);
        pulso.setToY(1);
        pulso.setInterpolator(Interpolator.EASE_OUT);
        pulso.play();
    }
}
