package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.controller.DadosProtagonista;
import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Protagonista;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
        nome = new TextField();
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
        ComboBox<Protagonista.ExperienciaEmocionalEnsinoMedio> experiencia =
                novoCombo(Protagonista.ExperienciaEmocionalEnsinoMedio.values());
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
}
