package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Final;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.model.PersonagemSecundario;

/** Resolve a cena final a partir da trajetória, sem oferecer um menu de finais ao jogador. */
public final class ResolucaoDeFuturo {
    public static final int CENA_RESOLVER_FUTURO = 667;

    private ResolucaoDeFuturo() {}

    /** Registra um único caminho de carreira e devolve a cena narrativa correspondente. */
    public static int resolverCena(Protagonista p) {
        limparEscolhasLegadas(p);
        Final resultado = avaliar(p);
        p.adicionarFlag(flagDoFuturo(resultado));
        return switch (resultado) {
            case PESQUISA_BRASIL, IC -> 662;
            case TECNOLOGIA, DTEC -> 663;
            case INTERNACIONAL -> 664;
            case CAMINHO_PROPRIO, INDEPENDENTE -> 665;
            case TRANSICAO -> 666;
        };
    }

    private static Final avaliar(Protagonista p) {
        // A rota independente assumida antes é a exceção: não se escolhe no último capítulo.
        // Perder o prazo, porém, também cai tecnicamente na rota C para a partida continuar;
        // esse fallback não deve virar uma carreira independente obrigatória no epílogo.
        if (!p.temFlag("cap4_perdeu_prazo")
                && (p.temFlag("trilha_indep") || p.temFlag("cap5c_concluido"))) {
            return Final.CAMINHO_PROPRIO;
        }

        int pesquisa = 0;
        int tecnologia = 0;
        int internacional = 0;
        int transicao = 0;

        if (p.temFlag("trilha_ic")) pesquisa += 2;
        if (p.temFlag("cap5a_liderou_artigo")) pesquisa += 2;
        if (p.temFlag("cap5a_registrou_experimento")) pesquisa += 2;
        if (p.temFlag("cap5a_perguntou_revisao")) pesquisa += 1;
        if (p.temFlag("item_pdfs_ic")) pesquisa += 1;
        if (p.temFlag("ic_sabe_cronograma")) pesquisa += 1;
        if (p.temFlag("cap7_questionou_autoria")) pesquisa += 2;
        if (p.temFlag("cap7_documentou_limitacoes")) pesquisa += 1;
        if (p.temFlag("cap8_resposta_autoria")) pesquisa += 1;
        if (p.getRelacionamento(PersonagemSecundario.ORIENTADOR_IC) >= 3) pesquisa += 1;

        if (p.temFlag("trilha_dtec")) tecnologia += 2;
        if (p.temFlag("industry_electronics")) tecnologia += 2;
        if (p.temFlag("cap1_depurou_circuito")) tecnologia += 2;
        if (p.temFlag("cap1_mediu_circuito") || p.temFlag("cap1_identificou_componentes")) tecnologia += 1;
        if (p.temFlag("ras_curiosa")) tecnologia += 1;
        if (p.temFlag("tisp_interesse")) tecnologia += 1;
        if (p.temFlag("wie_interesse")) tecnologia += 1;
        if (p.temFlag("software_fora")) tecnologia += 1;
        if (p.temFlag("cap5b_preparou_exercicio")) tecnologia += 1;
        if (p.temFlag("cap7_dtec_ultima_aula") || p.temFlag("cap7_dtec_nao_deu")) tecnologia += 1;
        if (p.getRelacionamento(PersonagemSecundario.PROFESSOR_DTEC) >= 3) tecnologia += 1;

        if (p.temFlag("cap4_plano_internacional")) internacional += 2;
        if (p.temFlag("cap8_resposta_internacional")) internacional += 2;
        if (p.temFlag("hab_rede_contatos")) internacional += 1;
        if (p.temFlag("item_vaga_estagio")) internacional += 1;
        if (p.temFlag("cap7_tcc_planejou")) internacional += 1;
        if (p.temFlag("cap2_trabalhou_em_equipe")) internacional += 1;

        if (p.temFlag("trilha_indefinida")) transicao += 2;
        if (p.temFlag("cap6_trancou")) transicao += 2;
        if (p.temFlag("cap6_reorganizou_rotina")) transicao += 1;
        if (p.temFlag("software_fora")) transicao += 2;
        if (p.temFlag("liga_jogos_contato")) transicao += 2;
        if (p.temFlag("cap8_resposta_honesta")) transicao += 1;
        int interesses = (p.temFlag("ras_curiosa") ? 1 : 0)
                + (p.temFlag("tisp_interesse") ? 1 : 0)
                + (p.temFlag("wie_interesse") ? 1 : 0);
        if (interesses >= 2) transicao += 2;

        int maior = Math.max(Math.max(pesquisa, tecnologia), Math.max(internacional, transicao));
        if (maior == 0) return Final.TRANSICAO;

        // Desempates respeitam evidências fortes, depois a experiência principal da trajetória.
        if (internacional == maior && p.temFlag("cap4_plano_internacional")
                && p.temFlag("cap8_resposta_internacional")) return Final.INTERNACIONAL;
        if (pesquisa == maior && tecnologia == maior && p.temFlag("trilha_ic")) return Final.PESQUISA_BRASIL;
        if (tecnologia == maior && pesquisa == maior && p.temFlag("trilha_dtec")) return Final.TECNOLOGIA;
        if (transicao == maior && p.temFlag("trilha_indefinida")) return Final.TRANSICAO;
        if (pesquisa == maior) return Final.PESQUISA_BRASIL;
        if (tecnologia == maior) return Final.TECNOLOGIA;
        if (internacional == maior) return Final.INTERNACIONAL;
        return Final.TRANSICAO;
    }

    private static String flagDoFuturo(Final f) {
        return switch (f) {
            case PESQUISA_BRASIL, IC -> "futuro_pesquisa_brasil";
            case TECNOLOGIA, DTEC -> "futuro_tecnologia";
            case INTERNACIONAL -> "futuro_internacional";
            case CAMINHO_PROPRIO, INDEPENDENTE -> "futuro_independente";
            case TRANSICAO -> "futuro_transicao";
        };
    }

    private static void limparEscolhasLegadas(Protagonista p) {
        // Saves da versão anterior guardam uma escolha explícita feita no último capítulo.
        // Ela não representa mais o critério do final, então é substituída pela avaliação atual.
        p.removerFlag("futuro_pesquisa_brasil");
        p.removerFlag("futuro_tecnologia");
        p.removerFlag("futuro_internacional");
        p.removerFlag("futuro_independente");
        p.removerFlag("futuro_transicao");
    }
}