package br.uefs.forkeazando.view.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayDeque;
import java.util.Deque;

/** Uma notificação por vez no cabeçalho, sem cobrir a leitura nem capturar cliques. */
public final class AvisosFx extends VBox {
    private record Aviso(String titulo, String mensagem) {}
    private final Deque<Aviso> fila = new ArrayDeque<>();
    private Aviso atual;

    public AvisosFx() {
        setAlignment(Pos.TOP_RIGHT);
        setMouseTransparent(true);
        setMaxWidth(350);
        setMaxHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(this, Pos.TOP_RIGHT);
        StackPane.setMargin(this, new Insets(18, 22, 0, 0));
    }

    public void mostrar(String titulo, String mensagem) {
        if (mensagem == null || mensagem.isBlank()) return;
        if ("AUTOSAVE".equals(titulo) && (atual != null || !fila.isEmpty())) return;
        Aviso novo = new Aviso(titulo, mensagem);
        if ("NOVO CAPÍTULO".equals(titulo)) {
            fila.removeIf(a -> "NOVO CAPÍTULO".equals(a.titulo()));
        }
        if (atual != null && ("AUTOSAVE".equals(atual.titulo())
                || "NOVO CAPÍTULO".equals(titulo) && "NOVO CAPÍTULO".equals(atual.titulo()))) {
            AnimacoesFx.pararArvore(this);
            getChildren().clear();
            atual = null;
        }
        fila.addLast(novo);
        exibirProximo();
    }

    private void exibirProximo() {
        if (atual != null || fila.isEmpty()) return;
        atual = fila.removeFirst();
        VBox aviso = new VBox(4);
        aviso.getStyleClass().add("aviso-discreto");
        Label titulo = new Label(atual.titulo());
        titulo.getStyleClass().add("aviso-discreto-titulo");
        Label texto = new Label(atual.mensagem());
        texto.getStyleClass().add("aviso-discreto-texto");
        texto.setWrapText(true);
        texto.setMaxWidth(318);
        texto.setMaxHeight(50);
        texto.setAccessibleText(atual.mensagem());
        aviso.getChildren().addAll(titulo, texto);
        if ("NOVO CAPÍTULO".equals(atual.titulo()) || "CONQUISTA DESBLOQUEADA".equals(atual.titulo())) {
            aviso.getStyleClass().add("aviso-marco");
        }
        getChildren().setAll(aviso);
        double segundos = Math.min(7, Math.max(3.2, atual.mensagem().length() / 36.0));
        AnimacoesFx.notificar(aviso, Duration.seconds(segundos), () -> {
            getChildren().clear();
            atual = null;
            exibirProximo();
        });
    }
}
