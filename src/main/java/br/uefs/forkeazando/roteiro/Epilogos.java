package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.PersonagemSecundario;
import br.uefs.forkeazando.model.Protagonista;

import java.util.ArrayList;
import java.util.List;

/**
 * Conteúdo dos epílogos. Decide QUAIS blocos o jogador vê ao terminar.
 * Não imprime: a CenaView só recebe a lista e desenha.
 */
public final class Epilogos {

    public record Epilogo(String titulo, String texto) {}

    public enum DesempenhoAcademico { DIFICULDADE, RECUPERACAO, DESTAQUE }
    public enum BemEstarFinal { FRAGILIZADO, OSCILANTE, ESTAVEL }

    private Epilogos() {}

    public static List<Epilogo> de(Protagonista p) {
        List<Epilogo> out = new ArrayList<>();
        DesempenhoAcademico desempenho = avaliarDesempenhoAcademico(p);
        BemEstarFinal bemEstar = avaliarBemEstar(p);

        retrospectivaTrilha(out, p, desempenho);
        desempenhoAcademico(out, desempenho);
        ecosDosCapitulos(out, p);
        perfilAcademico(out, p);
        evolucaoDoPerfil(out, p, bemEstar);
        personagens(out, p, bemEstar);
        quemVoceVirou(out, bemEstar, p);
        zeDoModulo8(out);
        umAnoDepois(out, p);
        formatura(out, p);
        oQueFica(out);
        ecoDaRespostaInicial(out, p);
        futuroEscolhido(out, p);

        return out;
    }

    // ═══ TRILHA ═══
    private static void retrospectivaTrilha(List<Epilogo> out, Protagonista p, DesempenhoAcademico d) {
        if (p.temFlag("trilha_ic")) {
            String t = switch (d) {
                case DIFICULDADE -> "A iniciação científica foi sua primeira aproximação com perguntas sem resposta pronta. "
                        + "Nem sempre você teve tempo, orientação ou estabilidade para avançar como queria. "
                        + "Ainda assim, aprendeu a separar uma hipótese de uma certeza e a reconhecer o que não sabe.";
                case RECUPERACAO -> "Na iniciação científica, você descobriu que pesquisar não é provar que estava certa; "
                        + "é investigar o que os resultados sustentam. Aprendeu a revisar uma hipótese e a pedir que outra "
                        + "pessoa olhe para o que você não percebeu.";
                case DESTAQUE -> "A iniciação científica ajudou você a formular perguntas melhores e defender uma ideia "
                        + "sem transformar toda discordância em derrota. Você também aprendeu a reconhecer quando os dados "
                        + "obrigam a mudar de rumo.";
            };
            out.add(new Epilogo("O QUE A PESQUISA DEIXOU", t));
        } else if (p.temFlag("trilha_dtec")) {
            String t = switch (d) {
                case DIFICULDADE -> "A experiência de monitoria e ensino colocou você diante de pessoas que não aprendiam "
                        + "no mesmo ritmo. Nem sempre você soube explicar; algumas vezes precisou admitir que também não sabia. "
                        + "Isso faz parte de ensinar e de construir coisas.";
                case RECUPERACAO -> "A trilha prática ensinou você a decompor problemas, testar possibilidades e explicar "
                        + "o raciocínio sem esconder as tentativas que falharam. Você aprendeu que pedir outra opinião é parte do processo.";
                case DESTAQUE -> "Na trilha de ensino e prática, você aprendeu que saber fazer e conseguir explicar são "
                        + "habilidades diferentes. As duas podem ajudar uma equipe a sair de um problema que parecia travado.";
            };
            out.add(new Epilogo("O QUE A PRÁTICA DEIXOU", t));
        } else if (p.temFlag("trilha_indep") || p.temFlag("trilha_indefinida")) {
            String t = switch (d) {
                case DIFICULDADE -> "O caminho independente colocou você diante de orçamento, prazo e cliente antes de haver "
                        + "garantia de estabilidade. Você aprendeu trabalho técnico, mas descobriu que sustentar o trabalho também "
                        + "exige negociação, organização e uma rede de apoio.";
                case RECUPERACAO -> "Projetos independentes ensinaram você a traduzir uma necessidade em escopo e a perceber "
                        + "quando uma promessa está vaga. Nem sempre foi fácil defender seus limites, mas agora você reconhece "
                        + "que eles precisam existir.";
                case DESTAQUE -> "A trilha independente fortaleceu sua autonomia e também ensinou que autonomia não é fazer "
                        + "tudo só. Você aprendeu a negociar, procurar parcerias e recusar um projeto que não se sustenta.";
            };
            out.add(new Epilogo("O QUE A AUTONOMIA DEIXOU", t));
        }
    }

    // ═══ DESEMPENHO (novo — DESTAQUE / DIFICULDADE, RECUPERACAO silencia) ═══
    private static void desempenhoAcademico(List<Epilogo> out, DesempenhoAcademico d) {
        switch (d) {
            case DESTAQUE -> out.add(new Epilogo("O QUE A FACULDADE VIU EM VOCÊ",
                    "Seu histórico tem números que impressionam. "
                            + "Bolsas, projetos, menções. Mas o que ninguém vê "
                            + "nos números é o que você fez com eles.\n\n"
                            + "Você aprendeu a transformar esforço em resultado. "
                            + "Isso serve pra próxima etapa. Só não confunda resultado "
                            + "com valor — em alguns dias da semana você vai precisar "
                            + "lembrar da diferença."));
            case DIFICULDADE -> out.add(new Epilogo("O QUE A FACULDADE NÃO VIU",
                    "Seu histórico tem números que não refletem o que você "
                            + "aprendeu. Reprovações, recuperações, semestres que quase "
                            + "não fecharam.\n\n"
                            + "O sistema mede presença e nota. Não mede a vez que você "
                            + "levantou de manhã mesmo sem saber se ia dar conta. "
                            + "Não mede o esforço que ninguém viu.\n\n"
                            + "Você terminou. Isso também é um dado. O sistema não "
                            + "registra — mas você sabe."));
            case RECUPERACAO -> { /* caso comum: silêncio */ }
        }
    }

