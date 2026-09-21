package br.uefs.forkeazando.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EstadoTest {

    @Test
    void capituloComecaEm1EAvancaCorretamente() {
        Estado estado = new Estado();
        assertEquals(1, estado.getCapituloAtual());

        estado.avancarCapitulo();

        assertEquals(2, estado.getCapituloAtual());
    }

    @Test
    void cenaAtualComecaEm0EMudaComSet() {
        Estado estado = new Estado();
        assertEquals(0, estado.getCenaAtualId());

        estado.setCenaAtualId(9);

        assertEquals(9, estado.getCenaAtualId());
    }

    @Test
    void iniciarDeveGuardarOProtagonista() {
        CaracteristicasProtagonista caracteristicas = new CaracteristicasProtagonista(
                false, false, false,
                CaracteristicasProtagonista.NivelVidaSocial.EQUILIBRADO,
                CaracteristicasProtagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                CaracteristicasProtagonista.SituacaoEconomica.ESTAVEL
        );
        Protagonista protagonista = new Protagonista(caracteristicas);
        Estado estado = new Estado();

        estado.iniciar(protagonista);

        assertEquals(protagonista, estado.getProtagonista());
    }
}