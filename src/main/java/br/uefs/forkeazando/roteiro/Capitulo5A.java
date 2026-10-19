package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;

import java.util.ArrayList;
import java.util.List;

public class Capitulo5A {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();

        Cena cena400 = new Cena(400, "Capítulo 5A — IC", "Narrador",
                "Reunião do grupo. Terça, 14h, sala 204. Cinco pessoas. Quatro " +
                        "mestrandos e você.\n\n" +
                        "O orientador te apresenta como 'a nova'. Ninguém pergunta seu nome. " +
                        "Você procura um lugar à mesa sem interromper a conversa.\n\n" +
                        "A reunião dura quarenta minutos. Ele fala de prazo, cronograma e " +
                        "artigo atrasado. Você entende metade — e anota até o que não entende.\n\n" +
                        "Quando a reunião termina, todo mundo parece saber o que fazer. " +
                        "Você fecha o caderno e espera descobrir por onde começar.");
        cena400.adicionarEscolha(new Escolha.Builder(
                "Falar meu nome e explicar o que eu sei fazer.", 402)
                .comAtributo("carisma", 2)
                .comRelacionamento(PersonagemSecundario.ORIENTADOR_IC, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.RECONHECIMENTO).build());
        cena400.adicionarEscolha(new Escolha.Builder(
                "Ficar quieta e anotar. Vou provar pelo trabalho.", 402)
                .comCustoEnergia(10).build());
        cena400.adicionarEscolha(new Escolha.Builder(
                "Sentar no canto. Talvez ninguém perceba que cheguei.", 402)
                .comParticipacaoMaxima(3)
                .comCustoEnergia(5).build());
        cenas.add(cena400);

