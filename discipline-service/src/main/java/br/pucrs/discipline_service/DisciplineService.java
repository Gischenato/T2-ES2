package br.pucrs.discipline_service;
import java.util.LinkedList;
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

    public Discipline findByClassCodeAndTurma(String classCode, String turma) throws DisciplineNotFoundException {
        Discipline discipline = repository.findByClassCodeAndTurma(classCode, turma)
        .orElseThrow(() -> new DisciplineNotFoundException("Discipline not found"));
        return discipline;
    }

    public List<Discipline> findAllByClassCodeAndTurma(List<ClassCodeAndTurmaDTO> classCodesAndTurmas) {
        List<Discipline> disciplines = new LinkedList<>();
        for (ClassCodeAndTurmaDTO classCodeAndTurma : classCodesAndTurmas) {
            String classCode = classCodeAndTurma.getClassCode();
            String turma = classCodeAndTurma.getTurma();
            Discipline discipline = repository.findByClassCodeAndTurma(classCode, turma)
            .orElse(null);
            if (discipline == null) continue;
            disciplines.add(discipline);
        }
        return disciplines;
    }

    public void registerDiscipline(Discipline discipline) {
        List<Discipline> disciplines = repository.findByClassCode(discipline.getClassCode());
        if (disciplines.size() > 0) {
            String name = disciplines.get(0).getName();
            if (!name.equalsIgnoreCase(discipline.getName())) {
                String error = "Classcode " + discipline.getClassCode() + " already registered with name " + name;
                throw new DisciplineAlreadyRegisteredException(error);
            }
        }

        for (Discipline d : disciplines) {
            if (d.getTurma().equals(discipline.getTurma())) {
                throw new DisciplineAlreadyRegisteredException("Discipline already registered");
            }
        }
        repository.save(discipline);
    }

}