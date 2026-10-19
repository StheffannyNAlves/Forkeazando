package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;

import java.util.ArrayList;
import java.util.List;

public class Capitulo5B {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();

        Cena cena450 = new Cena(450, "Capítulo 5B — DTEC", "Narrador",
                "Primeiro dia de monitoria. Sala 112.\n\n" +
                        "Doze alunos. Dois já abriram a lista; os outros ainda estão tentando descobrir " +
                        "qual parte da aula precisam rever. Você escreve no quadro a relação entre tensão, " +
                        "resistência e corrente.\n\n" +
                        "Um aluno substitui os valores na fórmula, mas usa a resistência em quilohms como " +
                        "se fosse ohms. O resultado sai mil vezes maior. Você percebe que apontar o erro " +
                        "não basta: precisa fazer a pessoa enxergar de onde veio.\n\n" +
                        "Outra pessoa pergunta se o LED pode ser ligado direto à fonte. Você respira e " +
                        "volta à ideia de limitar a corrente.\n\n" +
                        "Alguém no fundo pergunta: 'Mas isso vai cair na prova?'\n\n" +
                        "(Eu não sou o professor.)\n\n" +
                        "(Mas eu preciso conseguir explicar por que isso importa.)");
        cena450.adicionarEscolha(new Escolha.Builder(
                "Explicar do começo, devagar.", 452)
                .comScoreGanho(4).comCustoEnergia(20)
                .comRelacionamento(PersonagemSecundario.PROFESSOR_DTEC, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.CONHECIMENTO).build());
        cena450.adicionarEscolha(new Escolha.Builder(
                "Perguntar quem já sabe o básico.", 452)
                .comAtributo("carisma", 2).build());
        cena450.adicionarEscolha(new Escolha.Builder(
                "Passar um exercício e deixar a turma tentar.", 452)
                .comCustoEnergia(10).build());
        cenas.add(cena450);

