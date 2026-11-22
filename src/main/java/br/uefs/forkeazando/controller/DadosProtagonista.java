package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Protagonista;

/**
 * Dados coletados pela tela de criação. A validação e a montagem do
 * Protagonista ficam aqui (Controller), e não no handler do botão da View(estava antes aqui, precisava separar)
 */
public record DadosProtagonista(
        String nome,
        IdentidadeProtagonista identidade,
        boolean perfeccionista,
        boolean sociavel,
        boolean estudoTeorico,
        Protagonista.NivelVidaSocial vidaSocial,
        Protagonista.ExperienciaEmocionalEnsinoMedio experiencia,
        Protagonista.SituacaoEconomica situacaoEconomica) {

    public DadosProtagonista {
        if (identidade == null) {
            throw new IllegalArgumentException("Selecione Tiana ou Miles.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Digite o nome da personagem para continuar.");
        }
        if (vidaSocial == null || experiencia == null || situacaoEconomica == null) {
            throw new IllegalArgumentException("Preencha todas as características iniciais.");
        }
        nome = nome.trim();
    }

    public Protagonista criarProtagonista() {
        return new Protagonista(nome, identidade, perfeccionista, sociavel, estudoTeorico,
                vidaSocial, experiencia, situacaoEconomica);
    }
}
