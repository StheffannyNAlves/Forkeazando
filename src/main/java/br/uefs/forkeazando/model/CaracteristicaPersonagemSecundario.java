package br.uefs.forkeazando.model;
// Falta testar isoladamente
import java.util.Objects;

// define as caracteristicas dos personagens secundarios
public class CaracteristicaPersonagemSecundario {

    public enum Interesse {
        RECONHECIMENTO, OPORTUNIDADE, ESTABILIDADE, CONHECIMENTO
    }
    private String nome = "";
    private final Interesse interesse; // final pq o interesse dos personagens secundarios não muda, aqui basicamente é a área dele po
    private float nivelRelacionamento = 0;
    private static int contadorGeraId = 0;
    private final int id; // UNICO E NUNCA MUDA

    public CaracteristicaPersonagemSecundario(String nome, Interesse interesse) {
        this.nome = nome;
        this.interesse = interesse;
        id = gerarId();
    }

    public static int gerarId(){
        int valor = contadorGeraId;
        contadorGeraId++;
        return valor;

    }



    @Override  // reescrita da função equals()
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CaracteristicaPersonagemSecundario that = (CaracteristicaPersonagemSecundario) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void alterarRelacionamento(int delta){
        nivelRelacionamento += delta;
    }

    public Interesse getInteresse(){
        return interesse;
    }
    public int getId() {
        return id;
    }
    public float getNivelRelacionamento() {
        return nivelRelacionamento;
    }

    public String getNomePersonagemS() { return nome; }
}
// +calcularArea(lado: int): float