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
                "Segunda, 7h14. Você tá no ônibus, colada no vidro, e o celular apita.\n\n" +
                        "'ÁGATA — Bem-vindo ao semestre 2026.2'.\n\n" +
                        "O corpo do email tem doze links. Onze dizem 'consulte o edital'. " +
                        "O décimo segundo diz 'bom semestre!'.\n\n" +
                        "(Exclamação.)");
        cena100.adicionarEscolha(new Escolha.Builder(
                "Abrir os onze agora, com o sinal caindo.", 101)
                .comCustoEnergia(10).build());
        cena100.adicionarEscolha(new Escolha.Builder(
                "Marcar como não lido. 'Depois' significa nunca.", 101)
                .comFlagConcedida("ignorou_email").build());
        cenas.add(cena100);

        Cena cena101 = new Cena(101, "Capítulo 2", "Narrador",
                "Mural do módulo 1. Três cartazes novos, presos com fita crepe.\n\n" +
                        "CA de Computação — reunião quarta, 17h, sala 12.\n\n" +
                        "DCE — assembleia sobre o RU, quinta, 14h, pátio.\n\n" +
                        "Um terceiro, de um grupo de software com nome de quatro siglas.");
        cena101.adicionarEscolha(new Escolha.Builder("Ir no CA.", 102).build());
        cena101.adicionarEscolha(new Escolha.Builder("Ir na assembleia do DCE.", 105).build());
        cena101.adicionarEscolha(new Escolha.Builder("Ir no grupo de software.", 108).build());
        cena101.adicionarEscolha(new Escolha.Builder("Anotar os três e decidir depois.", 111)
                .comFlagConcedida("indecisa").build());
        cenas.add(cena101);

        Cena cena102 = new Cena(102, "Capítulo 2", "Narrador",
                "A reunião do CA é numa sala emprestada. Sete pessoas, duas pizzas, " +
                        "um crachá de 'Diretora' escrito à mão em fita crepe.\n\n" +
                        "Falam de um projeto de extensão em escola pública.");
        cena102.adicionarEscolha(new Escolha.Builder("Assinar a lista.", 103)
                .comScoreGanho(4).comParticipacaoGanha(5).build());
        cena102.adicionarEscolha(new Escolha.Builder("Perguntar quanto tempo por semana.", 103).build());
        cena102.adicionarEscolha(new Escolha.Builder("Só olhar. Ainda tô decidindo.", 103)
                .comCustoEnergia(5).build());
        cenas.add(cena102);

        Cena cena103 = new Cena(103, "Capítulo 2", "Narrador",
                "Sexta tem a primeira reunião de verdade.\n\n" +
                        "'Projeto de extensão' significa: ir numa escola pública às terças, " +
                        "ensinar Scratch pra vinte crianças de onze anos, e voltar pra casa " +
                        "às 19h com dor de cabeça.");
        cena103.adicionarEscolha(new Escolha.Builder("Aguentar.", 112)
                .comParticipacaoGanha(5).comCustoEnergia(20)
                .comAtributo("vidaSocial", 1).build());
        cena103.adicionarEscolha(new Escolha.Builder("Sair. Não é pra você.", 112)
                .comFlagConcedida("ca_saiu").build());
        cenas.add(cena103);

        Cena cena105 = new Cena(105, "Capítulo 2", "Narrador",
                "A assembleia é no pátio. Quarenta pessoas, um megafone, " +
                        "duas faixas contra a terceirização do RU.\n\n" +
                        "O assunto: a empresa quer subir de R$ 2 pra R$ 4.");
        cena105.adicionarEscolha(new Escolha.Builder("Assinar e ficar.", 106)
                .comParticipacaoGanha(5).comFlagConcedida("dce_ru").build());
        cena105.adicionarEscolha(new Escolha.Builder("Assinar e ir.", 106)
                .comCustoEnergia(5).build());
        cena105.adicionarEscolha(new Escolha.Builder("Não assinar.", 107)
                .comFlagConcedida("dce_fora").build());
        cenas.add(cena105);

        Cena cena106 = new Cena(106, "Capítulo 2", "Narrador",
                "A assembleia vira discussão sobre orçamento da universidade.\n\n" +
                        "Você entende metade. Mas entende o suficiente: o RU a R$ 2 é subsídio. " +
                        "O subsídio depende de verba do estado. A verba depende de político.\n\n" +
                        "(Tudo é política. Até o frango com quiabo.)");
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
                        "(Eu contei.)");
        cena109.adicionarEscolha(new Escolha.Builder("Fazer mesmo assim.", 112)
                .comScoreGanho(4).comCustoEnergia(20).build());
        cena109.adicionarEscolha(new Escolha.Builder("Só assistir.", 112).build());
        cenas.add(cena109);

        Cena cena111 = new Cena(111, "Capítulo 2", "Narrador",
                "A semana passou. Você não foi em nada.\n\n" +
                        "Não foi um erro. Só foi uma semana.");
        cena111.adicionarEscolha(new Escolha.Builder("Ir pra casa.", 112).build());
        cenas.add(cena111);

        Cena cena112 = new Cena(112, "Capítulo 2", "Cáio Andrade",
                "Cáio tá no pátio, comendo um salgado com uma mão e scrollando o " +
                        "celular com a outra. Ele te vê. Guarda o celular.\n\n" +
                        "'Tá indo em tudo, né?'\n\n" +
                        "Não é pergunta.\n\n" +
                        "É diagnóstico.\n\n" +
                        "(Ele me conhece há uma semana e já sabe.)\n\n" +
                        "'Eu fiz isso no primeiro semestre. Três grupos, dois projetos, " +
                        "um campeonato. No segundo eu não tava indo em NADA. Porque tava " +
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

        Cena cena113 = new Cena(113, "Capítulo 2", "Cáio Andrade",
                "Ele pensa. Não responde na hora.\n\n" +
                        "'Cortei tudo. Fiquei só no que eu não conseguia parar de pensar.'\n\n" +
                        "Pausa.\n\n" +
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

        Cena cena114 = new Cena(114, "Capítulo 2", "Cáio Andrade",
                "Ele te olha por um segundo.\n\n" +
                        "'Tá bom.'\n\n" +
                        "Volta pro celular.");
        cena114.adicionarEscolha(new Escolha.Builder("Seguir em frente.", 115).build());
        cenas.add(cena114);

        Cena cena115 = new Cena(115, "Capítulo 2", "Narrador",
                "Você vai embora. O semestre tá só começando.");
        cena115.adicionarEscolha(new Escolha.Builder("Encerrar Capítulo 2.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap2_concluido").build());
        cenas.add(cena115);

        // ─── FEIRA — hub IEEE ─────────────────────────────────────────
        Cena cena116 = new Cena(116, "Capítulo 2 — Feira de Graduação", "Narrador",
                "A universidade tá fazendo a Feira de Graduação.\n\n" +
                        "Estandes no pátio central. Caravanas de escolas da região. " +
                        "Crianças de 15 anos com camiseta de colégio público andando em fila.\n\n" +
                        "E um corredor inteiro de estandes com bandeiras azuis.\n\n" +
                        "IEEE.\n\n" +
                        "RAS, WIE, TISP, Liga de Jogos, Industry Electronics. " +
                        "Cada um com um cartaz, um banner e pelo menos duas pessoas " +
                        "com camiseta azul tentando te convencer a entrar.");
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
                        "Você não sabe. Você acha que não sabe. Você nunca tentou.");
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
                "O estande do WIE tá com uma toalha rosa e um banner " +
                        "que diz 'Women in Engineering'.\n\n" +
                        "Duas veteranas te cumprimentam. Falam de um projeto " +
                        "de extensão numa escola pública: ensino de lógica " +
                        "pra meninas de 12 anos.");
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
                "TISP: Teacher In-Service Program.\n\n" +
                        "O estande tem uma maquete de ponte de palito e um pôster " +
                        "de como construir uma catapulta com material de escritório.\n\n" +
                        "É um grupo que ensina professor de escola pública a ensinar " +
                        "engenharia pra criança.\n\n" +
                        "Você nunca tinha ouvido falar nisso.");
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
                "A Liga de Jogos montou um mini fliperama no estande. " +
                        "Um notebook com controle de SNES e um jogo de plataforma.\n\n" +
                        "O pessoal tá jogando. Um cara de camiseta de estúdio indie te olha:\n\n" +
                        "'Você joga? Tô fazendo um projeto de jogo narrativo " +
                        "pra disciplina de Jogos Digitais. Preciso de gente pra testar.'");
        cena123.adicionarEscolha(new Escolha.Builder(
                "Anotar o contato dele. Você quer testar.", 118)
                .comParticipacaoGanha(4).comCustoEnergia(5)
                .comFlagConcedida("liga_jogos_contato").build());
        cena123.adicionarEscolha(new Escolha.Builder(
                "Jogar uma partida rápida. Só isso.", 118)
                .comCustoEnergia(-10)
                .comAtributo("sanidade", 3).build());
        cenas.add(cena123);

// ─── FECHAMENTO DA FEIRA (antiga 117) ─────────────────────────
        Cena cena118 = new Cena(118, "Capítulo 2", "Narrador",
                "No estande do curso, um menino do 3º ano pergunta:\n\n" +
                        "'É difícil?'\n\n" +
                        "Você olha pro Cáio, que tá do lado com o copo de sempre.\n\n" +
                        "Ele responde antes de você.\n\n" +
                        "'É. Mas dá.'");
        cena118.adicionarEscolha(new Escolha.Builder("Concordar.", 112)
                .comRelacionamento(PersonagemSecundario.VETERANO, 1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena118.adicionarEscolha(new Escolha.Builder("Dizer que é tranquilo.", 112)
                .comFlagConcedida("mentiu_feira").build());
        cenas.add(cena118);

        return cenas;
    }
}