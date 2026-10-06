package br.uefs.forkeazando.view;

import br.uefs.forkeazando.model.Cena;
import br.uefs.forkeazando.model.Escolha;
import br.uefs.forkeazando.model.PersonagemSecundario;
import br.uefs.forkeazando.model.Protagonista;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CenaView {

    private static final int LARGURA = 64;
    private volatile boolean mostrarIndicadorPause = true;
    private volatile Protagonista protagonistaAtual;

    private static final Map<String, String> NOMES_HABILIDADES = Map.of(
            "hab_olho_edital",       "Olho de Edital",
            "hab_estomago_ferro",    "Estômago de Ferro",
            "hab_foco_total",        "Foco Total",
            "hab_rede_contatos",     "Rede de Contatos",
            "hab_palavra_honesta",   "Palavra Honesta",
            "hab_sobreviveu_greve",  "Sobreviveu à Greve",
            "hab_contrato_assinado", "Contrato Assinado"
    );

    private static final Map<String, String> NOMES_ITENS = Map.of(
            "item_cartao_ru",        "Cartão do RU",
            "item_esquema_copiado",  "Esquema elétrico copiado",
            "item_pdfs_ic",          "5 PDFs de IC",
            "item_contato_freela",   "Contato de freela",
            "item_vaga_estagio",     "QR code de vaga de estágio",
            "item_prova_email",      "Foto do e-mail comprometedor",
            "item_copo_caio",        "Copo térmico do Cáio",
            "item_contato_bia",      "Contato da Bia"
    );

    private static final List<Integer> CENAS_DE_CHEFE = List.of(201, 305, 451, 550, 601, 649);

    private String indicadorPause() {
        if (Entrada.estaPausado()) {
            return "[P] Continuar";
        }

        if (mostrarIndicadorPause) {
            return "[P] Pausar";
        }

        return "         ";
    }

    private void atualizarIndicadorPause() {
        System.out.print("\033[s");

        System.out.print("\033[2;1H");
        System.out.print("\033[2K");

        imprimirLinha(cabecalhoStatus(protagonistaAtual), Cores.CINZA);

        System.out.print("\033[u");
        System.out.flush();
    }
    public void renderizar(Cena cena, Protagonista p) {
        Entrada.aguardarPause();
        String corTexto = corPorFalante(cena.getPersonagemFalando());

        protagonistaAtual = p;
        System.out.println();
        imprimirLinha(cena.getCapitulo().toUpperCase(), Cores.BRANCO);
        imprimirLinha(cabecalhoStatus(p), Cores.CINZA);   // <- usar BRANCO se não tiver CINZA
        System.out.println(Cores.CIANO + "╠" + "═".repeat(LARGURA) + "╣" + Cores.RESET);

        if (!cena.getPersonagemFalando().equalsIgnoreCase("Narrador")) {
            imprimirLinha(cena.getPersonagemFalando().toUpperCase(), corTexto);
            imprimirLinha("", corTexto);
        }

        for (String paragrafo : cena.getTextoNarrativo().split("\n")) {
            Entrada.aguardarPause();
            if (paragrafo.equals("{{APROVACAO}}")) {
                mostrarAprovacao();
                continue;
            }
            if (paragrafo.isBlank()) {
                imprimirLinha("", corTexto);
                continue;
            }

            // Detecta pensamento interno: linha inteira entre parênteses
            String trimmed = paragrafo.trim();
            boolean ehPensamento = trimmed.startsWith("(") && trimmed.endsWith(")");
            String corLinha = ehPensamento ? Cores.CINZA : corTexto;

            for (String linhaQuebrada : quebrarLinha(paragrafo, LARGURA - 4)) {
                imprimirLinha(linhaQuebrada, corLinha);
                if (ehPensamento && linhaQuebrada.length() < 25) {
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ignored) {
                    }
                }
            }


        }

        System.out.println(Cores.CIANO + "╚" + "═".repeat(LARGURA) + "╝" + Cores.RESET);
    }

    private String cabecalhoStatus(Protagonista p) {
        String rank = p.getRank();
        int energia = p.getEnergia();
        int maxima = p.getEnergiaMaxima();
        String barra = barraEnergia(energia, maxima);
        return "RANK: " + rank + "  •  ENERGIA: " + barra + " " + energia + "/" + maxima + " " + indicadorPause();
    }

    private String barraEnergia(int atual, int maxima) {
        if (maxima <= 0) return "";
        int blocos = 10;
        int cheios = (int) Math.round((double) atual / maxima * blocos);
        cheios = Math.max(0, Math.min(blocos, cheios));
        return "▓".repeat(cheios) + "░".repeat(blocos - cheios);
    }



    public boolean deveMostrarBencao(int cenaId, Protagonista p) {
        return CENAS_DE_CHEFE.contains(cenaId) && p.temFlag("amigo_do_caramelo");
    }

    public void mostrarBencaoCaramelo() {
        System.out.println();
        System.out.println(Cores.AMARELO + ">>> O Caramelo passa do lado. Ele para. Ele te olha." + Cores.RESET);
        System.out.println(Cores.AMARELO + "    Ele vai embora." + Cores.RESET);
        System.out.println(Cores.CINZA + "    (Foi... um sinal?)" + Cores.RESET);
        System.out.println();
    }


    public void mostrarHabilidade(String flag) {
        String nome = NOMES_HABILIDADES.getOrDefault(flag, flag);
        System.out.println();
        System.out.println(Cores.MAGENTA + "  ╭─ NOVA HABILIDADE ─────────────────╮" + Cores.RESET);
        System.out.println(Cores.MAGENTA + "  │  " + nome + Cores.RESET);
        System.out.println(Cores.MAGENTA + "  ╰───────────────────────────────────╯" + Cores.RESET);
        System.out.println();
    }

    public void mostrarItem(String flag) {
        String nome = NOMES_ITENS.getOrDefault(flag, flag);
        System.out.println();
        System.out.println(Cores.CIANO + "  ╭─ ITEM ADQUIRIDO ──────────────────╮" + Cores.RESET);
        System.out.println(Cores.CIANO + "  │  " + nome + Cores.RESET);
        System.out.println(Cores.CIANO + "  ╰───────────────────────────────────╯" + Cores.RESET);
        System.out.println();
    }

    public void iniciarAnimacaoPause(Protagonista p) {
        Thread animacao = new Thread(() -> {
            while (true) {
                dormir(500);
                mostrarIndicadorPause = !mostrarIndicadorPause;
                atualizarIndicadorPause();

                System.out.print("\033[s");
                System.out.print("\033[2;1H");
                System.out.print("\033[2K");
                imprimirLinha(cabecalhoStatus(p), Cores.CINZA);
                System.out.print("\033[u");
                System.out.flush();
            }
        });

        animacao.setDaemon(true);
        animacao.start();
    }


    private void imprimirLinhaAprovacao(String linha, int margem) {
        System.out.print("║");

        for (int i = 0; i < margem; i++) {
            System.out.print(" ");
        }

        System.out.print(linha);

        for (int i = 0; i < margem; i++) {
            System.out.print(" ");
        }

        System.out.println("║");
    }

    private void apagarAprovacao(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            System.out.print("\033[2K");

            if (i < quantidade - 1) {
                System.out.print("\033[1A");
            }
        }

        System.out.print("\033[" + quantidade + "A");
    }

    private void dormir(long milissegundos) {
        long inicio = System.currentTimeMillis();

        while (System.currentTimeMillis() - inicio < milissegundos) {
            Entrada.aguardarPause();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
    public void mostrarAprovacao() {
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

        int largura = linhas[0].length();
        int margem = (LARGURA - largura) / 2;

        for (int ciclo = 0; ciclo < 3; ciclo++) {
            for (String linha : linhas) {
                imprimirLinhaAprovacao(linha, margem);
            }

            dormir(350);

            apagarAprovacao(linhas.length);

            dormir(200);
        }

        for (String linha : linhas) {
            imprimirLinhaAprovacao(linha, margem);
        }
    }

    public void mostrarImpactoRelacionamento(PersonagemSecundario npc, float valor,
                                             Protagonista.NivelRelacionamento nivel) {
        String nome = npc.getNome();
        String cor = valor > 0 ? Cores.VERDE : Cores.VERMELHO;
        String mudanca = valor > 0 ? "melhorou" : "piorou";

        System.out.println();
        System.out.println(cor + ">>> O teu relacionamento com " + nome + " " + mudanca + "." + Cores.RESET);
        System.out.println(Cores.CIANO + "[ Pressione ENTER para continuar... ]" + Cores.RESET);
        Entrada.lerLinha();
    }

    public void mostrarEpilogos(Protagonista p) {
        System.out.println();
        System.out.println(Cores.MAGENTA + "══════ O QUE FICOU ══════" + Cores.RESET);
        System.out.println();

        if (p.temFlag("trilha_ic")) {
            renderizarEpilogo("O QUE VOCÊ VIROU",
                    "Você trabalha com segurança de sistemas. Empresa média, " +
                            "carteira assinada, salário ok. Você ainda pesquisa, só que " +
                            "agora é pago.");
        } else if (p.temFlag("trilha_dtec")) {
            renderizarEpilogo("O QUE VOCÊ VIROU",
                    "Você trabalha com sistemas embarcados. Empresa de hardware, " +
                            "Salvador, café ruim. Você mexe com placa todo dia. Foi a " +
                            "coisa que você escolheu no primeiro semestre, sem saber que " +
                            "tava escolhendo.");
        } else if (p.temFlag("trilha_indep")) {
            renderizarEpilogo("O QUE VOCÊ VIROU",
                    "Você não tem emprego. Você tem clientes. Uns pagam bem. " +
                            "Uns somem. Você aprendeu a cobrar antes. E a dizer não. " +
                            "Não é o que sua mãe queria. É o que você construiu.");
        }

        if (p.temFlag("mentoria_caio")) {
            String texto = p.temFlag("caio_revelacao")
                    ? "Cáio tá terminando o curso. Ainda atrasado, mas terminando. " +
                    "Ele te mandou mensagem semana passada. Disse que finalmente " +
                    "entendeu o que você quis dizer no primeiro semestre."
                    : "Cáio não se formou. Trabalha numa empresa de hardware em " +
                    "Salvador, ganha menos do que merecia. O copo amassado " +
                    "continua com ele. Ou com você. Difícil saber.";
            renderizarEpilogo("CÁIO", texto);
        }

        // Ajustado: sem trama de denúncia, agora é autoria do artigo
        if (p.temFlag("cap7_questionou_autoria")) {
            renderizarEpilogo("ORIENTADOR",
                    "O artigo saiu. Com o nome dele primeiro. Você foi segunda " +
                            "autora. Ele nunca mais te chamou pra outro projeto.");
        } else if (p.temFlag("cap7_aceitou_autoria")) {
            renderizarEpilogo("ORIENTADOR",
                    "O artigo saiu. Com o nome dele primeiro. Ele te chamou pra " +
                            "mais dois projetos. Você aceitou os dois.");
        }

        if (p.temFlag("rivalidade_sistema")) {
            renderizarEpilogo("DANDARA",
                    "Dandara passou num concurso. Postou no LinkedIn, ganhou 200 " +
                            "curtidas, e depois sumiu por três meses. Você acha que ela " +
                            "tá bem.");
        }

        if (p.temFlag("cap3_colou_de_fato") || p.temFlag("bia_ainda_vende")) {
            renderizarEpilogo("BIA",
                    "Bia ainda vende resposta antiga. Só que agora é num site " +
                            "próprio. Ela te convidou pra ser sócia. Você ainda tá pensando.");
        } else if (p.temFlag("hab_palavra_honesta")) {
            renderizarEpilogo("BIA",
                    "Bia parou de vender resposta. Ninguém sabe por quê. Você " +
                            "acha que foi você. Ela tá fazendo mestrado agora.");
        } else {
            renderizarEpilogo("BIA",
                    "Você não sabe o que aconteceu com a Bia. Ela sumiu depois do " +
                            "terceiro semestre. Ninguém do curso fala dela.");
        }

        if (p.getConfianca() >= 7 && p.getVidaSocial() >= 6) {
            renderizarEpilogo("QUEM VOCÊ VIROU",
                    "Você sai da entrevista e alguém te liga. Não é a empresa. " +
                            "É um amigo. Você atende. Você ri. Você não lembra a última " +
                            "vez que riu no telefone.");
        } else if (p.getConfianca() <= 2) {
            renderizarEpilogo("QUEM VOCÊ VIROU",
                    "Você sai da entrevista e não tem ninguém pra ligar. Você " +
                            "abre o celular, olha a lista de contatos, fecha. Você vai " +
                            "pra casa sozinha.");
        }

        renderizarEpilogo("ZÉ DO MÓDULO 8",
                "O Zé continua lá. O bar tá maior agora. Ele te reconheceu quando " +
                        "você voltou no semestre passado e não cobrou a cerveja.");

        System.out.println(Cores.CIANO + "[ Pressione ENTER para encerrar. ]" + Cores.RESET);
        Entrada.lerLinha();
    }

    public void mostrarChuteDoSistema(Protagonista p) {
        String linhaDoChute;

        if (p.temFlag("sistema_chutou_ic")) {
            linhaDoChute = "Perfil compatível: IC. Iniciação Científica.";
        } else if (p.temFlag("sistema_chutou_dtec")) {
            linhaDoChute = "Perfil compatível: DTEC. Monitoria.";
        } else {
            linhaDoChute = "Perfil atípico. Sem categoria definida.";
        }

        System.out.println();
        System.out.println(Cores.AMARELO + "  ╭─────────────────────────────────────╮" + Cores.RESET);
        System.out.println(Cores.AMARELO + "  │  SISTEMA ACADÊMICO — VEREDITO       │" + Cores.RESET);
        System.out.println(Cores.AMARELO + "  ╰─────────────────────────────────────╯" + Cores.RESET);
        System.out.println();

        System.out.println(Cores.CINZA + "  'Processamento concluído.'" + Cores.RESET);
        System.out.println();
        System.out.println(Cores.AMARELO + "  " + linhaDoChute + Cores.RESET);
        System.out.println();

        System.out.println(Cores.CINZA + "  'Recomendação baseada em padrões históricos.'" + Cores.RESET);
        System.out.println(Cores.CINZA + "  'Ignore se preferir. O sistema não decide por você.'" + Cores.RESET);
        System.out.println(Cores.CINZA + "  'Ainda.'" + Cores.RESET);
        System.out.println();

        System.out.println(Cores.CIANO + "[ ENTER para continuar ]" + Cores.RESET);
        Entrada.lerLinha();
    }
    private void renderizarEpilogo(String titulo, String texto) {
        System.out.println(Cores.CIANO + "┌" + "─".repeat(LARGURA) + "┐" + Cores.RESET);
        imprimirLinha(titulo, Cores.AMARELO);
        System.out.println(Cores.CIANO + "├" + "─".repeat(LARGURA) + "┤" + Cores.RESET);

        for (String paragrafo : texto.split("\n")) {
            if (paragrafo.isBlank()) {
                imprimirLinha("", Cores.BRANCO);
                continue;
            }
            for (String linha : quebrarLinha(paragrafo, LARGURA - 4)) {
                imprimirLinha(linha, Cores.BRANCO);
            }
        }
        System.out.println(Cores.CIANO + "└" + "─".repeat(LARGURA) + "┘" + Cores.RESET);
        System.out.println();
    }


    public void exibirEscolhas(List<Escolha> disponiveis) {
        System.out.println();
        System.out.println(Cores.CIANO + "╔" + "═".repeat(LARGURA) + "╗" + Cores.RESET);
        imprimirLinha("O QUE VOCÊ VAI FAZER?", Cores.VERDE);
        System.out.println(Cores.CIANO + "╠" + "═".repeat(LARGURA) + "╣" + Cores.RESET);

        for (int i = 0; i < disponiveis.size(); i++) {
            String escolha = "[" + (i + 1) + "] " + disponiveis.get(i).getTextoAlternativa();
            for (String linhaQuebrada : quebrarLinha(escolha, LARGURA - 4)) {
                imprimirLinha(linhaQuebrada, Cores.VERDE);
            }
        }

        System.out.println(Cores.CIANO + "╚" + "═".repeat(LARGURA) + "╝" + Cores.RESET);
        System.out.println("0 - Menu principal");
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println();
        System.out.println(Cores.AMARELO + mensagem + Cores.RESET);
    }

    private String corPorFalante(String personagemFalando) {
        if (personagemFalando.equalsIgnoreCase("Narrador")) return Cores.BRANCO;
        if (personagemFalando.equalsIgnoreCase("Sistema"))  return Cores.AMARELO;
        return Cores.MAGENTA;
    }

    private void imprimirLinha(String texto, String cor) {
        int espacos = Math.max(0, LARGURA - texto.length() - 2);
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