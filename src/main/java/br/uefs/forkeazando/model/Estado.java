package br.uefs.forkeazando.model;
// só sabe o estado da partida, mas tbm precisa conhecer o protagonista e o relacionamento com os personagens secundarios, além das decisões tomadas ao longo do jogo
import java.util.ArrayList;
import java.util.List;

public class Estado {
    private Protagonista protagonista;
    // private List<PersonagemSecundario> personagensSecundarios = new ArrayList<>(); aqui existe em
    private int capituloAtual = 1;
    private int cenaAtualId = 0;

    public void iniciar(Protagonista protagonista) {
        this.protagonista = protagonista;
    }

    public void avancarCapitulo() {
        capituloAtual++;
    }

    public Protagonista getProtagonista() {
        return protagonista;
    }

    public int getCapituloAtual() {
        return capituloAtual;
    }

    public int getCenaAtualId() {
        return cenaAtualId;
    }

    public void setCenaAtualId(int novoid){
        this.cenaAtualId = novoid;
    }

}