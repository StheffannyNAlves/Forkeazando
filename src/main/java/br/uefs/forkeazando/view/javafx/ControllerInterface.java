package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Protagonista;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* Recebe os eventos da ‘interface’. O modelo e o roteiro pertencem à SessaoGrafica*/
public class ControllerInterface {

    @FXML private StackPane raiz;
    @FXML private StackPane painelArte;
    @FXML private ImageView arte;
    @FXML private VBox colunaMenu;
    @FXML private VBox listaBotoes;
    @FXML private Label tituloJogo;
    @FXML private Label status;
    @FXML private StackPane modal;
    @FXML private Label tituloModal;
    @FXML private Label textoModal;
    @FXML private Button botaoFechar;
    @FXML private StackPane telaSecundaria;

    private final List<Button> opcoes = new ArrayList<>();
    private final SessaoGrafica sessao = new SessaoGrafica();
    private int selecionado;
    private TranslateTransition movimentoCenario;
    private Animation animacaoTitulo;

    @FXML
    private void initialize() {
        Image imagem = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/images/campus_uefp.png")));
        arte.setImage(imagem);
        painelArte.setClip(new Rectangle(625, 760));

        String[] simbolos = {"▶", "▷", "▣", "▦", "▤", "✦", "▣", "★", "◆", "◀"};
        for (int i = 0; i < listaBotoes.getChildren().size(); i++) {
            Node node = listaBotoes.getChildren().get(i);
            if (!(node instanceof Button button)) continue;
            int indice = opcoes.size();
            opcoes.add(button);
            Label simbolo = new Label(simbolos[Math.min(i, simbolos.length - 1)]);
            simbolo.getStyleClass().add("icone-pixel");
            button.setGraphic(simbolo);
            button.setOnMouseEntered(e -> selecionar(indice));
        }
        selecionar(0);
        configurarTeclado();
        animarCenario();
        animarEntrada();
        Platform.runLater(raiz::requestFocus);
    }

    private void selecionar(int indice) {
        if (indice < 0 || indice >= opcoes.size()) return;
        for (Button b : opcoes) b.getStyleClass().remove("selecionado");
        selecionado = indice;
        opcoes.get(indice).getStyleClass().add("selecionado");
    }

