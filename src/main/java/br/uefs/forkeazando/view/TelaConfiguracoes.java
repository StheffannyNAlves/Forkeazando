package br.uefs.forkeazando.view;

// Mensagens da tela de configurações; o controller não imprime nada diretamente
public class TelaConfiguracoes {

    public void mostrarSalvo() {
        System.out.println();
        System.out.println(Cores.VERDE + "  Configurações salvas." + Cores.RESET);
        aguardarEnter();
    }

    public void mostrarErro(String mensagem) {
        System.out.println();
        System.out.println(Cores.VERMELHO + "  Erro ao salvar: " + mensagem + Cores.RESET);
        aguardarEnter();
    }

    private void aguardarEnter() {
        System.out.println(Cores.CIANO + "  [ ENTER para continuar ]" + Cores.RESET);
        Entrada.lerLinha();
    }
}