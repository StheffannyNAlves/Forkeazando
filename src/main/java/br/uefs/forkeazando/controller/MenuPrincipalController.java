package br.uefs.forkeazando.controller;
// Aqui nao trata nada do dao(LEMBRA DISSO)
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.dao.ConfiguracoesAdmin;
import br.uefs.forkeazando.dao.GerenciadorSlots;
import br.uefs.forkeazando.roteiro.*;
import br.uefs.forkeazando.view.CenaView;
import br.uefs.forkeazando.view.Cores;
import br.uefs.forkeazando.view.Entrada;
import br.uefs.forkeazando.view.MenuPrincipal;
import br.uefs.forkeazando.view.TelaCaracteristicas;
import br.uefs.forkeazando.view.widget.MenuInterativo;
import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.model.Progresso;
import br.uefs.forkeazando.view.ProgressoView;
import br.uefs.forkeazando.dao.GerenciadorSave;
import java.time.LocalDateTime;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class MenuPrincipalController {

    private final MenuPrincipal view;
    private boolean rodando = true;
    private static final Path PASTA_SAVES = Path.of("saves");
    private Estado partidaAtual;
    private final GerenciadorProgresso gerenciadorProgresso = new GerenciadorProgresso(PASTA_SAVES);
    private Progresso progresso;

    public MenuPrincipalController(MenuPrincipal view) {
        this.view = view;
    }

    public void iniciar() {
        try {
            progresso = gerenciadorProgresso.carregar();
        } catch (CarregamentoException | DadosInvalidosException e) {
            view.mostrarMensagem(Cores.VERMELHO + "Erro ao carregar progresso global" + e.getMessage() + Cores.RESET);
            progresso = null;
        }

        while (rodando) {
            List<MenuInterativo.Opcao> opcoes = List.of(
                    new MenuInterativo.Opcao("Nova Partida"),
                    new MenuInterativo.Opcao("Continuar"),
                    new MenuInterativo.Opcao("Carregar"),
                    new MenuInterativo.Opcao("Instruções"),
                    new MenuInterativo.Opcao("Opções de Estilo"),
                    new MenuInterativo.Opcao("Salvar"),
                    new MenuInterativo.Opcao("Créditos"),
                    new MenuInterativo.Opcao("Meu progresso"),
                    new MenuInterativo.Opcao("Sair")
            );

            MenuInterativo.Resposta resposta =
                    MenuInterativo.abrir(
                            opcoes, "FORKEAZANDO", view::mostrarMenu);
            view.pararAnimacao(); // interrompe a animacao antes de processaropcao() executar a proxima acao
            if (resposta.resultado
                    == MenuInterativo.Resultado.CANCELADO) {
                continue;
            }

            int indice = resposta.indiceOpcao;
            String[] codigos = {
                    "1", "2", "3", "4", "5",
                    "6", "7", "8", "0"
            };

            processarOpcao(codigos[indice]);
        }

    }


    private void carregarPartida() {
        List<String> slots = GerenciadorSlots.listar(PASTA_SAVES);
        List<MenuInterativo.Opcao> opcoes = new ArrayList<>();

        for (String slot : slots) {
            opcoes.add(new MenuInterativo.Opcao(slot));
        }

        int indiceAutosave = opcoes.size();
        opcoes.add(new MenuInterativo.Opcao("Carregar autosave"));

        int indiceExcluir = opcoes.size();
        opcoes.add(new MenuInterativo.Opcao("Excluir save"));

        int indiceCancelar = opcoes.size();
        opcoes.add(new MenuInterativo.Opcao("Cancelar"));

        MenuInterativo.Resposta resposta =
                MenuInterativo.abrir(opcoes, "Carregar partida");

        if (resposta.resultado != MenuInterativo.Resultado.SELECIONADO) {
            return;
        }

        int indice = resposta.indiceOpcao;

        if (indice == indiceCancelar) {
            return;
        }

        if (indice == indiceAutosave) {
            carregarAutosave();
            return;
        }

        if (indice == indiceExcluir) {
            excluirSave();
            return;
        }

        if (indice < 0 || indice >= slots.size()) {
            return;
        }

        // A lista de slots precisa manter a ordem Slot 1, Slot 2, Slot 3.
        int numeroSlot = indice + 1;

        try {
            partidaAtual = GerenciadorSlots.carregar(
                    PASTA_SAVES,
                    numeroSlot
            );

            executarPartida();
        } catch (CarregamentoException | DadosInvalidosException e) {
            view.mostrarMensagem(
                    Cores.VERMELHO + "Erro ao carregar: "
                            + e.getMessage() + Cores.RESET
            );
        }
    }

    private void carregarAutosave() {
        Path caminhoAutosave =
                PASTA_SAVES.resolve("autosave.json");

        if (!Files.exists(caminhoAutosave)) {
            view.mostrarMensagem("Ainda não existe um autosave.");
            return;
        }

        try {
            Estado estadoCarregado =
                    GerenciadorSave.carregar(caminhoAutosave);

            if (estadoCarregado == null
                    || estadoCarregado.getProtagonista() == null) {
                view.mostrarMensagem(
                        Cores.VERMELHO
                                + "O autosave não contém uma partida válida."
                                + Cores.RESET
                );
                return;
            }

            partidaAtual = estadoCarregado;
            executarPartida();

        } catch (CarregamentoException | DadosInvalidosException e) {
            view.mostrarMensagem(
                    Cores.VERMELHO
                            + "Erro ao carregar autosave: "
                            + e.getMessage()
                            + Cores.RESET
            );
        }
    }
    private void excluirSave() {
        List<String> slots = GerenciadorSlots.listar(PASTA_SAVES);
        List<MenuInterativo.Opcao> opcoes = new ArrayList<>();

        for (String slot : slots) {
            opcoes.add(new MenuInterativo.Opcao(slot));
        }

        int indiceCancelar = opcoes.size();

        opcoes.add(new MenuInterativo.Opcao("Cancelar"));

        MenuInterativo.Resposta resposta =
                MenuInterativo.abrir(opcoes, "Excluir save");

        if (resposta.resultado != MenuInterativo.Resultado.SELECIONADO) {
            return;
        }

        int indice = resposta.indiceOpcao;

        if (indice == indiceCancelar
                || indice < 0
                || indice >= slots.size()) {
            return;
        }

        if (slots.get(indice).contains(" - Vazio")) {
            view.mostrarMensagem("Esse slot já está vazio.");
            return;
        }

        int numeroSlot = indice + 1;

        List<MenuInterativo.Opcao> confirmacoes = List.of(
                new MenuInterativo.Opcao("Sim, excluir"),
                new MenuInterativo.Opcao("Não")
        );

        MenuInterativo.Resposta confirmacao =
                MenuInterativo.abrir(
                        confirmacoes,
                        "Excluir permanentemente o Slot "
                                + numeroSlot + "?"
                );

        if (confirmacao.resultado != MenuInterativo.Resultado.SELECIONADO
                || confirmacao.indiceOpcao != 0) {
            return;
        }

        try {
            GerenciadorSlots.excluir(PASTA_SAVES, numeroSlot);
            view.mostrarMensagem(
                    Cores.VERDE + "Slot " + numeroSlot
                            + " excluído." + Cores.RESET
            );
        } catch (SaveException e) {
            view.mostrarMensagem(
                    Cores.VERMELHO + "Erro ao excluir save: "
                            + e.getMessage() + Cores.RESET
            );
        }
    }

    private void visualizarProgresso() {
        if (progresso == null) {
            view.mostrarMensagem(Cores.VERMELHO + "Progresso indisponível. Verifique o arquivo saves/progresso.json." + Cores.RESET);
            return;
        }

        ProgressoView progressoView = new ProgressoView();
        progressoView.mostrarProgresso(progresso);
        progressoView.mostrarConquistas(progresso);
    }

    private void processarOpcao(String opcao) {
        switch (opcao) {
            case "1" -> {
                if (confirmarDescarte())
                    novaPartida();

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
            case "7" -> {
                view.mostrarCreditos();
                System.out.print("Pressione ENTER para voltar...");
                Entrada.lerLinha();
            }
            case "6" -> salvarPartidaManual();
            case "8" -> visualizarProgresso();
            case "0" -> {
                if (confirmarDescarte()) {
                    rodando = false;
                    view.mostrarMensagem("Saindo...");
                }
            }

            default -> view.mostrarMensagem("Opção inválida!");
        }
    }

    private boolean confirmarDescarte() {
        if (partidaAtual == null) {
            return true;
        }

        boolean temAlteracoes = partidaAtual.temAlteracoes();
        boolean temSlotLivre = GerenciadorSlots
                .primeiroSlotLivre(PASTA_SAVES)
                .isPresent();
        DecisaoSaida decisao = DecisaoSaida.decidir(temAlteracoes, temSlotLivre);
        return switch (decisao) {
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

        Progresso emUso = (progresso != null) ? progresso : new Progresso(); // evita que o construtor de CenasController receba null, sem apagar ou sobrescrever automaticamente os dados inválidos.

        ResultadoPartida resultado = new CenasController(partidaAtual, cenas, new CenaView(), emUso, gerenciadorProgresso).iniciar();

        if (resultado == ResultadoPartida.FIM_DE_JOGO
                || resultado == ResultadoPartida.VOLTAR_AO_MENU) {

            registrarProgresso();

            if (resultado == ResultadoPartida.FIM_DE_JOGO) {
                partidaAtual = null;
            }

        } else if (resultado == ResultadoPartida.ERRO_DE_ROTA) {

            view.mostrarMensagem("A partida foi mantida na memória. " + "Corrija a rota antes de continuar. "
                            + "O estado atual ainda não foi salvo automaticamente."
            );
        }
    }

    private void registrarProgresso() {
        if (progresso == null || partidaAtual == null) {
            view.mostrarMensagem(Cores.VERMELHO + "Progresso indisponível. Verifique o arquivo saves/progresso.json." + Cores.RESET);
            return;
        }

        Protagonista protagonista = partidaAtual.getProtagonista();
        LocalDateTime agora = LocalDateTime.now();

        boolean novoFinal = progresso.sincronizarFinais(protagonista, agora);
        boolean novaColecao = progresso.sincronizarColecao(protagonista, agora);

        try {
            gerenciadorProgresso.salvar(progresso);

            if (novoFinal) {
                view.mostrarMensagem(Cores.VERDE + "Novo final desbloqueado!" + Cores.RESET);
            }

            if (novaColecao) {
                view.mostrarMensagem(Cores.VERDE + "Coleção atualizada!" + Cores.RESET);
            }
        } catch (SaveException e) {
            view.mostrarMensagem(Cores.VERMELHO + "Não foi possível salvar o progresso global: " + e.getMessage() + Cores.RESET);
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

    private List<Cena> criarCenas() {
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
        cenas.addAll(Apagao.criarCenas());
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
            return escolherSobrescrita(false);
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
        }
    }


}