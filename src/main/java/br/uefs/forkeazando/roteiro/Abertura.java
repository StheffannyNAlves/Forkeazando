package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;

import java.util.ArrayList;
import java.util.List;
// representa uma prévia do futuro
public class Abertura {

    public static List<Cena> criarCenas() {
        List<Cena> cenas = new ArrayList<>();

        Cena cena700 = new Cena(700, "Prólogo", "Narrador",
                "Cinco anos depois.\n\n" +
                        "Você tá numa sala de entrevista. Mesa de vidro, " +
                        "cadeira desconfortável, um notebook aberto do outro lado " +
                        "com um adesivo do time de futebol.\n\n" +
                        "A pessoa da banca olha pro seu currículo. Ela para numa linha. " +
                        "Depois olha pra você.\n\n" +
                        "'Por que você quer trabalhar aqui?'\n\n" +
                        "(Eu ensaiei essa resposta. Eu ensaiei mil vezes.)");

        cena700.adicionarEscolha(new Escolha.Builder(
                "Porque eu preciso. Simples assim.", 701).build());
        cena700.adicionarEscolha(new Escolha.Builder(
                "Porque eu escolhi isso. Faz cinco anos que eu escolhi.", 701).build());
        cena700.adicionarEscolha(new Escolha.Builder(
                "Sinceramente? Eu ainda tô descobrindo.", 701).build());
        cenas.add(cena700);

        Cena cena701 = new Cena(701, "Prólogo", "Narrador",
                "A pessoa assente devagar, sem entregar nada. " +
                        "Ela faz uma anotação no notebook. " +
                        "Você não consegue ler de cabeça pra baixo.\n\n" +
                        "'Vamos voltar um pouco', ela diz. 'Cinco anos atrás. " +
                        "Por que Engenharia de Computação?'\n\n" +
                        "(Cinco anos atrás. Eu lembro. Eu lembro de tudo.)\n\n" +
                        "Corte seco.");

        cena701.adicionarEscolha(new Escolha.Builder(
                "Voltar pro começo.", 0).build());
        cenas.add(cena701);

        return cenas;
    }
}