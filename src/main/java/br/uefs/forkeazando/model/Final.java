package br.uefs.forkeazando.model;

import java.util.Collection;
import java.util.Optional;

/** Finais desbloqueáveis. Os cinco caminhos novos têm precedência sobre os finais antigos por trilha. */
public enum Final {
    PESQUISA_BRASIL("jornada_pesquisa_brasil", "futuro_pesquisa_brasil", "Pesquisa no Brasil", true),
    TECNOLOGIA("jornada_tecnologia", "futuro_tecnologia", "Tecnologia aplicada", true),
    INTERNACIONAL("jornada_internacional", "futuro_internacional", "Além das fronteiras", true),
    CAMINHO_PROPRIO("jornada_caminho_proprio", "futuro_independente", "Carreira independente", true),
    TRANSICAO("jornada_transicao", "futuro_transicao", "Recomeçar sem apagar o passado", true),

    // Mantidos para saves e progresso criados antes da escolha do futuro no Capítulo 8.
    IC("jornada_ic", "trilha_ic", "A Cientista", false),
    DTEC("jornada_dtec", "trilha_dtec", "Mão na Massa", false),
    INDEPENDENTE("jornada_independente", "trilha_indep", "O Contato", false);

    public static final String FLAG_CONCLUSAO = "final_formou";
    private final String codigo;
    private final String flagDesbloqueio;
    private final String titulo;
    private final boolean finalDeCarreira;

    Final(String codigo, String flagDesbloqueio, String titulo, boolean finalDeCarreira) {
        this.codigo = codigo;
        this.flagDesbloqueio = flagDesbloqueio;
        this.titulo = titulo;
        this.finalDeCarreira = finalDeCarreira;
    }

    public String getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }

    /** Mantém o nome do getter para compatibilidade; retorna a flag que identifica o final. */
    public String getTrilha() { return flagDesbloqueio; }

    public static Optional<Final> porCodigo(String codigo) {
        if (codigo == null) return Optional.empty();
        for (Final f : Final.values()) {
            if (f.codigo.equals(codigo)) return Optional.of(f);
        }
        return Optional.empty();
    }

    public static Optional<Final> identificar(Collection<String> flags) {
        if (flags == null || flags.isEmpty() || !flags.contains(FLAG_CONCLUSAO)) {
            return Optional.empty();
        }

        // A decisão explícita do último capítulo é o final da partida; a trilha inicial não a sobrepõe.
        for (Final f : Final.values()) {
            if (f.finalDeCarreira && flags.contains(f.flagDesbloqueio)) return Optional.of(f);
        }
        // Compatibilidade com partidas e progressos anteriores à escolha de futuro.
        for (Final f : Final.values()) {
            if (!f.finalDeCarreira && flags.contains(f.flagDesbloqueio)) return Optional.of(f);
        }
        return Optional.empty();
    }
}