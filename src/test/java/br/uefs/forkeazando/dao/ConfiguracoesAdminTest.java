package br.uefs.forkeazando.dao;

import br.uefs.forkeazando.excecao.CarregamentoException;
import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Configuracoes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguracoesAdminTest {

    @TempDir
    Path diretorioTemporario;

    @Test
    void devePreservarConfirmarSaidaAtivado()
            throws SaveException, DadosInvalidosException,
            CarregamentoException {

        Configuracoes original = new Configuracoes();
        original.setConfirmarSaida(true);

        Path arquivo = diretorioTemporario.resolve("config-true.json");

        ConfiguracoesAdmin.salvarConfigPersonalizadas(original, arquivo);

        Configuracoes carregada = ConfiguracoesAdmin.carregar(arquivo);

        assertTrue(carregada.isConfirmarSaida());
    }

    @Test
    void devePreservarConfirmarSaidaDesativado()
            throws SaveException, DadosInvalidosException,
            CarregamentoException {

        Configuracoes original = new Configuracoes();
        original.setConfirmarSaida(false);

        Path arquivo = diretorioTemporario.resolve("config-false.json");

        ConfiguracoesAdmin.salvarConfigPersonalizadas(original, arquivo);

        Configuracoes carregada = ConfiguracoesAdmin.carregar(arquivo);

        assertFalse(carregada.isConfirmarSaida());
    }
}