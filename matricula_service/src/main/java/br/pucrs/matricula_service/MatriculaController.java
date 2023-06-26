package br.pucrs.matricula_service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pucrs.matricula_service.errors.StudentNotFoundException;
import br.pucrs.matricula_service.models.Student;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/matricula")
public class MatriculaController {
    
    private final MatriculaService service;

	@PostMapping("/register")
	public ResponseEntity<String> registerMatricula (@RequestBody Matricula matricula) {
        try {
            service.registerMatricula(matricula);;
            return ResponseEntity.status(HttpStatus.CREATED).body("Discipline registered");
        } catch (StudentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
	}

    @GetMapping("/findStudent/{registrationNumber}")
    public Student getStudentByRegistrationNumber(@PathVariable String registrationNumber) {
        Student student = service.getStudentByRegistrationNumber(registrationNumber);
        return student;
    }

    // @GetMapping("/all")
	// public List<Discipline> getAllStudents() {
	// 	return this.service.findAll();
	// }
}
