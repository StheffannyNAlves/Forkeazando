package br.uefs.forkeazando.model;

import java.util.Objects;

public class PersonagemSecundario {

    public enum Interesse {
        RECONHECIMENTO, OPORTUNIDADE, ESTABILIDADE, CONHECIMENTO, CONTROLE
    }

    private static int contadorGeraId = 0;
    private final int id;
    private final String nome;
    private final Interesse interesse;

    private PersonagemSecundario(String nome, Interesse interesse) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome do personagem não pode ser vazio");
        if (interesse == null)
            throw new IllegalArgumentException("Interesse do personagem não pode ser nulo");
        this.id = contadorGeraId++;
        this.nome = nome;
        this.interesse = interesse;
    }

    public int getId()              { return id; }
    public String getNome()         { return nome; }
    public Interesse getInteresse() { return interesse; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonagemSecundario that)) return false;
        return id == that.id;
    }
    @Override public int hashCode() { return Objects.hashCode(id); }
    @Override public String toString() { return nome; }

    // PERSONAGENS DO JOGO

    public static final PersonagemSecundario VETERANO = new PersonagemSecundario(
            "Cáio Andrade", Interesse.ESTABILIDADE);

    public static final PersonagemSecundario ORIENTADOR_IC = new PersonagemSecundario(
            "Prof. Dr. Sérgio Viana", Interesse.RECONHECIMENTO);

    public static final PersonagemSecundario PROFESSOR_DTEC = new PersonagemSecundario(
            "Prof. Rafael Meira", Interesse.CONHECIMENTO);

    public static final PersonagemSecundario PROFESSOR_PBL = new PersonagemSecundario(
            "Prof. Tutor PBL", Interesse.RECONHECIMENTO);

    public static final PersonagemSecundario DANDARA = new PersonagemSecundario(
            "Dandara Oliveira", Interesse.OPORTUNIDADE);

    public static final PersonagemSecundario BIA = new PersonagemSecundario(
            "Beatriz Menezes", Interesse.OPORTUNIDADE);

    public static final PersonagemSecundario ZE_MODULO8 = new PersonagemSecundario(
            "Zé do Módulo 8", Interesse.ESTABILIDADE);

    public static final PersonagemSecundario SISTEMA = new PersonagemSecundario(
            "Sistema Acadêmico", Interesse.CONTROLE);

    // Amigos de turma (comentam nas cenas, não têm relacionamento)
    public static final PersonagemSecundario ALAN = new PersonagemSecundario(
            "Alan", Interesse.CONHECIMENTO);

    public static final PersonagemSecundario MALU = new PersonagemSecundario(
            "Malu", Interesse.OPORTUNIDADE);
}