package br.pucrs.matricula_service.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.pucrs.matricula_service.models.Student;
import feign.FeignException;

@FeignClient(name = "student-service", url = "http://localhost:8081/student")
public interface StudentClient {

    @GetMapping("/find/{registrationNumber}")
    Student getStudentByRegistrationNumber(@PathVariable String registrationNumber) throws FeignException;

    @PostMapping("/find/bulk")
    List<Student> getStudentsById(@RequestBody List<String> ids) throws FeignException;
}
