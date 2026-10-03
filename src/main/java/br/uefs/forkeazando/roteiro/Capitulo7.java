package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;

import java.util.ArrayList;
import java.util.List;

public class Capitulo7 {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();

        Cena cena600 = new Cena(600, "Capítulo 7 — A Última Prova", "Narrador",
                "Último semestre.\n\n" +
                        "TCC.\n\n" +
                        "Você tá cansada. Não é o cansaço de dormir mal. É o cansaço de " +
                        "cinco anos.\n\n" +
                        "Você abre o notebook.\n\n" +
                        "Documento em branco.\n\n" +
                        "(Título.)\n\n" +
                        "(Eu preciso escrever o título.)\n\n" +
                        "(Por onde se começa isso?)");
        cena600.adicionarEscolha(new Escolha.Builder("Uma página por dia.", 601)
                .comCustoEnergia(15).build());
        cena600.adicionarEscolha(new Escolha.Builder("Tudo de uma vez.", 601)
                .comCustoEnergia(40).build());
        cena600.adicionarEscolha(new Escolha.Builder("Começar amanhã.", 601)
                .comFlagConcedida("cap7_procrastinou").build());
        cena600.adicionarEscolha(new Escolha.Builder("Enfrentar tudo. Café e ódio.", 601)
                .comEnergiaAbaixoDe(20)
                .comCustoEnergia(-30)
                .comAtributo("confianca", -5).build());
        cena600.adicionarEscolha(new Escolha.Builder(
                "Você senta. Escreve. Só tem trabalho.", 601)
                .comSanidadeMinima(6)
                .comCustoEnergia(20)
                .comAtributo("confianca", 2).build());
        cena600.adicionarEscolha(new Escolha.Builder(
                "Dividir em capítulos. Um por semana.", 601)
                .comFlagRequerida("perfil_perfeccionista")
                .comCustoEnergia(20)
                .comAtributo("confianca", 2).build());
        cena600.adicionarEscolha(new Escolha.Builder("Escrever na véspera.", 601)
                .comFlagRequerida("perfil_pratico")
                .comCustoEnergia(35)
                .comAtributo("confianca", -1).build());
        cena600.adicionarEscolha(new Escolha.Builder(
                "Você senta. Abre o documento. Escreve a primeira frase.", 601)
                .comConfiancaMinima(6)
                .comCustoEnergia(20)
                .comAtributo("confianca", 1).build());
        cena600.adicionarEscolha(new Escolha.Builder(
                "Abre. Fecha. Abre de novo.", 601)
                .comConfiancaMaxima(3)
                .comAtributo("confianca", -1)
                .comFlagConcedida("cap7_procrastinou").build());
        cenas.add(cena600);

        Cena cena601 = new Cena(601, "Capítulo 7", "Prof. Dr. Sérgio Viana",
                "[TRILHA IC]\n\n" +
                        "╔══════════════════════════════════════════╗\n" +
                        "║      — A AUTORIA DO ARTIGO —             ║\n" +
                        "╚══════════════════════════════════════════╝\n\n" +
                        "Você escreveu o artigo. Sessenta páginas. Sozinha.\n\n" +
                        "O orientador vai colocar o nome dele primeiro na publicação.\n\n" +
                        "Isso é normal na academia. Todo orientador faz isso.\n\n" +
                        "Ele te chama na sala pra confirmar.\n\n" +
                        "'Você entende como funciona, né?'");
        cena601.adicionarEscolha(new Escolha.Builder(
                "Aceitar. Ele é o orientador. É assim mesmo.", 605)
                .comAtributo("confianca", -3)
                .comFlagConcedida("cap7_aceitou_autoria")
                .comRelacionamento(PersonagemSecundario.ORIENTADOR_IC, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.RECONHECIMENTO).build());
        cena601.adicionarEscolha(new Escolha.Builder(
                "Perguntar se ele leu o artigo antes de assinar.", 605)
                .comAtributo("confianca", 3)
                .comFlagConcedida("cap7_questionou_autoria")
                .comRelacionamento(PersonagemSecundario.ORIENTADOR_IC, -3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.RECONHECIMENTO).build());
        cena601.adicionarEscolha(new Escolha.Builder("Ficar quieta.", 605).build());
        cena601.adicionarEscolha(new Escolha.Builder(
                "Lembrar do Cáio. Ele te disse uma vez pra escolher uma coisa só.", 605)
                .comFlagRequerida("mentoria_caio")
                .comAtributo("confianca", 4)
                .comFlagConcedida("cap7_questionou_autoria")
                .comRelacionamento(PersonagemSecundario.ORIENTADOR_IC, -3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.RECONHECIMENTO).build());
        cenas.add(cena601);

