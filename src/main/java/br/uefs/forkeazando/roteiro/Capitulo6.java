package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;

import java.util.ArrayList;
import java.util.List;

public class Capitulo6 {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();

        Cena cena550 = new Cena(550, "Capítulo 6 — A Crise", "Narrador",
                "╔══════════════════════════════════════════╗\n" +
                        "║         CHEFE DO SISTEMA                 ║\n" +
                        "║         — O QUARTO FECHADO —             ║\n" +
                        "║                                          ║\n" +
                        "║  Sem antagonista.                        ║\n" +
                        "║  Só você.                                ║\n" +
                        "╚══════════════════════════════════════════╝\n\n" +
                        "Quarta-feira, 14h.\n\n" +
                        "Você não foi na aula. Não foi na monitoria. Não respondeu nenhuma " +
                        "mensagem. Não abriu o notebook em cinco dias.\n\n" +
                        "Você tá no quarto. Celular no silencioso. Olhando pro teto.\n\n" +
                        "(Quando foi que eu parei?)\n\n" +
                        "(Semana passada eu tava bem.)\n\n" +
                        "(Eu acho.)");
        cena550.adicionarEscolha(new Escolha.Builder(
                "Levantar. Tomar banho. Comer alguma coisa.", 551)
                .comAtributo("confianca", 2).comCustoEnergia(10).build());
        cena550.adicionarEscolha(new Escolha.Builder("Continuar deitada.", 552)
                .comAtributo("confianca", -2).build());
        cena550.adicionarEscolha(new Escolha.Builder("Ligar pro Cáio.", 553)
                .comRelacionamento(PersonagemSecundario.VETERANO, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena550.adicionarEscolha(new Escolha.Builder(
                "Você já passou por isso antes.", 552)
                .comConfiancaMaxima(2)
                .comAtributo("confianca", -1)
                .comFlagConcedida("cap6_afundou").build());
        cena550.adicionarEscolha(new Escolha.Builder(
                "Uma mensagem chega. 'Você tá bem?'", 555)
                .comVidaSocialMinima(5)
                .comAtributo("confianca", 2).build());
        cena550.adicionarEscolha(new Escolha.Builder("Você levanta. Sem drama.", 551)
                .comConfiancaMinima(6)
                .comAtributo("confianca", 3)
                .comCustoEnergia(10).build());
        cena550.adicionarEscolha(new Escolha.Builder(
                "Você não levanta. Você só quer ficar.", 552)
                .comSanidadeMaxima(2)
                .comAtributo("confianca", -3)
                .comFlagConcedida("cap6_crise_profunda").build());
        cena550.adicionarEscolha(new Escolha.Builder(
                "Você levanta. Você já passou por isso.", 551)
                .comSanidadeMinima(7)
                .comAtributo("confianca", 3)
                .comCustoEnergia(10).build());
        cena550.adicionarEscolha(new Escolha.Builder(
                "Ninguém mandou mensagem. Ninguém notou.", 552)
                .comParticipacaoMaxima(3)
                .comAtributo("confianca", -2).build());
        cenas.add(cena550);

        Cena cena551 = new Cena(551, "Capítulo 6", "Narrador",
                "Você levanta.\n\n" +
                        "O banho ajuda. O arroz que você fez ajuda mais ainda.\n\n" +
                        "Abre o notebook pela primeira vez em cinco dias.\n\n" +
                        "47 e-mails. Você lê três.\n\n" +
                        "Um é da PRAE. Sobre 'acompanhamento pedagógico'.\n\n" +
                        "(É o primeiro e-mail da PRAE em seis meses.)");
        cena551.adicionarEscolha(new Escolha.Builder("Responder. Pedir ajuda.", 555)
                .comAtributo("confianca", 2)
                .comFlagConcedida("cap6_pediu_ajuda").build());
        cena551.adicionarEscolha(new Escolha.Builder(
                "Arquivar. Você vai resolver sozinha.", 555)
                .comFlagConcedida("cap6_sozinha").build());
        cenas.add(cena551);

        Cena cena552 = new Cena(552, "Capítulo 6", "Narrador",
                "Você fica deitada. O dia passa. O sol bate na parede. Depois some.\n\n" +
                        "No dia seguinte, alguém bate na porta.\n\n" +
                        "É o Cáio.\n\n" +
                        "Ele não pergunta se você tá bem. Ele não fala nada.\n\n" +
                        "Ele só entra. Senta no chão do quarto.\n\n" +
                        "Fica lá.\n\n" +
                        "Uma hora.\n\n" +
                        "Duas horas.\n\n" +
                        "Ninguém fala nada.");
        cena552.adicionarEscolha(new Escolha.Builder("Falar. Contar tudo.", 555)
                .comRelacionamento(PersonagemSecundario.VETERANO, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE)
                .comAtributo("confianca", 2).build());
        cena552.adicionarEscolha(new Escolha.Builder("Ficar quieta.", 555)
                .comAtributo("confianca", 1).build());
        cenas.add(cena552);

        Cena cena553 = new Cena(553, "Capítulo 6", "Cáio Andrade",
                "Ele atende no terceiro toque.\n\n" +
                        "'Oi.'\n\n" +
                        "Pausa.\n\n" +
                        "'Tá tudo bem?'\n\n" +
                        "Você não responde.\n\n" +
                        "Ele não insiste.\n\n" +
                        "Você escuta ele respirando do outro lado. Uma cadeira arrastando. " +
                        "Um copo batendo na mesa.\n\n" +
                        "'Tô indo aí.'\n\n" +
                        "Desliga.");
        cena553.adicionarEscolha(new Escolha.Builder("Esperar.", 555)
                .comRelacionamento(PersonagemSecundario.VETERANO, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE)
                .comAtributo("confianca", 2).build());
        cenas.add(cena553);

        Cena cena555 = new Cena(555, "Capítulo 6", "Beatriz Menezes",
                "Você tá sentada no RU. Sem fome. O frango com quiabo esfriou no " +
                        "prato há vinte minutos.\n\n" +
                        "Bia senta do seu lado com a marmita dela. Não pergunta nada.\n\n" +
                        "Abre a marmita.\n\n" +
                        "Arroz com ovo. De novo.\n\n" +
                        "Ela fica em silêncio uns cinco minutos. Depois fala, sem olhar " +
                        "pra você:\n\n" +
                        "'Eu já passei por isso. No terceiro semestre.'\n\n" +
                        "Pausa.\n\n" +
                        "'Ninguém me falou nada. Eu só fiquei.'");
        cena555.adicionarEscolha(new Escolha.Builder("Perguntar como ela saiu.", 556)
                .comRelacionamento(PersonagemSecundario.BIA, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena555.adicionarEscolha(new Escolha.Builder("Ficar quieta.", 556)
                .comRelacionamento(PersonagemSecundario.BIA, 1f).build());
        cenas.add(cena555);

        Cena cena556 = new Cena(556, "Capítulo 6", "Beatriz Menezes",
                "Ela pensa antes de responder. Como se estivesse escolhendo as " +
                        "palavras.\n\n" +
                        "'Eu não saí. Eu só aprendi a andar devagar.'\n\n" +
                        "Pausa.\n\n" +
                        "'Você vai aprender também.'\n\n" +
                        "Ela te oferece metade do pão. Você aceita.");
        cena556.adicionarEscolha(new Escolha.Builder("Ficar com ela até terminar.", 554)
                .comAtributo("confianca", 1)
                .comAtributo("vidaSocial", 1).build());
        cenas.add(cena556);

        Cena cena554 = new Cena(554, "Capítulo 6", "Narrador",
                "Você tá de pé de novo.\n\n" +
                        "Não 100%. Mas de pé.\n\n" +
                        "O semestre ainda não acabou. Tem prova em três semanas. Tem " +
                        "trabalho pra entregar. Tem gente te esperando.\n\n" +
                        "Você tem uma escolha: continuar, ou parar de vez.");
        cena554.adicionarEscolha(new Escolha.Builder("Continuar.", 557)
                .comAtributo("confianca", 1)
                .comFlagConcedida("cap6_continuou").build());
        cena554.adicionarEscolha(new Escolha.Builder("Trancar o semestre.", 557)
                .comAtributo("confianca", 1)
                .comAtributo("vidaSocial", 1)
                .comFlagConcedida("cap6_trancou").build());
        cena554.adicionarEscolha(new Escolha.Builder("Enfrentar tudo. Café e ódio.", 557)
                .comEnergiaAbaixoDe(20)
                .comCustoEnergia(-30)
                .comAtributo("confianca", -5).build());
        cena554.adicionarEscolha(new Escolha.Builder(
                "Você tranca. Ninguém vai sentir sua falta.", 557)
                .comParticipacaoMaxima(3)
                .comFlagConcedida("cap6_trancou")
                .comAtributo("sanidade", 3).build());
        cenas.add(cena554);

        Cena cena557 = new Cena(557, "Capítulo 6", "Cáio Andrade",
                "Você vai embora do RU e encontra o Cáio na porta.\n\n" +
                        "Copo térmico amassado, como sempre.\n\n" +
                        "Ele tá parado, olhando pra rua como quem tá esperando alguém.\n\n" +
                        "Ele te vê.\n\n" +
                        "Hesita.\n\n" +
                        "'Eu preciso te contar uma coisa.'");
        cena557.adicionarEscolha(new Escolha.Builder("Deixa ele contar.", 558)
                .comRelacionamento(PersonagemSecundario.VETERANO, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena557.adicionarEscolha(new Escolha.Builder("Diz que não precisa.", 558)
                .comFlagConcedida("caio_nao_contou").build());
        cenas.add(cena557);

        Cena cena558 = new Cena(558, "Capítulo 6", "Cáio Andrade",
                "Ele olha pro copo. Ele não bebe.\n\n" +
                        "'Eu não reprovei Cálculo duas vezes.'\n\n" +
                        "Pausa longa.\n\n" +
                        "'Eu desisti.'\n\n" +
                        "Pausa.\n\n" +
                        "'Na segunda tentativa, eu saí no meio da prova. Ninguém sabe.'\n\n" +
                        "Pausa.\n\n" +
                        "'Eu deixo todo mundo achar que reprovei, porque é mais fácil.'\n\n" +
                        "Pausa.\n\n" +
                        "'Reprovado todo mundo entende. Desistiu, ninguém entende.'\n\n" +
                        "(Cinco anos.)\n\n" +
                        "(Ele tá aqui há cinco anos.)");
        cena558.adicionarEscolha(new Escolha.Builder("Por que ele voltou?", 559)
                .comRelacionamento(PersonagemSecundario.VETERANO, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena558.adicionarEscolha(new Escolha.Builder("Ele tá bem agora?", 559)
                .comAtributo("confianca", 2)
                .comRelacionamento(PersonagemSecundario.VETERANO, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena558.adicionarEscolha(new Escolha.Builder(
                "Não falar nada. Só ficar do lado dele.", 559)
                .comRelacionamento(PersonagemSecundario.VETERANO, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cenas.add(cena558);

        Cena cena559 = new Cena(559, "Capítulo 6", "Cáio Andrade",
                "Ele dá um gole. Dessa vez ele bebe. Um gole longo.\n\n" +
                        "'Eu voltei porque não sei fazer outra coisa.'\n\n" +
                        "Pausa.\n\n" +
                        "'E porque uma menina me disse uma vez que se eu ainda tava aqui, " +
                        "era porque alguma coisa valia.'\n\n" +
                        "Pausa.\n\n" +
                        "'Eu esqueci o nome dela. Mas eu lembro da frase.'\n\n" +
                        "Ele te olha.\n\n" +
                        "(Eu disse essa frase.)\n\n" +
                        "(No primeiro semestre.)\n\n" +
                        "(Eu nem lembrava mais.)");
        cena559.adicionarEscolha(new Escolha.Builder("Fui eu.", 560)
                .comRelacionamento(PersonagemSecundario.VETERANO, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE)
                .comFlagConcedida("caio_revelacao").build());
        cena559.adicionarEscolha(new Escolha.Builder("Não dizer nada.", 560)
                .comFlagConcedida("caio_nao_soube").build());
        cenas.add(cena559);

        Cena cena560 = new Cena(560, "Capítulo 6", "Narrador",
                "Você tá de pé de novo. O pior passou.\n\n" +
                        "Ou não.\n\n" +
                        "Difícil saber.\n\n" +
                        "Mas você ainda tá aqui.");
        cena560.adicionarEscolha(new Escolha.Builder("Continuar na IC.", 561)
                .comFlagRequerida("trilha_ic").build());
        cena560.adicionarEscolha(new Escolha.Builder("Continuar no DTEC.", 562)
                .comFlagRequerida("trilha_dtec").build());
        cena560.adicionarEscolha(new Escolha.Builder("Continuar sozinha.", 563)
                .comFlagRequerida("trilha_indep").build());
        cena560.adicionarEscolha(new Escolha.Builder(
                "Seguir em frente. Você não tem trilha. Você tem você.", 564)
                .build());
        cenas.add(cena560);

        Cena cena561 = new Cena(561, "Capítulo 6", "Prof. Dr. Sérgio Viana",
                "[TRILHA IC]\n\n" +
                        "Você volta pro laboratório.\n\n" +
                        "Ele tá cansado — como todo mundo nessa época do semestre.\n\n" +
                        "'Tem uma proposta nova. Projeto menor. Sem empresa, sem verba " +
                        "paralela. Só pesquisa.'\n\n" +
                        "Pausa.\n\n" +
                        "'Honesta.'");
        cena561.adicionarEscolha(new Escolha.Builder("Aceitar. Recomeçar limpo.", 564)
                .comRelacionamento(PersonagemSecundario.ORIENTADOR_IC, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.RECONHECIMENTO)
                .comFlagConcedida("cap6_ic_recomeco").build());
        cena561.adicionarEscolha(new Escolha.Builder("Recusar.", 564)
                .comFlagConcedida("cap6_ic_recusou").build());
        cenas.add(cena561);

        Cena cena562 = new Cena(562, "Capítulo 6", "Prof. Rafael Meira",
                "[TRILHA DTEC]\n\n" +
                        "Meira tá ensinando um aluno do primeiro semestre a segurar o " +
                        "multímetro direito. O menino tá com a mão tremendo.\n\n" +
                        "Ele te vê.\n\n" +
                        "'Você sumiu.'\n\n" +
                        "Pausa.\n\n" +
                        "'A greve acabou. O semestre recomeçou. Tem vaga pra monitor no " +
                        "próximo. Você quer?'");
        cena562.adicionarEscolha(new Escolha.Builder("Aceitar.", 564)
                .comRelacionamento(PersonagemSecundario.PROFESSOR_DTEC, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.CONHECIMENTO)
                .comFlagConcedida("cap6_dtec_recomeco").build());
        cena562.adicionarEscolha(new Escolha.Builder("Pedir um tempo.", 564)
                .comFlagConcedida("cap6_dtec_tempo").build());
        cenas.add(cena562);

        Cena cena563 = new Cena(563, "Capítulo 6", "Zé do Módulo 8",
                "[TRILHA INDEPENDENTE]\n\n" +
                        "O celular apita. É o Zé.\n\n" +
                        "'Aquele cliente que te enrolou voltou.'\n\n" +
                        "Pausa.\n\n" +
                        "'Diz que quer fechar projeto novo. Maior. Pagando metade adiantado.'\n\n" +
                        "Pausa.\n\n" +
                        "'Eu não sei se é furada. Você decide.'");
        cena563.adicionarEscolha(new Escolha.Builder("Aceitar.", 564)
                .comFlagConcedida("cap6_indep_recomeco").build());
        cena563.adicionarEscolha(new Escolha.Builder("Recusar.", 564)
                .comFlagConcedida("cap6_indep_recusou").build());
        cenas.add(cena563);

        Cena cena564 = new Cena(564, "Capítulo 6", "Narrador",
                "O semestre acaba.\n\n" +
                        "Não com pompa. Não com nota alta.\n\n" +
                        "Acaba só porque o calendário acabou.\n\n" +
                        "Você tá viva.\n\n" +
                        "Isso conta.");
        cena564.adicionarEscolha(new Escolha.Builder("Encerrar Capítulo 6.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap6_concluido").build());
        cenas.add(cena564);

        return cenas;
    }
}