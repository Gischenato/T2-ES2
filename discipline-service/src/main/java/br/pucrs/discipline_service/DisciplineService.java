package br.pucrs.discipline_service;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.pucrs.discipline_service.errors.DisciplineAlreadyRegisteredException;
import br.pucrs.discipline_service.errors.DisciplineNotFoundException;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class DisciplineService {
    
    private final DisciplineRepository repository;

    public Discipline findById(long id) {
        Discipline discipline = repository.findById(id)
        .orElseThrow(() -> new DisciplineNotFoundException("Discipline not found"));
        return discipline;
    }

    public List<Discipline> findAll() {
        return repository.findAll();
    }

    public void registerDiscipline(Discipline discipline) {
        if (repository.existsById(discipline.getId())) {
            throw new DisciplineAlreadyRegisteredException("Discipline already registered");
        }
        repository.save(discipline);
    }

}