    private void configurarTeclado() {
        raiz.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (modal.isVisible()) {
                if (e.getCode() == KeyCode.ESCAPE || e.getCode() == KeyCode.ENTER) {
                    fecharModal();
                    e.consume();
                }
                return;
            }
            if (telaSecundaria.isVisible()) {
                if (e.getCode() == KeyCode.ESCAPE) {
                    voltarAoMenu();
                    e.consume();
                }
                return;
            }
            switch (e.getCode()) {
                case UP -> selecionar((selecionado + opcoes.size() - 1) % opcoes.size());
                case DOWN -> selecionar((selecionado + 1) % opcoes.size());
                case ENTER -> opcoes.get(selecionado).fire();
                default -> { return; }
            }
            e.consume();
        });
    }

    private void animarCenario() {
        movimentoCenario = new TranslateTransition(Duration.seconds(12), arte);
        movimentoCenario.setFromX(15);
        movimentoCenario.setToX(-15);
        movimentoCenario.setAutoReverse(true);
        movimentoCenario.setCycleCount(Animation.INDEFINITE);
        movimentoCenario.play();
    }

    private void animarEntrada() {
        FadeTransition aparecer = new FadeTransition(Duration.millis(650), colunaMenu);
        aparecer.setFromValue(0);
        aparecer.setToValue(1);
        TranslateTransition deslocar = new TranslateTransition(Duration.millis(650), colunaMenu);
        deslocar.setFromX(20);
        deslocar.setToX(0);
        new ParallelTransition(aparecer, deslocar).play();

        ScaleTransition pulsar = new ScaleTransition(Duration.seconds(2.8), tituloJogo);
        pulsar.setFromX(1);
        pulsar.setFromY(1);
        pulsar.setToX(1.025);
        pulsar.setToY(1.025);
        pulsar.setAutoReverse(true);
        pulsar.setCycleCount(Animation.INDEFINITE);
        pulsar.play();
        animacaoTitulo = pulsar;
    }

    @FXML
    private void aoClicar(ActionEvent evento) {
        Button botao = (Button) evento.getSource();
        String opcao = String.valueOf(botao.getUserData());
        switch (opcao) {
            case "novaPartida" -> {
                if (sessao.possuiPartida()) confirmarNovaPartida();
                else abrirCriacao();
            }
            case "continuar" -> {
                if (sessao.possuiPartida()) abrirCena();
                else mostrarModal("CONTINUAR", "Não há partida em memória. Escolha NOVA PARTIDA para começar.");
            }
            case "instrucoes" -> mostrarModal("GUIA DE SOBREVIVÊNCIA",
                    "Suas escolhas alteram energia, score, participação e relacionamentos.\n\n"
                            + "No menu: setas e ENTER. Durante uma cena: clique numa decisão.\n"
                            + "ESC volta ao menu sem apagar a partida em memória.");
            case "creditos" -> mostrarModal("CRÉDITOS",
                    "FORKEAZANDO · 2026\n\nStheffanny Alves\n"
                            + "Universidade Estadual do Fork Perdido (UEFP)");
            case "sair" -> {
                if (sessao.possuiPartida()) confirmarSaida();
                else encerrar();
            }
            default -> mostrarModal(botao.getText().toUpperCase(),
                    "Esta opção ainda não foi integrada ao JavaFX. "
                            + "Os dados e as regras da versão de terminal continuam preservados.");
        }
    }

    private void confirmarNovaPartida() {
        VBox painel = novoPainel("INICIAR OUTRA PARTIDA?",
                "A partida atual está em memória e ainda não pode ser salva pela interface gráfica.");
        Button nova = botaoAcao("SIM, CRIAR OUTRA PERSONAGEM");
        nova.setOnAction(e -> abrirCriacao());
        Button voltar = botaoAcao("CANCELAR");
        voltar.setOnAction(e -> voltarAoMenu());
        painel.getChildren().addAll(nova, voltar);
        mostrarTela(painel);
    }

    private void confirmarSaida() {
        VBox painel = novoPainel("SAIR DO FORKEAZANDO?",
                "A partida atual está apenas em memória. Sair encerrará a sessão sem salvar.");
        Button sair = botaoAcao("SAIR SEM SALVAR");
        sair.setOnAction(e -> encerrar());
        Button voltar = botaoAcao("VOLTAR AO MENU");
        voltar.setOnAction(e -> voltarAoMenu());
        painel.getChildren().addAll(sair, voltar);
        mostrarTela(painel);
    }

    private void encerrar() {
        pararAnimacoes();
        Platform.exit();
    }

    private void abrirCriacao() {
        VBox conteudo = novoPainel("CRIE SUA PERSONAGEM", "Cada escolha muda seus atributos iniciais.");
        TextField nome = new TextField();
        nome.setPromptText("Nome da personagem");
        nome.getStyleClass().add("campo-pixel");
        nome.setMaxWidth(450);

        CheckBox perfeccionista = novoCheck("Sou perfeccionista");
        CheckBox sociavel = novoCheck("Sou sociável");
        CheckBox teorico = novoCheck("Prefiro estudar a teoria primeiro");

        ComboBox<Protagonista.NivelVidaSocial> vida = novoCombo(Protagonista.NivelVidaSocial.values());
        vida.setValue(Protagonista.NivelVidaSocial.EQUILIBRADO);
        ComboBox<Protagonista.ExperienciaEmocionalEnsinoMedio> experiencia =
                novoCombo(Protagonista.ExperienciaEmocionalEnsinoMedio.values());
        experiencia.setValue(Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA);
        ComboBox<Protagonista.SituacaoEconomica> economia = novoCombo(Protagonista.SituacaoEconomica.values());
        economia.setValue(Protagonista.SituacaoEconomica.ESTAVEL);

        conteudo.getChildren().addAll(
                rotulo("SEU NOME"), nome,
                perfeccionista, sociavel, teorico,
                rotulo("VIDA SOCIAL"), vida,
                rotulo("EXPERIÊNCIA NO ENSINO MÉDIO"), experiencia,
                rotulo("SITUAÇÃO ECONÔMICA"), economia
        );

        Label dica = new Label("Esses atributos usam o mesmo construtor da Fase 2.");
        dica.getStyleClass().add("texto-ajuda");
        Button comecar = botaoAcao("COMEÇAR A JORNADA");
        comecar.setOnAction(e -> {
            if (nome.getText().isBlank()) {
                mostrarModal("FALTOU O NOME", "Digite o nome da personagem para continuar.");
                return;
            }
            try {
                Protagonista protagonista = new Protagonista(
                        nome.getText(), perfeccionista.isSelected(), sociavel.isSelected(),
                        teorico.isSelected(), vida.getValue(), experiencia.getValue(), economia.getValue());
                sessao.novaPartida(protagonista);
                abrirCena();
            } catch (RuntimeException ex) {
                mostrarModal("ERRO AO INICIAR", ex.getMessage());
            }
        });
        Button voltar = botaoAcao("VOLTAR AO MENU");
        voltar.setOnAction(e -> voltarAoMenu());
        conteudo.getChildren().addAll(dica, new HBox(14, comecar, voltar));
        mostrarTela(conteudo);
        Platform.runLater(nome::requestFocus);
    }

    private void abrirCena() {
        try {
            Cena cena = sessao.cenaAtual();
            Protagonista p = sessao.estado().getProtagonista();
            VBox conteudo = novoPainel("CAPÍTULO " + sessao.estado().getCapituloAtual(),
                    p.getNome() + " · " + cena.getCapitulo());
            conteudo.getChildren().add(rotulo(
                    "ENERGIA " + p.getEnergia() + "/" + p.getEnergiaMaxima()
                            + "    SCORE " + p.getScore() + "    PARTICIPAÇÃO " + p.getParticipacao()));
            if (cena.getPersonagemFalando() != null && !cena.getPersonagemFalando().isBlank()) {
                conteudo.getChildren().add(rotulo(cena.getPersonagemFalando().toUpperCase()));
            }
            Label narrativa = new Label(cena.getTextoNarrativo());
            narrativa.getStyleClass().add("texto-narrativa");
            narrativa.setWrapText(true);
            narrativa.setMaxWidth(650);
            conteudo.getChildren().add(narrativa);

            List<Escolha> escolhas = sessao.escolhasDisponiveis();
            if (escolhas.isEmpty()) {
                conteudo.getChildren().add(rotulo("Nenhuma decisão disponível nesta cena."));
            }
            for (Escolha escolha : escolhas) {
                Button escolhaBotao = botaoAcao(escolha.getTextoAlternativa());
                escolhaBotao.setWrapText(true);
                escolhaBotao.setMaxWidth(Double.MAX_VALUE);
                escolhaBotao.setOnAction(e -> {
                    try {
                        boolean avancou = sessao.escolher(escolha);
                        if (avancou) abrirCena();
                        else mostrarModal("FIM DA PRÉVIA", "Chegamos à transição de capítulo "
                                + "A versão completa segue no terminal enquanto migro os eventos especiais");
                    } catch (RuntimeException ex) {
                        mostrarModal("NÃO FOI POSSÍVEL AVANÇAR", ex.getMessage());
                    }
                });
                conteudo.getChildren().add(escolhaBotao);
            }
            Button voltar = botaoAcao("VOLTAR AO MENU  ·  CONTINUAR DEPOIS");
            voltar.setOnAction(e -> voltarAoMenu());
            conteudo.getChildren().add(voltar);
            mostrarTela(conteudo);
        } catch (RuntimeException ex) {
            mostrarModal("ERRO AO CARREGAR CENA", ex.getMessage());
        }
    }

    private <E extends Enum<E>> ComboBox<E> novoCombo(E[] valores) {
        ComboBox<E> combo = new ComboBox<>();
        combo.getItems().addAll(valores);
        combo.setMaxWidth(450);
        combo.getStyleClass().add("combo-pixel");
        return combo;
    }

    private CheckBox novoCheck(String texto) {
        CheckBox check = new CheckBox(texto);
        check.getStyleClass().add("check-pixel");
        return check;
    }

    private Label rotulo(String texto) {
        Label l = new Label(texto);
        l.getStyleClass().add("rotulo-pixel");
        l.setWrapText(true);
        return l;
    }

    private Button botaoAcao(String texto) {
        Button b = new Button(texto);
        b.getStyleClass().add("botao-voltar");
        return b;
    }

    private VBox novoPainel(String titulo, String subtitulo) {
        VBox painel = new VBox(12);
        painel.setAlignment(Pos.CENTER_LEFT);
        painel.setPadding(new Insets(26));
        painel.setMaxWidth(760);
        painel.setMaxHeight(700);
        painel.getStyleClass().add("painel-interno");
        Label cabecalho = new Label(titulo);
        cabecalho.getStyleClass().add("titulo-modal");
        Label descricao = new Label(subtitulo);
        descricao.getStyleClass().add("texto-ajuda");
        painel.getChildren().addAll(cabecalho, descricao);
        return painel;
    }

    private void mostrarTela(VBox painel) {
        ScrollPane rolante = new ScrollPane(painel);
        rolante.setFitToWidth(true);
        rolante.setMaxWidth(810);
        rolante.setMaxHeight(700);
        rolante.getStyleClass().add("rolagem-pixel");
        telaSecundaria.getChildren().setAll(rolante);
        telaSecundaria.setManaged(true);
        telaSecundaria.setVisible(true);
        telaSecundaria.toFront();
    }

    private void voltarAoMenu() {
        telaSecundaria.setVisible(false);
        telaSecundaria.setManaged(false);
        telaSecundaria.getChildren().clear();
        status.setText(sessao.possuiPartida()
                ? "✦  PARTIDA EM MEMÓRIA · CLIQUE EM CONTINUAR"
                : "✦  SUA HISTÓRIA COMEÇA AQUI");
        raiz.requestFocus();
    }

    private void mostrarModal(String titulo, String conteudo) {
        tituloModal.setText(titulo);
        textoModal.setText(conteudo == null ? "Ocorreu um erro sem detalhes." : conteudo);
        modal.setManaged(true);
        modal.setVisible(true);
        modal.toFront();
        botaoFechar.requestFocus();
    }

    @FXML
    private void fecharModal() {
        modal.setVisible(false);
        modal.setManaged(false);
        if (telaSecundaria.isVisible()) telaSecundaria.toFront();
        else raiz.requestFocus();
    }

    public void pararAnimacoes() {
        if (movimentoCenario != null) movimentoCenario.stop();
        if (animacaoTitulo != null) animacaoTitulo.stop();
    }
}