    // ═══ ECOS (com as 12 amarrações novas) ═══
    private static void ecosDosCapitulos(List<Epilogo> out, Protagonista p) {
        StringBuilder t = new StringBuilder();

        // Cap 1
        if (p.temFlag("guardou_pra_si")) {
            t.append("Na primeira madrugada, você não acordou sua mãe. Guardou a notícia "
                    + "pra si. Não foi frieza — foi cuidado. Mas o hábito de não incomodar "
                    + "também apareceu em outros dias.\n\n");
        }
        if (p.temFlag("hab_olho_edital")) {
            t.append("Na primeira semana, você aprendeu a guardar comprovantes e conferir instruções. "
                    + "Esse cuidado não resolveu toda a burocracia, mas deu a você um ponto de apoio.\n\n");
        } else if (p.temFlag("pediu_ajuda_grupo")) {
            t.append("Na primeira semana, você procurou orientação em vez de fingir que já conhecia o campus. "
                    + "Pedir informação virou uma ferramenta, não uma confissão de incapacidade.\n\n");
        } else if (p.temFlag("aceitou_caos")) {
            t.append("Na primeira semana, você resolveu seguir mesmo sem entender tudo. A adaptação ajudou, "
                    + "embora depois tenha sido necessário aprender a se organizar.\n\n");
        }
        if (p.temFlag("item_cartao_ru")) {
            t.append("Você tirou o cartão do RU no primeiro dia. R$ 2,00 virou rotina — e você "
                    + "aprendeu a hora em que a fila é menor.\n\n");
        }
        if (p.temFlag("cap1_mediu_circuito") || p.temFlag("cap1_mediu_fonte")) {
            t.append("Na oficina, você mediu antes de trocar componente. Nem sempre foi assim "
                    + "— mas naquele dia foi.\n\n");
        }
        if (p.temFlag("cap1_conversou_modulo8") || p.temFlag("cap1_reconhecido_modulo8")) {
            t.append("No primeiro mês, você foi ao Módulo 8 uma vez. Não virou amizade, mas "
                    + "você viu que existia vida fora do campus.\n\n");
        }

        // Contexto social
        if (p.temFlag("origem_apertada")) {
            t.append("Você também aprendeu que tempo e dinheiro não são variáveis abstratas. Ônibus, alimentação, "
                    + "bolsa e trabalho mudaram o conjunto de escolhas que estava realmente ao seu alcance.\n\n");
        }

        // Cap 2
        if (p.temFlag("ignorou_email")) {
            t.append("Houve um aviso que você deixou passar. A universidade continuou funcionando como se todo mundo "
                    + "tivesse recebido, lido e entendido a mensagem.\n\n");
        } else if (p.temFlag("item_vaga_estagio")) {
            t.append("Você investigou uma oportunidade de estágio antes de saber se ela caberia na sua rotina. "
                    + "Descobriu que oportunidade também tem custo, prazo e condição de acesso.\n\n");
        } else if (p.temFlag("ras_curiosa") || p.temFlag("tisp_interesse") || p.temFlag("wie_interesse")) {
            t.append("Você experimentou espaços e comunidades antes de saber exatamente onde cabia. Nem todo contato "
                    + "virou projeto, mas alguns ajudaram a ampliar o que você imaginava possível.\n\n");
        }
        if (p.temFlag("cap2_so_assistiu")) {
            t.append("Num dos primeiros processos seletivos, você só assistiu. "
                    + "Não se inscreveu, não perguntou, não tentou. A escolha não te "
                    + "define. Mas você lembra dela.\n\n");
        }

        // Cap 3
        if (p.temFlag("cap3_colou_de_fato")) {
            t.append("Em uma avaliação, você cruzou uma linha para conseguir entregar. O resultado não desapareceu "
                    + "da sua história só porque o semestre acabou.\n\n");
        } else if (p.temFlag("hab_palavra_honesta")) {
            t.append("Quando seria mais fácil esconder uma dúvida, você decidiu falar a verdade. Isso não resolveu "
                    + "tudo de imediato, mas tornou possível decidir com base no que realmente estava acontecendo.\n\n");
        }

        // Cap 4
        if (p.temFlag("cap4_leu_editais")) {
            t.append("Antes de escolher uma trilha, você leu os editais e tentou entender os critérios. "
                    + "Nem toda regra estava escrita de um jeito útil, mas você aprendeu a procurar informação "
                    + "antes de assumir um compromisso.\n\n");
        } else if (p.temFlag("cap4_perguntou_alternativa") || p.temFlag("cap4_procurou_caio")) {
            t.append("Na hora de escolher uma trilha, você procurou outra pessoa e perguntou o que não entendia. "
                    + "O conselho ajudou, mas a decisão e suas consequências continuaram sendo suas.\n\n");
        }

        // Cap 5 — greve
        if (p.temFlag("cap5b_foi_assembleia")) {
            t.append("Você foi na assembleia do DCE. Aprendeu que algumas decisões "
                    + "passam longe do seu curso, mas chegam no seu prato.\n\n");
        } else if (p.temFlag("cap5b_furou_greve")) {
            t.append("Durante a greve, você continuou dando monitoria por fora. Fez o que achou certo. "
                    + "Não foi sem custo.\n\n");
        } else if (p.temFlag("cap5b_neutra")) {
            t.append("Na greve, você foi pra casa e esperou. Não é covardia — é uma "
                    + "resposta possível quando a decisão não está nas suas mãos.\n\n");
        }
        if (p.temFlag("hab_sobreviveu_greve")) {
            t.append(p.temFlag("cap5b_dividiu_comida")
                    ? "Na greve, você atravessou dias difíceis dividindo o que tinha. A lembrança não transforma a falta "
                    + "de comida e de renda em uma aventura; mostra quem tentou se apoiar quando a estrutura falhou.\n\n"
                    : "A greve mudou sua rotina e o calendário. Você continuou estudando, mas isso não significa que os "
                    + "65 dias tenham sido simples.\n\n");
        }
        if (p.temFlag("bia_sabe_greve")) {
            t.append("A Bia te avisou antes de a greve chegar. Você anotou o aviso. "
                    + "Ela sempre fala como se a conversa tivesse começado antes de você chegar.\n\n");
        }

        // Cap 5A
        if (p.temFlag("cap5a_liderou_artigo")) {
            t.append("No grupo de pesquisa, você assumiu a liderança de parte do artigo. Isso deu experiência e "
                    + "visibilidade, mas também tornou mais concreto o problema de dividir tarefas e reconhecer autoria.\n\n");
        } else if (p.temFlag("cap5a_perguntou_revisao")) {
            t.append("Na pesquisa, você pediu revisão em vez de tratar o primeiro rascunho como pronto.\n\n");
        } else if (p.temFlag("cap5a_saiu_grupo")) {
            t.append("Você saiu do grupo de pesquisa. A saída não apaga o que experimentou.\n\n");
        }
        if (p.temFlag("cap5a_registrou_experimento")) {
            t.append("Na IC, você registrou método e condições do experimento antes de redigir. "
                    + "Aprendizado que você leva pra qualquer lugar onde for preciso defender o que fez.\n\n");
        }

        // Cap 5B
        if (p.temFlag("cap5b_preparou_exercicio")) {
            t.append("Na monitoria, você montou um exercício em etapas e pediu que os alunos explicassem cada passo.\n\n");
        }
        if (p.temFlag("dtec_greve_incerta")) {
            t.append("No fim da greve, o Meira te prometeu 'talvez' uma vaga no próximo semestre. "
                    + "Você aprendeu a desconfiar de 'talvez'.\n\n");
        }

        // Cap 5C
        if (p.temFlag("cap5c_pesquisou_preco")) {
            t.append("No primeiro freela, você pesquisou preços antes de apresentar uma proposta.\n\n");
        } else if (p.temFlag("cap5c_cobrou")) {
            t.append("No primeiro freela, você cobrou pelo trabalho sem ter certeza de como a outra pessoa reagiria.\n\n");
        } else if (p.temFlag("cap5c_pediu_mais")) {
            t.append("No primeiro freela, você pediu mais pelo trabalho.\n\n");
        } else if (p.temFlag("cap5c_deixou_rolar")) {
            t.append("No primeiro freela, você deixou o valor em aberto e viu como a falta de um acordo claro cria "
                    + "dúvidas depois.\n\n");
        }
        if (p.temFlag("cap5c_validou_entrega")) {
            t.append("Você registrou o que foi entregue e conferiu com o cliente antes de cobrar. "
                    + "Trabalho técnico também se defende por escrito.\n\n");
        }
        if (p.temFlag("cap5c_cliente_pagou")) {
            t.append("O dinheiro do freela caiu. Você salvou os registros e decidiu que o próximo "
                    + "contrato seria combinado antes de abrir o editor.\n\n");
        } else if (p.temFlag("cap5c_cliente_nao_pagou")) {
            t.append("O dinheiro do freela não caiu. A raiva passou. A lição ficou.\n\n");
        }

        // Cap 6
        if (p.temFlag("cap6_trancou")) {
            t.append("Você trancou um semestre. O calendário mudou, e a pausa teve um custo real, mas não apagou "
                    + "o que você já tinha construído.\n\n");
        } else if (p.temFlag("cap6_continuou")) {
            t.append("Você continuou no semestre durante a crise. Foi a decisão que fez sentido naquele momento.\n\n");
        }
        if (p.temFlag("cap6_pediu_ajuda")) {
            t.append("Quando pediu ajuda, você deixou que outra pessoa soubesse o que estava acontecendo.\n\n");
        }
        if (p.temFlag("cap6_ic_recomeco") || p.temFlag("cap6_dtec_recomeco") || p.temFlag("cap6_indep_recomeco")) {
            t.append("Depois da crise, você voltou. Não como se nada tivesse acontecido — "
                    + "com a certeza de que a volta também era uma escolha.\n\n");
        } else if (p.temFlag("cap6_dtec_tempo") || p.temFlag("cap6_indep_recusou")) {
            t.append("Depois da crise, você pediu um tempo. Nem tudo precisava ser "
                    + "resolvido naquele semestre.\n\n");
        }

        // Cap 7
        if (p.temFlag("cap7_dtec_deu_nota")) {
            t.append("Na monitoria, você precisou avaliar o trabalho de outra pessoa.\n\n");
        } else if (p.temFlag("cap7_dtec_ultima_aula")) {
            t.append("Na última aula como monitora, você percebeu que ensinar também é reconhecer o que não cabe numa "
                    + "explicação só.\n\n");
        }
        if (p.temFlag("cap7_tcc_improvisou")) {
            t.append("Você terminou o TCC improvisando perto do prazo. O trabalho foi entregue, mas você lembra do custo.\n\n");
        } else if (p.temFlag("cap7_tcc_planejou")) {
            t.append("Você dividiu o TCC em etapas e acompanhou o que faltava.\n\n");
        }

        // Cap 8
        if (p.temFlag("item_copo_caio")) {
            t.append("O copo térmico amassado ficou com você.\n\n");
        }
        if (p.temFlag("cap8_saiu_antes")) {
            t.append("Você saiu da formatura antes do fim. Sem foto oficial, sem discurso do paraninfo.\n\n");
        }

        if (!t.isEmpty()) {
            out.add(new Epilogo("AS ESCOLHAS QUE CONTINUARAM COM VOCÊ", t.toString().trim()));
        }
    }

