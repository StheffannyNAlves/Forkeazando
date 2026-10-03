package br.uefs.forkeazando.model;

import java.util.Objects;

public final class Configuracoes {
    private int velocidadeTexto;
    private int volume;
    private boolean confirmarSaida;
    private boolean dicasAtalho;


    public Configuracoes(){
        velocidadeTexto = 1;
        volume = 50;
        confirmarSaida = true;
        dicasAtalho = true;
    }

    public Configuracoes(Configuracoes outra){
        this.velocidadeTexto = outra.velocidadeTexto;
        this.volume = outra.volume;
        this.confirmarSaida = outra.confirmarSaida;
        this.dicasAtalho = outra.dicasAtalho;
    }

    public int getVelocidadeTexto(){
        return velocidadeTexto;
    }

    public int getVolume() {
        return volume;
    }

    public boolean isConfirmarSaida() {
        return confirmarSaida;
    }

    public boolean isDicasAtalho(){
        return dicasAtalho;
    }

    public void setVelocidadeTexto(int v) {
        this.velocidadeTexto = Math.max(0, Math.min(2, v));
    }

    public void setVolume(int v) {
        this.volume = Math.max(0, Math.min(100, v));
    }

    public void setConfirmarSaida(boolean confirmarSaida) {
        this.confirmarSaida = confirmarSaida;
    }

    public void setDicasAtalho(boolean dicasAtalho) {
        this.dicasAtalho = dicasAtalho;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Configuracoes))
            return false;

        Configuracoes outra = (Configuracoes) o;

        return velocidadeTexto == outra.velocidadeTexto
                && volume == outra.volume
                && confirmarSaida == outra.confirmarSaida
                && dicasAtalho == outra.dicasAtalho;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                velocidadeTexto,
                volume,
                confirmarSaida,
                dicasAtalho);
    }
}
