package br.uefs.forkeazando.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PersonagemSecundario {

    public enum Interesse {
        RECONHECIMENTO, OPORTUNIDADE, ESTABILIDADE, CONHECIMENTO, CONTROLE
    }

    private static int contadorGeraId = 0;

    private static final Map<String, PersonagemSecundario> REGISTRO =
            new HashMap<>();

    private final int id;
    private final String codigo;
    private final String nome;
    private final Interesse interesse;

    private PersonagemSecundario(String codigo, String nome, Interesse interesse) {
        if (codigo == null || codigo.isBlank())
            throw new IllegalArgumentException("Código do personagem não pode ser vazio");

        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome do personagem não pode ser vazio");

        if (interesse == null)
            throw new IllegalArgumentException("Interesse do personagem não pode ser nulo");

        this.codigo = codigo.trim();
        this.nome = nome;
        this.interesse = interesse;
        this.id = contadorGeraId++;

        if (REGISTRO.putIfAbsent(this.codigo, this) != null) {
            throw new IllegalStateException(
                    "Código de personagem duplicado: " + this.codigo
            );
        }
    }

    public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public Interesse getInteresse() {
        return interesse;
    }

    public static PersonagemSecundario porCodigo(String codigo) {
        PersonagemSecundario personagem = REGISTRO.get(codigo);

        if (personagem == null) {
            throw new IllegalArgumentException(
                    "Personagem não encontrado para o código: " + codigo
            );
        }

        return personagem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonagemSecundario that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return nome;
    }

    // PERSONAGENS DO JOGO

    public static final PersonagemSecundario VETERANO =
            new PersonagemSecundario(
                    "VETERANO", "Cáio Andrade", Interesse.ESTABILIDADE);

    public static final PersonagemSecundario ORIENTADOR_IC =
            new PersonagemSecundario(
                    "ORIENTADOR_IC", "Prof. Dr. Sérgio Viana",
                    Interesse.RECONHECIMENTO);

    public static final PersonagemSecundario PROFESSOR_DTEC =
            new PersonagemSecundario(
                    "PROFESSOR_DTEC", "Prof. Rafael Meira",
                    Interesse.CONHECIMENTO);

    public static final PersonagemSecundario PROFESSOR_PBL =
            new PersonagemSecundario(
                    "PROFESSOR_PBL", "Prof. Tutor PBL",
                    Interesse.RECONHECIMENTO);

    public static final PersonagemSecundario DANDARA =
            new PersonagemSecundario(
                    "DANDARA", "Dandara Oliveira",
                    Interesse.OPORTUNIDADE);

    public static final PersonagemSecundario BIA =
            new PersonagemSecundario(
                    "BIA", "Beatriz Menezes",
                    Interesse.OPORTUNIDADE);

    public static final PersonagemSecundario ZE_MODULO8 =
            new PersonagemSecundario(
                    "ZE_MODULO8", "Zé do Módulo 8",
                    Interesse.ESTABILIDADE);

    public static final PersonagemSecundario SISTEMA =
            new PersonagemSecundario(
                    "SISTEMA", "Sistema Acadêmico",
                    Interesse.CONTROLE);

    // Amigos de turma (comentam nas cenas, não têm relacionamento)

    public static final PersonagemSecundario ALAN =
            new PersonagemSecundario(
                    "ALAN", "Alan", Interesse.CONHECIMENTO);

    public static final PersonagemSecundario MALU =
            new PersonagemSecundario(
                    "MALU", "Malu", Interesse.OPORTUNIDADE);
}