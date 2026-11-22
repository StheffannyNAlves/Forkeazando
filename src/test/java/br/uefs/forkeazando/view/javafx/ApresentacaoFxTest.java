package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.roteiro.ApresentacaoDeCena;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/** Requer um display gráfico; o restante da suíte continua disponível sem ele. */
class ApresentacaoFxTest {
    @BeforeAll static void iniciarToolkit() throws Exception {
        boolean linux = System.getProperty("os.name").toLowerCase().contains("linux");
        assumeTrue(!linux || System.getenv("DISPLAY") != null, "Testes JavaFX requerem DISPLAY (ex.: Xvfb)");
        CountDownLatch pronto = new CountDownLatch(1);
        try { Platform.startup(pronto::countDown); }
        catch (IllegalStateException iniciado) { pronto.countDown(); }
        assertTrue(pronto.await(10, TimeUnit.SECONDS));
        Platform.setImplicitExit(false);
    }

    private static void naFx(Runnable acao) throws Exception {
        FutureTask<Void> tarefa = new FutureTask<>(acao, null);
        Platform.runLater(tarefa);
        tarefa.get(8, TimeUnit.SECONDS);
    }

    @Test void substituirEfeitoInterrompeAnterior() throws Exception {
        naFx(() -> {
            Label no = new Label("Teste");
            FadeTransition antes = new FadeTransition(Duration.seconds(10), no);
            FadeTransition depois = new FadeTransition(Duration.seconds(10), no);
            AnimacoesFx.executar(no, "entrada", antes);
            AnimacoesFx.executar(no, "entrada", depois);
            assertEquals(Animation.Status.STOPPED, antes.getStatus());
            assertEquals(Animation.Status.RUNNING, depois.getStatus());
            AnimacoesFx.parar(no);
            assertEquals(Animation.Status.STOPPED, depois.getStatus());
        });
    }

    @Test void removerNoDaCenaCancelaAnimacaoEPausaPendente() throws Exception {
        naFx(() -> {
            Button no = new Button("Escolha");
            VBox raiz = new VBox(no);
            new Scene(raiz);
            Timeline ciclo = new Timeline(new javafx.animation.KeyFrame(Duration.seconds(10)));
            ciclo.setCycleCount(Animation.INDEFINITE);
            AnimacoesFx.executar(no, "ciclo", ciclo);
            AnimacoesFx.notificar(no, Duration.seconds(10), () -> fail("Notificação cancelada não deve concluir"));
            raiz.getChildren().clear();
            assertEquals(Animation.Status.STOPPED, ciclo.getStatus());
            assertEquals(1, no.getOpacity());
            assertEquals(0, no.getTranslateY());
        });
    }

    @Test void modalPausaEfeitosAtuaisENovosERetomaMesmoPonto() throws Exception {
        naFx(() -> {
            Label no = new Label("Texto");
            VBox raiz = new VBox(no);
            new Scene(raiz);
            Timeline atual = new Timeline(new javafx.animation.KeyFrame(Duration.seconds(10)));
            AnimacoesFx.executar(no, "atual", atual);
            AnimacoesFx.pausarArvore(raiz, true);
            assertEquals(Animation.Status.PAUSED, atual.getStatus());
            Timeline novo = new Timeline(new javafx.animation.KeyFrame(Duration.seconds(10)));
            AnimacoesFx.executar(no, "novo", novo);
            assertEquals(Animation.Status.PAUSED, novo.getStatus());
            AnimacoesFx.pausarArvore(raiz, false);
            assertEquals(Animation.Status.RUNNING, atual.getStatus());
            assertEquals(Animation.Status.RUNNING, novo.getStatus());
            AnimacoesFx.pararArvore(raiz);
        });
    }

    @Test void pausaAlcancaConteudoAntesDaSkinDoScrollPane() throws Exception {
        naFx(() -> {
            Label texto = new Label("Uma fala que ainda está sendo revelada.");
            var rolagem = new javafx.scene.control.ScrollPane(new VBox(texto));
            VBox raiz = new VBox(rolagem);
            new Scene(raiz);
            Timeline escrita = AnimacoesFx.revelarDialogo(texto, 0);
            AnimacoesFx.pausarArvore(raiz, true);
            assertEquals(Animation.Status.PAUSED, escrita.getStatus());
            AnimacoesFx.pausarArvore(raiz, false);
            assertEquals(Animation.Status.RUNNING, escrita.getStatus());
            AnimacoesFx.pararArvore(raiz);
        });
    }

