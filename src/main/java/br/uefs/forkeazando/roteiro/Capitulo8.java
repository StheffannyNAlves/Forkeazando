package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;

import java.util.ArrayList;
import java.util.List;

public class Capitulo8 {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();

        Cena cena649 = new Cena(649, "Capítulo 8 — Formatura", "Narrador",
                "╔══════════════════════════════════════════╗\n" +
                        "║         CHEFE FINAL                      ║\n" +
                        "║       — O BOLETO DA FORMATURA —          ║\n" +
                        "║                                          ║\n" +
                        "║  Você venceu o curso.                    ║\n" +
                        "║  Não venceu o boleto.                    ║\n" +
                        "╚══════════════════════════════════════════╝\n\n" +
                        "O pacote completo da formatura custa R$ 1.200.\n\n" +
                        "Beca alugada. Convite. Jantar. Foto. A taxa da empresa que organiza.\n\n" +
                        "Você tem R$ 400 na conta.\n\n" +
                        "Você já tinha reservado um convite individual quando o valor parecia " +
                        "possível. Só depois descobriu que ele não cobria a beca, o jantar nem " +
                        "as fotos. Ainda dá para transferi-lo a outra pessoa e recuperar parte " +
                        "do dinheiro.\n\n" +
                        "Sua mãe ofereceu o cartão dela.\n\n" +
                        "Dois colegas seus não vão na cerimônia.\n\n" +
                        "Um deles disse que 'formatura é pra quem tem dinheiro'.\n\n" +
                        "(Ele tá certo.)\n\n" +
                        "{{CALENDARIO_CAP8}}");
        cena649.adicionarEscolha(new Escolha.Builder("Aceitar o cartão.", 650)
                .comFlagConcedida("cap8_foi_formatura").build());
        cena649.adicionarEscolha(new Escolha.Builder(
                "Não ir. Fazer uma comemoração pequena em casa.", 656)
                .comAtributo("sanidade", 5)
                .comFlagConcedida("cap8_sem_cerimonia").build());
        cena649.adicionarEscolha(new Escolha.Builder(
                "Transferir o convite e recuperar parte do dinheiro.", 656)
                .comFlagConcedida("cap8_vendeu_ingresso").build());
        cena649.adicionarEscolha(new Escolha.Builder("Enfrentar tudo. Café e ódio.", 650)
                .comEnergiaAbaixoDe(20)
                .comCustoEnergia(30)
                .comPermiteEnergiaInsuficiente()
                .comFlagConcedida("cap8_foi_formatura")
                .comAtributo("confianca", -5).build());
        cena649.adicionarEscolha(new Escolha.Builder(
                "Você economizou cinco anos. Você vai.", 650)
                .comFlagRequerida("origem_apertada")
                .comFlagConcedida("cap8_foi_formatura")
                .comAtributo("confianca", 3).build());
        cena649.adicionarEscolha(new Escolha.Builder(
                "Você vai. De cabeça erguida.", 650)
                .comSanidadeMinima(6)
                .comFlagConcedida("cap8_foi_formatura")
                .comAtributo("confianca", 3).build());
        cena649.adicionarEscolha(new Escolha.Builder("Tem gente que você quer ver.", 650)
                .comVidaSocialMinima(6)
                .comFlagConcedida("cap8_foi_formatura")
                .comAtributo("confianca", 2).build());
        cena649.adicionarEscolha(new Escolha.Builder(
                "Você não vai. Não quer plateia.", 656)
                .comConfiancaMaxima(2)
                .comFlagConcedida("cap8_sem_cerimonia")
                .comAtributo("sanidade", 5).build());
        cenas.add(cena649);

        Cena cena650 = new Cena(650, "Capítulo 8", "Narrador",
                "Auditório com ar-condicionado quebrado.\n\n" +
                        "Você tá com a beca alugada. O gorro não cabe direito.\n\n" +
                        "Sua mãe tá na terceira fila, chorando, tirando foto com um celular " +
                        "de lente trincada.\n\n" +
                        "A cerimônia acaba rápido. Você não vai ver todo mundo depois.");
        cena650.adicionarEscolha(new Escolha.Builder("Procurar Caio.", 651).build());
        cena650.adicionarEscolha(new Escolha.Builder("Procurar Dandara.", 652).build());
        cena650.adicionarEscolha(new Escolha.Builder("Só olhar pro teto.", 653)
                .comAtributo("sanidade", 5).build());
        cenas.add(cena650);

