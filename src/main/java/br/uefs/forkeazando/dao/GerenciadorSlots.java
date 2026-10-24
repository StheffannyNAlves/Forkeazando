
package br.uefs.forkeazando.dao;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.model.ResumoSlot;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class GerenciadorSlots {

    public static final int TOTAL_SLOTS = 3;
    private static final String ARQUIVO_AUTOSAVE = "autosave.json";

    public static OptionalInt primeiroSlotLivre(Path pasta) {
        for (int i = 1; i <= TOTAL_SLOTS; i++) {
            if (!Files.exists(caminhoDoSlot(pasta, i))) {
                return OptionalInt.of(i);
            }
        }

        return OptionalInt.empty();
    }

    public static Path caminhoDoSlot(Path pasta, int numero) {
        if (numero < 1 || numero > TOTAL_SLOTS) {
            throw new IllegalArgumentException(
                    "Número de slot inválido: " + numero
                            + ". Deve estar entre 1 e " + TOTAL_SLOTS + "."
            );
        }

        return pasta.resolve("slot" + numero + ".json");
    }

    public static Path caminhoDoAutosave(Path pasta) {
        return pasta.resolve(ARQUIVO_AUTOSAVE);
    }

    private static void prepararSalvamento(Estado estado, Path pasta)
            throws SaveException {
        if (estado == null || estado.getProtagonista() == null) {
            throw new SaveException(
                    "Não é possível salvar um Estado sem protagonista"
            );
        }

        try {
            Files.createDirectories(pasta);
        } catch (IOException e) {
            throw new SaveException(
                    "Não foi possível criar a pasta de saves: " + pasta, e
            );
        }
    }

    public static void salvar(Estado estado, Path pasta, int numeroSlot)
            throws SaveException {
        Path destino = caminhoDoSlot(pasta, numeroSlot);
        prepararSalvamento(estado, pasta);

        GerenciadorSave.salvar(estado, destino);
        estado.marcarSalvo();
    }

    /**
     * Grava o autosave. Não chama marcarSalvo(): o autosave não substitui o
     * salvamento manual, então o jogo ainda deve perguntar se quer salvar ao sair.
     */
    public static void salvarAutosave(Estado estado, Path pasta)
            throws SaveException {
        prepararSalvamento(estado, pasta);
        GerenciadorSave.salvar(estado, caminhoDoAutosave(pasta));
    }

    public static Estado carregar(Path pasta, int numSlot)
            throws CarregamentoException, DadosInvalidosException {
        Path origem = caminhoDoSlot(pasta, numSlot);

        if (!Files.exists(origem)) {
            throw new CarregamentoException("Slot vazio: " + numSlot);
        }

        return GerenciadorSave.carregar(origem);
    }

    public static void excluir(Path pasta, int numSlot) throws SaveException {
        Path arquivo = caminhoDoSlot(pasta, numSlot);

        try {
            if (!Files.deleteIfExists(arquivo)) {
                throw new SaveException(
                        "O slot " + numSlot + " já está vazio"
                );
            }
        } catch (IOException e) {
            throw new SaveException(
                    "Não foi possível excluir o slot " + numSlot, e
            );
        }
    }

    public static List<ResumoSlot> listar(Path pasta) {
        List<ResumoSlot> resumos = new ArrayList<>();

        for (int i = 1; i <= TOTAL_SLOTS; i++) {
            Path arquivo = caminhoDoSlot(pasta, i);

            if (!Files.exists(arquivo)) {
                resumos.add(new ResumoSlot(
                        i, ResumoSlot.Status.VAZIO,
                        null, 0, null, 0, 0, null, 0, 0
                ));
                continue;
            }

            try {
                GerenciadorSave.SaveCarregado save =
                        GerenciadorSave.carregarComMetadados(arquivo);

                Estado estado = save.estado();
                Protagonista p = estado.getProtagonista();
                int capitulo = estado.getCapituloAtual();

                LocalDateTime data;

                if (save.dataSalvamento().isPresent()) {
                    data = save.dataSalvamento()
                            .get()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                } else {
                    // Compatibilidade com saves antigos, sem data persistida.
                    FileTime tempoArquivo = Files.getLastModifiedTime(arquivo);

                    data = tempoArquivo.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                }

                resumos.add(new ResumoSlot(i, ResumoSlot.Status.OCUPADO, p.getNome(), capitulo, data, p.getScore(),
                        p.getParticipacao(), String.valueOf(p.getRank()),p.getEnergia(), p.getEnergiaMaxima()
                ));

            } catch (CarregamentoException | DadosInvalidosException | IOException e) {
                resumos.add(new ResumoSlot(i, ResumoSlot.Status.CORROMPIDO, null, 0, null, 0, 0, null, 0, 0));
            }
        }

        return resumos;
    }
}
