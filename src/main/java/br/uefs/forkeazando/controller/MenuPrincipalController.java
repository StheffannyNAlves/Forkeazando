package br.uefs.forkeazando.controller;
import br.uefs.forkeazando.audio.GerenciadorAudio;
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.dao.ConfiguracoesAdmin;
import br.uefs.forkeazando.dao.GerenciadorSlots;
import br.uefs.forkeazando.roteiro.*;
import br.uefs.forkeazando.view.CenaView;
import br.uefs.forkeazando.view.TelaConfiguracoes;
import br.uefs.forkeazando.view.MenuPrincipal;
import br.uefs.forkeazando.view.TelaCaracteristicas;
import br.uefs.forkeazando.view.widget.MenuInterativo;
import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.model.Progresso;

import java.nio.file.Path;
import java.util.List;




/** Coordena o menu principal, o ciclo de vida da partida e o acesso a saves e configurações */
public class MenuPrincipalController {

    private static final Path PASTA_SAVES = GerenciadorSlots.PASTA_PADRAO;
    private static final int CENAS_POR_AUTOSAVE = 5;
    private GatilhoAutosave gatilhoAutosave = new GatilhoAutosave(CENAS_POR_AUTOSAVE);
    private final MenuPrincipal view;
    private boolean rodando = true;
    private Estado partidaAtual;
    private final GerenciadorProgresso gerenciadorProgresso = new GerenciadorProgresso(PASTA_SAVES);
    private Progresso progresso;
    private Integer slotAtual;
    private final ProgressoController progressoController;
    private final GerenciadorAudio gerenciadorAudio = new GerenciadorAudio();
    private final GerenciadorSavesController savesController;

    public MenuPrincipalController(MenuPrincipal view) {
        this.view = view;
        this.progressoController = new ProgressoController(view, this.gerenciadorProgresso);
        this.savesController = new GerenciadorSavesController(view, PASTA_SAVES);
    }

    public void iniciar() throws CarregamentoException, DadosInvalidosException {
        try {
            progresso = gerenciadorProgresso.carregar();
        } catch (CarregamentoException | DadosInvalidosException e) {
            view.mostrarErro("Erro ao carregar progresso global" + e.getMessage());
            progresso = null;
        }
        iniciarAudio();
        while (rodando) {
            List<MenuInterativo.Opcao> opcoes = List.of(
                    new MenuInterativo.Opcao("Nova Partida"),
                    new MenuInterativo.Opcao("Continuar"),
                    new MenuInterativo.Opcao("Carregar"),
                    new MenuInterativo.Opcao("Gerenciar saves"),
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
                    "6", "7", "8", "9", "0"
            };
            processarOpcao(codigos[indice]);
        }
        gerenciadorAudio.fechar();

    }
    private void iniciarAudio() {
        try{
            gerenciadorAudio.setVolume(ConfiguracoesAdmin.get().getVolume());
        } catch (CarregamentoException | DadosInvalidosException e) {
            view.mostrarMensagem("Não foi possível ler o volume salvo; usando o padrão.");
        }
        gerenciadorAudio.iniciar();
    }


    private void carregarPartida() {
        var selecao = savesController.carregarPartida();

        if (selecao.isEmpty()) {
            return;
        }

        GerenciadorSavesController.PartidaSelecionada selecionada =
                selecao.get();

        partidaAtual = selecionada.estado();
        slotAtual = selecionada.numeroSlot();
        gatilhoAutosave = new GatilhoAutosave(CENAS_POR_AUTOSAVE);
        try {
            executarPartida();
        } catch (CarregamentoException | DadosInvalidosException e) {
            view.mostrarErro("Erro ao carregar: " + e.getMessage());
        }
    }

    private void processarOpcao(String opcao) throws CarregamentoException, DadosInvalidosException {
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
            case "4" -> savesController.gerenciarSaves();
            case "5" -> view.mostrarInstrucoes();
            case "6" -> abrirConfiguracoes();
            case "7" -> salvarPartidaManual();
            case "8" -> view.mostrarCreditos();
            case "9" -> progressoController.visualizar(progresso);
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
        DecisaoSaida decisao = DecisaoSaida.decidir(temAlteracoes, temSlotLivre, deveConfirmarSaida());
        return switch (decisao) {
            case SAIR -> true;
            case PERGUNTAR_SALVAR -> perguntarSalvar();
            case ESCOLHER_SLOT -> savesController.escolherSobrescrita(partidaAtual
            ,true);
        };
    }

    private void abrirConfiguracoes() {
        try {
            var config = ConfiguracoesAdmin.get();
            new TelaConfiguracoesController(config, gerenciadorAudio, new TelaConfiguracoes()).iniciar();

        } catch (CarregamentoException e) {
            view.mostrarErro( "Erro ao carregar config: " + e.getMessage());

        } catch (DadosInvalidosException e) {
            view.mostrarErro( "Config inválida: " + e.getMessage());
        }
    }

    private void executarPartida() throws CarregamentoException, DadosInvalidosException {
        List<Cena> cenas = criarCenas();
        int cenaId = partidaAtual.getCenaAtualId();

        boolean cenaExiste = cenas.stream().anyMatch(c -> c.getId() == cenaId);
        if (!cenaExiste) {
            view.mostrarErro("Save inválido: a cena " + cenaId + " não existe.");
            partidaAtual = null;
            return;
        }

        Progresso emUso = (progresso != null) ? progresso : new Progresso(); // evita que o construtor de CenasController receba null, sem apagar ou sobrescrever automaticamente os dados inválidos.

        var config = ConfiguracoesAdmin.get();
        CenaView cenaView = new CenaView(config.isDicasAtalho(), config.getVelocidadeTexto());

        ResultadoPartida resultado = new CenasController(partidaAtual, cenas, cenaView, emUso, gerenciadorProgresso,
                savesController::salvarAutosave, gatilhoAutosave, this::salvarPartidaManual).iniciar();
        if (resultado == ResultadoPartida.FIM_DE_JOGO) {
            if (partidaAtual.isPartidaConcluida()) {
                savesController.salvarConclusao(partidaAtual, slotAtual);
            }
            partidaAtual = null;
            slotAtual = null;
        } else if (resultado == ResultadoPartida.ERRO_DE_ROTA) {
            view.mostrarMensagem("A partida foi mantida na memória. " + "Corrija a rota antes de continuar. "
                            + "O estado atual ainda não foi salvo automaticamente.");
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

        savesController.salvarPartida(partidaAtual)
                .ifPresent(numeroSlot -> slotAtual = numeroSlot);
    }

    private List<Cena> criarCenas() {
        return Roteiro.todasAsCenas();
    }

    private void novaPartida() throws CarregamentoException, DadosInvalidosException {
        Protagonista protagonista = new TelaCaracteristicasController(new TelaCaracteristicas()).iniciar();
        slotAtual = null;
        partidaAtual = new Estado();
        gatilhoAutosave = new GatilhoAutosave(CENAS_POR_AUTOSAVE);
        partidaAtual.iniciar(protagonista);
        partidaAtual.setCenaAtualId(700);

        executarPartida();
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
            case 0 -> savesController.salvarPartida(partidaAtual).isPresent();
            case 1 -> true;
            default -> false;
        };
    }


    private boolean deveConfirmarSaida(){
        try{
            return  ConfiguracoesAdmin.get().isConfirmarSaida();
        } catch (CarregamentoException | DadosInvalidosException e){
            return  true;}

    }


}