package br.uefs.forkeazando.view.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** Fábrica de componentes com o estilo pixel do jogo; evita repetir a montagem em cada tela */
final class ComponentesFx {
    private ComponentesFx() { }

    static <E extends Enum<E>> ComboBox<E> novoCombo(E[] valores) {
        ComboBox<E> combo = new ComboBox<>();
        combo.getItems().addAll(valores);
        combo.setMaxWidth(450);
        combo.getStyleClass().add("combo-pixel");
        return combo;
    }

    static CheckBox novoCheck(String texto) {
        CheckBox check = new CheckBox(texto);
        check.getStyleClass().add("check-pixel");
        return check;
    }

    static Label rotulo(String texto) {
        Label l = new Label(texto);
        l.getStyleClass().add("rotulo-pixel");
        l.setWrapText(true);
        return l;
    }

    static Button botaoAcao(String texto) {
        Button b = new Button(texto);
        b.getStyleClass().add("botao-voltar");
        b.setWrapText(true);
        b.setMinHeight(Region.USE_PREF_SIZE);
        AnimacoesFx.instalarHover(b);
        return b;
    }

    static VBox novoPainel(String titulo, String subtitulo) {
        VBox painel = new VBox(12);
        painel.setAlignment(Pos.CENTER_LEFT);
        painel.setPadding(new Insets(26));
        painel.setMaxWidth(760);
        painel.getStyleClass().add("painel-interno");
        Label cabecalho = new Label(titulo);
        cabecalho.getStyleClass().add("titulo-modal");
        cabecalho.setWrapText(true);
        cabecalho.setMaxWidth(450);
        Label descricao = new Label(subtitulo);
        descricao.getStyleClass().add("texto-ajuda");
        descricao.setWrapText(true);
        descricao.setMaxWidth(450);
        painel.getChildren().addAll(cabecalho, descricao);
        return painel;
    }
}
