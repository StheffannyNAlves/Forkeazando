package br.uefs.forkeazando.view.javafx;

import javafx.scene.layout.VBox;

// O que as telas auxiliares precisam da janela principal, sem conhecer o ControllerInterface
interface Navegacao {
    void mostrarTela(VBox painel);
    void voltarAoMenu();
    void mostrarModal(String titulo, String conteudo);
}
