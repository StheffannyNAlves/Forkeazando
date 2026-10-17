package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;

import java.util.ArrayList;
import java.util.List;

public class Capitulo5C {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();

        Cena cena500 = new Cena(500, "Capítulo 5C — Independente", "Narrador",
                "O contato do cara do cartaz finalmente responde. Ele não precisa de ajuda " +
                        "no aplicativo agora, mas indica um cliente: um restaurante que quer " +
                        "um site.\n\n" +
                        "O orçamento oferecido é de mil reais. A mensagem termina com uma palavra:\n\n" +
                        "'Simples.'\n\n" +
                        "Você olha para a tela. Não há lista de páginas, prazo definido nem " +
                        "explicação sobre manutenção. Só o preço e a promessa de que não deve " +
                        "dar muito trabalho.\n\n" +
                        "(Simples.)\n\n" +
                        "(Ele não explicou o que quer.)\n\n" +
                        "(Eu nunca fiz um site assim.)");
        cena500.adicionarEscolha(new Escolha.Builder("Aceitar.", 501)
                .comFlagConcedida("cap5c_aceitou").build());
        cena500.adicionarEscolha(new Escolha.Builder("Pesquisar preços antes de responder.", 501)
                .comAtributo("logica", 2).build());
        cena500.adicionarEscolha(new Escolha.Builder("Pedir um valor maior.", 501)
                .comFlagConcedida("cap5c_pediu_mais").build());
        cena500.adicionarEscolha(new Escolha.Builder(
                "Comparar três referências de orçamento antes de responder.", 501)
                .comLogicaMinima(5)
                .comFlagConcedida("cap5c_pesquisou_preco").build());
        cenas.add(cena500);

        Cena cena501 = new Cena(501, "Capítulo 5C", "Narrador",
                "Três semanas depois, o site está entregue. O cliente respondeu apenas 'tá bom' " +
                        "e prometeu pagar na semana seguinte.\n\n" +
                        "A semana passou. O dinheiro não caiu. Você encara a conversa no celular, " +
                        "tentando decidir se cobra de novo ou se espera um pouco mais.\n\n" +
                        "Mil reais pareciam uma oportunidade. Agora, cada dia sem pagamento " +
                        "parece uma aula que você não pediu para ter.");
        cena501.adicionarEscolha(new Escolha.Builder("Cobrar agora.", 502)
                .comFlagConcedida("cap5c_cobrou").build());
        cena501.adicionarEscolha(new Escolha.Builder("Dar mais uma semana.", 502)
                .comFlagConcedida("cap5c_deixou_rolar").build());
        cenas.add(cena501);