        Cena cena651 = new Cena(651, "Capítulo 8", "Caio Andrade",
                "Ele tá no fundo do auditório. Sem beca. Não se formou.\n\n" +
                        "Mas ele veio.\n\n" +
                        "Depois da cerimônia, ele te encontra na porta.\n\n" +
                        "Sem falar nada, ele te estende o copo térmico amassado.\n\n" +
                        "'Toma. Já passou por muita coisa. Vai passar por mais.'");
        cena651.adicionarEscolha(new Escolha.Builder("Aceitar.", 653)
                .comFlagConcedida("item_copo_caio")
                .comRelacionamento(PersonagemSecundario.VETERANO, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena651.adicionarEscolha(new Escolha.Builder("Recusar.", 653)
                .comRelacionamento(PersonagemSecundario.VETERANO, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena651.adicionarEscolha(new Escolha.Builder(
                "Devolver a frase dele: 'Se você ainda tá aqui, alguma coisa valia.'", 653)
                .comFlagRequerida("caio_revelacao")
                .comRelacionamento(PersonagemSecundario.VETERANO, 4f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cenas.add(cena651);

        Cena cena652 = new Cena(652, "Capítulo 8", "Dandara Oliveira",
                "Dandara se formou também.\n\n" +
                        "Ela tá com a beca do lado da família.\n\n" +
                        "Ela te vê.\n\n" +
                        "Hesita.\n\n" +
                        "Depois vem.");
        cena652.adicionarEscolha(new Escolha.Builder("Elogiar.", 653)
                .comRelacionamento(PersonagemSecundario.DANDARA, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena652.adicionarEscolha(new Escolha.Builder("Só 'parabéns'.", 653).build());
        cena652.adicionarEscolha(new Escolha.Builder("Onde ela vai trabalhar?", 653)
                .comRelacionamento(PersonagemSecundario.DANDARA, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cenas.add(cena652);

        Cena cena653 = new Cena(653, "Capítulo 8", "Narrador",
                "O paraninfo fala por quinze minutos.\n\n" +
                        "Ele diz 'o futuro é de vocês' quatro vezes.\n\n" +
                        "Ele diz 'mercado de trabalho' seis vezes.\n\n" +
                        "Ele não diz o nome de nenhum aluno.\n\n" +
                        "No fim, todo mundo bate palma e vai tirar foto.");
        cena653.adicionarEscolha(new Escolha.Builder("Ir tirar foto.", 654).build());
        cena653.adicionarEscolha(new Escolha.Builder("Sair antes do fim.", 654)
                .comFlagConcedida("cap8_saiu_antes").build());
        cena653.adicionarEscolha(new Escolha.Builder("Você sai. Ninguém vai notar.", 654)
                .comParticipacaoMaxima(3)
                .comFlagConcedida("cap8_saiu_antes").build());
        cenas.add(cena653);

        Cena cena656 = new Cena(656, "Capítulo 8 — Uma comemoração diferente", "Narrador",
                "A comemoração acontece em casa. Sem beca, sem pacote de fotos e sem " +
                        "o auditório lotado. Tem comida na mesa, mensagens chegando no celular " +
                        "e sua mãe perguntando se você vai sentir falta da cerimônia.\n\n" +
                        "Talvez sinta. Talvez não. O dinheiro pesou na decisão, mas não foi " +
                        "o dinheiro que tornou seus cinco anos reais. Você concluiu o curso. " +
                        "A cerimônia era uma forma de marcar isso, não a única forma possível.");
        cena656.adicionarEscolha(new Escolha.Builder("Seguir para o que vem depois.", 654).build());
        cenas.add(cena656);

        Cena cena654 = new Cena(654, "Capítulo 8 — Inserção", "Narrador",
                "[UM ANO DEPOIS]\n\n" +
                        "A mesma sala. Mesa de vidro.\n\n" +
                        "A pessoa da banca pergunta:\n\n" +
                        "'Por que você quer trabalhar aqui?'\n\n" +
                        "Só que agora você não ensaiou.\n\n" +
                        "Você não sabe o que vai sair da sua boca.");
        cena654.adicionarEscolha(new Escolha.Builder("Porque eu preciso. Simples.", 655)
                .comFlagConcedida("cap8_resposta_pratica").build());
        cena654.adicionarEscolha(new Escolha.Builder("Porque eu escolhi isso.", 655)
                .comFlagConcedida("cap8_resposta_conviccao").build());
        cena654.adicionarEscolha(new Escolha.Builder(
                "Sinceramente? Ainda tô descobrindo.", 655)
                .comFlagConcedida("cap8_resposta_honesta").build());
        cena654.adicionarEscolha(new Escolha.Builder(
                "Você responde sem hesitar.", 655)
                .comCarismaMinima(6)
                .comFlagConcedida("cap8_resposta_conviccao").build());
        cena654.adicionarEscolha(new Escolha.Builder(
                "Você fala da greve. Do RU fechado. De como o semestre mudou.", 655)
                .comFlagRequerida("hab_sobreviveu_greve")
                .comAtributo("confianca", 3)
                .comFlagConcedida("cap8_resposta_verdadeira").build());
        cena654.adicionarEscolha(new Escolha.Builder(
                "Você fala do e-mail da PRAE. Da vez que pediu ajuda.", 655)
                .comFlagRequerida("cap6_pediu_ajuda")
                .comAtributo("confianca", 3)
                .comFlagConcedida("cap8_resposta_verdadeira").build());
        cena654.adicionarEscolha(new Escolha.Builder(
                "Você fala do dia em que perguntou de quem era o artigo.", 655)
                .comFlagRequerida("cap7_questionou_autoria")
                .comAtributo("confianca", 3)
                .comFlagConcedida("cap8_resposta_verdadeira").build());
        cena654.adicionarEscolha(new Escolha.Builder(
                "Você explica como investigou uma falha no circuito: mediu, formulou hipótese e testou a correção.", 655)
                .comFlagRequerida("cap1_depurou_circuito")
                .comAtributo("confianca", 3)
                .comFlagConcedida("cap8_resposta_verdadeira").build());
        cena654.adicionarEscolha(new Escolha.Builder(
                "Você fala sobre registrar método e limitações para que outra pessoa consiga entender o trabalho.", 655)
                .comFlagRequerida("cap7_documentou_limitacoes")
                .comAtributo("confianca", 3)
                .comFlagConcedida("cap8_resposta_verdadeira").build());
        cenas.add(cena654);

        Cena cena655 = new Cena(655, "Capítulo 8", "Narrador",
                "Você para na calçada. O prédio fica para trás, mas não leva tudo com ele.\n\n" +
                        "Você carrega o que aprendeu, o que perdeu, as pessoas que ficaram " +
                        "e as perguntas que ainda não sabe responder.\n\n" +
                        "O diploma não prova que tudo dependeu só de você. Também não apaga " +
                        "o que você fez para chegar até aqui.\n\n" +
                        "A próxima etapa não vem com edital — e você não precisa atravessá-la " +
                        "fingindo que chegou aqui sozinha.");
        cena655.adicionarEscolha(new Escolha.Builder("Decidir o próximo passo, sem fingir que ele é definitivo.", 661).build());
        cenas.add(cena655);

        Cena cena661 = new Cena(661, "Capítulo 8 — Depois daqui", "Narrador",
                "Um ano depois, você não tem a vida resolvida. Tem um diploma, " +
                        "experiências que deram certo e outras que ainda incomodam.\n\n" +
                        "A trilha inicial ajudou a abrir algumas portas, mas não é uma sentença. " +
                        "Você pode aprofundar o que aprendeu, tentar outro contexto ou mudar de direção.\n\n" +
                        "Nenhuma alternativa vem sem custo. Nenhuma é uma prova de que você " +
                        "escolheu melhor do que outra pessoa. O que muda é o tipo de pergunta " +
                        "que você vai enfrentar daqui pra frente.");
        cena661.adicionarEscolha(new Escolha.Builder("Tentar uma pós-graduação ou seguir pesquisando no Brasil.", 662)
                .comFlagConcedida("futuro_pesquisa_brasil").build());
        cena661.adicionarEscolha(new Escolha.Builder("Construir tecnologia aplicada: firmware, hardware ou sistemas.", 663)
                .comFlagConcedida("futuro_tecnologia").build());
        cena661.adicionarEscolha(new Escolha.Builder("Preparar uma candidatura ou colaboração internacional.", 664)
                .comFlagConcedida("futuro_internacional").build());
        cena661.adicionarEscolha(new Escolha.Builder("Construir uma carreira independente, projeto por projeto.", 665)
                .comFlagConcedida("futuro_independente").build());
        cena661.adicionarEscolha(new Escolha.Builder("Mudar de área e aprender o que a nova direção exigir.", 666)
                .comFlagConcedida("futuro_transicao").build());
        cenas.add(cena661);

        Cena cena662 = new Cena(662, "Final — Pesquisa no Brasil", "Narrador",
                "Você procura um grupo de pesquisa no Brasil e começa a formular uma pergunta " +
                        "que seja possível investigar com os recursos disponíveis.\n\n" +
                        "A bolsa não é garantida, o primeiro projeto talvez mude de forma e " +
                        "nem todo resultado confirma a hipótese inicial. Você aprende a documentar " +
                        "o que não funcionou, a dividir autoria com clareza e a pedir revisão.\n\n" +
                        "Ficar no Brasil não transforma seu trabalho em uma versão menor de outro futuro. " +
                        "O valor estará nas perguntas que você consegue sustentar e no conhecimento " +
                        "que outras pessoas poderão usar.");
        cena662.adicionarEscolha(new Escolha.Builder("Levar esse caminho para o resto da história.", 660).build());
        cenas.add(cena662);

        Cena cena663 = new Cena(663, "Final — Tecnologia aplicada", "Narrador",
                "Você entra numa equipe que transforma ideias em sistemas que precisam funcionar " +
                        "fora da apresentação. Pode ser firmware, hardware, sistemas embarcados, " +
                        "segurança ou uma combinação que ainda está aprendendo a nomear.\n\n" +
                        "O protótipo passa no teste e falha no ambiente real. Um defeito simples " +
                        "consome uma semana. Você registra hipóteses, mede, testa e pede outra opinião.\n\n" +
                        "O trabalho não parece épico todos os dias. Às vezes é revisar uma solda, " +
                        "corrigir um erro de integração ou explicar por que a solução precisa de mais tempo. " +
                        "Ainda assim, algo que você construiu passa a funcionar para alguém.");
        cena663.adicionarEscolha(new Escolha.Builder("Levar esse caminho para o resto da história.", 660).build());
        cenas.add(cena663);

        Cena cena664 = new Cena(664, "Final — Além das fronteiras", "Narrador",
                "Você prepara uma candidatura internacional. O processo pede idioma, portfólio, " +
                        "cartas, recursos e paciência. Chegam respostas negativas; uma oportunidade " +
                        "precisa ser adiada porque o orçamento não fecha.\n\n" +
                        "Com o tempo, surge uma possibilidade concreta: estudar fora, trabalhar com " +
                        "uma equipe de outro país ou participar de uma colaboração sem se mudar. " +
                        "Você avalia o custo e a rede de apoio antes de decidir.\n\n" +
                        "O mundo ficou maior, mas não virou uma escala de valor. Você não precisa " +
                        "apagar de onde veio para aprender com outras pessoas nem precisa sair do país " +
                        "para fazer um trabalho relevante.");
        cena664.adicionarEscolha(new Escolha.Builder("Levar esse caminho para o resto da história.", 660).build());
        cenas.add(cena664);

        Cena cena665 = new Cena(665, "Final — Caminho próprio", "Narrador",
                "Você passa a escolher projetos por contrato e aprende que liberdade também tem " +
                        "planilhas, proposta, imposto, cobrança e semanas sem resposta.\n\n" +
                        "No começo, aceita mais do que deveria para não perder cliente. Depois começa " +
                        "a registrar escopo, prazo, pagamento e o que fica fora do combinado. Nem toda " +
                        "negociação termina bem, mas você reconhece mais cedo quando uma promessa " +
                        "está vaga demais.\n\n" +
                        "Você não precisa fazer tudo só. A autonomia cresce quando aprende a procurar " +
                        "parcerias e a dizer não a um trabalho que custa mais do que oferece.");
        cena665.adicionarEscolha(new Escolha.Builder("Levar esse caminho para o resto da história.", 660).build());
        cenas.add(cena665);

        Cena cena666 = new Cena(666, "Final — Recomeçar sem apagar o passado", "Narrador",
                "Você muda de direção. Há fundamentos da graduação que continuam úteis, mas eles " +
                        "não substituem tudo o que a nova área exige. Você identifica lacunas, monta " +
                        "projetos, estuda e talvez aceite começar numa posição menos confortável.\n\n" +
                        "Algumas tentativas não dão em nada. Você ajusta o plano sem transformar cada " +
                        "mudança numa acusação contra a pessoa que era antes.\n\n" +
                        "Os anos anteriores não foram uma promessa que você quebrou. Foram parte da " +
                        "formação que trouxe até aqui — e que não precisa decidir sozinha o que vem depois.");
        cena666.adicionarEscolha(new Escolha.Builder("Levar esse caminho para o resto da história.", 660).build());
        cenas.add(cena666);

        Cena cena660 = new Cena(660, "Capítulo 8", "Narrador", "FIM.");
        cena660.adicionarEscolha(new Escolha.Builder("Encerrar o jogo.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap8_concluido")
                .comFlagConcedida("final_formou").build());
        cenas.add(cena660);

        return cenas;
    }
}