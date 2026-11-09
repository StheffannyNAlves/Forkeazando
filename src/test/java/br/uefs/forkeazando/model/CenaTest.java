package br.uefs.forkeazando.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CenaTest {

    private static Protagonista protagonista() {
        return new Protagonista(
                "Teste",
                false, true, true,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL
        );
    }

    private static Cena cena() {
        return new Cena(0, "Capítulo 1", "Narrador", "teste teste");
    }

    private static  Protagonista comEnergia(int energia){
        Protagonista p = protagonista();
        p.gastarEnergia(p.getEnergiaMaxima() - energia);
        assertEquals(energia, p.getEnergia());
        return p;
    }

    @Test
    void guardaOQueFoiPassadoNoConstrutor() {
        Cena cena = new Cena(42, "Capítulo 3", "Dandara Oliveira", "texto da cena");

        assertEquals(42, cena.getId());
        assertEquals("Capítulo 3", cena.getCapitulo());
        assertEquals("Dandara Oliveira", cena.getPersonagemFalando());
        assertEquals("texto da cena", cena.getTextoNarrativo());
    }

    @Test
    void cenaSemEscolhasRetornaListaVazia() {
        assertTrue(cena().getEscolhasDisponiveis(protagonista()).isEmpty());
    }

    @Test
    void filtraEscolhaComRequisitoNaoCumprido() {
        Cena cena = cena();
        cena.adicionarEscolha(new Escolha.Builder("opção livre", 1).build());
        cena.adicionarEscolha(new Escolha.Builder("opção bloqueada", 2).comScoreMinimo(10).build());

        List<Escolha> disponiveis = cena.getEscolhasDisponiveis(protagonista());

        assertEquals(1, disponiveis.size());
        assertEquals("opção livre", disponiveis.get(0).getTextoAlternativa());
    }

    @Test
    void escolhaBloqueadaApareceQuandoORequisitoPassaASerCumprido() {
        Cena cena = cena();
        cena.adicionarEscolha(new Escolha.Builder("opção bloqueada", 2).comScoreMinimo(10).build());

        Protagonista p = protagonista();
        assertTrue(cena.getEscolhasDisponiveis(p).isEmpty());

        p.ganharScore(10);

        assertEquals(1, cena.getEscolhasDisponiveis(p).size());
    }

    @Test
    void todasDisponiveisQuandoNenhumaTemRequisito() {
        Cena cena = cena();
        cena.adicionarEscolha(new Escolha.Builder("a", 1).build());
        cena.adicionarEscolha(new Escolha.Builder("b", 2).build());
        cena.adicionarEscolha(new Escolha.Builder("c", 3).build());

        assertEquals(3, cena.getEscolhasDisponiveis(protagonista()).size());
    }

    @Test
    void nenhumaDisponivelQuandoTodasEstaoBloqueadas() {
        Cena cena = cena();
        cena.adicionarEscolha(new Escolha.Builder("a", 1).comScoreMinimo(50).build());
        cena.adicionarEscolha(new Escolha.Builder("b", 2).comFlagRequerida("flag_inexistente").build());

        // é este caso que faz o jogo mostrar "Fim do Jogo" no CenasController
        assertTrue(cena.getEscolhasDisponiveis(protagonista()).isEmpty());
    }

    @Test
    void mantemAOrdemEmQueAsEscolhasForamAdicionadas() {
        Cena cena = cena();
        cena.adicionarEscolha(new Escolha.Builder("primeira", 1).build());
        cena.adicionarEscolha(new Escolha.Builder("bloqueada", 2).comScoreMinimo(99).build());
        cena.adicionarEscolha(new Escolha.Builder("terceira", 3).build());

        List<Escolha> disponiveis = cena.getEscolhasDisponiveis(protagonista());

        assertEquals("primeira", disponiveis.get(0).getTextoAlternativa());
        assertEquals("terceira", disponiveis.get(1).getTextoAlternativa());
    }

    @Test
    void recalculaAListaACadaChamada() {
        Cena cena = cena();
        cena.adicionarEscolha(new Escolha.Builder("a", 1).build());
        Protagonista p = protagonista();

        assertEquals(1, cena.getEscolhasDisponiveis(p).size());

        cena.adicionarEscolha(new Escolha.Builder("b", 2).build());

        assertEquals(2, cena.getEscolhasDisponiveis(p).size());
    }

    @Test
    void listaDevolvidaNaoPodeSerAlterada() {
        Cena cena = cena();
        cena.adicionarEscolha(new Escolha.Builder("a", 1).build());
        List<Escolha> disponiveis = cena.getEscolhasDisponiveis(protagonista());

        assertThrows(UnsupportedOperationException.class,
                () -> disponiveis.add(new Escolha.Builder("intrusa", 9).build()));
    }

    @Test
    void getEscolhasDevolveTodasInclusiveAsBloqueadas() {
        Cena cena = cena();
        cena.adicionarEscolha(new Escolha.Builder("livre", 1).build());
        cena.adicionarEscolha(new Escolha.Builder("bloqueada", 2).comScoreMinimo(99).build());

        assertEquals(2, cena.getEscolhas().size());
    }


    @Test
    void identificaEscolhaBloqueadaSomentePorEnergia() { // garante que o apagao seja reconhecido quando falta energia
        Cena cena = cena();
        Protagonista p = protagonista();

        cena.adicionarEscolha(
                new Escolha.Builder("opção cara", 1)
                        .comCustoEnergia(p.getEnergia() + 1)
                        .build()
        );

        assertTrue(cena.getEscolhasDisponiveis(p).isEmpty());
        assertTrue(cena.temEscolhaBloqueadaSoPorEnergia(p));
    }

    @Test
    void naoIdentificaEscolhaBloqueadaPorOutroRequisito() { // garante que o jogo nao confunda falta de score com falta de energia
        Cena cena = cena();
        Protagonista p = protagonista();

        cena.adicionarEscolha(
                new Escolha.Builder("opção bloqueada", 1)
                        .comScoreMinimo(10)
                        .build()
        );

        assertTrue(cena.getEscolhasDisponiveis(p).isEmpty());
        assertFalse(cena.temEscolhaBloqueadaSoPorEnergia(p));
    }


    @Test
    void reconheceQuandoTodasAsEscolhasSoFicamBloqueadasPorEnergia(){
        Cena cena = cena();
        cena.adicionarEscolha(new Escolha.Builder("só olhar e ir embora", 1).comCustoEnergia(5).build());
        cena.adicionarEscolha(new Escolha.Builder("perguntar", 2).comCustoEnergia(10).build());

        Protagonista p = comEnergia(2);
        assertTrue(cena.getEscolhasDisponiveis(p).isEmpty());
        assertTrue(cena.temEscolhaBloqueadaSoPorEnergia(p));

    }

    @Test
    void bloqueioPorOutroRequisitoNaoContaComoFaltaDeEnergia() {
        Cena cena = cena();
        cena.adicionarEscolha(new Escolha.Builder("exige score", 1).comScoreMinimo(50).build());
        cena.adicionarEscolha(new Escolha.Builder("exige flag", 2).comFlagRequerida("inexistente").build());
        assertTrue(cena.getEscolhasDisponiveis(comEnergia(0)).isEmpty());
        assertFalse(cena.temEscolhaBloqueadaSoPorEnergia(comEnergia(0)));
    }

    @Test
    void escolhaQueAceitaEnergiaInsuficienteNaoEstaBloqueada() {Cena cena = cena();
        cena.adicionarEscolha(new Escolha.Builder("seguir no limite", 1).comCustoEnergia(5).comPermiteEnergiaInsuficiente().build());
        Protagonista p = comEnergia(0);
        assertEquals(1, cena.getEscolhasDisponiveis(p).size());
        assertFalse(cena.temEscolhaBloqueadaSoPorEnergia(p));
    }

    @Test
    void comEnergiaSuficienteNadaEstaBloqueadoPorEnergia() {
        Cena cena = cena();
        cena.adicionarEscolha(new Escolha.Builder("custa 5", 1).comCustoEnergia(5).build());
        assertFalse(cena.temEscolhaBloqueadaSoPorEnergia(comEnergia(5)));
    }

}