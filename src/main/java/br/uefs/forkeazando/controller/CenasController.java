package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.roteiro.Roteador;
import br.uefs.forkeazando.view.CenaView;
import br.uefs.forkeazando.view.Entrada;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class CenasController {
    private final Estado estado;
    private final List<Cena> cenas;
    private final CenaView view;

    public CenasController(Estado estado, List<Cena> cenas, CenaView view) {
        this.estado = estado;
        this.cenas = cenas;
        this.view = view;
    }

    public ResultadoPartida iniciar() {
        boolean jogando = true;
        while (jogando) {
            Cena atual = buscarCenaPorId(estado.getCenaAtualId());

            if (atual.getId() == 0) {
                view.mostrarAprovacao();
            }

            if (view.deveMostrarBencao(atual.getId(), estado.getProtagonista())) {
                view.mostrarBencaoCaramelo();
            }

            if (atual.getId() == 3050) {
                view.mostrarChuteDoSistema(estado.getProtagonista());
            }

            view.renderizar(atual, estado.getProtagonista());

            if (atual.getId() == 660) {
                view.mostrarEpilogos(estado.getProtagonista());
            }

            List<Escolha> disponiveis = atual.getEscolhasDisposniveis(estado.getProtagonista());

            if (disponiveis.isEmpty()) {
                view.mostrarMensagem("Fim do Jogo");
                jogando = false;
                continue;
            }

            view.exibirEscolhas(disponiveis);

            int indice = Entrada.lerInteiro(0, disponiveis.size());
            if (indice == 0){
                return ResultadoPartida.VOLTAR_AO_MENU;
            }
            Escolha escolhida = disponiveis.get(indice - 1);
            aplicarEscolha(escolhida);

            if (escolhida.getCenaDestinoId() == Estado.CENA_ENCERRAR) {
                jogando = tentarAvancarCapitulo();
                continue;
            }

            estado.setCenaAtualId(escolhida.getCenaDestinoId());
        }
        return ResultadoPartida.FIM_DE_JOGO;
    }

    private boolean tentarAvancarCapitulo() {
        int proximo = estado.getCapituloAtual() + 1;

        if (!Roteador.temProximoCapitulo(estado.getCapituloAtual())) {
            view.mostrarMensagem("Fim. Obrigado por jogar.");
            return false;
        }

        estado.avancarCapitulo();
        int primeiraCena = Roteador.proximaCena(proximo, estado.getProtagonista());

        if (primeiraCena == -1) {
            view.mostrarMensagem("Espere pelos próximos capítulos...");
            return false;
        }

        estado.setCenaAtualId(primeiraCena);
        return true;
    }

    private Escolha lerEscolha(List<Escolha> disponiveis) {
        int indice = Entrada.lerInteiro(1, disponiveis.size());
        if (indice == 0) {
            return null;
        }
        return disponiveis.get(indice - 1);
    }

    private void aplicarEscolha(Escolha escolhida) {
        estado.marcarAlterado();
        Protagonista p = estado.getProtagonista();
        int tamanhoListaAntes = p.getHistoricoFlags().size();

        if (escolhida.getFlagConcedida() != null) {
            p.adicionarFlag(escolhida.getFlagConcedida());
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
        if (escolhida.getCenaDestinoId() == 3050
                && !p.temFlag("sistema_chutou_ic")
                && !p.temFlag("sistema_chutou_dtec")
                && !p.temFlag("sistema_chutou_indep")) {
            String chute = calcularChute(p);
            p.adicionarFlag("sistema_chutou_" + chute);
        }

        notificaNovasFlags(p, tamanhoListaAntes);
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

    private static String calcularChute(Protagonista p) {
        int pontosIC = 0, pontosDTEC = 0, pontosIndep = 0;

        //  Q1: estudar vs mexer
        if (p.temFlag("sistema_q1_estudar")) {
            pontosIC += 3;
            pontosDTEC += 1;
            pontosIndep += 1;
        } else {
            pontosIC += 1;
            pontosDTEC += 3;
            pontosIndep += 1;
        }

        // ─── Q2: sozinha vs grupo ───
        if (p.temFlag("sistema_q2_sozinha")) {
            pontosIC += 1;
            pontosDTEC += 1;
            pontosIndep += 3;
        } else {
            pontosIC += 3;
            pontosDTEC += 2;
            pontosIndep += 1;
        }

        // ─── Q3: visível vs invisível ───
        if (p.temFlag("sistema_q3_visivel")) {
            pontosIC += 4;
            pontosDTEC += 1;
            pontosIndep += 1;
        } else {
            pontosIC += 1;
            pontosDTEC += 2;
            pontosIndep += 3;
        }

        // ─── Q4: planejar vs improvisar ───
        if (p.temFlag("sistema_q4_planejar")) {
            pontosIC += 3;
            pontosDTEC += 2;
            pontosIndep += 1;
        } else {
            pontosIC += 1;
            pontosDTEC += 2;
            pontosIndep += 4;
        }

        // ─── Sorteio ponderado ───
        int total = pontosIC + pontosDTEC + pontosIndep;
        int sorteio = new Random().nextInt(total);

        if (sorteio < pontosIC) {
            return "ic";
        }

        if (sorteio < pontosIC + pontosDTEC) {
            return "dtec";
        }

        return "indep";
    }
}