    // ═══ PERFIL ACADÊMICO (sistema_q*) ═══
    private static void perfilAcademico(List<Epilogo> out, Protagonista p) {
        StringBuilder t = new StringBuilder();
        if (p.temFlag("sistema_chutou_ambiguo")) {
            out.add(new Epilogo("O QUE O ÁGATA NÃO ENTENDEU",
                    "O sistema não conseguiu te classificar. "
                            + "Empatou entre dois perfis e desistiu.\n\n"
                            + "Vinte segundos, quatro perguntas, e ele desistiu. "
                            + "Você ainda tá tentando descobrir."));
        } else if (p.temFlag("sistema_chutou_indefinido")) {
            out.add(new Epilogo("O QUE O ÁGATA NÃO ENTENDEU",
                    "O sistema não te encaixou em categoria nenhuma.\n\n"
                            + "Talvez você tenha pulado a tela. Talvez ele tenha travado. "
                            + "Não importa: ele desistiu antes de você."));
        }
        if (p.temFlag("sistema_q1_estudar")) {
            t.append("Você prefere entender um problema antes de começar. Isso ajuda a construir soluções "
                    + "fundamentadas, mas você também precisou aprender que nem sempre dá para estudar tudo antes de tentar.\n\n");
        } else if (p.temFlag("sistema_q1_mexer")) {
            t.append("Você prefere começar a experimentar e aprender durante o processo.\n\n");
        }
        if (p.temFlag("sistema_q2_sozinha")) {
            t.append("Você tende a assumir as tarefas por conta própria. Isso pode dar autonomia, mas também significa "
                    + "que nem todo problema precisa ser resolvido sem ajuda.\n\n");
        } else if (p.temFlag("sistema_q2_grupo")) {
            t.append("Você vê valor em trabalhar com outras pessoas.\n\n");
        }
        if (p.temFlag("sistema_q3_visivel")) {
            t.append("Você quer que seu trabalho seja reconhecido. Não há nada de errado nisso.\n\n");
        } else if (p.temFlag("sistema_q3_invisivel")) {
            t.append("Você não considera a visibilidade o principal motivo para trabalhar.\n\n");
        }
        if (p.temFlag("sistema_q4_planejar")) {
            t.append("Você prefere saber o que vem pela frente.\n\n");
        } else if (p.temFlag("sistema_q4_improvisar")) {
            t.append("Você prefere decidir conforme as coisas acontecem.\n\n");
        }
        if (!t.isEmpty()) out.add(new Epilogo("O QUE O ÁGATA NÃO ENTENDEU", t.toString().trim()));
    }

