package br.uefs.forkeazando.view;

import br.uefs.forkeazando.model.Conquista;
import br.uefs.forkeazando.model.Final;
import br.uefs.forkeazando.model.Progresso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProgressoView {

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    public void mostrarProgresso(Progresso progresso) {
        System.out.println();
        System.out.println(Cores.CIANO + "========== MEU PROGRESSO ==========" + Cores.RESET);

        int desbloqueados = progresso.finaisAlcancados().size();
        System.out.println("Finais desbloqueados: " + desbloqueados
                + "/" + Final.values().length);
        System.out.println();

        for (Final f : Final.values()) {
            if (progresso.alcancou(f)) {
                String data = progresso.quandoAlcancou(f)
                        .map(FORMATO::format)
                        .orElse("Data indisponível");

                System.out.println(Cores.VERDE + "[✓] " + f.getTitulo()
                        + " — Desbloqueado em " + data + Cores.RESET);
            } else {
                System.out.println(Cores.AMARELO + "[ ] " + f.getTitulo()
                        + " — Bloqueado" + Cores.RESET);
            }
        }

        System.out.println();
        System.out.print("Pressione ENTER para voltar...");
        Entrada.lerLinha();
    }

    public void mostrarConquistas(Progresso progresso) {
        Conquista[] catalogo = Conquista.values();

        long desbloqueadas = java.util.Arrays.stream(catalogo)
                .filter(progresso::possui)
                .count();

        int total = catalogo.length;
        int porcentagem = total == 0
                ? 0
                : (int) (desbloqueadas * 100 / total);

        System.out.println();
        System.out.println("================================================");
        System.out.println("              FORKEAZANDO");
        System.out.println("               CONQUISTAS");
        System.out.println("================================================");
        System.out.printf("  Desbloqueadas: %d/%d (%d%%)%n",
                desbloqueadas, total, porcentagem);
        System.out.println();

        for (Conquista conquista : catalogo) {
            if (progresso.possui(conquista)) {
                String data = progresso.quandoDesbloqueou(conquista)
                        .map(FORMATO::format)
                        .orElse("Data indisponível");

                System.out.println("  [★] " + conquista.getTitulo());
                System.out.println("      " + conquista.getDescricao());
                System.out.println("      Desbloqueada em: " + data);
            } else {
                System.out.println("  [🔒] ???");
                System.out.println("      Conquista ainda não descoberta.");
            }

            System.out.println();
        }

        System.out.println("================================================");
        System.out.print("Pressione ENTER para voltar...");
        Entrada.lerLinha();
    }

}
