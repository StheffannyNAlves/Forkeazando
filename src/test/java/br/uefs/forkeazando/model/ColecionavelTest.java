package br.uefs.forkeazando.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ColecionavelTest {

    @Test
    void deveEncontrarColecionavelPelaFlag() {
        assertEquals(
                Colecionavel.OLHO_EDITAL,
                Colecionavel.porFlag("hab_olho_edital").orElseThrow()
        );

        assertEquals(
                Colecionavel.CARTAO_RU,
                Colecionavel.porFlag("item_cartao_ru").orElseThrow()
        );
    }

    @Test
    void deveRetornarVazioParaFlagDesconhecida() {
        assertTrue(Colecionavel.porFlag("flag_inexistente").isEmpty());
    }

    @Test
    void deveRetornarVazioParaFlagNula() {
        assertTrue(Colecionavel.porFlag(null).isEmpty());
    }

    @Test
    void todasAsFlagsDevemSerDistintas() {
        Set<String> flags = new HashSet<>();

        for (Colecionavel colecionavel : Colecionavel.values()) {
            assertTrue(
                    flags.add(colecionavel.getFlag()),
                    "Flag duplicada: " + colecionavel.getFlag()
            );
        }
    }

    @Test
    void habilidadesDevemTerPrefixoHab() {
        for (Colecionavel colecionavel : Colecionavel.values()) {
            if (colecionavel.getTipo() == Colecionavel.Tipo.HABILIDADE) {
                assertTrue(
                        colecionavel.getFlag().startsWith("hab_"),
                        "Habilidade com prefixo inválido: " + colecionavel.getFlag()
                );
            }
        }
    }

    @Test
    void itensDevemTerPrefixoItem() {
        for (Colecionavel colecionavel : Colecionavel.values()) {
            if (colecionavel.getTipo() == Colecionavel.Tipo.ITEM) {
                assertTrue(
                        colecionavel.getFlag().startsWith("item_"),
                        "Item com prefixo inválido: " + colecionavel.getFlag()
                );
            }
        }
    }

    @Test
    void catalogoDeveTerSeteHabilidadesEOitoItens() {
        long habilidades = 0;
        long itens = 0;

        for (Colecionavel colecionavel : Colecionavel.values()) {
            if (colecionavel.getTipo() == Colecionavel.Tipo.HABILIDADE) {
                habilidades++;
            } else if (colecionavel.getTipo() == Colecionavel.Tipo.ITEM) {
                itens++;
            }
        }

        assertEquals(7, habilidades);
        assertEquals(8, itens);
    }
}
