
package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.model.PersonagemSecundario;

import java.util.Objects;

public final class NarrativaDinamica {

    private NarrativaDinamica() {}

    public static String resolver(Cena cena, Protagonista p, int semestresExtras) {
        Objects.requireNonNull(cena);
        Objects.requireNonNull(p);
        String texto = NarrativaProtagonistas.resolver(cena, p);
        texto = texto.replace("{{NOTA_CALCULO}}", ResultadoCalculo.notaFormatada(p));
        texto = texto.replace("{{COMENTARIO_NOTA_CALCULO}}", ResultadoCalculo.comentario(p, cena.getId()));
        texto = texto.replace("{{APROVACAO}}", "");
        texto = texto.replace("{{REMENTENTE_LINKEDIN}}", remetenteLinkedIn(p));
        texto = texto.replace("{{ORIENTADOR_RECURSO}}", orientadorRecurso(p));
        texto = texto.replace("{{DEPOIS_DA_PORTA}}", depoisDaPorta(p));
        texto = texto.replace("{{CALENDARIO_CAP7}}", calendarioCap7(semestresExtras));
        texto = texto.replace("{{CALENDARIO_CAP8}}", calendarioCap8(semestresExtras));
        texto = texto.replace("{{FINAL_PESQUISA}}", FinaisNarrativos.pesquisa(p));
        texto = texto.replace("{{FINAL_TECNOLOGIA}}", FinaisNarrativos.tecnologia(p));
        texto = texto.replace("{{FINAL_INTERNACIONAL}}", FinaisNarrativos.internacional(p));
        texto = texto.replace("{{FINAL_INDEPENDENTE}}", FinaisNarrativos.independente(p));
        texto = texto.replace("{{FINAL_TRANSICAO}}", FinaisNarrativos.transicao(p));
        return texto;
    }

    private static String remetenteLinkedIn(Protagonista p) {
        if (p.temFlag("conheceu_malu")) {
            return "a Malu. Aquela do primeiro dia, do "
                    + "\"bom dia pra uma planta\". "
                    + "Ela não desistiu de você";
        }

        if (p.temFlag("cap3_colou_de_fato")) {
            return "a Bia. Você nem lembrava que tinham se adicionado";
        }

        return "uma colega de outro semestre";
    }

    private static String orientadorRecurso(Protagonista p) {
        if (!p.temFlag("sistema_contestado")) {
            return "";
        }

        return "'Você recorreu da classificação. Eu vi.'\n\n"
                + "'O recurso leva quinze dias. "
                + "O laboratório não espera.'\n\n"
                + "'Bem-vinda.'";
    }

    private static String depoisDaPorta(Protagonista p) {
        if (p.getRelacionamento(PersonagemSecundario.VETERANO) >= 2f) {
            return "É o Caio.\n\n"
                    + "Ele entra sem esperar resposta. "
                    + "Senta no chão do quarto. "
                    + "Não pergunta se você tá bem. "
                    + "Não fala nada.\n\n"
                    + "Uma hora.\n\nDuas horas.\n\n"
                    + "Ninguém fala nada.";
        }

        return "Você não abre.\n\n"
                + "O som some depois de um tempo. "
                + "Ou a pessoa foi embora.\n\n"
                + "(Talvez o Caio.)\n\n"
                + "(Talvez ninguém.)";
    }

    private static String calendarioCap7(int semestres) {
        if (semestres > 0) {
            return "O trancamento acrescentou " + semestres
                    + (semestres == 1 ? " semestre" : " semestres")
                    + " ao seu calendário. O registro guarda a data, "
                    + "mas não explica o que tornou a pausa necessária.";
        }

        return "Você continuou no calendário previsto. "
                + "Isso não significa que a crise tenha passado "
                + "sem deixar marcas na sua relação com o tempo.";
    }

    private static String calendarioCap8(int semestres) {
        if (semestres > 0) {
            return "Sua formatura chegou depois do calendário inicial, com "
                    + semestres + " "
                    + (semestres == 1
                    ? "semestre extra registrado"
                    : "semestres extras registrados")
                    + ". O histórico mostra o tempo, mas não tudo "
                    + "o que você precisou reorganizar.";
        }

        return "Você chegou à formatura no calendário previsto. "
                + "Isso não quer dizer que o caminho tenha sido simples "
                + "ou que você tenha atravessado tudo sem pausa.";
    }
}
