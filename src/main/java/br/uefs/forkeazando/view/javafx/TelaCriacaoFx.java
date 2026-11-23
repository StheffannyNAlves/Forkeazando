package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.controller.DadosProtagonista;
import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Protagonista;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.Objects;
import java.util.function.Consumer;

import static br.uefs.forkeazando.view.javafx.ComponentesFx.botaoAcao;
import static br.uefs.forkeazando.view.javafx.ComponentesFx.novoCheck;
import static br.uefs.forkeazando.view.javafx.ComponentesFx.novoCombo;
import static br.uefs.forkeazando.view.javafx.ComponentesFx.novoPainel;
import static br.uefs.forkeazando.view.javafx.ComponentesFx.rotulo;

/**
 * Criação do protagonista. Só coleta os campos e entrega um DadosProtagonista;
 * a validação e a criação do modelo acontecem no Controller.
 */
final class TelaCriacaoFx {
    private static final String RETRATO_TIANA = "/images/personagens/stheffanny/expressions/neutra.png";
    private static final String RETRATO_MILES = "/images/personagens/protagonista_masculino/expressions/neutra.png";
    private static final int TAMANHO_PREVIA = 128;

    private TextField nome;

    VBox criarPainel(Consumer<DadosProtagonista> aoComecar, Consumer<String> aoDadosInvalidos, Runnable aoVoltar) {
        Objects.requireNonNull(aoComecar);
        Objects.requireNonNull(aoDadosInvalidos);
        Objects.requireNonNull(aoVoltar);
        VBox conteudo = novoPainel("CRIE SEU PROTAGONISTA", "Cada escolha muda seus atributos iniciais.");
        ComboBox<IdentidadeProtagonista> identidade = novoCombo(IdentidadeProtagonista.values());
        identidade.setConverter(new StringConverter<>() {
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
        VBox cartaoTiana = criarCartaoPrevia(IdentidadeProtagonista.STHEFFANNY, "Tiana", identidade);
        VBox cartaoMiles = criarCartaoPrevia(IdentidadeProtagonista.DAVI, "Miles", identidade);
        HBox previaProtagonistas = new HBox(16, cartaoTiana, cartaoMiles);
        previaProtagonistas.getStyleClass().add("previa-protagonistas");

        nome = new TextField();
        nome.setPromptText("Nome exibido no jogo");
        nome.getStyleClass().add("campo-pixel");
        nome.setMaxWidth(450);
        nome.setText("Tiana");
        identidade.setValue(IdentidadeProtagonista.STHEFFANNY);
        atualizarDestaquePrevia(cartaoTiana, cartaoMiles, identidade.getValue());
        identidade.valueProperty().addListener((obs, anterior, proximo) -> {
            if (proximo == null) return;
            atualizarDestaquePrevia(cartaoTiana, cartaoMiles, proximo);
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
        ComboBox<Protagonista.ExperienciaEmocionalEnsinoMedio> experiencia =
                novoCombo(Protagonista.ExperienciaEmocionalEnsinoMedio.values());
        experiencia.setValue(Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA);
        ComboBox<Protagonista.SituacaoEconomica> economia = novoCombo(Protagonista.SituacaoEconomica.values());
        economia.setValue(Protagonista.SituacaoEconomica.ESTAVEL);

        conteudo.getChildren().addAll(
                rotulo("ESCOLHA O PROTAGONISTA"), previaProtagonistas, identidade,
                rotulo("SEU NOME"), nome,
                perfeccionista, sociavel, teorico,
                rotulo("VIDA SOCIAL"), vida,
                rotulo("EXPERIÊNCIA NO ENSINO MÉDIO"), experiencia,
                rotulo("SITUAÇÃO ECONÔMICA"), economia
        );

        Label dica = new Label("Essas escolhas definem seu ponto de partida na universidade.");
        dica.getStyleClass().add("texto-ajuda");
        Button comecar = botaoAcao("COMEÇAR A JORNADA");
        comecar.setOnAction(e -> {
            DadosProtagonista dados;
            try {
                dados = new DadosProtagonista(nome.getText(), identidade.getValue(),
                        perfeccionista.isSelected(), sociavel.isSelected(), teorico.isSelected(),
                        vida.getValue(), experiencia.getValue(), economia.getValue());
            } catch (IllegalArgumentException ex) {
                aoDadosInvalidos.accept(ex.getMessage());
                return;
            }
            aoComecar.accept(dados);
        });
        Button voltar = botaoAcao("VOLTAR AO MENU");
        voltar.setOnAction(e -> aoVoltar.run());
        conteudo.getChildren().addAll(dica, new HBox(14, comecar, voltar));
        return conteudo;
    }

    void focarNome() {
        if (nome != null) nome.requestFocus();
    }

    static String caminhoRetrato(IdentidadeProtagonista identidade) {
        return identidade == IdentidadeProtagonista.DAVI ? RETRATO_MILES : RETRATO_TIANA;
    }

    private static VBox criarCartaoPrevia(
            IdentidadeProtagonista valor,
            String nomeExibido,
            ComboBox<IdentidadeProtagonista> identidade) {
        var recurso = Objects.requireNonNull(
                TelaCriacaoFx.class.getResource(caminhoRetrato(valor)),
                "Imagem de prévia não encontrada para " + nomeExibido);
        ImageView retrato = new ImageView(new Image(recurso.toExternalForm()));
        retrato.setFitWidth(TAMANHO_PREVIA);
        retrato.setFitHeight(TAMANHO_PREVIA);
        retrato.setPreserveRatio(true);
        retrato.setSmooth(false);
        retrato.getStyleClass().add("retrato-personagem");

        Label legenda = new Label(nomeExibido);
        legenda.getStyleClass().add("previa-protagonista-nome");

        VBox cartao = new VBox(8, retrato, legenda);
        cartao.setAlignment(Pos.CENTER);
        cartao.setUserData(valor);
        cartao.getStyleClass().add("cartao-protagonista");
        cartao.setOnMouseClicked(e -> identidade.setValue(valor));
        return cartao;
    }

    private static void atualizarDestaquePrevia(
            VBox cartaoTiana,
            VBox cartaoMiles,
            IdentidadeProtagonista selecionada) {
        aplicarDestaque(cartaoTiana, selecionada == IdentidadeProtagonista.STHEFFANNY);
        aplicarDestaque(cartaoMiles, selecionada == IdentidadeProtagonista.DAVI);
    }

    private static void aplicarDestaque(VBox cartao, boolean ativo) {
        cartao.getStyleClass().remove("selecionado");
        if (ativo) cartao.getStyleClass().add("selecionado");
    }
}
