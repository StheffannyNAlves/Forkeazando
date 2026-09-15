// Não é uma decisão final, mas essa classe não é guardada por nenhuma outra classe como campo. Não possui uma associação sólida,
// tá mais pra uma associação fraca.

package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;

import java.util.ArrayList;
import java.util.List;

public class Capitulo1 {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();

        Cena cena0 = new Cena(0, "Capítulo 1", "Narrador",
                "Você atualizou a página do SISU pela décima segunda vez.\n\n"
                        + "Não porque alguma coisa fosse mudar.\n"
                        + "Mas porque, aparentemente, clicar em atualizar era uma estratégia válida para lidar com a ansiedade.");

        cena0.adicionarEscolha(new Escolha(
                "Atualizar a página mais uma vez.",
                1, null, null, 0, 0));

        cenas.add(cena0);

        Cena cena1 = new Cena(1, "Capítulo 1", "Sistema",
                "Resultado disponível.\n\n"
                        + "Por alguns segundos, você só fica olhando para a tela.\n"
                        + "Depois lê de novo.\n"
                        + "E de novo.\n\n"
                        + "Engenharia de Computação.\n\n"
                        + "Era isso.\n"
                        + "Você tinha conseguido.");

        cena1.adicionarEscolha(new Escolha(
                "Continuar e conferir as informações da matrícula.",
                2, null, null, 0, 0));

        cenas.add(cena1);

        Cena cena2 = new Cena(2, "Capítulo 1", "Narrador",
                "A matrícula não aconteceu como você imaginava.\n\n"
                        + "Não teve fila.\n"
                        + "Não teve alguém entregando uma pasta com seus documentos.\n"
                        + "Não teve ninguém dizendo pessoalmente: \"Bem-vinda à universidade.\"\n\n"
                        + "Foi tudo online.\n\n"
                        + "Formulários, documentos, prazos e campos para preencher.\n"
                        + "Até que, em algum momento, apareceu mais uma coisa para resolver.");

        cena2.adicionarEscolha(new Escolha(
                "Preencher tudo e conferir os documentos.",
                3, null, null, 0, 0));

        cenas.add(cena2);

        Cena cena3 = new Cena(3, "Capítulo 1", "Narrador",
                "Alguns dias depois, chegou um e-mail.\n\n"
                        + "\"Termo de compromisso — assinatura e envio.\"\n\n"
                        + "Você abre a mensagem.\n"
                        + "Lê as instruções.\n"
                        + "Confere o anexo.\n"
                        + "Lê as instruções novamente.\n\n"
                        + "Ainda estava em casa.\n"
                        + "Mas agora estava oficialmente entrando em Engenharia de Computação.\n\n"
                        + "A universidade parecia distante.\n"
                        + "Por enquanto, ela cabia inteira dentro de uma tela.");

        cena3.adicionarEscolha(new Escolha(
                "Assinar e enviar o termo.",
                4, null, null, 0, 0));

        cenas.add(cena3);

        Cena cena4 = new Cena(4, "Capítulo 1", "Narrador",
                "Primeira semana.\n\n"
                        + "SIECOMP.\n\n"
                        + "Agora não tinha mais tela para esconder a sensação de estar começando alguma coisa.\n\n"
                        + "Tinha gente por todos os lados.\n"
                        + "Calouros tentando descobrir para onde ir.\n"
                        + "Veteranos conversando como se já conhecessem todos os caminhos.\n"
                        + "Professores sendo apresentados.\n"
                        + "Palestras, avisos e uma quantidade considerável de informações para absorver de uma vez.\n\n"
                        + "Você olha ao redor.\n\n"
                        + "É aqui.");

        cena4.adicionarEscolha(new Escolha(
                "Prestar atenção na apresentação do curso.",
                5, null, null, 0, 0));

        cenas.add(cena4);

        Cena cena5 = new Cena(5, "Capítulo 1", "Professor",
                "Engenharia de Computação não é só programação.\n\n"
                        + "Tem hardware.\n"
                        + "Tem software.\n"
                        + "Tem pesquisa.\n"
                        + "Tem projetos.\n"
                        + "Tem extensão.\n"
                        + "Tem muita coisa que vocês ainda nem sabem que existe.\n\n"
                        + "E vocês vão ter bastante tempo para descobrir.");

        cena5.adicionarEscolha(new Escolha(
                "Pensar em tudo que pode fazer durante o curso.",
                6, null, null, 0, 0));

        cenas.add(cena5);

        Cena cena6 = new Cena(6, "Capítulo 1", "Narrador",
                "Entre uma palestra e outra, você começa a perceber que a universidade é bem maior do que parecia quando estava olhando tudo pela tela.\n\n"
                        + "Tem gente falando de iniciação científica.\n"
                        + "Tem gente falando de projetos.\n"
                        + "Tem gente tentando convencer os calouros a entrar em alguma atividade.\n\n"
                        + "Você ainda não sabe exatamente onde se encaixa.\n\n"
                        + "E, sinceramente, nem sabe se deveria saber.");

