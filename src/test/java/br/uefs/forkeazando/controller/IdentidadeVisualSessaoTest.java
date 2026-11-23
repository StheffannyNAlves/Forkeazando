package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.adapter.CarregamentoAdapter;
import br.uefs.forkeazando.dao.GerenciadorSave;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Final;
import br.uefs.forkeazando.model.IdentidadeProtagonista;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.roteiro.VisuaisDeCena;
import br.uefs.forkeazando.roteiro.VisuaisDeFinal;
import br.uefs.forkeazando.roteiro.Epilogos;
import br.uefs.forkeazando.view.ApresentacaoCena;
import br.uefs.forkeazando.view.pixel.Ilustracoes;
import br.uefs.forkeazando.view.pixel.PerfilProtagonista;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.lang.reflect.Proxy;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class IdentidadeVisualSessaoTest {
    @TempDir Path pasta;

    private static Protagonista personagem(IdentidadeProtagonista identidade) {
        // O nome não pode decidir qual protagonista aparece
        return new Protagonista("Nome personalizado", identidade, false, false, false,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
    }

    private static ApresentacaoCena apresentacao() {
        return (ApresentacaoCena) Proxy.newProxyInstance(ApresentacaoCena.class.getClassLoader(),
                new Class<?>[]{ApresentacaoCena.class}, (p, metodo, argumentos) -> null);
    }

    @Test void novasSessoesEDecisoesSeguidasDeCargaRecuperamAIdentidadeDoSlot() throws Exception {
        CarregamentoAdapter adapter = new CarregamentoAdapter(pasta);
        SessaoGrafica sessao = new SessaoGrafica(adapter, pasta);
        ApresentacaoCena view = apresentacao();
        int slot = 0;
        for (IdentidadeProtagonista identidade : IdentidadeProtagonista.values()) {
            PerfilProtagonista perfil = identidade == IdentidadeProtagonista.DAVI
                    ? PerfilProtagonista.MILES : PerfilProtagonista.TIANA;
            sessao.novaPartida(personagem(identidade), view);
            sessao.prepararCenaAtual();
            assertEquals(perfil, PerfilProtagonista.de(sessao.estado().getProtagonista()));
            sessao.encaminharEscolha(sessao.escolhasDisponiveis().getFirst());
            int cenaSalva = sessao.estado().getCenaAtualId();
            sessao.salvarEmSlot(++slot);
            IdentidadeProtagonista outra = identidade == IdentidadeProtagonista.DAVI
                    ? IdentidadeProtagonista.STHEFFANNY : IdentidadeProtagonista.DAVI;
            sessao.novaPartida(personagem(outra), view);
            sessao.prepararCenaAtual();
            sessao.carregarPartida(adapter.carregarSlot(slot), view);
            sessao.prepararCenaAtual();
            assertEquals(cenaSalva, sessao.estado().getCenaAtualId());
            assertEquals(perfil, PerfilProtagonista.de(sessao.estado().getProtagonista()));
            assertFalse(sessao.escolhasDisponiveis().isEmpty());
        }
    }

    @Test void savesEmCapitulosEFinaisPreservamExatamenteOsQuadrosParaAmbos() throws Exception {
        for (IdentidadeProtagonista identidade : IdentidadeProtagonista.values()) {
            Estado estado = new Estado(); estado.iniciar(personagem(identidade));
            PerfilProtagonista perfil = PerfilProtagonista.de(estado.getProtagonista());
            for (int id : new int[]{0, 8, 9, 14, 200, 306, 550, 555, 600, 605, 650, 655, 662, 663, 664, 665, 666}) {
                estado.setCenaAtualId(id);
                Path arquivo = pasta.resolve("visual-" + identidade + "-" + id + ".json");
                GerenciadorSave.salvar(estado, arquivo);
                Estado carregado = GerenciadorSave.carregar(arquivo);
                assertEquals(perfil, PerfilProtagonista.de(carregado.getProtagonista()));
                String chave = VisuaisDeCena.paraCena(id).orElseThrow().ilustracao();
                assertArrayEquals(Ilustracoes.linhas(chave, perfil),
                        Ilustracoes.linhas(chave, PerfilProtagonista.de(carregado.getProtagonista())), "Cena " + id);
            }
        }
    }

    @Test void todosOsFinaisAtuaisELegadosRestauramEpilogosEIdentidadeVisual() throws Exception {
        for (IdentidadeProtagonista identidade : IdentidadeProtagonista.values()) {
            for (Final finalEscolhido : Final.values()) {
                Protagonista jogador = personagem(identidade);
                jogador.adicionarFlag(Final.FLAG_CONCLUSAO);
                jogador.adicionarFlag(finalEscolhido.getTrilha());
                Estado estado = new Estado(); estado.iniciar(jogador);
                estado.setCenaAtualId(Estado.CENA_ENCERRAR);
                estado.concluir(finalEscolhido.getCodigo());
                Path arquivo = pasta.resolve(identidade + "-" + finalEscolhido + ".json");
                GerenciadorSave.salvar(estado, arquivo);
                Estado salvo = GerenciadorSave.carregar(arquivo);
                Protagonista restaurado = salvo.getProtagonista();
                assertTrue(salvo.isPartidaConcluida());
                assertEquals(finalEscolhido, Final.identificar(restaurado.getHistoricoFlags()).orElseThrow());
                assertEquals(Epilogos.de(jogador), Epilogos.de(restaurado));
                assertEquals(PerfilProtagonista.de(jogador), PerfilProtagonista.de(restaurado));
                String chave = VisuaisDeFinal.ilustracaoDe(restaurado);
                assertEquals(VisuaisDeFinal.ilustracaoDe(finalEscolhido), chave);
                assertArrayEquals(Ilustracoes.linhas(chave, PerfilProtagonista.de(jogador)),
                        Ilustracoes.linhas(chave, PerfilProtagonista.de(restaurado)));
            }
        }
    }
}
