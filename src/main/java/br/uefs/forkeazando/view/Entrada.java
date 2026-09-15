package br.uefs.forkeazando.view;

import java.util.Scanner;

public class Entrada {
    private static final Scanner scanner = new Scanner(System.in);
    private Entrada() {}

    public static String lerLinha() {
        return scanner.nextLine().trim();
    }

    public static int lerInteiro(int min, int max) {
        while (true) {
            try {
                int valor = Integer.parseInt(lerLinha());
                if (valor >= min && valor <= max) return valor;
                System.out.printf("Digite um número entre %d e %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Tente novamente: ");
            }
        }
    }
}