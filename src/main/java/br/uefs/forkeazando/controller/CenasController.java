package br.uefs.forkeazando.controller;
import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.*;
import br.uefs.forkeazando.roteiro.CenasDeChefe;
import br.uefs.forkeazando.roteiro.Roteador;
import br.uefs.forkeazando.roteiro.ResultadoCalculo;
import br.uefs.forkeazando.roteiro.ResolucaoDeFuturo;
import br.uefs.forkeazando.roteiro.SistemaAcademico;
import br.uefs.forkeazando.roteiro.VisuaisDeCena;
import br.uefs.forkeazando.view.ApresentacaoCena;
import br.uefs.forkeazando.view.widget.MenuInterativo;


import java.util.*;
import java.time.LocalDateTime;

public class CenasController {
    private final Estado estado;
    private final List<Cena> cenas;
    private final ApresentacaoCena view;
    private final GerenciadorConquistas gerenciadorConquistas;
    private final GerenciadorProgresso gerenciadorProgresso;
    private final Progresso progresso;
    private static final double RECUPERAR_ENERGIA = 0.4; /** energia máxima devolvida */
    private final GatilhoAutosave gatilhoAutosave;
    private final Runnable salvarManual;
    private final SeletorDeOpcoes seletor;
    private final Autosave autosave;
    private String ultimocapExibido;
    private static final int CENA_CHUTE_SISTEMA = 3050;
    private static final int CENA_CURRICULO = 654;
    private static final int CENA_EPILOGOS = 660;
    private static final int CENA_RESULTADO_LEGADO = 212;
    private Integer cenaPreparadaId = null;

    public CenasController(Estado estado, List<Cena> cenas, ApresentacaoCena view, Progresso progresso, GerenciadorProgresso gerenciadorProgresso, Autosave autosave, GatilhoAutosave gatilhoAutosave, Runnable salvarManual) {
        this(estado, cenas, view, progresso, gerenciadorProgresso, autosave, gatilhoAutosave, salvarManual, SeletorDeOpcoes.terminal());
    }

    public CenasController(Estado estado, List<Cena> cenas, ApresentacaoCena view, Progresso progresso, GerenciadorProgresso gerenciadorProgresso, Autosave autosave, GatilhoAutosave gatilhoAutosave, Runnable salvarManual, SeletorDeOpcoes seletor) {
        this.estado = estado;
        this.cenas = cenas;
        this.view = view;
        this.progresso = progresso;
        this.gerenciadorConquistas = new GerenciadorConquistas(gerenciadorProgresso, view::mostrarMensagem);
        this.gerenciadorProgresso = gerenciadorProgresso;
        this.autosave = Objects.requireNonNull(autosave);
        this.gatilhoAutosave = Objects.requireNonNull(gatilhoAutosave);
        this.salvarManual = Objects.requireNonNull(salvarManual);
        this.seletor = Objects.requireNonNull(seletor);
        gerenciadorConquistas.carregarDesbloqueadas(progresso);

    }

    public enum ResultadoTransicao {
        CONTINUAR_JOGO,
        FIM_DE_JOGO,
        ERRO_DE_ROTA
    }

    private ResultadoTransicao processarDestino(int destino){
        if (destino != Estado.CENA_ENCERRAR) {
            estado.setCenaAtualId(destino);
            return ResultadoTransicao.CONTINUAR_JOGO;
        }

        ResultadoTransicao resultado = tentarAvancarCapitulo();

        return switch (resultado) {
            case CONTINUAR_JOGO -> ResultadoTransicao.CONTINUAR_JOGO;
            case FIM_DE_JOGO -> ResultadoTransicao.FIM_DE_JOGO;
            case ERRO_DE_ROTA -> ResultadoTransicao.ERRO_DE_ROTA;
        };
    }

    public ResultadoTransicao registrarEscolha(Escolha escolhida) {
        Cena atual = buscarCenaPorId(estado.getCenaAtualId());
        ResultadoTransicao resultadoTransicao = processarEscolha(atual, escolhida);
        cenaPreparadaId = null;
        return resultadoTransicao;
    }

