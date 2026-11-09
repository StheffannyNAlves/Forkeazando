package br.uefs.forkeazando.view;
import br.uefs.forkeazando.model.ResumoSlot;
import java.time.format.DateTimeFormatter;

public final class ResumoSlotView {
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
    private ResumoSlotView(){}

    public static String formatar(ResumoSlot slot){
        return switch (slot.status()){
            case VAZIO -> "Slot " + slot.numero() + " - Vazio";
            case CORROMPIDO -> "Slot " + slot.numero() + " - Corrompido";
            case OCUPADO -> "Slot " + slot.numero() + " - Ocupado" +
                    " | " + slot.nome()
                    + " | Capítulo " + slot.capitulo()
                    + " | " + slot.data().format(FORMATO_DATA)
                    + "   Score: " + slot.score() + "/100"
                    + " | Participação: " + slot.participacao() + "/100"
                    + " | Rank: " + slot.rank()
                    + " | Energia: " + slot.energia() + "/" + slot.energiaMaxima();
        };
    }
}
