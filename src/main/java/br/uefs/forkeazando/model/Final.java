package br.uefs.forkeazando.model;

import java.util.Collection;
import java.util.Optional;

public enum Final {
    IC("jornada_ic", "trilha_ic", "A Cientista"),
    DTEC("jornada_dtec", "trilha_dtec", "Mão na Massa"),
    INDEPENDENTE("jornada_independente", "trilha_indep", "O Contato");

    public static final String FLAG_CONCLUSAO = "final_formou";
    private final String codigo;
    private final String trilha;
    private final String titulo;

    Final(String codigo, String trilha, String titulo) {
        this.codigo = codigo;
        this.trilha = trilha;
        this.titulo = titulo;
    }

    public String getCodigo()   { return codigo; }
    public String getTitulo() { return titulo; }

    public String getTrilha() {
        return trilha;
    }

    public static Optional<Final> porCodigo(String codigo){
        for(Final f: Final.values()){
            if (f.codigo.equals(codigo)) {
                return Optional.of(f);
            }
        }
        return Optional.empty();
    }

    public static Optional<Final> identificar(Collection<String> flags) {
        if (flags == null || flags.isEmpty())
            return Optional.empty();
        if (!flags.contains(FLAG_CONCLUSAO))
            return Optional.empty();

        for (Final f: Final.values()){
            if (flags.contains(f.trilha))
                return Optional.of(f);
        }
        return Optional.empty();
    }



}