    @Test void completarPreservaUnicodeEHandlerExistente() throws Exception {
        naFx(() -> {
            String completo = "Olá 👩🏽‍💻, UEFP!\nAção e e\u0301.";
            Label texto = new Label(completo);
            javafx.event.EventHandler<javafx.scene.input.MouseEvent> anterior = e -> {};
            texto.setOnMouseClicked(anterior);
            Timeline escrita = AnimacoesFx.revelarDialogo(texto, 0);
            assertEquals("", texto.getText());
            assertTrue(AnimacoesFx.completarDialogo(texto));
            assertEquals(completo, texto.getText());
            assertEquals(Animation.Status.STOPPED, escrita.getStatus());
            assertSame(anterior, texto.getOnMouseClicked());
            assertFalse(AnimacoesFx.completarDialogo(texto));
        });
    }

    @Test void velocidadeEPontuacaoAlteramRitmoSemCortarTexto() throws Exception {
        naFx(() -> {
            Label lenta = new Label("Ação, atenção!"), rapida = new Label("Ação, atenção!");
            Timeline a = AnimacoesFx.revelarDialogo(lenta, 0);
            Timeline b = AnimacoesFx.revelarDialogo(rapida, 2);
            assertTrue(a.getTotalDuration().greaterThan(b.getTotalDuration()));
            Label pontuada = new Label("a, b"), lisa = new Label("a  b");
            Timeline c = AnimacoesFx.revelarDialogo(pontuada, 1);
            Timeline d = AnimacoesFx.revelarDialogo(lisa, 1);
            assertTrue(c.getTotalDuration().greaterThan(d.getTotalDuration()));
            for (Label l : new Label[]{lenta, rapida, pontuada, lisa}) AnimacoesFx.parar(l);
            assertEquals("Ação, atenção!", rapida.getText());
        });
    }

    @Test void falaLongaNaoDeslocaEscolhasAoSerRevelada() throws Exception {
        naFx(() -> {
            DialogoFx fala = new DialogoFx("A matrícula trouxe novas perguntas, novas pessoas e novas escolhas. ".repeat(24));
            Button escolha = new Button("Continuar");
            VBox raiz = new VBox(12, fala, escolha);
            new Scene(raiz, 560, 800);
            raiz.applyCss(); raiz.layout();
            double antes = escolha.getLayoutY();
            fala.iniciar(0);
            raiz.applyCss(); raiz.layout();
            assertEquals(antes, escolha.getLayoutY(), 0.5);
            assertTrue(fala.completar());
            raiz.applyCss(); raiz.layout();
            assertEquals(antes, escolha.getLayoutY(), 0.5);
            fala.encerrar();
        });
    }

    @Test void respiracaoPreservaExpressaoEUsaRetratoExistente() throws Exception {
        naFx(() -> {
            try (ArtesFx artes = new ArtesFx()) {
                HBox painel = new HBox();
                new Scene(painel);
                artes.exibirRetrato(painel, ApresentacaoDeCena.comRetrato(null, "caio", "feliz"));
                ImageView retrato = (ImageView) painel.getChildren().getFirst();
                assertEquals(128, retrato.getImage().getWidth());
                assertTrue(retrato.getImage().getUrl().endsWith("/expressions/feliz.png"));
                assertFalse(retrato.isSmooth());
                AnimacoesFx.pararArvore(painel);
            }
        });
    }

    @Test void expressaoEspecificaDoRoteiroMantemFallbackPixelOriginal() throws Exception {
        naFx(() -> {
            try (ArtesFx artes = new ArtesFx()) {
                HBox painel = new HBox();
                new Scene(painel);
                artes.exibirRetrato(painel, ApresentacaoDeCena.comRetrato(null, "caio", "torto"));
                assertEquals(1, painel.getChildren().size());
                ImageView retrato = (ImageView) painel.getChildren().getFirst();
                assertNull(retrato.getImage().getUrl());
                assertFalse(retrato.isSmooth());
                AnimacoesFx.pararArvore(painel);
            }
        });
    }

