package br.uefs.forkeazando.controller;

import br.uefs.forkeazando.excecao.DadosInvalidosException;
import br.uefs.forkeazando.excecao.SaveException;
import br.uefs.forkeazando.model.Configuracoes;
import br.uefs.forkeazando.persistencia.ConfiguracoesAdmin;
import br.uefs.forkeazando.view.Cores;
import br.uefs.forkeazando.view.Entrada;
import br.uefs.forkeazando.view.widget.MenuInterativo;
import br.uefs.forkeazando.view.widget.MenuInterativo.Opcao;
import br.uefs.forkeazando.view.widget.MenuInterativo.Resposta;

import java.util.ArrayList;
import java.util.List;
// Decide quando salva, quando descarta e quando pergunta
public class TelaConfiguracoesController {

    private final Configuracoes original;
    private Configuracoes buffer;

    public TelaConfiguracoesController(Configuracoes original){
        this.original = original;
        this.buffer = copiar(original);
    }

    private Configuracoes copiar(Configuracoes c){
        return new Configuracoes(c);
    }

    public void iniciar(){
        while (true){
            List<Opcao> opcoes = montarOpcoes();
            Resposta r = MenuInterativo.abrir(opcoes, "CONFIGURACOES");

            if (r.resultado == MenuInterativo.Resultado.CANCELADO)
                return;
            aplicarValoresNasOpcoes(r.valoresEscolhidos);
            String labelConfirmado = opcoes.get(r.indiceOpcao).label;
            if (labelConfirmado.equals("Salvar")){
                salvar();
            } else if (labelConfirmado.equals("Sair")){
                if (sair())
                    return;
            }
        }
    }

    private List<Opcao> montarOpcoes() {
        List<Opcao> opcoes = new ArrayList<>();

        Opcao velocidade = new Opcao(
                "Velocidade",
                new String[]{"Lento", "Normal", "Rápido"});
        velocidade.indiceValor = buffer.getVelocidadeTexto();
        opcoes.add(velocidade);

        String[] valoresVolume = new String[11];

        for (int i = 0; i <= 10; i++) {
            valoresVolume[i] = String.valueOf(i * 10);
        }

        Opcao volume = new Opcao("Volume", valoresVolume);
        volume.indiceValor = buffer.getVolume() / 10;
        opcoes.add(volume);

        Opcao confirmarSaida = new Opcao(
                "Confirmar saída",
                new String[]{"Sim", "Não"});
        confirmarSaida.indiceValor =
                buffer.isConfirmarSaida() ? 0 : 1;
        opcoes.add(confirmarSaida);

        Opcao dicasAtalho = new Opcao(
                "Dicas de atalho",
                new String[]{"Sim", "Não"});
        dicasAtalho.indiceValor =
                buffer.isDicasAtalho() ? 0 : 1;
        opcoes.add(dicasAtalho);

        opcoes.add(new Opcao("Salvar"));
        opcoes.add(new Opcao("Sair"));

        return opcoes;
    }

    private void aplicarValoresNasOpcoes(int[] valores){
        if (valores == null || valores.length < 4)
            return;
        buffer.setVelocidadeTexto(valores[0]);
        buffer.setVolume(valores[1] * 10);
        buffer.setConfirmarSaida(valores[2] == 0);
        buffer.setDicasAtalho(valores[3] == 0);

    }

    private boolean sair(){
        if (!mudou())
            return true;

        List<Opcao> pergunta = new ArrayList<>();
        pergunta.add((new Opcao("Sim")));
        pergunta.add((new Opcao("Não")));
        Resposta r = MenuInterativo.abrir(pergunta, "Descartar alterações?");
        return r.resultado == MenuInterativo.Resultado.SELECIONADO && r.indiceOpcao == 0;
    }

    private boolean mudou(){
        return !buffer.equals(original);
    }

    private void salvar(){
        original.setVelocidadeTexto(buffer.getVelocidadeTexto());
        original.setDicasAtalho(buffer.isDicasAtalho());
        original.setVolume(buffer.getVolume());
        original.setConfirmarSaida(buffer.isConfirmarSaida());



        try {
            ConfiguracoesAdmin.salvar(original);
            System.out.println();
            System.out.println(Cores.VERDE + "  Configurações salvas." + Cores.RESET);
            System.out.println(Cores.CIANO + "  [ ENTER para continuar ]" + Cores.RESET);
            Entrada.lerLinha();
        } catch (SaveException | DadosInvalidosException e) {
            System.out.println();
            System.out.println(Cores.VERMELHO + "  Erro ao salvar: " + e.getMessage() + Cores.RESET);
            System.out.println(Cores.CIANO + "  [ ENTER para continuar ]" + Cores.RESET);
            Entrada.lerLinha();
        }

        buffer = new Configuracoes(original);
    }
}
