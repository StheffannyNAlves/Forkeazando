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
                        "O orientador te apresenta como 'a nova'. Ninguém te pergunta seu nome.\n\n" +
                        "A reunião dura quarenta minutos. Ele fala de prazo, de cronograma, " +
                        "de artigo atrasado. Você entende metade.\n\n" +
                        "Você anota tudo.");
        cena400.adicionarEscolha(new Escolha.Builder(
                "Falar meu nome e o que eu sei fazer.", 401)
                .comAtributo("carisma", 2)
                .comRelacionamento(PersonagemSecundario.ORIENTADOR_IC, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.RECONHECIMENTO).build());
        cena400.adicionarEscolha(new Escolha.Builder(
                "Ficar quieta e anotar. Você vai provar pelo trabalho.", 401)
                .comCustoEnergia(10).build());
        cena400.adicionarEscolha(new Escolha.Builder(
                "Você senta no canto. Ninguém nota que você chegou.", 401)
                .comParticipacaoMaxima(3)
                .comCustoEnergia(5).build());
        cenas.add(cena400);

        Cena cena402 = new Cena(402, "Capítulo 5A", "Narrador",
                "Bia tá no laboratório, com o mesmo fichário de sempre.\n\n" +
                        "'Achei que você fosse pra DTEC. Ainda bem que não.'\n\n" +
                        "Ela tá com um Excel aberto. Planilha de dados de um experimento " +
                        "que você não sabe qual é.");
        cena402.adicionarEscolha(new Escolha.Builder(
                "Perguntar o que ela faz aqui.", 403)
                .comRelacionamento(PersonagemSecundario.BIA, 1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena402.adicionarEscolha(new Escolha.Builder(
                "Perguntar se ela ainda vende resposta.", 403)
                .comFlagRequerida("cap3_colou_de_fato")
                .comRelacionamento(PersonagemSecundario.BIA, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE)
                .comFlagConcedida("bia_ainda_vende").build());
        cena402.adicionarEscolha(new Escolha.Builder(
                "Não falar nada. Você não precisa de aliada aqui.", 403)
                .comRelacionamento(PersonagemSecundario.BIA, -1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cenas.add(cena402);

        Cena cena403 = new Cena(403, "Capítulo 5A", "Beatriz Menezes",
                "'Eu não faço pesquisa. Eu faço o trabalho que ninguém quer.'\n\n" +
                        "Ela aponta pro Excel.\n\n" +
                        "'Planilha, transcrição, revisão de referência. Dois semestres nisso.'\n\n" +
                        "Pausa.\n\n" +
                        "'Você vai acabar fazendo também. Todo mundo faz.'");
        cena403.adicionarEscolha(new Escolha.Builder("Digo que entendo.", 401)
                .comRelacionamento(PersonagemSecundario.BIA, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena403.adicionarEscolha(new Escolha.Builder(
                "Digo que não é o que eu vim fazer.", 401)
                .comRelacionamento(PersonagemSecundario.BIA, -1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cenas.add(cena403);

        Cena cena401 = new Cena(401, "Capítulo 5A", "Narrador",
                "Três semanas depois.\n\n" +
                        "A reunião de terça foi cancelada. A de quarta também.\n\n" +
                        "O orientador mandou uma mensagem no grupo:\n\n" +
                        "'Semana que vem a gente retoma. Tô com prazo de artigo.'\n\n" +
                        "Ninguém respondeu no grupo.\n\n" +
                        "É a terceira vez no mês.");
        cena401.adicionarEscolha(new Escolha.Builder(
                "Mandar mensagem perguntando o que eu faço enquanto isso.", 405)
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
                "Você acha o cronograma do projeto no drive. " +
                        "Última atualização: quatro meses atrás.", 405)
                .comLogicaMinima(5)
                .comFlagConcedida("ic_sabe_cronograma")
                .comAtributo("confianca", 1).build());
        cenas.add(cena401);

        Cena cena405 = new Cena(405, "Capítulo 5A", "Narrador",
                "Sexta-feira o orientador aparece.\n\n" +
                        "Vinte minutos de reunião. Ele distribui tarefas, marca prazos, " +
                        "e no fim diz:\n\n" +
                        "'Vocês vão escrever um artigo. Cada um pega uma parte. " +
                        "Eu reviso no fim.'\n\n" +
                        "Ele sai antes de alguém perguntar qualquer coisa.\n\n" +
                        "Os mestrandos olham uns pros outros. Ninguém sabe o que fazer.");
        cena405.adicionarEscolha(new Escolha.Builder(
                "Assumir a coordenação. Alguém tem que fazer.", 410)
                .comScoreGanho(6).comCustoEnergia(25)
                .comAtributo("confianca", 2)
                .comFlagConcedida("cap5a_liderou_artigo").build());
        cena405.adicionarEscolha(new Escolha.Builder(
                "Pegar a parte mais fácil. Você não quer se queimar.", 410)
                .comScoreGanho(3).comCustoEnergia(10).build());
        cena405.adicionarEscolha(new Escolha.Builder(
                "Perguntar quem vai revisar no fim. Silêncio no grupo.", 410)
                .comFlagConcedida("cap5a_perguntou_revisao").build());
        cena405.adicionarEscolha(new Escolha.Builder(
                "Enfrentar tudo. Café e ódio.", 410)
                .comEnergiaAbaixoDe(20)
                .comCustoEnergia(-30)
                .comAtributo("confianca", -5).build());
        cenas.add(cena405);

        Cena cena410 = new Cena(410, "Capítulo 5A", "Narrador",
                "Dois meses depois, o artigo não tá pronto.\n\n" +
                        "Você escreveu sua parte. Entregou. Os mestrandos entregaram as deles.\n\n" +
                        "O orientador não revisou. Não respondeu. Não mandou nada no grupo.\n\n" +
                        "O prazo da revista passou semana passada.\n\n" +
                        "Ninguém avisou.");
        cena410.adicionarEscolha(new Escolha.Builder(
                "Aguentar. Você fez sua parte.", 415)
                .comAtributo("sanidade", 3).build());
        cena410.adicionarEscolha(new Escolha.Builder(
                "Pedir pra sair do grupo.", 415)
                .comFlagConcedida("cap5a_saiu_grupo").build());
        cena410.adicionarEscolha(new Escolha.Builder(
                "Você entende que pesquisa é isso. Muita coisa não sai.", 415)
                .comSanidadeMinima(6)
                .comAtributo("confianca", 2).build());
        cena410.adicionarEscolha(new Escolha.Builder(
                "Você pensa em desistir de tudo.", 415)
                .comConfiancaMaxima(3)
                .comAtributo("confianca", -2).build());
        cenas.add(cena410);

        // ─── CENA 415 — ENTREVISTA PARA BOLSA ────────────────────────
        Cena cena415 = new Cena(415, "Capítulo 5A", "Prof. Dr. Sérgio Viana",
                "Fim do semestre.\n\n" +
                        "O orientador te chama na sala.\n\n" +
                        "'Você entrou como voluntária em março. Trabalhou cinco meses " +
                        "sem bolsa. Eu vi.'\n\n" +
                        "Pausa.\n\n" +
                        "'Tem uma vaga de bolsista no próximo semestre. " +
                        "Concorrida. Depende do seu relatório. Depende da minha " +
                        "avaliação.'\n\n" +
                        "Ele te olha.\n\n" +
                        "'Tem alguma coisa que você queira me dizer antes de eu decidir?'");
        cena415.adicionarEscolha(new Escolha.Builder(
                "Falo do projeto. Do tema. Do que eu quero pesquisar.", 416)
                .comFlagRequerida("cap5a_liderou_artigo")
                .comAtributo("confianca", 2).comScoreGanho(4)
                .comFlagConcedida("entrevista_ic_boa").build());
        cena415.adicionarEscolha(new Escolha.Builder(
                "Falo do que eu fiz. Do quanto eu trabalhei.", 416)
                .comScoreGanho(3)
                .comFlagConcedida("entrevista_ic_trabalho").build());
        cena415.adicionarEscolha(new Escolha.Builder(
                "Falo que preciso da bolsa. Simples assim.", 416)
                .comAtributo("confianca", 1)
                .comFlagConcedida("entrevista_ic_honesta").build());
        cena415.adicionarEscolha(new Escolha.Builder(
                "Não sei o que dizer. Fico quieta.", 418)   // vai direto pra 418
                .comCustoEnergia(5).build());
        cenas.add(cena415);

// Chegou aqui → jogador disse algo → tem exatamente uma das três flags
        Cena cena416 = new Cena(416, "Capítulo 5A", "Narrador",
                "Ele faz uma anotação no papel.\n\n" +
                        "'Eu vejo semana que vem.'\n\n" +
                        "Você sai da sala.\n\n" +
                        "(Uma semana.)\n\n" +
                        "(Eu não vou conseguir dormir.)");
        cena416.adicionarEscolha(new Escolha.Builder("Continuar.", 417)
                .comFlagRequerida("entrevista_ic_boa").build());
        cena416.adicionarEscolha(new Escolha.Builder("Continuar.", 417)
                .comFlagRequerida("entrevista_ic_trabalho").build());
        cena416.adicionarEscolha(new Escolha.Builder("Continuar.", 417)
                .comFlagRequerida("entrevista_ic_honesta").build());
        cenas.add(cena416);

        Cena cena417 = new Cena(417, "Capítulo 5A", "Narrador",
                "Semana seguinte. Email do orientador.\n\n" +
                        "'Aprovada. Bolsa FAPESB. R$ 500 por mês. " +
                        "Começa no próximo semestre.'\n\n" +
                        "(R$ 500.)\n\n" +
                        "(Cinquenta a mais que o ônibus.)\n\n" +
                        "Você mostra o email pra sua mãe. Ela não entende muito bem " +
                        "o que é IC. Mas entende 'R$ 500'.");
        cena417.adicionarEscolha(new Escolha.Builder(
                "Encerrar Capítulo 5A.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap5a_concluido")
                .comFlagConcedida("bolsista_ic").build());
        cenas.add(cena417);

        Cena cena418 = new Cena(418, "Capítulo 5A", "Narrador",
                "Semana seguinte. Email do orientador.\n\n" +
                        "'A vaga foi pra outro aluno. Você continua como voluntária. " +
                        "Se abrir outra no meio do semestre, eu aviso.'\n\n" +
                        "(Não passou.)\n\n" +
                        "(Outro aluno passou.)\n\n" +
                        "Você fecha o email. Vai pro RU.");
        cena418.adicionarEscolha(new Escolha.Builder(
                "Encerrar Capítulo 5A.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap5a_concluido")
                .comFlagConcedida("ic_voluntaria_de_novo").build());
        cenas.add(cena418);
        return cenas;
    }
}