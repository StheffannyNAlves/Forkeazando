package br.uefs.forkeazando.dao;

import br.uefs.forkeazando.excecao.SaveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/*
 * O fallback sem ATOMIC_MOVE não é coberto por testes automatizados.
 * Sua execução depende de o sistema de arquivos não suportar a movimentação solicitada.
 */

class ArquivoAtomicoTest {

    @TempDir
    Path pasta;

    private static Path temporarioDe(Path destino) {
        return destino.resolveSibling(destino.getFileName() + ".tmp");
    }

    @Test
    void gravaOConteudoNoDestino() throws Exception {
        Path destino = pasta.resolve("dados.json");

        ArquivoAtomico.gravar(destino, "conteudo");

        assertEquals("conteudo", Files.readString(destino));
    }

    @Test
    void substituiUmArquivoQueJaExiste() throws Exception {
        Path destino = pasta.resolve("dados.json");
        Files.writeString(destino, "antigo");

        ArquivoAtomico.gravar(destino, "novo");

        assertEquals("novo", Files.readString(destino));
    }

    @Test
    void naoDeixaArquivoTemporarioDepoisDeGravar() throws Exception {
        Path destino = pasta.resolve("dados.json");

        ArquivoAtomico.gravar(destino, "conteudo");

        assertFalse(Files.exists(temporarioDe(destino)));
    }

    @Test
    void pastaInexistenteLancaSaveException() {
        Path destino = pasta.resolve("nao-existe").resolve("dados.json");

        SaveException e = assertThrows(SaveException.class,
                () -> ArquivoAtomico.gravar(destino, "conteudo"));

        assertInstanceOf(IOException.class, e.getCause());
    }

    @Test
    void falhaNaSubstituicaoLancaSaveExceptionESemDeixarTemporario() throws Exception {
        Path destino = Files.createDirectory(pasta.resolve("dados.json"));
        Files.writeString(destino.resolve("dentro.txt"), "x");

        assertThrows(SaveException.class,
                () -> ArquivoAtomico.gravar(destino, "conteudo"));

        assertFalse(Files.exists(temporarioDe(destino)));
    }
}