        Cena cena452 = new Cena(452, "Capítulo 5B", "Narrador",
                "Bia está na porta quando você sai.\n\n" +
                        "'Você escolheu DTEC.'\n\n" +
                        "Pausa. Ela olha para o quadro ainda cheio de fórmulas pela janela.\n\n" +
                        "'Boa sorte com a greve.'\n\n" +
                        "Ela fala como quem já sabe de alguma coisa. Ela sempre fala assim: " +
                        "como se a conversa tivesse começado antes de você chegar.");
        cena452.adicionarEscolha(new Escolha.Builder(
                "Perguntar o que ela sabe.", 453)
                .comRelacionamento(PersonagemSecundario.BIA, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE)
                .comFlagConcedida("bia_sabe_greve").build());
        cena452.adicionarEscolha(new Escolha.Builder(
                "Dizer que não estou preocupada.", 453)
                .comRelacionamento(PersonagemSecundario.BIA, -1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cenas.add(cena452);

        Cena cena453 = new Cena(453, "Capítulo 5B", "Beatriz Menezes",
                "'Greve de professor é bonita no jornal.'\n\n" +
                        "Bia guarda o celular no bolso.\n\n" +
                        "'Na prática, as aulas param, o RU pode fechar, a bolsa fica suspensa. " +
                        "E ninguém coloca isso em destaque no cartaz da assembleia.'\n\n" +
                        "Ela não espera você responder. Vai embora, deixando o aviso no ar.");
        cena453.adicionarEscolha(new Escolha.Builder("Anotar o aviso.", 451)
                .comAtributo("confianca", 1).build());
        cena453.adicionarEscolha(new Escolha.Builder("Ignorar.", 451).build());
        cenas.add(cena453);

        Cena cena451 = new Cena(451, "Capítulo 5B", "Narrador",
                "╔══════════════════════════════════════════╗\n" +
                        "║         CHEFE DO SISTEMA                 ║\n" +
                        "║     — O CALENDÁRIO REMANEJADO —          ║\n" +
                        "╚══════════════════════════════════════════╝\n\n" +
                        "Quatro semanas depois, os professores entram em greve. Não é um departamento: " +
                        "é a universidade inteira.\n\n" +
                        "Você recebe avisos fragmentados, cada um por um canal diferente:\n\n" +
                        "— As aulas são suspensas.\n" +
                        "— O RU continuará funcionando por um período reduzido, mas já há data para fechar.\n" +
                        "— A bolsa de monitoria fica suspensa.\n" +
                        "— O auxílio moradia continua em análise.\n" +
                        "— O calendário será remanejado para janeiro.\n\n" +
                        "(Janeiro.)\n\n" +
                        "(EU TÔ DE FÉRIAS EM JANEIRO.)\n\n" +
                        "A assembleia do DCE é segunda-feira, às 14h, no pátio. Ninguém consegue " +
                        "te dizer por quanto tempo aquilo vai durar.");
        cena451.adicionarEscolha(new Escolha.Builder("Ir na assembleia.", 455)
                .comFlagConcedida("cap5b_foi_assembleia")
                .comParticipacaoGanha(5).build());
        cena451.adicionarEscolha(new Escolha.Builder(
                "Continuar dando monitoria por fora.", 455)
                .comFlagConcedida("cap5b_furou_greve")
                .comScoreGanho(5)
                .comRelacionamento(PersonagemSecundario.PROFESSOR_DTEC, -2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.CONHECIMENTO).build());
        cena451.adicionarEscolha(new Escolha.Builder(
                "Ir pra casa. Esperar acabar.", 455)
                .comFlagConcedida("cap5b_neutra")
                .comAtributo("sanidade", 5).build());
        cena451.adicionarEscolha(new Escolha.Builder(
                "Enfrentar tudo. Café e ódio.", 455)
                .comEnergiaAbaixoDe(20)
                .comCustoEnergia(30)
                .comAtributo("confianca", -5)
                .comPermiteEnergiaInsuficiente().build());
        cena451.adicionarEscolha(new Escolha.Builder(
                "Você enfrenta a greve com calma. Já passou por coisa pior.", 455)
                .comSanidadeMinima(6)
                .comAtributo("confianca", 2).build());
        cenas.add(cena451);

        Cena cena455 = new Cena(455, "Capítulo 5B", "Dandara Oliveira",
                "Três semanas depois do início da greve, você encontra Dandara na fila do RU. " +
                        "É uma das últimas antes do fechamento anunciado.\n\n" +
                        "Ela segura uma marmita com as duas mãos.\n\n" +
                        "'Já são três semanas. Minha bolsa tá suspensa.'\n\n" +
                        "Pausa.\n\n" +
                        "'Minha mãe mandou comida de Feira de Santana. Porque, quando o RU fechar, " +
                        "eu não tenho oito reais pra gastar na cantina todo dia.'\n\n" +
                        "Ela abre a marmita. Arroz com ovo. O cheiro se mistura ao barulho da fila, " +
                        "que avança devagar.\n\n" +
                        "Dandara olha para a comida, depois para você. Não pede nada. Isso torna " +
                        "a situação mais difícil, não menos.");
        cena455.adicionarEscolha(new Escolha.Builder("Oferecer pra dividir.", 460)
                .comAtributo("confianca", 2)
                .comAtributo("vidaSocial", 2)
                .comFlagConcedida("cap5b_dividiu_comida")
                .comFlagConcedida("hab_sobreviveu_greve")
                .comRelacionamento(PersonagemSecundario.DANDARA, 4f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena455.adicionarEscolha(new Escolha.Builder("Dizer que a greve é justa.", 460)
                .comAtributo("vidaSocial", -1)
                .comFlagConcedida("hab_sobreviveu_greve")
                .comRelacionamento(PersonagemSecundario.DANDARA, -3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena455.adicionarEscolha(new Escolha.Builder("Ficar quieta.", 460)
                .comFlagConcedida("hab_sobreviveu_greve")
                .comRelacionamento(PersonagemSecundario.DANDARA, 1f).build());
        cena455.adicionarEscolha(new Escolha.Builder(
                "Você já está passando metade do pão antes que ela termine a frase.", 460)
                .comVidaSocialMinima(5)
                .comAtributo("confianca", 2)
                .comAtributo("vidaSocial", 2)
                .comFlagConcedida("cap5b_dividiu_comida")
                .comFlagConcedida("hab_sobreviveu_greve")
                .comRelacionamento(PersonagemSecundario.DANDARA, 4f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cenas.add(cena455);

        Cena cena460 = new Cena(460, "Capítulo 5B", "Narrador",
                "A greve termina sessenta e cinco dias depois de começar. O calendário oficial " +
                        "é publicado de novo, com aulas e avaliações remanejadas para janeiro.\n\n" +
                        "Você abre o calendário, fecha, e abre outra vez. As datas mudaram. " +
                        "Os compromissos não desapareceram; só foram empurrados para outro mês.\n\n" +
                        "Você não sabe se ganhou ou perdeu. Sabe que precisou reorganizar a vida " +
                        "em torno de uma decisão que nunca esteve nas suas mãos.");
        cena460.adicionarEscolha(new Escolha.Builder(
                "Encerrar Capítulo 5B.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap5b_concluido").build());
        cenas.add(cena460);

        return cenas;
    }
}