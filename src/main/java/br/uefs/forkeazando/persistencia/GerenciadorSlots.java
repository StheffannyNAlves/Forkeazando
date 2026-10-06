package br.uefs.forkeazando.persistencia;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;

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
    public static void salvar(Estado estado, Path pasta, int numeroSlot) throws SaveException {
        Path destino = caminhoDoSlot(pasta, numeroSlot);
        if (estado == null || estado.getProtagonista() == null) {
            throw new SaveException(
                    "Não é possível salvar um Estado sem protagonista"
            );
        }
        try {
            Files.createDirectories(pasta);
        } catch (IOException e) {
            throw new SaveException("Não foi possível criar a pasta de saves: " + pasta, e);
        }
        GerenciadorSave.salvar(estado, destino);
        estado.marcarSalvo();
    }

    public static Estado carregar(Path pasta, int numSlot)
         throws CarregamentoException, DadosInvalidosException{
        Path origem = caminhoDoSlot(pasta, numSlot);
        if (!Files.exists(origem)){
            throw  new CarregamentoException("Slot vazio: " + numSlot);
        }
        return GerenciadorSave.carregar(origem);
    }

    public static List<String> listar(Path pasta) {
        List<String> linhas = new ArrayList<>();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

        for (int i = 1; i <= TOTAL_SLOTS; i++){
            Path arquivo = caminhoDoSlot(pasta, i);
            if (!Files.exists(arquivo)) {
                linhas.add("Slot " + i + " - Vazio");
                continue;
            }

            try {
                Estado estado = GerenciadorSave.carregar(arquivo);
                String nome = estado.getProtagonista().getNome();
                int capitulo = estado.getCapituloAtual();
                FileTime tempoArquivo = Files.getLastModifiedTime(arquivo);
                LocalDateTime data = tempoArquivo.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                linhas.add("Slot " + i
                        + " - Ocupado"
                        + " | " + nome
                        + " | Capítulo " + capitulo
                        + " | " + data.format(formato)
                );
            } catch (CarregamentoException | DadosInvalidosException | IOException e){
                linhas.add("Slot " + i + " - Corrompido");
            }
        }

        return linhas;
    }
}