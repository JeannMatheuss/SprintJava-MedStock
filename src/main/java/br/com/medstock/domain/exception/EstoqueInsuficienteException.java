package br.com.medstock.domain.exception;

public class EstoqueInsuficienteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EstoqueInsuficienteException(String message) {
        super(message);
    }
}