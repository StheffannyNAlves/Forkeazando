package br.uefs.forkeazando.excecao;


public class SaveException extends PersistenciaException {
    public SaveException(String mensagem){
        super(mensagem);
    }
    public SaveException(String mensagem, Throwable causa){
        super(mensagem, causa);
    }
}
