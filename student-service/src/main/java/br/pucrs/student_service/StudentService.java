package br.pucrs.student_service;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.pucrs.student_service.errors.StudentAlreadyRegisteredException;
import br.pucrs.student_service.errors.StudentNotFoundException;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class StudentService {
    
    private final StudentRepository repository;

    public Student findById(long id) {
        Student student = repository.findById(id).orElse(null);
        if (student == null) {
            throw new StudentNotFoundException("Student not found");
        }
        return student;
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public List<Student> findByNameContaining(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public void registerStudent(Student student) {
        if(repository.findByDocumentNumber(student.getDocumentNumber()).isPresent()){
            throw new StudentAlreadyRegisteredException("Student already registered");
        }
        repository.save(student);
    }

    public List<StudentDisciplinesDTO> getStudentDisciplines(long id) {
        Student student = findById(id);
        List<Discipline> disciplines = student.getDisciplines();
        LinkedList<StudentDisciplinesDTO> disciplinesList = new LinkedList<StudentDisciplinesDTO>();

        for (Discipline d : disciplines) {
            disciplinesList.add(new StudentDisciplinesDTO(d));
        }

        return disciplinesList;
    }
}