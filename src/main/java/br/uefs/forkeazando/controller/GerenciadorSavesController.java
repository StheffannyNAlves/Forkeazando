
package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.dao.GerenciadorSave;
import br.uefs.forkeazando.dao.GerenciadorSlots;
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.ResumoSlot;
import br.uefs.forkeazando.view.widget.MenuInterativo;
import br.uefs.forkeazando.view.ResumoSlotView;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class GerenciadorSavesController {

    private final MensagensDeTela view;
    private final Path pastaSaves;
    private final SeletorDeOpcoes seletor;

    public GerenciadorSavesController(MensagensDeTela view, Path pastaSaves) {
        this(view, pastaSaves, SeletorDeOpcoes.terminal());
    }

    public GerenciadorSavesController(MensagensDeTela view, Path pastaSaves, SeletorDeOpcoes seletor) {
        this.view = view;
        this.pastaSaves = pastaSaves;
        this.seletor = seletor;
    }

    public record PartidaSelecionada(Estado estado, Integer numeroSlot) {
    }

    public Optional<PartidaSelecionada> carregarPartida() {
        List<ResumoSlot> slots = GerenciadorSlots.listar(pastaSaves);

        MenuInterativo.Resposta resposta = abrirMenuDeSlots(
                "Carregar partida", slots, "Carregar autosave", "Cancelar");

        if (resposta.resultado != MenuInterativo.Resultado.SELECIONADO) {
            return Optional.empty();
        }

        int indice = resposta.indiceOpcao;
        int indiceAutosave = slots.size();

        if (indice == indiceAutosave) {
            return carregarAutosave();
        }

        if (indice < 0 || indice >= slots.size()) {
            return Optional.empty();
        }

        ResumoSlot slot = slots.get(indice);

        if (slot.status() == ResumoSlot.Status.VAZIO) {
            view.mostrarMensagem("Esse slot está vazio.");
            return Optional.empty();
        }

        if (slot.status() == ResumoSlot.Status.CORROMPIDO) {
            view.mostrarMensagem("Esse save está corrompido.");
            return Optional.empty();
        }

        try {
            Estado estado = GerenciadorSlots.carregar(pastaSaves, slot.numero());
            return Optional.of(new PartidaSelecionada(estado, slot.numero()));

        } catch (CarregamentoException | DadosInvalidosException e) {
            view.mostrarErro("Erro ao carregar: " + e.getMessage());
            return Optional.empty();
        }
    }


    private Optional<PartidaSelecionada> carregarAutosave() {
        Path caminho = GerenciadorSlots.caminhoDoAutosave(pastaSaves);

        if (!GerenciadorSlots.existeAutosave(pastaSaves)) {
            view.mostrarMensagem("Ainda não existe um autosave.");
            return Optional.empty();
        }

        try {
            Estado estado = GerenciadorSave.carregar(caminho);

            if (estado.getProtagonista() == null) {
                view.mostrarErro("O autosave não contém uma partida válida.");
                return Optional.empty();
            }

            return Optional.of(new PartidaSelecionada(estado, null));

        } catch (CarregamentoException | DadosInvalidosException e) {
            view.mostrarErro("Erro ao carregar autosave: " + e.getMessage());

            return Optional.empty();
        }
    }

    public void gerenciarSaves() {
        List<MenuInterativo.Opcao> opcoes = List.of(
                new MenuInterativo.Opcao("Excluir save"),
                new MenuInterativo.Opcao("Excluir autosave"),
                new MenuInterativo.Opcao("Voltar")
        );

        MenuInterativo.Resposta resposta =
                seletor.escolher(opcoes, "Gerenciar saves");

        if (resposta.resultado != MenuInterativo.Resultado.SELECIONADO) {
            return;
        }

        switch (resposta.indiceOpcao) {
            case 0 -> excluirSave();
            case 1 -> excluirAutosave();
            default -> { }
        }
    }

    private boolean confirmarExclusao(String descricao) {
        List<MenuInterativo.Opcao> confirmacoes = List.of(
                new MenuInterativo.Opcao("Sim, excluir"),
                new MenuInterativo.Opcao("Não")
        );

        MenuInterativo.Resposta confirmacao = seletor.escolher(confirmacoes, "Excluir permanentemente " + descricao + "?");

        return confirmacao.resultado == MenuInterativo.Resultado.SELECIONADO && confirmacao.indiceOpcao == 0;
    }

    private void excluirAutosave() {
        if (!GerenciadorSlots.existeAutosave(pastaSaves)) {
            view.mostrarMensagem("Ainda não existe um autosave.");
            return;
        }

        if (!confirmarExclusao("o autosave")) {
            return;
        }

        try {
            GerenciadorSlots.excluirAutosave(pastaSaves);
            view.mostrarSucesso("Autosave excluído.");
        } catch (SaveException e) {
            view.mostrarErro("Erro ao excluir autosave: " + e.getMessage());
        }
    }

    private void excluirSave() {
        List<ResumoSlot> slots = GerenciadorSlots.listar(pastaSaves);

        MenuInterativo.Resposta resposta = abrirMenuDeSlots("Excluir save", slots, "Cancelar");

        if (resposta.resultado != MenuInterativo.Resultado.SELECIONADO || resposta.indiceOpcao < 0 || resposta.indiceOpcao >= slots.size()) {
            return;
        }

        ResumoSlot slot = slots.get(resposta.indiceOpcao);

        if (slot.status() == ResumoSlot.Status.VAZIO) {
            view.mostrarMensagem("Esse slot já está vazio.");
            return;
        }

        if (!confirmarExclusao("o Slot " + slot.numero())) {
            return;
        }

        try {
            GerenciadorSlots.excluir(pastaSaves, slot.numero());
            view.mostrarSucesso("Slot " + slot.numero() + " excluído.");

        } catch (SaveException e) {
            view.mostrarErro("Erro ao excluir: " + e.getMessage());
        }
    }
    public OptionalInt salvarPartida(Estado estado) {
        List<ResumoSlot> slots = GerenciadorSlots.listar(pastaSaves);

        MenuInterativo.Resposta resposta = abrirMenuDeSlots(
                "Escolha onde salvar a partida", slots, "Cancelar");

        if (resposta.resultado != MenuInterativo.Resultado.SELECIONADO
                || resposta.indiceOpcao < 0
                || resposta.indiceOpcao >= slots.size()) {
            return OptionalInt.empty();
        }

        ResumoSlot slot = slots.get(resposta.indiceOpcao);

        if (slot.status() != ResumoSlot.Status.VAZIO
                && sobrescritaRecusada(slot.numero())) {
            return OptionalInt.empty();
        }

        if (salvarNoSlot(estado, slot.numero())) {
            return OptionalInt.of(slot.numero());
        }

        return OptionalInt.empty();
    }

    public boolean escolherSobrescrita(
            Estado estado, boolean permitirProsseguirSemSalvar) {

        List<ResumoSlot> slots = GerenciadorSlots.listar(pastaSaves);

        int indiceProsseguir = permitirProsseguirSemSalvar ? slots.size() : -1;
        int indiceCancelar = slots.size() + (permitirProsseguirSemSalvar ? 1 : 0);

        MenuInterativo.Resposta resposta = permitirProsseguirSemSalvar
                ? abrirMenuDeSlots("Escolha um slot para sobrescrever", slots,
                "Prosseguir sem salvar", "Cancelar")
                : abrirMenuDeSlots("Escolha um slot para sobrescrever", slots,
                "Cancelar");

        if (resposta.resultado != MenuInterativo.Resultado.SELECIONADO) {
            return false;
        }

        if (resposta.indiceOpcao == indiceCancelar) {
            return false;
        }

        if (resposta.indiceOpcao == indiceProsseguir) {
            return true;
        }

        if (resposta.indiceOpcao < 0 || resposta.indiceOpcao >= slots.size()) {
            return false;
        }

        ResumoSlot slot = slots.get(resposta.indiceOpcao);

        if (slot.status() != ResumoSlot.Status.VAZIO
                && sobrescritaRecusada(slot.numero())) {
            return false;
        }

        return salvarNoSlot(estado, slot.numero());
    }

    private boolean sobrescritaRecusada(int numeroSlot) {
        List<MenuInterativo.Opcao> opcoes = List.of(
                new MenuInterativo.Opcao("Sim, sobrescrever"),
                new MenuInterativo.Opcao("Não")
        );

        MenuInterativo.Resposta resposta = seletor.escolher(opcoes, "Sobrescrever Slot " + numeroSlot + "?");

        return resposta.resultado != MenuInterativo.Resultado.SELECIONADO
                || resposta.indiceOpcao != 0;
    }

    private boolean salvarNoSlot(Estado estado, int numeroSlot) {
        try {
            GerenciadorSlots.salvar(estado, pastaSaves, numeroSlot);

            view.mostrarMensagem("Partida salva no Slot " + numeroSlot + "!");

            return true;
        } catch (SaveException e) {
            view.mostrarErro("Erro ao salvar: " + e.getMessage());
            return false;
        }
    }

    public void salvarAutosave(Estado estado) throws SaveException {
        GerenciadorSlots.salvarAutosave(estado, pastaSaves);
    }

    public void salvarConclusao(Estado estado, Integer numeroSlot) {
        try {
            GerenciadorSlots.salvarAutosave(estado, pastaSaves);

            if (numeroSlot != null) {
                GerenciadorSlots.salvar(estado, pastaSaves, numeroSlot);
            }

        } catch (SaveException e) {
            view.mostrarErro("Erro ao salvar a conclusão: " + e.getMessage());
        }
    }

    private MenuInterativo.Resposta abrirMenuDeSlots(
            String titulo, List<ResumoSlot> slots, String... extras) {
        List<MenuInterativo.Opcao> opcoes = new ArrayList<>();
        for (ResumoSlot slot : slots) {
            opcoes.add(new MenuInterativo.Opcao(ResumoSlotView.formatar(slot)));
        }
        for (String extra : extras) {
            opcoes.add(new MenuInterativo.Opcao(extra));
        }
        return seletor.escolher(opcoes, titulo);
    }


}
