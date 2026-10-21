package br.uefs.forkeazando.view;

import br.uefs.forkeazando.model.Conquista;
import br.uefs.forkeazando.model.Final;
import br.uefs.forkeazando.model.Progresso;
import br.uefs.forkeazando.view.pixel.ConquistasPainel;


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
        System.out.println();

        for (String linha : ConquistasPainel.linhas(
                progresso,
                data -> data.format(FORMATO)
        )) {
            System.out.println(linha);
        }

        System.out.println();
        System.out.print("Pressione ENTER para voltar...");
        Entrada.lerLinha();
    }

}
