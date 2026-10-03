package br.uefs.forkeazando.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Escolha implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String textoAlternativa;
    private final int cenaDestinoId;
    private final String flagConcedida;
    private final String flagRequerida;
    private final int scoreMinimo;
    private final int participacaoMinima;
    private final int participacaoMaxima;
    private final int scoreGanho;
    private final int participacaoGanha;
    private final int custoEnergia;
    private final int energiaAbaixoDe;
    private final int confiancaMinima;
    private final int confiancaMaxima;
    private final int vidaSocialMinima;
    private final int vidaSocialMaxima;
    private final int carismaMinimo;
    private final int carismaMaximo;
    private final int logicaMinima;
    private final int logicaMaxima;
    private final int sanidadeMinima;
    private final int sanidadeMaxima;
    private final PersonagemSecundario.Interesse interesseAlvo;
    private final Map<PersonagemSecundario, Float> impactoRelacionamento;
    private final Map<String, Integer> atributosAlterados;

    private Escolha(Builder b) {
        this.textoAlternativa      = b.textoAlternativa;
        this.cenaDestinoId         = b.cenaDestinoId;
        this.flagConcedida         = b.flagConcedida;
        this.flagRequerida         = b.flagRequerida;
        this.scoreMinimo           = b.scoreMinimo;
        this.participacaoMinima    = b.participacaoMinima;
        this.participacaoMaxima    = b.participacaoMaxima;
        this.scoreGanho            = b.scoreGanho;
        this.participacaoGanha     = b.participacaoGanha;
        this.custoEnergia          = b.custoEnergia;
        this.energiaAbaixoDe       = b.energiaAbaixoDe;
        this.confiancaMinima       = b.confiancaMinima;
        this.confiancaMaxima       = b.confiancaMaxima;
        this.vidaSocialMinima      = b.vidaSocialMinima;
        this.vidaSocialMaxima      = b.vidaSocialMaxima;
        this.carismaMinimo         = b.carismaMinimo;
        this.carismaMaximo         = b.carismaMaximo;
        this.logicaMinima          = b.logicaMinima;
        this.logicaMaxima          = b.logicaMaxima;
        this.sanidadeMinima        = b.sanidadeMinima;
        this.sanidadeMaxima        = b.sanidadeMaxima;
        this.interesseAlvo         = b.interesseAlvo;
        this.impactoRelacionamento = b.impactoRelacionamento;
        this.atributosAlterados    = b.atributosAlterados;
    }

    public boolean estaDisponivel(Protagonista p) {
        return p.atingiuRequisito(scoreMinimo, participacaoMinima)
                && ((participacaoMaxima <= 0) || p.getParticipacao() <= participacaoMaxima)
                && p.getEnergia() >= custoEnergia
                && ((flagRequerida == null) || p.temFlag(flagRequerida))
                && ((energiaAbaixoDe <= 0) || p.getEnergia() < energiaAbaixoDe)
                && ((confiancaMinima <= 0) || p.getConfianca() >= confiancaMinima)
                && ((confiancaMaxima <= 0) || p.getConfianca() <= confiancaMaxima)
                && ((vidaSocialMinima <= 0) || p.getVidaSocial() >= vidaSocialMinima)
                && ((vidaSocialMaxima <= 0) || p.getVidaSocial() <= vidaSocialMaxima)
                && ((carismaMinimo <= 0) || p.getCarisma() >= carismaMinimo)
                && ((carismaMaximo <= 0) || p.getCarisma() <= carismaMaximo)
                && ((logicaMinima <= 0) || p.getLogica() >= logicaMinima)
                && ((logicaMaxima <= 0) || p.getLogica() <= logicaMaxima)
                && ((sanidadeMinima <= 0) || p.getSanidade() >= sanidadeMinima)
                && ((sanidadeMaxima <= 0) || p.getSanidade() <= sanidadeMaxima);
    }

    public String getTextoAlternativa() { return textoAlternativa; }
    public int getCenaDestinoId()       { return cenaDestinoId; }
    public String getFlagConcedida()    { return flagConcedida; }
    public int getParticipacaoGanha()   { return participacaoGanha; }
    public int getScoreGanho()          { return scoreGanho; }
    public int getCustoEnergia()        { return custoEnergia; }
    public PersonagemSecundario.Interesse getInteresseAlvo() { return interesseAlvo; }
    public Map<PersonagemSecundario, Float> getImpactosRelacionamento() { return impactoRelacionamento; }
    public Map<String, Integer> getAtributosAlterados() { return atributosAlterados; }

    public static class Builder {
        private final String textoAlternativa;
        private final int cenaDestinoId;
        private String flagConcedida = null;
        private String flagRequerida = null;
        private int scoreMinimo = 0, participacaoMinima = 0, participacaoMaxima = 0;
        private int participacaoGanha = 0, scoreGanho = 0, custoEnergia = 0;
        private int energiaAbaixoDe = 0;
        private int confiancaMinima = 0, confiancaMaxima = 0;
        private int vidaSocialMinima = 0, vidaSocialMaxima = 0;
        private int carismaMinimo = 0, carismaMaximo = 0;
        private int logicaMinima = 0, logicaMaxima = 0;
        private int sanidadeMinima = 0, sanidadeMaxima = 0;
        private PersonagemSecundario.Interesse interesseAlvo = null;
        private Map<PersonagemSecundario, Float> impactoRelacionamento = new HashMap<>();
        private Map<String, Integer> atributosAlterados = new HashMap<>();

        public Builder(String t, int destino) { this.textoAlternativa = t; this.cenaDestinoId = destino; }

        public Builder comFlagConcedida(String v)   { flagConcedida = v; return this; }
        public Builder comFlagRequerida(String v)   { flagRequerida = v; return this; }
        public Builder comScoreMinimo(int v)        { scoreMinimo = v; return this; }
        public Builder comParticipacaoMinima(int v) { participacaoMinima = v; return this; }
        public Builder comParticipacaoMaxima(int v) { participacaoMaxima = v; return this; }
        public Builder comParticipacaoGanha(int v)  { participacaoGanha = v; return this; }
        public Builder comScoreGanho(int v)         { scoreGanho = v; return this; }
        public Builder comCustoEnergia(int v)       { custoEnergia = v; return this; }
        public Builder comEnergiaAbaixoDe(int v)    { energiaAbaixoDe = v; return this; }
        public Builder comConfiancaMinima(int v)    { confiancaMinima = v; return this; }
        public Builder comConfiancaMaxima(int v)    { confiancaMaxima = v; return this; }
        public Builder comVidaSocialMinima(int v)   { vidaSocialMinima = v; return this; }
        public Builder comVidaSocialMaxima(int v)   { vidaSocialMaxima = v; return this; }
        public Builder comCarismaMinima(int v)      { carismaMinimo = v; return this; }
        public Builder comCarismaMaxima(int v)      { carismaMaximo = v; return this; }
        public Builder comLogicaMinima(int v)       { logicaMinima = v; return this; }
        public Builder comLogicaMaxima(int v)       { logicaMaxima = v; return this; }
        public Builder comSanidadeMinima(int v)     { sanidadeMinima = v; return this; }
        public Builder comSanidadeMaxima(int v)     { sanidadeMaxima = v; return this; }
        public Builder comInteresseAlvo(PersonagemSecundario.Interesse i) { interesseAlvo = i; return this; }

        public Builder comRelacionamento(PersonagemSecundario p, float v) {
            impactoRelacionamento.put(p, v); return this;
        }
        public Builder comAtributo(String nome, int delta) {
            atributosAlterados.merge(nome, delta, Integer::sum); return this;
        }
        public Escolha build() { return new Escolha(this); }
    }
}