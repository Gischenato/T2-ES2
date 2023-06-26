package br.pucrs.discipline_service.errors;

public class DisciplineAlreadyRegisteredException extends RuntimeException {
    public DisciplineAlreadyRegisteredException(String message) {
        super(message);
    }
}