        Cena cena402 = new Cena(402, "Capítulo 5A", "Narrador",
                "No laboratório, Bia está com o fichário de sempre e um Excel aberto.\n\n" +
                        "'Achei que você fosse pra DTEC. Ainda bem que não.'\n\n" +
                        "Ela aponta para a planilha: dados de um experimento cujo nome " +
                        "não te diz absolutamente nada. As colunas estão preenchidas, " +
                        "mas ninguém parece ter explicado o que aqueles números significam.");
        cena402.adicionarEscolha(new Escolha.Builder(
                "Perguntar o que ela está fazendo ali.", 403)
                .comRelacionamento(PersonagemSecundario.BIA, 1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena402.adicionarEscolha(new Escolha.Builder(
                "Perguntar se ela ainda vende respostas.", 403)
                .comFlagRequerida("cap3_colou_de_fato")
                .comRelacionamento(PersonagemSecundario.BIA, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE)
                .comFlagConcedida("bia_ainda_vende").build());
        cena402.adicionarEscolha(new Escolha.Builder(
                "Não falar nada. Não preciso de aliada aqui.", 403)
                .comRelacionamento(PersonagemSecundario.BIA, -1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cenas.add(cena402);

        Cena cena403 = new Cena(403, "Capítulo 5A", "Beatriz Menezes",
                "'Eu não faço pesquisa. Eu faço o trabalho que ninguém quer.'\n\n" +
                        "Ela gira o notebook na sua direção.\n\n" +
                        "'Planilha, transcrição, revisão de referência. Dois semestres nisso.'\n\n" +
                        "Ela não parece estar reclamando para receber consolo. Parece estar " +
                        "avisando alguém que ainda pode decidir se quer entrar nessa.\n\n" +
                        "'Você vai acabar fazendo também. Todo mundo faz.'");
        cena403.adicionarEscolha(new Escolha.Builder("Digo que entendo.", 401)
                .comRelacionamento(PersonagemSecundario.BIA, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena403.adicionarEscolha(new Escolha.Builder(
                "Digo que não foi isso que vim fazer.", 401)
                .comRelacionamento(PersonagemSecundario.BIA, -1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cenas.add(cena403);

        Cena cena401 = new Cena(401, "Capítulo 5A", "Narrador",
                "Três semanas depois.\n\n" +
                        "A reunião de terça foi cancelada. A de quarta também. Na sexta, " +
                        "o orientador manda uma mensagem no grupo:\n\n" +
                        "'Semana que vem a gente retoma. Tô com prazo de artigo.'\n\n" +
                        "Ninguém responde. Você abre o calendário e conta quantas reuniões " +
                        "já foram adiadas. É a terceira vez no mês.\n\n" +
                        "O grupo continua existindo. O trabalho, por enquanto, parece não sair do lugar.");
        cena401.adicionarEscolha(new Escolha.Builder(
                "Mandar mensagem perguntando o que devo fazer enquanto isso.", 405)
                .comAtributo("carisma", 1)
                .comRelacionamento(PersonagemSecundario.ORIENTADOR_IC, 1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.RECONHECIMENTO).build());
        cena401.adicionarEscolha(new Escolha.Builder(
                "Ficar quieta. Ele que resolva quando puder.", 405)
                .comAtributo("confianca", -1).build());
        cena401.adicionarEscolha(new Escolha.Builder(
                "Perguntar pra Bia se é sempre assim.", 405)
                .comRelacionamento(PersonagemSecundario.BIA, 1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena401.adicionarEscolha(new Escolha.Builder(
                "Encontrar o cronograma no drive. Última atualização: quatro meses atrás.", 405)
                .comLogicaMinima(5)
                .comFlagConcedida("ic_sabe_cronograma")
                .comAtributo("confianca", 1).build());
        cenas.add(cena401);

        Cena cena405 = new Cena(405, "Capítulo 5A", "Narrador",
                "Na sexta-feira, o orientador finalmente aparece.\n\n" +
                        "Vinte minutos de reunião. Ele distribui tarefas, marca prazos e, " +
                        "na última frase, muda o tamanho do problema:\n\n" +
                        "'Vocês vão escrever um artigo. Cada um pega uma parte. Eu reviso no fim.'\n\n" +
                        "Ele sai antes que alguém pergunte como as partes devem se encaixar.\n\n" +
                        "Os mestrandos olham uns para os outros. Ninguém sabe o que fazer primeiro. " +
                        "O prazo, porém, já está no calendário.");
        cena405.adicionarEscolha(new Escolha.Builder(
                "Assumir a coordenação. Alguém precisa organizar isso.", 410)
                .comScoreGanho(6).comCustoEnergia(25)
                .comAtributo("confianca", 2)
                .comFlagConcedida("cap5a_liderou_artigo").build());
        cena405.adicionarEscolha(new Escolha.Builder(
                "Pegar a parte mais simples. Não quero me queimar.", 410)
                .comScoreGanho(3).comCustoEnergia(10).build());
        cena405.adicionarEscolha(new Escolha.Builder(
                "Perguntar quem vai revisar. O grupo fica em silêncio.", 410)
                .comFlagConcedida("cap5a_perguntou_revisao").build());
        cena405.adicionarEscolha(new Escolha.Builder(
                "Enfrentar tudo. Café e ódio.", 410)
                .comEnergiaAbaixoDe(20)
                .comCustoEnergia(30)
                .comAtributo("confianca", -5)
                .comPermiteEnergiaInsuficiente().build());
        cenas.add(cena405);

        Cena cena410 = new Cena(410, "Capítulo 5A", "Narrador",
                "Dois meses depois, o artigo continua sem estar pronto.\n\n" +
                        "Você escreveu sua parte e entregou. Os mestrandos fizeram o mesmo.\n\n" +
                        "O orientador não revisou, não respondeu e não mandou nada no grupo.\n\n" +
                        "O prazo da revista passou na semana anterior. Ninguém avisou.\n\n" +
                        "Você abre o arquivo mais uma vez. O texto está lá. O retorno que precisava, não.");
        cena410.adicionarEscolha(new Escolha.Builder(
                "Aguentar. Fiz a minha parte.", 415)
                .comAtributo("sanidade", 3).build());
        cena410.adicionarEscolha(new Escolha.Builder(
                "Pedir pra sair do grupo.", 415)
                .comFlagConcedida("cap5a_saiu_grupo").build());
        cena410.adicionarEscolha(new Escolha.Builder(
                "Aceitar que pesquisa também é lidar com resultados que não saem.", 415)
                .comSanidadeMinima(6)
                .comAtributo("confianca", 2).build());
        cena410.adicionarEscolha(new Escolha.Builder(
                "Pensar em desistir de tudo.", 415)
                .comConfiancaMaxima(3)
                .comAtributo("confianca", -2).build());
        cenas.add(cena410);

        // CENA 415 — ENTREVISTA PARA BOLSA
        Cena cena415 = new Cena(415, "Capítulo 5A", "Prof. Dr. Sérgio Viana",
                "Fim do semestre.\n\n" +
                        "O orientador te chama à sala. O mesmo lugar em que, meses atrás, " +
                        "você tentou explicar por que queria pesquisar.\n\n" +
                        "'Você entrou como voluntária no começo do semestre. Foram meses de " +
                        "reuniões, planilhas e tarefas sem remuneração. Eu vi.'\n\n" +
                        "Ele fecha uma pasta.\n\n" +
                        "'Tem uma vaga de bolsista para o próximo semestre. É concorrida. " +
                        "Depende do relatório e da minha avaliação.'\n\n" +
                        "Ele te olha, esperando.\n\n" +
                        "'Tem alguma coisa que você queira me dizer antes de eu decidir?' ");
        cena415.adicionarEscolha(new Escolha.Builder(
                "Falar do projeto, do tema e do que quero pesquisar.", 416)
                .comFlagRequerida("cap5a_liderou_artigo")
                .comAtributo("confianca", 2).comScoreGanho(4)
                .comFlagConcedida("entrevista_ic_boa").build());
        cena415.adicionarEscolha(new Escolha.Builder(
                "Falar do trabalho que fiz e do quanto me dediquei.", 416)
                .comScoreGanho(3)
                .comFlagConcedida("entrevista_ic_trabalho").build());
        cena415.adicionarEscolha(new Escolha.Builder(
                "Dizer a verdade: preciso da bolsa.", 416)
                .comAtributo("confianca", 1)
                .comFlagConcedida("entrevista_ic_honesta").build());
        cena415.adicionarEscolha(new Escolha.Builder(
                "Não saber o que dizer e deixar o silêncio responder.", 418)
                .comCustoEnergia(5).build());
        cenas.add(cena415);

        // CENA 416 — RESPOSTA DA ENTREVISTA
        Cena cena416 = new Cena(416, "Capítulo 5A", "Narrador",
                "Ele faz uma anotação no papel. Você tenta descobrir, pelo rosto dele, " +
                        "se aquela conversa mudou alguma coisa. Não consegue.\n\n" +
                        "'Eu vejo semana que vem.'\n\n" +
                        "Você sai da sala com uma resposta que não é resposta.\n\n" +
                        "(Uma semana.)\n\n" +
                        "(Eu não vou conseguir dormir.)");
        cena416.adicionarEscolha(new Escolha.Builder(
                "Sustentar a ideia de pesquisa que apresentei.", 417)
                .comFlagRequerida("entrevista_ic_boa").build());
        cena416.adicionarEscolha(new Escolha.Builder(
                "Esperar pelo reconhecimento do trabalho que fiz.", 417)
                .comFlagRequerida("entrevista_ic_trabalho").build());
        cena416.adicionarEscolha(new Escolha.Builder(
                "Esperar, mesmo depois de admitir que preciso da bolsa.", 417)
                .comFlagRequerida("entrevista_ic_honesta").build());
        // Fallback incondicional para saves antigos ou estado inesperado.
        cena416.adicionarEscolha(new Escolha.Builder(
                "Não insistir numa resposta. Você não controla a decisão dele.", 418)
                .build());
        cenas.add(cena416);

        Cena cena417 = new Cena(417, "Capítulo 5A", "Narrador",
                "Na semana seguinte, chega o e-mail do orientador.\n\n" +
                        "'Aprovada. Bolsa FAPESB. R$ 500 por mês. Começa no próximo semestre.'\n\n" +
                        "Você lê uma vez. Depois outra. A palavra 'aprovada' parece ter sido " +
                        "escrita para outra pessoa e enviada por engano para você.\n\n" +
                        "Você mostra o e-mail pra sua mãe. Ela não entende muito bem o que é IC. " +
                        "Mas entende o que uma renda mensal pode representar.\n\n" +
                        "R$ 500 não pagam o tempo que você investiu. Ainda assim, podem ajudar " +
                        "com as passagens e com parte das despesas. Pela primeira vez em meses, " +
                        "o esforço tem uma consequência concreta.");
        cena417.adicionarEscolha(new Escolha.Builder(
                "Encerrar Capítulo 5A.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap5a_concluido")
                .comFlagConcedida("bolsista_ic").build());
        cenas.add(cena417);

        Cena cena418 = new Cena(418, "Capítulo 5A", "Narrador",
                "Na semana seguinte, chega o e-mail do orientador.\n\n" +
                        "'A vaga foi para outro aluno. Você continua como voluntária por enquanto. " +
                        "Se abrir outra no meio do semestre, eu aviso.'\n\n" +
                        "Você lê até o fim, embora a decisão já esteja clara.\n\n" +
                        "(Não passei.)\n\n" +
                        "(Outra pessoa passou.)\n\n" +
                        "Você fecha o e-mail. Vai ao RU. Amanhã, o trabalho continua — com ou sem bolsa.");
        cena418.adicionarEscolha(new Escolha.Builder(
                "Encerrar Capítulo 5A.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap5a_concluido")
                .comFlagConcedida("ic_voluntaria_de_novo").build());
        cenas.add(cena418);

        return cenas;
    }
}