        Cena cena602 = new Cena(602, "Capítulo 7", "Narrador",
                "[TRILHA DTEC]\n\n" +
                        "╔══════════════════════════════════════════╗\n" +
                        "║      — O ALUNO QUE NÃO APRENDEU —        ║\n" +
                        "╚══════════════════════════════════════════╝\n\n" +
                        "Você é monitora de novo.\n\n" +
                        "Último semestre.\n\n" +
                        "Um aluno seu vai reprovar.\n\n" +
                        "Ele veio te pedir ajuda na véspera da prova final.\n\n" +
                        "'Você pode me dar uma nota? Só uma. Eu não consigo passar.'\n\n" +
                        "Pausa.\n\n" +
                        "'Eu preciso dessa matéria pra não perder a bolsa.'\n\n" +
                        "(Ele precisa.)\n\n" +
                        "(Eu sei que ele precisa.)\n\n" +
                        "(Mas ele não aprendeu.)");
        cena602.adicionarEscolha(new Escolha.Builder(
                "Dar a nota. Ele precisa mais que o sistema.", 605)
                .comAtributo("confianca", -3)
                .comFlagConcedida("cap7_dtec_deu_nota")
                .comRelacionamento(PersonagemSecundario.PROFESSOR_DTEC, -3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.CONHECIMENTO).build());
        cena602.adicionarEscolha(new Escolha.Builder(
                "Não dar. Ele reprova, mas aprende.", 605)
                .comAtributo("confianca", 3)
                .comFlagConcedida("cap7_dtec_nao_deu")
                .comRelacionamento(PersonagemSecundario.PROFESSOR_DTEC, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.CONHECIMENTO).build());
        cena602.adicionarEscolha(new Escolha.Builder(
                "Dar uma última aula. Só isso.", 605)
                .comAtributo("confianca", 4)
                .comFlagConcedida("cap7_dtec_ultima_aula")
                .comRelacionamento(PersonagemSecundario.PROFESSOR_DTEC, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.CONHECIMENTO).build());
        cenas.add(cena602);

        Cena cena603 = new Cena(603, "Capítulo 7", "Narrador",
                "[TRILHA INDEPENDENTE]\n\n" +
                        "╔══════════════════════════════════════════╗\n" +
                        "║       — O CONTRATO BOM DEMAIS —          ║\n" +
                        "╚══════════════════════════════════════════╝\n\n" +
                        "Cliente novo. Paga cinco mil. Metade adiantado. Projeto pequeno.\n\n" +
                        "Você pesquisou. O cara tem CNPJ. Tem site. Tem cliente grande.\n\n" +
                        "Mas o contrato tem uma cláusula. Você não entendeu bem.\n\n" +
                        "(Não é golpe.)\n\n" +
                        "(Eu acho.)\n\n" +
                        "(É?)\n\n" +
                        "Você não sabe.");
        cena603.adicionarEscolha(new Escolha.Builder("Aceitar.", 605)
                .comAtributo("confianca", -2)
                .comFlagConcedida("cap7_indep_aceitou").build());
        cena603.adicionarEscolha(new Escolha.Builder("Recusar.", 605)
                .comAtributo("confianca", 3)
                .comFlagConcedida("cap7_indep_recusou").build());
        cena603.adicionarEscolha(new Escolha.Builder("Ver com advogado.", 605)
                .comAtributo("logica", 3)
                .comFlagConcedida("cap7_indep_advogado").build());
        cena603.adicionarEscolha(new Escolha.Builder(
                "Você lê a cláusula três vezes. Entende.", 605)
                .comLogicaMinima(6)
                .comFlagConcedida("cap7_indep_advogado")
                .comAtributo("confianca", 2).build());
        cena603.adicionarEscolha(new Escolha.Builder(
                "Você convence o cliente a tirar a cláusula.", 605)
                .comCarismaMinima(6)
                .comFlagConcedida("cap7_indep_recusou")
                .comAtributo("confianca", 3).build());
        cenas.add(cena603);

        Cena cena605 = new Cena(605, "Capítulo 7", "Narrador",
                "Você entregou o TCC.\n\n" +
                        "A defesa foi numa sala com três professores e um projetor que não " +
                        "funcionou na primeira tentativa.\n\n" +
                        "Você passou.\n\n" +
                        "Você não sentiu o que achou que ia sentir.\n\n" +
                        "(Era isso?)\n\n" +
                        "(Cinco anos. Pra isso.)");
        cena605.adicionarEscolha(new Escolha.Builder("Encerrar Capítulo 7.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap7_concluido").build());
        cenas.add(cena605);

        return cenas;
    }
}