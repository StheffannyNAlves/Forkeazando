package br.uefs.forkeazando.view.pixel;

import br.uefs.forkeazando.model.Protagonista;
import java.util.Objects;
import static br.uefs.forkeazando.view.pixel.Cor.rgb;

/** Identidade da arte, independente do nome escolhido e das decisões narrativas. */
public enum PerfilProtagonista {
    TIANA(rgb(173, 104, 65), rgb(46, 30, 31), rgb(234, 119, 36), rgb(59, 78, 137), true),
    MILES(rgb(193, 132, 87), rgb(24, 24, 31), rgb(55, 83, 76), rgb(48, 56, 66), false);

    private final int pele, cabelo, roupa, calca;
    private final boolean cabeloComprido;

    PerfilProtagonista(int pele, int cabelo, int roupa, int calca, boolean cabeloComprido) {
        this.pele = pele;
        this.cabelo = cabelo;
        this.roupa = roupa;
        this.calca = calca;
        this.cabeloComprido = cabeloComprido;
    }

    /** As flags de origem já são persistidas nos saves, inclusive nos anteriores à mudança visual. */
    public static PerfilProtagonista de(Protagonista protagonista) {
        Objects.requireNonNull(protagonista, "O protagonista é obrigatório");
        return protagonista.temFlag("protagonista_masculino") ? MILES : TIANA;
    }

    public int pele() { return pele; }
    public int cabelo() { return cabelo; }
    public int roupa() { return roupa; }
    public int calca() { return calca; }
    public boolean cabeloComprido() { return cabeloComprido; }
}
