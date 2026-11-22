package br.uefs.forkeazando.view.javafx;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.text.BreakIterator;
import java.util.*;

/** Efeitos da view. Cada nó possui seus efeitos e pode encerrá-los ao sair de tela. */
public final class AnimacoesFx {
    private static final Object EFEITOS = new Object();
    private static final Object INSTALADO = new Object();
    private static final Object HOVER = new Object();
    private static final Object DIGITACAO = new Object();
    private static final Object PAUSADOS = new Object();
    private static final Object SUSPENSO = new Object();

    private AnimacoesFx() {}

    @SuppressWarnings("unchecked")
    private static Map<String, Animation> efeitos(Node no) {
        if (no.getProperties().putIfAbsent(INSTALADO, true) == null) {
            no.sceneProperty().addListener((obs, antes, depois) -> {
                if (antes != null && depois == null) parar(no);
            });
        }
        return (Map<String, Animation>) no.getProperties()
                .computeIfAbsent(EFEITOS, chave -> new HashMap<String, Animation>());
    }

    /** Substitui somente o efeito que disputa as mesmas propriedades do nó. */
    public static void executar(Node no, String canal, Animation animacao) {
        Map<String, Animation> ativos = efeitos(no);
        Animation anterior = ativos.put(canal, animacao);
        if (anterior != null) anterior.stop();
        EventHandler<javafx.event.ActionEvent> aoTerminar = animacao.getOnFinished();
        animacao.setOnFinished(e -> {
            ativos.remove(canal, animacao);
            if (aoTerminar != null) aoTerminar.handle(e);
        });
        animacao.play();
        for (Node atual = no; atual != null; atual = atual.getParent()) {
            if (Boolean.TRUE.equals(atual.getProperties().get(SUSPENSO))) {
                animacao.pause();
                no.getProperties().put(PAUSADOS, new ArrayList<>(
                        ativos.values().stream().filter(a -> a.getStatus() == Animation.Status.PAUSED).toList()));
                break;
            }
        }
    }

    public static void parar(Node no) {
        if (no instanceof Label texto) completarDialogo(texto);
        Object registro = no.getProperties().remove(EFEITOS);
        if (registro instanceof Map<?, ?> ativos) {
            ativos.values().forEach(a -> ((Animation) a).stop());
            // Uma entrada cancelada não deixa um nó reutilizado invisível ou deslocado.
            if (!ativos.isEmpty()) {
                no.setOpacity(1);
                no.setTranslateX(0);
                no.setTranslateY(0);
                no.setScaleX(1);
                no.setScaleY(1);
            }
        }
        no.getProperties().remove(PAUSADOS);
        no.getProperties().remove(SUSPENSO);
    }

    public static void pararArvore(Node no) {
        parar(no);
        filhos(no).forEach(AnimacoesFx::pararArvore);
    }

    private static List<Node> filhos(Node no) {
        // O conteúdo existe antes da skin/viewport do ScrollPane ser anexada à cena.
        if (no instanceof ScrollPane rolagem) {
            return rolagem.getContent() == null ? List.of() : List.of(rolagem.getContent());
        }
        return no instanceof Parent grupo ? List.copyOf(grupo.getChildrenUnmodifiable()) : List.of();
    }

    /** Pausa também digitação e esperas; fechar um modal retoma o mesmo ponto. */
    public static void pausarArvore(Node no, boolean pausar) {
        if (pausar) no.getProperties().put(SUSPENSO, true);
        else no.getProperties().remove(SUSPENSO);
        Object registro = no.getProperties().get(EFEITOS);
        if (registro instanceof Map<?, ?> ativos) {
            if (pausar) {
                List<Animation> pausados = new ArrayList<>();
                for (Object valor : ativos.values()) {
                    Animation a = (Animation) valor;
                    if (a.getStatus() == Animation.Status.RUNNING) {
                        a.pause();
                        pausados.add(a);
                    }
                }
                if (!pausados.isEmpty()) no.getProperties().put(PAUSADOS, pausados);
            } else {
                Object pausados = no.getProperties().remove(PAUSADOS);
                if (pausados instanceof List<?> lista) {
                    for (Object valor : lista) {
                        Animation a = (Animation) valor;
                        if (ativos.containsValue(a) && a.getStatus() == Animation.Status.PAUSED) a.play();
                    }
                }
            }
        }
        filhos(no).forEach(filho -> pausarArvore(filho, pausar));
    }

    public static void instalarHover(Button botao) {
        if (botao.getProperties().putIfAbsent(HOVER, true) != null) return;
        botao.hoverProperty().addListener((obs, antes, depois) -> realcar(botao));
        botao.pressedProperty().addListener((obs, antes, depois) -> realcar(botao));
        botao.focusedProperty().addListener((obs, antes, depois) -> realcar(botao));
    }

    private static void realcar(Button botao) {
        // Sem escalar fontes e sprites: a seleção termina em coordenadas inteiras.
        boolean destaque = botao.isHover() || botao.isFocused()
                || botao.getStyleClass().contains("selecionado");
        TranslateTransition mover = new TranslateTransition(Duration.millis(110), botao);
        mover.setToX(botao.isPressed() ? 1 : destaque ? 3 : 0);
        mover.setInterpolator(Interpolator.EASE_OUT);
        executar(botao, "hover", mover);
    }

    public static void destacarSelecao(Button botao, List<Button> opcoes) {
        opcoes.forEach(AnimacoesFx::realcar);
    }

