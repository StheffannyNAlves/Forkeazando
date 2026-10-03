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
                "O cliente respondeu.\n\n" +
                        "Site pra um restaurante. Mil reais. 'Simples'.\n\n" +
                        "(Simples.)\n\n" +
                        "(Ele nunca fez site na vida.)\n\n" +
                        "(Eu também não.)");
        cena500.adicionarEscolha(new Escolha.Builder("Aceitar.", 501)
                .comFlagConcedida("cap5c_aceitou").build());
        cena500.adicionarEscolha(new Escolha.Builder("Pesquisar preço.", 501)
                .comAtributo("logica", 2).build());
        cena500.adicionarEscolha(new Escolha.Builder("Pedir mais.", 501)
                .comFlagConcedida("cap5c_pediu_mais").build());
        cena500.adicionarEscolha(new Escolha.Builder(
                "Você pesquisa em três fontes. Mil reais é o dobro do justo.", 501)
                .comLogicaMinima(5)
                .comFlagConcedida("cap5c_pesquisou_preco").build());
        cenas.add(cena500);

        Cena cena501 = new Cena(501, "Capítulo 5C", "Narrador",
                "Três semanas.\n\n" +
                        "Você entregou. Ele disse 'tá bom'.\n\n" +
                        "Disse 'na semana que vem'.\n\n" +
                        "(Semana que vem.)\n\n" +
                        "(Eu já ouvi essa frase antes.)");
        cena501.adicionarEscolha(new Escolha.Builder("Cobrar.", 502)
                .comFlagConcedida("cap5c_cobrou").build());
        cena501.adicionarEscolha(new Escolha.Builder("Deixar rolar.", 502)
                .comFlagConcedida("cap5c_deixou_rolar").build());
        cenas.add(cena501);

        Cena cena502 = new Cena(502, "Capítulo 5C", "Beatriz Menezes",
                "Bia te encontra num café perto do campus.\n\n" +
                        "'Você tá fazendo freela.'\n\n" +
                        "Não é pergunta.\n\n" +
                        "'Eu vivo disso desde o segundo semestre. A bolsa não paga o " +
                        "aluguel. O freela paga.'\n\n" +
                        "Ela te passa um papel com um contato.\n\n" +
                        "'Se o cliente te enrolar, me chama. Eu conheço os nomes.'");
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
                "Três semanas depois, o cliente não pagou.\n\n" +
                        "Você mandou mensagem. Ele visualizou. Não respondeu.\n\n" +
                        "Você mandou de novo. Ele disse 'tô vendo aqui'.\n\n" +
                        "Você mandou uma terceira. Ele não visualizou.\n\n" +
                        "(Tá vendo.)\n\n" +
                        "(Claro que tá.)");
        cena503.adicionarEscolha(new Escolha.Builder("Ir no restaurante pessoalmente.", 505)
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
        cena503.adicionarEscolha(new Escolha.Builder("Enfrentar tudo. Café e ódio.", 505)
                .comEnergiaAbaixoDe(20)
                .comCustoEnergia(-30)
                .comAtributo("confianca", -5).build());
        cena503.adicionarEscolha(new Escolha.Builder(
                "Você fala com ele. Não ameaça. Só mostra que sabe.", 505)
                .comCarismaMinima(5)
                .comFlagConcedida("cap5c_cliente_pagou")
                .comAtributo("confianca", 2).build());
        cenas.add(cena503);

        Cena cena505 = new Cena(505, "Capítulo 5C", "Narrador",
                "Você não recebeu o dinheiro.\n\n" +
                        "Mas recebeu outra coisa: a certeza de que ninguém vai te ensinar " +
                        "a se proteger no mercado.\n\n" +
                        "Você vai ter que aprender sozinha.\n\n" +
                        "(Ou com a Bia.)\n\n" +
                        "(Provavelmente com a Bia.)");
        cena505.adicionarEscolha(new Escolha.Builder("Encerrar Capítulo 5C.", Estado.CENA_ENCERRAR)
                .comFlagConcedida("cap5c_concluido").build());
        cenas.add(cena505);

        return cenas;
    }
}