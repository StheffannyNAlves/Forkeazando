package br.uefs.forkeazando.view.javafx;
import br.uefs.forkeazando.adapter.GerenciamentoPartidas;
import br.uefs.forkeazando.audio.GerenciadorAudio;
import br.uefs.forkeazando.observer.ObservadorPartida;
import br.uefs.forkeazando.roteiro.*;
import br.uefs.forkeazando.model.*;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
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
import br.uefs.forkeazando.adapter.CarregamentoAdapter;
import br.uefs.forkeazando.dao.GerenciadorSlots;
import br.uefs.forkeazando.view.ApresentacaoCena;
import br.uefs.forkeazando.view.pixel.PerfilProtagonista;
import br.uefs.forkeazando.controller.CenasController;
import br.uefs.forkeazando.controller.DadosProtagonista;
import br.uefs.forkeazando.controller.SessaoGrafica;
import static br.uefs.forkeazando.view.javafx.ComponentesFx.*;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.*;

// Recebe os eventos da ‘interface’. O modelo e o roteiro pertencem à SessaoGrafica
public class ControllerInterface implements ApresentacaoCena, ObservadorPartida {

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
    @FXML private ScrollPane rolagemModal;
    @FXML private Button botaoFechar;
    @FXML private StackPane telaSecundaria;
    @FXML private Label dicaTeclado;

    private final TelaConfigFx telaConfiguracoes = new TelaConfigFx();
    private Configuracoes configuracoes = new Configuracoes();
    private GerenciadorAudio audio;
    private final TelaStatusFx telaStatus = new TelaStatusFx();
    private final List<Button> opcoes = new ArrayList<>();
    private final GerenciamentoPartidas acessoPartidas = new CarregamentoAdapter(GerenciadorSlots.PASTA_PADRAO);
    private final SessaoGrafica sessao = new SessaoGrafica(acessoPartidas);
    private final Navegacao navegacao = new Navegacao() {
        @Override public void mostrarTela(VBox painel) { ControllerInterface.this.mostrarTela(painel); }
        @Override public void voltarAoMenu() { ControllerInterface.this.voltarAoMenu(); }
        @Override public void mostrarModal(String titulo, String conteudo) { ControllerInterface.this.mostrarModal(titulo, conteudo); }
    };
    private final TelaSavesFx telaSaves = new TelaSavesFx(sessao, navegacao);
    private final TelaCriacaoFx telaCriacao = new TelaCriacaoFx();
    private int selecionado;
    private final Deque<String[]> avisosPendentes = new ArrayDeque<>();
    private AvisosFx avisosDiscretos;
    private String feedbackEscolhaPendente;
    private VBox curriculoPendente;
    private String ilustracaoAtual;
    private PerfilProtagonista perfilIlustracaoAtual;
    private final ArtesFx artes = new ArtesFx();
    private DialogoFx dialogoAtual;
    private boolean fechandoModal;
    private boolean suspenso;
    private Label indicadoresHud;
    private ResumoAtributos ultimoResumoAtributos;

    private record ResumoAtributos(
            Estado estado, int energia, int score, int participacao,
            int confianca, int vidaSocial, int carisma, int logica, int sanidade) {
        static ResumoAtributos de(Estado estado) {
            Protagonista p = estado.getProtagonista();
            return new ResumoAtributos(estado, p.getEnergia(), p.getScore(), p.getParticipacao(),
                    p.getConfianca(), p.getVidaSocial(), p.getCarisma(), p.getLogica(), p.getSanidade());
        }

        List<String> variacoesDesde(ResumoAtributos anterior) {
            List<String> mudancas = new ArrayList<>();
            registrarDelta(mudancas, "Energia", energia - anterior.energia);
            registrarDelta(mudancas, "Score", score - anterior.score);
            registrarDelta(mudancas, "Participação", participacao - anterior.participacao);
            registrarDelta(mudancas, "Confiança", confianca - anterior.confianca);
            registrarDelta(mudancas, "Vida social", vidaSocial - anterior.vidaSocial);
            registrarDelta(mudancas, "Carisma", carisma - anterior.carisma);
            registrarDelta(mudancas, "Lógica", logica - anterior.logica);
            registrarDelta(mudancas, "Sanidade", sanidade - anterior.sanidade);
            return mudancas;
        }

        private static void registrarDelta(List<String> mudancas, String nome, int delta) {
            if (delta != 0) {
                mudancas.add(nome + " " + (delta > 0 ? "+" : "") + delta);
            }
        }
    }

