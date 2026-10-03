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
                        "A formatura custa R$ 1.200.\n\n" +
                        "Beca alugada. Convite. Jantar. Foto. A taxa da empresa que organiza.\n\n" +
                        "Você tem R$ 400 na conta.\n\n" +
                        "Sua mãe ofereceu o cartão dela.\n\n" +
                        "Dois colegas seus não vão na cerimônia.\n\n" +
                        "Um deles disse que 'formatura é pra quem tem dinheiro'.\n\n" +
                        "(Ele tá certo.)");
        cena649.adicionarEscolha(new Escolha.Builder("Aceitar o cartão.", 650)
                .comFlagConcedida("cap8_foi_formatura").build());
        cena649.adicionarEscolha(new Escolha.Builder("Não ir. Festa em casa.", 650)
                .comAtributo("sanidade", 5)
                .comFlagConcedida("cap8_sem_cerimonia").build());
        cena649.adicionarEscolha(new Escolha.Builder("Vender o ingresso.", 650)
                .comFlagConcedida("cap8_vendeu_ingresso").build());
        cena649.adicionarEscolha(new Escolha.Builder("Enfrentar tudo. Café e ódio.", 650)
                .comEnergiaAbaixoDe(20)
                .comCustoEnergia(-30)
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
                "Você não vai. Não quer plateia.", 650)
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
        cena650.adicionarEscolha(new Escolha.Builder("Procurar Cáio.", 651).build());
        cena650.adicionarEscolha(new Escolha.Builder("Procurar Dandara.", 652).build());
        cena650.adicionarEscolha(new Escolha.Builder("Só olhar pro teto.", 653)
                .comAtributo("sanidade", 5).build());
        cenas.add(cena650);

        Cena cena651 = new Cena(651, "Capítulo 8", "Cáio Andrade",
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

        Cena cena654 = new Cena(654, "Capítulo 8 — Inserção", "Narrador",
                "[1 ANO DEPOIS]\n\n" +
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
                "Você fala da Dandara. Da greve. Da vez que não desistiu.", 655)
                .comFlagRequerida("hab_sobreviveu_greve")
                .comAtributo("confianca", 3)
                .comFlagConcedida("cap8_resposta_verdadeira").build());
        cenas.add(cena654);

        Cena cena655 = new Cena(655, "Capítulo 8", "Narrador",
                "Você tá na porta do prédio.\n\n" +
                        "Daqui pra frente é outra coisa.\n\n" +
                        "Não tem edital.\n\n" +
                        "Não tem mural.\n\n" +
                        "Não tem prova.\n\n" +
                        "Só o que você escolheu ser.");
        cena655.adicionarEscolha(new Escolha.Builder("Encerrar.", 660).build());
        cenas.add(cena655);

        Cena cena660 = new Cena(660, "Capítulo 8", "Narrador", "FIM.");
        cena660.adicionarEscolha(new Escolha.Builder("Encerrar o jogo.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap8_concluido")
                .comFlagConcedida("final_formou").build());
        cenas.add(cena660);

        return cenas;
    }
}