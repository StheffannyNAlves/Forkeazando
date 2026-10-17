package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;

import java.util.ArrayList;
import java.util.List;

public class Capitulo2 {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();

        Cena cena100 = new Cena(100, "Capítulo 2 — As Primeiras Sereias", "Narrador",
                "Segunda, 7h14. Você está no ônibus, encostada no vidro, quando o celular vibra.\n\n" +
                        "'ÁGATA — Bem-vindo ao semestre 2026.2'.\n\n" +
                        "O e-mail tem doze links. Onze terminam em 'consulte o edital'. " +
                        "O último deseja um bom semestre.\n\n" +
                        "Você abre o primeiro link. A página demora a carregar. O ônibus passa por um buraco, " +
                        "o sinal desaparece e o aviso continua ali, esperando que você descubra sozinha por onde começar.");
        cena100.adicionarEscolha(new Escolha.Builder(
                "Abrir os onze agora, com o sinal caindo.", 101)
                .comCustoEnergia(10).build());
        cena100.adicionarEscolha(new Escolha.Builder(
                "Marcar como não lido. 'Depois' significa nunca.", 101)
                .comFlagConcedida("ignorou_email").build());
        cenas.add(cena100);

        Cena cena101 = new Cena(101, "Capítulo 2", "Narrador",
                "Mural do Módulo 1. Três cartazes novos, presos com fita crepe.\n\n" +
                        "CA de Computação — reunião quarta, 17h, sala 12.\n\n" +
                        "DCE — assembleia sobre o RU, quinta, 14h, pátio.\n\n" +
                        "O terceiro anuncia um grupo de software com um nome feito de quatro siglas.\n\n" +
                        "Você lê os três cartazes. Parece que todo mundo já sabe onde quer estar. " +
                        "Você ainda está tentando entender o que cada lugar faz.");
        cena101.adicionarEscolha(new Escolha.Builder("Ir no CA.", 102).build());
        cena101.adicionarEscolha(new Escolha.Builder("Ir na assembleia do DCE.", 105).build());
        cena101.adicionarEscolha(new Escolha.Builder("Ir no grupo de software.", 108).build());
        cena101.adicionarEscolha(new Escolha.Builder("Anotar os três e decidir depois.", 111).comFlagConcedida("indecisa").build());
        cena101.adicionarEscolha(new Escolha.Builder("Ir na Feira de Graduação, no Módulo 3.", 116).build());
        cenas.add(cena101);

        Cena cena102 = new Cena(102, "Capítulo 2", "Narrador",
                "A reunião do CA é numa sala emprestada. Sete pessoas, duas pizzas, " +
                        "um crachá de 'Diretora' escrito à mão em fita crepe.\n\n" +
                        "O grupo conversa sobre um projeto de extensão numa escola pública. " +
                        "Você tenta acompanhar, sem saber ainda se aquilo cabe na sua rotina.");
        cena102.adicionarEscolha(new Escolha.Builder("Assinar a lista.", 103)
                .comScoreGanho(4).comParticipacaoGanha(5).build());
        cena102.adicionarEscolha(new Escolha.Builder("Perguntar quanto tempo por semana.", 103).build());
        cena102.adicionarEscolha(new Escolha.Builder("Só olhar. Ainda tô decidindo.", 103)
                .comCustoEnergia(5).build());
        cenas.add(cena102);

        Cena cena103 = new Cena(103, "Capítulo 2", "Narrador",
                "Na sexta acontece a primeira reunião de verdade.\n\n" +
                        "O projeto de extensão significa ir a uma escola pública às terças, " +
                        "ensinar Scratch a vinte crianças de onze anos e voltar para casa " +
                        "às 19h com dor de cabeça.\n\n" +
                        "A descrição parece simples até você imaginar isso encaixado entre aulas, " +
                        "trabalhos e o tempo que leva para chegar em casa.");
        cena103.adicionarEscolha(new Escolha.Builder("Aguentar.", 112)
                .comParticipacaoGanha(5).comCustoEnergia(20)
                .comAtributo("vidaSocial", 1).build());
        cena103.adicionarEscolha(new Escolha.Builder("Sair. Não é pra você.", 112)
                .comFlagConcedida("ca_saiu").build());
        cenas.add(cena103);

