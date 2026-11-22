package br.uefs.forkeazando.view.pixel;

import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Protagonista;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import static org.junit.jupiter.api.Assertions.*;

class ProtagonistasVisuaisTest {
    private static final Set<String> SEM_JOGADOR_VISIVEL = Set.of(
            "cap1_patio", "cap4_agata", "cap4_agata_resultado", "memoria_bancada");

    static Protagonista personagem(IdentidadeProtagonista identidade, String nome) {
        return new Protagonista(nome, identidade, false, false, false,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
    }

    @Test void identidadeVisualIndependeDoNomeLivre() {
        assertEquals(PerfilProtagonista.TIANA,
                PerfilProtagonista.de(personagem(IdentidadeProtagonista.STHEFFANNY, "Miles")));
        assertEquals(PerfilProtagonista.MILES,
                PerfilProtagonista.de(personagem(IdentidadeProtagonista.DAVI, "Tiana")));
    }

    @Test void todaCenaComJogadorPossuiSilhuetaPropriaSemRecolorirOsAmbientesVazios() {
        for (String chave : Ilustracoes.chaves()) {
            Animacao tiana = Ilustracoes.animacao(chave, PerfilProtagonista.TIANA).orElseThrow();
            Animacao miles = Ilustracoes.animacao(chave, PerfilProtagonista.MILES).orElseThrow();
            assertEquals(tiana.total(), miles.total(), chave);
            for (int i = 0; i < tiana.total(); i++) {
                boolean iguais = Arrays.equals(tiana.renderizar(i), miles.renderizar(i));
                assertEquals(SEM_JOGADOR_VISIVEL.contains(chave), iguais, chave + " / quadro " + i);
            }
        }
    }

    @Test void ambosTemEntradaCompletaERepousoSemAlterarASequenciaNarrativa() {
        for (PerfilProtagonista perfil : PerfilProtagonista.values()) {
            for (String chave : Ilustracoes.chaves()) {
                Animacao entrada = Ilustracoes.animacao(chave, perfil).orElseThrow();
                Animacao repouso = Ilustracoes.ambiente(chave, perfil).orElseThrow();
                assertTrue(entrada.total() * entrada.intervaloMs() <= 2500, chave);
                assertArrayEquals(entrada.renderizar(entrada.total() - 1), Ilustracoes.linhas(chave, perfil));
                var textosFinais = entrada.desenhar(entrada.total() - 1).textos();
                Set<Integer> estados = new HashSet<>();
                for (int q = 0; q < repouso.total(); q++) {
                    Tela quadro = repouso.desenhar(q);
                    assertEquals(66, quadro.largura());
                    assertEquals(48, quadro.altura());
                    assertEquals(textosFinais, quadro.textos(), chave);
                    estados.add(Arrays.hashCode(quadro.renderizar()));
                }
                assertTrue(estados.size() > 1, perfil + " / " + chave);
            }
        }
    }

    @Test void aparenciaENomesDosRecursosNaoVazamEntreThreadsOuCaches() throws Exception {
        try (var executor = Executors.newFixedThreadPool(2)) {
            for (String chave : Ilustracoes.chaves()) {
                String[] tiana = Ilustracoes.linhas(chave, PerfilProtagonista.TIANA);
                String[] miles = Ilustracoes.linhas(chave, PerfilProtagonista.MILES);
                var a = executor.submit(() -> Ilustracoes.linhas(chave, PerfilProtagonista.TIANA));
                var b = executor.submit(() -> Ilustracoes.linhas(chave, PerfilProtagonista.MILES));
                assertArrayEquals(tiana, a.get());
                assertArrayEquals(miles, b.get());
                a.get()[0] = "alterado pelo consumidor";
                assertArrayEquals(tiana, Ilustracoes.linhas(chave, PerfilProtagonista.TIANA));
            }
        }
    }

    @Test void silhuetasMantemComprimentoDeCabeloRoupaEExpressaoEmTodasAsPoses() {
        for (PerfilProtagonista perfil : PerfilProtagonista.values()) {
            ProtagonistaVisual jogador = new ProtagonistaVisual(perfil, 0);
            Tela costas = new Tela(40, 24);
            jogador.costas(costas, 20, 5, 16, 20);
            assertEquals(perfil.roupa(), costas.get(20, 23));
            assertEquals(perfil == PerfilProtagonista.TIANA, costas.get(17, 17) == perfil.cabelo());
            Tela frente = new Tela(40, 24);
            jogador.frente(frente, 20, 5, 18, ProtagonistaVisual.Expressao.SURPRESA);
            assertEquals(perfil.roupa(), frente.get(26, 20));
            assertNotEquals(0, frente.get(20, 12));
        }
    }

    @Test void repousoPreservaIndicadoresEFinaisDasDuasIdentidades() {
        for (PerfilProtagonista perfil : PerfilProtagonista.values()) {
            var tecnologia = Ilustracoes.ambiente("futuro_tecnologia", perfil).orElseThrow();
            var memoria = Ilustracoes.ambiente("memoria_bancada", perfil).orElseThrow();
            var led = Ilustracoes.animacao("memoria_bancada", perfil).orElseThrow();
            int apagado = led.desenhar(led.total() - 1).get(30, 17);
            for (int q = 0; q < 12; q++) {
                assertTrue(((tecnologia.desenhar(q).get(49, 30) >> 8) & 255) > 200);
                assertEquals(apagado, memoria.desenhar(q).get(30, 17));
            }
        }
    }
}
