// Executa os efeitos das escolhas
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
import br.uefs.forkeazando.view.CenaView;
import br.uefs.forkeazando.view.widget.MenuInterativo;


import java.util.*;
import java.time.LocalDateTime;

public class CenasController {
    private final Estado estado;
    private final List<Cena> cenas;
    private final CenaView view;
    private final GerenciadorConquistas gerenciadorConquistas;
    private final GerenciadorProgresso gerenciadorProgresso;
    private final Progresso progresso;
    private static final double RECUPERAR_ENERGIA = 0.4; /** energia máxima devolvida */
    private String ultimocapExibido;

    public CenasController(Estado estado, List<Cena> cenas, CenaView view, Progresso progresso, GerenciadorProgresso gerenciadorProgresso) {
        this.estado = estado;
        this.cenas = cenas;
        this.view = view;
        this.progresso = progresso;
        this.gerenciadorConquistas = new GerenciadorConquistas(gerenciadorProgresso, view::mostrarMensagem);
        this.gerenciadorProgresso = gerenciadorProgresso;
        gerenciadorConquistas.carregarDesbloqueadas(progresso);

    }

    private enum ResultadoAvancoCapitulo {
        CAPITULO_AVANCADO,
        FIM_DE_JOGO,
        ERRO_DE_ROTA
    }