    // ═══ EVOLUÇÃO DO PERFIL (com sistema_aceito novo) ═══
    private static void evolucaoDoPerfil(List<Epilogo> out, Protagonista p, BemEstarFinal bemEstar) {
        // Aceite do veredito do Cap 4 (novo)
        if (p.temFlag("sistema_aceito") && p.temFlag("sistema_q4_planejar")) {
            out.add(new Epilogo("O QUE VOCÊ APRENDEU SOBRE SI",
                    "No Cap 4, o ÁGATA te classificou e você aceitou. "
                            + "Não questionou, não recorreu, seguiu em frente.\n\n"
                            + "No fim, você não sabe se foi sabedoria ou cansaço. "
                            + "Sabe que o veredito não definiu seu semestre — "
                            + "as escolhas que vieram depois definiram."));
        }

        // Planejar vs procrastinar
        if (p.temFlag("sistema_q4_planejar") && p.temFlag("cap7_procrastinou")) {
            out.add(new Epilogo("O QUE VOCÊ APRENDEU SOBRE SI",
                    "No começo do curso, você disse ao ÁGATA que precisava de um plano. "
                            + "No último semestre, adiou o TCC até não poder mais adiar.\n\n"
                            + "Você não deixou de precisar de organização. Descobriu que saber disso "
                            + "e conseguir fazer acontecer são coisas diferentes."));
        } else if (p.temFlag("sistema_q4_improvisar") && p.temFlag("cap7_tcc_planejou")) {
            out.add(new Epilogo("O QUE VOCÊ APRENDEU SOBRE SI",
                    "No começo do curso, você disse ao ÁGATA que preferia descobrir as coisas na hora. "
                            + "No TCC, acabou dividindo o trabalho em etapas.\n\n"
                            + "Talvez você não tenha mudado de personalidade. Só descobriu que ter um plano "
                            + "também pode ser uma forma de abrir espaço para improvisar."));
        }

        // Reforços por trilha
        if (p.temFlag("trilha_ic")) {
            if (p.temFlag("sistema_q3_visivel") && p.temFlag("cap7_aceitou_autoria")) {
                out.add(new Epilogo("O PREÇO DO RECONHECIMENTO",
                        "Você disse ao ÁGATA que queria ver seu nome publicado. Quando chegou a hora "
                                + "de defender o próprio trabalho, aceitou a decisão do orientador.\n\n"
                                + "Talvez tenha sido prudência. Talvez tenha sido medo. Só você sabe quanto "
                                + "dessa escolha ainda incomoda."));
            } else if (p.temFlag("sistema_q3_invisivel") && p.temFlag("cap7_questionou_autoria")) {
                out.add(new Epilogo("O QUE MERECE SER RECONHECIDO",
                        "Você disse ao ÁGATA que ver seu nome publicado não mudaria o que faz. "
                                + "Ainda assim, quando chegou a hora de discutir a autoria, você questionou o orientador.\n\n"
                                + "Não era uma questão de fama. Era sobre reconhecer quem fez o trabalho."));
            }
        }
        if (p.temFlag("trilha_indep")) {
            if (p.temFlag("sistema_q1_estudar") && p.temFlag("cap7_indep_aceitou")) {
                out.add(new Epilogo("ANTES DE ASSINAR",
                        "Você disse ao ÁGATA que precisava entender um problema antes de agir. "
                                + "Mesmo assim, diante de um contrato que não compreendia direito, você aceitou.\n\n"
                                + "Uma resposta no formulário não garante que você consiga agir de acordo com ela "
                                + "quando o dinheiro e a urgência entram na conversa."));
            } else if (p.temFlag("sistema_q1_mexer") && p.temFlag("cap7_indep_advogado")) {
                out.add(new Epilogo("EXPERIMENTAR TAMBÉM É SABER PARAR",
                        "Você disse ao ÁGATA que aprendia mexendo. Diante de um contrato duvidoso, "
                                + "decidiu procurar orientação antes de assumir o risco.\n\n"
                                + "Você não deixou de experimentar. Aprendeu que algumas decisões merecem uma pausa."));
            }
            if (p.temFlag("cap7_indep_negociou")) {
                out.add(new Epilogo("NEGOCIAR TAMBÉM É SABER DIZER NÃO",
                        "Você não aceitou o contrato no escuro. Conversou, identificou a cláusula "
                                + "que não fazia sentido e conseguiu uma versão revisada."));
            }
        }
    }

