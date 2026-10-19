package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;

import java.util.ArrayList;
import java.util.List;

/**
 * Cenas do esgotamento (energia zerada). O CenasController leva o jogador para a 970 quando a
 * energia chega a 0 e, ao sair (destino CENA_RETOMAR), devolve parte da energia e retoma o caminho.
 * Textos provisórios: reescreva no seu tom.
 */
public class Apagao {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();

        Cena cena970 = new Cena(Estado.CENA_APAGAO, "Esgotamento", "Narrador",
                "A energia chegou a zero.\n\n" +
                        "Não é dramático. Você só percebe que leu a mesma linha três vezes, " +
                        "que o barulho da sala ficou longe e que o celular está escorregando " +
                        "da sua mão.\n\n" +
                        "(Acho que eu preciso parar.)");

        cena970.adicionarEscolha(new Escolha.Builder(
                "Caio aparece do seu lado com um copo de café. Não pergunta nada.", 971)
                .comRelacionamentoMinimo(PersonagemSecundario.VETERANO, 3f)
                .comRelacionamento(PersonagemSecundario.VETERANO, 1f).build());
        cena970.adicionarEscolha(new Escolha.Builder(
                "Malu percebe antes de você. Pega suas anotações e fala com o professor.", 971)
                .comRelacionamentoMinimo(PersonagemSecundario.MALU, 3f)
                .comRelacionamento(PersonagemSecundario.MALU, 1f).build());
        cena970.adicionarEscolha(new Escolha.Builder(
                "Dandara te segura pelo braço antes de você levantar rápido demais.", 971)
                .comRelacionamentoMinimo(PersonagemSecundario.DANDARA, 3f)
                .comRelacionamento(PersonagemSecundario.DANDARA, 1f).build());
        cena970.adicionarEscolha(new Escolha.Builder(
                "Alguém do seu grupo percebe e avisa o professor por você.", 971)
                .comVidaSocialMinima(5).build());
        cena970.adicionarEscolha(new Escolha.Builder(
                "Ninguém percebe. Você fica sentada até a sala esvaziar.", 972)
                .comVidaSocialMaxima(4).build());
        cenas.add(cena970);

        Cena cena971 = new Cena(Estado.CENA_APAGAO + 1, "Esgotamento", "Narrador",
                "Você apaga por alguns minutos. Quando volta, alguém ficou.\n\n" +
                        "Não resolve o semestre. Mas o dia não foi perdido.\n\n" +
                        "Você aceita o que não dá para fazer hoje e deixa para depois.");
        cena971.adicionarEscolha(new Escolha.Builder(
                "Descansar um pouco e voltar.", Estado.CENA_RETOMAR)
                .comAtributo("sanidade", 1)
                .comFlagConcedida("apagao_com_apoio").build());
        cenas.add(cena971);

        Cena cena972 = new Cena(Estado.CENA_APAGAO + 2, "Esgotamento", "Narrador",
                "Quando você levanta, a aula já acabou. A porta está aberta, o quadro " +
                        "apagado, e o aviso do prazo passou sem você ver.\n\n" +
                        "Não é falta de esforço. Você só chegou ao fim do que tinha.");
        cena972.adicionarEscolha(new Escolha.Builder(
                "Recolher suas coisas e seguir.", Estado.CENA_RETOMAR)
                .comScoreGanho(-3)
                .comParticipacaoGanha(-2)
                .comAtributo("confianca", -1)
                .comFlagConcedida("apagao_sozinha").build());
        cenas.add(cena972);

        return cenas;
    }
}