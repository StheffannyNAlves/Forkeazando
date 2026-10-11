package br.uefs.forkeazando.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EscolhaTest {

    // Valores iniciais deste protagonista (confira com o ProtagonistaTest):
    // score 0, participação 0, confiança 3, vida social 3, carisma 5,
    // lógica 7, sanidade 5, energia 60.
    private static Protagonista protagonista() {
        return new Protagonista(
                "Teste",
                false, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        );
    }

    private static Escolha.Builder escolha() {
        return new Escolha.Builder("Teste", 1);
    }


    @Test
    void estaDisponivelQuandoTodosRequisitosSaoCumpridos() {
        Protagonista p = protagonista();
        p.ganharScore(10);
        p.ganharParticipacao(10);

        Escolha e = escolha()
                .comScoreMinimo(8)
                .comParticipacaoMinima(8)
                .comCustoEnergia(10)
                .build();

        assertTrue(e.estaDisponivel(p));
    }

    @Test
    void scoreNoMinimoExatoEstaDisponivel() {
        Protagonista p = protagonista();
        p.ganharScore(8);

        assertTrue(escolha().comScoreMinimo(8).build().estaDisponivel(p));
    }

    @Test
    void scoreUmAbaixoDoMinimoNaoEstaDisponivel() {
        Protagonista p = protagonista();
        p.ganharScore(7);

        assertFalse(escolha().comScoreMinimo(8).build().estaDisponivel(p));
    }

    @Test
    void participacaoInsuficienteNaoEstaDisponivel() {
        Protagonista p = protagonista();
        p.ganharScore(10);
        p.ganharParticipacao(5);

        Escolha e = escolha().comScoreMinimo(8).comParticipacaoMinima(8).build();

        assertFalse(e.estaDisponivel(p));
    }

    @Test
    void participacaoNoMaximoExatoEstaDisponivel() {
        Protagonista p = protagonista();
        p.ganharParticipacao(3);

        assertTrue(escolha().comParticipacaoMaxima(3).build().estaDisponivel(p));
    }

    @Test
    void participacaoAcimaDoMaximoNaoEstaDisponivel() {
        Protagonista p = protagonista();
        p.ganharParticipacao(4);

        assertFalse(escolha().comParticipacaoMaxima(3).build().estaDisponivel(p));
    }


    @Test
    void flagRequeridaAusenteNaoEstaDisponivel() {
        Protagonista p = protagonista();

        assertFalse(escolha().comFlagRequerida("flag_inexistente").build().estaDisponivel(p));
    }

    @Test
    void flagRequeridaPresenteEstaDisponivel() {
        Protagonista p = protagonista();
        p.adicionarFlag("flag_teste");

        assertTrue(escolha().comFlagRequerida("flag_teste").build().estaDisponivel(p));
    }


    @Test
    void energiaInsuficienteNaoEstaDisponivel() {
        Protagonista p = protagonista();

        assertFalse(escolha().comCustoEnergia(9999).build().estaDisponivel(p));
    }

    @Test
    void custoIgualAEnergiaEstaDisponivel() {
        Protagonista p = protagonista();   // energia 60

        assertTrue(escolha().comCustoEnergia(60).build().estaDisponivel(p));
    }

    @Test
    void custoUmAcimaDaEnergiaNaoEstaDisponivel() {
        Protagonista p = protagonista();

        assertFalse(escolha().comCustoEnergia(61).build().estaDisponivel(p));
    }

    @Test
    void custoNegativoRecuperaEnergiaEEstaSempreDisponivel() {
        Protagonista p = protagonista();
        p.gastarEnergia(60);               // energia 0

        assertTrue(escolha().comCustoEnergia(-30).build().estaDisponivel(p));
    }

    @Test
    void energiaAbaixoDeExigeEnergiaEstritamenteMenor() {
        Protagonista p = protagonista();
        p.gastarEnergia(45);               // energia 15

        assertTrue(escolha().comEnergiaAbaixoDe(20).build().estaDisponivel(p));
    }

    @Test
    void energiaIgualAoLimiteDeAbaixoDeNaoEstaDisponivel() {
        Protagonista p = protagonista();
        p.gastarEnergia(40);               // energia 20

        assertFalse(escolha().comEnergiaAbaixoDe(20).build().estaDisponivel(p));
    }


    @Test
    void sanidadeAbaixoDoMinimoNaoEstaDisponivel() {
        Protagonista p = protagonista();   // sanidade 5

        assertFalse(escolha().comSanidadeMinima(6).build().estaDisponivel(p));
    }

    @Test
    void sanidadeNoMinimoExatoEstaDisponivel() {
        Protagonista p = protagonista();
        p.alterarSanidade(1);              // sanidade 6

        assertTrue(escolha().comSanidadeMinima(6).build().estaDisponivel(p));
    }

    @Test
    void confiancaAcimaDoMaximoNaoEstaDisponivel() {
        Protagonista p = protagonista();   // confiança 3

        assertFalse(escolha().comConfiancaMaxima(2).build().estaDisponivel(p));
    }

    @Test
    void confiancaNoMaximoExatoEstaDisponivel() {
        Protagonista p = protagonista();
        p.alterarConfianca(-1);            // confiança 2

        assertTrue(escolha().comConfiancaMaxima(2).build().estaDisponivel(p));
    }

    @Test
    void maximoZeroSignificaSemLimite() {
        Protagonista p = protagonista();   // confiança 3

        // limitação conhecida: 0 é o valor "não definido", então
        // "no máximo 0" não consegue ser expresso
        assertTrue(escolha().comConfiancaMaxima(0).build().estaDisponivel(p));
    }


    @Test
    void atributosAlteradosSeAcumulamNoMesmoNome() {
        Escolha e = escolha()
                .comAtributo("sanidade", 2)
                .comAtributo("sanidade", 3)
                .build();

        assertEquals(5, e.getAtributosAlterados().get("sanidade"));
    }

    @Test
    void impactoDeRelacionamentoFicaNoPersonagemCerto() {
        Escolha e = escolha()
                .comRelacionamento(PersonagemSecundario.BIA, 2f)
                .build();

        float impacto = e.getImpactosRelacionamento().get(PersonagemSecundario.BIA);

        assertEquals(2f, impacto, 0.001f);
        assertNull(e.getImpactosRelacionamento().get(PersonagemSecundario.VETERANO));
    }

    @Test
    void escolhaPodeConcederMaisDeUmaFlag() {
        Escolha e = escolha()
                .comFlagConcedida("final_ic")
                .comFlagConcedida("item_pdfs_ic")
                .build();

        assertEquals(List.of("final_ic", "item_pdfs_ic"), e.getFlagsConcedidas());
    }

    @Test
    void escolhaSemFlagNaoConcedeNada() {
        assertTrue(escolha().build().getFlagsConcedidas().isEmpty());
    }

    @Test
    void listaDeFlagsConcedidasNaoPodeSerAlterada() {
        Escolha e = escolha().comFlagConcedida("a").build();
        List<String> flags = e.getFlagsConcedidas();

        assertThrows(UnsupportedOperationException.class, () -> flags.add("b"));
    }
}