    public ResultadoPartida iniciar() {
        view.resetarCacheVisual();
        boolean jogando = true;
        while (jogando) {
            Cena atual = buscarCenaPorId(estado.getCenaAtualId());

            // A resolução do futuro é uma transição automática, não uma escolha de carreira.
            if (atual.getId() == ResolucaoDeFuturo.CENA_RESOLVER_FUTURO) {
                int cenaFinal = ResolucaoDeFuturo.resolverCena(estado.getProtagonista());
                estado.setCenaAtualId(cenaFinal);
                estado.marcarAlterado();
                continue;
            }

            String capituloAtual = atual.getCapitulo();
            if (!capituloAtual.equals(ultimocapExibido)){
                view.mostrarTransicao(capituloAtual);
                ultimocapExibido = capituloAtual;
            }
            if (CenasDeChefe.recebeBencaoDoCaramelo(atual.getId(), estado.getProtagonista())) {
                view.mostrarBencaoCaramelo();
            }

            if (atual.getId() == 3050 && !estado.getProtagonista().temFlag("viu_chute_sistema")) {
                view.mostrarChuteDoSistema(estado.getProtagonista());
                estado.getProtagonista().adicionarFlag("viu_chute_sistema");
                estado.marcarAlterado();
            }

            if (atual.getId() == 654
                    && !estado.getProtagonista().temFlag("viu_curriculo_cap8")) {
                view.mostrarCurriculo(estado.getProtagonista());
                estado.getProtagonista().adicionarFlag("viu_curriculo_cap8");
                estado.marcarAlterado();
            }

            if ((ResultadoCalculo.ehCenaDeResultado(atual.getId()) || atual.getId() == 212)
                    && ResultadoCalculo.assegurarNota(estado.getProtagonista(), atual.getId())) {
                estado.marcarAlterado();
            }

            view.renderizar(atual, estado.getProtagonista(),
                    estado.getSemestresExtras(),
                    VisuaisDeCena.paraCena(atual.getId()).orElse(null));
            if (atual.getId() == 660) {
                view.mostrarEpilogos(estado.getProtagonista());
            }

            List<Escolha> disponiveis = atual.getEscolhasDisponiveis(estado.getProtagonista());

            if (disponiveis.isEmpty()) {
                if (podeApagar(atual)
                    && atual.temEscolhaBloqueadaSoPorEnergia(estado.getProtagonista())){
                    entrarEmApagao(atual.getId());
                    continue;
                }
                view.mostrarMensagem("Fim do Jogo");
                jogando = false;
                continue;
            }

            int indice;

            while (true) {
                List<MenuInterativo.Opcao> opcoes = new ArrayList<>();

                for (Escolha escolha : disponiveis) {
                    opcoes.add(new MenuInterativo.Opcao(escolha.getTextoAlternativa()));
                }

                // Ações extras também ficam disponíveis pelo teclado
                opcoes.add(new MenuInterativo.Opcao("Abrir galeria"));
                opcoes.add(new MenuInterativo.Opcao("Voltar ao menu principal"));

                MenuInterativo.Resposta resposta =
                        MenuInterativo.abrir(opcoes, "O QUE VOCÊ VAI FAZER?");

                if (resposta.resultado != MenuInterativo.Resultado.SELECIONADO) {
                    // Esc: retorna ao menu principal.
                    return ResultadoPartida.VOLTAR_AO_MENU;
                }

                indice = resposta.indiceOpcao;

                if (indice == disponiveis.size()) {
                    view.mostrarGaleria(estado.getProtagonista());
                    continue;
                }

                if (indice == disponiveis.size() + 1) {
                    return ResultadoPartida.VOLTAR_AO_MENU;
                }

                break;
            }

            Escolha escolhida = disponiveis.get(indice);
            aplicarEscolha(escolhida);
            int destino = escolhida.getCenaDestinoId();


            if (destino == Estado.CENA_RETOMAR) {
                // Retoma o caminho original após o apagão.
                destino = estado.consumirDestinoPendente();

                Protagonista protagonista = estado.getProtagonista();
                protagonista.recuperarEnergia(
                        (int) Math.round(
                                protagonista.getEnergiaMaxima()
                                        * RECUPERAR_ENERGIA
                        )
                );

            } else if (podeApagar(atual)
                    && estado.getProtagonista().getEnergia() == 0) {

                entrarEmApagao(destino);
                continue;
            }



            if (destino == Estado.CENA_ENCERRAR) {
                ResultadoAvancoCapitulo resultadoAvanco =
                        tentarAvancarCapitulo();

                if (resultadoAvanco
                        == ResultadoAvancoCapitulo.CAPITULO_AVANCADO) {
                    continue;
                }

                if (resultadoAvanco
                        == ResultadoAvancoCapitulo.FIM_DE_JOGO) {
                    return ResultadoPartida.FIM_DE_JOGO;
                }

                return ResultadoPartida.ERRO_DE_ROTA;
            }

            estado.setCenaAtualId(destino);
        }
        return ResultadoPartida.FIM_DE_JOGO;
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



    private ResultadoAvancoCapitulo tentarAvancarCapitulo() {
        int capituloAtual = estado.getCapituloAtual();

        if (!Roteador.temProximoCapitulo(capituloAtual)) {
            view.mostrarMensagem("Fim. Obrigado por jogar.");
            return ResultadoAvancoCapitulo.FIM_DE_JOGO;
        }

        int proximoCapitulo = capituloAtual + 1;

        // Determina a rota antes de alterar o estado da partida
        int primeiraCena = Roteador.proximaCena(proximoCapitulo, estado);

        if (primeiraCena < 0) {
            view.mostrarMensagem("Não foi possível determinar a próxima rota. " + "A partida foi mantida na memória, " + "mas a rota precisa ser corrigida.");
            return ResultadoAvancoCapitulo.ERRO_DE_ROTA;
        }

        // A transição só ocorre depois de validar o destino.
        estado.avancarCapitulo();
        estado.setCenaAtualId(primeiraCena);
        estado.marcarAlterado();

        return ResultadoAvancoCapitulo.CAPITULO_AVANCADO;
    }

//
//    private Escolha lerEscolha(List<Escolha> disponiveis) {
//        int indice = Entrada.lerInteiro(1, disponiveis.size());
//        if (indice == 0) {
//            return null;
//        }
//        return disponiveis.get(indice - 1);
//    }

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
    private void registrarTrilhaConcedida(List<String> flagsConcedidas, Protagonista protagonista) {

        Estado.Trilha trilhaDaEscolha = identificarTrilhaNasFlags(flagsConcedidas);

        Estado.Trilha trilhaDoHistorico = identificarTrilhaNasFlags(protagonista.getHistoricoFlags());

        Estado.Trilha trilhaDoEstado = estado.getTrilha();

        // O Estado e o histórico precisam concordar.
        if (trilhaDoEstado != null && trilhaDoHistorico != null && trilhaDoEstado != trilhaDoHistorico) {
            throw new IllegalStateException(
                    "A trilha do Estado diverge das flags da protagonista."
            );
        }
        // Um Estado com trilha definida não pode perder sua flag correspondente
        if (trilhaDoEstado != null && trilhaDoHistorico == null) {
            throw new IllegalStateException(
                    "O Estado possui uma trilha, mas a protagonista não possui "
                            + "a flag correspondente."
            );
        }

        Estado.Trilha trilhaAtual = trilhaDoEstado != null ? trilhaDoEstado : trilhaDoHistorico;

        // Uma escolha posterior não pode substituir a trilha.
        if (trilhaDaEscolha != null && trilhaAtual != null && trilhaDaEscolha != trilhaAtual) {

            throw new IllegalStateException(
                    "Não é permitido trocar a trilha acadêmica escolhida."
            );
        }

        // Registra a primeira trilha selecionada.
        if (trilhaAtual == null && trilhaDaEscolha != null) {
            estado.definirTrilha(trilhaDaEscolha);
        } else if (trilhaDoEstado == null && trilhaDoHistorico != null) {
            // Recupera a trilha a partir das flags de uma partida antiga.
            estado.definirTrilha(trilhaDoHistorico);
        }
    }

    private Estado.Trilha identificarTrilhaNasFlags(List<String> flags) {
        boolean ic = flags.contains("trilha_ic");
        boolean dtec = flags.contains("trilha_dtec");
        boolean indep = flags.contains("trilha_indep");

        int quantidade = (ic ? 1 : 0) + (dtec ? 1 : 0) + (indep ? 1 : 0);

        if (quantidade > 1) {
            throw new IllegalStateException(
                    "A protagonista possui mais de uma trilha acadêmica."
            );
        }

        if (ic) {
            return Estado.Trilha.IC;
        }

        if (dtec) {
            return Estado.Trilha.DTEC;
        }

        if (indep) {
            return Estado.Trilha.INDEP;
        }

        return null;
    }

    private void aplicarEscolha(Escolha escolhida) {
        Protagonista p = estado.getProtagonista();
        boolean trancamentoJaRegistrado = p.temFlag("cap6_trancou");
        int tamanhoListaAntes = p.getHistoricoFlags().size();

        registrarTrilhaConcedida(escolhida.getFlagsConcedidas(), p);
        estado.marcarAlterado();

        for (String flag : escolhida.getFlagsConcedidas()) {
            p.adicionarFlag(flag);
        }
        if (!trancamentoJaRegistrado && p.temFlag("cap6_trancou")) {
            estado.adicionarSemestreExtra();
        }

        p.ganharScore(escolhida.getScoreGanho());
        p.ganharParticipacao(escolhida.getParticipacaoGanha());
        p.gastarEnergia(escolhida.getCustoEnergia());

        for (Map.Entry<PersonagemSecundario, Float> impacto : escolhida.getImpactosRelacionamento().entrySet()) {
            PersonagemSecundario npc = impacto.getKey();
            float valor = impacto.getValue();

            if (escolhida.getInteresseAlvo() != null && escolhida.getInteresseAlvo() == npc.getInteresse()) {
                valor += Math.signum(valor);
            }

            p.alterarRelacionamento(npc, valor);
            Protagonista.NivelRelacionamento nivel = Protagonista.categorizarRelacionamento(p.getRelacionamento(npc));
            view.mostrarImpactoRelacionamento(npc, valor, nivel);
        }

        for (Map.Entry<String, Integer> entry : escolhida.getAtributosAlterados().entrySet()) {
            p.alterarAtributo(entry.getKey(), entry.getValue());
        }

        // Sistema faz o chute depois da última resposta do Akinator
        if (escolhida.getCenaDestinoId() == 3050){
            SistemaAcademico.chute(p);
        }

        notificaNovasFlags(p, tamanhoListaAntes);
        salvarColecao(p);
        gerenciadorConquistas.verificar(p, progresso, view::mostrarConquista);
    }

    private void notificaNovasFlags(Protagonista p, int tamanhoAntes) {
        List<String> flags = p.getHistoricoFlags();

        for (int i = tamanhoAntes; i < flags.size(); i++) {
            String flag = flags.get(i);

            if (flag.startsWith("hab_")) {
                view.mostrarHabilidade(flag);
            } else if (flag.startsWith("item_")) {
                view.mostrarItem(flag);
            }

            // ‘flags’ comuns (traco_*, cap*, final_*, etc.) não notificam
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

    // FIX (Bug 3): calcularChute agora é DETERMINÍSTICO.
    // O sorteio ponderado original fazia dois jogadores com as mesmas 4 respostas
    // receberem chutes diferentes — o que contradizia o epílogo (mostrarPerfilAcademico),
    // que lê as respostas cruas. Determinístico mantém a coerência:
    // o Sistema é burro, mas é BURRO DE UM JEITO EXPLICÁVEL.
    //
    // Se você preferir manter o caos original, é só reintroduzir o sorteio:
    //   int total = pontosIC + pontosDTEC + pontosIndep;
    //   int sorteio = new Random().nextInt(total);
    //   if (sorteio < pontosIC) return "ic";
    //   if (sorteio < pontosIC + pontosDTEC) return "dtec";
    //   return "indep";
    private static String calcularChute(Protagonista p) {
        int pontosIC = 0, pontosDTEC = 0, pontosIndep = 0;

        if (p.temFlag("sistema_q1_estudar")) {
            pontosIC += 3; pontosDTEC += 1; pontosIndep += 1;
        } else {
            pontosIC += 1; pontosDTEC += 3; pontosIndep += 1;
        }

        if (p.temFlag("sistema_q2_sozinha")) {
            pontosIC += 1; pontosDTEC += 1; pontosIndep += 3;
        } else {
            pontosIC += 3; pontosDTEC += 2; pontosIndep += 1;
        }

        if (p.temFlag("sistema_q3_visivel")) {
            pontosIC += 4; pontosDTEC += 1; pontosIndep += 1;
        } else {
            pontosIC += 1; pontosDTEC += 2; pontosIndep += 3;
        }

        if (p.temFlag("sistema_q4_planejar")) {
            pontosIC += 3; pontosDTEC += 2; pontosIndep += 1;
        } else {
            pontosIC += 1; pontosDTEC += 2; pontosIndep += 4;
        }

        int max = Math.max(pontosIC, Math.max(pontosDTEC, pontosIndep));
        if (pontosIC == max) return "ic";       // empate → IC vence (burro de propósito)
        if (pontosDTEC == max) return "dtec";
        return "indep";
    }
}