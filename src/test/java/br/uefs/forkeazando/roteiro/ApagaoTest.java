package br.uefs.forkeazando.roteiro;
import br.uefs.forkeazando.model.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ApagaoTest {

    private static Protagonista protagonista(){
        return new Protagonista("Teste", false, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
    }

    private static Cena cena(int id){
        return Roteiro.todasAsCenas().stream().filter(c -> c.getId() == id).findFirst().orElseThrow();
    }

    private static void definirVidaSocial(Protagonista p, int valor){
        p.alterarAtributo("vidaSocial", valor - p.getVidaSocial());
        assertEquals(valor, p.getVidaSocial());
    }

    private static List<Cena> cenasDosCapitulos() {
        return Roteiro.cenasDosCapitulos();
    }

    @Test
    void sao3CenasDentroDaFaixaReservadaEsemColisaoComOsCapitulos(){
        List<Cena> apagao = Roteiro.todasAsCenas()
                .stream()
                .filter(c-> Estado.ehCenaDeApagao(c.getId()))
                .toList();
        assertEquals(3, apagao.size());

        Set<Integer> usados = new HashSet<>();
        for (Cena c : cenasDosCapitulos())
            usados.add(c.getId());
        for (Cena c: apagao){
            assertTrue(Estado.ehCenaDeApagao(c.getId()), "fora da faixa: " + c.getId());
            assertFalse(usados.contains(c.getId()), "colide com cena existente: " + c.getId());
        }
    }



    @Test
    void naCena970SempreHaPeloMenosUmaSaidaQualquerQueSejaAVidaSocial() {
        for (int vida = 0; vida <= 10; vida++) {
            Protagonista p = protagonista();
            definirVidaSocial(p, vida);
            assertFalse(cena(Estado.CENA_APAGAO).getEscolhasDisponiveis(p).isEmpty(), "cena 970 sem saída com vida social " + vida);}
    }

    @Test
    void ajudaDeUmaPessoaSoApareceQuandoElaEhAliada() {
        Protagonista p = protagonista();
        definirVidaSocial(p, 0);
        Cena c970 = cena(Estado.CENA_APAGAO);
        long antes = c970.getEscolhasDisponiveis(p).stream().filter(e -> e.getTextoAlternativa().startsWith("Caio")).count();
        p.alterarRelacionamento(PersonagemSecundario.VETERANO, 3f);
        long depois = c970.getEscolhasDisponiveis(p).stream().filter(e -> e.getTextoAlternativa().startsWith("Caio")).count();
        assertEquals(0, antes);
        assertEquals(1, depois);
    }

    @Test
    void asEscolhasDa970LevamACenaComApoioOuSozinha() {
        for (Escolha e : cena(Estado.CENA_APAGAO).getEscolhas()) {
            assertTrue(e.getCenaDestinoId() == Estado.CENA_APAGAO + 1 || e.getCenaDestinoId() == Estado.CENA_APAGAO + 2,"destino inesperado: " + e.getCenaDestinoId());
        }
    }

    @Test
    void asCenasDeSaidaRetomamOCaminhoOriginal() {
        for (int id : new int[]{Estado.CENA_APAGAO + 1, Estado.CENA_APAGAO + 2}) {
            for (Escolha e : cena(id).getEscolhas()) {
                assertEquals(Estado.CENA_RETOMAR, e.getCenaDestinoId());
            }
        }
    }

    @Test
    void comApoioNaoCustaNadaESozinhaCusta() {
        Escolha comApoio = cena(Estado.CENA_APAGAO + 1).getEscolhas().get(0);
        Escolha sozinha = cena(Estado.CENA_APAGAO + 2).getEscolhas().get(0);
        assertTrue(comApoio.getFlagsConcedidas().contains("apagao_com_apoio"));
        assertEquals(0, comApoio.getScoreGanho());
        assertEquals(1, comApoio.getAtributosAlterados().get("sanidade"));
        assertTrue(sozinha.getFlagsConcedidas().contains("apagao_sozinha"));
        assertEquals(-3, sozinha.getScoreGanho());
        assertEquals(-2, sozinha.getParticipacaoGanha());
    }

}
