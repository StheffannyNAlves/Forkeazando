package br.uefs.forkeazando.model;

import java.util.HashMap;
import java.util.Map;

public class Escolha {
    private final String textoAlternativa;
    private final int cenaDestinoId;
    private final String flagConcedida;
    private final String flagRequerida;
    private final int scoreMinimo;
    private final int participacaoMinima;
    private final int scoreGanho;
    private final int participacaoGanha;
    private final int custoEnergia;
    private final Map<PersonagemSecundario, Float> impactoRelacionamento;


    private Escolha(Builder builder) {
        this.textoAlternativa    = builder.textoAlternativa;
        this.cenaDestinoId       = builder.cenaDestinoId;
        this.flagConcedida       = builder.flagConcedida;
        this.flagRequerida       = builder.flagRequerida;
        this.scoreMinimo         = builder.scoreMinimo;
        this.participacaoGanha = builder.participacaoGanha;
        this.participacaoMinima  = builder.participacaoMinima;
        this.scoreGanho = builder.scoreGanho;
        this.custoEnergia = builder.custoEnergia;
        this.impactoRelacionamento = builder.impactoRelacionamento;
    }

    public boolean estaDisponivel(Protagonista p) {
        boolean requisitosOk = p.atingiuRequisito(scoreMinimo, participacaoMinima);
        boolean energiaOk = p.getEnergia() >= custoEnergia;
        boolean flagOk = (flagRequerida == null) || p.temFlag(flagRequerida);
        return requisitosOk && flagOk && energiaOk;
    }

    public String getTextoAlternativa() { return textoAlternativa; }
    public int getCenaDestinoId()       { return cenaDestinoId; }
    public String getFlagConcedida()    { return flagConcedida; }


    public int getParticipacaoGanha() {
        return participacaoGanha;
    }

    public int getScoreGanho() {
        return scoreGanho;
    }
    public int getCustoEnergia() {
        return custoEnergia;
    }

    public Map<PersonagemSecundario, Float> getImpactosRelacionamento() {
        return impactoRelacionamento;
    }

    public static class Builder{
        private final String textoAlternativa;
        private final int cenaDestinoId;
        private String flagConcedida = null;
        private String flagRequerida = null;
        private int scoreMinimo = 0;
        private int participacaoMinima = 0;
        private int participacaoGanha = 0;
        private int scoreGanho = 0;
        private int custoEnergia;
        private Map<PersonagemSecundario, Float> impactoRelacionamento = new HashMap<>();



        public Builder(String textoAlternativa, int cenaDestinoId) {
            this.textoAlternativa = textoAlternativa;
            this.cenaDestinoId = cenaDestinoId;
        }

        public Builder comFlagConcedida(String flagConcedida) {
            this.flagConcedida = flagConcedida;
            return this;
        }

        public Builder comFlagRequerida(String flagRequerida) {
            this.flagRequerida = flagRequerida;
            return this;
        }

        public Builder comScoreMinimo(int scoreMinimo) {
            this.scoreMinimo = scoreMinimo;
            return this;
        }

        public Builder comParticipacaoMinima(int participacaoMinima) {
            this.participacaoMinima = participacaoMinima;
            return this;
        }

        public Builder comParticipacaoGanha(int participacaoGanha){
            this.participacaoGanha = participacaoGanha;
            return this;
        }

        public Builder comScoreGanho(int scoreGanho){
            this.scoreGanho = scoreGanho;
            return this;
        }

        public Builder comCustoEnergia(int custoEnergia){
            this.custoEnergia = custoEnergia;
            return this;
        }

        public Builder comRelacionamento(PersonagemSecundario personagem, float ganho) {
            this.impactoRelacionamento.put(personagem, ganho);
            return this;
        }

        public  Escolha build(){
            return new Escolha(this);
        }
    }
}




