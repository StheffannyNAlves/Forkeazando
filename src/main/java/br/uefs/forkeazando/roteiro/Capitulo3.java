package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;

import java.util.ArrayList;
import java.util.List;

public class Capitulo3 {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();

        Cena cena200 = new Cena(200, "Capítulo 3 — A Primeira Prova Real", "Narrador",
                "Cálculo I. Segunda-feira, 7h.\n\n" +
                        "Você saiu de casa às 5h20. Pegou dois ônibus. O segundo atrasou. " +
                        "Chegou 6h55. A sala tá com quarenta pessoas e pelo menos trinta " +
                        "tão com a cara de quem passou a noite estudando.\n\n" +
                        "O professor entra 7h20 porque 'o trânsito na Getúlio Vargas tá horrível'.\n\n" +
                        "(Todo semestre é isso.)\n\n" +
                        "(TODO semestre.)\n\n" +
                        "Atrás dele, entra o Meira, o da oficina. Fiscal hoje.\n\n" +
                        "Ele te vê. Ele não reage.\n\n" +
                        "(Ele lembra de mim?)\n\n" +
                        "(Ele me viu montar o LED. Ou me viu copiar o esquema?)\n\n" +
                        "Cinco questões. A primeira é uma integral.");
        cena200.adicionarEscolha(new Escolha.Builder("Revisar as fórmulas.", 201)
                .comCustoEnergia(5).build());
        cena200.adicionarEscolha(new Escolha.Builder("Só respirar.", 201)
                .comFlagConcedida("cap3_confiou").build());
        cena200.adicionarEscolha(new Escolha.Builder("Procurar Bia.", 204)
                .comAtributo("confianca", -1).build());
        cena200.adicionarEscolha(new Escolha.Builder(
                "Fazer contato visual com Meira.", 213)
                .comFlagRequerida("item_esquema_copiado")
                .comAtributo("confianca", 1).build());
        cenas.add(cena200);

        Cena cena213 = new Cena(213, "Capítulo 3", "Prof. Rafael Meira",
                "Ele passa perto da sua mesa. Baixo. Sem parar de andar.\n\n" +
                        "'Aquele esquema que você copiou. Tava errado.'\n\n" +
                        "Pausa.\n\n" +
                        "'O do grupo da frente usava resistor de 220. O certo era 330. " +
                        "Se você tivesse queimado o LED, eu ia saber.'\n\n" +
                        "Ele continua andando.\n\n" +
                        "(Ele viu.)\n\n" +
                        "(Ele sempre viu.)");
        cena213.adicionarEscolha(new Escolha.Builder("Terminar a prova.", 201)
                .comAtributo("confianca", 2).build());
        cenas.add(cena213);

        Cena cena201 = new Cena(201, "Capítulo 3", "Narrador",
                "╔══════════════════════════════════════════╗\n" +
                        "║         CHEFE DO SISTEMA                 ║\n" +
                        "║      — A INTEGRAL IMPOSSÍVEL —           ║\n" +
                        "╚══════════════════════════════════════════╝\n\n" +
                        "Cinco questões.\n\n" +
                        "Você olha pra primeira. Integral.\n\n" +
                        "Olha pra segunda. Outra integral. Só que pior.\n\n" +
                        "Olha pra terceira. Você decide não olhar mais.\n\n" +
                        "(Eu não vou passar.)\n\n" +
                        "(Eu SEI que não vou passar.)\n\n" +
                        "Mas tem uma coisa estranha: você não tá com medo.\n\n" +
                        "Você tá cansada.\n\n" +
                        "E cansada é diferente de com medo.");
        cena201.adicionarEscolha(new Escolha.Builder("Fazer o que sabe.", 202)
                .comScoreGanho(4).comCustoEnergia(20)
                .comAtributo("confianca", 2)
                .comFlagConcedida("cap3_tentou").build());
        cena201.adicionarEscolha(new Escolha.Builder("Olhar de lado.", 205)
                .comFlagConcedida("cap3_colou").build());
        cena201.adicionarEscolha(new Escolha.Builder("Entregar em branco.", 207)
                .comAtributo("confianca", -2)
                .comFlagConcedida("cap3_desistiu").build());
        cena201.adicionarEscolha(new Escolha.Builder(
                "Respirar fundo. Você leu o datasheet do professor.", 202)
                .comFlagRequerida("hab_foco_total")
                .comScoreGanho(6).comCustoEnergia(15)
                .comAtributo("confianca", 3).build());
        cena201.adicionarEscolha(new Escolha.Builder(
                "Enfrentar tudo. Café e ódio.", 202)
                .comEnergiaAbaixoDe(20)
                .comCustoEnergia(-30)
                .comAtributo("confianca", -5).build());
        cena201.adicionarEscolha(new Escolha.Builder(
                "Reconhecer o formato. É do livro, com números trocados.", 202)
                .comLogicaMinima(6)
                .comScoreGanho(7).comCustoEnergia(15)
                .comAtributo("confianca", 3).build());
        cena201.adicionarEscolha(new Escolha.Builder(
                "Você respira. Já passou por coisa pior.", 202)
                .comSanidadeMinima(6)
                .comScoreGanho(5).comCustoEnergia(15).build());
        cenas.add(cena201);

