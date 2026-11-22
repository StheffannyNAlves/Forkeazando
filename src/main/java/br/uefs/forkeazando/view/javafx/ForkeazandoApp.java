package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.audio.GerenciadorAudio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ForkeazandoApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menuCompleto.fxml"));
        StackPane raiz = loader.load();
        ControllerInterface controller = loader.getController();
        GerenciadorAudio audio = new GerenciadorAudio();
        controller.configurarAudio(audio);
        stage.setTitle("FORKEAZANDO • Universidade Estadual do Fork Perdido");
        HBox colunas = (HBox) raiz.getChildren().get(0);
        StackPane paisagem = (StackPane) colunas.getChildren().get(0);
        VBox menu = (VBox) colunas.getChildren().get(1);
        ImageView imagem = (ImageView) paisagem.getChildren().get(0);
        Rectangle recorte = (Rectangle) paisagem.getClip();
        final double LARGURA_BASE = 1280;
        final double ALTURA_BASE = 760;
        raiz.setMinSize(LARGURA_BASE, ALTURA_BASE);
        raiz.setPrefSize(LARGURA_BASE, ALTURA_BASE);
        raiz.setMaxSize(LARGURA_BASE, ALTURA_BASE);
        Group jogoEscalavel = new Group(raiz);
        StackPane container = new StackPane(jogoEscalavel);
        container.setStyle("-fx-background-color: #241342;");

        Scene scene = new Scene(container, LARGURA_BASE, ALTURA_BASE);

        Runnable atualizarEscala = () -> {
            double escalaX = scene.getWidth() / 1280.0;
            double escalaY = scene.getHeight() / 760.0;

            // Mantém a proporção dos elementos.
            double escala = Math.min(escalaX, escalaY);

            if (escala <= 0) return;

            // Largura disponível antes de aplicar a escala
            double larguraDisponivel = scene.getWidth() / escala;

            double espacoExtra = larguraDisponivel - 1280.0;

            // Distribui o espaço adicional entre as colunas
            double larguraPaisagem = 625.0 + espacoExtra / 2.0;
            double larguraMenu = 655.0 + espacoExtra / 2.0;

            raiz.setMinWidth(larguraDisponivel);
            raiz.setPrefWidth(larguraDisponivel);
            raiz.setMaxWidth(larguraDisponivel);

            colunas.setMinWidth(larguraDisponivel);
            colunas.setPrefWidth(larguraDisponivel);
            colunas.setMaxWidth(larguraDisponivel);

            paisagem.setMinWidth(larguraPaisagem);
            paisagem.setPrefWidth(larguraPaisagem);
            paisagem.setMaxWidth(larguraPaisagem);
            menu.setMinWidth(larguraMenu);
            menu.setPrefWidth(larguraMenu);
            menu.setMaxWidth(larguraMenu);

            // Atualiza o recorte da paisagem
            recorte.setWidth(larguraPaisagem);

            // Garante que a imagem cubra a coluna
            imagem.setFitWidth(Math.max(805, larguraPaisagem));

            jogoEscalavel.setScaleX(escala);
            jogoEscalavel.setScaleY(escala);
        };

        scene.widthProperty().addListener((obs, antigo, novo) -> atualizarEscala.run());
        scene.heightProperty().addListener((obs, antigo, novo) -> atualizarEscala.run());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setResizable(true);
        stage.setOnCloseRequest(e -> {e.consume();controller.solicitarSaida();});
        stage.show();
        atualizarEscala.run();
        audio.iniciar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}