    // A resolução do futuro é uma transição automática, não uma escolha de carreira
    private boolean resolverTransicaoAutomatica(Cena atual){
        if (atual.getId() != ResolucaoDeFuturo.CENA_RESOLVER_FUTURO)
            return false;
        int cenaFinal = ResolucaoDeFuturo.resolverCena(estado.getProtagonista());
        estado.setCenaAtualId(cenaFinal);
        estado.marcarAlterado();
        return true;
    }
    private int aplicarEscolhaEObterDestino(Escolha escolhida){
        aplicarEscolha(escolhida);
        gatilhoAutosave.registrarCenaConcluida();
        return escolhida.getCenaDestinoId();
    }

    private ResultadoTransicao processarEscolha(Cena atual, Escolha escolhida){
        if (atual.getId() != estado.getCenaAtualId()) {
            throw new IllegalArgumentException("A cena informada não é a cena atual.");
        }

        if (!atual.getEscolhasDisponiveis(estado.getProtagonista()).contains(escolhida)) {
            throw new IllegalArgumentException("Escolha indisponível.");
        }
        int destino = aplicarEscolhaEObterDestino(escolhida);
        if (destino == Estado.CENA_RETOMAR) {
            // Retoma o caminho original após o apagão
            destino = estado.consumirDestinoPendente();
            Protagonista protagonista = estado.getProtagonista();
            protagonista.recuperarEnergia((int) Math.round(protagonista.getEnergiaMaxima() * RECUPERAR_ENERGIA));

        } else if (podeApagar(atual)
                && estado.getProtagonista().getEnergia() == 0) {

            entrarEmApagao(destino);
            return ResultadoTransicao.CONTINUAR_JOGO;
        }

        registrarFinalSeIdentificado();
        return processarDestino(destino);
    }

    public ResultadoPartida iniciar() {
        view.resetarCacheVisual();
        boolean jogando = true;
        while (jogando) {
            if (gatilhoAutosave.deveSalvar()){
                registrarAutosave();
            }
            Cena atual = buscarCenaPorId(estado.getCenaAtualId());

            if (resolverTransicaoAutomatica(atual)) {
                continue;
            }

            processarEntradaDaCena(atual);

            List<Escolha> disponiveis = atual.getEscolhasDisponiveis(estado.getProtagonista());

            if (disponiveis.isEmpty()) {
                if (podeApagar(atual)
                    && atual.temEscolhaBloqueadaSoPorEnergia(estado.getProtagonista())){
                    entrarEmApagao(atual.getId());
                    continue;
                }
                view.mostrarMensagem("Nenhuma escolha disponível nesta cena (erro de rota).");
                return ResultadoPartida.ERRO_DE_ROTA;
            }

            int indice;

            while (true) {
                List<MenuInterativo.Opcao> opcoes = new ArrayList<>();

                for (Escolha escolha : disponiveis) {
                    opcoes.add(new MenuInterativo.Opcao(escolha.getTextoAlternativa()));
                }

                // Ações extras também ficam disponíveis pelo teclado
                opcoes.add(new MenuInterativo.Opcao("Abrir galeria"));
                opcoes.add(new MenuInterativo.Opcao("Salvar partida"));
                opcoes.add(new MenuInterativo.Opcao("Voltar ao menu principal"));



                MenuInterativo.Resposta resposta = seletor.escolher(opcoes, "O QUE VOCÊ VAI FAZER?");

                if (resposta.resultado != MenuInterativo.Resultado.SELECIONADO) {
                    return ResultadoPartida.VOLTAR_AO_MENU;
                }

                indice = resposta.indiceOpcao;

                if (indice == disponiveis.size()) {
                    view.mostrarGaleria(estado.getProtagonista());
                    continue;
                }

                if (indice == disponiveis.size() + 1) {
                    salvarManual.run();
                    continue;
                }

                if (indice == disponiveis.size() + 2) {
                    return ResultadoPartida.VOLTAR_AO_MENU;
                }



                break;
            }

            Escolha escolhida = disponiveis.get(indice);
            ResultadoTransicao resultadoTransicao = processarEscolha(atual, escolhida);
            if (resultadoTransicao == ResultadoTransicao.CONTINUAR_JOGO) {
                continue;
            }
            if (resultadoTransicao == ResultadoTransicao.ERRO_DE_ROTA)
                return ResultadoPartida.ERRO_DE_ROTA;
            if (resultadoTransicao == ResultadoTransicao.FIM_DE_JOGO)
                return ResultadoPartida.FIM_DE_JOGO;

        }
        return ResultadoPartida.FIM_DE_JOGO;
    }

