package br.pucrs.matricula_service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.pucrs.matricula_service.clients.DisciplineClient;
import br.pucrs.matricula_service.clients.StudentClient;
import br.pucrs.matricula_service.errors.AlreadyRegisteredException;
import br.pucrs.matricula_service.errors.NotFoundException;
import br.pucrs.matricula_service.models.ClassCodeAndTurmaDTO;
import br.pucrs.matricula_service.models.Discipline;
import br.pucrs.matricula_service.models.Student;
import feign.FeignException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MatriculaService {
    
    private final MatriculaRepository repository;
    private final StudentClient studentClient;
    private final DisciplineClient disciplineClient;

    private Student getStudentByRegistrationNumber(String registrationNumber) throws NotFoundException {
        try {
            return studentClient.getStudentByRegistrationNumber(registrationNumber);
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new NotFoundException("Student not found");
            }
            return null;
        }
    }

    private List<Student> getStudentsById(List<String> ids) throws NotFoundException {
        try {
            return studentClient.getStudentsById(ids);
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new NotFoundException("Student not found");
            } else if (e.status() == 400) {
                throw new NotFoundException("Invalid student id");
            } else {
                throw new NotFoundException(e.getMessage());
            }
        }
    }

    private Discipline getDisciplineByClassCodeAndTurma(String classCode, String turma) throws NotFoundException {
        try {
            return disciplineClient.getDisciplineByClassCodeAndTurma(classCode, turma);
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new NotFoundException("Discipline not found");
            }
            return null;
        }
    }

    private List<Discipline> getDisciplinesByClassCodeAndTurma(List<ClassCodeAndTurmaDTO> codes) throws NotFoundException {
        try {
            return disciplineClient.getAllDisciplinesByClassCodeAndTurma(codes);
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new NotFoundException("Discipline not found");
            }
            return null;
        }
    }   

    public List<Student> findStudentsInClass(String classCode, String turma) {
        getDisciplineByClassCodeAndTurma(classCode, turma);
        List<Matricula> matriculas = repository.findByClassCodeAndTurma(classCode, turma);
        if (matriculas.size() == 0) return List.of();
        List<String> ids = matriculas.stream().map(Matricula::getStudentRegistrationNumber).toList();
        System.out.println(ids.toString());
        List<Student> students = getStudentsById(ids);
        return students;
    }

    public List<Discipline> findStudentClasses(String registrationNumber) {
        getStudentByRegistrationNumber(registrationNumber);

        List<Matricula> matriculas = repository.findByStudentRegistrationNumber(registrationNumber);
        if (matriculas.size() == 0) return List.of();
        List<ClassCodeAndTurmaDTO> codes = matriculas.stream().map(Matricula::getClassCodeAndTurmaDTO).toList();
        List<Discipline> disciplines = getDisciplinesByClassCodeAndTurma(codes);
        return disciplines;
    }

    public Matricula findById(long id) {
        Matricula matricula = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Matricula not found"));
        return matricula;
    }


    public void registerMatricula(Matricula matricula) {
        String registrationNumber = matricula.getStudentRegistrationNumber();
        String classCode = matricula.getClassCode();
        if (repository.existsByStudentRegistrationNumberAndClassCode(registrationNumber, classCode))
            throw new AlreadyRegisteredException("Student already registered in this discipline");
        getStudentByRegistrationNumber(registrationNumber);
        getDisciplineByClassCodeAndTurma(classCode, matricula.getTurma());
        
        repository.save(matricula);
    }
}