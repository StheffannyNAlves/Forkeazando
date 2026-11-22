package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.audio.GerenciadorAudio;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ForkeazandoApp extends Application {
    private ControllerInterface controller;
    private GerenciadorAudio audio;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menuCompleto.fxml"));
        StackPane raiz = loader.load();
        controller = loader.getController();
        audio = new GerenciadorAudio();
        controller.configurarAudio(audio);
        stage.setTitle("FORKEAZANDO • Universidade Estadual do Fork Perdido");
        StackPane paisagem = (StackPane) loader.getNamespace().get("painelArte");
        VBox menu = (VBox) loader.getNamespace().get("colunaMenu");
        ImageView imagem = (ImageView) loader.getNamespace().get("arte");
        ScrollPane rolagemMenu = (ScrollPane) loader.getNamespace().get("rolagemMenu");
        Scene scene = new Scene(raiz, 1280, 760);

        // Layout em pixels lógicos: textos não são ampliados por uma escala fracionária global.
        paisagem.setMinSize(0, 0);
        paisagem.prefWidthProperty().bind(scene.widthProperty().multiply(0.48));
        HBox.setHgrow(rolagemMenu, Priority.ALWAYS);
        menu.minHeightProperty().bind(Bindings.createDoubleBinding(
                () -> Math.max(0, rolagemMenu.getViewportBounds().getHeight()),
                rolagemMenu.viewportBoundsProperty()));
        Runnable cobrirPaisagem = () -> {
            double proporcao = imagem.getImage().getWidth() / imagem.getImage().getHeight();
            double largura = Math.max(paisagem.getWidth(), paisagem.getHeight() * proporcao);
            // Há margem para o deslocamento ambiental existente, sem esticar a arte.
            imagem.setFitWidth(Math.ceil(largura) + 32);
            imagem.setFitHeight(0);
        };
        paisagem.widthProperty().addListener((obs, antes, depois) -> cobrirPaisagem.run());
        paisagem.heightProperty().addListener((obs, antes, depois) -> cobrirPaisagem.run());
        stage.iconifiedProperty().addListener((obs, antes, depois) -> controller.suspenderAnimacoes(depois));
        stage.setScene(scene);
        stage.setMinWidth(960);
        stage.setMinHeight(620);
        stage.setResizable(true);
        stage.setOnCloseRequest(e -> { e.consume(); controller.solicitarSaida(); });
        stage.show();
        cobrirPaisagem.run();
        audio.iniciar();
    }

    @Override
    public void stop() {
        if (controller != null) controller.pararAnimacoes();
        if (audio != null) audio.fechar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