    public void prepararCenaAtual() {
        Cena atual = buscarCenaPorId(estado.getCenaAtualId());

        // Evita ciclos entre transições automáticas.
        Set<Integer> visitadas = new HashSet<>();

        while (true) {
            if (!visitadas.add(atual.getId())) {
                throw new IllegalStateException("Ciclo de transições automáticas na cena: " + atual.getId());
            }
            if (!resolverTransicaoAutomatica(atual)) {
                break;
            }
            atual = buscarCenaPorId(estado.getCenaAtualId());
        }
        // A entrada desta cena já foi processada
        if (Objects.equals(cenaPreparadaId, atual.getId())) {
            return;
        }
        cenaPreparadaId = atual.getId();
        try {
            processarEntradaDaCena(atual);
        } catch (RuntimeException e) {
            cenaPreparadaId = null;
            throw e;
        }
    }


    private void processarEntradaDaCena(Cena atual) {
        String capituloAtual = atual.getCapitulo();
        if (!capituloAtual.equals(ultimocapExibido)){
            view.mostrarTransicao(capituloAtual);
            ultimocapExibido = capituloAtual;
        }
        if (CenasDeChefe.recebeBencaoDoCaramelo(atual.getId(), estado.getProtagonista())) {
            view.mostrarBencaoCaramelo();
        }

        if (atual.getId() == CENA_CHUTE_SISTEMA && !estado.getProtagonista().temFlag("viu_chute_sistema")) {
            view.mostrarChuteDoSistema(estado.getProtagonista());
            estado.getProtagonista().adicionarFlag("viu_chute_sistema");
            estado.marcarAlterado();
        }

        if (atual.getId() == CENA_CURRICULO
                && !estado.getProtagonista().temFlag("viu_curriculo_cap8")) {
            view.mostrarCurriculo(estado.getProtagonista());
            estado.getProtagonista().adicionarFlag("viu_curriculo_cap8");
            estado.marcarAlterado();
        }

        if ((ResultadoCalculo.ehCenaDeResultado(atual.getId()) || atual.getId() == CENA_RESULTADO_LEGADO)
                && ResultadoCalculo.assegurarNota(estado.getProtagonista(), atual.getId())) {
            estado.marcarAlterado();
        }

        view.renderizar(atual, estado.getProtagonista(), estado.getSemestresExtras(),VisuaisDeCena.paraCena(atual.getId()).orElse(null));
        if (atual.getId() == CENA_EPILOGOS) {
            view.mostrarEpilogos(estado.getProtagonista());
        }
    }

    private void registrarFinalSeIdentificado() {
        Protagonista p = estado.getProtagonista();
        Optional<Final> f = Final.identificar(p.getHistoricoFlags());
        f.ifPresent(aFinal -> estado.concluir(aFinal.getCodigo()));
    }

    private void registrarAutosave(){
        try {
            autosave.salvar(estado);
            view.mostrarMensagem("Progresso salvo automaticamente.");
        } catch (SaveException e){
            view.mostrarMensagem("Não foi possível salvar automaticamente: " + e.getMessage());
        }
    }
    private boolean podeApagar(Cena atual) { // esse metódo evita que o apagão dispare dentro do próprio apagão, que dispare com um destino pendente e que dispare no ultimo capitulo
        return !Estado.ehCenaDeApagao(atual.getId())
                && !estado.temDestinoPendente()
                && Roteador.temProximoCapitulo(estado.getCapituloAtual());
    }

