package br.uefs.forkeazando.roteiro;

import static org.junit.jupiter.api.Assertions.*;
import br.uefs.forkeazando.model.Final;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.view.pixel.Ilustracoes;
import org.junit.jupiter.api.Test;

class VisuaisDeFinalTest {

        private static Protagonista protagonista(){
            return new Protagonista("Teste", false, true, true,
                    Protagonista.NivelVidaSocial.EQUILIBRADO,
                    Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                    Protagonista.SituacaoEconomica.ESTAVEL);
        }

        @Test
        void  todoFinalTemUmaIlustracao(){
            for (Final f: Final.values()){
                String chave = VisuaisDeFinal.ilustracaoDe(f);
                assertTrue(Ilustracoes.existe(chave), "final" + f + " aponta para uma arte inexistente: " + chave);
            }
        }

        @Test
        void ilustracaoPadraoExiste(){
            assertTrue(Ilustracoes.existe(VisuaisDeFinal.ILUSTRACAO_PADRAO));
        }

        @Test
        void semConclusaoUsaIlustracaoETituloPadrao(){
            Protagonista p = protagonista();
            assertEquals(VisuaisDeFinal.ILUSTRACAO_PADRAO, VisuaisDeFinal.ilustracaoDe(p));
            assertEquals(VisuaisDeFinal.TITULO_PADRAO, VisuaisDeFinal.tituloDe(p));

        }


    @Test
    void cadaFinalDeCarreiraIdentificadoUsaASuaIlustracaoESeuTitulo() {
            for (Final f : Final.values()) {
            Protagonista p = protagonista();
            p.adicionarFlag(Final.FLAG_CONCLUSAO);
            p.adicionarFlag(f.getTrilha());
            assertEquals(VisuaisDeFinal.ilustracaoDe(f), VisuaisDeFinal.ilustracaoDe(p), "arte do final " + f);
            assertEquals(f.getTitulo(), VisuaisDeFinal.tituloDe(p), "título do final " + f);
        }
    }

    @Test
    void asArtesDosFinaisDeCarreiraSaoTodasDiferentes() {
        long distintas = java.util.Arrays.stream(Final.values()).map(VisuaisDeFinal::ilustracaoDe).distinct().count();
        assertEquals(Final.values().length, distintas);
    }


















}
