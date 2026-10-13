package br.uefs.forkeazando.dao;
import java.nio.file.AtomicMoveNotSupportedException;
import br.uefs.forkeazando.excecao.SaveException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
// Só se preocupa em gravar o conteudo num arquivo temporário, tentar a substituição e lidar com falha de escrita e movimentação
// Revisar essa classe, será que é realmente necessária? <- meus lembretes
public class ArquivoAtomico {

    private ArquivoAtomico(){}
    public static void gravar(Path destino, String conteudo) throws SaveException {
        Path temporario = destino.resolveSibling(
                destino.getFileName() + ".tmp"
        );

        try {
            Files.writeString(temporario, conteudo);

            try {
                Files.move(temporario, destino,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporario, destino,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            apagarTemporario(temporario);
            throw new SaveException(
                    "Não foi possível gravar o arquivo: " + destino, e
            );
        }
    }

    private static void apagarTemporario(Path temporario) {
        try {
            Files.deleteIfExists(temporario);
        } catch (IOException ignored) {
        }
    }
}