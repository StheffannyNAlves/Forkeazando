package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.roteiro.Roteador;
import br.uefs.forkeazando.roteiro.Roteiro;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Adapta o modelo e o roteiro existentes para as primeiras cenas no JavaFX.
 * Não substitui CenasController (versão completa de terminal): progressão,
 * autosave, conquistas e eventos especiais serão migrados separadamente.
 */
public final class SessaoGrafica {
    private final Map<Integer, Cena> cenas;
    private Estado estado;

    public SessaoGrafica() {
        this.cenas = Roteiro.todasAsCenas().stream()
                .collect(Collectors.toMap(Cena::getId, Function.identity()));
    }

    public void novaPartida(Protagonista protagonista) {
        Estado novo = new Estado();
        novo.iniciar(protagonista);
        novo.setCenaAtualId(700); // A mesma abertura usada no controller do terminal.
        estado = novo;
    }

    public boolean possuiPartida() {
        return estado != null;
    }

    public Estado estado() {
        if (estado == null) throw new IllegalStateException("Nenhuma partida em andamento.");
        return estado;
    }

    public Cena cenaAtual() {
        int id = estado().getCenaAtualId();
        Cena cena = cenas.get(id);
        if (cena == null) throw new IllegalStateException("Cena não encontrada: " + id);
        return cena;
    }

    public List<Escolha> escolhasDisponiveis() {
        return cenaAtual().getEscolhasDisponiveis(estado().getProtagonista());
    }

    /**
     * Prévia gráfica: avança pelo roteiro real até concluir o capítulo 1.
     * Não avança para os capítulos seguintes, que têm eventos especiais próprios.
     */
    public boolean escolher(Escolha escolha) {
        if (!escolhasDisponiveis().contains(escolha)) {
            throw new IllegalArgumentException("Escolha indisponível.");
        }
        Estado atual = estado();
        if (atual.getCapituloAtual() > 1) {
            throw new IllegalStateException("A prévia gráfica está limitada ao capítulo inicial.");
        }
        int destino = escolha.getCenaDestinoId();
        if (destino == Estado.CENA_RETOMAR) {
            throw new IllegalStateException("Evento de apagão ainda não disponível na interface gráfica.");
        }
        if (destino == Estado.CENA_ENCERRAR) {
            // Evita iniciar o próximo capítulo sem a lógica completa de CenasController.
            return false;
        }
        if (!cenas.containsKey(destino)) {
            throw new IllegalStateException("Cena de destino não encontrada: " + destino);
        }
        atual.aplicarEscolha(escolha); // Reutiliza consequências e flags do modelo real.
        atual.setCenaAtualId(destino);
        return true;
    }
}