        Cena cena502 = new Cena(502, "Capítulo 5C", "Beatriz Menezes",
                "Bia te encontra num café perto do campus. Você mal terminou de contar o que " +
                        "aconteceu e ela já entende do que se trata.\n\n" +
                        "'Você tá fazendo freela.'\n\n" +
                        "Não é pergunta.\n\n" +
                        "'Eu vivo disso desde o segundo semestre. Bolsa não paga o aluguel. " +
                        "Freela paga — quando pagam.'\n\n" +
                        "Ela pega um pedaço de papel e escreve um contato.\n\n" +
                        "'Se o cliente te enrolar, me chama. Eu conheço os nomes.'\n\n" +
                        "Ela empurra o papel pela mesa, mas não solta na mesma hora. " +
                        "'Não entrega tudo sem deixar combinado o que acontece depois.'");
        cena502.adicionarEscolha(new Escolha.Builder("Aceitar o contato.", 503)
                .comFlagConcedida("item_contato_bia")
                .comRelacionamento(PersonagemSecundario.BIA, 3f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena502.adicionarEscolha(new Escolha.Builder("Resolver sozinha.", 503)
                .comFlagConcedida("cap5c_sozinha")
                .comRelacionamento(PersonagemSecundario.BIA, -1f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena502.adicionarEscolha(new Escolha.Builder(
                "Aceitar. Você já conhece metade dessa gente.", 503)
                .comFlagRequerida("origem_amigueiro")
                .comFlagConcedida("item_contato_bia")
                .comRelacionamento(PersonagemSecundario.BIA, 4f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE)
                .comAtributo("vidaSocial", 2).build());
        cenas.add(cena502);

        Cena cena503 = new Cena(503, "Capítulo 5C", "Narrador",
                "Mais algumas semanas passam. O pagamento continua pendente.\n\n" +
                        "Você tenta contato. A primeira mensagem fica sem resposta. Quando insiste, " +
                        "o cliente escreve: 'tô vendo aqui'. A terceira mensagem nem chega a ser " +
                        "visualizada.\n\n" +
                        "Você já não sabe se está negociando um pagamento ou pedindo que alguém " +
                        "reconheça que o seu trabalho existiu.\n\n" +
                        "(Tá vendo.)\n\n" +
                        "(Claro que tá.)");
        cena503.adicionarEscolha(new Escolha.Builder(
                "Ir ao restaurante pessoalmente.", 505)
                .comAtributo("carisma", 2).build());
        cena503.adicionarEscolha(new Escolha.Builder("Desistir.", 505)
                .comAtributo("sanidade", -5).build());
        cena503.adicionarEscolha(new Escolha.Builder("Aprender a fazer contrato.", 505)
                .comAtributo("logica", 3)
                .comFlagConcedida("hab_contrato_assinado").build());
        cena503.adicionarEscolha(new Escolha.Builder("Mandar mensagem pra Bia.", 505)
                .comFlagRequerida("item_contato_bia")
                .comFlagConcedida("cap5c_bia_ajudou")
                .comRelacionamento(PersonagemSecundario.BIA, 2f)
                .comInteresseAlvo(PersonagemSecundario.Interesse.OPORTUNIDADE).build());
        cena503.adicionarEscolha(new Escolha.Builder(
                "Enfrentar tudo. Café e ódio.", 505)
                .comEnergiaAbaixoDe(20)
                .comCustoEnergia(30)
                .comAtributo("confianca", -5)
                .comPermiteEnergiaInsuficiente().build());
        cena503.adicionarEscolha(new Escolha.Builder(
                "Falar com ele sem ameaçar: mostrar que você tem como comprovar a entrega.", 506)
                .comCarismaMinima(5)
                .comAtributo("confianca", 2).build());
        cenas.add(cena503);

        Cena cena505 = new Cena(505, "Capítulo 5C", "Narrador",
                "O dinheiro não chegou. Você fecha a conversa sem uma data de pagamento e " +
                        "guarda os arquivos do projeto.\n\n" +
                        "A raiva vem primeiro. Depois, uma vergonha que você sabe que não deveria " +
                        "sentir: você fez o trabalho, entregou no prazo e ainda precisa correr atrás " +
                        "do que foi combinado.\n\n" +
                        "Mas alguma coisa mudou. Agora você sabe o que precisa aprender além de " +
                        "escrever código: registrar escopo, prazo, forma de pagamento e o que fazer " +
                        "quando o cliente desaparece.\n\n" +
                        "(Sozinha.)\n\n" +
                        "(Ou com a Bia.)\n\n" +
                        "(Provavelmente com a Bia.)");
        cena505.adicionarEscolha(new Escolha.Builder(
                "Encerrar Capítulo 5C.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap5c_concluido").build());
        cenas.add(cena505);

        Cena cena506 = new Cena(506, "Capítulo 5C", "Narrador",
                "Dois dias depois, o pagamento cai.\n\n" +
                        "Não foi por gentileza. Você registrou o que tinha combinado, cobrou com " +
                        "clareza e mostrou que podia comprovar a entrega. Pela primeira vez desde " +
                        "que começou esse trabalho, a conversa deixou de ser uma promessa vaga.\n\n" +
                        "O dinheiro não apaga as semanas de ansiedade, mas paga o trabalho. Você " +
                        "salva os registros e decide que, no próximo freela, vai combinar escopo, " +
                        "prazo e forma de pagamento antes de abrir o editor.");
        cena506.adicionarEscolha(new Escolha.Builder(
                "Encerrar Capítulo 5C.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap5c_concluido")
                .comFlagConcedida("cap5c_cliente_pagou").build());
        cenas.add(cena506);

        return cenas;
    }
}