        Cena cena105 = new Cena(105, "Capítulo 2", "Narrador",
                "A assembleia acontece no pátio. Cerca de quarenta pessoas, um megafone " +
                        "e duas faixas contra a terceirização do RU.\n\n" +
                        "A discussão começa com uma proposta: a empresa quer aumentar o preço da refeição de R$ 2 para R$ 4. Você acompanha as falas, tentando entender como uma decisão dessas chega até o prato de quem está na fila.");
        cena105.adicionarEscolha(new Escolha.Builder("Assinar e ficar.", 106)
                .comParticipacaoGanha(5).comFlagConcedida("dce_ru").build());
        cena105.adicionarEscolha(new Escolha.Builder("Assinar e ir.", 106)
                .comCustoEnergia(5).build());
        cena105.adicionarEscolha(new Escolha.Builder("Não assinar.", 107)
                .comFlagConcedida("dce_fora").build());
        cenas.add(cena105);

        Cena cena106 = new Cena(106, "Capítulo 2", "Narrador",
                "A assembleia vira discussão sobre orçamento da universidade.\n\n" +
                        "Você entende metade. O preço do RU depende de subsídios; os subsídios, " +
                        "de verbas públicas; e as verbas, de decisões políticas.\n\n" +
                        "Na volta, você pensa que até o frango com quiabo acaba entrando nessa conversa.");
        cena106.adicionarEscolha(new Escolha.Builder("Continuar indo.", 112)
                .comParticipacaoGanha(5).comAtributo("vidaSocial", 1).build());
        cena106.adicionarEscolha(new Escolha.Builder("Ir uma vez e nunca mais.", 112)
                .comCustoEnergia(5).build());
        cenas.add(cena106);

        Cena cena107 = new Cena(107, "Capítulo 2", "Narrador",
                "Você passa no RU no caminho. A fila tá curta hoje.\n\n" +
                        "Frango com quiabo. De novo.");
        cena107.adicionarEscolha(new Escolha.Builder("Seguir pro software.", 108).build());
        cenas.add(cena107);

        Cena cena108 = new Cena(108, "Capítulo 2", "Narrador",
                "Auditório emprestado. Slide. Pausa dramática. Um cara de camisa " +
                        "social fala 'disruptivo' três vezes em dois minutos.\n\n" +
                        "Você pensa em ir embora. Aí ele menciona um processo seletivo " +
                        "pra estágio. Você fica.");
        cena108.adicionarEscolha(new Escolha.Builder("Pegar o QR code.", 109)
                .comFlagConcedida("item_vaga_estagio").build());
        cena108.adicionarEscolha(new Escolha.Builder(
                "Perguntar publicamente se é remunerado.", 109)
                .comFlagConcedida("pergunta_ousada").build());
        cena108.adicionarEscolha(new Escolha.Builder("Ir embora no meio.", 111)
                .comFlagConcedida("software_fora").build());
        cenas.add(cena108);

        Cena cena109 = new Cena(109, "Capítulo 2", "Narrador",
                "Quatro etapas.\n\n" +
                        "A primeira é um desafio de código que você não sabe fazer.\n\n" +
                        "A segunda é uma entrevista com alguém que fala 'fit cultural' " +
                        "quatro vezes em sete minutos.\n\n" +
                        "Você tenta imaginar como responder sem fingir que entendeu o que isso quer dizer.");
        cena109.adicionarEscolha(new Escolha.Builder("Fazer mesmo assim.", 112)
                .comScoreGanho(4).comCustoEnergia(20).build());
        cena109.adicionarEscolha(new Escolha.Builder("Só assistir.", 112).build());
        cenas.add(cena109);

        Cena cena111 = new Cena(111, "Capítulo 2", "Narrador",
                "A semana passa. Você não foi a nenhuma das atividades.\n\n" +
                        "Não foi um erro. Foi uma semana em que você ainda não conseguiu escolher — e talvez precisasse desse tempo para entender o que queria.");
        cena111.adicionarEscolha(new Escolha.Builder("Ir pra casa.", 112).build());
        cenas.add(cena111);

