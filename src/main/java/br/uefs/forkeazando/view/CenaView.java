package br.uefs.forkeazando.view;

import br.uefs.forkeazando.model.*;
import br.uefs.forkeazando.roteiro.ApresentacaoDeCena;
import br.uefs.forkeazando.roteiro.Epilogos;
import br.uefs.forkeazando.roteiro.SistemaAcademico;
import br.uefs.forkeazando.roteiro.VisuaisDeFinal;
import br.uefs.forkeazando.view.pixel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CenaView {

    private static final int LARGURA_PADRAO = 64;
    private static final int LARGURA_MINIMA = 44;
    private int larg = LARGURA_PADRAO;

    private static final int LARGURA_ARTE = LARGURA_PADRAO + 2;
    private static final int ALTURA_MINIMA_ARTE = 30;
    private static final int ALTURA_MINIMA_RETRATO = 22;
    private static final int LARGURA_RETRATO = Retratos.LARGURA;

    private volatile boolean mostrarIndicadorPause = true;
    private volatile Protagonista protagonistaAtual;
    private String ultimaIlustracao;
    private boolean avisouArteOculta;
    private final boolean dicasAtalho;
    private final int velocidadeTexto;
    private static final long[] PAUSA_LINHA_MS = {80, 30, 0};
    private static final long[] PAUSA_PENSAMENTO_MS = {600, 400, 150};

    public CenaView(boolean dicasAtalho, int velocidadeTexto) {
        this.dicasAtalho = dicasAtalho;
        this.velocidadeTexto = Math.max(0, Math.min(2, velocidadeTexto));
    }

    private void pausarAposLinha(boolean ehPensamento, String linha) {
        dormir(PAUSA_LINHA_MS[velocidadeTexto]);

        if (ehPensamento && linha.length() < 25) {
            dormir(PAUSA_PENSAMENTO_MS[velocidadeTexto]);
        }
    }

    static int larguraDaCaixa(int colunas) {
        return Math.max(LARGURA_MINIMA, Math.min(LARGURA_PADRAO, colunas - 2));
    }

    private void atualizarLargura() {
        try {
            larg = larguraDaCaixa(Entrada.terminal().getWidth());
        } catch (RuntimeException e) {
            larg = LARGURA_PADRAO;
        }
    }

    static boolean arteCabe(int largura, int altura) {
        return largura >= LARGURA_ARTE && altura >= ALTURA_MINIMA_ARTE;
    }

    static boolean retratoCabe(int largura, int altura) {
        return largura >= LARGURA_MINIMA + 2 && altura >= ALTURA_MINIMA_RETRATO;
    }

    private boolean terminalComporta() {
        try {
            return arteCabe(Entrada.terminal().getWidth(), Entrada.terminal().getHeight());
        } catch (RuntimeException e) {
            return false;
        }
    }

    private boolean retratoComporta() {
        try {
            return retratoCabe(Entrada.terminal().getWidth(), Entrada.terminal().getHeight());
        } catch (RuntimeException e) {
            return false;
        }
    }

    /** Chamar ao carregar save: se a arte já estava na tela antes, ela precisa redesenhar. */
    public void resetarCacheVisual() {
        this.ultimaIlustracao = null;
        this.avisouArteOculta = false;
    }

    private String indicadorPause() {
        return Entrada.estaPausado() ? "[C] Continuar" : "[P] Pausar";
    }

    public void mostrarGaleria(Protagonista protagonista) {
        List<String> flags = protagonista.getHistoricoFlags();
        System.out.println();
        for (String linha : Galeria.linhas(flags)) {
            System.out.println(linha);
        }
        System.out.println();
        boolean encontrou = false;
        for (Colecionavel c : Colecionavel.values()) {
            if (flags.contains(c.getFlag())) {
                if (!encontrou) {
                    System.out.println("COLECIONÁVEIS DESBLOQUEADOS:");
                    encontrou = true;
                }
                String tipo = c.getTipo() == Colecionavel.Tipo.HABILIDADE ? "Habilidade" : "Item";
                System.out.println("  [✓] " + tipo + ": " + c.getNome());
            }
        }
        if (!encontrou) {
            System.out.println("Você ainda não desbloqueou nenhum colecionável.");
        }
        System.out.println();
        System.out.println("Pressione ENTER para voltar...");
    }


    public void renderizar(Cena cena, Protagonista p) {
        renderizar(cena, p, p.temFlag("cap6_trancou") ? 1 : 0, null);
    }

    public void renderizar(Cena cena, Protagonista p,
                           int semestresExtras, ApresentacaoDeCena visual) {
        Entrada.permitirPausa(true);
        atualizarLargura();
        try {
            String corTexto = corPorFalante(cena.getPersonagemFalando());
            protagonistaAtual = p;
            System.out.println();
            mostrarIlustracao(visual);
            imprimirLinha(cena.getCapitulo().toUpperCase(), Cores.BRANCO);
            imprimirLinhaHud(cabecalhoStatus(p));
            imprimirLinhaHud(Hud.barras(
                    p.getEnergia(), p.getEnergiaMaxima(),
                    p.getScore(), p.getParticipacao()));
            System.out.println(Cores.CIANO + "╠" + "═".repeat(larg) + "╣" + Cores.RESET);

            if (!cena.getPersonagemFalando().equalsIgnoreCase("Narrador")) {
                if (!mostrarRetrato(cena.getPersonagemFalando(), corTexto, visual)) {
                    imprimirLinha(cena.getPersonagemFalando().toUpperCase(), corTexto);
                }
                imprimirLinha("", corTexto);
            }

            for (String paragrafo : cena.getTextoNarrativo().split("\n")) {
                Entrada.aguardarPause();

                if (paragrafo.equals("{{APROVACAO}}")) {
                    mostrarAprovacao();
                    continue;
                }

                if (paragrafo.equals("{{CALENDARIO_CAP7}}")) {
                    String t = semestresExtras > 0
                            ? "O trancamento acrescentou " + semestresExtras
                            + (semestresExtras == 1 ? " semestre" : " semestres")
                            + " ao seu calendário. O registro guarda a data, mas não explica "
                            + "o que tornou a pausa necessária."
                            : "Você continuou no calendário previsto. Isso não significa "
                            + "que a crise tenha passado sem deixar marcas na sua relação com o tempo.";
                    for (String l : quebrarLinha(t, larg - 4)) imprimirLinha(l, Cores.CINZA);
                    continue;
                }

                if (paragrafo.equals("{{CALENDARIO_CAP8}}")) {
                    String t = semestresExtras > 0
                            ? "Sua formatura chegou depois do calendário inicial, com "
                            + semestresExtras + " "
                            + (semestresExtras == 1 ? "semestre extra registrado" : "semestres extras registrados")
                            + ". O histórico mostra o tempo, mas não tudo o que você precisou reorganizar."
                            : "Você chegou à formatura no calendário previsto. Isso não quer dizer "
                            + "que o caminho tenha sido simples ou que você tenha atravessado tudo sem pausa.";
                    for (String l : quebrarLinha(t, larg - 4)) imprimirLinha(l, Cores.CINZA);
                    continue;
                }

                if (paragrafo.equals("{{ORIENTADOR_RECURSO}}")) {
                    if (p.temFlag("sistema_contestado")) {
                        String t = "'Você recorreu da classificação. Eu vi.'\n\n"
                                + "'O recurso leva quinze dias. O laboratório não espera.'\n\n"
                                + "'Bem-vinda.'";
                        imprimirBloco(t, corTexto);
                    }
                    continue;
                }

                if (paragrafo.contains("{{REMENTENTE_LINKEDIN}}")) {
                    String rem;
                    if (p.temFlag("conheceu_malu")) {
                        rem = "a Malu. Aquela do primeiro dia, do \"bom dia pra uma planta\". Ela não desistiu de você";
                    } else if (p.temFlag("cap3_colou_de_fato")) {
                        rem = "a Bia. Você nem lembrava que tinham se adicionado";
                    } else {
                        rem = "uma colega de outro semestre";
                    }
                    paragrafo = paragrafo.replace("{{REMENTENTE_LINKEDIN}}", rem);
                    // cai no fluxo normal de impressão abaixo
                }

                if (paragrafo.equals("{{DEPOIS_DA_PORTA}}")) {
                    String t = p.getRelacionamento(PersonagemSecundario.VETERANO) >= 2f
                            ? "É o Caio.\n\nEle entra sem esperar resposta. Senta no chão do quarto. "
                            + "Não pergunta se você tá bem. Não fala nada.\n\nUma hora.\n\nDuas horas.\n\nNinguém fala nada."
                            : "Você não abre.\n\nO som some depois de um tempo. Ou a pessoa foi embora.\n\n(Talvez o Caio.)\n\n(Talvez ninguém.)";
                    for (String l : quebrarLinha(t, larg - 4)) imprimirLinha(l, Cores.CINZA);
                    continue;
                }

                if (paragrafo.equals("{{FINAL_PESQUISA}}"))       { mostrarFinalPesquisa(p, corTexto);       continue; }
                if (paragrafo.equals("{{FINAL_TECNOLOGIA}}"))     { mostrarFinalTecnologia(p, corTexto);     continue; }
                if (paragrafo.equals("{{FINAL_INTERNACIONAL}}"))  { mostrarFinalInternacional(p, corTexto);  continue; }
                if (paragrafo.equals("{{FINAL_INDEPENDENTE}}"))   { mostrarFinalIndependente(p, corTexto);   continue; }
                if (paragrafo.equals("{{FINAL_TRANSICAO}}"))      { mostrarFinalTransicao(p, corTexto);      continue; }

                if (paragrafo.isBlank()) {
                    imprimirLinha("", corTexto);
                    continue;
                }

                String trimmed = paragrafo.trim();
                boolean ehPensamento = trimmed.startsWith("(") && trimmed.endsWith(")");
                String corLinha = ehPensamento ? Cores.CINZA : corTexto;

                for (String linhaQuebrada : quebrarLinha(paragrafo, larg - 4)) {
                    imprimirLinha(linhaQuebrada, corLinha);
                    pausarAposLinha(ehPensamento, linhaQuebrada);
                }
            }
            System.out.println(Cores.CIANO + "╚" + "═".repeat(larg) + "╝" + Cores.RESET);
        } finally {
            Entrada.permitirPausa(false);
        }
    }

    private Hud.Linha cabecalhoStatus(Protagonista p) {
        return Hud.status(p.getRank(), indicadorPause(), larg - 2);
    }

    private void imprimirLinhaHud(Hud.Linha linha) {
        int espacos = Math.max(0, larg - 2 - linha.visivel());
        System.out.println(Cores.CIANO + "║" + Cores.RESET + "  "
                + linha.ansi() + " ".repeat(espacos)
                + Cores.CIANO + "║" + Cores.RESET);
    }

    //  Mensagens / avisos

    public void mostrarBencaoCaramelo() {
        System.out.println();
        System.out.println(Cores.AMARELO + ">>> O Caramelo passa do lado. Ele para. Ele te olha." + Cores.RESET);
        System.out.println(Cores.AMARELO + "    Ele vai embora." + Cores.RESET);
        System.out.println(Cores.CINZA + "    (Foi... um sinal?)" + Cores.RESET);
        System.out.println();
    }

    public void mostrarHabilidade(String flag) {
        String nome = Colecionavel.porFlag(flag).map(Colecionavel::getNome).orElse(flag);
        System.out.println();
        System.out.println(Cores.MAGENTA + "  ╭─ NOVA HABILIDADE ─────────────────╮" + Cores.RESET);
        System.out.println(Cores.MAGENTA + "  │  " + nome + Cores.RESET);
        System.out.println(Cores.MAGENTA + "  ╰───────────────────────────────────╯" + Cores.RESET);
        System.out.println();
    }

    public void mostrarItem(String flag) {
        String nome = Colecionavel.porFlag(flag).map(Colecionavel::getNome).orElse(flag);
        System.out.println();
        System.out.println(Cores.CIANO + "  ╭─ ITEM ADQUIRIDO ──────────────────╮" + Cores.RESET);
        System.out.println(Cores.CIANO + "  │  " + nome + Cores.RESET);
        System.out.println(Cores.CIANO + "  ╰───────────────────────────────────╯" + Cores.RESET);
        System.out.println();
    }

    public void mostrarConquista(Conquista conquista) {
        mostrarMensagem("\n🏆 CONQUISTA DESBLOQUEADA: " + conquista.getTitulo()
                + "\n" + conquista.getDescricao());
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println();
        System.out.println(Cores.AMARELO + mensagem + Cores.RESET);
    }

    //  Aprovação (animação)

    private void imprimirLinhaAprovacao(String linha, int margem) {
        System.out.print("║");
        for (int i = 0; i < margem; i++) System.out.print(" ");
        System.out.print(linha);
        for (int i = 0; i < margem; i++) System.out.print(" ");
        System.out.println("║");
    }

    private void apagarAprovacao(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            System.out.print("\033[2K");
            if (i < quantidade - 1) System.out.print("\033[1A");
        }
        System.out.print("\033[" + quantidade + "A");
    }

    private void dormir(long milissegundos) {
        long inicio = System.currentTimeMillis();
        while (System.currentTimeMillis() - inicio < milissegundos) {
            Entrada.aguardarPause();
            try { Thread.sleep(50); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
        }
    }

    public void mostrarAprovacao() {
        atualizarLargura();
        String[] linhas = {
                "╔══════════════════════════════════════════╗",
                "║                                          ║",
                "║              APROVADO(A)                 ║",
                "║                                          ║",
                "║       ENGENHARIA DE COMPUTAÇÃO           ║",
                "║                                          ║",
                "║   Universidade Estadual do Fork Perdido  ║",
                "║                  (UEFP)                  ║",
                "║                                          ║",
                "╚══════════════════════════════════════════╝"
        };
        int larguraArte = linhas[0].length();
        int margem = Math.max(0, (larg - larguraArte) / 2);
        for (int ciclo = 0; ciclo < 3; ciclo++) {
            for (String linha : linhas) imprimirLinhaAprovacao(linha, margem);
            dormir(350);
            apagarAprovacao(linhas.length);
            dormir(200);
        }
        for (String linha : linhas) imprimirLinhaAprovacao(linha, margem);
    }


    public void mostrarImpactoRelacionamento(PersonagemSecundario npc, float valor,
                                             Protagonista.NivelRelacionamento nivel) {
        String cor = valor > 0 ? Cores.VERDE : Cores.VERMELHO;
        String mudanca = valor > 0 ? "melhorou" : "piorou";
        System.out.println();
        System.out.println(cor + ">>> O teu relacionamento com " + npc.getNome()
                + " " + mudanca + "." + Cores.RESET);
        System.out.println(Cores.CIANO + "[ Pressione ENTER para continuar... ]" + Cores.RESET);
        Entrada.lerLinha();
    }


    public void mostrarEpilogos(Protagonista p) {
        atualizarLargura();
        System.out.println();
        System.out.println(Cores.MAGENTA + "══════ O QUE FICOU ══════" + Cores.RESET);
        mostrarIlustracaoDoFinal(p);
        System.out.println();

        for (Epilogos.Epilogo bloco : Epilogos.de(p)) {
            renderizarEpilogo(bloco.titulo(), bloco.texto());
        }

        System.out.println(Cores.CIANO + "[ Pressione ENTER para encerrar. ]" + Cores.RESET);
        Entrada.lerLinha();
    }

    private void renderizarEpilogo(String titulo, String texto) {
        System.out.println(Cores.CIANO + "┌" + "─".repeat(larg) + "┐" + Cores.RESET);
        imprimirLinha(titulo, Cores.AMARELO);
        System.out.println(Cores.CIANO + "├" + "─".repeat(larg) + "┤" + Cores.RESET);
        for (String paragrafo : texto.split("\n")) {
            if (paragrafo.isBlank()) {
                imprimirLinha("", Cores.BRANCO);
                continue;
            }
            for (String linha : quebrarLinha(paragrafo, larg - 4)) {
                imprimirLinha(linha, Cores.BRANCO);
            }
        }
        System.out.println(Cores.CIANO + "└" + "─".repeat(larg) + "┘" + Cores.RESET);
        System.out.println();
    }


    public void mostrarCurriculo(Protagonista p) {
        atualizarLargura();

        var linhas = Curriculo.linhas(p);
        if (linhas.isEmpty()) return;

        System.out.println();
        System.out.println(Cores.AMARELO + "╔" + "═".repeat(larg) + "╗" + Cores.RESET);
        imprimirLinha("CURRÍCULO — " + p.getNome().toUpperCase(), Cores.AMARELO);
        System.out.println(Cores.AMARELO + "╠" + "═".repeat(larg) + "╣" + Cores.RESET);

        Curriculo.Categoria categoriaAtual = null;
        for (var linha : linhas) {
            if (linha.categoria() != categoriaAtual) {
                if (categoriaAtual != null) imprimirLinha("", Cores.CINZA);
                imprimirLinha(linha.categoria().rotulo, Cores.CIANO);
                categoriaAtual = linha.categoria();
            }
            String marcador = linha.positiva() ? "  • " : "  ✗ ";
            String cor = linha.positiva() ? Cores.BRANCO : Cores.CINZA;
            for (String quebrada : quebrarLinha(marcador + linha.texto(), larg - 4)) {
                imprimirLinha(quebrada, cor);
            }
        }

        System.out.println(Cores.AMARELO + "╚" + "═".repeat(larg) + "╝" + Cores.RESET);
        System.out.println();
        System.out.println(Cores.CIANO + "[ ENTER para continuar ]" + Cores.RESET);
        Entrada.lerLinha();
    }


    public void mostrarTransicao(String capitulo) {
        if (!terminalComporta() || !Animador.habilitada()) return;
        Animacao animacao = Transicao.animacao(capitulo);
        Animador.tocar(animacao, System.out, this::dormir);
    }


    private void mostrarIlustracao(ApresentacaoDeCena visual) {
        if (visual == null || visual.ilustracao() == null) return;
        String chave = visual.ilustracao();
        if (chave.equals(ultimaIlustracao)) return;
        if (!terminalComporta()) {
            avisarArteOculta();
            return;
        }
        desenharIlustracao(chave);
    }

    private void avisarArteOculta() {
        if (!avisouArteOculta) {
            avisouArteOculta = true;
            System.out.println(Cores.CINZA + "  (aumente o terminal p/ " + LARGURA_ARTE + "×" + ALTURA_MINIMA_ARTE
                    + " e veja a arte)" + Cores.RESET);
        }
    }

    private void mostrarIlustracaoDoFinal(Protagonista p) {
        String chave = VisuaisDeFinal.ilustracaoDe(p);
        System.out.println(Cores.AMARELO + "  FINAL — "
                + VisuaisDeFinal.tituloDe(p).toUpperCase() + Cores.RESET);
        desenharIlustracao(chave);
        System.out.println();
    }

    private void desenharIlustracao(String chave) {
        if (!Ilustracoes.existe(chave) || !terminalComporta()) return;
        Optional<Animacao> animacao = Ilustracoes.animacao(chave);
        if (animacao.isPresent() && Animador.habilitada()) {
            Animador.tocar(animacao.get(), System.out, this::dormir);
        } else {
            for (String linha : Ilustracoes.linhas(chave)) {
                System.out.println(linha + Cores.RESET);
            }
        }
        ultimaIlustracao = chave;
    }


    private boolean mostrarRetrato(String nomeFalante, String corTexto, ApresentacaoDeCena visual) {
        if (visual == null || visual.falante() == null || !retratoComporta()) return false;
        Optional<String[]> retrato = Retratos.linhas(visual.falante(), visual.expressao());
        if (retrato.isEmpty()) return false;
        String[] linhas = retrato.get();
        int campoNome = larg - 2 - LARGURA_RETRATO - 2;
        String nome = nomeFalante.toUpperCase();
        if (nome.length() > campoNome) nome = nome.substring(0, campoNome);
        int preenchimento = Math.max(0, campoNome - nome.length());
        int meio = linhas.length / 2;
        for (int i = 0; i < linhas.length; i++) {
            String lateral = (i == meio)
                    ? corTexto + nome + Cores.RESET + " ".repeat(preenchimento)
                    : " ".repeat(campoNome);
            System.out.println(Cores.CIANO + "║" + Cores.RESET + "  " + linhas[i] + Cores.RESET
                    + "  " + lateral + Cores.CIANO + "║" + Cores.RESET);
        }
        return true;
    }


    public void mostrarChuteDoSistema(Protagonista p) {
        atualizarLargura();

        SistemaAcademico.Perfil perfil = SistemaAcademico.classificar(p);
        String linhaDoChute = SistemaAcademico.descricaoDe(perfil);

        System.out.println();
        System.out.println(Cores.AMARELO + "  ╭─────────────────────────────────────╮" + Cores.RESET);
        System.out.println(Cores.AMARELO + "  │  SISTEMA ACADÊMICO — VEREDITO       │" + Cores.RESET);
        System.out.println(Cores.AMARELO + "  ╰─────────────────────────────────────╯" + Cores.RESET);
        System.out.println();

        for (String l : quebrarLinha("'Processamento concluído.'", larg)) {
            System.out.println(Cores.CINZA + "  " + l + Cores.RESET);
        }
        System.out.println();
        System.out.println(Cores.AMARELO + "  " + linhaDoChute + Cores.RESET);
        System.out.println();

        for (String l : quebrarLinha("'Recomendação baseada em padrões históricos.'", larg)) {
            System.out.println(Cores.CINZA + "  " + l + Cores.RESET);
        }
        for (String l : quebrarLinha("'Ignore se preferir. O sistema não decide por você.'", larg)) {
            System.out.println(Cores.CINZA + "  " + l + Cores.RESET);
        }
        for (String l : quebrarLinha("'Ainda.'", larg)) {
            System.out.println(Cores.CINZA + "  " + l + Cores.RESET);
        }
        System.out.println();
        System.out.println(Cores.CIANO + "[ ENTER para continuar ]" + Cores.RESET);
        Entrada.lerLinha();
    }

    //  Coda dos finais (dívida técnica: deveria estar em Epilogos)
    //  Cada método imprime o bloco de personagem que precede a
    //  reflexão analítica da cena 662-666.

    private void mostrarFinalPesquisa(Protagonista p, String corTexto) {
        String t;
        if (p.getRelacionamento(PersonagemSecundario.DANDARA) >= 3f) {
            t = "Três meses depois. Você tá no laboratório, às 23h47.\n\n"
                    + "O celular apita.\n\n"
                    + "'Li o artigo.' — é a Dandara.\n\n"
                    + "'Demorei duas semanas. Não entendi metade. A outra metade me deu raiva.'\n\n"
                    + "Pausa.\n\n"
                    + "'Quando você termina outro?'";
        } else if (p.temFlag("caio_revelacao")) {
            t = "Três meses depois. Você tá no laboratório, às 23h47.\n\n"
                    + "O celular apita.\n\n"
                    + "'Vi teu nome no artigo.' — é o Caio.\n\n"
                    + "'Lembrei da frase que você me disse. Ainda tá valendo, né?'\n\n"
                    + "Você manda 'tá'. Ele manda um áudio de três minutos.\n\n"
                    + "Você ouve até o fim.";
        } else if (p.temFlag("cap7_questionou_autoria")) {
            t = "Três meses depois. O artigo foi publicado.\n\n"
                    + "Você abre o PDF. Seu nome tá lá. Segundo autora.\n\n"
                    + "Abre o e-mail do orientador.\n\n"
                    + "'Eu subestimei você. Não é elogio. É reconhecimento.'\n\n"
                    + "Você fecha o e-mail.";
        } else {
            t = "Três meses depois. Você tá no laboratório, às 23h47.\n\n"
                    + "O celular não apita. O artigo não saiu ainda.\n\n"
                    + "A bolsa cobre metade das passagens.\n\n"
                    + "Você continua.";
        }
        imprimirBloco(t, corTexto);
    }

    private void mostrarFinalTecnologia(Protagonista p, String corTexto) {
        String t;
        if (p.temFlag("mentoria_caio") || p.temFlag("caio_revelacao")) {
            t = "Primeiro dia. O ambiente de dev demora 4 horas pra liberar.\n\n"
                    + "O celular apita.\n\n"
                    + "'Cê tá no meu time, né?' — é o Caio.\n\n"
                    + "'Não derruba o servidor.'\n\n"
                    + "Você ri. Ele não tá brincando. Você também não.";
        } else if (p.temFlag("cap5c_bia_ajudou") || p.temFlag("item_contato_bia")) {
            t = "Primeiro dia. A empresa tem quinze pessoas.\n\n"
                    + "Você abre o Slack. Tem mensagem da Bia:\n\n"
                    + "'Eu avisei que o cliente ia pagar mais. Ninguém acreditou.'\n\n"
                    + "Você não responde. Guarda o contato.";
        } else {
            t = "Primeiro dia. A senha do ambiente de dev demora 4 horas pra liberar.\n\n"
                    + "Alguém do time te passa o caminho.\n\n"
                    + "O primeiro commit é pequeno.\n\n"
                    + "Não derruba nada. Ainda.";
        }
        imprimirBloco(t, corTexto);
    }

    private void mostrarFinalInternacional(Protagonista p, String corTexto) {
        String t;
        if (p.temFlag("malu_reconectou") || p.temFlag("conheceu_malu")) {
            t = "A semana antes do embarque. Malas na cama.\n\n"
                    + "O celular apita. É a Malu:\n\n"
                    + "'Eu falei bom dia pra uma planta. Você agora fala bom dia em quantas línguas?'\n\n"
                    + "Pausa.\n\n"
                    + "'Quando você voltar, a gente se fala. Ou não. Só não some.'\n\n"
                    + "Você promete. Você sabe que talvez não cumpra. Ela também.";
        } else if (p.temFlag("item_copo_caio")) {
            t = "A semana antes do embarque. O copo térmico amassado tá em cima da escrivaninha.\n\n"
                    + "Você pensa em deixar. Pensa em levar.\n\n"
                    + "Pensa em ligar pro Caio. Não liga.\n\n"
                    + "Leva o copo. Fecha a mala.";
        } else {
            t = "A semana antes do embarque. Malas na cama.\n\n"
                    + "O celular não apita.\n\n"
                    + "Você abre a conversa com a mãe. Fecha. Abre. Fecha.\n\n"
                    + "Vai dar notícia em pessoa.";
        }
        imprimirBloco(t, corTexto);
    }

    private void mostrarFinalIndependente(Protagonista p, String corTexto) {
        String t;
        if (p.temFlag("cap5c_bia_ajudou") || p.temFlag("item_contato_bia")) {
            t = "O primeiro contrato grande. R$ 8 mil. Escopo claro.\n\n"
                    + "Você lê a proposta da Bia: 50/50.\n\n"
                    + "'Eu entro com o contato. Você entra com o código. Metade pra cada. Se der ruim, a gente divide.'\n\n"
                    + "Você pensa em dizer não. Você diz sim.\n\n"
                    + "Pela primeira vez, você não tá fazendo tudo sozinha.";
        } else if (p.temFlag("cap7_indep_negociou")) {
            t = "O primeiro contrato grande. R$ 8 mil. Cláusula revisada.\n\n"
                    + "O cliente pergunta se você tem CNPJ. Você manda o número.\n\n"
                    + "Ele responde 'ok'.\n\n"
                    + "Você fecha o notebook. Já era hora.";
        } else {
            t = "O primeiro contrato pequeno. R$ 800. Cláusula confusa.\n\n"
                    + "Você lê três vezes. Não entende. Não assina.\n\n"
                    + "O cliente reclama. Você aprendeu.\n\n"
                    + "Próximo.";
        }
        imprimirBloco(t, corTexto);
    }

    private void mostrarFinalTransicao(Protagonista p, String corTexto) {
        String t;
        if (p.temFlag("malu_reconectou") || p.temFlag("conheceu_malu")) {
            t = "Primeiro dia da nova área. Você tá no corredor errado.\n\n"
                    + "O celular apita. É a Malu:\n\n"
                    + "'Eu também mudei. Saí de Engenharia. Tô em Design.'\n\n"
                    + "Pausa.\n\n"
                    + "'Todo mundo acha que foi fracasso. Eu acho que foi a coisa mais honesta que eu fiz.'\n\n"
                    + "Você guarda a mensagem. Vai procurar a sala certa.";
        } else if (p.temFlag("mentoria_caio")) {
            t = "Primeiro dia da nova área. Você tá no corredor errado.\n\n"
                    + "Lembra do Caio. Ele desistiu. Voltou. Desistiu de novo. Voltou de novo.\n\n"
                    + "O copo térmico dele continua amassado. O seu caminho também.\n\n"
                    + "Você segue.";
        } else {
            t = "Primeiro dia da nova área. Você tá no corredor errado.\n\n"
                    + "Ninguém aqui sabe o que você fez antes. Ninguém vai perguntar.\n\n"
                    + "Você não precisa contar tudo. Só o suficiente pra começar.";
        }
        imprimirBloco(t, corTexto);
    }

    private void imprimirParagrafo(String paragrafo, String corTexto) {
        if (paragrafo.isBlank()) {
            imprimirLinha("", corTexto);
            return;
        }
        String trimmed = paragrafo.trim();
        boolean ehPensamento = trimmed.startsWith("(") && trimmed.endsWith(")");
        String corLinha = ehPensamento ? Cores.CINZA : corTexto;
        for (String linhaQuebrada : quebrarLinha(paragrafo, larg - 4)) {
            imprimirLinha(linhaQuebrada, corLinha);
            pausarAposLinha(ehPensamento, linhaQuebrada);
        }
    }

    private void imprimirBloco(String texto, String corTexto) {
        for (String paragrafo : texto.split("\n")) {
            Entrada.aguardarPause();
            imprimirParagrafo(paragrafo, corTexto);
        }
    }

    private String corPorFalante(String personagemFalando) {
        if (personagemFalando.equalsIgnoreCase("Narrador")) return Cores.BRANCO;
        if (personagemFalando.equalsIgnoreCase("Sistema"))  return Cores.AMARELO;
        return Cores.MAGENTA;
    }

    private void imprimirLinha(String texto, String cor) {
        int espacos = Math.max(0, larg - texto.length() - 2);
        System.out.println(Cores.CIANO + "║" + Cores.RESET
                + "  " + cor + texto + Cores.RESET + " ".repeat(espacos)
                + Cores.CIANO + "║" + Cores.RESET);
    }

    private List<String> quebrarLinha(String texto, int largura) {
        List<String> linhas = new ArrayList<>();
        String[] palavras = texto.split(" ");
        StringBuilder atual = new StringBuilder();
        for (String palavra : palavras) {
            if (atual.length() + palavra.length() + 1 > largura) {
                linhas.add(atual.toString());
                atual = new StringBuilder();
            }
            if (!atual.isEmpty()) atual.append(" ");
            atual.append(palavra);
        }
        if (!atual.isEmpty()) linhas.add(atual.toString());
        return linhas;
    }

}