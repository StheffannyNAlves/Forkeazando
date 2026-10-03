package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;

import java.util.ArrayList;
import java.util.List;

public class Capitulo4 {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();

        Cena cena300 = new Cena(300, "Capítulo 4 — A Bifurcação", "Narrador",
                "Segunda-feira, 8h. Mural do módulo 1.\n\n" +
                        "Dois editais novos, colados um do lado do outro. Literalmente " +
                        "colados com fita crepe por cima da fita antiga do semestre passado.\n\n" +
                        "Ninguém tirou o de antes. Ninguém vai tirar o de agora.\n\n" +
                        "O primeiro: PIBIC. Iniciação Científica. R$ 700 se for CNPq, " +
                        "R$ 500 se for FAPESB. Vinte e duas páginas. Exige Lattes.\n\n" +
                        "O segundo: Monitoria de DTEC. R$ 600. Exige CR acima de 7.\n\n" +
                        "As duas provas são na mesma semana.\n\n" +
                        "(Isso não é coincidência.)");
        cena300.adicionarEscolha(new Escolha.Builder("Ler os dois editais inteiros.", 301)
                .comCustoEnergia(15)
                .comFlagConcedida("cap4_leu_editais").build());
        cena300.adicionarEscolha(new Escolha.Builder("Perguntar no grupo do WhatsApp.", 301)
                .comRelacionamento(PersonagemSecundario.DANDARA, 1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena300.adicionarEscolha(new Escolha.Builder("Procurar o Cáio.", 301)
                .comFlagConcedida("cap4_procurou_caio").build());
        cenas.add(cena300);

        Cena cena301 = new Cena(301, "Capítulo 4", "Cáio Andrade",
                "Ele tá no mesmo lugar. Mesmo copo. Mesma cara de quem dormiu 4h " +
                        "e não lembra a última vez que comeu algo que não fosse salgado de cantina.\n\n" +
                        "'Os dois editais são de propósito. Eles querem que você escolha.'\n\n" +
                        "Pausa.\n\n" +
                        "'E se você não escolher, você fica sem os dois. É assim todo semestre.'\n\n" +
                        "Ele dá um gole.\n\n" +
                        "'IC é política. DTEC é greve. Os dois são bons. Nenhum dos dois é fácil.'");
        cena301.adicionarEscolha(new Escolha.Builder("Qual ele escolheria?", 302)
                .comFlagConcedida("cap4_conselho_caio")
                .comRelacionamento(PersonagemSecundario.VETERANO, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena301.adicionarEscolha(new Escolha.Builder("Tem terceira opção?", 302)
                .comFlagConcedida("cap4_perguntou_alternativa").build());
        cena301.adicionarEscolha(new Escolha.Builder("Agradecer e resolver sozinha.", 302)
                .comRelacionamento(PersonagemSecundario.VETERANO, -1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cenas.add(cena301);

        Cena cena302 = new Cena(302, "Capítulo 4", "Narrador",
                "Colegiado.\n\n" +
                        "Você pergunta sobre os editais.\n\n" +
                        "A funcionária responde com quatro siglas. Duas você nunca ouviu.\n\n" +
                        "Depois diz:\n\n" +
                        "'Tá no ÁGATA.'\n\n" +
                        "O ÁGATA tá fora do ar.\n\n" +
                        "(Claro.)\n\n" +
                        "(CLARO.)");
        cena302.adicionarEscolha(new Escolha.Builder("Insistir.", 303)
                .comAtributo("carisma", 2).build());
        cena302.adicionarEscolha(new Escolha.Builder("Agradecer e sair.", 303)
                .comCustoEnergia(5).build());
        cenas.add(cena302);

        // CENA 303 — ORIENTADOR IC
        Cena cena303 = new Cena(303, "Capítulo 4", "Prof. Dr. Sérgio Viana",
                "A sala do grupo tem cheiro de café velho. Três quadros brancos com " +
                        "anotações de semestres atrás. Ninguém apagou. Ninguém vai apagar.\n\n" +
                        "O orientador te olha por cima dos óculos.\n\n" +
                        "'Você quer entrar na IC? Por quê?'\n\n" +
                        "Não é pergunta de educação. Ele tá medindo se você sabe responder " +
                        "sob pressão.");
        cena303.adicionarEscolha(new Escolha.Builder(
                "Porque quero aprender a pesquisar. Mesmo que dê trabalho.", 304)
                .comAtributo("confianca", 1)
                .comRelacionamento(PersonagemSecundario.ORIENTADOR_IC, 3f).build());
        cena303.adicionarEscolha(new Escolha.Builder(
                "Porque quero a bolsa. (Pausa.) E porque quero aprender também.", 304)
                .comRelacionamento(PersonagemSecundario.ORIENTADOR_IC, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.RECONHECIMENTO).build());
        cena303.adicionarEscolha(new Escolha.Builder(
                "Porque quero mudar o mundo com tecnologia.", 304)
                .comAtributo("confianca", -1)
                .comRelacionamento(PersonagemSecundario.ORIENTADOR_IC, -2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.RECONHECIMENTO).build());
        cenas.add(cena303);

        // CENA 304 — PROF. MEIRA
        Cena cena304 = new Cena(304, "Capítulo 4", "Prof. Rafael Meira",
                "Ele te recebe no laboratório. Protoboard numa mão, multímetro no " +
                        "bolso da calça. Não senta. Você também não senta.\n\n" +
                        "'Monitoria não é aula de reforço. É você ensinar quem sabe menos.'\n\n" +
                        "Pausa. Ele te olha fixo.\n\n" +
                        "'Você aguenta explicar a mesma coisa cinco vezes sem perder a paciência?'");
        cena304.adicionarEscolha(new Escolha.Builder(
                "Aguento. Já ensinei colega antes, na escola.", 3045)
                .comRelacionamento(PersonagemSecundario.PROFESSOR_DTEC, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.CONHECIMENTO).build());
        cena304.adicionarEscolha(new Escolha.Builder(
                "Não sei. Mas quero tentar.", 3045)
                .comAtributo("confianca", 1)
                .comRelacionamento(PersonagemSecundario.PROFESSOR_DTEC, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.CONHECIMENTO).build());
        cena304.adicionarEscolha(new Escolha.Builder(
                "Aguento. Eu sou bem paciente.", 3045)
                .comRelacionamento(PersonagemSecundario.PROFESSOR_DTEC, 1f).build());
        cenas.add(cena304);

        // O SISTEMA ENTREVISTA A PROTAGONISTA

        // O SISTEMA APARECE
        Cena cena3045 = new Cena(3045, "Capítulo 4", "Sistema Acadêmico",
                "Você tá voltando pra casa.\n\n" +
                        "O celular apita. Não é WhatsApp. Não é email.\n\n" +
                        "É uma notificação do ÁGATA.\n\n" +
                        "'Avaliação de Perfil Acadêmico — 4 perguntas — obrigatório'\n\n" +
                        "Botão único: 'Começar'.\n\n" +
                        "(Obrigatório.)\n\n" +
                        "(Eu tô na rua.)\n\n" +
                        "Você clica em começar.");
        cena3045.adicionarEscolha(new Escolha.Builder("Começar.", 3046).build());
        cenas.add(cena3045);

        Cena cena3046 = new Cena(3046, "Capítulo 4", "Sistema Acadêmico",
                "'Pergunta 1 de 4.'\n\n" +
                        "'Imagine o seguinte cenário.'\n\n" +
                        "'Você tem um problema novo pra resolver. Você tem duas opções: " +
                        "passar um mês estudando o assunto a fundo antes de tocar em nada, " +
                        "ou começar a mexer por tentativa e erro até descobrir o que funciona.'\n\n" +
                        "'Qual das duas te descreve melhor?'\n\n" +
                        "(Isso é avaliação de perfil?)\n\n" +
                        "(Pra que isso serve?)");
        cena3046.adicionarEscolha(new Escolha.Builder(
                "Estudar primeiro. Sem entender, eu não mexo.", 3047)
                .comFlagConcedida("sistema_q1_estudar").build());
        cena3046.adicionarEscolha(new Escolha.Builder(
                "Mexer primeiro. Eu aprendo quebrando.", 3047)
                .comFlagConcedida("sistema_q1_mexer").build());
        cenas.add(cena3046);


        Cena cena3047 = new Cena(3047, "Capítulo 4", "Sistema Acadêmico",
                "'Pergunta 2 de 4.'\n\n" +
                        "'Você prefere trabalhar sozinha ou em grupo?'\n\n" +
                        "(Nenhuma das duas.)\n\n" +
                        "(Depende.)\n\n" +
                        "O formulário não tem campo pra 'depende'. Só tem duas opções.");
        cena3047.adicionarEscolha(new Escolha.Builder(
                "Sozinha. Cada um por si.", 3048)
                .comFlagConcedida("sistema_q2_sozinha").build());
        cena3047.adicionarEscolha(new Escolha.Builder(
                "Em grupo. É mais rápido.", 3048)
                .comFlagConcedida("sistema_q2_grupo").build());
        cenas.add(cena3047);

        Cena cena3048 = new Cena(3048, "Capítulo 4", "Sistema Acadêmico",
                "'Pergunta 3 de 4.'\n\n" +
                        "'Seu nome publicado num artigo científico. Numa banca de TCC. " +
                        "Numa placa de laboratório.'\n\n" +
                        "'Isso te interessa?'\n\n" +
                        "(Uma placa de laboratório?)\n\n" +
                        "(Isso é específico demais.)");
        cena3048.adicionarEscolha(new Escolha.Builder(
                "Sim. Quero ver meu nome em algum lugar.", 3049)
                .comFlagConcedida("sistema_q3_visivel").build());
        cena3048.adicionarEscolha(new Escolha.Builder(
                "Não. Isso não muda o que eu faço.", 3049)
                .comFlagConcedida("sistema_q3_invisivel").build());
        cenas.add(cena3048);

        // CENA 3049 — PERGUNTA 4
        Cena cena3049 = new Cena(3049, "Capítulo 4", "Sistema Acadêmico",
                "'Última pergunta.'\n\n" +
                        "'Você prefere saber exatamente o que vai fazer amanhã, ou " +
                        "descobrir na hora?'\n\n" +
                        "(O que isso tem a ver com edital?)\n\n" +
                        "O Sistema não explica.");
        cena3049.adicionarEscolha(new Escolha.Builder(
                "Saber. Preciso de plano.", 3050)
                .comFlagConcedida("sistema_q4_planejar").build());
        cena3049.adicionarEscolha(new Escolha.Builder(
                "Descobrir. Plano engessa.", 3050)
                .comFlagConcedida("sistema_q4_improvisar").build());
        cenas.add(cena3049);

        // ═══ CENA 3050 — O CHUTE ═══
        // O texto real do chute é injetado pela CenaView antes da renderização.
        // Esta cena só serve como marcador pra view saber quando mostrar o chute.
        Cena cena3050 = new Cena(3050, "Capítulo 4", "Sistema Acadêmico",
                "'Processando.'\n\n" +
                        "...\n\n" +
                        "...\n\n" +
                        "(O que é isso?)\n\n" +
                        "(O que ele tá fazendo com essas respostas?)");
        cena3050.adicionarEscolha(new Escolha.Builder("Continuar.", 305).build());
        cenas.add(cena3050);

        // CENA 305 — O PRAZO DUPLO
        Cena cena305 = new Cena(305, "Capítulo 4", "Narrador",
                "╔══════════════════════════════════════════╗\n" +
                        "║         CHEFE DO SISTEMA                 ║\n" +
                        "║        — O PRAZO DUPLO —                 ║\n" +
                        "║                                          ║\n" +
                        "║  Escolha. Uma. Só uma.                   ║\n" +
                        "║  E não tem volta.                        ║\n" +
                        "╚══════════════════════════════════════════╝\n\n" +
                        "Sexta-feira à noite. Você tá em casa.\n\n" +
                        "Os dois editais abertos no ÁGATA. Prazo amanhã às 23h59.\n\n" +
                        "O ÁGATA caiu às 20h.\n\n" +
                        "Você já tentou dez vezes.\n\n" +
                        "(Dez.)\n\n" +
                        "(Eu contei.)");
        cena305.adicionarEscolha(new Escolha.Builder(
                "PIBIC. Quero aprender a pesquisar. E R$ 700 é R$ 700.", 371)
                .comScoreMinimo(20).comParticipacaoMinima(15).build());
        cena305.adicionarEscolha(new Escolha.Builder(
                "DTEC. Quero ensinar. E R$ 600 paga o ônibus.", 372)
                .comScoreMinimo(20).build());
        cena305.adicionarEscolha(new Escolha.Builder(
                "Nenhum dos dois. Vou fazer freela.", 373)
                .comScoreMinimo(7).build());
        cena305.adicionarEscolha(new Escolha.Builder(
                "Nenhum dos dois. Nem sei se quero continuar no curso.", 373)
                .build());
        cena305.adicionarEscolha(new Escolha.Builder(
                "Não consigo decidir. Fecho o notebook e durmo.", 306).build());
        cena305.adicionarEscolha(new Escolha.Builder(
                "Enfrentar tudo. Café e ódio.", 306)
                .comEnergiaAbaixoDe(20)
                .comCustoEnergia(-30)
                .comAtributo("confianca", -5).build());
        cenas.add(cena305);

        Cena cena306 = new Cena(306, "Capítulo 4", "Narrador",
                "Sábado, 23h58.\n\n" +
                        "Você abre o ÁGATA de novo.\n\n" +
                        "Clica em IC. Volta. Clica em DTEC. Volta. Clica em IC de novo. " +
                        "Fecha a aba. Abre de novo.\n\n" +
                        "23h59.\n\n" +
                        "(Eu preciso escolher AGORA.)");
        cena306.adicionarEscolha(new Escolha.Builder("IC.", 371)
                .comScoreMinimo(20).comParticipacaoMinima(15).build());
        cena306.adicionarEscolha(new Escolha.Builder("DTEC.", 372)
                .comScoreMinimo(20).build());
        cena306.adicionarEscolha(new Escolha.Builder("Nenhum.", 373)
                .comScoreMinimo(7).build());
        cena306.adicionarEscolha(new Escolha.Builder("Perdeu o prazo.", 374).build());
        cenas.add(cena306);

        Cena cena371 = new Cena(371, "Capítulo 4", "Narrador",
                "Você preenche o formulário de IC.\n\n" +
                        "'Tem certeza? Esta escolha é definitiva.'\n\n" +
                        "Você clica em sim.\n\n" +
                        "Fecha o notebook.\n\n" +
                        "Fica olhando pro teto por uns dez minutos.\n\n" +
                        "O ventilador do quarto tá fazendo um barulho que você nunca tinha " +
                        "notado antes.");
        cena371.adicionarEscolha(new Escolha.Builder(
                "Encerrar Capítulo 4.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("trilha_ic")
                .comFlagConcedida("cap4_concluido").build());
        cenas.add(cena371);

        Cena cena372 = new Cena(372, "Capítulo 4", "Narrador",
                "Você preenche o formulário de DTEC.\n\n" +
                        "Mesma pergunta de sempre. Você clica em sim.\n\n" +
                        "Você pensa em Dandara.\n\n" +
                        "Você não sabe se ela se inscreveu também.\n\n" +
                        "Você não vai perguntar.");
        cena372.adicionarEscolha(new Escolha.Builder(
                "Encerrar Capítulo 4.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("trilha_dtec")
                .comFlagConcedida("cap4_concluido").build());
        cenas.add(cena372);

        //  CENA 373 ENTRADA INDEPENDENTE
        Cena cena373 = new Cena(373, "Capítulo 4", "Narrador",
                "Você fecha o notebook. Vai tomar banho.\n\n" +
                        "Não se inscreve em nada.\n\n" +
                        "No dia seguinte, tem um terceiro cartaz no mural — de um cara que " +
                        "tá montando um app e precisa de ajuda.\n\n" +
                        "Só um nome e um número. Você anota.");
        cena373.adicionarEscolha(new Escolha.Builder(
                "Encerrar Capítulo 4.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("trilha_indep")
                .comFlagConcedida("cap4_concluido").build());
        cenas.add(cena373);

        // CENA 374 PERDEU O PRAZO
        Cena cena374 = new Cena(374, "Capítulo 4", "Narrador",
                "Você perdeu o prazo.\n\n" +
                        "Segunda-feira o mural tá sem os editais. Alguém já tirou. " +
                        "Ninguém colou nada no lugar.\n\n" +
                        "Você fica olhando pro espaço vazio onde os dois papéis estavam. " +
                        "Um retângulo de fita crepe velha. Nada mais.\n\n" +
                        "(Quem entrou?)\n\n" +
                        "(Não sei.)\n\n" +
                        "(Eu não pergunto.)");
        cena374.adicionarEscolha(new Escolha.Builder(
                "Encerrar Capítulo 4.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("trilha_indefinida")
                .comFlagConcedida("trilha_indep")
                .comFlagConcedida("cap4_concluido").build());
        cenas.add(cena374);

        return cenas;
    }
}