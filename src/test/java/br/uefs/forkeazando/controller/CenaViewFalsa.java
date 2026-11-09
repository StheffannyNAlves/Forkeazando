package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Conquista;
import br.uefs.forkeazando.model.PersonagemSecundario;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.roteiro.ApresentacaoDeCena;
import br.uefs.forkeazando.view.CenaView;

import java.util.ArrayList;
import java.util.List;

// CenaView que não escreve no terminal: só registra o que o controller mandou exibir
class CenaViewFalsa extends CenaView {

    final List<Integer> cenasRenderizadas = new ArrayList<>();
    final List<String> transicoes = new ArrayList<>();
    final List<String> mensagens = new ArrayList<>();
    final List<String> habilidades = new ArrayList<>();
    final List<String> itens = new ArrayList<>();
    final List<Conquista> conquistas = new ArrayList<>();
    final List<PersonagemSecundario> impactos = new ArrayList<>();
    int galeriasAbertas;
    int bencaos;

    CenaViewFalsa() {
        super(false, 0);
    }

    @Override
    public void resetarCacheVisual() { }

    @Override
    public void renderizar(Cena cena, Protagonista p, int semestresExtras, ApresentacaoDeCena visual) {
        cenasRenderizadas.add(cena.getId());
    }

    @Override
    public void mostrarTransicao(String capitulo) {
        transicoes.add(capitulo);
    }

    @Override
    public void mostrarMensagem(String mensagem) {
        mensagens.add(mensagem);
    }

    @Override
    public void mostrarGaleria(Protagonista protagonista) {
        galeriasAbertas++;
    }

    @Override
    public void mostrarBencaoCaramelo() {
        bencaos++;
    }

    @Override
    public void mostrarHabilidade(String flag) {
        habilidades.add(flag);
    }

    @Override
    public void mostrarItem(String flag) {
        itens.add(flag);
    }

    @Override
    public void mostrarConquista(Conquista conquista) {
        conquistas.add(conquista);
    }

    @Override
    public void mostrarImpactoRelacionamento(PersonagemSecundario npc, float valor,
                                             Protagonista.NivelRelacionamento nivel) {
        impactos.add(npc);
    }

    @Override
    public void mostrarChuteDoSistema(Protagonista p) { }

    @Override
    public void mostrarCurriculo(Protagonista p) { }

    @Override
    public void mostrarEpilogos(Protagonista p) { }

    boolean temMensagemContendo(String trecho) {
        return mensagens.stream().anyMatch(m -> m.contains(trecho));
    }
}
