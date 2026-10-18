package br.uefs.forkeazando.view;

import br.uefs.forkeazando.model.*;

import java.util.ArrayList;
import java.util.List;
import br.uefs.forkeazando.roteiro.ApresentacaoDeCena;
import br.uefs.forkeazando.view.pixel.Animacao;
import br.uefs.forkeazando.view.pixel.Animador;
import br.uefs.forkeazando.view.pixel.Ilustracoes;
import br.uefs.forkeazando.view.pixel.Retratos;
import java.util.Optional;

public class CenaView {

    private static final int LARGURA = 64;
    private volatile boolean mostrarIndicadorPause = true;
    private volatile Protagonista protagonistaAtual;
    /** Largura mínima do terminal: a arte tem a largura da caixa (LARGURA + 2 bordas). */
    private static final int LARGURA_ARTE = LARGURA + 2;
    private static final int ALTURA_MINIMA_ARTE = 30;
    private static final int LARGURA_RETRATO = Retratos.LARGURA;
    private String ultimaIlustracao;

    private enum DesempenhoAcademico {
        DIFICULDADE, RECUPERACAO, DESTAQUE
    }

    private enum BemEstarFinal {
        FRAGILIZADO, OSCILANTE, ESTAVEL
    }



    private static final List<Integer> CENAS_DE_CHEFE = List.of(201, 305, 451, 550, 601, 649);

    private String indicadorPause() {
        if (Entrada.estaPausado()) {
            return "[C] Continuar";
        }
        return "[P] Pausar";
    }

    //    private void atualizarIndicadorPause() {
//        System.out.print("\033[s");
//
//        System.out.print("\033[2;1H");
//        System.out.print("\033[2K");
//
//        imprimirLinha(cabecalhoStatus(protagonistaAtual), Cores.CINZA);
//
//        System.out.print("\033[u");
//        System.out.flush();
//    }


