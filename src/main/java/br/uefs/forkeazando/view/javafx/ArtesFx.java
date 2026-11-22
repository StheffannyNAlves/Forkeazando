package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.roteiro.ApresentacaoDeCena;
import br.uefs.forkeazando.view.pixel.Ilustracoes;
import br.uefs.forkeazando.view.pixel.Retratos;
import br.uefs.forkeazando.view.pixel.Tela;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.*;

/** Apresentação dos recursos existentes; não decide cenas, emoções ou consequências. */
public final class ArtesFx implements AutoCloseable {
    private static final int ESCALA_CENARIO = 6;
    private static final Map<String, String> TRANSICOES = Map.of(
            "neutra:feliz", "01_neutra_para_feliz.png",
            "feliz:pensativa", "02_feliz_para_pensativa.png",
            "pensativa:surpresa", "03_pensativa_para_surpresa.png",
            "surpresa:triste", "04_surpresa_para_triste.png",
            "triste:brava", "05_triste_para_brava.png",
            "brava:neutra", "06_brava_para_neutra.png");
    private final Map<String, Optional<Image>> imagens = limitado(64);
    // Este cache pertence exclusivamente ao executor de desenho.
    private record Cenario(java.util.List<Tela> entrada, java.util.List<Tela> repouso) {}
    private final Map<String, Cenario> quadros = limitado(6);
    private final ExecutorService desenho = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "forkeazando-arte");
        t.setDaemon(true);
        return t;
    });
    private String ultimoFalante;
    private String ultimaExpressao;
    private volatile boolean encerrado;

    private static <K, V> Map<K, V> limitado(int limite) {
        return new LinkedHashMap<>(limite, 0.75f, true) {
            @Override protected boolean removeEldestEntry(Map.Entry<K, V> entrada) {
                return size() > limite;
            }
        };
    }

    private Image imagem(String caminho) {
        return imagens.computeIfAbsent(caminho, chave -> {
            var recurso = ArtesFx.class.getResource(chave);
            return recurso == null ? Optional.empty()
                    : Optional.of(new Image(recurso.toExternalForm()));
        }).orElse(null);
    }

    private static ImageView retrato(Image imagem) {
        ImageView no = new ImageView(imagem);
        no.setFitWidth(128);
        no.setFitHeight(128);
        no.setPreserveRatio(true);
        no.setSmooth(false);
        no.getStyleClass().add("retrato-personagem");
        return no;
    }

    public void exibirRetrato(HBox painel, ApresentacaoDeCena visual) {
        if (visual == null || visual.falante() == null) {
            resetarRetrato();
            return;
        }
        String falante = visual.falante();
        String expressao = visual.expressao() == null ? "neutra" : visual.expressao();
        String anterior = Objects.equals(ultimoFalante, falante) ? ultimaExpressao : null;
        ultimoFalante = falante;
        ultimaExpressao = expressao;
        String pasta = switch (falante) {
            case "sergio" -> "prof_sergio";
            case "rafael" -> "prof_rafael";
            case "protagonista_stheffanny" -> "stheffanny";
            case "protagonista_masculino", "caio", "bia", "dandara", "malu",
                 "alan", "noa", "lia", "stheffanny" -> falante;
            default -> null;
        };
        String base = "/images/personagens/" + pasta;
        Image rosto = pasta == null ? null : imagem(base + "/expressions/" + expressao + ".png");
        if (rosto == null) {
            // Expressões específicas do roteiro (lado, torto, seria...) continuam no renderizador original.
            Retratos.pixels(falante, expressao).ifPresent(tela -> {
                ImageView no = retrato(converter(tela));
                no.setFitWidth(no.getImage().getWidth());
                no.setFitHeight(no.getImage().getHeight());
                painel.getChildren().add(no);
                AnimacoesFx.entradaRetrato(no);
                respirar(no);
            });
            return;
        }
        ImageView no = retrato(rosto);
        painel.getChildren().add(no);
        AnimacoesFx.entradaRetrato(no);
        Runnable repouso = () -> {
            no.setImage(rosto);
            if ("neutra".equals(expressao)) piscar(no, base, rosto);
            else respirar(no);
        };
        String arquivo = TRANSICOES.get(anterior + ":" + expressao);
        Image intermediaria = arquivo == null ? null
                : imagem(base + "/animations/expressoes/transitions/" + arquivo);
        if (intermediaria == null) repouso.run();
        else {
            no.setImage(intermediaria);
            PauseTransition transicao = new PauseTransition(Duration.millis(110));
            transicao.setOnFinished(e -> repouso.run());
            AnimacoesFx.executar(no, "expressao", transicao);
        }
    }

    private static void respirar(ImageView no) {
        // Passos de um pixel, sem deformar o retrato nem substituir sua expressão.
        Timeline ciclo = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(no.translateYProperty(), 0, Interpolator.DISCRETE)),
                new KeyFrame(Duration.millis(1600), new KeyValue(no.translateYProperty(), -1, Interpolator.DISCRETE)),
                new KeyFrame(Duration.millis(3100), new KeyValue(no.translateYProperty(), 0, Interpolator.DISCRETE)),
                new KeyFrame(Duration.millis(4700)));
        ciclo.setCycleCount(Animation.INDEFINITE);
        AnimacoesFx.executar(no, "respiracao", ciclo);
    }

    private void piscar(ImageView no, String base, Image rosto) {
        Image meio = imagem(base + "/animations/idle/frame_001_piscada_meio.png");
        Image fechado = imagem(base + "/animations/idle/frame_002_olhos_fechados.png");
        Image ar = imagem(base + "/animations/idle/frame_003_respiracao.png");
        if (meio == null || fechado == null || ar == null) {
            respirar(no);
            return;
        }
        long espera = ThreadLocalRandom.current().nextLong(3100, 6200);
        Timeline ciclo = new Timeline(
                new KeyFrame(Duration.millis(1300), e -> no.setImage(ar)),
                new KeyFrame(Duration.millis(2200), e -> no.setImage(rosto)),
                new KeyFrame(Duration.millis(espera), e -> no.setImage(meio)),
                new KeyFrame(Duration.millis(espera + 60), e -> no.setImage(fechado)),
                new KeyFrame(Duration.millis(espera + 145), e -> no.setImage(meio)),
                new KeyFrame(Duration.millis(espera + 200), e -> no.setImage(rosto)));
        ciclo.setOnFinished(e -> {
            if (no.getScene() != null && !encerrado) piscar(no, base, rosto);
        });
        AnimacoesFx.executar(no, "repouso", ciclo);
    }

    public void exibirIlustracao(VBox painel, String chave, boolean animar) {
        if (chave == null || encerrado) return;
        Ilustracoes.animacao(chave).ifPresent(animacao -> {
            int largura = Ilustracoes.LARGURA;
            int altura = Ilustracoes.LINHAS * 2; // Tela: cada linha de terminal contém dois pixels.
            WritableImage buffer = new WritableImage(largura * ESCALA_CENARIO, altura * ESCALA_CENARIO);
            ImageView no = new ImageView(buffer);
            no.setFitWidth(396); // 66 × 6: pixels inteiros no tamanho de referência.
            no.setFitHeight(288);
            no.setPreserveRatio(true);
            no.setSmooth(false);
            no.getStyleClass().add("ilustracao-cena");
            painel.getChildren().add(no);
            Runnable[] aoAnexar = {null};
            boolean[] removida = {false};
            Future<?> pedido = desenho.submit(() -> {
                try {
                    Cenario pronto = quadros.get(chave);
                    var repouso = Ilustracoes.ambiente(chave).orElseThrow();
                    if (pronto == null) {
                        var entradaFonte = new java.util.ArrayList<Tela>();
                        var repousoFonte = new java.util.ArrayList<Tela>();
                        for (int i = 0; i < animacao.total(); i++) {
                            if (Thread.currentThread().isInterrupted()) return;
                            entradaFonte.add(animacao.desenhar(i));
                        }
                        for (int i = 0; i < repouso.total(); i++) {
                            if (Thread.currentThread().isInterrupted()) return;
                            repousoFonte.add(repouso.desenhar(i));
                        }
                        pronto = new Cenario(java.util.List.copyOf(entradaFonte), java.util.List.copyOf(repousoFonte));
                        quadros.put(chave, pronto);
                    }
                    java.util.List<Tela> fonte = animar ? pronto.entrada()
                            : java.util.List.of(pronto.entrada().getLast());
                    int[][] entrada = rasterizar(fonte);
                    if (entrada == null) return;
                    int[][] ambiente = rasterizar(pronto.repouso());
                    if (ambiente == null) return;
                    Platform.runLater(() -> {
                        if (encerrado || removida[0]) return;
                        Runnable apresentar = () -> {
                            escrever(buffer, entrada[0]);
                            Runnable iniciarRepouso = () -> {
                                Timeline ciclo = new Timeline();
                                for (int i = 0; i <= ambiente.length; i++) {
                                    int indice = i % ambiente.length;
                                    ciclo.getKeyFrames().add(new KeyFrame(Duration.millis(i * repouso.intervaloMs()),
                                            e -> escrever(buffer, ambiente[indice])));
                                }
                                ciclo.setCycleCount(Animation.INDEFINITE);
                                AnimacoesFx.executar(no, "quadros", ciclo);
                            };
                            if (!animar) { iniciarRepouso.run(); return; }
                            AnimacoesFx.entradaIlustracao(no);
                            Timeline introducao = new Timeline();
                            for (int i = 1; i < entrada.length; i++) {
                                int indice = i;
                                introducao.getKeyFrames().add(new KeyFrame(
                                        Duration.millis(i * Math.max(1, animacao.intervaloMs())),
                                        e -> escrever(buffer, entrada[indice])));
                            }
                            introducao.setOnFinished(e -> iniciarRepouso.run());
                            AnimacoesFx.executar(no, "quadros", introducao);
                        };
                        if (no.getScene() != null) apresentar.run();
                        else aoAnexar[0] = apresentar;
                    });
                } catch (RuntimeException ex) {
                    System.err.println("Não foi possível preparar a ilustração " + chave + ": " + ex.getMessage());
                }
            });
            no.sceneProperty().addListener((obs, antes, depois) -> {
                if (depois != null && aoAnexar[0] != null && !removida[0] && !encerrado) {
                    Runnable iniciar = aoAnexar[0];
                    aoAnexar[0] = null;
                    iniciar.run();
                }
                if (antes != null && depois == null) {
                    removida[0] = true;
                    aoAnexar[0] = null;
                    pedido.cancel(true);
                }
            });
        });
    }

    private static int[][] rasterizar(java.util.List<Tela> quadros) {
        int[][] resultado = new int[quadros.size()][];
        for (int i = 0; i < quadros.size(); i++) {
            if (Thread.currentThread().isInterrupted()) return null;
            resultado[i] = RasterizadorCenario.rasterizar(quadros.get(i), ESCALA_CENARIO);
        }
        return resultado;
    }

    private static int[] pixels(Tela tela) {
        int[] dados = new int[tela.largura() * tela.altura()];
        for (int y = 0; y < tela.altura(); y++) {
            for (int x = 0; x < tela.largura(); x++) {
                dados[y * tela.largura() + x] = 0xFF000000 | tela.get(x, y);
            }
        }
        return dados;
    }

    private static int[] ampliarPixels(int[] origem, int largura, int altura, int escala) {
        int larguraFinal = largura * escala;
        int[] resultado = new int[larguraFinal * altura * escala];
        for (int y = 0; y < altura * escala; y++) {
            for (int x = 0; x < larguraFinal; x++) {
                resultado[y * larguraFinal + x] = origem[(y / escala) * largura + x / escala];
            }
        }
        return resultado;
    }

    private static void escrever(WritableImage imagem, int[] dados) {
        imagem.getPixelWriter().setPixels(0, 0, (int) imagem.getWidth(), (int) imagem.getHeight(),
                PixelFormat.getIntArgbInstance(), dados, 0, (int) imagem.getWidth());
    }

    private static Image converter(Tela tela) {
        int escala = Math.max(1, 128 / Math.max(tela.largura(), tela.altura()));
        WritableImage imagem = new WritableImage(tela.largura() * escala, tela.altura() * escala);
        escrever(imagem, ampliarPixels(pixels(tela), tela.largura(), tela.altura(), escala));
        return imagem;
    }

    public void resetarRetrato() {
        ultimoFalante = null;
        ultimaExpressao = null;
    }

    @Override public void close() {
        encerrado = true;
        desenho.shutdownNow();
        imagens.clear();
    }
}
