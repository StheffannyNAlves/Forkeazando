package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Textos de apresentação do status. Não altera as regras, flags nem o estado da partida. */
public final class StatusNarrativo {
    private static final Map<String, String> MARCOS = Map.ofEntries(
            Map.entry("guardou_pra_si", "Guardou a notícia do ingresso antes de contar à família."),
            Map.entry("hab_olho_edital", "Passou a conferir editais e comprovantes com atenção."),
            Map.entry("pediu_ajuda_grupo", "Pediu informações ao grupo de ingressantes."),
            Map.entry("aceitou_caos", "Seguiu adiante mesmo sem conhecer toda a rotina."),
            Map.entry("item_cartao_ru", "Organizou o acesso ao Restaurante Universitário."),
            Map.entry("cap1_mediu_circuito", "Mediu o circuito antes de substituir componentes na oficina."),
            Map.entry("cap1_mediu_fonte", "Mediu a fonte antes de alterar o circuito."),
            Map.entry("cap1_conversou_modulo8", "Conheceu pessoas no Módulo 8."),
            Map.entry("cap1_reconhecido_modulo8", "Fortaleceu laços no Módulo 8."),
            Map.entry("ignorou_email", "Deixou passar um aviso importante."),
            Map.entry("item_vaga_estagio", "Investigou uma oportunidade de estágio."),
            Map.entry("ras_curiosa", "Demonstrou interesse pela comunidade de robótica."),
            Map.entry("tisp_interesse", "Demonstrou interesse por tecnologia e projetos."),
            Map.entry("wie_interesse", "Explorou a comunidade de mulheres na engenharia."),
            Map.entry("cap2_so_assistiu", "Observou uma oportunidade sem se inscrever."),
            Map.entry("cap3_colou_de_fato", "Recorreu à cola durante uma avaliação."),
            Map.entry("hab_palavra_honesta", "Escolheu ser honesta diante de uma situação difícil."),
            Map.entry("cap4_leu_editais", "Consultou os editais antes de escolher uma trilha."),
            Map.entry("cap4_perguntou_alternativa", "Pediu esclarecimentos antes de definir a trilha."),
            Map.entry("cap4_procurou_caio", "Procurou Caio para discutir possibilidades."),
            Map.entry("trilha_ic", "Escolheu seguir uma trajetória em pesquisa."),
            Map.entry("trilha_dtec", "Escolheu a trilha de ensino e prática."),
            Map.entry("trilha_indep", "Escolheu realizar projetos independentes."),
            Map.entry("cap5b_foi_assembleia", "Participou da assembleia durante a greve."),
            Map.entry("cap5b_furou_greve", "Continuou atividades de monitoria durante a greve."),
            Map.entry("cap5b_neutra", "Preferiu não participar das decisões coletivas durante a greve."),
            Map.entry("cap5b_dividiu_comida", "Compartilhou recursos durante a greve."),
            Map.entry("cap5a_liderou_artigo", "Assumiu a liderança de parte de um artigo."),
            Map.entry("cap5a_perguntou_revisao", "Pediu revisão do trabalho de pesquisa."),
            Map.entry("cap5a_saiu_grupo", "Decidiu deixar um grupo de pesquisa."),
            Map.entry("cap5a_registrou_experimento", "Registrou método e condições dos experimentos."),
            Map.entry("cap5b_preparou_exercicio", "Preparou exercícios para apoiar colegas."),
            Map.entry("cap5c_bia_ajudou", "Recebeu apoio de Bia em um projeto."),
            Map.entry("item_contato_bia", "Manteve o contato de Bia para trabalhos futuros."),
            Map.entry("cap6_trancou", "Trancou o curso e ajustou o percurso acadêmico."),
            Map.entry("cap7_questionou_autoria", "Questionou a autoria de um trabalho."),
            Map.entry("cap7_indep_negociou", "Negociou as condições de um trabalho independente."),
            Map.entry("sistema_contestado", "Contestou uma classificação do Sistema Acadêmico."),
            Map.entry("mentoria_caio", "Recebeu orientação de Caio."),
            Map.entry("malu_reconectou", "Retomou o contato com Malu.")
    );

    private StatusNarrativo() {}

    public static List<String> marcos(Protagonista protagonista) {
        Objects.requireNonNull(protagonista, "A protagonista não pode ser nula");
        return protagonista.getHistoricoFlags().stream()
                .map(MARCOS::get)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    public static String objetivo(Estado estado) {
        Objects.requireNonNull(estado, "O estado não pode ser nulo");
        if (estado.isPartidaConcluida()) {
            return "Jornada concluída: consulte os finais e as conquistas alcançadas.";
        }
        if (Estado.ehCenaDeApagao(estado.getCenaAtualId())) {
            return "Atravessar o esgotamento e encontrar um caminho para retomar a jornada.";
        }
        if (estado.getCenaAtualId() == 700) {
            return "Descobrir como a trajetória universitária começou.";
        }
        return switch (estado.getCapituloAtual()) {
            case 1 -> "Organizar o ingresso e os primeiros passos na universidade.";
            case 2 -> "Explorar oportunidades e encontrar espaço na rotina acadêmica.";
            case 3 -> "Enfrentar a avaliação e lidar com suas consequências.";
            case 4 -> "Investigar opções e decidir qual trilha seguir.";
            case 5 -> "Conduzir sua trilha entre projetos, prazos e desafios da universidade.";
            case 6 -> "Reorganizar os estudos e as relações após um período de desgaste.";
            case 7 -> "Preparar, testar e defender o trabalho de conclusão de curso.";
            case 8 -> "Concluir a graduação e escolher os próximos caminhos.";
            default -> "Continuar a história e tomar a próxima decisão.";
        };
    }
}
