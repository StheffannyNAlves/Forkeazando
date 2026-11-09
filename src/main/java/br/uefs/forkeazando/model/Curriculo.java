package br.uefs.forkeazando.model;

import java.util.ArrayList;
import java.util.List;


public final class Curriculo {

    public enum Categoria {
        FORMACAO("FORMAÇÃO"),
        EXPERIENCIA("EXPERIÊNCIA"),
        PROJETOS("PROJETOS"),
        HABILIDADES("HABILIDADES"),
        INTEGRIDADE("INTEGRIDADE"),
        LACUNAS("LACUNAS");

        public final String rotulo;
        Categoria(String rotulo) { this.rotulo = rotulo; }
    }


    public record Linha(Categoria categoria, String texto, boolean positiva) {}

    private Curriculo() {}

    public static List<Linha> linhas(Protagonista p) {
        List<Linha> l = new ArrayList<>();

        l.add(new Linha(Categoria.FORMACAO,
                "Engenharia de Computação — UEFP (em curso)", true));

        if (p.temFlag("trilha_ic")) {
            String status;
            if (p.temFlag("bolsista_ic")) status = "bolsista FAPESB";
            else if (p.temFlag("ic_voluntaria_de_novo")) status = "voluntária, sem bolsa";
            else status = "voluntária";
            l.add(new Linha(Categoria.EXPERIENCIA,
                    "Iniciação Científica — 1 semestre, " + status, true));
        }
        if (p.temFlag("trilha_dtec")) {
            String status = p.temFlag("dtec_greve_incerta")
                    ? "1 semestre, interrompida pela greve"
                    : "1 semestre, voluntária";
            l.add(new Linha(Categoria.EXPERIENCIA,
                    "Monitoria de DTEC — " + status, true));
        }
        if (p.temFlag("cap5c_cliente_pagou")) {
            l.add(new Linha(Categoria.EXPERIENCIA,
                    "Freelance de site — R$ 1.000 (pago)", true));
        }
        if (p.temFlag("cap5c_cliente_nao_pagou")) {
            l.add(new Linha(Categoria.EXPERIENCIA,
                    "Freelance de site — R$ 1.000 (não pago)", false));
        }
        if (p.temFlag("industry_electronics")) {
            l.add(new Linha(Categoria.EXPERIENCIA,
                    "Sociedade técnica Industry Electronics — membro", true));
        }

        if (p.temFlag("cap1_depurou_circuito")) {
            l.add(new Linha(Categoria.PROJETOS,
                    "LED aceso na oficina do primeiro semestre (foto no LinkedIn)", true));
        }
        if (p.temFlag("cap5a_liderou_artigo")) {
            l.add(new Linha(Categoria.PROJETOS,
                    "Artigo científico — coordenação de parte do grupo", true));
        }
        if (p.temFlag("liga_jogos_contato") || p.temFlag("cap2_trabalhou_em_equipe")) {
            l.add(new Linha(Categoria.PROJETOS,
                    "Teste de usabilidade de jogo narrativo", true));
        }
        if (p.temFlag("cap7_tcc_planejou") || p.temFlag("cap7_documentou_limitacoes")) {
            l.add(new Linha(Categoria.PROJETOS,
                    "TCC — documentação de método e limitações", true));
        }


        if (p.temFlag("hab_olho_edital")) {
            l.add(new Linha(Categoria.HABILIDADES,
                    "Leitura de editais e documentação oficial", true));
        }
        if (p.temFlag("cap1_mediu_circuito") || p.temFlag("cap1_depurou_circuito")) {
            l.add(new Linha(Categoria.HABILIDADES,
                    "Depuração de circuito com multímetro", true));
        }
        if (p.temFlag("cap7_dtec_ultima_aula")
                || p.temFlag("cap7_dtec_nao_deu")
                || p.temFlag("cap7_dtec_deu_nota")
                || p.temFlag("cap5b_preparou_exercicio")) {
            l.add(new Linha(Categoria.HABILIDADES,
                    "Explicar conceitos técnicos em diferentes níveis", true));
        }
        if (p.temFlag("hab_contrato_assinado")
                || p.temFlag("cap7_indep_advogado")
                || p.temFlag("cap7_indep_negociou")) {
            l.add(new Linha(Categoria.HABILIDADES,
                    "Negociação de escopo e contrato", true));
        }
        if (p.temFlag("cap7_documentou_limitacoes")) {
            l.add(new Linha(Categoria.HABILIDADES,
                    "Documentação técnica (método, resultados, limitações)", true));
        }

        if (p.temFlag("cap3_colou_de_fato")) {
            l.add(new Linha(Categoria.INTEGRIDADE,
                    "Colou em uma prova do primeiro semestre", false));
        }
        if (p.temFlag("hab_palavra_honesta")) {
            l.add(new Linha(Categoria.INTEGRIDADE,
                    "Recusou colar em uma prova", true));
        }
        if (p.temFlag("cap7_questionou_autoria")) {
            l.add(new Linha(Categoria.INTEGRIDADE,
                    "Questionou ordem de autores de artigo científico", true));
        }
        if (p.temFlag("cap7_aceitou_autoria")) {
            l.add(new Linha(Categoria.INTEGRIDADE,
                    "Aceitou ordem de autores questionável", false));
        }
        if (p.temFlag("cap7_dtec_deu_nota")) {
            l.add(new Linha(Categoria.INTEGRIDADE,
                    "Deu nota a aluno que não aprendeu", false));
        }
        if (p.temFlag("cap7_dtec_nao_deu")) {
            l.add(new Linha(Categoria.INTEGRIDADE,
                    "Recusou dar nota a aluno que não aprendeu", true));
        }

        if (!p.temFlag("item_vaga_estagio")
                && !p.temFlag("bolsista_ic")
                && !p.temFlag("cap5c_cliente_pagou")) {
            l.add(new Linha(Categoria.LACUNAS,
                    "Sem experiência remunerada formal", false));
        }
        if (!p.temFlag("bolsista_ic")
                && !p.temFlag("cap7_questionou_autoria")
                && !p.temFlag("cap5a_liderou_artigo")) {
            l.add(new Linha(Categoria.LACUNAS,
                    "Sem Lattes ativo", false));
        }
        if (!p.temFlag("industry_electronics")
                && !p.temFlag("wie_interesse")
                && !p.temFlag("ras_curiosa")) {
            l.add(new Linha(Categoria.LACUNAS,
                    "Sem participação em comunidade técnica", false));
        }
        if (!p.temFlag("cap7_indep_negociou")
                && !p.temFlag("hab_contrato_assinado")
                && !p.temFlag("cap7_indep_advogado")) {
            l.add(new Linha(Categoria.LACUNAS,
                    "Sem experiência com contrato ou negociação", false));
        }
        if (!p.temFlag("cap5b_dividiu_comida")
                && !p.temFlag("cap6_pediu_ajuda")
                && !p.temFlag("malu_reconectou")) {
            l.add(new Linha(Categoria.LACUNAS,
                    "Poucas evidências de trabalho em equipe sob pressão", false));
        }

        return l;
    }

    public static int pesoPositivo(Protagonista p) {
        int n = 0;
        for (Linha linha : linhas(p)) {
            if (linha.positiva() && linha.categoria() != Categoria.LACUNAS) n++;
        }
        return n;
    }
}