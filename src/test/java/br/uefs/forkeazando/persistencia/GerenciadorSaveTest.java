package br.uefs.forkeazando.persistencia;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.PersonagemSecundario;
import br.uefs.forkeazando.model.Protagonista;
import com.google.gson.JsonParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;


class GerenciadorSaveTest {

    @TempDir
    Path diretorioTemporario;

    @Test
    void deveSalvarECarregarEstadoCorretamente() throws IOException, CarregamentoException, SaveException, DadosInvalidosException {
        Protagonista protagonista = new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        );

        protagonista.ganharScore(40);

        protagonista.alterarRelacionamento(
                PersonagemSecundario.VETERANO,
                3.5f
        );

        Estado estadoOriginal = new Estado();

        estadoOriginal.iniciar(protagonista);
        estadoOriginal.setCenaAtualId(3050);

        Path arquivo = diretorioTemporario.resolve("save.json");

        GerenciadorSave.salvar(estadoOriginal, arquivo);
        Estado estadoCarregado = GerenciadorSave.carregar(arquivo);
        assertNotSame(estadoOriginal, estadoCarregado);

        assertEquals(
                estadoOriginal.getProtagonista().getNome(),
                estadoCarregado.getProtagonista().getNome()
        );

        assertEquals(
                estadoOriginal.getProtagonista().getScore(),
                estadoCarregado.getProtagonista().getScore()
        );

        assertEquals(
                estadoOriginal.getProtagonista().getRelacionamento(
                        PersonagemSecundario.VETERANO
                ),
                estadoCarregado.getProtagonista().getRelacionamento(
                        PersonagemSecundario.VETERANO
                ),
                0.001f
        );

        assertEquals(
                estadoOriginal.getProtagonista().getHistoricoFlags(),
                estadoCarregado.getProtagonista().getHistoricoFlags()
        );

        assertEquals(
                estadoOriginal.getCenaAtualId(),
                estadoCarregado.getCenaAtualId()
        );
    }

    @Test
    void deveLancarExcecaoAoCarregarArquivoInexistente()
            throws CarregamentoException {

        Path arquivo = diretorioTemporario.resolve("nao_existe.json");

        CarregamentoException excecao = assertThrows(
                CarregamentoException.class,
                () -> GerenciadorSave.carregar(arquivo)
        );

        assertInstanceOf(IOException.class, excecao.getCause());
    }


    @Test
    void deveLancarExcecaoAoCarregarJsonInvalido()
            throws IOException, CarregamentoException {

        Path arquivo = diretorioTemporario.resolve("quebrado.json");

        Files.writeString(arquivo, "{ \"protagonista\": ");

        CarregamentoException excecao = assertThrows(
                CarregamentoException.class,
                () -> GerenciadorSave.carregar(arquivo)
        );

        assertInstanceOf(JsonParseException.class, excecao.getCause());
    }
    @Test
    void deveRejeitarJsonQueNaoEhObjeto() throws IOException {
        Path arquivo = diretorioTemporario.resolve("save.json");

        Files.writeString(arquivo, "[]");

        assertThrows(
                DadosInvalidosException.class,
                () -> GerenciadorSave.carregar(arquivo)
        );
    }
    @Test
    void deveRejeitarProtagonistaQueNaoEhObjeto() throws IOException {
        Path arquivo = diretorioTemporario.resolve("save.json");

        Files.writeString(
                arquivo,
                """
                {
                    "protagonista": "Stheffanny",
                    "capituloAtual": 1,
                    "cenaAtualId": 3050
                }
                """
        );

        assertThrows(
                DadosInvalidosException.class,
                () -> GerenciadorSave.carregar(arquivo)
        );
    }
    @Test
    void deveLancarExcecaoAoCarregarArquivoVazio()
            throws IOException, CarregamentoException {

        Path arquivo = diretorioTemporario.resolve("vazio.json");

        Files.writeString(arquivo, "");

        CarregamentoException excecao = assertThrows(
                CarregamentoException.class,
                () -> GerenciadorSave.carregar(arquivo)
        );

        assertNull(excecao.getCause());
    }

    @Test
    void deveLancarExcecaoAoSalvarEmDiretorioInexistente()
            throws SaveException {

        Path arquivo = diretorioTemporario
                .resolve("pasta_inexistente")
                .resolve("save.json");

        Estado estado = new Estado();

        SaveException excecao = assertThrows(
                SaveException.class,
                () -> GerenciadorSave.salvar(estado, arquivo)
        );

        assertInstanceOf(
                NoSuchFileException.class,
                excecao.getCause()
        );
    }


    @Test
    void deveSobrescreverSaveExistente()
            throws SaveException, IOException {

        Path arquivo = diretorioTemporario.resolve("save.json");

        Estado estado1 = new Estado();

        Estado estado2 = new Estado();

        Protagonista protagonista = new Protagonista(
                "Stheffanny",
                false,
                true,
                true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        );

        estado2.iniciar(protagonista);
        estado2.setCenaAtualId(3050);

        GerenciadorSave.salvar(estado1, arquivo);
        GerenciadorSave.salvar(estado2, arquivo);

        String conteudo = Files.readString(arquivo);

        assertTrue(conteudo.contains("Stheffanny"));
        assertTrue(conteudo.contains("3050"));
    }


    @Test
    void deveRejeitarSaveSemProtagonista() throws IOException {
        Path arquivo = diretorioTemporario.resolve("save.json");

        Files.writeString(
                arquivo,
                """
                {
                    "capituloAtual": 1,
                    "cenaAtualId": 3050
                }
                """
        );

        assertThrows(
                DadosInvalidosException.class,
                () -> GerenciadorSave.carregar(arquivo)
        );
    }
    @Test
    void deveRejeitarSaveSemHistoricoFlags() throws IOException {
        Path arquivo = diretorioTemporario.resolve("save.json");

        Files.writeString(
                arquivo,
                """
                {
                    "protagonista": {
                        "nome": "Stheffanny",
                        "relacionamentos": {}
                    },
                    "capituloAtual": 1,
                    "cenaAtualId": 3050
                }
                """
        );

        assertThrows(
                DadosInvalidosException.class,
                () -> GerenciadorSave.carregar(arquivo)
        );
    }
    @Test
    void deveRejeitarSaveSemRelacionamentos() throws IOException {
        Path arquivo = diretorioTemporario.resolve("save.json");

        Files.writeString(
                arquivo,
                """
                {
                    "protagonista": {
                        "nome": "Stheffanny",
                        "historicoFlags": []
                    },
                    "capituloAtual": 1,
                    "cenaAtualId": 3050
                }
                """
        );

        assertThrows(
                DadosInvalidosException.class,
                () -> GerenciadorSave.carregar(arquivo)
        );
    }

    @Test
    void naoDeveDeixarArquivoTemporarioAposSalvar()
            throws SaveException {

        Path arquivo = diretorioTemporario.resolve("save.json");
        Path temporario = diretorioTemporario.resolve("save.json.tmp");

        Estado estado = new Estado();

        GerenciadorSave.salvar(estado, arquivo);

        assertTrue(Files.exists(arquivo));
        assertFalse(Files.exists(temporario));
    }
}
