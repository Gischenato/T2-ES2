package br.pucrs.matricula_service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pucrs.matricula_service.models.Discipline;
import br.pucrs.matricula_service.models.Student;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/matricula")
public class MatriculaController {

    private final MatriculaService service;

	@PostMapping()
	public ResponseEntity<String> registerMatricula (@RequestBody Matricula matricula) {
        try {
            service.registerMatricula(matricula);;
            return ResponseEntity.status(HttpStatus.CREATED).body("Student matriculado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
	}

    @GetMapping("/class/{classCode}/{turma}")
    public ResponseEntity<Object> getClassStudents (@PathVariable String classCode, @PathVariable String turma) {
        try {
            List<Student> matriculas = service.findStudentsInClass(classCode, turma);
            return ResponseEntity.status(HttpStatus.OK).body(matriculas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/student/{registrationNumber}")
    public ResponseEntity<Object> getStudentClasses (@PathVariable String registrationNumber) {
        try {
            List<Discipline> matriculas = service.findStudentClasses(registrationNumber);
            return ResponseEntity.status(HttpStatus.OK).body(matriculas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}