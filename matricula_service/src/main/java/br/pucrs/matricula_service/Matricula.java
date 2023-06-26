package br.pucrs.matricula_service;

import br.pucrs.matricula_service.models.ClassCodeAndTurmaDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(nullable = false)
    String studentRegistrationNumber;

    @Column(nullable = false)
    String classCode;;

    @Column(nullable = false)
    String turma;

    public ClassCodeAndTurmaDTO getClassCodeAndTurmaDTO() {
        return new ClassCodeAndTurmaDTO(classCode, turma);
    }
}