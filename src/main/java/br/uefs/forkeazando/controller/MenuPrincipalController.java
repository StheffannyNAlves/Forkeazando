package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.persistencia.ConfiguracoesAdmin;
import br.uefs.forkeazando.persistencia.GerenciadorSlots;
import br.uefs.forkeazando.roteiro.*;
import br.uefs.forkeazando.view.CenaView;
import br.uefs.forkeazando.view.Cores;
import br.uefs.forkeazando.view.Entrada;
import br.uefs.forkeazando.view.MenuPrincipal;
import br.uefs.forkeazando.view.TelaCaracteristicas;
import br.uefs.forkeazando.view.widget.MenuInterativo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class MenuPrincipalController {

    private final MenuPrincipal view;
    private boolean rodando = true;
    private static final Path PASTA_SAVES = Path.of("saves");
    private Estado partidaAtual;

    public MenuPrincipalController(MenuPrincipal view) {
        this.view = view;
    }

    public void iniciar() {
        while (rodando) {
            view.mostrarMenu();
            String opcao = view.lerOpcoes();
            processarOpcao(opcao);
        }
    }

    private void carregarPartida() {
        List<String> slots = GerenciadorSlots.listar(PASTA_SAVES);
        List<MenuInterativo.Opcao> opcoes = new ArrayList<>();

        for (String slot : slots) {
            opcoes.add(new MenuInterativo.Opcao(slot));
        }

        opcoes.add(new MenuInterativo.Opcao("Cancelar"));

        MenuInterativo.Resposta resposta =
                MenuInterativo.abrir(opcoes, "Carregar partida");

        if (resposta.resultado == MenuInterativo.Resultado.CANCELADO
                || resposta.indiceOpcao == opcoes.size() - 1) {
            return;
        }

        int numeroSlot = resposta.indiceOpcao + 1;

        try {
            partidaAtual = GerenciadorSlots.carregar(PASTA_SAVES, numeroSlot);
            executarPartida();
        } catch (CarregamentoException | DadosInvalidosException e) {
            view.mostrarMensagem(
                    Cores.VERMELHO + "Erro ao carregar: "
                            + e.getMessage() + Cores.RESET
            );
        }
    }

    private void processarOpcao(String opcao) {
        switch (opcao) {
            case "1" -> {
                if (confirmarDescarte()) {
                    novaPartida();
                }
            }
            case "2" -> {
                if (partidaAtual == null) {
                    view.mostrarMensagem("Não há partida para continuar.");
                } else {
                    executarPartida();
                }
            }
            case "3" -> {
                if (confirmarDescarte()) {
                    carregarPartida();
                }
            }
            case "4" -> view.mostrarInstrucoes();
            case "5" -> abrirConfiguracoes();
            case "7" -> { view.mostrarCreditos();
                System.out.print("Pressione ENTER para voltar...");
                Entrada.lerLinha();
            }
            case "6" -> salvarPartidaManual();
            case "0" -> {
                if (confirmarDescarte()) { rodando = false;
                    view.mostrarMensagem("Saindo...");
                }
            }

            default -> view.mostrarMensagem("Opção inválida!");
        }
    }

    private boolean confirmarDescarte(){
        if (partidaAtual == null){
            return true;
        }

        boolean temAlteracoes = partidaAtual.temAlteracoes();
        boolean temSlotLivre = GerenciadorSlots
                .primeiroSlotLivre(PASTA_SAVES)
                .isPresent();
        DecisaoSaida decisao = DecisaoSaida.decidir(temAlteracoes, temSlotLivre);
        return switch (decisao){
            case SAIR -> true;
            case PERGUNTAR_SALVAR -> perguntarSalvar();
            case ESCOLHER_SLOT -> escolherSobrescrita(true);
        };
    }

    private void abrirConfiguracoes() {
        try {
            var config = ConfiguracoesAdmin.get();
            new TelaConfiguracoesController(config).iniciar();

        } catch (CarregamentoException e) {
            view.mostrarMensagem(
                    Cores.VERMELHO
                            + "Erro ao carregar config: "
                            + e.getMessage()
                            + Cores.RESET
            );

        } catch (DadosInvalidosException e) {
            view.mostrarMensagem(
                    Cores.VERMELHO
                            + "Config inválida: "
                            + e.getMessage()
                            + Cores.RESET
            );
        }
    }
    private void executarPartida() {
        List<Cena> cenas = criarCenas();
        int cenaId = partidaAtual.getCenaAtualId();

        boolean cenaExiste = cenas.stream().anyMatch(c -> c.getId() == cenaId);
        if (!cenaExiste) {
            view.mostrarMensagem(Cores.VERMELHO
                    + "Save inválido: a cena " + cenaId + " não existe."
                    + Cores.RESET);
            partidaAtual = null;
            return;
        }

        ResultadoPartida resultado = new CenasController(partidaAtual, cenas, new CenaView()).iniciar();

        if (resultado == ResultadoPartida.FIM_DE_JOGO) {
            partidaAtual = null;
        }
    }

    private void salvarPartidaManual() {
        if (partidaAtual == null) {
            view.mostrarMensagem("Não há partida para salvar.");
            return;
        }
        if (!partidaAtual.temAlteracoes()) {
            view.mostrarMensagem("Nada novo para salvar.");
            return;
        }
        salvarPartida();
    }

    private List<Cena> criarCenas(){
        List<Cena> cenas = new ArrayList<>();

        cenas.addAll(Abertura.criarCenas());
        cenas.addAll(Capitulo1.criarCenas());
        cenas.addAll(Capitulo2.criarCenas());
        cenas.addAll(Capitulo3.criarCenas());
        cenas.addAll(Capitulo4.criarCenas());
        cenas.addAll(Capitulo5A.criarCenas());
        cenas.addAll(Capitulo5B.criarCenas());
        cenas.addAll(Capitulo5C.criarCenas());
        cenas.addAll(Capitulo6.criarCenas());
        cenas.addAll(Capitulo7.criarCenas());
        cenas.addAll(Capitulo8.criarCenas());
        return cenas;
    }

    private void novaPartida() {
        Protagonista protagonista =
                new TelaCaracteristicasController(new TelaCaracteristicas()).iniciar();

        partidaAtual = new Estado();
        partidaAtual.iniciar(protagonista);
        partidaAtual.setCenaAtualId(700);

        executarPartida();
    }

    private boolean escolherSobrescrita(boolean permitirSairSemSalvar) {
        List<MenuInterativo.Opcao> opcoes = new ArrayList<>();
        for (String slot : GerenciadorSlots.listar(PASTA_SAVES)) {
            opcoes.add(new MenuInterativo.Opcao(slot));
        }

        int indiceSairSemSalvar = -1;
        if (permitirSairSemSalvar) {
            indiceSairSemSalvar = opcoes.size();
            opcoes.add(new MenuInterativo.Opcao("Sair sem salvar"));
        }

        int indiceCancelar = opcoes.size();
        opcoes.add(new MenuInterativo.Opcao("Cancelar"));

        MenuInterativo.Resposta resposta = MenuInterativo.abrir(
                opcoes, "Slots cheios: o slot escolhido será sobrescrito.");

        if (resposta.resultado == MenuInterativo.Resultado.CANCELADO
                || resposta.indiceOpcao == indiceCancelar) {
            return false;
        }
        if (resposta.indiceOpcao == indiceSairSemSalvar) {
            return true;
        }
        return confirmarSobrescrita(resposta.indiceOpcao + 1);
    }

    private boolean confirmarSobrescrita(int numeroSlot) {
        List<MenuInterativo.Opcao> opcoes = List.of(
                new MenuInterativo.Opcao("Sim, sobrescrever"),
                new MenuInterativo.Opcao("Não")
        );

        MenuInterativo.Resposta resposta =
                MenuInterativo.abrir(
                        opcoes,
                        "Sobrescrever Slot " + numeroSlot + "?"
                );

        if (resposta.resultado == MenuInterativo.Resultado.CANCELADO) {
            return false;
        }

        return resposta.indiceOpcao == 0 && salvarPartida(numeroSlot);
    }



    private boolean perguntarSalvar() {
        List<MenuInterativo.Opcao> opcoes = List.of(
                new MenuInterativo.Opcao("Salvar"),
                new MenuInterativo.Opcao("Não salvar"),
                new MenuInterativo.Opcao("Cancelar")
        );

        MenuInterativo.Resposta resposta =
                MenuInterativo.abrir(opcoes, "Salvar progresso?");

        if (resposta.resultado == MenuInterativo.Resultado.CANCELADO) {
            return false;
        }

        return switch (resposta.indiceOpcao) {
            case 0 -> salvarPartida();
            case 1 -> true;
            default -> false;
        };
    }

    private boolean salvarPartida() {
        OptionalInt slotLivre = GerenciadorSlots.primeiroSlotLivre(PASTA_SAVES);

        if (slotLivre.isEmpty()) {
            return escolherSobrescrita(true);
        }

        return salvarPartida(slotLivre.getAsInt());
    }

    private boolean salvarPartida(int numeroSlot) {
        try {
            GerenciadorSlots.salvar(partidaAtual, PASTA_SAVES, numeroSlot);
            view.mostrarMensagem("Partida salva no Slot " + numeroSlot + "!");
            return true;
        } catch (SaveException e) {
            view.mostrarMensagem(
                    Cores.VERMELHO + "Erro ao salvar: " + e.getMessage() + Cores.RESET
            );
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}