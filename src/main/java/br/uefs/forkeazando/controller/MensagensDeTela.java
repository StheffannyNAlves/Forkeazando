package br.uefs.forkeazando.controller;

/** Canal pelo qual o controller avisa o jogador (informação, erro, sucesso). */
public interface MensagensDeTela {

    void mostrarMensagem(String mensagem);

    void mostrarErro(String mensagem);

    void mostrarSucesso(String mensagem);
}
