package br.uefs.forkeazando.view;

import br.uefs.forkeazando.model.CaracteristicasProtagonista;

import java.util.Scanner;
// INCOMPLETO
public class TelaCaracteristicas {
      private final Scanner scanner = new Scanner(System.in);

    public boolean perguntarPersonalidade() {
        System.out.println();
        System.out.println(Cores.CIANO + "╔══════════════════════════════════════════════════════════╗" + Cores.RESET);
        System.out.println(Cores.CIANO + "║                      Personalidade                        ║" + Cores.RESET);
        System.out.println(Cores.CIANO + "╠══════════════════════════════════════════════════════════╣" + Cores.RESET);
        System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [1] Perfeccionista        " + Cores.CIANO + "║" + Cores.RESET);
        System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [2] Relaxado(a)           " + Cores.CIANO + "║" + Cores.RESET);
        System.out.println(Cores.CIANO + "╚══════════════════════════════════════════════════════════╝" + Cores.RESET);

        return lerEscolhaBinaria();
    } // Ainda sem influencia


    public boolean perguntarSociavel() {
            System.out.println();
            System.out.println(Cores.CIANO + "╔══════════════════════════════════════════════════════════╗" + Cores.RESET);
            System.out.println(Cores.CIANO + "║                      VIDA SOCIAL                         ║" + Cores.RESET);
            System.out.println(Cores.CIANO + "╠══════════════════════════════════════════════════════════╣" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [1] Sim           " + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [2] Não           " + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "╚══════════════════════════════════════════════════════════╝" + Cores.RESET);

            return lerEscolhaBinaria();
        } // Ainda sem influencia

        public boolean perguntarTipoDeEstudo() {
            System.out.println();
            System.out.println(Cores.CIANO + "╔══════════════════════════════════════════════════════════╗" + Cores.RESET);
            System.out.println(Cores.CIANO + "║                     TIPO DE ESTUDO                       ║" + Cores.RESET);
            System.out.println(Cores.CIANO + "╠══════════════════════════════════════════════════════════╣" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [1] Teórico(a)    " + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [2] Prático(a)    " + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "╚══════════════════════════════════════════════════════════╝" + Cores.RESET);

            return lerEscolhaBinaria();
        }

        private boolean lerEscolhaBinaria() {
            System.out.print(Cores.AMARELO + "Escolha > " + Cores.RESET);
            return scanner.nextLine().trim().equals("1");
        }

        // Adicionar a resposta pela seta + enter, fica melhor assim. MAS TERMINE O QUE PRECISA SER FINALIZADO LOGO
        public CaracteristicasProtagonista.SituacaoEconomica perguntarSituacaoEconomica() {
            System.out.println();
            System.out.println(Cores.CIANO + "╔══════════════════════════════════════════════════════════╗" + Cores.RESET);
            System.out.println(Cores.CIANO + "║              SITUAÇÃO ECONÔMICA DA FAMÍLIA               ║" + Cores.RESET);
            System.out.println(Cores.CIANO + "╠══════════════════════════════════════════════════════════╣" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [1] Apertada      " + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [2] Estável       " + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [3] Confortável   " + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [4] Elite         " + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "╚══════════════════════════════════════════════════════════╝" + Cores.RESET);

            return switch (lerEscolha(4)) {
                case 1 -> CaracteristicasProtagonista.SituacaoEconomica.APERTADA;
                case 2 -> CaracteristicasProtagonista.SituacaoEconomica.ESTAVEL;
                case 3 -> CaracteristicasProtagonista.SituacaoEconomica.CONFORTAVEL;
                default -> CaracteristicasProtagonista.SituacaoEconomica.ELITE;
            };
        }

        private int lerEscolha(int max) {
            System.out.print(Cores.AMARELO + "Escolha > " + Cores.RESET);
            String entrada = scanner.nextLine().trim();
            return Integer.parseInt(entrada); // esse metodo converte texto pra numero, ajuda dms
        }

        public CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio perguntarExperienciaEM() {
            System.out.println();
            System.out.println(Cores.CIANO + "╔══════════════════════════════════════════════════════════╗" + Cores.RESET);
            System.out.println(Cores.CIANO + "║                 EXPERIÊNCIA NO ENSINO MÉDIO              ║" + Cores.RESET);
            System.out.println(Cores.CIANO + "╠══════════════════════════════════════════════════════════╣" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [1] Difícil                                           " + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [2] Mediano                                           " + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [3] Excelente                                         " + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "╚══════════════════════════════════════════════════════════╝" + Cores.RESET);

            return switch (lerEscolha(3)) {
                case 1 -> CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEGATIVA;
                case 2 -> CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA;
                default -> CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.POSITIVA;
            };
        }

        public CaracteristicasProtagonista.NivelVidaSocial perguntarVidaSocial() {
            System.out.println();
            System.out.println(Cores.CIANO + "╔══════════════════════════════════════════════════════════╗" + Cores.RESET);
            System.out.println(Cores.CIANO + "║                   VIDA SOCIAL                            ║" + Cores.RESET);
            System.out.println(Cores.CIANO + "╠══════════════════════════════════════════════════════════╣" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [1] Isolado(a)    " + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [2] Equilibrado(a)" + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  [3] Muito ativo(a) / amigueiro(a) " + Cores.CIANO + "║" + Cores.RESET);
            System.out.println(Cores.CIANO + "╚══════════════════════════════════════════════════════════╝" + Cores.RESET);

            return switch (lerEscolha(3)) {
                case 1 -> CaracteristicasProtagonista.NivelVidaSocial.ISOLADO;
                case 2 -> CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO;
                default -> CaracteristicasProtagonista.NivelVidaSocial.AMIGUEIRO;
            };
        }
    }