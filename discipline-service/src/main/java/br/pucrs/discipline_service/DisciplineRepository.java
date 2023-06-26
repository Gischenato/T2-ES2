package br.pucrs.discipline_service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
     public List<Discipline> findByClassCode(String classCode);
}
