package br.pucrs.matricula_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.pucrs.matricula_service.models.Student;

@FeignClient(name = "student-service", url = "http://localhost:8081/student")
public interface StudentClient {

    @GetMapping("/find/{registrationNumber}")
    Student getStudentByRegistrationNumber(@PathVariable String registrationNumber);
}
