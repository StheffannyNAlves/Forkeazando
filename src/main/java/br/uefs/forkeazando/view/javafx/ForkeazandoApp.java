package br.uefs.forkeazando.view.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ForkeazandoApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menuCompleto.fxml"));
        StackPane raiz = loader.load();
        ControllerInterface controller = loader.getController();

        stage.setTitle("FORKEAZANDO • Universidade Estadual do Fork Perdido");
        stage.setScene(new Scene(raiz, 1280, 760));
        stage.setResizable(false); // Layout pixel art com proporções fixas nesta etapa.
        stage.setOnCloseRequest(e -> controller.pararAnimacoes());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
