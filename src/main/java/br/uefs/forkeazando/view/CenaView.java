package br.uefs.forkeazando.view;

import br.uefs.forkeazando.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CenaView {

    private static final int LARGURA = 64;
    private volatile boolean mostrarIndicadorPause = true;
    private volatile Protagonista protagonistaAtual;

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
        Entrada.permitirPausa(true);
        try {
            String corTexto = corPorFalante(cena.getPersonagemFalando());

            protagonistaAtual = p;
            System.out.println();
            imprimirLinha(cena.getCapitulo().toUpperCase(), Cores.BRANCO);
            imprimirLinha(cabecalhoStatus(p), Cores.CINZA);   // <- usar BRANCO se não tiver CINZA
            System.out.println(Cores.CIANO + "╠" + "═".repeat(LARGURA) + "╣" + Cores.RESET);

            if (!cena.getPersonagemFalando().equalsIgnoreCase("Narrador")) {
                imprimirLinha(cena.getPersonagemFalando().toUpperCase(), corTexto);
                imprimirLinha("", corTexto);
            }

            for (String paragrafo : cena.getTextoNarrativo().split("\n")) {
                Entrada.aguardarPause();
                if (paragrafo.equals("{{APROVACAO}}")) {
                    mostrarAprovacao();
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


        if (p.temFlag("trilha_ic")) {
            switch (desempenho) {
                case DIFICULDADE -> renderizarEpilogo("O QUE VOCÊ VIROU",
                        "Você trabalha com segurança de sistemas. O contrato " +
                                "é temporário, o salário não é grande coisa e a pesquisa " +
                                "ainda parece distante do que você imaginava.\n\n" +
                                "Você continua fazendo perguntas, mas nem sempre encontra " +
                                "alguém disposto a ouvir as respostas. Às vezes, se pergunta " +
                                "se era isso mesmo que queria.\n\n" +
                                "Você ainda não chegou onde esperava. Mas sabe investigar, " +
                                "reconhecer um erro e começar de novo. Isso ninguém pode " +
                                "tirar do que você aprendeu.");

                case RECUPERACAO -> renderizarEpilogo("O QUE VOCÊ VIROU",
                        "Você trabalha com segurança de sistemas. Empresa média, " +
                                "carteira assinada, salário ok. Você ainda pesquisa, só que " +
                                "agora é pago.\n\n" +
                                "Nem todo projeto dá certo. Nem toda hipótese se confirma. " +
                                "Você aprendeu que pesquisar não é provar que estava certa: " +
                                "é descobrir o que os resultados realmente dizem.\n\n" +
                                "Ainda tem muito que aprender. A diferença é que agora " +
                                "você não precisa saber tudo para continuar.");

                case DESTAQUE -> renderizarEpilogo("O QUE VOCÊ VIROU",
                        "Você trabalha com segurança de sistemas. Tem seu salário, " +
                                "seus projetos e uma pesquisa que já não depende apenas " +
                                "de alguém acreditar no seu potencial.\n\n" +
                                "Você aprendeu a defender suas ideias sem tratar cada " +
                                "discordância como uma derrota. Aprendeu também a reconhecer " +
                                "quando estava errada e a continuar investigando.\n\n" +
                                "No começo, você queria provar que era capaz. Hoje, " +
                                "está ocupada demais fazendo perguntas melhores.");
            }
        } else if (p.temFlag("trilha_dtec")) {
            switch (desempenho) {
                case DIFICULDADE -> renderizarEpilogo("O QUE VOCÊ VIROU",
                        "Você trabalha com sistemas embarcados. Os contratos " +
                                "são curtos, os prazos são apertados e tem dia em que " +
                                "uma placa que funcionava ontem resolve não funcionar hoje.\n\n" +
                                "Você ainda duvida da própria capacidade quando precisa " +
                                "pedir ajuda ou admitir que não encontrou o defeito.\n\n" +
                                "Mas continua tentando entender o que aconteceu, em vez " +
                                "de fingir que sabe. Por enquanto, é assim que você " +
                                "segue construindo seu espaço.");

                case RECUPERACAO -> renderizarEpilogo("O QUE VOCÊ VIROU",
                        "Você trabalha com sistemas embarcados. Empresa de hardware, " +
                                "Salvador, café ruim. Você mexe com placa todo dia. Foi a " +
                                "coisa que você escolheu no primeiro semestre, sem saber " +
                                "que tava escolhendo.\n\n" +
                                "Já queimou horas procurando um defeito que estava num fio " +
                                "solto. Já precisou pedir ajuda. Já voltou pra casa pensando " +
                                "no que poderia ter feito diferente.\n\n" +
                                "Você ainda não sabe tudo. Mas agora sabe que consegue " +
                                "aprender o que falta.");

                case DESTAQUE -> renderizarEpilogo("O QUE VOCÊ VIROU",
                        "Você trabalha com sistemas embarcados. Empresa de hardware, " +
                                "Salvador, café ruim. Tem projeto que leva semanas e defeito " +
                                "que só aparece quando tudo parece pronto.\n\n" +
                                "Você já consegue olhar para um problema, separar as " +
                                "possibilidades e descobrir por onde começar. E, quando " +
                                "não consegue resolver sozinha, sabe que pedir ajuda " +
                                "também faz parte do trabalho.\n\n" +
                                "No primeiro semestre, você escolheu essa área sem saber " +
                                "direito o que estava escolhendo. Agora sabe por que ficou.");
            }
        } else if (p.temFlag("trilha_indep")) {
            switch (desempenho) {
                case DIFICULDADE -> renderizarEpilogo("O QUE VOCÊ VIROU",
                        "Você não tem emprego fixo. Tem clientes. Alguns pagam; " +
                                "outros desaparecem antes de acertar a conta.\n\n" +
                                "Você sabe fazer o trabalho. O difícil é conseguir viver " +
                                "dele. Tem orçamento aberto na tela, mensagens sem resposta " +
                                "e uma conta vencendo amanhã.\n\n" +
                                "Você chegou até aqui, mas ainda não chegou à estabilidade " +
                                "que queria. Amanhã vai precisar tentar de novo.");

                case RECUPERACAO -> renderizarEpilogo("O QUE VOCÊ VIROU",
                        "Você não tem emprego fixo. Tem clientes, alguns bons " +
                                "e outros que dão trabalho demais pelo que pagam.\n\n" +
                                "Já aprendeu a cobrar antes e a desconfiar de promessa " +
                                "vaga. Nem sempre consegue colocar isso em prática.\n\n" +
                                "A conta do mês quase fecha. Você ainda está aprendendo " +
                                "a construir uma carreira sem deixar que a instabilidade " +
                                "decida tudo por você.");

                case DESTAQUE -> renderizarEpilogo("O QUE VOCÊ VIROU",
                        "Você não tem emprego. Tem clientes. Uns pagam bem. " +
                                "Uns somem. Você aprendeu a cobrar antes e a dizer não.\n\n" +
                                "Hoje, escolhe melhor os projetos que aceita. Ainda " +
                                "existem meses ruins, mas eles já não decidem tudo " +
                                "por você.\n\n" +
                                "Não foi a carreira mais segura. Foi uma carreira " +
                                "que você construiu. E agora sabe melhor como sustentá-la.");
            }
        }
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

        System.out.println(Cores.CIANO + "[ Pressione ENTER para encerrar. ]" + Cores.RESET);
        Entrada.lerLinha();
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