    private void entrarEmApagao(int destinoOriginal) {
        estado.guardarDestinoPendente(destinoOriginal);
        estado.setCenaAtualId(Estado.CENA_APAGAO);
        estado.marcarAlterado();
    }



    private ResultadoTransicao tentarAvancarCapitulo() {
        int capituloAtual = estado.getCapituloAtual();

        if (!Roteador.temProximoCapitulo(capituloAtual)) {
            view.mostrarMensagem("Fim. Obrigado por jogar.");
            return ResultadoTransicao.FIM_DE_JOGO;
        }

        int proximoCapitulo = capituloAtual + 1;

        // Determina a rota antes de alterar o estado da partida
        int primeiraCena = Roteador.proximaCena(proximoCapitulo, estado);

        if (primeiraCena < 0) {
            view.mostrarMensagem("Não foi possível determinar a próxima rota. " + "A partida foi mantida na memória, " + "mas a rota precisa ser corrigida.");
            return ResultadoTransicao.ERRO_DE_ROTA;
        }

        // A transição só ocorre depois de validar o destino.
        estado.avancarCapitulo();
        estado.setCenaAtualId(primeiraCena);
        estado.marcarAlterado();

        return ResultadoTransicao.CONTINUAR_JOGO;
    }


    private void salvarColecao(Protagonista protagonista) {
        boolean novaColecao = progresso.sincronizarColecao(
                protagonista,
                LocalDateTime.now()
        );

        if (!novaColecao) {
            return;
        }

        try {
            gerenciadorProgresso.salvar(progresso);
        } catch (SaveException e) {
            view.mostrarMensagem("Erro ao salvar a coleção: " + e.getMessage());
        }
    }
    private void aplicarEscolha(Escolha escolhida) {
        Protagonista p = estado.getProtagonista();
        int tamanhoListaAntes = p.getHistoricoFlags().size();

        for (Estado.ImpactoRelacionamento impacto : estado.aplicarEscolha(escolhida)) {
            view.mostrarImpactoRelacionamento(impacto.personagem(), impacto.valor(), impacto.nivel());
        }

        String consequencia = escolhida.getTextoConsequencia();
        if (consequencia != null && !consequencia.isBlank()) {
            view.mostrarMensagem("CONSEQUÊNCIA DA ESCOLHA\n" + consequencia);
        }

        // O Sistema faz o chute depois da última resposta do Akinator.
        if (escolhida.getCenaDestinoId() == CENA_CHUTE_SISTEMA) {
            SistemaAcademico.chute(p);
        }

        notificaNovasFlags(p, tamanhoListaAntes);
        salvarColecao(p);
        gerenciadorConquistas.verificar(p, progresso, view::mostrarConquista);
    }

    // Avisa a trilha na hora; senão o jogador só descobre no Cap 5
    private void notificaNovasFlags(Protagonista p, int tamanhoAntes) {
        List<String> flags = p.getHistoricoFlags();
        for (int i = tamanhoAntes; i < flags.size(); i++) {
            String flag = flags.get(i);
            if (flag.startsWith("hab_")) {
                view.mostrarHabilidade(flag);
            } else if (flag.startsWith("item_")) {
                view.mostrarItem(flag);
            } else if (flag.equals("trilha_ic")
                    || flag.equals("trilha_dtec")
                    || flag.equals("trilha_indep")) {

                String nomeTrilha = flag.equals("trilha_ic")   ? "Iniciação Científica (IC)"
                        : flag.equals("trilha_dtec") ? "Monitoria (DTEC)"
                        :                              "Caminho Independente";
                view.mostrarMensagem("Sua trilha foi definida: " + nomeTrilha);
            }
        }
    }

    private Cena buscarCenaPorId(int id) {
        for (Cena c : cenas) {
            if (c.getId() == id) {
                return c;
            }
        }

        throw new IllegalStateException("Cena não encontrada: " + id);
    }

}