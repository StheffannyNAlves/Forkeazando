package br.uefs.forkeazando.view;

import br.uefs.forkeazando.view.pixel.AberturaMenu;
import br.uefs.forkeazando.view.pixel.Animador;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MenuPrincipal {


    private static final int ARTE_L = AberturaMenu.LARGURA;
    private static final int ARTE_A = AberturaMenu.LINHAS;    // linhas de texto = 48 pixels
    private static final int MENU_L = 32;   // largura da caixa do menu (com bordas)
    private static final int ESPACO = 3;    // espaço entre a arte e o menu
    private static final int LARGURA_LADO_A_LADO = ARTE_L + ESPACO + MENU_L;
    private static final long INTERVALO_ANIMACAO_MS = 140;


    private static final String RST = "\033[0m";
    private static final String BORDA = "\033[38;2;120;100;240m";
    private static final String BG_CAIXA = "\033[48;2;14;12;40m";
    private static final String TXT = "\033[38;2;205;210;255m";
    private static final String TAG = "\033[38;2;196;186;255m";
    private static final String OURO = "\033[1;38;2;255;214;102m";
    private static final String SEL = "\033[1;38;2;255;255;255;48;2;76;56;215m";
    private static final String DICA = "\033[38;2;255;206;84m";

    private static final String[] OPCOES = {
            "[1] Nova Partida",
            "[2] Continuar",
            "[3] Carregar",
            "[4] Gerenciar saves",
            "[5] Instruções",
            "[6] Opções de Estilo",
            "[7] Salvar",
            "[8] Créditos",
            "[9] Meu progresso",
            "[0] Sair"
    };

    private static final String[] TAGLINE = {
            "Entre o que era certo",
            "e o que ainda pode ser."
    };

    // 8 quadros, cada um desenhado e renderizado uma vez só
    private final String[][] quadros = new String[AberturaMenu.QUADROS][];
    private int quadroAtual = 0;
    private int cacheLargura = -1;
    private int cacheAltura = -1;

    // Toda escrita na tela passa por `trava`: o laço de animação e o redesenho completo do
    // menu (a cada tecla) nunca se misturam.
    private final Object trava = new Object();
    private Thread animador;
    private volatile boolean animando;
    private int ultimoSelecionado;
    private int ultimaMargem;
    private int ultimaLargura = -1;
    private int ultimaAltura = -1;

    private final boolean dicasAtalho;

    public MenuPrincipal(boolean dicasAtalho) {
        this.dicasAtalho = dicasAtalho;
    }

    /** Compatibilidade com quem chama sem item selecionado. */
    public void mostrarMenu() {
        mostrarMenu(0);
    }

    public void mostrarMenu(int selecionado) {
        synchronized (trava) {
            desenharMenu(selecionado);
        }
    }


    private void desenharMenu(int selecionado) {
        Terminal terminal = Entrada.terminal();
        PrintWriter out = terminal.writer();
        String nl = System.lineSeparator();

        int larg = terminal.getWidth();
        int alt = terminal.getHeight();
        ultimoSelecionado = selecionado;

        if (larg < MENU_L + 4) {
            terminal.puts(InfoCmp.Capability.clear_screen);
            out.println("Aumente a largura do terminal.");
            out.flush();
            ultimaLargura = -1;
            return;
        }

        // Limpa a tela inteira só quando o tamanho muda; nas outras teclas
        // volta ao topo e sobrescreve (sem piscar).
        if (larg != cacheLargura || alt != cacheAltura) {
            terminal.puts(InfoCmp.Capability.clear_screen);
            cacheLargura = larg;
            cacheAltura = alt;
        }
        ultimaLargura = larg;
        ultimaAltura = alt;

        boolean comArte = larg >= LARGURA_LADO_A_LADO && alt > ARTE_A;
        List<String> menu = montarMenu(selecionado);

        out.print("\033[H");

        if (comArte) {
            String[] arte = quadro(quadroAtual);
            // arte + menu lado a lado, centralizados como UM bloco só
            int margemColunas = (larg - LARGURA_LADO_A_LADO) / 2;
            ultimaMargem = margemColunas;
            String margem = " ".repeat(margemColunas);
            int deslocamento = (ARTE_A - menu.size()) / 2;

            for (int r = 0; r < ARTE_A; r++) {
                int i = r - deslocamento;
                String direita = (i >= 0 && i < menu.size())
                        ? menu.get(i)
                        : " ".repeat(MENU_L);
                out.print(margem + arte[r] + RST + " ".repeat(ESPACO) + direita + "\033[K" + nl);
            }
        } else {
            // terminal pequeno: só o menu, centralizado
            String margem = " ".repeat((larg - MENU_L) / 2);
            out.print(nl);
            for (String linha : menu) {
                out.print(margem + linha + "\033[K" + nl);
            }
        }

        out.print("\033[J");
        out.flush();

        if (comArte && Animador.habilitada()) {
            iniciarAnimacao();
        } else {
            pararLaco();
        }
    }

    private String[] quadro(int indice) {
        if (quadros[indice] == null) {
            quadros[indice] = AberturaMenu.linhas(indice);
        }
        return quadros[indice];
    }

    //  ANIMAÇÃO: só repinta as colunas da arte, no lugar, sem tocar no menu

    private void iniciarAnimacao() {
        if (animando) return;
        animando = true;
        animador = new Thread(this::laco, "menu-animacao");
        animador.setDaemon(true);
        animador.start();
    }

    private void laco() {
        while (animando) {
            try {
                Thread.sleep(INTERVALO_ANIMACAO_MS);
            } catch (InterruptedException e) {
                return;
            }
            synchronized (trava) {
                if (!animando) return;
                avancarQuadro();
            }
        }
    }

    // Chamado dentro de `trava`
    private void avancarQuadro() {
        Terminal terminal = Entrada.terminal();
        if (terminal.getWidth() != ultimaLargura || terminal.getHeight() != ultimaAltura) {
            desenharMenu(ultimoSelecionado);      // o terminal mudou de tamanho: redesenho completo
            return;
        }
        quadroAtual = (quadroAtual + 1) % AberturaMenu.QUADROS;
        String[] arte = quadro(quadroAtual);
        PrintWriter out = terminal.writer();
        out.print("\0337");                       // salva o cursor
        for (int r = 0; r < ARTE_A; r++) {
            out.print("\033[" + (r + 1) + ";" + (ultimaMargem + 1) + "H" + arte[r] + RST);
        }
        out.print("\0338");                       // restaura o cursor
        out.flush();
    }

    private void pararLaco() {
        animando = false;
        Thread t = animador;
        animador = null;
        if (t != null) t.interrupt();
    }

    // Para a animação. Chamar sempre que o menu fechar (opção escolhida ou Esc), antes de a tela ser usada por outra coisa: sem isso o laço continuaria pintando a arte por cima do jogo.

    public void pararAnimacao() {
        synchronized (trava) {
            boolean estavaAnimando = animando;
            pararLaco();
            if (estavaAnimando) {
                // um quadro pode ter sido pintado entre o clear do MenuInterativo e esta chamada
                Terminal terminal = Entrada.terminal();
                terminal.puts(InfoCmp.Capability.clear_screen);
                terminal.flush();
            }
        }
    }


    private List<String> montarMenu(int selecionado) {
        int interno = MENU_L - 2;
        List<String> l = new ArrayList<>();

        l.add(BORDA + "╭" + "─".repeat(interno) + "╮" + RST);
        l.add(caixa(OURO + centralizar("FORKEAZANDO", interno)));
        for (String t : TAGLINE) {
            l.add(caixa(TAG + centralizar(t, interno)));
        }
        l.add(BORDA + "├" + "─".repeat(interno) + "┤" + RST);

        for (int i = 0; i < OPCOES.length; i++) {
            boolean ativo = i == selecionado;
            String texto = alinharEsquerda(" " + (ativo ? "❯ " : "  ") + OPCOES[i], interno);
            l.add(caixa((ativo ? SEL : TXT) + texto));
        }

        l.add(BORDA + "╰" + "─".repeat(interno) + "╯" + RST);

        if (dicasAtalho) {
            l.add(DICA + alinharEsquerda("↑ ↓ navegar   ENTER escolher", MENU_L) + RST);
        }

        return l;
    }

    /** Uma linha da caixa: bordas + fundo escuro + conteúdo já colorido. */
    private String caixa(String interno) {
        return BORDA + BG_CAIXA + "│" + interno + "\033[22m" + BORDA + BG_CAIXA + "│" + RST;
    }

    //  helpers de layout (texto puro, sem ANSI dentro do cálculo)

    private String centralizar(String texto, int largura) {
        if (texto.length() > largura) texto = texto.substring(0, largura);
        int total = largura - texto.length();
        int esquerda = total / 2;
        return " ".repeat(esquerda) + texto + " ".repeat(total - esquerda);
    }

    private String alinharEsquerda(String texto, int largura) {
        if (texto.length() > largura) texto = texto.substring(0, largura);
        return texto + " ".repeat(largura - texto.length());
    }


    public String lerOpcoes() {
        return Entrada.lerLinha();
    }

    public void mostrarInstrucoes() {
        System.out.println(Cores.CIANO
                +        "\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║               GUIA DE SOBREVIVÊNCIA                  ║");
        System.out.println("║          ENGENHARIA DE COMPUTAÇÃO · UEFP             ║");
        System.out.println("╚══════════════════════════════════════════════════════╝"
                + Cores.RESET);

        System.out.println("\nBem-vinda à Engenharia de Computação!");
        System.out.println("Sobreviver ao semestre exige estratégia. Preste atenção:\n");

        System.out.println(Cores.MAGENTA
                + "┌─ 01 · ESCOLHAS E CONSEQUÊNCIAS ──────────────────────┐"
                + Cores.RESET);
        System.out.println("Leia cada cena e digite o número da opção desejada.");
        System.out.println("Suas decisões moldam sua personalidade (flags),");
        System.out.println("desbloqueando opções futuras ou fechando caminhos.\n");

        System.out.println(Cores.VERDE
                + "┌─ 02 · GERENCIAMENTO DE RECURSOS ─────────────────────┐"
                + Cores.RESET);
        System.out.println("Sua vida universitária depende de três recursos:\n");

        System.out.println(Cores.VERMELHO + "  ♥ ENERGIA"
                + Cores.RESET);
        System.out.println("    Sua moeda mais valiosa. Assumir responsabilidades");
        System.out.println("    demais pode esgotá-la.\n");

        System.out.println(Cores.AMARELO + "  ★ SCORE"
                + Cores.RESET);
        System.out.println("    Representa seu desempenho técnico. Você precisará");
        System.out.println("    demonstrar que sabe o que está fazendo.\n");

        System.out.println(Cores.AZUL + "  ◆ PARTICIPAÇÃO"
                + Cores.RESET);
        System.out.println("    Sua atuação nas discussões de PBL. Ficar calada");
        System.out.println("    pode poupar energia, mas também fechar portas.\n");

        System.out.println(Cores.MAGENTA
                + "┌─ 03 · RELACIONAMENTOS ───────────────────────────────┐"
                + Cores.RESET);
        System.out.println("Veteranos, professores e colegas lembram de você.");
        System.out.println("Networking importa — e o Módulo 8 também.\n");

        System.out.println(Cores.AMARELO
                + "┌─ DICA DE SOBREVIVÊNCIA ──────────────────────────────┐"
                + Cores.RESET);
        System.out.println("Sua situação econômica e seu histórico escolar");
        System.out.println("definem sua energia inicial.");
        System.out.println(Cores.AMARELO
                + "O sistema não é justo. Adapte-se.\n"
                + Cores.RESET);

        System.out.println(Cores.CIANO
                + "────────────────────────────────────────────────────────");
        System.out.println("     [ ENTER ]  VOLTAR AO MENU PRINCIPAL");
        System.out.println("────────────────────────────────────────────────────────"
                + Cores.RESET);

        Entrada.lerLinha();
    }

    public void mostrarCreditos() {
        System.out.println(Cores.CIANO + "╔══════════════════════════════╗" + Cores.RESET);
        System.out.println(Cores.CIANO + "║           CRÉDITOS           ║" + Cores.RESET);
        System.out.println(Cores.CIANO + "║                              ║" + Cores.RESET);
        System.out.println(Cores.MAGEN + "║       Stheffanny Alves       ║" + Cores.RESET);
        System.out.println(Cores.BRANCO + "║                              ║" + Cores.RESET);
        System.out.println(Cores.CIANO + "║         FORKEAZANDO          ║" + Cores.RESET);
        System.out.println(Cores.CIANO + "║             2026             ║" + Cores.RESET);
        System.out.println(Cores.CIANO + "╚══════════════════════════════╝" + Cores.RESET);
        System.out.print("Pressione ENTER para voltar...");
        Entrada.lerLinha();
    }

    public void mostrarMensagem(String msg) {
        System.out.println(msg);
    }
    public void mostrarErro(String msg) {
        System.out.println(Cores.VERMELHO + msg + Cores.RESET);
    }

    public void mostrarSucesso(String msg) {
        System.out.println(Cores.VERDE + msg + Cores.RESET);
    }
}