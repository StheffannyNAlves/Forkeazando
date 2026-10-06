package br.uefs.forkeazando.view;

import java.util.concurrent.TimeUnit;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Entrada {

    static final Terminal TERMINAL;
    private static final Reader READER;
    private static final PrintWriter WRITER;

    private static volatile boolean pausado = false;

    public static final BlockingQueue<String> TECLAS =
            new LinkedBlockingQueue<>();


    public static Terminal terminal(){
        return TERMINAL;
    }
    static {
        try {
            TERMINAL = TerminalBuilder.builder().system(true).build();
            TERMINAL.enterRawMode();

            READER = TERMINAL.reader();
            WRITER = TERMINAL.writer();

            iniciarLeituraTeclado();

        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Entrada() {}

    private static void iniciarLeituraTeclado() {
        Thread leitor = new Thread(() -> {
            try {
                while (true) {
                    int c = READER.read();

                    if (c == 27) {              // ESC — pode ser seta ou EXIT sozinho
                        int next = READER.read();
                        if (next == '[') {
                            int dir = READER.read();
                            switch (dir) {
                                case 'A' -> TECLAS.put("UP");
                                case 'B' -> TECLAS.put("DOWN");
                                case 'C' -> TECLAS.put("RIGHT");
                                case 'D' -> TECLAS.put("LEFT");
                            }
                        } else {
                            TECLAS.put("EXIT"); // ESC sozinho
                        }
                    } else if (c == '\r' || c == '\n') {
                        TECLAS.put("ENTER");
                    } else if (c == 127 || c == 8) {
                        TECLAS.put("BACKSPACE");
                    } else if (c == 3) {        // Ctrl+C
                        TECLAS.put("EXIT");

                    } else if (c == 16) {            // Ctrl+P
                        alternarPause();
                    } else if (c >= 32) {
                        TECLAS.put(String.valueOf((char) c));
                    }
                }
            } catch (IOException | InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        leitor.setDaemon(true);
        leitor.start();
    }

    public static boolean estaPausado() {
        return pausado;
    }

    public static void alternarPause() {
        pausado = !pausado;
    }

    public static void aguardarPause() {
        while (pausado) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public static String lerLinha() {
        StringBuilder sb = new StringBuilder();
        try {
            while (true) {
                aguardarPause();

                String tecla = TECLAS.poll(100, TimeUnit.MILLISECONDS);
                if (tecla == null) continue;

                if ("ENTER".equals(tecla)) {
                    WRITER.println();
                    WRITER.flush();
                    break;
                }

                if ("BACKSPACE".equals(tecla)) {
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                        WRITER.print("\b \b");
                        WRITER.flush();
                    }
                    continue;
                }

                if (tecla.length() == 1 && tecla.charAt(0) >= 32) {
                    sb.append(tecla);
                    WRITER.print(tecla);
                    WRITER.flush();
                }
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        return sb.toString().trim();
    }

    public static int lerInteiro(int min, int max) {
        while (true) {
            aguardarPause();

            try {
                int v = Integer.parseInt(lerLinha());

                if (v >= min && v <= max) {
                    return v;
                }

            } catch (NumberFormatException ignored) {}

            WRITER.print("Digite entre " + min + " e " + max + ": ");
            WRITER.flush();
        }
    }
}