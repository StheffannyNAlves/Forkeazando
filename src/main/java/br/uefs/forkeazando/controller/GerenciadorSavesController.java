
package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.dao.GerenciadorSave;
import br.uefs.forkeazando.dao.GerenciadorSlots;
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.ResumoSlot;
import br.uefs.forkeazando.view.Cores;
import br.uefs.forkeazando.view.MenuPrincipal;
import br.uefs.forkeazando.view.widget.MenuInterativo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class GerenciadorSavesController {

    private static final DateTimeFormatter FORMATO_DATA_SLOT =
            DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

    private final MenuPrincipal view;
    private final Path pastaSaves;

    public GerenciadorSavesController(MenuPrincipal view, Path pastaSaves) {
        this.view = view;
        this.pastaSaves = pastaSaves;
    }

    public record PartidaSelecionada(Estado estado, Integer numeroSlot) {
    }

    public Optional<PartidaSelecionada> carregarPartida() {
        List<ResumoSlot> slots = GerenciadorSlots.listar(pastaSaves);
        List<MenuInterativo.Opcao> opcoes = new ArrayList<>();

        for (ResumoSlot slot : slots) {
            opcoes.add(new MenuInterativo.Opcao(formatarSlot(slot)));
        }

        int indiceAutosave = opcoes.size();
        opcoes.add(new MenuInterativo.Opcao("Carregar autosave"));

        opcoes.add(new MenuInterativo.Opcao("Cancelar"));

        MenuInterativo.Resposta resposta =
                MenuInterativo.abrir(opcoes, "Carregar partida");

        if (resposta.resultado != MenuInterativo.Resultado.SELECIONADO) {
            return Optional.empty();
        }

        int indice = resposta.indiceOpcao;

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
            Estado estado = GerenciadorSlots.carregar(
                    pastaSaves, slot.numero()
            );

            return Optional.of(
                    new PartidaSelecionada(estado, slot.numero())
            );

        } catch (CarregamentoException | DadosInvalidosException e) {
            view.mostrarMensagem(
                    Cores.VERMELHO + "Erro ao carregar: "
                            + e.getMessage() + Cores.RESET
            );
            return Optional.empty();
        }
    }

    private Optional<PartidaSelecionada> carregarAutosave() {
        Path caminho = GerenciadorSlots.caminhoDoAutosave(pastaSaves);

        if (!Files.exists(caminho)) {
            view.mostrarMensagem("Ainda não existe um autosave.");
            return Optional.empty();
        }

        try {
            Estado estado = GerenciadorSave.carregar(caminho);

            if (estado.getProtagonista() == null) {
                view.mostrarMensagem(
                        Cores.VERMELHO
                                + "O autosave não contém uma partida válida."
                                + Cores.RESET
                );
                return Optional.empty();
            }

            return Optional.of(new PartidaSelecionada(estado, null));

        } catch (CarregamentoException | DadosInvalidosException e) {
            view.mostrarMensagem(
                    Cores.VERMELHO + "Erro ao carregar autosave: "
                            + e.getMessage() + Cores.RESET
            );
            return Optional.empty();
        }
    }

    public void gerenciarSaves() {
        List<MenuInterativo.Opcao> opcoes = List.of(
                new MenuInterativo.Opcao("Excluir save"),
                new MenuInterativo.Opcao("Voltar")
        );

        MenuInterativo.Resposta resposta =
                MenuInterativo.abrir(opcoes, "Gerenciar saves");

        if (resposta.resultado == MenuInterativo.Resultado.SELECIONADO
                && resposta.indiceOpcao == 0) {
            excluirSave();
        }
    }

    private void excluirSave() {
        List<ResumoSlot> slots = GerenciadorSlots.listar(pastaSaves);
        List<MenuInterativo.Opcao> opcoes = new ArrayList<>();

        for (ResumoSlot slot : slots) {
            opcoes.add(new MenuInterativo.Opcao(formatarSlot(slot)));
        }

        opcoes.add(new MenuInterativo.Opcao("Cancelar"));

        MenuInterativo.Resposta resposta =
                MenuInterativo.abrir(opcoes, "Excluir save");

        if (resposta.resultado != MenuInterativo.Resultado.SELECIONADO
                || resposta.indiceOpcao < 0
                || resposta.indiceOpcao >= slots.size()) {
            return;
        }

        ResumoSlot slot = slots.get(resposta.indiceOpcao);

        if (slot.status() == ResumoSlot.Status.VAZIO) {
            view.mostrarMensagem("Esse slot já está vazio.");
            return;
        }

        List<MenuInterativo.Opcao> confirmacoes = List.of(
                new MenuInterativo.Opcao("Sim, excluir"),
                new MenuInterativo.Opcao("Não")
        );

        MenuInterativo.Resposta confirmacao =
                MenuInterativo.abrir(
                        confirmacoes,
                        "Excluir permanentemente o Slot "
                                + slot.numero() + "?"
                );

        if (confirmacao.resultado != MenuInterativo.Resultado.SELECIONADO
                || confirmacao.indiceOpcao != 0) {
            return;
        }

        try {
            GerenciadorSlots.excluir(pastaSaves, slot.numero());

            view.mostrarMensagem(
                    Cores.VERDE + "Slot " + slot.numero()
                            + " excluído." + Cores.RESET
            );

        } catch (SaveException e) {
            view.mostrarMensagem(
                    Cores.VERMELHO + "Erro ao excluir save: "
                            + e.getMessage() + Cores.RESET
            );
        }
    }

    public OptionalInt salvarPartida(Estado estado) {
        List<ResumoSlot> slots = GerenciadorSlots.listar(pastaSaves);
        List<MenuInterativo.Opcao> opcoes = new ArrayList<>();

        for (ResumoSlot slot : slots) {
            opcoes.add(new MenuInterativo.Opcao(formatarSlot(slot)));
        }

        opcoes.add(new MenuInterativo.Opcao("Cancelar"));

        MenuInterativo.Resposta resposta =
                MenuInterativo.abrir(
                        opcoes, "Escolha onde salvar a partida"
                );

        if (resposta.resultado != MenuInterativo.Resultado.SELECIONADO
                || resposta.indiceOpcao < 0
                || resposta.indiceOpcao >= slots.size()) {
            return OptionalInt.empty();
        }

        ResumoSlot slot = slots.get(resposta.indiceOpcao);

        if (slot.status() != ResumoSlot.Status.VAZIO
                && !confirmarSobrescrita(slot.numero())) {
            return OptionalInt.empty();
        }

        if (salvarNoSlot(estado, slot.numero())) {
            return OptionalInt.of(slot.numero());
        }

        return OptionalInt.empty();
    }

    public boolean escolherSobrescrita(
            Estado estado,
            boolean permitirProsseguirSemSalvar) {

        List<ResumoSlot> slots = GerenciadorSlots.listar(pastaSaves);
        List<MenuInterativo.Opcao> opcoes = new ArrayList<>();

        for (ResumoSlot slot : slots) {
            opcoes.add(new MenuInterativo.Opcao(formatarSlot(slot)));
        }

        int indiceProsseguir = -1;

        if (permitirProsseguirSemSalvar) {
            indiceProsseguir = opcoes.size();
            opcoes.add(new MenuInterativo.Opcao("Prosseguir sem salvar"));
        }

        int indiceCancelar = opcoes.size();
        opcoes.add(new MenuInterativo.Opcao("Cancelar"));

        MenuInterativo.Resposta resposta =
                MenuInterativo.abrir(
                        opcoes,
                        "Escolha um slot para sobrescrever"
                );

        if (resposta.resultado != MenuInterativo.Resultado.SELECIONADO) {
            return false;
        }

        if (resposta.indiceOpcao == indiceCancelar) {
            return false;
        }

        if (resposta.indiceOpcao == indiceProsseguir) {
            return true;
        }

        if (resposta.indiceOpcao < 0
                || resposta.indiceOpcao >= slots.size()) {
            return false;
        }

        ResumoSlot slot = slots.get(resposta.indiceOpcao);

        if (slot.status() != ResumoSlot.Status.VAZIO
                && !confirmarSobrescrita(slot.numero())) {
            return false;
        }

        return salvarNoSlot(estado, slot.numero());
    }

    private boolean confirmarSobrescrita(int numeroSlot) {
        List<MenuInterativo.Opcao> opcoes = List.of(
                new MenuInterativo.Opcao("Sim, sobrescrever"),
                new MenuInterativo.Opcao("Não")
        );

        MenuInterativo.Resposta resposta =
                MenuInterativo.abrir(
                        opcoes, "Sobrescrever Slot " + numeroSlot + "?"
                );

        return resposta.resultado == MenuInterativo.Resultado.SELECIONADO
                && resposta.indiceOpcao == 0;
    }

    private boolean salvarNoSlot(Estado estado, int numeroSlot) {
        try {
            GerenciadorSlots.salvar(estado, pastaSaves, numeroSlot);

            view.mostrarMensagem(
                    "Partida salva no Slot " + numeroSlot + "!"
            );

            return true;

        } catch (SaveException e) {
            view.mostrarMensagem(
                    Cores.VERMELHO + "Erro ao salvar: "
                            + e.getMessage() + Cores.RESET
            );
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
            view.mostrarMensagem(
                    "Erro ao salvar a conclusão: " + e.getMessage()
            );
        }
    }

    private String formatarSlot(ResumoSlot slot) {
        if (slot.status() == ResumoSlot.Status.VAZIO) {
            return "Slot " + slot.numero() + " - Vazio";
        }

        if (slot.status() == ResumoSlot.Status.CORROMPIDO) {
            return "Slot " + slot.numero() + " - Corrompido";
        }

        return "Slot " + slot.numero() + " - Ocupado"
                + " | " + slot.nome()
                + " | Capítulo " + slot.capitulo()
                + " | " + slot.data().format(FORMATO_DATA_SLOT)
                + "   Score: " + slot.score() + "/100"
                + " | Participação: " + slot.participacao() + "/100"
                + " | Rank: " + slot.rank()
                + " | Energia: " + slot.energia()
                + "/" + slot.energiaMaxima();
    }
}
