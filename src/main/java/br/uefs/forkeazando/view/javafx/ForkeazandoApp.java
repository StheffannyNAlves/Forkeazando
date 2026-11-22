package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.audio.GerenciadorAudio;

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
        GerenciadorAudio audio = new GerenciadorAudio();
        controller.configurarAudio(audio);
        stage.setTitle("FORKEAZANDO • Universidade Estadual do Fork Perdido");
        stage.setScene(new Scene(raiz, 1280, 760));
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> {controller.pararAnimacoes();audio.fechar();});
        stage.show();
        audio.iniciar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}