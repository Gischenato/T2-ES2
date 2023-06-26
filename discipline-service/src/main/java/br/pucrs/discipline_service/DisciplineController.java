package br.pucrs.discipline_service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pucrs.discipline_service.errors.DisciplineAlreadyRegisteredException;
import br.pucrs.discipline_service.errors.DisciplineNotFoundException;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/discipline")
public class DisciplineController {
    
    private final DisciplineService service;

	@PostMapping("/register")
	public ResponseEntity<String> registerStudent(@RequestBody Discipline discipline) {
        try {
            service.registerDiscipline(discipline);
            return ResponseEntity.status(HttpStatus.CREATED).body("Discipline registered");
        } catch (DisciplineAlreadyRegisteredException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
	}

    @GetMapping("/all")
	public List<Discipline> getAllStudents() {
		return this.service.findAll();
	}

    @GetMapping("/find/{classCode}/{turma}")
    public ResponseEntity<Discipline> getDisciplineByClassCodeAndTurma(@PathVariable String classCode, @PathVariable String turma) {
        try {
            Discipline discipline = service.findByClassCodeAndTurma(classCode, turma);
            return ResponseEntity.status(HttpStatus.OK).body(discipline);
        } catch (DisciplineNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/find/bulk")
    public List<Discipline> getAllDisciplinesById(@RequestBody List<ClassCodeAndTurmaDTO> classCodesAndTurmas) {
        return this.service.findAllByClassCodeAndTurma(classCodesAndTurmas);
    }
}
