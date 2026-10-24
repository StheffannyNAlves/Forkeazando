package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Protagonista;

/**
 * O Sistema Acadêmico (ÁGATA) classifica a protagonista com base nas
 * 4 respostas da cena 3046-3049. É burro de propósito: 4 cliques não
 * definem uma pessoa, e a classificação erra com frequência.
 *
 * A classe existe pra tirar essa lógica de dentro da CenaView.
 */
public final class SistemaAcademico {

    public enum Perfil {
        IC,        // Estudar + sozinha + visibilidade + planejar
        DTEC,      // Grupo + visibilidade + improvisar
        INDEP,     // Mexer + invisível + improvisar
        AMBIGUO,   // Empate entre dois ou mais perfis
        INDEFINIDO //  Nenhuma resposta coerente (perfil sem dados)
    }

    private SistemaAcademico() {}

    public static Perfil classificar(Protagonista p) {
        int ic    = 0;
        int dtec  = 0;
        int indep = 0;

        // Q1 — Estudar vs. Mexer
        if (p.temFlag("sistema_q1_estudar"))       ic++;
        else if (p.temFlag("sistema_q1_mexer"))    indep++;

        // Q2 — Sozinha vs. Grupo
        if (p.temFlag("sistema_q2_grupo"))         dtec++;
        else if (p.temFlag("sistema_q2_sozinha"))  ic++;

        // Q3 — Visibilidade vs. Invisibilidade
        // (o Sistema acha que quem quer visibilidade serve pra IC ou DTEC)
        if (p.temFlag("sistema_q3_visivel"))       { ic++; dtec++; }
        else if (p.temFlag("sistema_q3_invisivel")) indep++;

        // Q4 — Planejar vs. Improvisar
        if (p.temFlag("sistema_q4_planejar"))      ic++;
        else if (p.temFlag("sistema_q4_improvisar")) { dtec++; indep++; }

        int max = Math.max(ic, Math.max(dtec, indep));

        // Nenhuma resposta coerente — jogador pulou a tela ou save corrompido
        if (max == 0) return Perfil.INDEFINIDO;

        int empatados =
                (ic    == max ? 1 : 0) +
                        (dtec  == max ? 1 : 0) +
                        (indep == max ? 1 : 0);

        if (empatados > 1) return Perfil.AMBIGUO;

        if (ic    == max) return Perfil.IC;
        if (dtec  == max) return Perfil.DTEC;
        return Perfil.INDEP;
    }

    /** Texto que aparece no banner amarelo. */
    public static String descricaoDe(Perfil perfil) {
        return switch (perfil) {
            case IC         -> "Perfil compatível: IC. Iniciação Científica.";
            case DTEC       -> "Perfil compatível: DTEC. Monitoria.";
            case INDEP      -> "Perfil compatível: INDEP. Projetos independentes.";
            case AMBIGUO    -> "Perfil híbrido. Sugestão: IC ou DTEC.";
            case INDEFINIDO -> "Perfil atípico. Sem categoria definida.";
        };
    }

    /**
     * Registra a classificação no protagonista pra ser lida
     * por cenas futuras (o orientador sabe, o epílogo comenta, etc.).
     * Idempotente: chamar duas vezes não duplica flag.
     */
    public static void chute(Protagonista p) {
        if (p.temAlgumaFlagComPrefixo("sistema_chutou_")) {
            return;
        }
        Perfil perfil = classificar(p);
        p.adicionarFlag("sistema_chutou_" + perfil.name().toLowerCase());
    }
}