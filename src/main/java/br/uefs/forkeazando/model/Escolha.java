package br.uefs.forkeazando.model;


public class Escolha {
    private final String textoAlternativa;
    private final int cenaDestinoId;
    private final String flagConcedida;   // flag dada ao escolher (pode ser null)
    private final String flagRequerida;   // flag necessária para aparecer (pode ser null)
    private final int scoreMinimo;
    private final int participacaoMinima;

    public Escolha(String textoAlternativa, int cenaDestinoId,
                   String flagConcedida, String flagRequerida,
                   int scoreMinimo, int participacaoMinima) {
        this.textoAlternativa    = textoAlternativa;
        this.cenaDestinoId       = cenaDestinoId;
        this.flagConcedida       = flagConcedida;
        this.flagRequerida       = flagRequerida;
        this.scoreMinimo         = scoreMinimo;
        this.participacaoMinima  = participacaoMinima;
    }

    public boolean estaDisponivel(Protagonista p) {
        boolean requisitosOk = p.atingiuRequisito(scoreMinimo, participacaoMinima);
        boolean flagOk = (flagRequerida == null) || p.temFlag(flagRequerida);
        return requisitosOk && flagOk;
    }

    public String getTextoAlternativa() { return textoAlternativa; }
    public int getCenaDestinoId()       { return cenaDestinoId; }
    public String getFlagConcedida()    { return flagConcedida; }
}