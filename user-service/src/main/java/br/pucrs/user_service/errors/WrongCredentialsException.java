package br.pucrs.user_service.errors;

public class WrongCredentialsException extends RuntimeException{
    public WrongCredentialsException(String message) {
        super(message);
    }
}
