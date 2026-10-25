package br.uefs.forkeazando.view;

import br.uefs.forkeazando.model.ResumoSlot;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ResumoSlotViewTest {

    @Test
    void slotVazio() {
        var slot = new ResumoSlot(1, ResumoSlot.Status.VAZIO, null, 0, null, 0, 0, null, 0, 0);
        assertEquals("Slot 1 - Vazio", ResumoSlotView.formatar(slot));
    }

    @Test
    void slotCorrompido() {
        var slot = new ResumoSlot(2, ResumoSlot.Status.CORROMPIDO, null, 0, null, 0, 0, null, 0, 0);

        assertEquals("Slot 2 - Corrompido", ResumoSlotView.formatar(slot));
    }

    @Test
    void slotOcupadoMostraTodosOsMetadados() {
        var slot = new ResumoSlot(3, ResumoSlot.Status.OCUPADO, "Ana", 4,
                LocalDateTime.of(2026, 10, 18, 14, 5), 55, 20, "Veterano Casca Grossa", 30, 60);

        assertEquals("Slot 3 - Ocupado | Ana | Capítulo 4 | 18/10/26 14:05   Score: 55/100"
                        + " | Participação: 20/100 | Rank: Veterano Casca Grossa | Energia: 30/60",
                ResumoSlotView.formatar(slot));
    }
}