package br.uefs.forkeazando.view.pixel;

/** Utilitários de cor (RGB empacotado em int, 0xRRGGBB) usados pela pixel art. */
public final class Cor {

    private Cor() {}

    public static int rgb(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    /** Mistura a→b; f=0 devolve a, f=1 devolve b. */
    public static int mix(int a, int b, double f) {
        int r = (int) Math.round(((a >> 16) & 255) * (1 - f) + ((b >> 16) & 255) * f);
        int g = (int) Math.round(((a >> 8) & 255) * (1 - f) + ((b >> 8) & 255) * f);
        int bl = (int) Math.round((a & 255) * (1 - f) + (b & 255) * f);
        return rgb(r, g, bl);
    }

    /** Degradê por faixas: ys[i] é a linha onde começa a cor cs[i]. */
    public static int gradiente(int y, int[] ys, int[][] cs) {
        for (int i = 0; i < ys.length - 1; i++) {
            if (y <= ys[i + 1]) {
                double f = (double) (y - ys[i]) / (ys[i + 1] - ys[i]);
                int[] a = cs[i];
                int[] b = cs[i + 1];
                return mix(rgb(a[0], a[1], a[2]), rgb(b[0], b[1], b[2]), f);
            }
        }
        int[] u = cs[cs.length - 1];
        return rgb(u[0], u[1], u[2]);
    }
}