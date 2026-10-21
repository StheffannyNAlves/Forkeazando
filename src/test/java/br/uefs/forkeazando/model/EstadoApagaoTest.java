package br.uefs.forkeazando.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class EstadoApagaoTest {
    @Test
    void comecaSemDestinoPendente() {
        assertFalse(new Estado().temDestinoPendente());
    }


   @Test
    void guardaEConsomeODestinoUmaVezSo() {
        Estado estado = new Estado();
        estado.guardarDestinoPendente(122);
        assertTrue(estado.temDestinoPendente());
        assertEquals(122, estado.consumirDestinoPendente());
        assertFalse(estado.temDestinoPendente());
    }

    @Test
    void consumirSemDestinoPendenteFalhaEmVezDeLevarParaUmaCenaInvalida() {
        assertThrows(IllegalStateException.class, () -> new Estado().consumirDestinoPendente());
    }

    @Test
    void aFaixaDoApagaoSaoAsCenas970A972() {
        assertFalse(Estado.ehCenaDeApagao(Estado.CENA_APAGAO - 1));
        assertTrue(Estado.ehCenaDeApagao(Estado.CENA_APAGAO));
        assertTrue(Estado.ehCenaDeApagao(Estado.CENA_APAGAO + 1));
        assertTrue(Estado.ehCenaDeApagao(Estado.CENA_APAGAO + 2));
        assertFalse(Estado.ehCenaDeApagao(Estado.CENA_APAGAO + 3));
    }

    @Test
    void asCenasEspeciaisNaoSeConfundemEntreSi(){
        assertFalse(Estado.ehCenaDeApagao(Estado.CENA_RETOMAR));
        assertFalse(Estado.ehCenaDeApagao(Estado.CENA_ENCERRAR));
    }






}
