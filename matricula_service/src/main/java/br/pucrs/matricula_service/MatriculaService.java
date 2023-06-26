package br.pucrs.matricula_service;

import org.springframework.stereotype.Repository;

import br.pucrs.matricula_service.clients.StudentClient;
import br.pucrs.matricula_service.errors.StudentAlreadyRegisteredException;
import br.pucrs.matricula_service.errors.StudentNotFoundException;
import br.pucrs.matricula_service.models.Student;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class MatriculaService {
    
    private final MatriculaRepository repository;
    private final StudentClient studentClient;

    public Matricula findById(long id) {
        Matricula matricula = repository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException("Matricula not found"));
        return matricula;
    }

    public Student getStudentByRegistrationNumber(String registrationNumber) {
        Student student = studentClient.getStudentByRegistrationNumber(registrationNumber);
        return student;
    }

    // public List<Discipline> findAll() {
    //     return repository.findAll();
    // }

    public void registerMatricula(Matricula matricula) {
        if (repository.existsById(matricula.getId()))
            throw new StudentAlreadyRegisteredException("Student already registered");
        repository.save(matricula);
    }
}