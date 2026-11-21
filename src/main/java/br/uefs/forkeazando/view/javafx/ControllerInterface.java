package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.model.*;
import br.uefs.forkeazando.roteiro.ApresentacaoDeCena;
import br.uefs.forkeazando.roteiro.Epilogos;
import br.uefs.forkeazando.roteiro.SistemaAcademico;
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
import br.uefs.forkeazando.adapter.AcessoPartidas;
import br.uefs.forkeazando.adapter.CarregamentoAdapter;
import br.uefs.forkeazando.dao.GerenciadorSlots;
import br.uefs.forkeazando.view.ApresentacaoCena;
import br.uefs.forkeazando.controller.CenasController;
import br.uefs.forkeazando.controller.GatilhoAutosave;
import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.*;

/* Recebe os eventos da ‘interface’. O modelo e o roteiro pertencem à SessaoGrafica*/
public class ControllerInterface implements ApresentacaoCena {

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
    private final AcessoPartidas acessoPartidas = new CarregamentoAdapter(GerenciadorSlots.PASTA_PADRAO);
    private int selecionado;
    private TranslateTransition movimentoCenario;
    private Animation animacaoTitulo;
    private final Deque<String[]> avisosPendentes = new ArrayDeque<>();

    @FXML
    private void  initialize() {
        Image imagem = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/campus_uefp.png")));
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
    private void abrirTelaSalvamento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/telaSalvamento.fxml"));
            VBox painel = loader.load();
            TelaCarregarController controller = loader.getController();
            controller.configurar(acessoPartidas, this::receberPartidaCarregada, mensagem -> mostrarModal("ERRO AO CARREGAR", mensagem), this::voltarAoMenu);
            mostrarTela(painel);

        } catch (IOException e) {
            e.printStackTrace();

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
            sessao.carregarPartida(carregado);
            sessao.inicializarControladorPartida(this);
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
            case "continuar" -> {
                if (sessao.possuiPartida()) abrirCena();
                else mostrarModal("CONTINUAR", "Não há partida em memória. Escolha NOVA PARTIDA para começar.");
            }
            case "instrucoes" -> mostrarModal("GUIA DE SOBREVIVÊNCIA", "Suas escolhas alteram energia, score, participação e relacionamentos.\n\n" + "No menu: setas e ENTER. Durante uma cena: clique numa decisão.\n" + "ESC volta ao menu sem apagar a partida em memória.");
            case "carregar" -> abrirTelaSalvamento();
            case "creditos" -> mostrarModal("CRÉDITOS", "FORKEAZANDO · 2026\n\nStheffanny Alves\n" + "Universidade Estadual do Fork Perdido (UEFP)");
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
        VBox conteudo = novoPainel("CRIE SEU PROTAGONISTA", "Cada escolha muda seus atributos iniciais.");
        ComboBox<IdentidadeProtagonista> identidade = novoCombo(IdentidadeProtagonista.values());
        identidade.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(IdentidadeProtagonista valor) {
                if (valor == null) return "";
                return valor == IdentidadeProtagonista.STHEFFANNY ? "Tiana" : "Miles";
            }
            @Override
            public IdentidadeProtagonista fromString(String texto) {
                return "Miles".equalsIgnoreCase(texto) ? IdentidadeProtagonista.DAVI : IdentidadeProtagonista.STHEFFANNY;
            }
        });
        TextField nome = new TextField();
        nome.setPromptText("Nome exibido no jogo");
        nome.getStyleClass().add("campo-pixel");
        nome.setMaxWidth(450);
        nome.setText("Tiana");
        identidade.setValue(IdentidadeProtagonista.STHEFFANNY);
        identidade.valueProperty().addListener((obs, anterior, proximo) -> {
            if (proximo == null) return;
            String anteriorPadrao = anterior == IdentidadeProtagonista.DAVI ? "Miles" : "Tiana";
            if (nome.getText().isBlank() || nome.getText().equals(anteriorPadrao)) {
                nome.setText(proximo == IdentidadeProtagonista.DAVI ? "Miles" : "Tiana");
            }
        });

        CheckBox perfeccionista = novoCheck("Sou perfeccionista");
        CheckBox sociavel = novoCheck("Sou sociável");
        CheckBox teorico = novoCheck("Prefiro estudar a teoria primeiro");

        ComboBox<Protagonista.NivelVidaSocial> vida = novoCombo(Protagonista.NivelVidaSocial.values());
        vida.setValue(Protagonista.NivelVidaSocial.EQUILIBRADO);
        ComboBox<Protagonista.ExperienciaEmocionalEnsinoMedio> experiencia = novoCombo(Protagonista.ExperienciaEmocionalEnsinoMedio.values());
        experiencia.setValue(Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA);
        ComboBox<Protagonista.SituacaoEconomica> economia = novoCombo(Protagonista.SituacaoEconomica.values());
        economia.setValue(Protagonista.SituacaoEconomica.ESTAVEL);

        conteudo.getChildren().addAll(
                rotulo("ESCOLHA O PROTAGONISTA"), identidade,
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
            if (identidade.getValue() == null) {
                mostrarModal("ESCOLHA O PROTAGONISTA", "Selecione Tiana ou Miles.");
                return;
            }
            if (nome.getText().isBlank()) {
                mostrarModal("FALTOU O NOME", "Digite o nome da personagem para continuar.");
                return;
            }
            try {
                Protagonista protagonista = new Protagonista(nome.getText(), identidade.getValue(), perfeccionista.isSelected(), sociavel.isSelected(), teorico.isSelected(), vida.getValue(), experiencia.getValue(), economia.getValue());
                sessao.novaPartida(protagonista);
                sessao.inicializarControladorPartida(this);
                sessao.prepararCenaAtual();
            } catch (CarregamentoException | DadosInvalidosException ex) {
                mostrarModal("ERRO NO PROGRESSO", ex.getMessage());
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
            VBox conteudo = novoPainel("CAPÍTULO " + sessao.estado().getCapituloAtual(), p.getNome() + " · " + cena.getCapitulo());
            conteudo.getChildren().add(rotulo("ENERGIA " + p.getEnergia() + "/" + p.getEnergiaMaxima() + "    SCORE " + p.getScore() + "    PARTICIPAÇÃO " + p.getParticipacao()));
            if (cena.getPersonagemFalando() != null && !cena.getPersonagemFalando().isBlank()) {
                conteudo.getChildren().add(rotulo(cena.getPersonagemFalando().toUpperCase()));
            }
            Label narrativa = new Label(br.uefs.forkeazando.roteiro.NarrativaProtagonistas.resolver(cena, p));
            narrativa.getStyleClass().add("texto-narrativa");
            narrativa.setWrapText(true);
            narrativa.setMaxWidth(480);

            var recurso = getClass().getResource(
                    "/images/personagens/malu/neutra.png"
            );

            HBox dialogo = new HBox(16);
            dialogo.setAlignment(Pos.TOP_LEFT);

            if (recurso != null) {
                Image imagem = new Image(recurso.toExternalForm());

                ImageView retrato = new ImageView(imagem);
                retrato.setFitWidth(128);
                retrato.setFitHeight(128);
                retrato.setPreserveRatio(true);
                retrato.setSmooth(false);

                dialogo.getChildren().add(retrato);
            }

            dialogo.getChildren().add(narrativa);

            conteudo.getChildren().add(dialogo);
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
                        CenasController.ResultadoTransicao resultado = sessao.encaminharEscolha(escolha);

                        switch (resultado) {
                            case CONTINUAR_JOGO -> sessao.prepararCenaAtual();
                            case FIM_DE_JOGO -> mostrarModal("FIM DE JOGO", "Você concluiu sua jornada!");
                            case ERRO_DE_ROTA -> mostrarModal("ERRO DE ROTA", "Não foi possível determinar a próxima cena.");
                        }
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
        if (modal.isVisible()) {
            modal.toFront();
        }
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

        modal.setManaged(true);
        modal.setVisible(true);
        modal.toFront();
        botaoFechar.requestFocus();
    }

    @FXML
    private void fecharModal() {
        if (!avisosPendentes.isEmpty()) {
            String[] proximo = avisosPendentes.removeFirst();
            exibirModalAgora(proximo[0], proximo[1]);
            return;
        }

        modal.setVisible(false);
        modal.setManaged(false);

        if (telaSecundaria.isVisible()) {
            telaSecundaria.toFront();
        } else {
            raiz.requestFocus();
        }
    }

    public void pararAnimacoes() {
        if (movimentoCenario != null) movimentoCenario.stop();
        if (animacaoTitulo != null) animacaoTitulo.stop();
    }

    @Override
    public void resetarCacheVisual() {
        avisosPendentes.clear();
    }

    @Override
    public void mostrarGaleria(Protagonista protagonista) {
        VBox painel = novoPainel("GALERIA",
                "Habilidades e itens conquistados durante a jornada.");
        painel.setMaxHeight(Double.MAX_VALUE);

        for (Colecionavel.Tipo tipo : Colecionavel.Tipo.values()) {
            painel.getChildren().add(rotulo(tipo == Colecionavel.Tipo.HABILIDADE
                    ? "HABILIDADES" : "ITENS"));

            for (Colecionavel item : Colecionavel.values()) {
                if (item.getTipo() != tipo) continue;

                boolean desbloqueado = protagonista.temFlag(item.getFlag());
                painel.getChildren().add(rotulo(
                        (desbloqueado ? "✓ " : "🔒 ")
                                + (desbloqueado ? item.getNome() : "???")
                ));
            }
        }

        Button voltar = botaoAcao("VOLTAR À CENA");
        voltar.setOnAction(e -> abrirCena());
        painel.getChildren().add(voltar);
        mostrarTela(painel);
    }

    @Override
    public void renderizar(Cena cena, Protagonista p,
                           int semestresExtras, ApresentacaoDeCena visual) {
        // Reaproveita temporariamente o renderizador JavaFX existente.
        if (!sessao.possuiPartida()
                || sessao.estado().getCenaAtualId() != cena.getId()) {
            throw new IllegalStateException(
                    "A cena gráfica não corresponde à partida atual.");
        }

        abrirCena();
    }

    @Override
    public void mostrarBencaoCaramelo() {
        mostrarModal("BENÇÃO DO CARAMELO",
                "O Caramelo passa do lado. Ele para. Ele te olha.\n\n"
                        + "Ele vai embora.\n\n(Foi... um sinal?)");
    }

    @Override
    public void mostrarHabilidade(String flag) {
        String nome = Colecionavel.porFlag(flag)
                .map(Colecionavel::getNome)
                .orElse(flag);

        mostrarModal("NOVA HABILIDADE", nome);
    }

    @Override
    public void mostrarItem(String flag) {
        String nome = Colecionavel.porFlag(flag)
                .map(Colecionavel::getNome)
                .orElse(flag);

        mostrarModal("ITEM ADQUIRIDO", nome);
    }



    @Override
    public void mostrarConquista(Conquista conquista) {
        mostrarModal("CONQUISTA DESBLOQUEADA",
                conquista.getTitulo() + "\n\n" + conquista.getDescricao());
    }

    @Override
    public void mostrarMensagem(String mensagem) {
        mostrarModal("MENSAGEM", mensagem);
    }

    @Override
    public void mostrarImpactoRelacionamento(
            PersonagemSecundario npc,
            float valor,
            Protagonista.NivelRelacionamento nivel) {

        String mudanca = valor > 0 ? "melhorou"
                : valor < 0 ? "piorou" : "não mudou";

        mostrarModal("RELACIONAMENTO",
                "Seu relacionamento com " + npc.getNome()
                        + " " + mudanca + ".\n"
                        + "Nível atual: " + nivel);
    }

    @Override
    public void mostrarEpilogos(Protagonista p) {
        VBox painel = novoPainel("O QUE FICOU",
                "O fim da jornada de " + p.getNome());
        painel.setMaxHeight(Double.MAX_VALUE);

        for (Epilogos.Epilogo bloco : Epilogos.de(p)) {
            painel.getChildren().add(rotulo(bloco.titulo()));

            Label texto = new Label(bloco.texto());
            texto.getStyleClass().add("texto-narrativa");
            texto.setWrapText(true);
            texto.setMaxWidth(650);

            painel.getChildren().add(texto);
        }

        Button voltar = botaoAcao("VOLTAR AO MENU");
        voltar.setOnAction(e -> voltarAoMenu());
        painel.getChildren().add(voltar);

        mostrarTela(painel);
    }

    @Override
    public void mostrarCurriculo(Protagonista p) {
        var linhas = Curriculo.linhas(p);
        Curriculo.Categoria categoria = null;
        StringBuilder conteudo = new StringBuilder();

        for (Curriculo.Linha linha : linhas) {
            if (categoria != null && categoria != linha.categoria()) {
                mostrarModal("CURRÍCULO · " + categoria.rotulo,
                        conteudo.toString());
                conteudo.setLength(0);
            }

            categoria = linha.categoria();
            conteudo.append(linha.positiva() ? "✓ " : "✗ ")
                    .append(linha.texto())
                    .append("\n");
        }

        if (categoria != null) {
            mostrarModal("CURRÍCULO · " + categoria.rotulo,
                    conteudo.toString());
        }
    }

    @Override
    public void mostrarTransicao(String capitulo) {
        mostrarModal("NOVO CAPÍTULO", capitulo);
    }

    @Override
    public void mostrarChuteDoSistema(Protagonista p) {
        String resultado = SistemaAcademico.descricaoDe(
                SistemaAcademico.classificar(p));

        mostrarModal("SISTEMA ACADÊMICO · VEREDITO",
                "Processamento concluído.\n\n"
                        + resultado
                        + "\n\nRecomendação baseada em padrões históricos."
                        + "\nO sistema não decide por você. Ainda.");
    }
}