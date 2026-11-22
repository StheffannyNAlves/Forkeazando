package br.uefs.forkeazando.view.pixel;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.*;

class AnimacoesCompletasTest {
    private static long assinatura(Tela t) {
        long hash = 1;
        for (int y = 0; y < t.altura(); y++) for (int x = 0; x < t.largura(); x++) hash = hash * 31 + t.get(x, y);
        return hash * 31 + t.textos().hashCode();
    }
    @Test void cadaAmbienteTemEntradaCompletaERepousoComMovimento() {
        for (String chave : Ilustracoes.chaves()) {
            Animacao entrada = Ilustracoes.animacao(chave).orElseThrow();
            Animacao repouso = Ilustracoes.ambiente(chave).orElseThrow();
            var estados = new HashSet<Long>();
            for (int i = 0; i < repouso.total(); i++) {
                Tela quadro = repouso.desenhar(i);
                assertEquals(66, quadro.largura(), chave);
                assertEquals(48, quadro.altura(), chave);
                estados.add(assinatura(quadro));
            }
            assertTrue(estados.size() > 1, "Repouso estático: " + chave);
            assertTrue(entrada.total() > 1, "Entrada incompleta: " + chave);
        }
    }
    @Test void repousoNaoApagaRotulosNemRetrocedeResultadosDaEntrada() {
        for (String chave : Ilustracoes.chaves()) {
            Animacao entrada = Ilustracoes.animacao(chave).orElseThrow();
            var textos = entrada.desenhar(entrada.total() - 1).textos();
            Animacao repouso = Ilustracoes.ambiente(chave).orElseThrow();
            for (int i = 0; i < repouso.total(); i++) assertEquals(textos, repouso.desenhar(i).textos(), chave);
        }
    }
    @Test void textosSaoUmaCopiaImutavelEConservamCorEPosicao() {
        Tela tela = new Tela(20, 4);
        tela.texto(2, 1, "UEFP", 0xffcc88);
        var textos = tela.textos();
        assertEquals(new Tela.Texto(2, 1, "UEFP", 0xffcc88), textos.getFirst());
        assertThrows(UnsupportedOperationException.class, textos::clear);
        tela.texto(2, 1, "CSA ", 0xffcc88);
        assertEquals("UEFP", textos.getFirst().valor());
    }
    @Test void memoriaDoFinalMantemOLedApagadoEmTodosOsQuadros() {
        Animacao ambiente = Ilustracoes.ambiente("memoria_bancada").orElseThrow();
        Animacao entrada = Ilustracoes.animacao("memoria_bancada").orElseThrow();
        int apagado = entrada.desenhar(entrada.total() - 1).get(30,17);
        for (int i = 0; i < ambiente.total(); i++) assertEquals(apagado, ambiente.desenhar(i).get(30,17));
    }
    @Test void agataConcluiProcessamentoSemAnunciarResultadoDurantePerguntas() {
        var a = Ilustracoes.animacao("cap4_agata_resultado").orElseThrow();
        var fim = a.desenhar(a.total()-1);
        assertTrue(fim.textos().stream().anyMatch(t -> t.valor().contains("PERFIL GERADO")));
        var perguntas = Ilustracoes.animacao("cap4_agata").orElseThrow();
        assertFalse(perguntas.desenhar(perguntas.total()-1).textos().stream()
                .anyMatch(t -> t.valor().contains("PERFIL GERADO")));
    }

    @Test void onibusEscreveOPDaUefpESeguraAPalavraAntesDeApagar() {
        assertEquals("X....", Elementos.letra('F')[6], "F não pode usar o glifo de R");
        assertEquals("X....", Elementos.letra('P')[6], "P não pode usar o glifo de R");
        Animacao a = Ilustracoes.animacao("cap1_onibus").orElseThrow();
        Tela antesDoP = a.desenhar(5), completo = a.desenhar(11);
        int diferencas = 0;
        for (int y = 10; y < 24; y++) for (int x = 44; x < 54; x++)
            if (antesDoP.get(x,y) != completo.get(x,y)) diferencas++;
        assertTrue(diferencas > 10, "A quarta letra precisa ser desenhada");
        assertArrayEquals(a.renderizar(11), a.renderizar(13), "A palavra precisa permanecer legível antes da palma");
    }
    @Test void gorrosTerminamOArcoAntesDoRepousoDaFormatura() {
        Animacao a = Ilustracoes.animacao("cap8_formatura").orElseThrow();
        assertTrue(contarCor(a.desenhar(13), 0, 22, Cor.rgb(20,20,28)) > 0);
        assertEquals(0, contarCor(a.desenhar(a.total()-1), 0, 22, Cor.rgb(20,20,28)));
    }
    @Test void aviaoConcluiADecolagemSaindoDaJanela() {
        Animacao a = Ilustracoes.animacao("futuro_internacional").orElseThrow();
        assertTrue(contarCor(a.desenhar(0), 0, 48, Cor.rgb(246,246,252)) > 0);
        assertEquals(0, contarCor(a.desenhar(a.total()-1), 0, 48, Cor.rgb(246,246,252)));
    }
    @Test void placaTerminaComIndicadorDeAlimentacaoAceso() {
        Animacao a = Ilustracoes.animacao("futuro_tecnologia").orElseThrow();
        int led = a.desenhar(a.total()-1).get(49,30);
        assertTrue(((led >> 8) & 255) > 200);
    }
    private static int contarCor(Tela t, int inicioY, int fimY, int cor) {
        int total=0;
        for (int y=inicioY;y<fimY;y++) for(int x=0;x<t.largura();x++) if(t.get(x,y)==cor) total++;
        return total;
    }

}
