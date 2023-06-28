package br.pucrs.matricula_service.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.pucrs.matricula_service.models.ClassCodeAndTurmaDTO;
import br.pucrs.matricula_service.models.Discipline;

@FeignClient(name = "discipline-service", url = "http://localhost:8082/discipline")
public interface DisciplineClient {

    @GetMapping("/find/{classCode}/{turma}")
    Discipline getDisciplineByClassCodeAndTurma(@PathVariable String classCode, @PathVariable String turma);

    @PostMapping("/find/bulk")
    List<Discipline> getAllDisciplinesByClassCodeAndTurma(@RequestBody List<ClassCodeAndTurmaDTO> classCodesAndTurmas);
}
