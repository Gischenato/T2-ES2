package br.pucrs.containers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pucrs.containers.errors.StudentAlreadyRegisteredException;
import br.pucrs.containers.errors.StudentNotFoundException;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/student")
public class StudentController {
    
    private final StudentService service;

	@PostMapping("/register")
	public ResponseEntity<String> registerStudent(@RequestBody Student student) {
		try {
            service.registerStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body("Student registered");
        } catch (StudentAlreadyRegisteredException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
	}

    @GetMapping("/all")
	public List<Student> getAllStudents() {
		return this.service.findAll();
	}

	@GetMapping("/find/{registrationNumber}")
	public ResponseEntity<Student> getStudentByRegistrationNumber(@PathVariable String registrationNumber) {
		try {
            long regNmb = Long.parseLong(registrationNumber);
            Student student = this.service.findById(regNmb);
            return ResponseEntity.ok(student);
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
	}

	@GetMapping("/findByName/{name}")
	public ResponseEntity<List<Student>> getStudentByName(@PathVariable String name) {
		List<Student> students = service.findByNameContaining(name);
		if (students.isEmpty()) { return ResponseEntity.notFound().build(); }
		return ResponseEntity.ok(students);
	}

    @GetMapping("/getClasses/{registrationNumber}")
    public ResponseEntity<List<StudentDisciplinesDTO>> getStudentClasses(@PathVariable String registrationNumber) {
        try {
            long regNmb = Long.parseLong(registrationNumber);
            List<StudentDisciplinesDTO> disciplinesList = service.getStudentDisciplines(regNmb);
            return ResponseEntity.ok(disciplinesList);
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
