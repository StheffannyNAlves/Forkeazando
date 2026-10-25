package br.uefs.forkeazando.controller;

import java.util.ArrayList;
import java.util.List;

/** Guarda as mensagens que o controller mandaria para a tela. */
class MensagensRegistradas implements MensagensDeTela {

    final List<String> informacoes = new ArrayList<>();
    final List<String> erros = new ArrayList<>();
    final List<String> sucessos = new ArrayList<>();

    @Override
    public void mostrarMensagem(String mensagem) {
        informacoes.add(mensagem);
    }

    @Override
    public void mostrarErro(String mensagem) {
        erros.add(mensagem);
    }

    @Override
    public void mostrarSucesso(String mensagem) {
        sucessos.add(mensagem);
    }
}
