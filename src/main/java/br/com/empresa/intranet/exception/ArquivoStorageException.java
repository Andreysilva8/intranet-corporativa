package br.com.empresa.intranet.exception;

public class ArquivoStorageException extends RuntimeException {

    public ArquivoStorageException(String mensagem) {
        super(mensagem);
    }

    public ArquivoStorageException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
