package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.model.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;

public final class TelaStatusFx {

    private static final List<PersonagemSecundario> PERSONAGENS = List.of(
            PersonagemSecundario.VETERANO,
            PersonagemSecundario.ORIENTADOR_IC,
            PersonagemSecundario.PROFESSOR_DTEC,
            PersonagemSecundario.PROFESSOR_PBL,
            PersonagemSecundario.DANDARA,
            PersonagemSecundario.BIA,
            PersonagemSecundario.ZE_MODULO8,
            PersonagemSecundario.SISTEMA
    );

    public VBox criarPainel(Estado estado, Runnable aoVoltar) {
        Objects.requireNonNull(estado);
        Objects.requireNonNull(aoVoltar);

        Protagonista p = estado.getProtagonista();

        VBox painel = new VBox(12);
        painel.setAlignment(Pos.CENTER_LEFT);
        painel.setPadding(new Insets(26));
        painel.setMaxWidth(760);
        painel.getStyleClass().add("painel-interno");

        Label titulo = texto("STATUS · " + p.getNome().toUpperCase());
        titulo.getStyleClass().add("titulo-modal");

        painel.getChildren().addAll(
                titulo,
                texto("CAPÍTULO " + estado.getCapituloAtual()
                        + " · " + p.getRank()),

                texto("ATRIBUTOS"),
                texto("Energia: " + p.getEnergia()
                        + "/" + p.getEnergiaMaxima()),
                texto("Score: " + p.getScore()),
                texto("Participação: " + p.getParticipacao()),
                texto("Confiança: " + p.getConfianca()),
                texto("Vida social: " + p.getVidaSocial()),
                texto("Carisma: " + p.getCarisma()),
                texto("Lógica: " + p.getLogica()),
                texto("Sanidade: " + p.getSanidade())
        );

        // Inventário: usa os colecionáveis já definidos no modelo.
        painel.getChildren().add(texto("INVENTÁRIO"));

        int quantidadeItens = 0;

        for (Colecionavel item : Colecionavel.values()) {
            if (item.getTipo() == Colecionavel.Tipo.ITEM
                    && p.temFlag(item.getFlag())) {

                painel.getChildren().add(
                        texto("◆ " + item.getNome())
                );

                quantidadeItens++;
            }
        }

        if (quantidadeItens == 0) {
            painel.getChildren().add(
                    texto("Nenhum item encontrado.")
            );
        }

        // Relacionamentos registrados no modelo.
        painel.getChildren().add(texto("RELACIONAMENTOS"));

        int quantidadeRelacoes = 0;

        for (PersonagemSecundario personagem : PERSONAGENS) {
            float pontos = p.getRelacionamento(personagem);

            if (pontos != 0f) {
                painel.getChildren().add(
                        texto(personagem.getNome() + ": " + pontos)
                );
                quantidadeRelacoes++;
            }
        }

        if (quantidadeRelacoes == 0) {
            painel.getChildren().add(
                    texto("Nenhum relacionamento alterado.")
            );
        }

        // Objetivo derivado da progressão atual.
        painel.getChildren().add(texto("OBJETIVO ATUAL"));

        painel.getChildren().add(
                texto(estado.isPartidaConcluida()
                        ? "Jornada acadêmica concluída."
                        : "Continuar a jornada do capítulo "
                        + estado.getCapituloAtual() + ".")
        );

        // Registros persistidos das escolhas e dos marcos.
        painel.getChildren().add(texto("DECISÕES E MARCOS"));

        List<String> registros = p.getHistoricoFlags()
                .stream()
                .filter(flag -> !flag.startsWith("item_"))
                .filter(flag -> !flag.startsWith("hab_"))
                .filter(flag -> !flag.startsWith("perfil_"))
                .toList();

        if (registros.isEmpty()) {
            painel.getChildren().add(
                    texto("Nenhum marco registrado.")
            );
        } else {
            int inicio = Math.max(0, registros.size() - 8);

            for (int i = inicio; i < registros.size(); i++) {
                painel.getChildren().add(
                        texto("◆ " + registros.get(i)
                                .replace('_', ' '))
                );
            }
        }

        Button voltar = new Button("VOLTAR À HISTÓRIA");
        voltar.getStyleClass().add("botao-voltar");
        AnimacoesFx.instalarHover(voltar);
        voltar.setOnAction(e -> aoVoltar.run());

        painel.getChildren().add(voltar);

        return painel;
    }

    private static Label texto(String conteudo) {
        Label label = new Label(conteudo);
        label.setWrapText(true);
        label.getStyleClass().add("rotulo-pixel");
        return label;
    }
}