package br.uefs.forkeazando.view;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Conquista;
import br.uefs.forkeazando.model.PersonagemSecundario;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.roteiro.ApresentacaoDeCena;
/**
 * Define o contrato de apresentação utilizado pelo CenasController
 * Aplica o Princípio da Inversão de Dependência (DIP), do SOLID,
 * permitindo que o controlador dependa de uma abstração,
 * sem conhecer os detalhes das implementações de apresentação
 * (terminal ou JavaFX). CenasController deixa de depender de CenaView, e passa a depender
 * de ApresentacaoCena
 *
 * <p>Também é a aplicação do padrão GoF <b>Strategy</b>: a forma de apresentar a
 * partida é um comportamento intercambiável escolhido na construção do contexto.
 * <ul>
 *   <li>Strategy: esta interface;</li>
 *   <li>Context: {@code CenasController}, que guarda a referência e delega toda
 *       apresentação a ela, sem saber qual implementação está em uso;</li>
 *   <li>ConcreteStrategy: {@code CenaView} (terminal) e {@code ControllerInterface} (JavaFX).</li>
 * </ul>
 * O mesmo núcleo de regras roda com qualquer uma das duas, sem duplicar consequências.
 */
public interface ApresentacaoCena {
    void resetarCacheVisual();

    void mostrarGaleria(Protagonista protagonista);

    void renderizar(Cena cena, Protagonista p, int semestresExtras, ApresentacaoDeCena visual);

    void mostrarBencaoCaramelo();

    void mostrarHabilidade(String flag);

    void mostrarItem(String flag);

    void mostrarConquista(Conquista conquista);

    void mostrarMensagem(String mensagem);

    void mostrarImpactoRelacionamento(PersonagemSecundario npc, float valor, Protagonista.NivelRelacionamento nivel);

    void mostrarEpilogos(Protagonista p);

    void mostrarCurriculo(Protagonista p);

    void mostrarTransicao(String capitulo);

    void mostrarChuteDoSistema(Protagonista p);
}
