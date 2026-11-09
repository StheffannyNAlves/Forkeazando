package br.uefs.forkeazando.excecao;

public class DadosInvalidosException extends PersistenciaException {

    public DadosInvalidosException(String mensagem) {
        super(mensagem);
    }

    public DadosInvalidosException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}