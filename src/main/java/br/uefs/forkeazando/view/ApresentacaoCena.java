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