    // ═══ PERSONAGENS (Caio, Orientador, Dandara, Bia com bem-estar) ═══
    private static void personagens(List<Epilogo> out, Protagonista p, BemEstarFinal bemEstar) {
        if (p.temFlag("mentoria_caio")) {
            String t;
            if (p.temFlag("caio_revelacao")) {
                if (bemEstar == BemEstarFinal.FRAGILIZADO) {
                    t = "Caio terminou o curso. Você viu a foto no LinkedIn. "
                            + "Ele não te mandou mensagem. Você também não.\n\n"
                            + "A frase que vocês trocaram no primeiro semestre continua verdadeira. "
                            + "Só não tem mais ninguém segurando.";
                } else {
                    t = "Cáio tá terminando o curso. Ainda atrasado, mas terminando. "
                            + "Ele te mandou mensagem semana passada. Disse que finalmente entendeu "
                            + "o que você quis dizer no primeiro semestre.\n\n"
                            + "Você respondeu que também demorou pra entender algumas coisas. "
                            + "Ele mandou um áudio de três minutos. Você ouviu até o fim.";
                }
            } else {
                t = "Cáio não se formou. Trabalha numa empresa de hardware em Salvador, "
                        + "ganha menos do que merecia. O copo amassado continua com ele. Ou com você.\n\n"
                        + "Vocês não conseguiram resolver tudo um pelo outro. Mas, em algum momento, "
                        + "isso deixou de ser o que importava.";
            }
            out.add(new Epilogo("CÁIO", t));
        }

        if (p.temFlag("cap7_questionou_autoria")) {
            out.add(new Epilogo("ORIENTADOR",
                    "O artigo saiu. Com o nome dele primeiro. Você foi segunda autora. "
                            + "Ele nunca mais te chamou pra outro projeto.\n\n"
                            + "Você perdeu uma oportunidade. Não perdeu o direito de reconhecer o próprio trabalho."));
        } else if (p.temFlag("cap7_aceitou_autoria")) {
            out.add(new Epilogo("ORIENTADOR",
                    "O artigo saiu. Com o nome dele primeiro. Ele te chamou pra mais dois projetos. "
                            + "Você aceitou os dois.\n\n"
                            + "O currículo cresceu. A dúvida também."));
        }

        if (Protagonista.categorizarRelacionamento(p.getRelacionamento(PersonagemSecundario.DANDARA))
                == Protagonista.NivelRelacionamento.RIVAL) {
            out.add(new Epilogo("DANDARA",
                    "Dandara passou num concurso. Postou no LinkedIn, ganhou 200 curtidas, "
                            + "e depois sumiu por três meses. Você acha que ela tá bem.\n\n"
                            + "Durante muito tempo, o sucesso dela parecia diminuir o seu. Agora você "
                            + "consegue olhar pra própria trajetória sem transformar a dela numa disputa."));
        } else if (bemEstar == BemEstarFinal.ESTAVEL
                && p.getRelacionamento(PersonagemSecundario.DANDARA) >= 4f) {
            out.add(new Epilogo("DANDARA",
                    "Dandara passou num concurso. Postou no LinkedIn. Você curtiu.\n\n"
                            + "Ela te chamou pra comemorar e você foi. Um bar. Sem motivo acadêmico. "
                            + "Sem falar de nota, de bolsa, de CR.\n\n"
                            + "Vocês passaram anos disputando coisas que o sistema distribuía mal. "
                            + "Naquela noite, por algumas horas, isso não importou."));
        }

        if (p.temFlag("cap3_colou_de_fato") || p.temFlag("bia_ainda_vende")) {
            String t = bemEstar == BemEstarFinal.FRAGILIZADO
                    ? "Bia ainda vende resposta antiga. Só que agora é num site próprio. "
                    + "Ela te convidou pra ser sócia. Você não respondeu.\n\n"
                    + "Algumas portas se fecham do lado de dentro."
                    : "Bia ainda vende resposta antiga. Só que agora é num site próprio. "
                    + "Ela te convidou pra ser sócia. Você ainda tá pensando.\n\n"
                    + "Dessa vez, você sabe que não precisa responder só porque alguém espera uma resposta sua.";
            out.add(new Epilogo("BIA", t));
        } else if (p.temFlag("hab_palavra_honesta")) {
            out.add(new Epilogo("BIA",
                    "Bia parou de vender resposta. Ninguém sabe por quê. Você acha que foi você. "
                            + "Ela tá fazendo mestrado agora.\n\n"
                            + "Você nunca soube se aquela conversa mudou alguma coisa. "
                            + "Nem toda consequência chega com confirmação."));
        } else {
            out.add(new Epilogo("BIA",
                    "Você não sabe o que aconteceu com a Bia. Ela sumiu depois do terceiro semestre.\n\n"
                            + "Algumas histórias continuam sem resposta. Você aprendeu a conviver com isso, "
                            + "mesmo sem gostar."));
        }
    }