        Cena cena202 = new Cena(202, "Capítulo 3", "Narrador",
                "Você resolve a primeira.\n\n" +
                        "A segunda você tenta por vinte minutos. Erra.\n\n" +
                        "A terceira sai pela metade.\n\n" +
                        "As duas últimas ficam em branco.\n\n" +
                        "Entrega com 1h30 de prova.");
        cena202.adicionarEscolha(new Escolha.Builder("Sair sem olhar.", 210)
                .comCustoEnergia(10).build());
        cena202.adicionarEscolha(new Escolha.Builder("Esperar os colegas.", 210)
                .comRelacionamento(PersonagemSecundario.DANDARA, 1f).build());
        cenas.add(cena202);

        Cena cena204 = new Cena(204, "Capítulo 3", "Narrador",
                "Bia tá sentada no fundo com o fichário aberto.\n\n" +
                        "Ela te olha. Entende o que você quer. Desliza um papel pra cima " +
                        "da mesa. Sem falar nada.\n\n" +
                        "Na frente, Dandara tá suando. Você sabe que ela não come há dois " +
                        "dias pra pagar o ônibus.\n\n" +
                        "(Eu não devia estar olhando pra esse papel.)\n\n" +
                        "(Eu não devia.)\n\n" +
                        "Você olha pro papel.\n\n" +
                        "Você olha pra Dandara.");
        cena204.adicionarEscolha(new Escolha.Builder("Copiar.", 205)
                .comScoreGanho(6).comAtributo("confianca", -3)
                .comFlagConcedida("cap3_colou_de_fato")
                .comRelacionamento(PersonagemSecundario.BIA, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena204.adicionarEscolha(new Escolha.Builder("Devolver o papel.", 201)
                .comAtributo("confianca", 2)
                .comFlagConcedida("hab_palavra_honesta")
                .comRelacionamento(PersonagemSecundario.BIA, -1f).build());
        cenas.add(cena204);

        Cena cena205 = new Cena(205, "Capítulo 3", "Narrador",
                "Você copia.\n\n" +
                        "Não olha pra Dandara. Entrega antes dela e sai.\n\n" +
                        "No corredor, você fica com aquela sensação estranha de ter ganhado " +
                        "algo que não era seu.\n\n" +
                        "(Eu vou esquecer isso.)\n\n" +
                        "(Um dia.)");
        cena205.adicionarEscolha(new Escolha.Builder("Ir pra casa.", 210)
                .comAtributo("confianca", -10).build());
        cena205.adicionarEscolha(new Escolha.Builder("Esperar Dandara.", 210)
                .comFlagConcedida("cap3_encarou_dandara").build());
        cenas.add(cena205);

        Cena cena207 = new Cena(207, "Capítulo 3", "Narrador",
                "Você levanta.\n\n" +
                        "Entrega com nome e matrícula e nada mais.\n\n" +
                        "O professor não fala nada.\n\n" +
                        "Alguém no fundo ri. Você não sabe se é de você ou de outra coisa.");
        cena207.adicionarEscolha(new Escolha.Builder("Sentar no pátio.", 208)
                .comCustoEnergia(5).build());
        cena207.adicionarEscolha(new Escolha.Builder("Ir pro ponto.", 209).build());
        cenas.add(cena207);

        Cena cena208 = new Cena(208, "Capítulo 3", "Cáio Andrade",
                "Cáio senta do seu lado. Sem falar. Cinco minutos de silêncio.\n\n" +
                        "Aí ele fala, sem olhar pra você:\n\n" +
                        "'Também entreguei em branco a minha primeira prova.'\n\n" +
                        "Pausa.\n\n" +
                        "'Fiquei com 1,2. Reprovei. Refiz.'\n\n" +
                        "Pausa.\n\n" +
                        "'Não é o fim do mundo. Só é um semestre.'");
        cena208.adicionarEscolha(new Escolha.Builder("Perguntar como ele fez.", 210)
                .comAtributo("confianca", 1)
                .comRelacionamento(PersonagemSecundario.VETERANO, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.ESTABILIDADE).build());
        cena208.adicionarEscolha(new Escolha.Builder("Agradecer e ir.", 209).build());
        cenas.add(cena208);

        Cena cena209 = new Cena(209, "Capítulo 3", "Narrador",
                "Você dorme no ônibus. Alguém tá escutando funk alto sem fone. " +
                        "Você quase perde a parada.");
        cena209.adicionarEscolha(new Escolha.Builder("Seguir.", 210).build());
        cenas.add(cena209);

        Cena cena210 = new Cena(210, "Capítulo 3", "Narrador",
                "Sexta-feira. Resultado no mural.\n\n" +
                        "Você procura seu nome. A nota tá do lado.\n\n" +
                        "Você não olha pra nota dos outros. Mas você vê a da Dandara, " +
                        "porque tá do lado da sua.\n\n" +
                        "8,5.\n\n" +
                        "Meira passa no corredor. Ele te vê olhando pro mural. Para.\n\n" +
                        "'Cálculo é filtro. Não é o curso.'\n\n" +
                        "Pausa.\n\n" +
                        "'Você vai ver.'\n\n" +
                        "E vai embora.");
        cena210.adicionarEscolha(new Escolha.Builder("Ela mereceu.", 214)
                .comRelacionamento(PersonagemSecundario.DANDARA, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE)
                .comAtributo("sanidade", 5).build());
        cena210.adicionarEscolha(new Escolha.Builder("Ela tirou 8,5 e eu com 4.", 214)
                .comRelacionamento(PersonagemSecundario.DANDARA, -1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena210.adicionarEscolha(new Escolha.Builder("Não olhar.", 214)
                .comAtributo("sanidade", 8).build());
        cenas.add(cena210);

        Cena cena211 = new Cena(211, "Capítulo 3", "Dandara Oliveira",
                "Dandara te para no corredor do módulo 2. Cartão do RU na mão.\n\n" +
                        "'Você viu o edital de Monitoria?'\n\n" +
                        "Você não viu.\n\n" +
                        "'A bolsa é R$ 600. Eu preciso dessa bolsa.'\n\n" +
                        "Pausa.\n\n" +
                        "'Meu auxílio moradia não saiu. A PRAE não me deu nem resposta.'\n\n" +
                        "Ela fala rápido. Como se tivesse ensaiado e não quisesse perder " +
                        "o timing.\n\n" +
                        "Ela não espera você responder. Vai embora.");
        cena211.adicionarEscolha(new Escolha.Builder("Não se inscrever.", 212)
                .comRelacionamento(PersonagemSecundario.DANDARA, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena211.adicionarEscolha(new Escolha.Builder("Se inscrever também.", 212)
                .comFlagConcedida("cap3_vai_disputar")
                .comRelacionamento(PersonagemSecundario.DANDARA, -2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena211.adicionarEscolha(new Escolha.Builder("Perguntar sobre a PRAE.", 212)
                .comFlagConcedida("cap3_sabe_prae")
                .comRelacionamento(PersonagemSecundario.DANDARA, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cenas.add(cena211);

        Cena cena212 = new Cena(212, "Capítulo 3", "Narrador",
                "Primeira prova. Primeiro resultado.\n\n" +
                        "Você ainda tá no jogo.\n\n" +
                        "Mas você já sabe uma coisa que não sabia antes: a faculdade não " +
                        "é difícil só por causa do conteúdo.");
        cena212.adicionarEscolha(new Escolha.Builder("Encerrar Capítulo 3.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap3_concluido").build());
        cenas.add(cena212);
        Cena cena214 = new Cena(214, "Capítulo 3", "Narrador",
                "Você tá no ônibus, voltando pra casa. O celular apita.\n\n" +
                        "LinkedIn.\n\n" +
                        "Uma notificação de mensagem. Alguém viu seu post sobre " +
                        "o LED que você fez acender no primeiro dia de aula. " +
                        "Você nem lembrava que tinha postado aquilo.\n\n" +
                        "É uma colega de outro semestre.\n\n" +
                        "'Oi! Vi seu post. Você mexe com eletrônica? " +
                        "Tô montando uma sociedade técnica de embarcados, " +
                        "Industry Electronics. Já tem professor conselheiro. " +
                        "Topa fazer parte?'");
        cena214.adicionarEscolha(new Escolha.Builder(
                "Aceitar. Você nem sabe o que é direito, mas quer.", 211)
                .comParticipacaoGanha(4).comCustoEnergia(15)
                .comFlagConcedida("industry_electronics")
                .comRelacionamento(PersonagemSecundario.MALU, 1f).build());
        cena214.adicionarEscolha(new Escolha.Builder(
                "Perguntar quanto tempo consome por semana.", 211)
                .comAtributo("logica", 1).comCustoEnergia(5).build());
        cena214.adicionarEscolha(new Escolha.Builder(
                "Agradecer e recusar. Você já tá no limite.", 211)
                .comFlagConcedida("recusou_industry").build());
        cenas.add(cena214);
        return cenas;
    }
}