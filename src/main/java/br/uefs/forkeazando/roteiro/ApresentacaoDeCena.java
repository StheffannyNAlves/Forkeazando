package br.uefs.forkeazando.roteiro;

/**
 * O que a view deve mostrar além do texto de uma cena. Só chaves (texto puro):
 * a lógica do jogo decide QUAL imagem e QUEM fala; como desenhar é com a view.
 *
 * @param ilustracao chave da ilustração de fundo (null = não muda a atual)
 * @param falante    chave do retrato de quem fala (null = sem retrato)
 * @param expressao  expressão do retrato (null = neutra)
 */
public record ApresentacaoDeCena(String ilustracao, String falante, String expressao) {

    public static ApresentacaoDeCena soIlustracao(String ilustracao) {
        return new ApresentacaoDeCena(ilustracao, null, null);
    }

    public static ApresentacaoDeCena comRetrato(String ilustracao, String falante, String expressao) {
        return new ApresentacaoDeCena(ilustracao, falante, expressao);
    }
}