    public void mostrarGaleria(Protagonista protagonista) {
        List<String> flags = protagonista.getHistoricoFlags();

        System.out.println("\n========== GALERIA ==========");
        System.out.println("\n--- HABILIDADES ---");

        boolean encontrou = false;

        for (String flag : flags) {
            if (flag.startsWith("hab_")) {
                mostrarHabilidade(flag);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhuma habilidade desbloqueada ainda.");
        }

        System.out.println("\n--- ITENS ---");
        encontrou = false;

        for (String flag : flags) {
            if (flag.startsWith("item_")) {
                mostrarItem(flag);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum item conquistado ainda.");
        }

        System.out.println("\n=============================");
        System.out.println("Pressione ENTER para voltar...");
    }

    public void renderizar(Cena cena, Protagonista p) {
        renderizar(cena, p, p.temFlag("cap6_trancou") ? 1 : 0, null);
    }

    /**
     * @param visual ilustração/retrato decididos pelo roteiro (null = só texto)
     */
    public void renderizar(Cena cena, Protagonista p,
                           int semestresExtras, ApresentacaoDeCena visual)  {
        Entrada.permitirPausa(true);
        try {
            String corTexto = corPorFalante(cena.getPersonagemFalando());

            protagonistaAtual = p;
            System.out.println();
            mostrarIlustracao(visual);
            imprimirLinha(cena.getCapitulo().toUpperCase(), Cores.BRANCO);
            imprimirLinha(cabecalhoStatus(p), Cores.CINZA);   // <- usar BRANCO se não tiver CINZA
            System.out.println(Cores.CIANO + "╠" + "═".repeat(LARGURA) + "╣" + Cores.RESET);

            if (!cena.getPersonagemFalando().equalsIgnoreCase("Narrador")) {
                if (!mostrarRetrato(cena.getPersonagemFalando(), corTexto, visual)) {
                    imprimirLinha(cena.getPersonagemFalando().toUpperCase(), corTexto);
                }
                imprimirLinha("", corTexto);
            }

            for (String paragrafo : cena.getTextoNarrativo().split("\n")) {
                Entrada.aguardarPause();
                if (paragrafo.equals("{{APROVACAO}}")) {
                    mostrarAprovacao();
                    continue;
                }

                if (paragrafo.equals("{{CALENDARIO_CAP7}}")) {
                    String textoCalendario;
                    if (semestresExtras > 0) {
                        String unidade = semestresExtras == 1 ? "semestre" : "semestres";
                        textoCalendario = "O trancamento acrescentou " + semestresExtras + " " + unidade
                                + " ao seu calendário. O registro guarda a data, mas não explica "
                                + "o que tornou a pausa necessária.";
                    } else {
                        textoCalendario = "Você continuou no calendário previsto. Isso não significa "
                                + "que a crise tenha passado sem deixar marcas na sua relação com o tempo.";
                    }
                    for (String linha : quebrarLinha(textoCalendario, LARGURA - 4)) {
                        imprimirLinha(linha, Cores.CINZA);
                    }
                    continue;
                }

                if (paragrafo.equals("{{CALENDARIO_CAP8}}")) {
                    String textoCalendario;
                    if (semestresExtras > 0) {
                        String unidade = semestresExtras == 1 ? "semestre" : "semestres";
                        textoCalendario = "Sua formatura chegou depois do calendário inicial, com "
                                + semestresExtras + " " + unidade + " extra(s) registrado(s). "
                                + "O histórico mostra o tempo, mas não tudo o que você precisou reorganizar.";
                    } else {
                        textoCalendario = "Você chegou à formatura no calendário previsto. Isso não quer dizer "
                                + "que o caminho tenha sido simples ou que você tenha atravessado tudo sem pausa.";
                    }
                    for (String linha : quebrarLinha(textoCalendario, LARGURA - 4)) {
                        imprimirLinha(linha, Cores.CINZA);
                    }
                    continue;
                }

                if (paragrafo.isBlank()) {
                    imprimirLinha("", corTexto);
                    continue;
                }

                // Detecta pensamento interno: linha inteira entre parênteses
                String trimmed = paragrafo.trim();
                boolean ehPensamento = trimmed.startsWith("(") && trimmed.endsWith(")");
                String corLinha = ehPensamento ? Cores.CINZA : corTexto;

                for (String linhaQuebrada : quebrarLinha(paragrafo, LARGURA - 4)) {
                    imprimirLinha(linhaQuebrada, corLinha);
                    if (ehPensamento && linhaQuebrada.length() < 25) {
                        dormir(400);
                    }
                }
            }
            System.out.println(Cores.CIANO + "╚" + "═".repeat(LARGURA) + "╝" + Cores.RESET);
        } finally {
            Entrada.permitirPausa(false);
        }
    }


    private String cabecalhoStatus(Protagonista p) {
        String rank = p.getRank();
        int energia = p.getEnergia();
        int maxima = p.getEnergiaMaxima();
        String barra = barraEnergia(energia, maxima);
        return "RANK: " + rank + "  •  ENERGIA: " + barra + " " + energia + "/" + maxima + " " + indicadorPause();
    }

    private String barraEnergia(int atual, int maxima) {
        if (maxima <= 0) return "";
        int blocos = 10;
        int cheios = (int) Math.round((double) atual / maxima * blocos);
        cheios = Math.max(0, Math.min(blocos, cheios));
        return "▓".repeat(cheios) + "░".repeat(blocos - cheios);
    }



    public boolean deveMostrarBencao(int cenaId, Protagonista p) {
        return CENAS_DE_CHEFE.contains(cenaId) && p.temFlag("amigo_do_caramelo");
    }

    public void mostrarBencaoCaramelo() {
        System.out.println();
        System.out.println(Cores.AMARELO + ">>> O Caramelo passa do lado. Ele para. Ele te olha." + Cores.RESET);
        System.out.println(Cores.AMARELO + "    Ele vai embora." + Cores.RESET);
        System.out.println(Cores.CINZA + "    (Foi... um sinal?)" + Cores.RESET);
        System.out.println();
    }


    public void mostrarHabilidade(String flag) {
        String nome = Colecionavel.porFlag(flag)
                .map(Colecionavel::getNome)
                .orElse(flag);
        System.out.println();
        System.out.println(Cores.MAGENTA + "  ╭─ NOVA HABILIDADE ─────────────────╮" + Cores.RESET);
        System.out.println(Cores.MAGENTA + "  │  " + nome + Cores.RESET);
        System.out.println(Cores.MAGENTA + "  ╰───────────────────────────────────╯" + Cores.RESET);
        System.out.println();
    }

    public void mostrarItem(String flag) {
        String nome = Colecionavel.porFlag(flag)
                .map(Colecionavel::getNome)
                .orElse(flag);
        System.out.println();
        System.out.println(Cores.CIANO + "  ╭─ ITEM ADQUIRIDO ──────────────────╮" + Cores.RESET);
        System.out.println(Cores.CIANO + "  │  " + nome + Cores.RESET);
        System.out.println(Cores.CIANO + "  ╰───────────────────────────────────╯" + Cores.RESET);
        System.out.println();
    }


    public void mostrarConquista(Conquista conquista) {
        mostrarMensagem(
                "\n🏆 CONQUISTA DESBLOQUEADA: " + conquista.getTitulo()
                        + "\n" + conquista.getDescricao()
        );
    }


//    public void iniciarAnimacaoPause(Protagonista p) {
//        Thread animacao = new Thread(() -> {
//            while (true) {
//                dormir(500);
//                mostrarIndicadorPause = !mostrarIndicadorPause;
//                atualizarIndicadorPause();
//
//                System.out.print("\033[s");
//                System.out.print("\033[2;1H");
//                System.out.print("\033[2K");
//                imprimirLinha(cabecalhoStatus(p), Cores.CINZA);
//                System.out.print("\033[u");
//                System.out.flush();
//            }
//        });
//
//        animacao.setDaemon(true);
//        animacao.start();
//    }


    private void imprimirLinhaAprovacao(String linha, int margem) {
        System.out.print("║");

        for (int i = 0; i < margem; i++) {
            System.out.print(" ");
        }

        System.out.print(linha);

        for (int i = 0; i < margem; i++) {
            System.out.print(" ");
        }

        System.out.println("║");
    }

    private void apagarAprovacao(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            System.out.print("\033[2K");

            if (i < quantidade - 1) {
                System.out.print("\033[1A");
            }
        }

        System.out.print("\033[" + quantidade + "A");
    }

    private void dormir(long milissegundos) {
        long inicio = System.currentTimeMillis();

        while (System.currentTimeMillis() - inicio < milissegundos) {
            Entrada.aguardarPause();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
    public void mostrarAprovacao() {
        String[] linhas = {
                "╔══════════════════════════════════════════╗",
                "║                                          ║",
                "║              APROVADO(A)                 ║",
                "║                                          ║",
                "║       ENGENHARIA DE COMPUTAÇÃO           ║",
                "║                                          ║",
                "║   Universidade Estadual do Fork Perdido  ║",
                "║                  (UEFP)                  ║",
                "║                                          ║",
                "╚══════════════════════════════════════════╝"
        };

        int largura = linhas[0].length();
        int margem = (LARGURA - largura) / 2;

        for (int ciclo = 0; ciclo < 3; ciclo++) {
            for (String linha : linhas) {
                imprimirLinhaAprovacao(linha, margem);
            }

            dormir(350);

            apagarAprovacao(linhas.length);

            dormir(200);
        }

        for (String linha : linhas) {
            imprimirLinhaAprovacao(linha, margem);
        }
    }

    public void mostrarImpactoRelacionamento(PersonagemSecundario npc, float valor,
                                             Protagonista.NivelRelacionamento nivel) {
        String nome = npc.getNome();
        String cor = valor > 0 ? Cores.VERDE : Cores.VERMELHO;
        String mudanca = valor > 0 ? "melhorou" : "piorou";

        System.out.println();
        System.out.println(cor + ">>> O teu relacionamento com " + nome + " " + mudanca + "." + Cores.RESET);
        System.out.println(Cores.CIANO + "[ Pressione ENTER para continuar... ]" + Cores.RESET);
        Entrada.lerLinha();
    }


    public void mostrarEpilogos(Protagonista p) {
        System.out.println();
        System.out.println(Cores.MAGENTA + "══════ O QUE FICOU ══════" + Cores.RESET);
        DesempenhoAcademico desempenho = avaliarDesempenhoAcademico(p);
        System.out.println();

        mostrarRetrospectivaTrilha(p, desempenho);
        mostrarEcosDosCapitulos(p);
        mostrarPerfilAcademico(p);
        mostrarEvolucaoPerfil(p);
        if (p.temFlag("mentoria_caio")) {
            String texto = p.temFlag("caio_revelacao")
                    ? "Cáio tá terminando o curso. Ainda atrasado, mas terminando. " +
                    "Ele te mandou mensagem semana passada. Disse que finalmente " +
                    "entendeu o que você quis dizer no primeiro semestre.\n\n" +
                    "Você respondeu que também demorou pra entender algumas coisas. " +
                    "Ele mandou um áudio de três minutos. Você ouviu até o fim."
                    : "Cáio não se formou. Trabalha numa empresa de hardware em " +
                    "Salvador, ganha menos do que merecia. O copo amassado " +
                    "continua com ele. Ou com você. Difícil saber.\n\n" +
                    "Vocês não conseguiram resolver tudo um pelo outro. " +
                    "Mas, em algum momento, isso deixou de ser o que importava.";
            renderizarEpilogo("CÁIO", texto);}
        if (p.temFlag("cap7_questionou_autoria")) {
            renderizarEpilogo("ORIENTADOR",
                    "O artigo saiu. Com o nome dele primeiro. Você foi segunda " +
                            "autora. Ele nunca mais te chamou pra outro projeto.\n\n" +
                            "Você perdeu uma oportunidade. Não perdeu o direito de " +
                            "reconhecer o próprio trabalho. Demorou pra acreditar nisso. " +
                            "Mas o seu nome continua no artigo.");
        } else if (p.temFlag("cap7_aceitou_autoria")) {
            renderizarEpilogo("ORIENTADOR",
                    "O artigo saiu. Com o nome dele primeiro. Ele te chamou pra " +
                            "mais dois projetos. Você aceitou os dois.\n\n" +
                            "O currículo cresceu. A dúvida também. Você ainda não sabe " +
                            "se fez a escolha certa. Pelo menos agora sabe que escolhas " +
                            "convenientes também têm consequências.");}
        if (Protagonista.categorizarRelacionamento(
                p.getRelacionamento(PersonagemSecundario.DANDARA))
                == Protagonista.NivelRelacionamento.RIVAL) {

            renderizarEpilogo("DANDARA",
                    "Dandara passou num concurso. Postou no LinkedIn, ganhou 200 " +
                            "curtidas, e depois sumiu por três meses. Você acha que ela " +
                            "tá bem.\n\n" +
                            "Durante muito tempo, o sucesso dela parecia diminuir o seu. " +
                            "Agora você consegue olhar pra própria trajetória sem precisar " +
                            "transformar a dela numa disputa.");
        }

        if (p.temFlag("cap3_colou_de_fato") || p.temFlag("bia_ainda_vende")) {
            renderizarEpilogo("BIA",
                    "Bia ainda vende resposta antiga. Só que agora é num site " +
                            "próprio. Ela te convidou pra ser sócia. Você ainda tá pensando.\n\n" +
                            "Talvez vocês ainda tenham alguma coisa pra construir juntas. " +
                            "Talvez não. Dessa vez, você sabe que não precisa responder " +
                            "só porque alguém espera uma resposta sua.");
        } else if (p.temFlag("hab_palavra_honesta")) {
            renderizarEpilogo("BIA",
                    "Bia parou de vender resposta. Ninguém sabe por quê. Você " +
                            "acha que foi você. Ela tá fazendo mestrado agora.\n\n" +
                            "Você nunca soube se aquela conversa mudou alguma coisa. " +
                            "Nem toda consequência chega com confirmação.");
        } else {
            renderizarEpilogo("BIA",
                    "Você não sabe o que aconteceu com a Bia. Ela sumiu depois do " +
                            "terceiro semestre. Ninguém do curso fala dela.\n\n" +
                            "Algumas histórias continuam sem resposta. Você aprendeu " +
                            "a conviver com isso, mesmo sem gostar.");
        }
        if (p.getConfianca() >= 7 && p.getVidaSocial() >= 6) {
            renderizarEpilogo("QUEM VOCÊ VIROU",
                    "Você sai da entrevista e alguém te liga. Não é a empresa. " +
                            "É um amigo. Você atende. Você ri. Você não lembra a última " +
                            "vez que riu no telefone.\n\n" +
                            "Por alguns minutos, você não pensa em currículo, salário " +
                            "ou no que deveria ter feito diferente. Só conversa. " +
                            "Você tinha esquecido que isso também fazia parte da vida.");
        } else if (p.getConfianca() <= 2) {
            renderizarEpilogo("QUEM VOCÊ VIROU",
                    "Você sai da entrevista e não tem ninguém pra ligar. Você " +
                            "abre o celular, olha a lista de contatos, fecha. Você vai " +
                            "pra casa sozinha.\n\n" +
                            "A solidão não desaparece só porque você chegou até aqui. " +
                            "Mas chegar até aqui também não foi pouca coisa. " +
                            "Amanhã ainda existe, mesmo que você não saiba o que fazer com ele.");
        }
        renderizarEpilogo("ZÉ DO MÓDULO 8",
                "O Zé continua lá. O bar tá maior agora. Ele te reconheceu quando " +
                        "você voltou no semestre passado e não cobrou a cerveja.\n\n" +
                        "Você pergunta por quê. Ele dá de ombros.\n\n" +
                        "\"Tu já gastou demais nessa faculdade, minha filha. Essa fica por conta da casa.\"");
        if (p.temFlag("cap8_resposta_verdadeira")) {
            renderizarEpilogo("UM ANO DEPOIS",
                    "Você falou da greve. Da Dandara. Das vezes em que quase " +
                            "desistiu e continuou mesmo assim.\n\n" +
                            "Você não contou uma história perfeita. Contou a sua. " +
                            "E, dessa vez, não precisou diminuir o que fez pra parecer " +
                            "que merecia estar ali.");
        } else if (p.temFlag("cap8_resposta_honesta")) {
            renderizarEpilogo("UM ANO DEPOIS",
                    "Você disse que ainda tava descobrindo.\n\n" +
                            "A entrevista não resolveu o futuro. O diploma também não. " +
                            "Mas, pela primeira vez, você não precisou fingir que sabia.\n\n" +
                            "Você passou anos tentando descobrir quem precisava ser " +
                            "pra chegar até aqui. Talvez agora possa descobrir quem " +
                            "quer ser depois daqui.");
        } else if (p.temFlag("cap8_resposta_conviccao")) {
            renderizarEpilogo("UM ANO DEPOIS",
                    "Você disse que escolheu aquela área. E era verdade. " +
                            "Não porque nunca teve dúvidas, mas porque teve dúvidas " +
                            "e continuou escolhendo.\n\n" +
                            "Talvez você mude de ideia um dia. Isso não torna " +
                            "o caminho que trouxe você até aqui menos seu.");
        } else {
            renderizarEpilogo("UM ANO DEPOIS",
                    "Você disse que precisava trabalhar. Não era uma resposta " +
                            "bonita. Era a verdade.\n\n" +
                            "Nem toda escolha começa com um sonho. Às vezes, começa " +
                            "com uma conta vencendo e alguém contando com você.\n\n" +
                            "Isso não torna a sua conquista menor.");
        }
        if (p.temFlag("cap8_sem_cerimonia")) {
            renderizarEpilogo("A FORMATURA",
                    "Você não teve a foto oficial com a beca, nem o nome anunciado " +
                            "no auditório. Talvez isso ainda doa quando alguém pergunta.\n\n" +
                            "Mas você concluiu o curso. A cerimônia era uma forma de " +
                            "marcar esse momento. Não era o que tornava os anos de esforço reais.");
        } else if (p.temFlag("cap8_vendeu_ingresso")) {
            renderizarEpilogo("A FORMATURA",
                    "Você vendeu o ingresso. A conta precisava fechar, e ela fechou.\n\n" +
                            "Por um tempo, você achou que tinha perdido a chance de " +
                            "comemorar. Depois entendeu que abrir mão de uma cerimônia " +
                            "não apaga aquilo que você conquistou.");
        } else if (p.temFlag("cap8_foi_formatura")) {
            renderizarEpilogo("A FORMATURA",
                    "Sua mãe ainda tem a foto da formatura no celular de lente " +
                            "trincada. Ela mostra a foto pra quem pergunta e pra quem " +
                            "não perguntou também.\n\n" +
                            "Você lembra do boleto, da beca alugada e de tudo que quase " +
                            "impediu aquele dia. Na foto, nada disso aparece. Mas você sabe.");
        }

        renderizarEpilogo("O QUE FICA",
                "Você não saiu da universidade com tudo resolvido.\n\n" +
                        "Ainda tem contas, dúvidas e coisas que você gostaria de ter " +
                        "feito diferente. Tem gente que ficou pelo caminho. Tem gente " +
                        "que ficou com você.\n\n" +
                        "Mas você chegou até aqui sem ser a mesma pessoa que entrou.\n\n" +
                        "O diploma não prova que você conseguiu fazer tudo certo. " +
                        "Prova que, apesar de tudo, você chegou ao fim de uma parte " +
                        "da história.\n\n" +
                        "A próxima não vem com gabarito.");

        mostrarEcoDaRespostaInicial(p);
        mostrarFuturoEscolhido(p);
        System.out.println(Cores.CIANO + "[ Pressione ENTER para encerrar. ]" + Cores.RESET);
        Entrada.lerLinha();
    }


    private void mostrarRetrospectivaTrilha(Protagonista p, DesempenhoAcademico desempenho) {
        if (p.temFlag("trilha_ic")) {
            String texto = switch (desempenho) {
                case DIFICULDADE -> "A iniciação científica foi sua primeira aproximação com perguntas sem resposta pronta. " +
                        "Nem sempre você teve tempo, orientação ou estabilidade para avançar como queria. " +
                        "Ainda assim, aprendeu a separar uma hipótese de uma certeza e a reconhecer o que não sabe.";
                case RECUPERACAO -> "Na iniciação científica, você descobriu que pesquisar não é provar que estava certa; " +
                        "é investigar o que os resultados sustentam. Aprendeu a revisar uma hipótese e a pedir que outra " +
                        "pessoa olhe para o que você não percebeu.";
                case DESTAQUE -> "A iniciação científica ajudou você a formular perguntas melhores e defender uma ideia " +
                        "sem transformar toda discordância em derrota. Você também aprendeu a reconhecer quando os dados " +
                        "obrigam a mudar de rumo.";
            };
            renderizarEpilogo("O QUE A PESQUISA DEIXOU", texto);
        } else if (p.temFlag("trilha_dtec")) {
            String texto = switch (desempenho) {
                case DIFICULDADE -> "A experiência de monitoria e ensino colocou você diante de pessoas que não aprendiam " +
                        "no mesmo ritmo. Nem sempre você soube explicar; algumas vezes precisou admitir que também não sabia. " +
                        "Isso faz parte de ensinar e de construir coisas.";
                case RECUPERACAO -> "A trilha prática ensinou você a decompor problemas, testar possibilidades e explicar " +
                        "o raciocínio sem esconder as tentativas que falharam. Você aprendeu que pedir outra opinião é parte do processo.";
                case DESTAQUE -> "Na trilha de ensino e prática, você aprendeu que saber fazer e conseguir explicar são " +
                        "habilidades diferentes. As duas podem ajudar uma equipe a sair de um problema que parecia travado.";
            };
            renderizarEpilogo("O QUE A PRÁTICA DEIXOU", texto);
        } else if (p.temFlag("trilha_indep") || p.temFlag("trilha_indefinida")) {
            String texto = switch (desempenho) {
                case DIFICULDADE -> "O caminho independente colocou você diante de orçamento, prazo e cliente antes de haver " +
                        "garantia de estabilidade. Você aprendeu trabalho técnico, mas descobriu que sustentar o trabalho também " +
                        "exige negociação, organização e uma rede de apoio.";
                case RECUPERACAO -> "Projetos independentes ensinaram você a traduzir uma necessidade em escopo e a perceber " +
                        "quando uma promessa está vaga. Nem sempre foi fácil defender seus limites, mas agora você reconhece " +
                        "que eles precisam existir.";
                case DESTAQUE -> "A trilha independente fortaleceu sua autonomia e também ensinou que autonomia não é fazer " +
                        "tudo só. Você aprendeu a negociar, procurar parcerias e recusar um projeto que não se sustenta.";
            };
            renderizarEpilogo("O QUE A AUTONOMIA DEIXOU", texto);
        }
    }

    private void mostrarEcosDosCapitulos(Protagonista p) {
        StringBuilder texto = new StringBuilder();

        // Capítulo 1: as primeiras estratégias continuam diferentes; não foram apagadas pelo diploma.
        if (p.temFlag("hab_olho_edital")) {
            texto.append("Na primeira semana, você aprendeu a guardar comprovantes e conferir instruções. " +
                    "Esse cuidado não resolveu toda a burocracia, mas deu a você um ponto de apoio.\n\n");
        } else if (p.temFlag("pediu_ajuda_grupo")) {
            texto.append("Na primeira semana, você procurou orientação em vez de fingir que já conhecia o campus. " +
                    "Pedir informação virou uma ferramenta, não uma confissão de incapacidade.\n\n");
        } else if (p.temFlag("aceitou_caos")) {
            texto.append("Na primeira semana, você resolveu seguir mesmo sem entender tudo. A adaptação ajudou, " +
                    "embora depois tenha sido necessário aprender a se organizar.\n\n");
        }

// Capítulo 2: limitações e pequenas oportunidades afetam as escolhas que continuam possíveis.
        if (p.temFlag("origem_apertada")) {
            texto.append("Você também aprendeu que tempo e dinheiro não são variáveis abstratas. Ônibus, alimentação, " +
                    "bolsa e trabalho mudaram o conjunto de escolhas que estava realmente ao seu alcance. " +
                    "Isso não foi falta de esforço.\n\n");
        }
        if (p.temFlag("ignorou_email")) {
            texto.append("Houve um aviso que você deixou passar. A universidade continuou funcionando como se todo mundo " +
                    "tivesse recebido, lido e entendido a mensagem. Você aprendeu a procurar a informação — e também " +
                    "que um sistema não deveria depender de um único e-mail.\n\n");
        } else if (p.temFlag("item_vaga_estagio")) {
            texto.append("Você investigou uma oportunidade de estágio antes de saber se ela caberia na sua rotina. " +
                    "Descobriu que oportunidade também tem custo, prazo e condição de acesso.\n\n");
        } else if (p.temFlag("ras_curiosa") || p.temFlag("tisp_interesse") || p.temFlag("wie_interesse")) {
            texto.append("Você experimentou espaços e comunidades antes de saber exatamente onde cabia. Nem todo contato " +
                    "virou projeto, mas alguns ajudaram a ampliar o que você imaginava possível.\n\n");
        }

// Capítulo 3: a integridade influencia como a protagonista lida depois com autoria e incerteza.
        if (p.temFlag("cap3_colou_de_fato")) {
            texto.append("Em uma avaliação, você cruzou uma linha para conseguir entregar. O resultado não desapareceu " +
                    "da sua história só porque o semestre acabou. Você teve de lidar com a diferença entre escapar de " +
                    "uma consequência e aprender com o que fez.\n\n");
        } else if (p.temFlag("hab_palavra_honesta")) {
            texto.append("Quando seria mais fácil esconder uma dúvida, você decidiu falar a verdade. Isso não resolveu " +
                    "tudo de imediato, mas tornou possível decidir com base no que realmente estava acontecendo.\n\n");
        }

// Capítulo 4: a forma como a protagonista pesquisou as opções influencia como o final apresenta suas escolhas.
        if (p.temFlag("cap4_leu_editais")) {
            texto.append("Antes de escolher uma trilha, você leu os editais e tentou entender os critérios. " +
                    "Nem toda regra estava escrita de um jeito útil, mas você aprendeu a procurar informação " +
                    "antes de assumir um compromisso.\n\n");
        } else if (p.temFlag("cap4_perguntou_alternativa") || p.temFlag("cap4_procurou_caio")) {
            texto.append("Na hora de escolher uma trilha, você procurou outra pessoa e perguntou o que não entendia. " +
                    "O conselho ajudou, mas a decisão e suas consequências continuaram sendo suas.\n\n");
        }

// Capítulo 5A: a primeira experiência com pesquisa deixou marcas, mesmo que o futuro escolhido mude.
        if (p.temFlag("cap5a_liderou_artigo")) {
            texto.append("No grupo de pesquisa, você assumiu a liderança de parte do artigo. Isso deu experiência e " +
                    "visibilidade, mas também tornou mais concreto o problema de dividir tarefas e reconhecer autoria.\n\n");
        } else if (p.temFlag("cap5a_perguntou_revisao")) {
            texto.append("Na pesquisa, você pediu revisão em vez de tratar o primeiro rascunho como pronto. Aprendeu que " +
                    "receber crítica pode fortalecer um trabalho, desde que haja espaço para discutir o que ela significa.\n\n");
        } else if (p.temFlag("cap5a_saiu_grupo")) {
            texto.append("Você saiu do grupo de pesquisa. A saída não apaga o que experimentou; ajudou a entender que " +
                    "uma oportunidade só vale a pena se as condições de trabalho também fizerem sentido.\n\n");
        }

// Capítulo 5C: preço e escopo importam para a trajetória independente e continuam relevantes em outros caminhos.
        if (p.temFlag("cap5c_pesquisou_preco")) {
            texto.append("No primeiro freela, você pesquisou preços antes de apresentar uma proposta. Descobriu que cobrar " +
                    "também envolve justificar o escopo e reconhecer o valor do próprio tempo.\n\n");
        } else if (p.temFlag("cap5c_cobrou")) {
            texto.append("No primeiro freela, você cobrou pelo trabalho sem ter certeza de como a outra pessoa reagiria. " +
                    "O desconforto não prova que o preço estava errado; foi parte de aprender a conversar sobre valor.\n\n");
        } else if (p.temFlag("cap5c_pediu_mais")) {
            texto.append("No primeiro freela, você pediu mais pelo trabalho. A negociação não garantiu que tudo fosse fácil, " +
                    "mas mostrou que a primeira oferta não precisa ser o limite da conversa.\n\n");
        } else if (p.temFlag("cap5c_deixou_rolar")) {
            texto.append("No primeiro freela, você deixou o valor em aberto e viu como a falta de um acordo claro cria " +
                    "dúvidas depois. Aprendeu por experiência que escopo e pagamento precisam ser conversados cedo.\n\n");
        }

// Capítulo 7: as experiências com ensino e as decisões finais sobre o TCC podem influenciar diferentes trajetórias profissionais.
        if (p.temFlag("cap7_dtec_deu_nota")) {
            texto.append("Na monitoria, você precisou avaliar o trabalho de outra pessoa. Isso trouxe a responsabilidade " +
                    "de explicar critérios e separar uma nota da pessoa que a recebe.\n\n");
        } else if (p.temFlag("cap7_dtec_ultima_aula")) {
            texto.append("Na última aula como monitora, você percebeu que ensinar também é reconhecer o que não cabe numa " +
                    "explicação só. A experiência pode continuar útil, mesmo fora da sala de aula.\n\n");
        }
        if (p.temFlag("cap7_tcc_improvisou")) {
            texto.append("Você terminou o TCC improvisando perto do prazo. O trabalho foi entregue, mas você lembra do custo " +
                    "de deixar decisões difíceis para o último momento.\n\n");
        } else if (p.temFlag("cap7_tcc_planejou")) {
            texto.append("Você dividiu o TCC em etapas e acompanhou o que faltava. O plano não impediu mudanças, mas ajudou " +
                    "a perceber os problemas antes que todos virassem urgência.\n\n");
        }

        // Capítulos 5B e 6: greve, apoio de pares e crise permanecem como parte da historia
        if (p.temFlag("hab_sobreviveu_greve")) {
            texto.append(p.temFlag("cap5b_dividiu_comida")
                    ? "Na greve, você atravessou dias difíceis dividindo o que tinha. A lembrança não transforma a falta " +
                    "de comida e de renda em uma aventura; mostra quem tentou se apoiar quando a estrutura falhou.\n\n"
                    : "A greve mudou sua rotina e o calendário. Você continuou estudando, mas isso não significa que os " +
                    "65 dias tenham sido simples ou que todo o custo tenha sido individual.\n\n");
        }
        if (p.temFlag("cap6_trancou")) {
            texto.append("Você trancou um semestre. O calendário mudou, e a pausa teve um custo real, mas não apagou o que " +
                    "você já tinha construído. Continuar depois não exigiu fingir que nada aconteceu.\n\n");
        } else if (p.temFlag("cap6_continuou")) {
            texto.append("Você continuou no semestre durante a crise. Isso não transforma sofrimento em mérito nem prova " +
                    "que insistir seja sempre a única resposta possível. Foi a decisão que fez sentido naquele momento.\n\n");
        }
        if (p.temFlag("cap6_pediu_ajuda")) {
            texto.append("Quando pediu ajuda, você deixou que outra pessoa soubesse o que estava acontecendo. O problema não " +
                    "sumiu por causa disso, mas você não precisou carregar tudo em silêncio.\n\n");
        }

        if (!texto.isEmpty()) {
            renderizarEpilogo("AS ESCOLHAS QUE CONTINUARAM COM VOCÊ", texto.toString().trim());
        }
    }

    private void mostrarEcoDaRespostaInicial(Protagonista p) {
        String texto;
        if (p.temFlag("prologo_resposta_pratica") && p.temFlag("cap8_resposta_conviccao")) {
            texto = "No começo, você respondeu que precisava daquele trabalho. Cinco anos depois, conseguiu dizer " +
                    "que também o escolheu. A necessidade não deixou de existir; ela passou a dividir espaço com " +
                    "uma decisão que você entende melhor.";
        } else if (p.temFlag("prologo_resposta_conviccao") && p.temFlag("cap8_resposta_honesta")) {
            texto = "No começo, você disse que tinha escolhido esse caminho. Anos depois, admitiu que ainda estava " +
                    "descobrindo. As duas respostas podem ser verdadeiras: escolher não exige prometer que nunca vai mudar.";
        } else if (p.temFlag("prologo_resposta_aberta") && p.temFlag("cap8_resposta_conviccao")) {
            texto = "No começo, você não sabia explicar o que estava buscando. No fim, conseguiu defender uma escolha " +
                    "sem precisar prometer que ela seria definitiva.";
        } else if (p.temFlag("prologo_resposta_pratica") && p.temFlag("cap8_resposta_honesta")) {
            texto = "Você começou falando da necessidade e terminou admitindo que ainda estava descobrindo. Não é uma " +
                    "resposta perfeita para entrevista, mas é uma medida mais honesta da pessoa que chegou até aqui.";
        } else {
            texto = "A pergunta da entrevista que abriu a história não ganhou uma resposta universal. O que mudou foi " +
                    "a sua capacidade de explicar de onde veio, o que escolheu e o que ainda não sabe.";
        }
        renderizarEpilogo("A PERGUNTA DO COMEÇO", texto);
    }

    private void mostrarFuturoEscolhido(Protagonista p) {
        String titulo;
        StringBuilder texto = new StringBuilder();

        if (p.temFlag("futuro_pesquisa_brasil")) {
            titulo = "SEU CAMINHO — PESQUISA NO BRASIL";
            texto.append("Você continua fazendo perguntas difíceis sem tratar o Brasil como sala de espera para um futuro " +
                    "mais legítimo. O trabalho pode acontecer numa universidade, num instituto, numa empresa ou em colaboração " +
                    "com outras equipes. Bolsa, orientação e recursos continuam sendo condições concretas, não garantias.\n\n");
            if (p.temFlag("trilha_ic")) {
                texto.append("Você já viu por dentro um pouco da pesquisa. Isso ajuda a reconhecer o trabalho que existe entre " +
                        "uma hipótese e um resultado, mas não garante vaga nem financiamento.\n\n");
            } else if (p.temFlag("trilha_dtec")) {
                texto.append("Você chega à pesquisa com experiência prática e de ensino. Vai precisar aprender métodos e escrita " +
                        "acadêmica, mas sabe que uma ideia também precisa ser construída, testada e explicada.\n\n");
            } else {
                texto.append("Seu caminho até aqui foi menos linear que uma seleção acadêmica costuma supor. Você precisará montar " +
                        "uma ponte entre as experiências anteriores e as perguntas que deseja investigar.\n\n");
            }
        } else if (p.temFlag("futuro_tecnologia")) {
            titulo = "SEU CAMINHO — TECNOLOGIA APLICADA";
            texto.append("Você escolhe construir tecnologia que precisa funcionar no mundo real: firmware, hardware, sistemas " +
                    "embarcados, software ou segurança. O primeiro produto não sai perfeito. Você vai testar, medir, revisar, " +
                    "documentar e explicar decisões para outras pessoas.\n\n");
            if (p.temFlag("trilha_dtec")) {
                texto.append("A experiência prática e de ensino ajuda você a traduzir conhecimento em procedimentos que outra pessoa " +
                        "consegue repetir. Ainda há ferramentas e domínios que você não conhece.\n\n");
            } else if (p.temFlag("trilha_ic")) {
                texto.append("O olhar investigativo que desenvolveu na pesquisa ajuda a formular hipóteses antes de trocar peças ou " +
                        "reescrever código no escuro. Agora você precisa conectar essa investigação a prazo, equipe e manutenção.\n\n");
            } else {
                texto.append("A experiência com projetos e negociação ajuda a entender o problema de quem vai usar a solução. " +
                        "Você também terá de aprofundar fundamentos técnicos e provar que o sistema funciona.\n\n");
            }
        } else if (p.temFlag("futuro_internacional")) {
            titulo = "SEU CAMINHO — ALÉM DAS FRONTEIRAS";
            texto.append("Você prepara uma oportunidade internacional. Idioma, portfólio, cartas, visto, financiamento e distância " +
                    "da rede de apoio entram na conta. Há candidaturas recusadas e planos ajustados; sair do país não acontece " +
                    "como recompensa automática. Uma colaboração internacional também pode começar sem mudança definitiva.\n\n");
            if (p.temFlag("origem_apertada")) {
                texto.append("A parte financeira pesa de forma concreta. Você procura bolsas, prazos e alternativas antes de assumir " +
                        "um custo que não consegue sustentar. Isso é planejamento, não falta de ambição.\n\n");
            } else {
                texto.append("Você compara a oportunidade com o que ela exige de você e de quem faz parte da sua vida. A decisão " +
                        "não precisa ser tomada para impressionar ninguém.\n\n");
            }
        } else if (p.temFlag("futuro_independente")) {
            titulo = "SEU CAMINHO — CARREIRA INDEPENDENTE";
            texto.append("Você escolhe montar uma carreira com clientes e projetos. Autonomia vem junto com orçamento, contrato, " +
                    "imposto, cobrança e períodos sem trabalho. Sustentabilidade passa por escolher melhor o que aceita e procurar " +
                    "parcerias, não por fazer tudo sem ajuda.\n\n");
            if (p.temFlag("cap7_indep_negociou")) {
                texto.append("Você já conseguiu revisar uma cláusula antes de assinar. Essa experiência não elimina o risco, mas deixa " +
                        "você mais preparada para definir escopo, prazo e pagamento antes de começar.\n\n");
            } else if (p.temFlag("cap7_indep_aceitou")) {
                texto.append("Você aceitou um contrato sem compreender tudo o que estava escrito. A consequência não define toda a sua " +
                        "carreira, mas deixou uma regra mais clara: urgência não transforma uma cláusula ruim em boa.\n\n");
            } else if (p.temFlag("cap7_indep_advogado")) {
                texto.append("Você procurou orientação antes de assumir um risco que não compreendia. Aprendeu que negociar também é " +
                        "conseguir tempo para ler e pedir ajuda.\n\n");
            } else if (p.temFlag("trilha_indefinida")) {
                texto.append("A entrada no trabalho independente não veio como um plano perfeito: você perdeu um prazo e precisou encontrar " +
                        "outra porta. Com o tempo, pode transformar essa contingência numa escolha mais deliberada.\n\n");
            }
        } else if (p.temFlag("futuro_transicao")) {
            titulo = "SEU CAMINHO — RECOMEÇAR SEM APAGAR O PASSADO";
            texto.append("Você muda de área e encara o custo real de aprender um vocabulário novo, construir portfólio e disputar " +
                    "oportunidades de entrada. A formação em Engenharia de Computação continua sendo parte de você, mas não substitui " +
                    "o que o novo caminho exige.\n\n");
            if (p.temFlag("cap6_trancou")) {
                texto.append("Você já precisou reorganizar o calendário uma vez. Isso não torna a transição fácil, mas ajuda a reconhecer " +
                        "que ajustar o plano não é o mesmo que abandonar tudo.\n\n");
            } else {
                texto.append("Você não precisou trancar o semestre para perceber que um caminho pode mudar. Continuar e mudar de direção " +
                        "não são decisões incompatíveis.\n\n");
            }
        } else {
            titulo = "SEU CAMINHO — AINDA EM ABERTO";
            texto.append("Esta partida chegou ao encerramento sem registrar uma escolha de futuro. Você concluiu a graduação, mas o jogo " +
                    "não presume qual profissão escolheu. Partidas antigas podem terminar assim; numa nova trajetória, a decisão final " +
                    "abre um dos cinco caminhos.");
        }

        if (p.temFlag("cap6_pediu_ajuda")) {
            texto.append("Você também tem uma lembrança concreta de ter pedido ajuda quando precisava. Na próxima etapa, isso pode significar " +
                    "procurar orientação, dividir uma tarefa ou admitir cedo que um acordo precisa ser revisto.\n\n");
        }
        if (p.temFlag("cap7_questionou_autoria")) {
            texto.append("Ao discutir a autoria do artigo, você defendeu o reconhecimento do seu trabalho mesmo sabendo que poderia perder " +
                    "uma oportunidade. Essa experiência não garante que todo ambiente seja justo; ajuda você a reconhecer a questão quando ela aparece.\n\n");
        } else if (p.temFlag("cap7_aceitou_autoria")) {
            texto.append("Você aceitou a decisão sobre a autoria e ganhou acesso a novos projetos. A experiência continua ambígua: oportunidades " +
                    "podem ter valor e, ao mesmo tempo, deixar uma dúvida sobre os limites que você gostaria de ter defendido.\n\n");
        }
        if (p.temFlag("hab_olho_edital") && p.temFlag("cap7_tcc_planejou")) {
            texto.append("No começo, você guardava comprovantes e conferia instruções; no fim, dividiu o TCC em etapas. Essas ações pequenas " +
                    "viraram uma forma concreta de proteger tempo e trabalho.\n\n");
        }
        if (p.temFlag("cap5b_dividiu_comida")) {
            texto.append("Você leva da greve a lembrança de que a continuidade dos estudos também depende de cuidado coletivo e condições materiais.\n\n");
        }
        renderizarEpilogo(titulo, texto.toString().trim());
    }


    static boolean arteCabe(int largura, int altura) {
        return largura >= LARGURA_ARTE && altura >= ALTURA_MINIMA_ARTE;
    }

    private boolean terminalComporta() {
        try {
            return arteCabe(Entrada.terminal().getWidth(), Entrada.terminal().getHeight());
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * Imprime a ilustração acima da caixa da cena. Só desenha quando a chave
     * muda: cenas seguidas no mesmo lugar não repetem 24 linhas de imagem.
     */
    private void mostrarIlustracao(ApresentacaoDeCena visual) {
        if (visual == null || visual.ilustracao() == null) {
            return;
        }
        String chave = visual.ilustracao();
        if (chave.equals(ultimaIlustracao) || !Ilustracoes.existe(chave) || !terminalComporta()) {
            return;
        }
        Optional<Animacao> animacao = Ilustracoes.animacao(chave);
        if (animacao.isPresent() && Animador.habilitada()) {
            Animador.tocar(animacao.get(), System.out, this::dormir);   // respeita a pausa do jogo
        } else {
            for (String linha : Ilustracoes.linhas(chave)) {
                System.out.println(linha + Cores.RESET);
            }
        }
        ultimaIlustracao = chave;
    }

    /**
     * Retrato à esquerda, nome do falante ao lado (centralizado na altura).
     * @return true se imprimiu o retrato (o nome já está incluso)
     */
    private boolean mostrarRetrato(String nomeFalante, String corTexto, ApresentacaoDeCena visual) {
        if (visual == null || visual.falante() == null || !terminalComporta()) {
            return false;
        }
        Optional<String[]> retrato = Retratos.linhas(visual.falante(), visual.expressao());
        if (retrato.isEmpty()) {
            return false;
        }
        String[] linhas = retrato.get();
        int campoNome = LARGURA - 2 - LARGURA_RETRATO - 2;
        String nome = nomeFalante.toUpperCase();
        if (nome.length() > campoNome) {
            nome = nome.substring(0, campoNome);
        }
        int meio = linhas.length / 2;
        for (int i = 0; i < linhas.length; i++) {
            String lateral = (i == meio)
                    ? corTexto + nome + Cores.RESET + " ".repeat(campoNome - nome.length())
                    : " ".repeat(campoNome);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  " + linhas[i] + Cores.RESET
                    + "  " + lateral + Cores.CIANO + "║" + Cores.RESET);
        }
        return true;
    }


    public void mostrarChuteDoSistema(Protagonista p) {
        String linhaDoChute;

        if (p.temFlag("sistema_chutou_ic")) {
            linhaDoChute = "Perfil compatível: IC. Iniciação Científica.";
        } else if (p.temFlag("sistema_chutou_dtec")) {
            linhaDoChute = "Perfil compatível: DTEC. Monitoria.";
        } else {
            linhaDoChute = "Perfil atípico. Sem categoria definida.";
        }

        System.out.println();
        System.out.println(Cores.AMARELO + "  ╭─────────────────────────────────────╮" + Cores.RESET);
        System.out.println(Cores.AMARELO + "  │  SISTEMA ACADÊMICO — VEREDITO       │" + Cores.RESET);
        System.out.println(Cores.AMARELO + "  ╰─────────────────────────────────────╯" + Cores.RESET);
        System.out.println();

        System.out.println(Cores.CINZA + "  'Processamento concluído.'" + Cores.RESET);
        System.out.println();
        System.out.println(Cores.AMARELO + "  " + linhaDoChute + Cores.RESET);
        System.out.println();

        System.out.println(Cores.CINZA + "  'Recomendação baseada em padrões históricos.'" + Cores.RESET);
        System.out.println(Cores.CINZA + "  'Ignore se preferir. O sistema não decide por você.'" + Cores.RESET);
        System.out.println(Cores.CINZA + "  'Ainda.'" + Cores.RESET);
        System.out.println();

        System.out.println(Cores.CIANO + "[ ENTER para continuar ]" + Cores.RESET);
        Entrada.lerLinha();
    }
    private void renderizarEpilogo(String titulo, String texto) {
        System.out.println(Cores.CIANO + "┌" + "─".repeat(LARGURA) + "┐" + Cores.RESET);
        imprimirLinha(titulo, Cores.AMARELO);
        System.out.println(Cores.CIANO + "├" + "─".repeat(LARGURA) + "┤" + Cores.RESET);

        for (String paragrafo : texto.split("\n")) {
            if (paragrafo.isBlank()) {
                imprimirLinha("", Cores.BRANCO);
                continue;
            }
            for (String linha : quebrarLinha(paragrafo, LARGURA - 4)) {
                imprimirLinha(linha, Cores.BRANCO);
            }
        }
        System.out.println(Cores.CIANO + "└" + "─".repeat(LARGURA) + "┘" + Cores.RESET);
        System.out.println();
    }



    private void mostrarPerfilAcademico(Protagonista p) {
        StringBuilder texto = new StringBuilder();

        if (p.temFlag("sistema_q1_estudar")) {
            texto.append("Você prefere entender um problema antes de começar. ")
                    .append("Isso ajuda a construir soluções fundamentadas, ")
                    .append("mas você também precisou aprender que nem sempre ")
                    .append("dá para estudar tudo antes de tentar.\n\n");
        } else if (p.temFlag("sistema_q1_mexer")) {
            texto.append("Você prefere começar a experimentar e aprender ")
                    .append("durante o processo. Isso ajuda a descobrir ")
                    .append("possibilidades, mas você também precisou aprender ")
                    .append("que algumas tentativas exigem preparação.\n\n");
        }

        if (p.temFlag("sistema_q2_sozinha")) {
            texto.append("Você tende a assumir as tarefas por conta própria. ")
                    .append("Isso pode dar autonomia, mas também significa ")
                    .append("que nem todo problema precisa ser resolvido ")
                    .append("sem ajuda.\n\n");
        } else if (p.temFlag("sistema_q2_grupo")) {
            texto.append("Você vê valor em trabalhar com outras pessoas. ")
                    .append("Aprendeu, porém, que dividir tarefas exige ")
                    .append("comunicação e responsabilidade, não apenas ")
                    .append("juntar todo mundo na mesma sala.\n\n");
        }

        if (p.temFlag("sistema_q3_visivel")) {
            texto.append("Você quer que seu trabalho seja reconhecido. ")
                    .append("Não há nada de errado nisso. O desafio é ")
                    .append("não deixar que o reconhecimento se torne ")
                    .append("a única medida do que você faz.\n\n");
        } else if (p.temFlag("sistema_q3_invisivel")) {
            texto.append("Você não considera a visibilidade o principal ")
                    .append("motivo para trabalhar. Ainda assim, aprendeu ")
                    .append("que apresentar suas contribuições é importante ")
                    .append("para que outras pessoas conheçam seu trabalho.\n\n");
        }

        if (p.temFlag("sistema_q4_planejar")) {
            texto.append("Você prefere saber o que vem pela frente. ")
                    .append("Planejar ajuda a se organizar, embora a ")
                    .append("universidade tenha mostrado que nem tudo ")
                    .append("obedece ao cronograma.\n\n");
        } else if (p.temFlag("sistema_q4_improvisar")) {
            texto.append("Você prefere decidir conforme as coisas ")
                    .append("acontecem. Adaptabilidade ajuda, mas você ")
                    .append("também descobriu que alguma organização ")
                    .append("pode evitar problemas desnecessários.\n\n");
        }

        if (!texto.isEmpty()) {
            renderizarEpilogo("O QUE O ÁGATA NÃO ENTENDEU", texto.toString().trim());
        }
    }


    private void mostrarEvolucaoPerfil(Protagonista p) {
        if (p.temFlag("sistema_q4_planejar")
                && p.temFlag("cap7_procrastinou")) {
            renderizarEpilogo("O QUE VOCÊ APRENDEU SOBRE SI",
                    "No começo do curso, você disse ao ÁGATA que precisava " +
                            "de um plano. No último semestre, adiou o TCC até não " +
                            "poder mais adiar.\n\n" +
                            "Você não deixou de precisar de organização. Descobriu " +
                            "que saber disso e conseguir fazer acontecer são coisas " +
                            "diferentes.");
        } else if (p.temFlag("sistema_q4_improvisar")
                && p.temFlag("cap7_tcc_planejou")) {
            renderizarEpilogo("O QUE VOCÊ APRENDEU SOBRE SI",
                    "No começo do curso, você disse ao ÁGATA que preferia " +
                            "descobrir as coisas na hora. No TCC, acabou dividindo " +
                            "o trabalho em etapas.\n\n" +
                            "Talvez você não tenha mudado de personalidade. " +
                            "Só descobriu que ter um plano também pode ser uma " +
                            "forma de abrir espaço para improvisar.");
        }

        if (p.temFlag("trilha_ic")) {
            if (p.temFlag("sistema_q3_visivel")
                    && p.temFlag("cap7_aceitou_autoria")) {
                renderizarEpilogo("O PREÇO DO RECONHECIMENTO",
                        "Você disse ao ÁGATA que queria ver seu nome " +
                                "publicado. Quando chegou a hora de defender " +
                                "o próprio trabalho, aceitou a decisão do orientador.\n\n" +
                                "Talvez tenha sido prudência. Talvez tenha sido " +
                                "medo do que aconteceria se você questionasse. " +
                                "Só você sabe quanto dessa escolha ainda incomoda.");
            } else if (p.temFlag("sistema_q3_invisivel")
                    && p.temFlag("cap7_questionou_autoria")) {
                renderizarEpilogo("O QUE MERECE SER RECONHECIDO",
                        "Você disse ao ÁGATA que ver seu nome publicado " +
                                "não mudaria o que faz. Ainda assim, quando chegou " +
                                "a hora de discutir a autoria do artigo, você " +
                                "questionou o orientador.\n\n" +
                                "Não era uma questão de fama. Era sobre reconhecer " +
                                "quem fez o trabalho.");
            }
        }

        if (p.temFlag("trilha_indep")) {
            if (p.temFlag("sistema_q1_estudar")
                    && p.temFlag("cap7_indep_aceitou")) {
                renderizarEpilogo("ANTES DE ASSINAR",
                        "Você disse ao ÁGATA que precisava entender " +
                                "um problema antes de agir. Mesmo assim, diante " +
                                "de um contrato que não compreendia direito, " +
                                "você aceitou.\n\n" +
                                "Uma resposta no formulário não garante que " +
                                "você consiga agir de acordo com ela quando " +
                                "o dinheiro e a urgência entram na conversa.");
            } else if (p.temFlag("sistema_q1_mexer")
                    && p.temFlag("cap7_indep_advogado")) {
                renderizarEpilogo("EXPERIMENTAR TAMBÉM É SABER PARAR",
                        "Você disse ao ÁGATA que aprendia mexendo. " +
                                "Diante de um contrato duvidoso, decidiu " +
                                "procurar orientação antes de assumir o risco.\n\n" +
                                "Você não deixou de experimentar. Aprendeu " +
                                "que algumas decisões merecem uma pausa.");
            }

            if (p.temFlag("cap7_indep_negociou")) {
                renderizarEpilogo("NEGOCIAR TAMBÉM É SABER DIZER NÃO",
                        "Você não aceitou o contrato no escuro. Conversou, identificou " +
                                "a cláusula que não fazia sentido e conseguiu uma versão revisada.\n\n" +
                                "O trabalho ainda vai exigir esforço; pelo menos, os limites " +
                                "ficaram mais claros antes de você começar.");
            }
        }
    }



    private DesempenhoAcademico avaliarDesempenhoAcademico(Protagonista p) {
        int score = p.getScore();
        int participacao = p.getParticipacao();

        if (score < 30 && participacao < 30) {
            return DesempenhoAcademico.DIFICULDADE;
        }

        if (score >= 70 && participacao >= 50) {
            return DesempenhoAcademico.DESTAQUE;
        }

        return DesempenhoAcademico.RECUPERACAO;
    }

    private BemEstarFinal avaliarBemEstar(Protagonista p) {
        int atributosBaixos = 0;

        if (p.getConfianca() <= 2) atributosBaixos++;
        if (p.getSanidade() <= 2) atributosBaixos++;
        if (p.getVidaSocial() <= 2) atributosBaixos++;

        if (atributosBaixos >= 2) {
            return BemEstarFinal.FRAGILIZADO;
        }

        if (p.getConfianca() >= 6 && p.getSanidade() >= 6
                && p.getVidaSocial() >= 6) {
            return BemEstarFinal.ESTAVEL;
        }

        return BemEstarFinal.OSCILANTE;
    }

    public void exibirEscolhas(List<Escolha> disponiveis) {
        System.out.println();
        System.out.println(Cores.CIANO + "╔" + "═".repeat(LARGURA) + "╗" + Cores.RESET);
        imprimirLinha("O QUE VOCÊ VAI FAZER?", Cores.VERDE);
        System.out.println(Cores.CIANO + "╠" + "═".repeat(LARGURA) + "╣" + Cores.RESET);

        for (int i = 0; i < disponiveis.size(); i++) {
            String escolha = "[" + (i + 1) + "] " + disponiveis.get(i).getTextoAlternativa();
            for (String linhaQuebrada : quebrarLinha(escolha, LARGURA - 4)) {
                imprimirLinha(linhaQuebrada, Cores.VERDE);
            }
        }

        System.out.println(Cores.CIANO + "╚" + "═".repeat(LARGURA) + "╝" + Cores.RESET);
        System.out.println("0 - Menu principal");
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println();
        System.out.println(Cores.AMARELO + mensagem + Cores.RESET);
    }

    private String corPorFalante(String personagemFalando) {
        if (personagemFalando.equalsIgnoreCase("Narrador")) return Cores.BRANCO;
        if (personagemFalando.equalsIgnoreCase("Sistema"))  return Cores.AMARELO;
        return Cores.MAGENTA;
    }

    private void imprimirLinha(String texto, String cor) {
        int espacos = Math.max(0, LARGURA - texto.length() - 2);
        System.out.println(Cores.CIANO + "║" + Cores.RESET
                + "  " + cor + texto + Cores.RESET + " ".repeat(espacos)
                + Cores.CIANO + "║" + Cores.RESET);
    }

    private List<String> quebrarLinha(String texto, int largura) {
        List<String> linhas = new ArrayList<>();
        String[] palavras = texto.split(" ");
        StringBuilder atual = new StringBuilder();

        for (String palavra : palavras) {
            if (atual.length() + palavra.length() + 1 > largura) {
                linhas.add(atual.toString());
                atual = new StringBuilder();
            }
            if (!atual.isEmpty()) atual.append(" ");
            atual.append(palavra);
        }
        if (!atual.isEmpty()) linhas.add(atual.toString());
        return linhas;
    }



}