    // ═══ QUEM VOCÊ VIROU (substitui o antigo por bemEstar) ═══
    private static void quemVoceVirou(List<Epilogo> out, BemEstarFinal bemEstar, Protagonista p) {
        String t;
        switch (bemEstar) {
            case ESTAVEL -> t = "Você sai da entrevista e alguém te liga. Não é a empresa. É um amigo. "
                    + "Você atende. Você ri. Você não lembra a última vez que riu no telefone.\n\n"
                    + "Por alguns minutos, você não pensa em currículo, salário ou no que deveria ter feito diferente.";
            case FRAGILIZADO -> {
                if (p.temFlag("cap6_crise_profunda")) {
                    t = "Você sai da entrevista e não tem ninguém pra ligar.\n\n"
                            + "Não é a primeira vez que a solidão chega desse jeito. No quarto, na semana "
                            + "em que você não conseguiu sair, já era assim. A diferença é que agora você "
                            + "sabe que consegue atravessar — mesmo sem saber direito como.";
                } else if (p.temFlag("cap6_afundou")) {
                    t = "Você sai da entrevista e não tem ninguém pra ligar.\n\n"
                            + "Você já tinha passado por isso antes. Não precisava ser de novo.\n\n"
                            + "Chegar até aqui não foi pouca coisa. Amanhã ainda existe.";
                } else {
                    t = "Você sai da entrevista e não tem ninguém pra ligar.\n\n"
                            + "A solidão não desaparece só porque você chegou até aqui. Mas chegar "
                            + "até aqui também não foi pouca coisa.";
                }
            }
            default -> t = "Você sai da entrevista. O celular tem poucas mensagens. "
                    + "Você responde duas, deixa três. Vai pra casa.\n\n"
                    + "Não é o desfecho que você imaginou cinco anos atrás. Também não é o pior.";
        }
        out.add(new Epilogo("QUEM VOCÊ VIROU", t));
    }

    // ═══ ZÉ ═══
    private static void zeDoModulo8(List<Epilogo> out) {
        out.add(new Epilogo("ZÉ DO MÓDULO 8",
                "O Zé continua lá. O bar tá maior agora. Ele te reconheceu quando você voltou "
                        + "no semestre passado e não cobrou a cerveja.\n\n"
                        + "\"Tu já gastou demais nessa faculdade, minha filha. Essa fica por conta da casa.\""));
    }

    // ═══ UM ANO DEPOIS ═══
    private static void umAnoDepois(List<Epilogo> out, Protagonista p) {
        String t;
        if (p.temFlag("cap8_resposta_verdadeira")) {
            t = "Você falou da greve. Da Dandara. Das vezes em que quase desistiu e continuou mesmo assim.\n\n"
                    + "Você não contou uma história perfeita. Contou a sua.";
        } else if (p.temFlag("cap8_resposta_confissao")) {
            t = "Você escolheu contar o que ninguém pergunta em entrevista.\n\n"
                    + "Não foi uma confissão estratégica. Foi a única resposta que reconhecia a pessoa que fez o trabalho "
                    + "— inclusive as partes que você não queria mostrar.";
        } else if (p.temFlag("cap8_resposta_autoria")) {
            t = "Você terminou a entrevista falando de um artigo que quase não teve seu nome.\n\n"
                    + "Cinco anos antes, você não sabia que ia precisar defender isso. Aprendeu.";
        } else if (p.temFlag("cap8_resposta_honesta")) {
            t = "Você disse que ainda tava descobrindo.\n\n"
                    + "A entrevista não resolveu o futuro. O diploma também não. "
                    + "Mas, pela primeira vez, você não precisou fingir que sabia.";
        } else if (p.temFlag("cap8_resposta_conviccao")) {
            t = "Você disse que escolheu aquela área. E era verdade.\n\n"
                    + "Não porque nunca teve dúvidas, mas porque teve dúvidas e continuou escolhendo.";
        } else {
            t = "Você disse que precisava trabalhar. Não era uma resposta bonita. Era a verdade.\n\n"
                    + "Nem toda escolha começa com um sonho. Isso não torna a sua conquista menor.";
        }
        out.add(new Epilogo("UM ANO DEPOIS", t));
    }

