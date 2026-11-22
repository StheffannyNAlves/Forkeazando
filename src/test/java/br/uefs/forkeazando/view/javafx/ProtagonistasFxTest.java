package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.roteiro.ApresentacaoDeCena;
import br.uefs.forkeazando.view.pixel.Ilustracoes;
import br.uefs.forkeazando.view.pixel.PerfilProtagonista;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ProtagonistasFxTest {
    @BeforeAll static void iniciarToolkit() throws Exception {
        assumeTrue(!System.getProperty("os.name").toLowerCase().contains("linux") || System.getenv("DISPLAY") != null,
                "A validação gráfica requer DISPLAY (ex.: Xvfb)");
        CountDownLatch pronto = new CountDownLatch(1);
        try { Platform.startup(pronto::countDown); }
        catch (IllegalStateException iniciado) { pronto.countDown(); }
        assertTrue(pronto.await(10, TimeUnit.SECONDS));
        Platform.setImplicitExit(false);
    }

    private static <T> T naFx(Supplier<T> acao) throws Exception {
        FutureTask<T> tarefa = new FutureTask<>(acao::get);
        Platform.runLater(tarefa);
        return tarefa.get(8, TimeUnit.SECONDS);
    }

    private static void aguardarImagem(ImageView imagem) throws Exception {
        CountDownLatch pronto = new CountDownLatch(1);
        naFx(() -> {
            Timeline verificar = new Timeline(new KeyFrame(Duration.millis(20), e -> {
                if (imagem.getImage().getPixelReader().getArgb(395, 287) >>> 24 == 255) pronto.countDown();
            }));
            verificar.setCycleCount(250);
            AnimacoesFx.executar(imagem, "teste-espera", verificar);
            return null;
        });
        assertTrue(pronto.await(6, TimeUnit.SECONDS), "O executor de arte deve entregar a imagem");
    }

    private static int hashImagem(ImageView imagem) {
        int[] pixels = new int[396 * 288];
        imagem.getImage().getPixelReader().getPixels(0, 0, 396, 288,
                javafx.scene.image.PixelFormat.getIntArgbInstance(), pixels, 0, 396);
        return Arrays.hashCode(pixels);
    }

    @Test void alternarIdentidadeComAMesmaChaveNaoReaproveitaOJogadorDoCache() throws Exception {
        ArtesFx artes = naFx(ArtesFx::new);
        VBox painel = naFx(() -> { VBox p = new VBox(); new Scene(p); return p; });
        try {
            for (PerfilProtagonista perfil : new PerfilProtagonista[]{PerfilProtagonista.TIANA,
                    PerfilProtagonista.MILES, PerfilProtagonista.TIANA, PerfilProtagonista.MILES}) {
                ImageView imagem = naFx(() -> {
                    painel.getChildren().clear();
                    artes.exibirIlustracao(painel, "cap1_matricula", perfil, false);
                    return (ImageView) painel.getChildren().getFirst();
                });
                aguardarImagem(imagem);
                Set<Integer> esperados = new HashSet<>();
                var entrada = Ilustracoes.animacao("cap1_matricula", perfil).orElseThrow();
                // O primeiro pulso pode ainda mostrar a composição final da entrada.
                esperados.add(Arrays.hashCode(RasterizadorCenario.rasterizar(
                        entrada.desenhar(entrada.total() - 1), 6)));
                var ambiente = Ilustracoes.ambiente("cap1_matricula", perfil).orElseThrow();
                for (int q = 0; q < ambiente.total(); q++)
                    esperados.add(Arrays.hashCode(RasterizadorCenario.rasterizar(ambiente.desenhar(q), 6)));
                assertTrue(naFx(() -> esperados.contains(hashImagem(imagem))), "Aparência errada: " + perfil);
            }
        } finally {
            naFx(() -> { AnimacoesFx.pararArvore(painel); artes.close(); return null; });
        }
    }

    @Test void resultadoAnteriorPreparadoSemCenaNaoApareceDepoisDeNovaSessao() throws Exception {
        ArtesFx artes = naFx(ArtesFx::new);
        VBox antiga = naFx(VBox::new);
        VBox atual = naFx(() -> { VBox p = new VBox(); new Scene(p); return p; });
        try {
            ImageView antigaImagem = naFx(() -> {
                artes.exibirIlustracao(antiga, "cap1_oficina_led", PerfilProtagonista.TIANA, true);
                artes.resetar();
                return (ImageView) antiga.getChildren().getFirst();
            });
            ImageView nova = naFx(() -> {
                artes.exibirIlustracao(atual, "cap1_oficina_led", PerfilProtagonista.MILES, false);
                return (ImageView) atual.getChildren().getFirst();
            });
            aguardarImagem(nova);
            naFx(() -> {
                new Scene(antiga);
                assertEquals(0, antigaImagem.getImage().getPixelReader().getArgb(395, 287) >>> 24,
                        "O resultado da sessão anterior deve continuar descartado");
                return null;
            });
        } finally {
            naFx(() -> { AnimacoesFx.pararArvore(antiga); AnimacoesFx.pararArvore(atual); artes.close(); return null; });
        }
    }

    @Test void retratosDosDoisProtagonistasMantemExpressoesERecursosProprios() throws Exception {
        naFx(() -> {
            try (ArtesFx artes = new ArtesFx()) {
                for (String falante : new String[]{"protagonista_stheffanny", "protagonista_masculino"}) {
                    for (String expressao : new String[]{"neutra", "feliz", "pensativa", "surpresa", "triste", "brava"}) {
                        HBox painel = new HBox(); new Scene(painel);
                        artes.resetarRetrato();
                        artes.exibirRetrato(painel, ApresentacaoDeCena.comRetrato(null, falante, expressao));
                        ImageView no = (ImageView) painel.getChildren().getFirst();
                        String pasta = falante.equals("protagonista_stheffanny") ? "stheffanny" : falante;
                        assertTrue(no.getImage().getUrl().endsWith(pasta + "/expressions/" + expressao + ".png"));
                        assertFalse(no.isSmooth());
                        AnimacoesFx.pararArvore(painel);
                    }
                }
            }
            return null;
        });
    }
}