    @FXML
    private void  initialize() {
        sessao.adicionarObservador(this);
        try {
            configuracoes = sessao.carregarConfiguracoes();
        } catch (CarregamentoException | DadosInvalidosException ex) {
            Platform.runLater(() -> mostrarModal("ERRO NAS CONFIGURAÇÕES", ex.getMessage()));
        }
        Image imagem = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/campus_uefp.png")));
        arte.setImage(imagem);
        Rectangle recorte = new Rectangle();
        recorte.widthProperty().bind(painelArte.widthProperty());
        recorte.heightProperty().bind(painelArte.heightProperty());
        painelArte.setClip(recorte);

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
            AnimacoesFx.instalarHover(button);
        }
        selecionar(0);
        configurarTeclado();
        animarCenario();
        animarEntrada();
        Platform.runLater(raiz::requestFocus);
        aplicarDicas();
    }

    private void selecionar(int indice) {
        if (indice < 0 || indice >= opcoes.size()) return;
        for (Button b : opcoes) b.getStyleClass().remove("selecionado");
        selecionado = indice;
        opcoes.get(indice).getStyleClass().add("selecionado");
        AnimacoesFx.destacarSelecao(opcoes.get(indice), opcoes);
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
                if (dialogoAtual != null
                        && (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.ENTER)
                        && dialogoAtual.completar()) {
                    e.consume();
                    return;
                }
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
        Timeline ambiente = new Timeline();
        for (int i = 0; i <= 12; i++) {
            ambiente.getKeyFrames().add(new KeyFrame(Duration.seconds(i * 1.25),
                    new KeyValue(arte.translateXProperty(), i - 6, Interpolator.DISCRETE)));
        }
        ambiente.setAutoReverse(true);
        ambiente.setCycleCount(Animation.INDEFINITE);
        AnimacoesFx.executar(arte, "ambiente", ambiente);
    }

    private void animarEntrada() {
        AnimacoesFx.entradaTela(colunaMenu);
        // O título permanece nítido: sem zoom contínuo nas letras.
        FadeTransition brilho = new FadeTransition(Duration.seconds(3.6), tituloJogo);
        brilho.setFromValue(0.88);
        brilho.setToValue(1);
        brilho.setAutoReverse(true);
        brilho.setCycleCount(Animation.INDEFINITE);
        AnimacoesFx.executar(tituloJogo, "brilho", brilho);
    }

    private void abrirTelaSalvamento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/telaSalvamento.fxml"));
            VBox painel = loader.load();
            TelaCarregarController controller = loader.getController();
            controller.configurar(acessoPartidas, this::receberPartidaCarregada, mensagem -> mostrarModal("ERRO AO CARREGAR", mensagem), this::voltarAoMenu);
            mostrarTela(painel);

        } catch (IOException e) {
            mostrarModal("ERRO NA INTERFACE", "Não foi possível abrir a tela de salvamento: " + e.getMessage());
        }
    }

    private void receberPartidaCarregada(Estado carregado) {

        if (sessao.possuiPartida()
                && sessao.estado().temAlteracoes()) {

            VBox painel = novoPainel("SUBSTITUIR PARTIDA?", "A partida atual possui alterações não salvas.");

            Button confirmar = botaoAcao("SIM, CARREGAR");
            confirmar.setOnAction(e -> concluirCarregamento(carregado));

            Button cancelar = botaoAcao("CANCELAR");
            cancelar.setOnAction(e -> abrirTelaSalvamento());

            painel.getChildren().addAll(confirmar, cancelar);
            mostrarTela(painel);
            return;
        }

        concluirCarregamento(carregado);
    }

    private void concluirCarregamento(Estado carregado) {
        try {
            // Prepara tudo antes de descartar a partida anterior.
            sessao.carregarPartida(carregado, this);
            resetarCacheVisual();
            sessao.prepararCenaAtual();

        } catch (CarregamentoException | DadosInvalidosException e) {
            mostrarModal("ERRO NO PROGRESSO", e.getMessage());

        } catch (IllegalArgumentException e) {
            mostrarModal("PARTIDA INCOMPATÍVEL", e.getMessage());
        }
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
            case "continuar" -> continuar();
            case "instrucoes" -> mostrarModal("GUIA DE SOBREVIVÊNCIA", "Suas escolhas alteram energia, score, participação e relacionamentos.\n\n" + "No menu: setas e ENTER. Durante uma cena: clique numa decisão.\n" + "ESC volta ao menu sem apagar a partida em memória.");
            case "carregar" -> abrirTelaSalvamento();
            case "gerenciarSaves" -> telaSaves.abrir();
            case "estilo" -> abrirOpcoesEstilo();
            case "progresso" -> abrirMeuProgresso();
            case "creditos" -> mostrarModal("CRÉDITOS", "FORKEAZANDO · 2026\n\nStheffanny Alves\n" + "Universidade Estadual do Fork Perdido (UEFP)");
            case "sair" -> solicitarSaida();
            default -> mostrarModal(botao.getText().toUpperCase(), "Esta opção ainda não foi integrada ao JavaFX. " + "Os dados e as regras da versão de terminal continuam preservados.");
        }
    }

    private void abrirOpcoesEstilo() {
        mostrarTela(telaConfiguracoes.criarPainel(configuracoes, this::salvarConfiguracoes, this::voltarAoMenu));
    }

    private void salvarConfiguracoes(Configuracoes novas) {
        try {
            sessao.salvarConfiguracoes(novas);
            configuracoes = new Configuracoes(novas);
            aplicarDicas();
            if (audio != null) {
                audio.setVolume(configuracoes.getVolume());
            }
            voltarAoMenu();
            mostrarNotificacao("CONFIGURAÇÕES", "Preferências salvas com sucesso!");

        } catch (SaveException | DadosInvalidosException ex) {
            mostrarModal(
                    "ERRO AO SALVAR",
                    ex.getMessage()
            );
        }
    }

    private void aplicarDicas() {
        boolean mostrar = configuracoes.isDicasAtalho();
        dicaTeclado.setVisible(mostrar);
        dicaTeclado.setManaged(mostrar);
    }

    public void configurarAudio(GerenciadorAudio gerenciadorAudio) {
        audio = Objects.requireNonNull(gerenciadorAudio);

        audio.setVolume(
                configuracoes.getVolume()
        );
    }

    private void confirmarNovaPartida() {
        boolean pendente = sessao.estado().temAlteracoes();
        String mensagem = pendente
                ? "Você possui alterações não salvas. "
                + "Iniciar outra partida descartará esse progresso. "
                + "Deseja continuar?"
                : "Deseja iniciar uma nova partida? "
                + "A partida atual será substituída na memória.";

        VBox painel = novoPainel("INICIAR NOVA PARTIDA?", mensagem);
        Button nova = botaoAcao("SIM, NOVA PARTIDA");
        nova.setOnAction(e -> abrirCriacao());
        Button cancelar = botaoAcao("CANCELAR");
        cancelar.setOnAction(e -> voltarAoMenu());
        painel.getChildren().addAll(nova, cancelar);
        mostrarTela(painel);
    }

    private void confirmarSaida() {
        boolean alteracoesPendentes = sessao.possuiPartida() && sessao.estado().temAlteracoes();

        String mensagem = alteracoesPendentes ? "Existem alterações que podem não estar salvas. " + "Deseja salvar antes de sair?" : "Deseja realmente encerrar o jogo?";
        VBox painel = novoPainel("SAIR DO FORKEAZANDO?", mensagem);
        if (alteracoesPendentes) {
            Button salvar = botaoAcao("ABRIR SALVAMENTO");
            salvar.setOnAction(e -> telaSaves.abrir());
            painel.getChildren().add(salvar);
        }
        Button sair = botaoAcao(alteracoesPendentes ? "SAIR SEM SALVAR" : "SIM, SAIR");
        sair.setOnAction(e -> encerrar());

        Button cancelar = botaoAcao("CANCELAR");
        cancelar.setOnAction(e -> voltarAoMenu());

        painel.getChildren().addAll(sair, cancelar);

        mostrarTela(painel);
    }

    private void encerrar() {
        if (audio != null){
            audio.fechar();
        }
        pararAnimacoes();
        Platform.exit();
    }

    private void abrirCriacao() {
        mostrarTela(telaCriacao.criarPainel(
                this::iniciarPartida,
                mensagem -> mostrarModal("DADOS INCOMPLETOS", mensagem),
                this::voltarAoMenu));
        Platform.runLater(telaCriacao::focarNome);
    }

    private void iniciarPartida(DadosProtagonista dados) {
        try {
            sessao.novaPartida(dados, this);
            resetarCacheVisual();
            sessao.prepararCenaAtual();
        } catch (CarregamentoException | DadosInvalidosException ex) {
            mostrarModal("ERRO NO PROGRESSO", ex.getMessage());
        } catch (RuntimeException ex) {
            mostrarModal("ERRO AO INICIAR", ex.getMessage());
        }
    }

    /** Retoma a partida em memória; sem ela, tenta o último salvamento automático. */
    private void continuar() {
        if (sessao.possuiPartida()) {
            abrirCena();
            return;
        }
        if (!sessao.existeAutosave()) {
            mostrarModal("CONTINUAR", "Não há partida em andamento nem salvamento automático. "
                    + "Escolha NOVA PARTIDA ou CARREGAR.");
            return;
        }
        try {
            Estado ultimo = sessao.carregarAutosave();
            if (ultimo.isPartidaConcluida()) {
                mostrarModal("CONTINUAR", "O último salvamento automático é de uma jornada já concluída. "
                        + "Escolha NOVA PARTIDA ou carregue um slot.");
                return;
            }
            receberPartidaCarregada(ultimo);
        } catch (CarregamentoException | DadosInvalidosException e) {
            mostrarModal("ERRO AO CARREGAR", e.getMessage());
        }
    }

    private void abrirMeuProgresso() {
        try {
            Progresso progresso = sessao.consultarProgresso();
            VBox painel = novoPainel("MEU PROGRESSO", "As jornadas e conquistas que você acumulou.");
            List<Final> finaisAtuais = Arrays.stream(Final.values())
                    .filter(Final::isFinalDeCarreira)
                    .toList();
            List<Final> finaisLegados = Arrays.stream(Final.values())
                    .filter(f -> !f.isFinalDeCarreira())
                    .toList();
            long desbloqueados = finaisAtuais.stream()
                    .filter(progresso::alcancou)
                    .count();
            painel.getChildren().add(rotulo("FINAIS DESBLOQUEADOS: " + desbloqueados + "/" + finaisAtuais.size()));
            for (Final finalJogo : finaisAtuais) {
                painel.getChildren().add(rotulo(progresso.alcancou(finalJogo) ? "★ " + finalJogo.getTitulo() : "🔒 Final ainda não descoberto"));
            }
            List<Final> legadosDesbloqueados = finaisLegados.stream()
                    .filter(progresso::alcancou)
                    .toList();
            if (!legadosDesbloqueados.isEmpty()) {
                painel.getChildren().add(rotulo(
                        "HISTÓRICO · FINAIS LEGADOS"));
                for (Final legado : legadosDesbloqueados) {
                    painel.getChildren().add(rotulo(
                            "◆ " + legado.getTitulo()
                    ));
                }
            }
            painel.getChildren().add(rotulo("CONQUISTAS: " + progresso.conquistasDesbloqueadas().size() + "/" + Conquista.values().length));
            for (Conquista conquista : Conquista.values()) {
                boolean desbloqueada = progresso.possui(conquista);
                painel.getChildren().add(rotulo(desbloqueada ? "★ " + conquista.getTitulo() + " — " + conquista.getDescricao() : "🔒 Conquista não desbloqueada"));
            }
            painel.getChildren().add(rotulo("COLECIONÁVEIS DESCOBERTOS: " + progresso.colecionaveisColetados().size() + "/" + Colecionavel.values().length));
            for (Colecionavel.Tipo tipo : Colecionavel.Tipo.values()) {
                painel.getChildren().add(rotulo(tipo == Colecionavel.Tipo.HABILIDADE ? "HABILIDADES" : "ITENS"));
                for (Colecionavel item : Colecionavel.values()) {
                    if (item.getTipo() != tipo) continue;
                    boolean coletado = progresso.coletou(item);
                    painel.getChildren().add(rotulo((coletado ? "✓ " : "🔒 ") + (coletado ? item.getNome() : "???")));
                }
            }

            Button voltar = botaoAcao("VOLTAR AO MENU");
            voltar.setOnAction(e -> voltarAoMenu());

            painel.getChildren().add(voltar);
            mostrarTela(painel);

        } catch (CarregamentoException | DadosInvalidosException ex) {
            mostrarModal("ERRO NO PROGRESSO", ex.getMessage());
        }
    }

    private void abrirCena() {
        try {
            Cena cena = sessao.cenaAtual();
            Protagonista p = sessao.estado().getProtagonista();
            VBox conteudo = novoPainel("CAPÍTULO " + sessao.estado().getCapituloAtual(), p.getNome() + " · " + cena.getCapitulo());
            indicadoresHud = rotulo("");
            indicadoresHud.getStyleClass().add("hud-destaque");
            conteudo.getStyleClass().add("painel-cena");
            conteudo.setMaxWidth(1080);
            conteudo.getChildren().add(indicadoresHud);
            atualizar(sessao.estado());

            if (feedbackEscolhaPendente != null) {
                Label feedback = rotulo("↳ SUA ESCOLHA\n" + feedbackEscolhaPendente);
                feedback.getStyleClass().add("feedback-escolha");
                conteudo.getChildren().add(feedback);
                feedbackEscolhaPendente = null;
            }
            ApresentacaoDeCena visual = VisuaisDeCena.paraCena(cena.getId()).orElse(null);

            VBox colunaVisual = new VBox(16);
            colunaVisual.getStyleClass().add("coluna-cena");
            colunaVisual.setMinWidth(418);
            colunaVisual.setPrefWidth(418);
            exibirIlustracao(colunaVisual, visual, PerfilProtagonista.de(p));
            HBox personagem = new HBox(14);
            personagem.setAlignment(Pos.CENTER_LEFT);
            artes.exibirRetrato(personagem, visual);
            if (cena.getPersonagemFalando() != null && !cena.getPersonagemFalando().isBlank()) {
                Label falante = rotulo(cena.getPersonagemFalando().toUpperCase(Locale.ROOT));
                falante.getStyleClass().add("nome-falante");
                personagem.getChildren().add(falante);
            }
            if (!personagem.getChildren().isEmpty()) colunaVisual.getChildren().add(personagem);
            String textoResolvido = NarrativaDinamica.resolver(cena, p, sessao.estado().getSemestresExtras());
            DialogoFx narrativa = new DialogoFx(textoResolvido);
            HBox.setHgrow(narrativa, Priority.ALWAYS);
            HBox corpo = new HBox(24, colunaVisual, narrativa);
            corpo.setAlignment(Pos.TOP_LEFT);
            corpo.setFillHeight(false);
            corpo.getStyleClass().add("cena-corpo");
            conteudo.getChildren().add(corpo);
            if (cena.getId() == 0) {
                conteudo.getStyleClass().add("momento-especial");
                Label marco = rotulo("✦  APROVAÇÃO NA UEFP · UM NOVO COMEÇO");
                marco.getStyleClass().add("marco-jornada");
                conteudo.getChildren().add(2, marco);
            }

            List<Escolha> escolhas = sessao.escolhasDisponiveis();
            if (escolhas.isEmpty()) {
                conteudo.getChildren().add(rotulo("Nenhuma decisão disponível nesta cena."));
            }
            Label convite = rotulo(escolhas.size() > 1 ? "◆  ESCOLHA SEU CAMINHO" : "◆  CONTINUE SUA HISTÓRIA");
            convite.getStyleClass().add("titulo-escolhas");
            conteudo.getChildren().add(convite);
            for (Escolha escolha : escolhas) {
                Button escolhaBotao = botaoAcao(escolha.getTextoAlternativa());
                escolhaBotao.getStyleClass().add("escolha-narrativa");
                escolhaBotao.setWrapText(true);
                escolhaBotao.setMaxWidth(Double.MAX_VALUE);
                escolhaBotao.setOnAction(e -> {
                    try {
                        CenasController.ResultadoTransicao resultado = sessao.encaminharEscolha(escolha);

                        switch (resultado) {
                            case CONTINUAR_JOGO -> sessao.prepararCenaAtual();
                            case FIM_DE_JOGO -> {
                                try {
                                    sessao.finalizarPartida();
                                    voltarAoMenu();
                                    mostrarModal("JORNADA CONCLUÍDA", "Sua história foi concluída e salva!");
                                } catch (SaveException ex) {
                                    mostrarModal("ERRO AO SALVAR CONCLUSÃO", "Sua partida permanece em memória.\n" + ex.getMessage());
                                }
                            }
                            case ERRO_DE_ROTA -> mostrarModal("ERRO DE ROTA", "Não foi possível determinar a próxima cena.");
                        }
                    } catch (RuntimeException ex) {
                        mostrarModal("NÃO FOI POSSÍVEL AVANÇAR", ex.getMessage());
                    }
                });
                conteudo.getChildren().add(escolhaBotao);
            }
            Button galeria = botaoAcao("GALERIA E CONQUISTAS");
            galeria.setOnAction(e -> mostrarGaleria(sessao.estado().getProtagonista()));
            galeria.getStyleClass().add("acao-secundaria");

            Button statusBotao = botaoAcao("VER STATUS");
            statusBotao.setOnAction(e -> abrirStatus());
            statusBotao.getStyleClass().add("acao-secundaria");

            Button voltar = botaoAcao("VOLTAR AO MENU  ·  CONTINUAR DEPOIS");
            voltar.setOnAction(e -> voltarAoMenu());
            voltar.getStyleClass().add("acao-secundaria");
            HBox ferramentas = new HBox(12, galeria, statusBotao);
            ferramentas.getStyleClass().add("acoes-cena");
            conteudo.getChildren().addAll(ferramentas, voltar);
            mostrarTela(conteudo);
            dialogoAtual = narrativa;
            narrativa.iniciar(configuracoes.getVelocidadeTexto());
            atualizarPausas();
        } catch (RuntimeException ex) {
            mostrarModal("ERRO AO CARREGAR CENA", ex.getMessage());
        }
    }




    private void exibirIlustracao(VBox conteudo, ApresentacaoDeCena visual, PerfilProtagonista perfil) {
        String anterior = ilustracaoAtual;
        PerfilProtagonista perfilAnterior = perfilIlustracaoAtual;
        if (visual != null && visual.ilustracao() != null) ilustracaoAtual = visual.ilustracao();
        perfilIlustracaoAtual = perfil;
        artes.exibirIlustracao(conteudo, ilustracaoAtual, perfil,
                !Objects.equals(anterior, ilustracaoAtual) || perfilAnterior != perfil);
    }

    private void encerrarCenaAtual() {
        if (dialogoAtual != null) {
            dialogoAtual.encerrar();
            dialogoAtual = null;
        }
        AnimacoesFx.pararArvore(telaSecundaria);
    }

    private void mostrarTela(VBox painel) {
        encerrarCenaAtual();
        if (!painel.getStyleClass().contains("painel-cena")) indicadoresHud = null;
        ScrollPane rolante = new ScrollPane(painel);
        rolante.setFitToWidth(true);
        rolante.setMaxWidth(painel.getStyleClass().contains("painel-cena") ? 1104 : 810);
        rolante.setMaxHeight(Double.MAX_VALUE);
        rolante.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rolante.getStyleClass().add("rolagem-pixel");
        StackPane.setMargin(rolante, new Insets(24, 28, 24, 28));
        telaSecundaria.getChildren().setAll(rolante);
        telaSecundaria.setManaged(true);
        telaSecundaria.setVisible(true);
        telaSecundaria.toFront();
        AnimacoesFx.entradaTela(rolante);
        AnimacoesFx.entradaBotoes(painel);
        if (avisosDiscretos != null) avisosDiscretos.toFront();
        if (modal.isVisible()) modal.toFront();
        atualizarPausas();
        Platform.runLater(() -> {
            if (rolante.getScene() != null && !modal.isVisible()) rolante.requestFocus();
        });
    }

    private void voltarAoMenu() {
        encerrarCenaAtual();
        indicadoresHud = null;
        telaSecundaria.setVisible(false);
        telaSecundaria.setManaged(false);
        telaSecundaria.getChildren().clear();
        status.setText(sessao.possuiPartida()
                ? "✦  PARTIDA EM MEMÓRIA · CLIQUE EM CONTINUAR"
                : "✦  SUA HISTÓRIA COMEÇA AQUI");
        atualizarPausas();
        raiz.requestFocus();
    }

    public void suspenderAnimacoes(boolean pausar) {
        suspenso = pausar;
        atualizarPausas();
    }

    private void atualizarPausas() {
        AnimacoesFx.pausarArvore(raiz.getChildren().get(0),
                suspenso || telaSecundaria.isVisible() || modal.isVisible());
        AnimacoesFx.pausarArvore(telaSecundaria, suspenso || modal.isVisible());
        AnimacoesFx.pausarArvore(modal, suspenso);
        if (avisosDiscretos != null) AnimacoesFx.pausarArvore(avisosDiscretos, suspenso || modal.isVisible());
    }

    private void mostrarNotificacao(String titulo, String mensagem) {
        if (avisosDiscretos == null) {
            avisosDiscretos = new AvisosFx();
            raiz.getChildren().add(avisosDiscretos);
        }
        avisosDiscretos.mostrar(titulo, mensagem);
        avisosDiscretos.toFront();
        atualizarPausas();
        if (modal.isVisible()) modal.toFront();
    }

    private void mostrarModal(String titulo, String conteudo) {
        if ("PARTIDA SALVA".equals(titulo) || "PARTIDA EXCLUÍDA".equals(titulo)) {
            mostrarNotificacao(titulo, conteudo);
            return;
        }
        if (modal.isVisible()) {
            avisosPendentes.addLast(new String[]{titulo, conteudo});
            return;
        }

        exibirModalAgora(titulo, conteudo);
    }

    private void exibirModalAgora(String titulo, String conteudo) {
        tituloModal.setText(titulo);
        textoModal.setText(conteudo == null
                ? "Ocorreu um erro sem detalhes."
                : conteudo);
        rolagemModal.setPrefViewportHeight(Math.min(360, textoModal.prefHeight(540)));

        fechandoModal = false;
        modal.setManaged(true);
        modal.setVisible(true);
        modal.toFront();
        AnimacoesFx.entradaModal(modal);
        atualizarPausas();
        botaoFechar.requestFocus();
    }

    @FXML
    private void fecharModal() {
        if (fechandoModal) return;
        if (!avisosPendentes.isEmpty()) {
            String[] proximo = avisosPendentes.removeFirst();
            exibirModalAgora(proximo[0], proximo[1]);
            return;
        }
        fechandoModal = true;
        AnimacoesFx.saidaModal(modal, () -> {
            fechandoModal = false;
            if (!avisosPendentes.isEmpty()) {
                String[] proximo = avisosPendentes.removeFirst();
                exibirModalAgora(proximo[0], proximo[1]);
                return;
            }
            modal.setVisible(false);
            modal.setManaged(false);
            AnimacoesFx.pararArvore(modal);
            atualizarPausas();
            if (telaSecundaria.isVisible()) telaSecundaria.requestFocus();
            else raiz.requestFocus();
        });
    }

    public void pararAnimacoes() {
        encerrarCenaAtual();
        AnimacoesFx.pararArvore(raiz);
        artes.close();
    }

    @Override
    public void resetarCacheVisual() {
        encerrarCenaAtual();
        avisosPendentes.clear();
        feedbackEscolhaPendente = null;
        ilustracaoAtual = null;
        perfilIlustracaoAtual = null;
        ultimoResumoAtributos = null;
        artes.resetar();
    }

    @Override
    public void mostrarGaleria(Protagonista protagonista) {
        VBox painel = novoPainel("GALERIA", "Habilidades e itens conquistados durante a jornada.");
        painel.setMaxHeight(Double.MAX_VALUE);
        for (Colecionavel.Tipo tipo : Colecionavel.Tipo.values()) {
            painel.getChildren().add(rotulo(tipo == Colecionavel.Tipo.HABILIDADE
                    ? "HABILIDADES" : "ITENS"));
            for (Colecionavel item : Colecionavel.values()) {
                if (item.getTipo() != tipo) continue;
                boolean desbloqueado = protagonista.temFlag(item.getFlag());
                painel.getChildren().add(rotulo((desbloqueado ? "✓ " : "🔒 ") + (desbloqueado ? item.getNome() : "???")));
            }
        }
        painel.getChildren().add(rotulo("★ CONQUISTAS"));

        try {
            Progresso progresso = sessao.consultarProgresso();
            for (Conquista conquista : Conquista.values()) {
                boolean desbloqueada = progresso.possui(conquista);
                painel.getChildren().add(rotulo(desbloqueada
                                ? "★ " + conquista.getTitulo()
                                + " — " + conquista.getDescricao()
                                : "🔒 Conquista bloqueada"));
            }

        } catch (CarregamentoException | DadosInvalidosException ex) {
            mostrarModal("ERRO NO PROGRESSO", ex.getMessage());
        }

        Button voltar = botaoAcao("VOLTAR À CENA");
        voltar.setOnAction(e -> abrirCena());
        painel.getChildren().add(voltar);
        mostrarTela(painel);
    }

    @Override
    public void renderizar(Cena cena, Protagonista p, int semestresExtras, ApresentacaoDeCena visual) {
        // Reaproveita temporariamente o renderizador JavaFX existente
        if (!sessao.possuiPartida()
                || sessao.estado().getCenaAtualId() != cena.getId()) {
            throw new IllegalStateException("A cena gráfica não corresponde à partida atual.");
        }

        abrirCena();
        if (curriculoPendente != null) {
            VBox painel = curriculoPendente;
            curriculoPendente = null;
            mostrarTela(painel);
        }
    }

    @Override
    public void mostrarBencaoCaramelo() {
        mostrarModal("BENÇÃO DO CARAMELO", "O Caramelo passa do lado. Ele para. Ele te olha.\n\n" + "Ele vai embora.\n\n(Foi... um sinal?)");
    }

    @Override
    public void mostrarHabilidade(String flag) {
        String nome = Colecionavel.porFlag(flag)
                .map(Colecionavel::getNome)
                .orElse(flag);

        mostrarNotificacao("NOVA HABILIDADE", nome);
    }

    @Override
    public void mostrarItem(String flag) {
        String nome = Colecionavel.porFlag(flag)
                .map(Colecionavel::getNome)
                .orElse(flag);

        mostrarNotificacao("ITEM ADQUIRIDO", nome);
    }



    @Override
    public void mostrarConquista(Conquista conquista) {
        mostrarNotificacao("CONQUISTA DESBLOQUEADA", conquista.getTitulo() + "\n\n" + conquista.getDescricao());
    }

    @Override
    public void mostrarMensagem(String mensagem) {

        if (mensagem != null && mensagem.startsWith("CONSEQUÊNCIA DA ESCOLHA\n")) {
            feedbackEscolhaPendente = mensagem.substring("CONSEQUÊNCIA DA ESCOLHA\n".length());
            return;
        }
        if ("Progresso salvo automaticamente.".equals(mensagem)) {
            mostrarNotificacao("AUTOSAVE", "Progresso salvo automaticamente.");
            return;
        }
        if (mensagem != null && mensagem.startsWith("Sua trilha foi definida:")) {
            mostrarNotificacao("SEU CAMINHO", mensagem);
            return;
        }
        if (mensagem != null && mensagem.startsWith("Novo final desbloqueado:")) {
            mostrarNotificacao("NOVO FINAL", mensagem);
            return;
        }
        // Falhas de salvamento, problemas de rota e mensagens inesperadas não desaparecem sozinhas
        mostrarModal("MENSAGEM", mensagem);
    }

    @Override
    public void mostrarImpactoRelacionamento(PersonagemSecundario npc, float valor, Protagonista.NivelRelacionamento nivel) {
        String mudanca = valor > 0 ? "melhorou"
                : valor < 0 ? "piorou" : "não mudou";
        mostrarNotificacao("RELACIONAMENTO", "Seu relacionamento com " + npc.getNome()
                        + " " + mudanca + ".\n"
                        + "Nível atual: " + nivel);
    }

    @Override
    public void mostrarEpilogos(Protagonista p) {
        VBox painel = novoPainel("O QUE FICOU", "O fim da jornada de " + p.getNome());
        painel.getStyleClass().add("momento-especial");
        artes.exibirIlustracao(painel, VisuaisDeFinal.ilustracaoDe(p), PerfilProtagonista.de(p), true);
        painel.setMaxHeight(Double.MAX_VALUE);
        for (Epilogos.Epilogo bloco : Epilogos.de(p)) {
            painel.getChildren().add(rotulo(bloco.titulo()));
            Label texto = new Label(bloco.texto());
            texto.getStyleClass().add("texto-narrativa");
            texto.setWrapText(true);
            texto.setMaxWidth(650);
            painel.getChildren().add(texto);
        }
        Button continuar = botaoAcao("CONTINUAR PARA O ENCERRAMENTO");
        continuar.setOnAction(e -> abrirCena());
        painel.getChildren().add(continuar);
        mostrarTela(painel);
    }

    @Override
    public void mostrarCurriculo(Protagonista p) {
        VBox painel = novoPainel("SEU CURRÍCULO", "Um retrato das escolhas feitas até aqui.");
        painel.getStyleClass().add("momento-especial");
        painel.setMaxHeight(Double.MAX_VALUE);
        Curriculo.Categoria categoriaAnterior = null;
        for (Curriculo.Linha linha : Curriculo.linhas(p)) {
            if (categoriaAnterior != linha.categoria()) {
                Label secao = rotulo("◆ " + linha.categoria().rotulo);
                secao.getStyleClass().add("curriculo-secao");
                painel.getChildren().add(secao);
                categoriaAnterior = linha.categoria();
            }
            Label item = rotulo((linha.positiva() ? "✓ " : "✗ ") + linha.texto());
            item.getStyleClass().add("curriculo-linha");
            painel.getChildren().add(item);
        }
        Button continuar = botaoAcao("CONTINUAR A HISTÓRIA");
        continuar.setOnAction(e -> abrirCena());
        painel.getChildren().add(continuar);
        curriculoPendente = painel;
    }


    private void abrirStatus() {
        mostrarTela(telaStatus.criarPainel(sessao.estado(), this::abrirCena));
    }

    @Override
    public void mostrarTransicao(String capitulo) {
        mostrarNotificacao("NOVO CAPÍTULO", capitulo);
    }

    @Override
    public void mostrarChuteDoSistema(Protagonista p) {
        String resultado = SistemaAcademico.descricaoDe(
                SistemaAcademico.classificar(p));

        mostrarModal("SISTEMA ACADÊMICO · VEREDITO", "Processamento concluído.\n\n" + resultado + "\n\nRecomendação baseada em padrões históricos." + "\nO sistema não decide por você. Ainda.");
    }


    public void solicitarSaida() {
        boolean possuiPartida = sessao.possuiPartida();
        boolean alteracoesPendentes = possuiPartida && sessao.estado().temAlteracoes();
        if (alteracoesPendentes || (possuiPartida && configuracoes.isConfirmarSaida())) {
            confirmarSaida();
            return;
        }
        encerrar();
    }

    @Override
    public void atualizar(Estado estado) {
        if (estado == null || estado.getProtagonista() == null) {
            ultimoResumoAtributos = null;
            return;
        }
        ResumoAtributos atual = ResumoAtributos.de(estado);
        if (ultimoResumoAtributos != null && ultimoResumoAtributos.estado() == estado && raiz != null) {
            List<String> variacoes = atual.variacoesDesde(ultimoResumoAtributos);
            if (!variacoes.isEmpty()) {
                mostrarNotificacao("ATRIBUTOS", String.join("  ·  ", variacoes));
            }
        }
        ultimoResumoAtributos = atual;

        if (indicadoresHud == null)
            return;
        Protagonista p = estado.getProtagonista();
        String resumo = "ENERGIA " + p.getEnergia() + "/" + p.getEnergiaMaxima()
                + "    SCORE " + p.getScore() + "    PARTICIPAÇÃO " + p.getParticipacao();
        if (!resumo.equals(indicadoresHud.getText())) {
            indicadoresHud.setText(resumo);
            AnimacoesFx.pulsarHud(indicadoresHud);
        }
    }
}

