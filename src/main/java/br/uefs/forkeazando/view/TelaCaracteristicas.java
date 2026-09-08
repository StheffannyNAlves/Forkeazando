package br.uefs.forkeazando.view;

import java.util.Scanner;

public class TelaCaracteristicas {
    private final Scanner scanner = new Scanner(System.in);

    public boolean perguntarPersonalidade() {
        System.out.println(Cores.CIANO + "Personalidade:" + Cores.RESET);
        System.out.println("1. Perfeccionista");
        System.out.println("2. Relaxado(a)");
        return lerEscolhaBinaria();
    }

    public boolean perguntarSociavel() {
        System.out.println(Cores.CIANO + "Sociável?" + Cores.RESET);
        System.out.println("1. Sim");
        System.out.println("2. Não");
        return lerEscolhaBinaria();
    }

    public boolean perguntarTipoDeEstudo() {
        System.out.println(Cores.CIANO + "Tipo de estudo:" + Cores.RESET);
        System.out.println("1. Teórico(a)");
        System.out.println("2. Prático(a)");
        return lerEscolhaBinaria();
    }

    private boolean lerEscolhaBinaria() {
        System.out.print(Cores.AMARELO + "Escolha: " + Cores.RESET);
        return scanner.nextLine().trim().equals("1");
    }
}