    private static ParallelTransition entrada(Node no, int ms, double y) {
        no.setOpacity(0);
        no.setTranslateY(y);
        FadeTransition fade = new FadeTransition(Duration.millis(ms), no);
        fade.setToValue(1);
        TranslateTransition mover = new TranslateTransition(Duration.millis(ms), no);
        mover.setToY(0);
        mover.setInterpolator(Interpolator.EASE_OUT);
        return new ParallelTransition(fade, mover);
    }

    public static void entradaTela(Node tela) {
        executar(tela, "entrada", entrada(tela, 210, 6));
    }

    public static void entradaModal(StackPane modal) {
        executar(modal, "entrada", entrada(modal, 160, 0));
        if (!modal.getChildren().isEmpty()) {
            Node janela = modal.getChildren().get(0);
            executar(janela, "entrada", entrada(janela, 200, 8));
        }
    }

    public static void saidaModal(StackPane modal, Runnable aoTerminar) {
        FadeTransition saida = new FadeTransition(Duration.millis(130), modal);
        saida.setToValue(0);
        saida.setOnFinished(e -> aoTerminar.run());
        executar(modal, "entrada", saida);
    }

    public static void entradaBotoes(Parent painel) {
        List<Button> botoes = new ArrayList<>();
        coletarBotoes(painel, botoes);
        for (int i = 0; i < Math.min(8, botoes.size()); i++) {
            Button botao = botoes.get(i);
            ParallelTransition aparecer = entrada(botao, 145, 4);
            aparecer.setDelay(Duration.millis(i * 24L));
            executar(botao, "entrada", aparecer);
        }
    }

    private static void coletarBotoes(Parent raiz, List<Button> encontrados) {
        for (Node filho : raiz.getChildrenUnmodifiable()) {
            if (filho instanceof Button b) encontrados.add(b);
            else if (filho instanceof Parent p) coletarBotoes(p, encontrados);
        }
    }

    public static Timeline revelarDialogo(Label narrativa) {
        return revelarDialogo(narrativa, 1);
    }

    public static Timeline revelarDialogo(Label narrativa, int velocidade) {
        completarDialogo(narrativa);
        String texto = narrativa.getText();
        if (texto == null || texto.isEmpty()) return null;
        int intervalo = switch (velocidade) { case 0 -> 36; case 2 -> 11; default -> 21; };
        Timeline timeline = new Timeline();
        BreakIterator caracteres = BreakIterator.getCharacterInstance(Locale.forLanguageTag("pt-BR"));
        caracteres.setText(texto);
        double tempo = 0;
        for (int fim = caracteres.first(); (fim = caracteres.next()) != BreakIterator.DONE;) {
            int indice = fim;
            tempo += intervalo;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(tempo),
                    e -> narrativa.setText(texto.substring(0, indice))));
            int anterior = texto.codePointBefore(fim);
            if (".!?…".indexOf(anterior) >= 0) tempo += intervalo * 5;
            else if (",;:\n".indexOf(anterior) >= 0) tempo += intervalo * 2;
        }
        EventHandler<MouseEvent> clique = e -> { completarDialogo(narrativa); e.consume(); };
        Runnable concluir = () -> {
            timeline.stop();
            narrativa.setText(texto);
            narrativa.removeEventHandler(MouseEvent.MOUSE_CLICKED, clique);
            narrativa.getProperties().remove(DIGITACAO);
        };
        narrativa.getProperties().put(DIGITACAO, concluir);
        narrativa.addEventHandler(MouseEvent.MOUSE_CLICKED, clique);
        narrativa.setText("");
        timeline.setOnFinished(e -> concluir.run());
        executar(narrativa, "digitacao", timeline);
        return timeline;
    }

    public static boolean completarDialogo(Label narrativa) {
        Object concluir = narrativa.getProperties().get(DIGITACAO);
        if (concluir instanceof Runnable acao) {
            acao.run();
            Object registro = narrativa.getProperties().get(EFEITOS);
            if (registro instanceof Map<?, ?> ativos) ativos.remove("digitacao");
            return true;
        }
        return false;
    }

    public static void entradaRetrato(ImageView retrato) {
        FadeTransition aparecer = new FadeTransition(Duration.millis(180), retrato);
        retrato.setOpacity(0);
        aparecer.setToValue(1);
        executar(retrato, "entrada", aparecer);
    }

    public static void entradaIlustracao(ImageView ilustracao) {
        FadeTransition aparecer = new FadeTransition(Duration.millis(280), ilustracao);
        ilustracao.setOpacity(0);
        aparecer.setToValue(1);
        executar(ilustracao, "entrada", aparecer);
    }

    public static void pulsarHud(Label hud) {
        if (hud == null || hud.getScene() == null) return;
        FadeTransition pulso = new FadeTransition(Duration.millis(210), hud);
        pulso.setFromValue(0.7);
        pulso.setToValue(1);
        executar(hud, "feedback", pulso);
    }

    public static void notificar(Node aviso, Duration duracao, Runnable aoTerminar) {
        ParallelTransition aparecer = entrada(aviso, 180, -6);
        FadeTransition desaparecer = new FadeTransition(Duration.millis(230), aviso);
        desaparecer.setToValue(0);
        SequentialTransition sequencia = new SequentialTransition(aparecer,
                new PauseTransition(duracao), desaparecer);
        sequencia.setOnFinished(e -> aoTerminar.run());
        executar(aviso, "notificacao", sequencia);
    }
}
