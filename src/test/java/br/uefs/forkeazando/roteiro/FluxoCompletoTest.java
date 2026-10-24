package br.uefs.forkeazando.roteiro;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FluxoCompletoTest {


    private static Map<Integer, Cena> jogo() {
        Map<Integer, Cena> mapa = new HashMap<>();
        for (Cena c : Roteiro.todasAsCenas()) mapa.put(c.getId(), c);
        return mapa;
    }

    private static Protagonista novoProtagonista() {
        return new Protagonista("Teste", false, false, false,
                Protagonista.NivelVidaSocial.EQUILIBRADO,
                Protagonista.ExperienciaEmocionalEnsinoMedio.NEUTRA,
                Protagonista.SituacaoEconomica.ESTAVEL);
    }


    private static int executar(Map<Integer, Cena> jogo, Protagonista p,
                                int cenaAtual, String trecho) {
        Cena cena = jogo.get(cenaAtual);
        assertNotNull(cena, "cena " + cenaAtual + " não existe no roteiro");
        if (ResultadoCalculo.ehCenaDeResultado(cenaAtual)
                || cenaAtual == 212) {
            ResultadoCalculo.assegurarNota(p, cenaAtual);
        }
        Escolha escolhida = null;
        for (Escolha e : cena.getEscolhasDisponiveis(p)) {
            if (e.getTextoAlternativa().toLowerCase().contains(trecho.toLowerCase())) {
                escolhida = e;
                break;
            }
        }
        assertNotNull(escolhida,
                "cena " + cenaAtual + ": nenhuma escolha contém '" + trecho + "'. "
                        + "Disponíveis: " + cena.getEscolhasDisponiveis(p).stream()
                        .map(Escolha::getTextoAlternativa).toList());

        // aplica efeitos (o mesmo que CenasController.aplicarEscolha faz, sem view)
        for (String f : escolhida.getFlagsConcedidas()) p.adicionarFlag(f);
        for (var entry : escolhida.getImpactosRelacionamento().entrySet()) {
            p.alterarRelacionamento(entry.getKey(), entry.getValue());
        }
        for (var entry : escolhida.getAtributosAlterados().entrySet()) {
            p.alterarAtributo(entry.getKey(), entry.getValue());
        }
        p.ganharScore(escolhida.getScoreGanho());
        p.ganharParticipacao(escolhida.getParticipacaoGanha());
        p.gastarEnergia(escolhida.getCustoEnergia());

        return escolhida.getCenaDestinoId();
    }

    /**
     * Recupera energia máxima, simula a pausa entre capítulos.
     */
    private static void descansar(Protagonista p) {
        p.recuperarEnergia(p.getEnergiaMaxima());
    }


    @Test
    @DisplayName("Partida IC: 700 → 660 sem becos sem saída")
    void partidaIcCompleta() {
        Map<Integer, Cena> jogo = jogo();
        Protagonista p = novoProtagonista();
        int cena = 700;

        // Prólogo
        cena = executar(jogo, p, cena, "Porque eu escolhi");
        assertEquals(701, cena);
        cena = executar(jogo, p, cena, "Voltar pro começo");
        assertEquals(0, cena);

        cena = executar(jogo, p, cena, "Acordar ela");
        cena = executar(jogo, p, cena, "Ler o edital");
        cena = executar(jogo, p, cena, "Puxar conversa");
        cena = executar(jogo, p, cena, "Falar com ele");
        cena = executar(jogo, p, cena, "Tá aqui há quanto tempo");
        cena = executar(jogo, p, cena, "Pergunto se ele quer ajuda");
        cena = executar(jogo, p, cena, "Ler o datasheet");
        if (jogo.containsKey(16)) {
            cena = executar(jogo, p, cena, "Assumir a coordenação");
            descansar(p);
        }
        cena = executar(jogo, p, cena, "Assumir a Coordenação");
        cena = executar(jogo, p, cena, "Voltar pra oficina");
        cena = executar(jogo, p, cena, "Medir a tensão");
        cena = executar(jogo, p, cena, "Encerrar");
        assertEquals(Estado.CENA_ENCERRAR, cena, "fim do Cap 1");

        descansar(p);
        cena = Roteador.proximaCena(2, p);
        assertEquals(100, cena);
        cena = executar(jogo, p, cena, "Abrir os onze");
        cena = executar(jogo, p, cena, "Ir no CA");
        cena = executar(jogo, p, cena, "Assinar a lista");
        cena = executar(jogo, p, cena, "Aguentar");
        cena = executar(jogo, p, cena, "Perguntar como ele saiu");
        cena = executar(jogo, p, cena, "Perguntar o que ele cortou primeiro");
        cena = executar(jogo, p, cena, "Se você ainda tá aqui");
        assertEquals(118, cena, "cena 117 deve avançar para 118");
        cena = executar(jogo, p, cena, "Propor estudar juntas");
        assertEquals(115, cena, "cena 118 deve avançar para 115");
        cena = executar(jogo, p, cena, "Encerrar Capítulo 2");
        assertEquals(Estado.CENA_ENCERRAR, cena, "fim do Cap 2");

        descansar(p);
        cena = Roteador.proximaCena(3, p);
        assertEquals(200, cena);
        cena = executar(jogo, p, cena, "Só respirar");
        cena = executar(jogo, p, cena, "Respirar fundo");
        cena = executar(jogo, p, cena, "Anotar onde travou");
        cena = executar(jogo, p, cena, "Ela mereceu");

        cena = executar(jogo, p, cena, "Não se inscrever");
        assertEquals(214, cena);
        cena = executar(jogo, p, cena, "Aceitar");
        assertEquals(212, cena);

        if (p.temFlag("cap3_nota_insuficiente")) {
            cena = executar(jogo, p, cena, "A média não veio");
            cena = executar(jogo, p, cena, "Levar a prova à monitoria");
        } else if (p.temFlag("cap3_nota_suficiente_honesta")) {
            cena = executar(jogo, p, cena, "Passei, mas preciso revisar");
            cena = executar(jogo, p, cena, "Refazer as questões");
        } else if (p.temFlag("cap3_nota_alta_honesta")) {
            cena = executar(jogo, p, cena, "Fui bem");
            cena = executar(jogo, p, cena, "Anotar o que ainda errei");
        } else {
            fail("A prova não registrou uma classificação honesta esperada.");
        }
        assertEquals(218, cena, "a reflexão deve chegar à cena 218");
        cena = executar(jogo, p, cena, "Encerrar Capítulo 3");
        assertEquals(Estado.CENA_ENCERRAR, cena, "fim do Cap 3");

        descansar(p);
        cena = Roteador.proximaCena(4, p);
        assertEquals(300, cena);
        cena = executar(jogo, p, cena, "Ler os dois editais");  // 300 → 301
        cena = executar(jogo, p, cena, "Qual ele escolheria");  // 301 → 3011
        cena = executar(jogo, p, cena, "Seguir");               // 3011 → 302
        cena = executar(jogo, p, cena, "Insistir");             // 302 → 303
        cena = executar(jogo, p, cena, "quero aprender a pesquisar"); // 303 → 304
        cena = executar(jogo, p, cena, "Não sei. Mas quero tentar.");  // 304 → 3045
        cena = executar(jogo, p, cena, "Começar");            // 3045
        cena = executar(jogo, p, cena, "Estudar primeiro");    // 3046
        cena = executar(jogo, p, cena, "Sozinha");             // 3047
        cena = executar(jogo, p, cena, "Sim");                 // 3048
        cena = executar(jogo, p, cena, "Saber");               // 3049
        assertEquals(3050, cena, "deve chegar no veredito");


        cena = executar(jogo, p, cena, "Discordar"); // 3050 -> 3052
        cena = executar(jogo, p, cena, "Seguir");    // 3052 -> 305
        assertEquals(305, cena, "deve chegar no prazo duplo");

        cena = executar(jogo, p, cena, "PIBIC");
        cena = executar(jogo, p, cena, "Encerrar Capítulo 4");
        assertEquals(Estado.CENA_ENCERRAR, cena, "fim do Cap 4");

        descansar(p);
        cena = Roteador.proximaCena(5, p);
        assertEquals(400, cena, "trilha_ic deveria entrar em 400");

        cena = executar(jogo, p, cena, "Falar meu nome");            // 400 -> 402
        cena = executar(jogo, p, cena, "Perguntar o que ela está fazendo"); // 402 -> 403
        cena = executar(jogo, p, cena, "Dizer que entendo");         // 403 -> 401
        cena = executar(jogo, p, cena, "Perguntar pra Bia");         // 401 -> 405
        cena = executar(jogo, p, cena, "Assumir a coordenação");     // 405 -> 451
        assertEquals(451, cena, "405 deve ir para a cena da greve");

        cena = executar(jogo, p, cena, "Ir na assembleia");          // 451 -> 455
        cena = executar(jogo, p, cena, "Oferecer pra dividir");      // 455 -> 460
        cena = executar(jogo, p, cena, "Voltar pro laboratório");    // 460 -> 410
        assertEquals(410, cena, "460 deve voltar para a IC");

        cena = executar(jogo, p, cena, "Aguentar");                  // 410 -> 415
        cena = executar(jogo, p, cena, "Falar do projeto");          // 415 -> 416
        cena = executar(jogo, p, cena, "Esperar a resposta");        // 416 -> 417
        assertEquals(417, cena, "o percurso escolhido deve chegar à aprovação da bolsa");

        cena = executar(jogo, p, cena, "Encerrar Capítulo 5A");
        assertEquals(Estado.CENA_ENCERRAR, cena, "fim do percurso da IC no Cap 5");

        descansar(p);
        cena = Roteador.proximaCena(6, p);
        assertEquals(540, cena, "o Capítulo 6 começa na cena de entrada da trilha");

        cena = executar(jogo, p, cena, "Seguir");                    // 540 -> 541
        assertEquals(541, cena, "a trilha IC deve passar pela cena 541");

        cena = executar(jogo, p, cena, "Pedir prorrogação");         // 541 -> 550
        assertEquals(550, cena, "a cena 541 deve conduzir à cena 550");

        cena = executar(jogo, p, cena, "Malu manda mensagem");       // 550 -> 555
        cena = executar(jogo, p, cena, "Perguntar como ela saiu");   // 555 -> 556
        cena = executar(jogo, p, cena, "Ficar com ela");             // 556 -> 554
        cena = executar(jogo, p, cena, "Continuar, mas reorganizar"); // 554 -> 557
        cena = executar(jogo, p, cena, "Deixar ele contar");         // 557 -> 558
        cena = executar(jogo, p, cena, "Por que ele voltou");        // 558 -> 559
        cena = executar(jogo, p, cena, "Fui eu");                    // 559 -> 560
        cena = executar(jogo, p, cena, "Continuar");                // 560 -> 567
        cena = executar(jogo, p, cena, "Voltar para a IC");         // 567 -> 561
        cena = executar(jogo, p, cena, "Aceitar");                  // 561 -> 564
        cena = executar(jogo, p, cena, "Encerrar Capítulo 6");

        assertEquals(Estado.CENA_ENCERRAR, cena, "fim do Cap 6");

        descansar(p);
        cena = Roteador.proximaCena(7, p);
        assertEquals(600, cena);

        cena = executar(jogo, p, cena, "Uma página por dia");       // 600 -> 599
        cena = executar(jogo, p, cena, "Continuar");                // 599 -> 601
        cena = executar(jogo, p, cena, "Perguntar, com educação");   // 601 -> 6011
        cena = executar(jogo, p, cena, "Sair da sala");             // 6011 -> 605
        cena = executar(jogo, p, cena, "Encerrar Capítulo 7");

        assertEquals(Estado.CENA_ENCERRAR, cena, "fim do Cap 7");

        descansar(p);
        cena = Roteador.proximaCena(8, p);
        assertEquals(649, cena);

        cena = executar(jogo, p, cena, "Aceitar o cartão");         // 649 -> 650
        cena = executar(jogo, p, cena, "Procurar Caio");            // 650 -> 651
        cena = executar(jogo, p, cena, "Aceitar");                  // 651 -> 653
        cena = executar(jogo, p, cena, "Ir tirar foto");            // 653 -> 654
        cena = executar(jogo, p, cena, "Eu questionei a ordem dos autores"); // 654 -> 655
        cena = executar(jogo, p, cena, "Decidir o próximo passo");  // 655 -> 661
        cena = executar(jogo, p, cena, "Continuar. Ver o que a sua trajetória abriu"); // 661 -> 667

        assertEquals(
                ResolucaoDeFuturo.CENA_RESOLVER_FUTURO,
                cena,
                "a cena 661 deve conduzir à resolução automática do futuro"
        );

        // O controlador resolve a cena 667 por Java, sem escolha JSON.
        cena = ResolucaoDeFuturo.resolverCena(p);
        assertEquals(662, cena, "a trajetória desta partida IC deve levar ao final de pesquisa");

        cena = executar(jogo, p, cena, "Levar esse caminho");       // 662 -> 660
        assertEquals(660, cena, "o final deve seguir para a cena de encerramento");

        cena = executar(jogo, p, cena, "Encerrar o jogo");
        assertEquals(Estado.CENA_ENCERRAR, cena, "fim do Cap 8");

        assertTrue(p.temFlag("trilha_ic"),
                "trilha_ic deveria ter sido definida no Cap 4");
        assertTrue(p.temFlag("bolsista_ic") || p.temFlag("ic_voluntaria_de_novo"),
                "fim do Cap 5 deveria registrar bolsista ou voluntária");
        assertTrue(p.temFlag("cap5_concluido"),
                "cap5_concluido deveria estar setada");
        assertTrue(p.temFlag("caio_revelacao"),
                "caio_revelacao deveria vir da escolha 'Fui eu'");
        assertTrue(p.temFlag("cap6_continuou"),
                "cap6_continuou deveria vir da escolha de reorganizar");
        assertTrue(p.temFlag("futuro_pesquisa_brasil"),
                "futuro_pesquisa_brasil deveria ser definido pelo resolvedor");
        assertTrue(p.temFlag("cap7_questionou_autoria"),
                "a partida deveria registrar o questionamento da autoria");
        assertTrue(p.temFlag("cap8_resposta_autoria"),
                "a resposta final deveria retomar o questionamento da autoria");
        assertTrue(p.temFlag("hab_sobreviveu_greve"),
                "hab_sobreviveu_greve deveria vir da cena 455");
        assertTrue(p.temFlag("cap5b_dividiu_comida"),
                "cap5b_dividiu_comida deveria vir de 'Oferecer pra dividir'");
    }
}