        Cena cena112 = new Cena(112, "Capítulo 2", "Caio Andrade",
                "Caio tá no pátio, comendo um salgado com uma mão e scrollando o " +
                        "celular com a outra. Quando te vê, guarda o aparelho.\n\n" +
                        "'Tá indo em tudo, né?'\n\n" +
                        "Não parece uma pergunta. Parece que ele já chegou a uma conclusão.\n\n" +
                        "'Eu fiz isso no primeiro semestre. Três grupos, dois projetos, " +
                        "um campeonato. No segundo, eu não estava indo a nada. Porque estava " +
                        "reprovando em tudo.'\n\n" +
                        "(Ele tá me assustando ou me avisando?)\n\n" +
                        "Os dois.");
        cena112.adicionarEscolha(new Escolha.Builder(
                "Pergunto como ele saiu disso.", 113)
                .comRelacionamento(PersonagemSecundario.VETERANO, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena112.adicionarEscolha(new Escolha.Builder(
                "Digo que eu não vou fazer isso. Consigo lidar com tudo.", 114)
                .comAtributo("confianca", -1)
                .comRelacionamento(PersonagemSecundario.VETERANO, -2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena112.adicionarEscolha(new Escolha.Builder("Fico quieta.", 115).build());
        cenas.add(cena112);

        Cena cena113 = new Cena(113, "Capítulo 2", "Caio Andrade",
                "Ele pensa antes de responder.\n\n" +
                        "'Cortei tudo. Fiquei só no que eu não conseguia parar de pensar.'\n\n" +
                        "Ele faz uma pausa, como se tentasse escolher as palavras.\n\n" +
                        "'O problema não é fazer muito. É fazer muito do que você não quer.'");
        cena113.adicionarEscolha(new Escolha.Builder(
                "Perguntar o que ele corta primeiro.", 115)
                .comAtributo("confianca", 1)
                .comRelacionamento(PersonagemSecundario.VETERANO, 1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena113.adicionarEscolha(new Escolha.Builder(
                "Agradecer e ir embora.", 115)
                .comRelacionamento(PersonagemSecundario.VETERANO, 1f).build());
        cenas.add(cena113);

        Cena cena114 = new Cena(114, "Capítulo 2", "Caio Andrade",
                "Ele te olha por um instante.\n\n" +
                        "'Tá bom.'\n\n" +
                        "Depois, volta para o celular. A conversa acabou ali.");
        cena114.adicionarEscolha(new Escolha.Builder("Seguir em frente.", 115).build());
        cenas.add(cena114);

        Cena cena115 = new Cena(115, "Capítulo 2", "Narrador",
                "Você vai embora. O semestre está só começando, e ainda há mais coisas chamando sua atenção do que horas disponíveis no dia.");
        cena115.adicionarEscolha(new Escolha.Builder("Encerrar Capítulo 2.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap2_concluido").build());
        cenas.add(cena115);

        // ─── FEIRA — hub IEEE ─────────────────────────────────────────
        Cena cena116 = new Cena(116, "Capítulo 2 — Feira de Graduação", "Narrador",
                "A universidade está realizando a Feira de Graduação.\n\n" +
                        "Há estandes no pátio central e caravanas de escolas da região. Estudantes " +
                        "do ensino médio caminham em grupos, olhando os cursos e tentando imaginar como seria estudar ali.\n\n" +
                        "Um corredor inteiro reúne estandes com bandeiras azuis: IEEE, RAS, WIE, TISP, Liga de Jogos e Industry Electronics. " +
                        "Em cada um, alguém tenta explicar o grupo " +
                        "e convencer os visitantes a participar.");
        cena116.adicionarEscolha(new Escolha.Builder("Ir no RAS. Robô é mais legal.", 120)
                .comCustoEnergia(10).build());
        cena116.adicionarEscolha(new Escolha.Builder("Ir no WIE. Parece acolhedor.", 121)
                .comCustoEnergia(5).build());
        cena116.adicionarEscolha(new Escolha.Builder("Ir no TISP.", 122)
                .comCustoEnergia(10).build());
        cena116.adicionarEscolha(new Escolha.Builder("Ir na Liga de Jogos.", 123)
                .comCustoEnergia(5).build());
        cena116.adicionarEscolha(new Escolha.Builder("Só passar rápido pra ver como é.", 118)
                .comCustoEnergia(5).build());
        cena116.adicionarEscolha(new Escolha.Builder("Ignorar. Você não tem tempo.", 118).build());
        cenas.add(cena116);

// ─── RAS ──────────────────────────────────────────────────────
        Cena cena120 = new Cena(120, "Capítulo 2", "Narrador",
                "O estande do RAS tem dois carrinhos de robô e um Arduino " +
                        "que não funciona há dois semestres.\n\n" +
                        "Uma menina com cabelo preso te olha.\n\n" +
                        "'Sabe soldar?'\n\n" +
                        "Você não sabe. Ou acha que não sabe. Nunca tentou.");
        cena120.adicionarEscolha(new Escolha.Builder(
                "Não sei. Mas quero aprender.", 118)
                .comParticipacaoGanha(4).comCustoEnergia(15)
                .comRelacionamento(PersonagemSecundario.MALU, 2f)
                .comFlagConcedida("ras_curiosa").build());
        cena120.adicionarEscolha(new Escolha.Builder(
                "Não sei soldar e não tô afim de aprender agora.", 118)
                .comCustoEnergia(5).build());
        cenas.add(cena120);

// ─── WIE ──────────────────────────────────────────────────────
        Cena cena121 = new Cena(121, "Capítulo 2", "Narrador",
                "O estande do WIE tem uma toalha rosa e um banner " +
                        "escrito 'Women in Engineering'.\n\n" +
                        "Duas veteranas te cumprimentam. Contam sobre um projeto " +
                        "de extensão numa escola pública, com atividades de lógica " +
                        "para meninas de doze anos.\n\n" +
                        "Você escuta, tentando entender como é participar de um grupo assim durante o semestre.");
        cena121.adicionarEscolha(new Escolha.Builder(
                "Assinar a lista de interesse.", 118)
                .comParticipacaoGanha(4).comCustoEnergia(5)
                .comFlagConcedida("wie_interesse").build());
        cena121.adicionarEscolha(new Escolha.Builder(
                "Perguntar como é a rotina do grupo.", 118)
                .comAtributo("confianca", 1).comCustoEnergia(5).build());
        cenas.add(cena121);

// ─── TISP ─────────────────────────────────────────────────────
        Cena cena122 = new Cena(122, "Capítulo 2", "Narrador",
                "TISP significa Teacher In-Service Program.\n\n" +
                        "No estande, há uma maquete de ponte feita de palitos e um pôster " +
                        "que ensina a construir uma catapulta com materiais de escritório.\n\n" +
                        "O grupo ajuda professores de escolas públicas a ensinar " +
                        "engenharia às crianças. Você nunca tinha ouvido falar desse tipo de iniciativa.");
        cena122.adicionarEscolha(new Escolha.Builder(
                "Assinar a lista.", 118)
                .comParticipacaoGanha(4).comCustoEnergia(10)
                .comFlagConcedida("tisp_interesse").build());
        cena122.adicionarEscolha(new Escolha.Builder(
                "Só olhar e ir embora.", 118)
                .comCustoEnergia(5).build());
        cenas.add(cena122);

// ─── LIGA DE JOGOS ────────────────────────────────────────────
        Cena cena123 = new Cena(123, "Capítulo 2", "Narrador",
                "A Liga de Jogos montou um pequeno fliperama no estande: " +
                        "um notebook, um controle de SNES e um jogo de plataforma.\n\n" +
                        "As pessoas se revezam no jogo. Um rapaz de camiseta de estúdio indie te olha.\n\n" +
                        "'Você joga? Estou fazendo um projeto de jogo narrativo " +
                        "para a disciplina de Jogos Digitais. Preciso de gente para testar.'");
        cena123.adicionarEscolha(new Escolha.Builder(
                "Anotar o contato dele. Você quer testar.", 118)
                .comParticipacaoGanha(4).comCustoEnergia(5)
                .comFlagConcedida("liga_jogos_contato").build());
        cena123.adicionarEscolha(new Escolha.Builder(
                "Jogar uma partida rápida. Só isso.", 118)
                .comAtributo("energia", -10)
                .comAtributo("sanidade", 3).build());
        cenas.add(cena123);

// ─── FECHAMENTO DA FEIRA (antiga 117) ─────────────────────────
        Cena cena118 = new Cena(118, "Capítulo 2", "Narrador",
                "No estande do curso, um estudante do terceiro ano pergunta:\n\n" +
                        "'É difícil?'\n\n" +
                        "Você olha pro Caio, que tá do lado com o copo de sempre.\n\n" +
                        "Ele responde antes de você:\n\n" +
                        "'É. Mas dá.'\n\n" +
                        "O estudante parece pensar na resposta. Você também.");
        cena118.adicionarEscolha(new Escolha.Builder("Concordar.", 112)
                .comRelacionamento(PersonagemSecundario.VETERANO, 1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena118.adicionarEscolha(new Escolha.Builder("Dizer que é tranquilo.", 112)
                .comFlagConcedida("mentiu_feira").build());
        cenas.add(cena118);

        return cenas;
    }
}