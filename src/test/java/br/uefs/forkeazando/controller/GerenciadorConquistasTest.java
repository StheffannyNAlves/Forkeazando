package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.dao.GerenciadorProgresso;
import br.uefs.forkeazando.model.Conquista;
import br.uefs.forkeazando.model.Progresso;
import br.uefs.forkeazando.model.Protagonista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GerenciadorConquistasTest {

    @TempDir
    Path pasta;

    private Protagonista p;
    private Progresso progresso;
    private List<String> mensagens;
    private List<Conquista> notificadas;

    @BeforeEach
    void preparar() {
        p = new Protagonista("Ana", false, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
        progresso = new Progresso();
        mensagens = new ArrayList<>();
        notificadas = new ArrayList<>();
    }

    private GerenciadorConquistas gerenciador(GerenciadorProgresso gp) {
        GerenciadorConquistas g = new GerenciadorConquistas(gp, mensagens::add);
        g.carregarDesbloqueadas(progresso);
        return g;
    }

    @Test
    void desbloqueiaNotificaEPersiste() {
        p.adicionarFlag("cap3_concluido");
        GerenciadorProgresso gp = new GerenciadorProgresso(pasta);

        gerenciador(gp).verificar(p, progresso, notificadas::add);

        assertEquals(List.of(Conquista.PRIMEIRO_PASSO), notificadas);
        assertTrue(Files.exists(pasta.resolve("progresso.json")));
        assertTrue(mensagens.isEmpty());
    }

    @Test
    void naoNotificaDuasVezesAMesmaConquista() {
        p.adicionarFlag("cap3_concluido");
        GerenciadorConquistas g = gerenciador(new GerenciadorProgresso(pasta));

        g.verificar(p, progresso, notificadas::add);
        g.verificar(p, progresso, notificadas::add);

        assertEquals(1, notificadas.size());
    }

    @Test
    void falhaDeGravacaoGeraUmaUnicaMensagemMesmoComVariasConquistas() throws IOException {
        // A "pasta" é um arquivo comum, então gravar o progresso sempre falha.
        Path arquivoNoLugarDaPasta = Files.writeString(pasta.resolve("bloqueio"), "x");
        p.adicionarFlag("cap3_concluido");
        p.adicionarFlag("hab_sobreviveu_greve");

        gerenciador(new GerenciadorProgresso(arquivoNoLugarDaPasta))
                .verificar(p, progresso, notificadas::add);

        assertEquals(2, notificadas.size());
        assertEquals(1, mensagens.size());
        assertTrue(mensagens.get(0).contains(Conquista.PRIMEIRO_PASSO.getTitulo()));
        assertTrue(mensagens.get(0).contains(Conquista.SOBREVIVENTE.getTitulo()));
    }

    @Test
    void persistenciaPendenteETentadaNaProximaVerificacao() throws IOException {
        Path bloqueio = Files.writeString(pasta.resolve("bloqueio"), "x");
        p.adicionarFlag("cap3_concluido");
        GerenciadorConquistas g = gerenciador(new GerenciadorProgresso(bloqueio));
        g.verificar(p, progresso, notificadas::add);
        assertEquals(1, mensagens.size());

        g.verificar(p, progresso, notificadas::add);

        assertEquals(2, mensagens.size());
    }

    @Test
    void argumentosNulosSaoRejeitados() {
        GerenciadorConquistas g = gerenciador(new GerenciadorProgresso(pasta));

        assertThrows(NullPointerException.class, () -> g.verificar(null, progresso, notificadas::add));
        assertThrows(NullPointerException.class, () -> g.verificar(p, null, notificadas::add));
        assertThrows(NullPointerException.class, () -> g.verificar(p, progresso, null));
    }
}