    // ═══ FORMATURA ═══
    private static void formatura(List<Epilogo> out, Protagonista p) {
        if (p.temFlag("cap8_sem_cerimonia")) {
            out.add(new Epilogo("A FORMATURA",
                    "Você não teve a foto oficial com a beca, nem o nome anunciado no auditório.\n\n"
                            + "Mas você concluiu o curso. A cerimônia era uma forma de marcar esse momento. "
                            + "Não era o que tornava os anos de esforço reais. Em casa, vocês encontram um jeito próprio de celebrar."));
        } else if (p.temFlag("cap8_comemoracao_coletiva")) {
            out.add(new Epilogo("A FORMATURA",
                    "Você foi à colação e depois encontrou a turma. Não teve pacote de fotos nem jantar contratado: "
                            + "teve comida dividida, fotos de celular e gente que ajudou a organizar. "
                            + "Não foi uma versão inferior da festa cara. Foi uma comemoração que coube no que vocês tinham."));
        } else if (p.temFlag("cap8_vendeu_ingresso")) {
            out.add(new Epilogo("A FORMATURA",
                    "Você vendeu o ingresso da festa privada e foi à colação oficial. "
                            + "A conta precisava fechar, mas isso não exigiu abrir mão de ouvir seu nome no auditório.\n\n"
                            + "Sua mãe ainda tem a foto que conseguiu tirar com o celular de lente trincada."));
        } else if (p.temFlag("cap8_pacote_completo")) {
            out.add(new Epilogo("A FORMATURA",
                    "O pacote custou caro, e aceitar a ajuda da sua mãe também teve peso. "
                            + "Mas naquele dia você conseguiu participar da cerimônia e guardar as fotos. "
                            + "A ajuda não transforma sua conquista em dívida moral: vocês decidiram juntas como atravessar aquele custo."));
        } else if (p.temFlag("cap8_colacao_oficial") || p.temFlag("cap8_foi_formatura")) {
            out.add(new Epilogo("A FORMATURA",
                    "Você participou da colação sem contratar o pacote completo. "
                            + "Não teve álbum profissional nem jantar organizado pela empresa, mas teve o nome anunciado "
                            + "e a foto que sua mãe tirou com o celular de lente trincada."));
        }
    }

    // ═══ O QUE FICA ═══
    private static void oQueFica(List<Epilogo> out) {
        out.add(new Epilogo("O QUE FICA",
                "Você não saiu da universidade com tudo resolvido.\n\n"
                        + "Ainda tem contas, dúvidas e coisas que você gostaria de ter feito diferente. "
                        + "Tem gente que ficou pelo caminho. Tem gente que ficou com você.\n\n"
                        + "O diploma não prova que você conseguiu fazer tudo certo. Prova que, apesar de tudo, "
                        + "você chegou ao fim de uma parte da história.\n\n"
                        + "A próxima não vem com gabarito."));
    }

    // ═══ ECO DA RESPOSTA INICIAL (com cap8_resposta_confissao/autoria novos) ═══
    private static void ecoDaRespostaInicial(List<Epilogo> out, Protagonista p) {
        String t;
        if (p.temFlag("prologo_resposta_pratica") && p.temFlag("cap8_resposta_conviccao")) {
            t = "No começo, você respondeu que precisava daquele trabalho. Cinco anos depois, conseguiu dizer "
                    + "que também o escolheu. A necessidade não deixou de existir; ela passou a dividir espaço com "
                    + "uma decisão que você entende melhor.";
        } else if (p.temFlag("prologo_resposta_conviccao") && p.temFlag("cap8_resposta_honesta")) {
            t = "No começo, você disse que tinha escolhido esse caminho. Anos depois, admitiu que ainda estava "
                    + "descobrindo. As duas respostas podem ser verdadeiras.";
        } else if (p.temFlag("prologo_resposta_aberta") && p.temFlag("cap8_resposta_conviccao")) {
            t = "No começo, você não sabia explicar o que estava buscando. No fim, conseguiu defender uma escolha "
                    + "sem precisar prometer que ela seria definitiva.";
        } else if (p.temFlag("prologo_resposta_pratica") && p.temFlag("cap8_resposta_honesta")) {
            t = "Você começou falando da necessidade e terminou admitindo que ainda estava descobrindo. "
                    + "Não é uma resposta perfeita para entrevista, mas é uma medida mais honesta da pessoa que chegou até aqui.";
        } else if (p.temFlag("cap8_resposta_confissao")) {
            t = "No começo, você respondeu o que dava pra responder. Anos depois, escolheu contar o que ninguém "
                    + "pergunta em entrevista. Não foi uma confissão estratégica. Foi a única resposta que reconhecia "
                    + "a pessoa que fez o trabalho — inclusive as partes que você não queria mostrar.";
        } else if (p.temFlag("cap8_resposta_autoria")) {
            t = "Você terminou a entrevista falando de um artigo que quase não teve seu nome. "
                    + "Não foi uma resposta fácil de ouvir. Foi a que mais se parecia com você.";
        } else {
            t = "A pergunta da entrevista que abriu a história não ganhou uma resposta universal. "
                    + "O que mudou foi a sua capacidade de explicar de onde veio, o que escolheu e o que ainda não sabe.";
        }
        out.add(new Epilogo("A PERGUNTA DO COMEÇO", t));
    }