    @Test void cenarioPreservaAlturaCompletaEPixelsInteiros() throws Exception {
        CountDownLatch pronto = new CountDownLatch(1);
        AtomicReference<ImageView> exibida = new AtomicReference<>();
        AtomicReference<ArtesFx> renderizador = new AtomicReference<>();
        AtomicReference<VBox> painel = new AtomicReference<>();
        naFx(() -> {
            ArtesFx artes = new ArtesFx();
            renderizador.set(artes);
            VBox raiz = new VBox();
            painel.set(raiz);
            new Scene(raiz);
            artes.exibirIlustracao(raiz, "cap1_matricula", false);
            ImageView imagem = (ImageView) raiz.getChildren().getFirst();
            exibida.set(imagem);
            Timeline verificar = new Timeline(new javafx.animation.KeyFrame(Duration.millis(20), e -> {
                if ((imagem.getImage().getPixelReader().getArgb(395, 287) >>> 24) == 255) pronto.countDown();
            }));
            verificar.setCycleCount(100);
            AnimacoesFx.executar(raiz, "aguardar", verificar);
        });
        try {
            assertTrue(pronto.await(5, TimeUnit.SECONDS), "O desenho deve chegar à interface sem bloqueá-la");
            naFx(() -> {
                var imagem = exibida.get().getImage();
                assertEquals(396, imagem.getWidth());
                assertEquals(288, imagem.getHeight());
                var pixels = imagem.getPixelReader();
                assertEquals(pixels.getArgb(0, 0), pixels.getArgb(5, 5));
                var original = br.uefs.forkeazando.view.pixel.Ilustracoes.animacao("cap1_matricula").orElseThrow();
                var ultimo = original.desenhar(original.total() - 1);
                assertEquals(0xFF000000 | ultimo.get(65, 47), pixels.getArgb(395, 287));
            });
        } finally {
            naFx(() -> {
                AnimacoesFx.pararArvore(painel.get());
                renderizador.get().close();
            });
        }
    }

    @Test void notificacoesNaoSeEmpilhamSobreNarrativa() throws Exception {
        naFx(() -> {
            AvisosFx avisos = new AvisosFx();
            new Scene(new VBox(avisos));
            avisos.mostrar("CONQUISTA DESBLOQUEADA", "Primeiro passo");
            avisos.mostrar("RELACIONAMENTO", "Seu relacionamento com Malu melhorou.");
            avisos.mostrar("AUTOSAVE", "Progresso salvo automaticamente.");
            assertEquals(1, avisos.getChildren().size());
            assertTrue(avisos.isMouseTransparent());
            assertEquals("CONQUISTA DESBLOQUEADA",
                    ((Label) ((VBox) avisos.getChildren().getFirst()).getChildren().getFirst()).getText());
            AnimacoesFx.pararArvore(avisos);
        });
    }
    @Test void imagemPreparadaAntesDaAnexacaoApareceEContinuaAnimada() throws Exception {
        CountDownLatch terminou = new CountDownLatch(1);
        AtomicReference<ArtesFx> renderer = new AtomicReference<>();
        AtomicReference<VBox> painel = new AtomicReference<>();
        long[] assinaturas = new long[2];
        naFx(() -> {
            ArtesFx artes = new ArtesFx(); renderer.set(artes);
            VBox raiz = new VBox(); painel.set(raiz);
            artes.exibirIlustracao(raiz, "cap1_onibus", false);
            ImageView no = (ImageView) raiz.getChildren().getFirst();
            Timeline ensaio = new Timeline(
                    new javafx.animation.KeyFrame(Duration.millis(1000), e -> new Scene(raiz)),
                    new javafx.animation.KeyFrame(Duration.millis(2100), e -> assinaturas[0] = hashImagem(no)),
                    new javafx.animation.KeyFrame(Duration.millis(3000), e -> {
                        assinaturas[1] = hashImagem(no); terminou.countDown();
                    }));
            AnimacoesFx.executar(raiz, "ensaio", ensaio);
        });
        try {
            assertTrue(terminou.await(6, TimeUnit.SECONDS));
            assertNotEquals(0, assinaturas[0], "Imagem deve aparecer mesmo anexada depois da preparação");
            assertNotEquals(assinaturas[0], assinaturas[1], "Reutilizar cenário não pode congelar o repouso");
        } finally { naFx(() -> { AnimacoesFx.pararArvore(painel.get()); renderer.get().close(); }); }
    }

    private static long hashImagem(ImageView imagem) {
        var pixels = imagem.getImage().getPixelReader();
        if ((pixels.getArgb(0,0) >>> 24) == 0) return 0;
        long hash = 1;
        for (int y = 0; y < 288; y += 6) for (int x = 0; x < 396; x += 6) hash = hash * 31 + pixels.getArgb(x,y);
        return hash;
    }

}
