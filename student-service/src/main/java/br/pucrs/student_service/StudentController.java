package br.pucrs.student_service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pucrs.student_service.errors.StudentAlreadyRegisteredException;
import br.pucrs.student_service.errors.StudentNotFoundException;
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

    @PostMapping("/find/bulk")
    public List<Student> getStudentsById(@RequestBody List<Long> ids) {
        return this.service.findAllById(ids);
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

    @GetMapping("/find/document/{documentNumber}")
	public ResponseEntity<Student> getStudentByDocumentNumber(@PathVariable String documentNumber) {
		try {
            Student student = this.service.findByDocumentNumber(documentNumber);
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

}
