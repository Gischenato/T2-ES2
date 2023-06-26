package br.pucrs.containers.errors;

public class StudentAlreadyRegisteredException extends RuntimeException {
    public StudentAlreadyRegisteredException(String message) {
        super(message);
    }
}