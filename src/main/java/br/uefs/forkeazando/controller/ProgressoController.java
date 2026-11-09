package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Progresso;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.view.MenuPrincipal;
import br.uefs.forkeazando.view.ProgressoView;

import java.time.LocalDateTime;

public class ProgressoController {

    private final MenuPrincipal view;
    private final GerenciadorProgresso gerenciadorProgresso;

    public ProgressoController(MenuPrincipal view, GerenciadorProgresso gerenciadorProgresso) {
        this.view = view;
        this.gerenciadorProgresso = gerenciadorProgresso;
    }

    public void visualizar(Progresso progresso) {
        if (progresso == null) {
            view.mostrarErro("Progresso indisponível. Verifique o arquivo saves/progresso.json.");
            return;
        }

        ProgressoView progressoView = new ProgressoView();
        progressoView.mostrarProgresso(progresso);
        progressoView.mostrarConquistas(progresso);
    }

    public void registrar(Progresso progresso, Estado partidaAtual) {
        if (progresso == null || partidaAtual == null) {
            view.mostrarErro("Progresso indisponível. Verifique o arquivo saves/progresso.json.");
            return;
        }

        Protagonista protagonista = partidaAtual.getProtagonista();
        LocalDateTime agora = LocalDateTime.now();

        boolean novoFinal =
                progresso.sincronizarFinais(protagonista, agora);
        boolean novaColecao =
                progresso.sincronizarColecao(protagonista, agora);

        try {
            gerenciadorProgresso.salvar(progresso);

            if (novoFinal) {
                view.mostrarSucesso("Novo final desbloqueado!");
            }

            if (novaColecao) {
                view.mostrarSucesso("Coleção atualizada!");
            }
        } catch (SaveException e) {
            view.mostrarErro("Não foi possível salvar o progresso global: " + e.getMessage());
        }
    }
}