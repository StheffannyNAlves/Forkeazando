package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Protagonista;

import java.util.Locale;

/**
 * Registra a nota da primeira prova de Cálculo I com base no percurso até a avaliação.
 * A tentativa honesta considera preparação, raciocínio e estratégias usadas na prova.
 * Colar e entregar em branco são resultados distintos e não são confundidos com domínio
 * do conteúdo. A nota é persistida como uma flag para continuar funcionando nos saves.
 */
public final class ResultadoCalculo {
    private static final String PREFIXO_NOTA = "cap3_nota_valor_";

    private ResultadoCalculo() {}

    public static boolean ehCenaDeResultado(int cenaId) {
        return cenaId == 210 || cenaId == 2105 || cenaId == 2107;
    }

    /** Mantém saves antigos jogáveis caso tenham sido salvos depois da entrega e antes do mural. */
    public static boolean assegurarNota(Protagonista p, int cenaId) {
        if (temNota(p)) return false;

        // Uma partida antiga pode ser retomada direto no mural, antes da cena 212.
        // A marca cap3_olhou_resposta diferencia o novo caminho parcial de cola.
        if ((cenaId == 2105 || cenaId == 212)
                && p.temFlag("cap3_colou_de_fato")
                && !p.temFlag("cap3_copiou_bia")
                && !p.temFlag("cap3_olhou_resposta")) {
            p.adicionarFlag("cap3_nota_legada_colando");
        }

        if (cenaId == 212) {
            int resultadoLegado = p.temFlag("cap3_colou_de_fato") ? 2105
                    : p.temFlag("cap3_desistiu") ? 2107 : 210;
            return registrarNota(p, resultadoLegado);
        }
        return registrarNota(p, cenaId);
    }

    /** @return true quando uma nota nova foi registrada; false se já existia no save. */
    public static boolean registrarNota(Protagonista p, int cenaId) {
        if (!ehCenaDeResultado(cenaId) || temNota(p)) return false;

        double nota;
        if (cenaId == 2107) {
            nota = 0.0; // a prova foi entregue em branco
        } else if (cenaId == 2105) {
            if (p.temFlag("cap3_nota_legada_colando") || p.temFlag("cap3_copiou_bia")) {
                nota = 7.5; // a folha copiada continha as respostas desta avaliação
            } else {
                nota = 6.0; // olhar a prova alheia ajuda em parte, mas não garante domínio das cinco questões
            }
        } else {
            nota = calcularTentativaHonesta(p);
        }

        int decimos = (int) Math.round(nota * 10);
        p.adicionarFlag(PREFIXO_NOTA + decimos);
        if (nota < 5.0) {
            p.adicionarFlag("cap3_nota_insuficiente");
        } else if (nota < 7.0) {
            p.adicionarFlag("cap3_nota_suficiente");
        } else {
            p.adicionarFlag("cap3_nota_alta");
        }

        if (cenaId == 210) {
            if (nota >= 7.0) {
                p.adicionarFlag("cap3_nota_alta_honesta");
            } else if (nota >= 5.0) {
                p.adicionarFlag("cap3_nota_suficiente_honesta");
            }
        } else if (cenaId == 2105) {
            if (nota >= 7.0) {
                p.adicionarFlag("cap3_nota_alta_colando");
            } else if (nota >= 5.0) {
                p.adicionarFlag("cap3_nota_suficiente_colando");
            }
        }
        return true;
    }

    private static boolean temNota(Protagonista p) {
        return p.getHistoricoFlags().stream().anyMatch(f -> f.startsWith(PREFIXO_NOTA));
    }

    private static double calcularTentativaHonesta(Protagonista p) {
        int evidencias = 0;

        // Preparação anterior: basta uma das formas de estudo para contar a base.
        if (p.temFlag("estuda_com_dandara") || p.temFlag("bia_resumos")) evidencias += 2;
        if (p.temFlag("hab_foco_total")) evidencias += 2;
        if (p.temFlag("perfil_teorico")) evidencias += 1;

        // Capacidade de raciocinar sobre o exercício e estratégias escolhidas na prova.
        if (p.getLogica() >= 7) evidencias += 2;
        else if (p.getLogica() >= 5) evidencias += 1;
        if (p.temFlag("cap3_estrategia_foco")) evidencias += 2;
        if (p.temFlag("cap3_reconheceu_questoes")) evidencias += 2;
        if (p.temFlag("cap3_manteve_calma")) evidencias += 1;
        if (p.temFlag("cap3_usou_resumo")) evidencias += 1;
        if (p.temFlag("cap3_tentou")) evidencias += 1;
        if (p.temFlag("cap3_confiou")) evidencias += 1;
        if (p.getSanidade() >= 6) evidencias += 1;

        // A nota cresce em meios pontos, entre 3,5 e 9,0. Não depende de uma escolha única.
        return Math.min(9.0, 3.5 + Math.min(evidencias, 11) * 0.5);
    }

    public static double nota(Protagonista p) {
        for (String flag : p.getHistoricoFlags()) {
            if (flag.startsWith(PREFIXO_NOTA)) {
                String valor = flag.substring(PREFIXO_NOTA.length());
                try {
                    return Integer.parseInt(valor) / 10.0;
                } catch (NumberFormatException ignorada) {
                    // Um save antigo ou editado manualmente não impede a história de continuar.
                }
            }
        }
        return 0.0;
    }

    public static String notaFormatada(Protagonista p) {
        return String.format(Locale.forLanguageTag("pt-BR"), "%.1f", nota(p));
    }

    public static String comentario(Protagonista p, int cenaId) {
        if (cenaId == 2107) {
            return "A prova ficou em branco. O zero registra o que foi entregue naquele dia, não "
                    + "a capacidade de aprender o conteúdo. Você ainda precisa decidir o que fazer com essa informação.";
        }
        if (cenaId == 2105) {
            if (p.temFlag("cap3_copiou_bia") || p.temFlag("cap3_nota_legada_colando")) {
                return "A folha da Bia carregou você até essa nota. Ninguém interrompeu a prova, mas o resultado "
                        + "não responde ao que você realmente sabe. A vantagem veio acompanhada de uma coisa que "
                        + "você não consegue desver.";
            }
            return "Olhar a prova alheia rendeu alguns pontos, mas não transformou o que você não sabia em domínio. "
                    + "A nota e a decisão de como consegui-la vão continuar com você.";
        }

        double nota = nota(p);
        if (nota >= 8.0) {
            return "Você acertou mais do que esperava. Ainda há pontos para revisar, mas o método que você "
                    + "construiu apareceu no papel — não por acaso, nem porque tudo ficou fácil.";
        }
        if (nota >= 7.0) {
            return "Você foi bem. Duas questões ainda ficaram marcadas no caderno. A nota mostra domínio "
                    + "de parte do conteúdo, não que Cálculo tenha deixado de exigir trabalho.";
        }
        if (nota >= 5.0) {
            return "Você passou, mas alguns passos não fecharam. Há algo para comemorar e algo concreto "
                    + "para revisar antes da próxima avaliação.";
        }
        return "Você não alcançou a média. A prova mostrou lacunas específicas; não deu uma sentença "
                + "sobre sua capacidade. O que você fizer agora também vai contar.";
    }
}