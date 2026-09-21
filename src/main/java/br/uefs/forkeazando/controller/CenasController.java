package br.uefs.forkeazando.controller;
import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.view.CenaView;

import java.util.List;
import java.util.Scanner;

public class CenasController {

    private final Estado estado;
    private final List<Cena> cenas;
    private final CenaView view;
    private final Scanner scanner = new Scanner(System.in);

    public CenasController(Estado estado, List<Cena> cenas, CenaView view) {
        this.estado = estado;
        this.cenas = cenas;
        this.view = view;
    }

    public void iniciar() {
        boolean jogando = true;
        while (jogando) {
            Cena atual = buscarCenaPorId(estado.getCenaAtualId());
            view.renderizar(atual);

            List<Escolha> disponiveis = atual.getEscolhasDisposniveis(estado.getProtagonista());

            if (disponiveis.isEmpty()) {
                view.mostrarMensagem("Espere pelos próximos capítulos...");
                jogando = false;
                continue;
            }

            view.exibirEscolhas(disponiveis);

            String entrada = scanner.nextLine().trim();
            int indiceEscolhido = Integer.parseInt(entrada);
            Escolha escolhida = disponiveis.get(indiceEscolhido - 1);

            if (escolhida.getFlagConcedida() != null) {
                estado.getProtagonista().adicionarFlag(escolhida.getFlagConcedida());
            }

            Protagonista p = estado.getProtagonista();
            p.ganharScore(escolhida.getScoreGanho());
            p.ganharParticipacao(escolhida.getParticipacaoGanha());
            p.gastarEnergia(escolhida.getCustoEnergia());
            if (escolhida.getPersonagemAfetado() != null) {
                p.alterarRelacionamento(escolhida.getPersonagemAfetado(), escolhida.getRelacionamentoGanho());
            }
            if (escolhida.getCenaDestinoId() == Estado.CENA_ENCERRAR) {
                view.mostrarMensagem("Espere os próximos capítulos...");
                jogando = false;
                continue;

            }
            estado.setCenaAtualId(escolhida.getCenaDestinoId());
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