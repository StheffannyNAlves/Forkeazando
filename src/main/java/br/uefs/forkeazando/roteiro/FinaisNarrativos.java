
package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.PersonagemSecundario;
import br.uefs.forkeazando.model.Protagonista;

public final class FinaisNarrativos {

    private FinaisNarrativos() {}

    public static String pesquisa(Protagonista p) {
        if (p.getRelacionamento(PersonagemSecundario.DANDARA) >= 3f) {
            return """
                    Três meses depois. Você tá no laboratório, às 23h47.

                    O celular apita.

                    'Li o artigo.' — é a Dandara.

                    'Demorei duas semanas. Não entendi metade. A outra metade me deu raiva.'

                    Pausa.

                    'Quando você termina outro?'
                    """;
        }

        if (p.temFlag("caio_revelacao")) {
            return """
                    Três meses depois. Você tá no laboratório, às 23h47.

                    O celular apita.

                    'Vi teu nome no artigo.' — é o Caio.

                    'Lembrei da frase que você me disse. Ainda tá valendo, né?'

                    Você manda 'tá'. Ele manda um áudio de três minutos.

                    Você ouve até o fim.
                    """;
        }

        if (p.temFlag("cap7_questionou_autoria")) {
            String coautoria = p.temFlag("protagonista_masculino") ? "Segundo autor." : "Segunda autora.";
            return """
                    Três meses depois. O artigo foi publicado.

                    Você abre o PDF. Seu nome tá lá. %s

                    Abre o e-mail do orientador.

                    'Eu subestimei você. Não é elogio. É reconhecimento.'

                    Você fecha o e-mail.
                    """.formatted(coautoria);
        }

        return """
                Três meses depois. Você tá no laboratório, às 23h47.

                O celular não apita. O artigo não saiu ainda.

                A bolsa cobre metade das passagens.

                Você continua.
                """;
    }

    public static String tecnologia(Protagonista p) {
        if (p.temFlag("mentoria_caio") || p.temFlag("caio_revelacao")) {
            return """
                    Primeiro dia. O ambiente de dev demora 4 horas pra liberar.

                    O celular apita.

                    'Cê tá no meu time, né?' — é o Caio.

                    'Não derruba o servidor.'

                    Você ri. Ele não tá brincando. Você também não.
                    """;
        }

        if (p.temFlag("cap5c_bia_ajudou") || p.temFlag("item_contato_bia")) {
            return """
                    Primeiro dia. A empresa tem quinze pessoas.

                    Você abre o Slack. Tem mensagem da Bia:

                    'Eu avisei que o cliente ia pagar mais. Ninguém acreditou.'

                    Você não responde. Guarda o contato.
                    """;
        }

        return """
                Primeiro dia. A senha do ambiente de dev demora 4 horas pra liberar.

                Alguém do time te passa o caminho.

                O primeiro commit é pequeno.

                Não derruba nada. Ainda.
                """;
    }

    public static String internacional(Protagonista p) {
        if (p.temFlag("malu_reconectou") || p.temFlag("conheceu_malu")) {
            return """
                    A semana antes do embarque. Malas na cama.

                    O celular apita. É a Malu:

                    'Eu falei bom dia pra uma planta. Você agora fala bom dia em quantas línguas?'

                    Pausa.

                    'Quando você voltar, a gente se fala. Ou não. Só não some.'

                    Você promete. Você sabe que talvez não cumpra. Ela também.
                    """;
        }

        if (p.temFlag("item_copo_caio")) {
            return """
                    A semana antes do embarque. O copo térmico amassado tá em cima da escrivaninha.

                    Você pensa em deixar. Pensa em levar.

                    Pensa em ligar pro Caio. Não liga.

                    Leva o copo. Fecha a mala.
                    """;
        }

        return """
                A semana antes do embarque. Malas na cama.

                O celular não apita.

                Você abre a conversa com a mãe. Fecha. Abre. Fecha.

                Vai dar notícia em pessoa.
                """;
    }

    public static String independente(Protagonista p) {
        if (p.temFlag("cap5c_bia_ajudou") || p.temFlag("item_contato_bia")) {
            String so = p.temFlag("protagonista_masculino") ? "sozinho" : "sozinha";
            return """
                    O primeiro contrato grande. R$ 8 mil. Escopo claro.

                    Você lê a proposta da Bia: 50/50.

                    'Eu entro com o contato. Você entra com o código. Metade pra cada. Se der ruim, a gente divide.'

                    Você pensa em dizer não. Você diz sim.

                    Pela primeira vez, você não tá fazendo tudo %s.
                    """.formatted(so);
        }

        if (p.temFlag("cap7_indep_negociou")) {
            return """
                    O primeiro contrato grande. R$ 8 mil. Cláusula revisada.

                    O cliente pergunta se você tem CNPJ. Você manda o número.

                    Ele responde 'ok'.

                    Você fecha o notebook. Já era hora.
                    """;
        }

        return """
                O primeiro contrato pequeno. R$ 800. Cláusula confusa.

                Você lê três vezes. Não entende. Não assina.

                O cliente reclama. Você aprendeu.

                Próximo.
                """;
    }

    public static String transicao(Protagonista p) {
        if (p.temFlag("malu_reconectou") || p.temFlag("conheceu_malu")) {
            return """
                    Primeiro dia da nova área. Você tá no corredor errado.

                    O celular apita. É a Malu:

                    'Eu também mudei. Saí de Engenharia. Tô em Design.'

                    Pausa.

                    'Todo mundo acha que foi fracasso. Eu acho que foi a coisa mais honesta que eu fiz.'

                    Você guarda a mensagem. Vai procurar a sala certa.
                    """;
        }

        if (p.temFlag("mentoria_caio")) {
            return """
                    Primeiro dia da nova área. Você tá no corredor errado.

                    Lembra do Caio. Ele desistiu. Voltou. Desistiu de novo. Voltou de novo.

                    O copo térmico dele continua amassado. O seu caminho também.

                    Você segue.
                    """;
        }

        return """
                Primeiro dia da nova área. Você tá no corredor errado.

                Ninguém aqui sabe o que você fez antes. Ninguém vai perguntar.

                Você não precisa contar tudo. Só o suficiente pra começar.
                """;
    }
}
