package br.uefs.forkeazando.view.javafx;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.Estado;
import br.uefs.forkeazando.model.Protagonista;
import br.uefs.forkeazando.roteiro.Roteiro;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SessaoGrafica {
    private final Map<Integer, Cena> cenas;
    private Estado estado;

    public SessaoGrafica() {
        this.cenas = Roteiro.todasAsCenas().stream().collect(Collectors.toMap(Cena::getId, Function.identity()));
    }

    public void novaPartida(Protagonista protagonista) {
        Estado novo = new Estado();
        novo.iniciar(protagonista);
        novo.setCenaAtualId(700);
        estado = novo;
    }

    public void carregarPartida(Estado estadoCarregado){
        Objects.requireNonNull(estadoCarregado, "O estado carregado não pode está nulo");
        List<String> problemas = estadoCarregado.verificarIntegridade();

        if (!problemas.isEmpty()) {
            throw new IllegalArgumentException(
                    String.join("; ", problemas)
            );
        }

        if (!cenas.containsKey(estadoCarregado.getCenaAtualId())) {
            throw new IllegalArgumentException("Cena inexistente no roteiro");
        }

        this.estado = estadoCarregado;
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
            // Evita iniciar o próximo capítulo sem a lógica completa de CenasController
            return false;
        }
        if (!cenas.containsKey(destino)) {
            throw new IllegalStateException("Cena de destino não encontrada: " + destino);
        }
        atual.aplicarEscolha(escolha); // Reutiliza consequências e flags do modelo terminal
        atual.setCenaAtualId(destino);
        return true;
    }
}