        cena6.adicionarEscolha(new Escolha(
                "Conversar com alguém que já está no curso.",
                7, null, null, 0, 0));

        cenas.add(cena6);

        Cena cena7 = new Cena(7, "Capítulo 1", "Veterano",
                "Primeiro semestre, né?\n\n"
                        + "Você confirma com a cabeça.\n\n"
                        + "\"Então aproveita enquanto ainda dá tempo de não saber o que tá acontecendo.\"\n\n"
                        + "Você ri, sem saber se ele está brincando.\n\n"
                        + "\"Tô falando sério. No começo parece que tem tempo pra tudo. Depois você descobre que não tem.\"");

        cena7.adicionarEscolha(new Escolha(
                "Perguntar o que ele recomenda fazer no primeiro semestre.",
                8, null, null, 0, 0));

        cenas.add(cena7);

        Cena cena8 = new Cena(8, "Capítulo 1", "Veterano",
                "\"Conhece o curso primeiro.\"\n\n"
                        + "Ele aponta para o movimento ao redor.\n\n"
                        + "\"Vai nas coisas. Conversa com o pessoal. Testa o que você achar interessante.\"\n\n"
                        + "Ele dá de ombros.\n\n"
                        + "\"Depois você decide o que quer levar a sério.\"\n\n"
                        + "Antes que você consiga perguntar qualquer coisa, alguém chama o veterano do outro lado do corredor.\n\n"
                        + "\"Já vou!\"\n\n"
                        + "Ele se afasta.");

        cena8.adicionarEscolha(new Escolha(
                "Seguir para a próxima atividade.",
                9, null, null, 0, 0));

        cenas.add(cena8);

        Cena cena9 = new Cena(9, "Capítulo 1", "Narrador",
                "Entre as atividades da primeira semana, aparece uma oficina de Introdução à Eletrônica.\n\n"
                        + "Você entra sem saber exatamente o que esperar.\n\n"
                        + "Na mesa, componentes, fios e uma protoboard.\n\n"
                        + "Aquilo parece muito mais concreto do que qualquer formulário de matrícula que você preencheu em casa.");

        cena9.adicionarEscolha(new Escolha(
                "Tentar acompanhar a montagem.",
                10, null, null, 0, 0));

        cenas.add(cena9);

        Cena cena10 = new Cena(10, "Capítulo 1", "Professor",
                "\"Não precisa saber tudo agora.\"\n\n"
                        + "O professor aponta para a montagem.\n\n"
                        + "\"A ideia é justamente vocês terem um primeiro contato. Entender o que está acontecendo, fazer perguntas e, principalmente, não ter medo de mexer.\"\n\n"
                        + "Você olha novamente para os componentes.\n\n"
                        + "Talvez Engenharia de Computação fosse mesmo isso:\n"
                        + "entender como as coisas funcionam e descobrir o que você consegue fazer com elas.");

        cena10.adicionarEscolha(new Escolha(
                "Perguntar como a montagem funciona.",
                11, null, null, 0, 0));

        cenas.add(cena10);

        Cena cena11 = new Cena(11, "Capítulo 1", "Narrador",
                "A primeira semana passa rápido.\n\n"
                        + "Você ainda não sabe qual área vai seguir.\n"
                        + "Não sabe se vai gostar mais de hardware, software, pesquisa ou qualquer outra coisa que apareceu nas palestras.\n\n"
                        + "Também não sabe quem vai continuar falando com você depois daquela semana.\n\n"
                        + "Mas já conheceu algumas pessoas.\n"
                        + "Já viu um pouco do curso.\n"
                        + "Já colocou a mão em uma protoboard.\n\n"
                        + "E, pela primeira vez, a ideia de estar na universidade deixa de parecer uma coisa que aconteceu no SISU.\n\n"
                        + "Agora está acontecendo com você.");

        cena11.adicionarEscolha(new Escolha(
                "Continuar.",
                12, null, null, 0, 0));

        cenas.add(cena11);

        Cena cena12 = new Cena(12, "Capítulo 1", "Narrador",
                "Na próxima semana começam as aulas de verdade.\n\n"
                        + "E, junto delas, vêm as primeiras disciplinas, os primeiros trabalhos, os primeiros grupos e as primeiras decisões.\n\n"
                        + "Você ainda não sabe, mas algumas delas vão voltar muito mais tarde.\n\n"
                        + "Por enquanto, você só precisa aparecer.");

        cenas.add(cena12);

        return cenas;
    }
}