package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.controller.DadosProtagonista;
import br.uefs.forkeazando.model.IdentidadeProtagonista;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class TelaCriacaoFxTest {

    @BeforeAll
    static void iniciarToolkit() throws Exception {
        assumeTrue(!System.getProperty("os.name").toLowerCase().contains("linux") || System.getenv("DISPLAY") != null,
                "A validação gráfica requer DISPLAY (ex.: Xvfb)");
        CountDownLatch pronto = new CountDownLatch(1);
        try {
            Platform.startup(pronto::countDown);
        } catch (IllegalStateException iniciado) {
            pronto.countDown();
        }
        assertTrue(pronto.await(10, TimeUnit.SECONDS));
        Platform.setImplicitExit(false);
    }

    private static <T> T naFx(Supplier<T> acao) throws Exception {
        FutureTask<T> tarefa = new FutureTask<>(acao::get);
        Platform.runLater(tarefa);
        return tarefa.get(8, TimeUnit.SECONDS);
    }

    @Test
    void exibePreviaVisualDeTianaEMilesComImagensExistentesENomesCorrespondentes() throws Exception {
        naFx(() -> {
            TelaCriacaoFx tela = new TelaCriacaoFx();
            VBox painel = tela.criarPainel(d -> {}, e -> {}, () -> {});
            new Scene(painel);

            HBox previa = painel.getChildren().stream()
                    .filter(n -> n.getStyleClass().contains("previa-protagonistas"))
                    .map(HBox.class::cast)
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("Contêiner de prévia dos protagonistas ausente"));

            assertEquals(2, previa.getChildren().size());

            VBox cartaoTiana = (VBox) previa.getChildren().get(0);
            VBox cartaoMiles = (VBox) previa.getChildren().get(1);

            assertEquals(IdentidadeProtagonista.STHEFFANNY, cartaoTiana.getUserData());
            assertEquals(IdentidadeProtagonista.DAVI, cartaoMiles.getUserData());

            ImageView imgTiana = (ImageView) cartaoTiana.getChildren().get(0);
            Label nomeTiana = (Label) cartaoTiana.getChildren().get(1);
            assertNotNull(imgTiana.getImage());
            assertFalse(imgTiana.getImage().isError());
            assertTrue(imgTiana.getImage().getUrl().endsWith("stheffanny/expressions/neutra.png"));
            assertFalse(imgTiana.isSmooth());
            assertEquals("Tiana", nomeTiana.getText());

            ImageView imgMiles = (ImageView) cartaoMiles.getChildren().get(0);
            Label nomeMiles = (Label) cartaoMiles.getChildren().get(1);
            assertNotNull(imgMiles.getImage());
            assertFalse(imgMiles.getImage().isError());
            assertTrue(imgMiles.getImage().getUrl().endsWith("protagonista_masculino/expressions/neutra.png"));
            assertFalse(imgMiles.isSmooth());
            assertEquals("Miles", nomeMiles.getText());

            assertTrue(cartaoTiana.getStyleClass().contains("selecionado"));
            assertFalse(cartaoMiles.getStyleClass().contains("selecionado"));
            return null;
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    void alternarProtagonistaSincronizaDestaquePreservaNomePersonalizadoEEntregaDadosCorretos() throws Exception {
        naFx(() -> {
            AtomicReference<DadosProtagonista> recebido = new AtomicReference<>();
            TelaCriacaoFx tela = new TelaCriacaoFx();
            VBox painel = tela.criarPainel(recebido::set, msg -> fail("Erro inesperado: " + msg), () -> {});
            new Scene(painel);

            HBox previa = (HBox) painel.getChildren().stream()
                    .filter(n -> n.getStyleClass().contains("previa-protagonistas"))
                    .findFirst().orElseThrow();
            VBox cartaoTiana = (VBox) previa.getChildren().get(0);
            VBox cartaoMiles = (VBox) previa.getChildren().get(1);

            ComboBox<IdentidadeProtagonista> combo = (ComboBox<IdentidadeProtagonista>) painel.getChildren().stream()
                    .filter(ComboBox.class::isInstance)
                    .findFirst().orElseThrow();
            TextField campoNome = (TextField) painel.getChildren().stream()
                    .filter(TextField.class::isInstance)
                    .findFirst().orElseThrow();

            // Clicar na prévia de Miles seleciona Miles e atualiza o nome padrão
            cartaoMiles.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                    MouseButton.PRIMARY, 1, false, false, false, false,
                    true, false, false, true, false, false, null));

            assertEquals(IdentidadeProtagonista.DAVI, combo.getValue());
            assertEquals("Miles", campoNome.getText());
            assertFalse(cartaoTiana.getStyleClass().contains("selecionado"));
            assertTrue(cartaoMiles.getStyleClass().contains("selecionado"));

            // Personalizar o nome e alternar via ComboBox preserva o nome customizado e atualiza o destaque
            campoNome.setText("Alex");
            combo.setValue(IdentidadeProtagonista.STHEFFANNY);
            assertEquals("Alex", campoNome.getText());
            assertTrue(cartaoTiana.getStyleClass().contains("selecionado"));
            assertFalse(cartaoMiles.getStyleClass().contains("selecionado"));

            // Iniciar partida mantém a entrega intacta de DadosProtagonista
            List<Node> botoes = painel.getChildren().stream()
                    .filter(HBox.class::isInstance)
                    .flatMap(h -> ((HBox) h).getChildren().stream())
                    .filter(Button.class::isInstance)
                    .toList();
            Button comecar = (Button) botoes.stream()
                    .filter(b -> "COMEÇAR A JORNADA".equals(((Button) b).getText()))
                    .findFirst().orElseThrow();
            comecar.fire();

            assertNotNull(recebido.get());
            assertEquals("Alex", recebido.get().nome());
            assertEquals(IdentidadeProtagonista.STHEFFANNY, recebido.get().identidade());
            return null;
        });
    }
}
