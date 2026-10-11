
package br.uefs.forkeazando.dao;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Final;
import br.uefs.forkeazando.model.Progresso;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GerenciadorProgressoTest {
    @TempDir
    Path tempDir;

    private static Path arquivoDe(Path pasta) {
        return pasta.resolve(GerenciadorProgresso.NOME_ARQUIVO);
    }
    @Test
    void arquivoAusenteDevolveProgressoVazio() throws Exception {
        GerenciadorProgresso gerenciador =
                new GerenciadorProgresso(tempDir.resolve("saves"));

        Progresso progresso = gerenciador.carregar();

        assertNotNull(progresso);
        assertTrue(progresso.finaisAlcancados().isEmpty());    }

    @Test
    void arquivoVazioLancaCarregamentoException() throws Exception {
        Path pasta = Files.createDirectory(tempDir.resolve("vazio"));
        Files.createFile(pasta.resolve("progresso.json"));

        GerenciadorProgresso gerenciador = new GerenciadorProgresso(pasta);

        assertThrows(CarregamentoException.class, gerenciador::carregar);
    }

    @Test
    void jsonQuebradoLancaCarregamentoException() throws Exception {
        Path pasta = Files.createDirectory(tempDir.resolve("quebrado"));
        Files.writeString(pasta.resolve("progresso.json"), "{ inválido");

        GerenciadorProgresso gerenciador = new GerenciadorProgresso(pasta);

        assertThrows(CarregamentoException.class, gerenciador::carregar);
    }

    @Test
    void raizQueNaoEhObjetoLancaDadosInvalidosException() throws Exception {
        verificarJsonInvalido("[]");
        verificarJsonInvalido("\"texto\"");
    }

    @Test
    void finaisNuloOuArrayLancaDadosInvalidosException() throws Exception {
        verificarJsonInvalido("{\"finais\":null}");
        verificarJsonInvalido("{\"finais\":[]}");
    }

    @Test
    void dataInvalidaLancaDadosInvalidosException() throws Exception {
        verificarJsonInvalido(
                "{\"finais\":{\"final_ic\":\"data-invalida\"}}"
        );
    }

    private void verificarJsonInvalido(String conteudo) throws Exception {
        Path pasta = Files.createTempDirectory(tempDir, "caso-");
        Files.writeString(pasta.resolve("progresso.json"), conteudo);
        GerenciadorProgresso gerenciador = new GerenciadorProgresso(pasta);

        assertThrows(DadosInvalidosException.class, gerenciador::carregar);
    }
    @Test
    void salvarCriaPastaInexistente() throws Exception {
        Path pasta = tempDir.resolve("nova-pasta");
        GerenciadorProgresso gerenciador = new GerenciadorProgresso(pasta);

        gerenciador.salvar(new Progresso());

        assertTrue(Files.isDirectory(pasta));
        assertTrue(Files.isRegularFile(pasta.resolve("progresso.json")));
    }

    @Test
    void arquivoTemporarioNaoSobraDepoisDeSalvar() throws Exception {
        Path pasta = Files.createDirectory(tempDir.resolve("temporario"));
        GerenciadorProgresso gerenciador = new GerenciadorProgresso(pasta);

        gerenciador.salvar(new Progresso());

        try (var arquivos = Files.list(pasta)) {
            assertFalse(
                    arquivos.anyMatch(p ->
                            p.getFileName().toString().endsWith(".tmp"))
            );
        }
    }

    @Test
    void salvarComArquivoNoLugarDaPastaLancaSaveException()
            throws Exception {
        Path arquivo = tempDir.resolve("nao-e-pasta");
        Files.writeString(arquivo, "sou um arquivo");

        GerenciadorProgresso gerenciador =
                new GerenciadorProgresso(arquivo);

        assertThrows(
                SaveException.class,
                () -> gerenciador.salvar(new Progresso())
        );
    }


    @Test
    void arquivoCorrompidoBloqueiaSalvamento() throws Exception {
        Path pasta = Files.createDirectory(tempDir.resolve("corrompido"));
        Files.writeString(pasta.resolve("progresso.json"), "{ inválido");

        GerenciadorProgresso gerenciador = new GerenciadorProgresso(pasta);

        assertThrows(CarregamentoException.class, gerenciador::carregar);

        assertThrows(
                SaveException.class,
                () -> gerenciador.salvar(new Progresso())
        );
        assertEquals("{ inválido", Files.readString(pasta.resolve("progresso.json")));
    }


    @Test
    void salvarECarregarPreservaFinaisDatasEOrdem() throws Exception {
        Path pasta = tempDir.resolve("ordem");
        GerenciadorProgresso gerenciador = new GerenciadorProgresso(pasta);
        Progresso original = new Progresso();

        LocalDateTime data1 = LocalDateTime.parse("2026-01-10T12:00:00");
        LocalDateTime data2 = LocalDateTime.parse("2026-02-20T15:30:00");

        List<Final> ordemEsperada = List.of(Final.DTEC, Final.IC);

        assertTrue(original.registrarFinal(Final.DTEC, data1));
        assertTrue(original.registrarFinal(Final.IC, data2));

        gerenciador.salvar(original);
        Progresso carregado = gerenciador.carregar();

        assertEquals(ordemEsperada, carregado.finaisAlcancados());
        assertEquals(data1, carregado.quandoAlcancou(Final.DTEC).orElseThrow());
        assertEquals(data2, carregado.quandoAlcancou(Final.IC).orElseThrow());
    }



    @Test
    void flagDesconhecidaSobreviveAoCarregarESalvar() throws Exception {
        Path pasta = Files.createDirectory(tempDir.resolve("flag-desconhecida"));
        Path arquivo = pasta.resolve("progresso.json");

        Files.writeString(arquivo,
                """
                {
                  "finais": {
                    "final_que_nao_existe": "2026-03-01T10:00:00",
                    "jornada_dtec": "2026-03-02T11:00:00"
                  }
                }
                """);

        GerenciadorProgresso gerenciador = new GerenciadorProgresso(pasta);
        Progresso progresso = gerenciador.carregar();

        assertTrue(progresso.alcancou(Final.DTEC));

        gerenciador.salvar(progresso);

        JsonObject json = JsonParser.parseString(
                Files.readString(arquivo)
        ).getAsJsonObject();

        JsonObject finais = json.getAsJsonObject("finais");

        assertTrue(finais.has("final_que_nao_existe"));
        assertEquals(
                "2026-03-01T10:00:00",
                finais.get("final_que_nao_existe").getAsString()
        );
    }

    @Test
    void arquivoCorrigidoDepoisDeCorrompidoVoltaASalvar() throws Exception {
        Path pasta = Files.createDirectory(tempDir.resolve("corrigido"));
        GerenciadorProgresso g = new GerenciadorProgresso(pasta);
        Path arquivo = g.caminhoArquivo();

        Files.writeString(arquivo, "{ inválido");
        assertThrows(CarregamentoException.class, g::carregar);

        Files.writeString(arquivo, "{}");
        g.carregar();

        assertDoesNotThrow(() -> g.salvar(new Progresso()));
    }

}
