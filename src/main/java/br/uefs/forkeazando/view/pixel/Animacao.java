package br.uefs.forkeazando.view.pixel;



import java.util.function.IntFunction;

/**
 * Animação de entrada de uma ilustração: N quadros desenhados por uma função
 * pura (quadro -> Tela). O último quadro é a arte final estática, então quem
 * não quer (ou não pode) animar só desenha o último.
 */
public record Animacao(int total, long intervaloMs, IntFunction<Tela> quadro) {

    public Animacao {
        if (total < 1) throw new IllegalArgumentException("Animação precisa de ao menos 1 quadro");
        if (intervaloMs < 0) throw new IllegalArgumentException("Intervalo negativo");
    }

    public Tela desenhar(int indice) {
        if (indice < 0 || indice >= total) {
            throw new IndexOutOfBoundsException("Quadro " + indice + " de " + total);
        }
        return quadro.apply(indice);
    }

    public String[] renderizar(int indice) {
        return desenhar(indice).renderizar();
    }
}