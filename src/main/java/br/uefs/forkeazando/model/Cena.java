package br.uefs.forkeazando.model;

import java.util.ArrayList;
import java.util.List;

public class Cena {
    private final int id;
    private final String capitulo;
    private final String personagemFalando;
    private final String textoNarrativo;
    private final List<Escolha> escolhas = new ArrayList<>();

    public Cena(int id, String capitulo, String personagemFalando, String textoNarrativo) {
        this.id = id;
        this.capitulo = capitulo;
        this.personagemFalando = personagemFalando;
        this.textoNarrativo = textoNarrativo;
    }
     public void adicionarEscolha(Escolha e){
        escolhas.add(e);
     }

     public  List<Escolha> getEscolhasDisponiveis(Protagonista p){  // Expressão lambda, mais limpo que fazer um if. Aqui retorna uma lista com as opções disponiveis
        return escolhas.stream()
                .filter(e -> e.estaDisponivel(p))
                .toList();
     }
     public List<Escolha> getEscolhas() {
        return List.copyOf(escolhas);
    }
     public int getId(){ return id; }
     public String getCapitulo() { return capitulo;}
     public String getPersonagemFalando() { return personagemFalando;}
     public String getTextoNarrativo() {return textoNarrativo; }

    public boolean temEscolhaBloqueadaSoPorEnergia(Protagonista p){
        return escolhas.stream()
                .anyMatch(e -> !e.estaDisponivel(p) && e.estaDisponivelIgnorandoEnergia(p)); // consegue reconhecer quando a falta de energia é o motivo do bloqueio das escolhas
    }
}
