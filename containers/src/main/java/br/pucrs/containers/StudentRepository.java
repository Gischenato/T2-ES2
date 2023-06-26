package br.pucrs.containers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    public Optional<Student> findByDocumentNumber(String documentNumber);
    List<Student> findByNameContainingIgnoreCase(String name);
}
