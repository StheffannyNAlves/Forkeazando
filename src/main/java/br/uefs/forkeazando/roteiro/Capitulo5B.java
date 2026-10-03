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
                        "Doze alunos. Dois sabem por que tão ali. Os outros dez foram " +
                        "porque a disciplina é obrigatória e o horário bateu com o deles.\n\n" +
                        "Você escreve no quadro a primeira coisa que o professor te pediu.\n\n" +
                        "Alguém no fundo pergunta:\n\n" +
                        "'Vai cair na prova?'\n\n" +
                        "(Eu não sei.)\n\n" +
                        "(Eu não sou o professor.)\n\n" +
                        "(Eu tenho vinte anos.)");
        cena450.adicionarEscolha(new Escolha.Builder("Explicar do começo, devagar.", 452)
                .comScoreGanho(4).comCustoEnergia(20)
                .comRelacionamento(PersonagemSecundario.PROFESSOR_DTEC, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.CONHECIMENTO).build());
        cena450.adicionarEscolha(new Escolha.Builder("Perguntar quem já sabe o básico.", 452)
                .comAtributo("carisma", 2).build());
        cena450.adicionarEscolha(new Escolha.Builder("Passar exercício e deixar eles tentarem.", 452)
                .comCustoEnergia(10).build());
        cenas.add(cena450);

        Cena cena452 = new Cena(452, "Capítulo 5B", "Narrador",
                "Bia tá na porta quando você sai.\n\n" +
                        "'Você escolheu DTEC.'\n\n" +
                        "Pausa.\n\n" +
                        "'Boa sorte com a greve.'\n\n" +
                        "Ela fala como quem já sabe de algo. Ela sempre fala assim.");
        cena452.adicionarEscolha(new Escolha.Builder("Perguntar o que ela sabe.", 453)
                .comRelacionamento(PersonagemSecundario.BIA, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE)
                .comFlagConcedida("bia_sabe_greve").build());
        cena452.adicionarEscolha(new Escolha.Builder("Dizer que não tô preocupada.", 453)
                .comRelacionamento(PersonagemSecundario.BIA, -1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cenas.add(cena452);

        Cena cena453 = new Cena(453, "Capítulo 5B", "Beatriz Menezes",
                "'Greve de professor é bonito no jornal.'\n\n" +
                        "Pausa.\n\n" +
                        "'Na prática, o RU fecha, a bolsa suspende, e ninguém fala isso " +
                        "na assembleia.'\n\n" +
                        "Ela vai embora.");
        cena453.adicionarEscolha(new Escolha.Builder("Anotar o aviso.", 451)
                .comAtributo("confianca", 1).build());
        cena453.adicionarEscolha(new Escolha.Builder("Ignorar.", 451).build());
        cenas.add(cena453);

        Cena cena451 = new Cena(451, "Capítulo 5B", "Narrador",
                "╔══════════════════════════════════════════╗\n" +
                        "║         CHEFE DO SISTEMA                 ║\n" +
                        "║     — O CALENDÁRIO REMANEJADO —          ║\n" +
                        "╚══════════════════════════════════════════╝\n\n" +
                        "Quatro semanas depois, os professores entram em greve.\n\n" +
                        "Não é greve de um departamento. É da universidade inteira.\n\n" +
                        "Você descobre da pior forma:\n\n" +
                        "— As aulas param.\n" +
                        "— O RU fecha.\n" +
                        "— A bolsa de Monitoria fica suspensa.\n" +
                        "— O auxílio moradia fica em análise.\n" +
                        "— O calendário é remanejado pra janeiro.\n\n" +
                        "(Janeiro.)\n\n" +
                        "(EU TÔ DE FÉRIAS EM JANEIRO.)\n\n" +
                        "A assembleia do DCE é segunda, 14h, no pátio.");
        cena451.adicionarEscolha(new Escolha.Builder("Ir na assembleia.", 455)
                .comFlagConcedida("cap5b_foi_assembleia")
                .comParticipacaoGanha(5).build());
        cena451.adicionarEscolha(new Escolha.Builder("Continuar dando monitoria por fora.", 455)
                .comFlagConcedida("cap5b_furou_greve")
                .comScoreGanho(5)
                .comRelacionamento(PersonagemSecundario.PROFESSOR_DTEC, -2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.CONHECIMENTO).build());
        cena451.adicionarEscolha(new Escolha.Builder("Ir pra casa. Esperar acabar.", 455)
                .comFlagConcedida("cap5b_neutra")
                .comAtributo("sanidade", 5).build());
        cena451.adicionarEscolha(new Escolha.Builder("Enfrentar tudo. Café e ódio.", 455)
                .comEnergiaAbaixoDe(20)
                .comCustoEnergia(-30)
                .comAtributo("confianca", -5).build());
        cena451.adicionarEscolha(new Escolha.Builder(
                "Você enfrenta a greve com calma. Já passou por coisa pior.", 455)
                .comSanidadeMinima(6)
                .comAtributo("confianca", 2).build());
        cenas.add(cena451);

        Cena cena455 = new Cena(455, "Capítulo 5B", "Dandara Oliveira",
                "Dandara te encontra na fila do RU — a última antes de fechar.\n\n" +
                        "Ela tá com uma marmita.\n\n" +
                        "'A greve durou três semanas. Minha bolsa tá suspensa.'\n\n" +
                        "Pausa.\n\n" +
                        "'Eu tô comendo marmita que minha mãe mandou de Feira de Santana. " +
                        "Porque o RU fechou e eu não tenho R$ 8 pra cantina todo dia.'\n\n" +
                        "Ela abre a marmita.\n\n" +
                        "Arroz com ovo.\n\n" +
                        "(Três semanas.)");
        cena455.adicionarEscolha(new Escolha.Builder("Oferecer pra dividir.", 460)
                .comAtributo("confianca", 2)
                .comAtributo("vidaSocial", 2)
                .comFlagConcedida("cap5b_dividiu_comida")
                .comRelacionamento(PersonagemSecundario.DANDARA, 4f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena455.adicionarEscolha(new Escolha.Builder("Dizer que a greve é justa.", 460)
                .comAtributo("vidaSocial", -1)
                .comRelacionamento(PersonagemSecundario.DANDARA, -3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena455.adicionarEscolha(new Escolha.Builder("Ficar quieta.", 460)
                .comRelacionamento(PersonagemSecundario.DANDARA, 1f).build());
        cena455.adicionarEscolha(new Escolha.Builder(
                "Você já tá passando metade do pão antes dela terminar a frase.", 460)
                .comVidaSocialMinima(5)
                .comAtributo("confianca", 2)
                .comAtributo("vidaSocial", 2)
                .comFlagConcedida("cap5b_dividiu_comida")
                .comRelacionamento(PersonagemSecundario.DANDARA, 4f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cenas.add(cena455);

        Cena cena460 = new Cena(460, "Capítulo 5B", "Narrador",
                "A greve acaba. Sessenta e cinco dias depois.\n\n" +
                        "O calendário é remanejado. Você vai ter aula em janeiro.\n\n" +
                        "Você não sabe se ganhou ou perdeu.\n\n" +
                        "Você só sabe que tá diferente.");
        cena460.adicionarEscolha(new Escolha.Builder("Encerrar Capítulo 5B.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap5b_concluido")
                .comFlagConcedida("hab_sobreviveu_greve").build());
        cenas.add(cena460);

        return cenas;
    }
}