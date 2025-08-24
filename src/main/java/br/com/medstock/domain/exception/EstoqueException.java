package br.com.medstock.domain.exception;

public class EstoqueException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EstoqueException(String message) {
        super(message);
    }
}