    // ═══ FUTURO ESCOLHIDO ═══
    private static void futuroEscolhido(List<Epilogo> out, Protagonista p) {
        String titulo;
        StringBuilder t = new StringBuilder();

        if (p.temFlag("futuro_pesquisa_brasil")) {
            titulo = "SEU CAMINHO — PESQUISA NO BRASIL";
            t.append("Você continua fazendo perguntas difíceis sem tratar o Brasil como sala de espera. "
                    + "Bolsa, orientação e recursos continuam sendo condições concretas, não garantias.\n\n");
            if (p.temFlag("trilha_ic")) t.append("Você já viu por dentro um pouco da pesquisa.\n\n");
            else if (p.temFlag("trilha_dtec")) t.append("Você chega à pesquisa com experiência prática e de ensino.\n\n");
            else t.append("Seu caminho até aqui foi menos linear que uma seleção acadêmica costuma supor.\n\n");
        } else if (p.temFlag("futuro_tecnologia")) {
            titulo = "SEU CAMINHO — TECNOLOGIA APLICADA";
            t.append("Você escolhe construir tecnologia que precisa funcionar no mundo real. "
                    + "O primeiro produto não sai perfeito.\n\n");
            if (p.temFlag("trilha_dtec")) t.append("A experiência prática e de ensino ajuda você a traduzir conhecimento em procedimentos.\n\n");
            else if (p.temFlag("trilha_ic")) t.append("O olhar investigativo que desenvolveu na pesquisa ajuda a formular hipóteses.\n\n");
            else t.append("A experiência com projetos e negociação ajuda a entender o problema de quem vai usar a solução.\n\n");
        } else if (p.temFlag("futuro_internacional")) {
            titulo = "SEU CAMINHO — ALÉM DAS FRONTEIRAS";
            t.append("Você prepara uma oportunidade internacional. Idioma, portfólio, cartas, visto, financiamento "
                    + "e distância da rede de apoio entram na conta.\n\n");
            if (p.temFlag("origem_apertada")) t.append("A parte financeira pesa de forma concreta. Você procura bolsas, prazos e alternativas.\n\n");
            else t.append("Você compara a oportunidade com o que ela exige de você e de quem faz parte da sua vida.\n\n");
        } else if (p.temFlag("futuro_independente")) {
            titulo = "SEU CAMINHO — CARREIRA INDEPENDENTE";
            t.append("Você escolhe montar uma carreira com clientes e projetos. Autonomia vem junto com orçamento, "
                    + "contrato, imposto, cobrança e períodos sem trabalho.\n\n");
            if (p.temFlag("cap7_indep_negociou")) t.append("Você já conseguiu revisar uma cláusula antes de assinar.\n\n");
            else if (p.temFlag("cap7_indep_aceitou")) t.append("Você aceitou um contrato sem compreender tudo o que estava escrito.\n\n");
            else if (p.temFlag("cap7_indep_advogado")) t.append("Você procurou orientação antes de assumir um risco que não compreendia.\n\n");
        } else if (p.temFlag("futuro_transicao")) {
            titulo = "SEU CAMINHO — RECOMEÇAR SEM APAGAR O PASSADO";
            t.append("Você muda de área e encara o custo real de aprender um vocabulário novo.\n\n");
            if (p.temFlag("cap6_trancou")) t.append("Você já precisou reorganizar o calendário uma vez.\n\n");
            else t.append("Você não precisou trancar o semestre para perceber que um caminho pode mudar.\n\n");
        } else {
            titulo = "SEU CAMINHO — AINDA EM ABERTO";
            t.append("Esta partida chegou ao encerramento sem registrar uma escolha de futuro.");
        }

        if (p.temFlag("cap6_pediu_ajuda")) t.append("Você também tem uma lembrança concreta de ter pedido ajuda quando precisava.\n\n");
        if (p.temFlag("cap7_questionou_autoria")) t.append("Ao discutir a autoria do artigo, você defendeu o reconhecimento do seu trabalho.\n\n");
        else if (p.temFlag("cap7_aceitou_autoria")) t.append("Você aceitou a decisão sobre a autoria e ganhou acesso a novos projetos.\n\n");
        if (p.temFlag("hab_olho_edital") && p.temFlag("cap7_tcc_planejou")) t.append("No começo, você guardava comprovantes; no fim, dividiu o TCC em etapas.\n\n");
        if (p.temFlag("cap5b_dividiu_comida")) t.append("Você leva da greve a lembrança de que a continuidade dos estudos depende de cuidado coletivo.\n\n");

        out.add(new Epilogo(titulo, t.toString().trim()));
    }

    // ═══ AVALIAÇÕES ═══
    public static DesempenhoAcademico avaliarDesempenhoAcademico(Protagonista p) {
        int score = p.getScore();
        int part = p.getParticipacao();
        if (score < 30 && part < 30) return DesempenhoAcademico.DIFICULDADE;
        if (score >= 70 && part >= 50) return DesempenhoAcademico.DESTAQUE;
        return DesempenhoAcademico.RECUPERACAO;
    }

    public static BemEstarFinal avaliarBemEstar(Protagonista p) {
        int baixos = 0;
        if (p.getConfianca() <= 2) baixos++;
        if (p.getSanidade() <= 2) baixos++;
        if (p.getVidaSocial() <= 2) baixos++;
        if (baixos >= 2) return BemEstarFinal.FRAGILIZADO;
        if (p.getConfianca() >= 6 && p.getSanidade() >= 6 && p.getVidaSocial() >= 6)
            return BemEstarFinal.ESTAVEL;
        return BemEstarFinal.OSCILANTE;
    }
}