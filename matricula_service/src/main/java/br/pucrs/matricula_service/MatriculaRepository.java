package br.pucrs.matricula_service;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    public boolean existsByStudentRegistrationNumberAndClassCode(String studentRegistrationNumber, String classCode);
    public List<Matricula> findByClassCodeAndTurma(String classCode, String turma);
    public List<Matricula> findByStudentRegistrationNumber(String studentRegistrationNumber);
}
