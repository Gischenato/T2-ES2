package br.pucrs.discipline_service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pucrs.discipline_service.errors.DisciplineAlreadyRegisteredException;
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
}
