package br.uefs.forkeazando.model;

public class PersonagemSecundario {

    public enum Interesse {
        RECONHECIMENTO, OPORTUNIDADE, ESTABILIDADE, CONHECIMENTO
    }
    private String nome = "";
    private final Interesse interesse;
    private float nivelRelacionamento = 0;

    public PersonagemSecundario(String nome, Interesse interesse) {
        this.nome = nome;
        this.interesse = interesse;
    }

    public void alterarRelacionamento(int delta){
        nivelRelacionamento += delta;
    }
    public Interesse getInteresse(){
        return interesse;
    }

    public float getNivelRelacionamento() {
        return nivelRelacionamento;
    }

    public String getNome() { return nome; }
}
